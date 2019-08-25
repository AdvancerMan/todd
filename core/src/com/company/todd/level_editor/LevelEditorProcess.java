package com.company.todd.level_editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.company.todd.game.level.Level;
import com.company.todd.game.objs.static_objs.Platform;
import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

public class LevelEditorProcess extends GameProcess {
    protected Texture move, scaleUp, scaleDown;
    protected Rectangle moveRect, scaleUpRect, scaleDownRect;
    protected Platform platformNow;
    protected Vector2 firstTouchPos;
    protected Platform.Types platformTypes;

    private Vector2 lastTouchPos;
    private boolean scaleUpFlag, scaleDownFlag;
    private MoveType moveType;


    public LevelEditorProcess(ToddEthottGame game, MyScreen screen) {
        super(game, screen, new Level(game));
        addJustCreatedObjectsToProcess();

        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.FOREST);
        pixmap.fill();
        move = new Texture(pixmap);
        moveRect = new Rectangle(0, 0, 100, 100);

        pixmap.setColor(Color.YELLOW);
        pixmap.fill();
        scaleUp = new Texture(pixmap);
        scaleUpRect = new Rectangle(0, 100, 100, 100);

        pixmap.setColor(Color.RED);
        pixmap.fill();
        scaleDown = new Texture(pixmap);
        scaleDownRect = new Rectangle(0, 200, 100, 100);

        pixmap.dispose();

        platformNow = null;
        firstTouchPos = new Vector2();

        platformTypes = new Platform.Types(game);

        lastTouchPos = new Vector2();
        scaleDownFlag = false;
        scaleUpFlag = false;
        moveType = MoveType.NOT_MOVING;
    }

    public void update(float delta) {
        Vector2 touchPos = screen.getTouchPos();

        updateButtonsFlags(touchPos);

        if (scaleUpFlag || scaleDownFlag || moveType == MoveType.MOVING) {
            updateButtons(delta, touchPos);
        } else if (moveType == MoveType.NOT_MOVING) {
            updatePlatformPlacing(delta, touchPos);
        }
    }

    private void updatePlatformPlacing(float delta, Vector2 touchPos_) {
        Vector2 touchPos = new Vector2(touchPos_);
        screen.fromScreenToWorldCoord(touchPos);

        if (Gdx.input.isTouched()) {
            if (platformNow == null) {
                platformNow = new Platform(game, platformTypes.getPlatformType("grassPlatform"),
                        touchPos.x, touchPos.y, 0, 0);

                addObject(platformNow);
                addJustCreatedObjectsToProcess();
                platformNow.setGameProcess(this);
                firstTouchPos = touchPos;
            } else {
                platformNow.setSize(Math.abs(firstTouchPos.x - touchPos.x), Math.abs(firstTouchPos.y - touchPos.y));

                if (firstTouchPos.x >= touchPos.x && firstTouchPos.y <= touchPos.y) {
                    platformNow.setPosition(touchPos.x, firstTouchPos.y);
                } else if (firstTouchPos.x <= touchPos.x && firstTouchPos.y >= touchPos.y) {
                    platformNow.setPosition(firstTouchPos.x, touchPos.y);
                } else if (firstTouchPos.x >= touchPos.x && firstTouchPos.y >= touchPos.y) {
                    platformNow.setPosition(touchPos.x, touchPos.y);
                } else {
                    platformNow.setPosition(firstTouchPos.x, firstTouchPos.y);
                }
            }
        } else {
            platformNow = null;
        }
    }

    private void updateButtons(float delta, Vector2 touchPos) {
        if (moveType.equals(MoveType.MOVING) && Gdx.input.isTouched()) {
            if (Gdx.input.justTouched()) {
                lastTouchPos.set(touchPos);
            } else {
                float kx = screen.getCameraViewportWidth() / Gdx.graphics.getWidth();
                float ky = screen.getCameraViewportHeight() / Gdx.graphics.getHeight();

                screen.translateCamera(kx * (lastTouchPos.x - touchPos.x), ky * (lastTouchPos.y - touchPos.y));
                lastTouchPos.set(touchPos);
            }

            return;
        }

        if (scaleDownFlag && Gdx.input.isTouched()) {
            screen.changeCameraViewportSize(-60 * delta, -60 * delta);
        }

        if (scaleUpFlag && Gdx.input.isTouched()) {
            screen.changeCameraViewportSize(60 * delta, 60 * delta);
        }
    }

    private void checkTouchedButtons(Vector2 touchPos) {
        if (moveRect.contains(touchPos)) {
            if (moveType.equals(MoveType.MOVING)) {
                moveType = MoveType.MOVING_PRESSED;
            } else if (moveType.equals(MoveType.NOT_MOVING)) {
                moveType = MoveType.NOT_MOVING_PRESSED;
            }
        }

        if (scaleUpRect.contains(touchPos)) {
            scaleUpFlag = true;
        }

        if (scaleDownRect.contains(touchPos)) {
            scaleDownFlag = true;
        }
    }

    private void checkNotTouchedButtons(Vector2 touchPos) {
        if (!moveRect.contains(touchPos)) {
            if (moveType.equals(MoveType.MOVING_PRESSED)) {
                moveType = MoveType.NOT_MOVING;
            } else if (moveType.equals(MoveType.NOT_MOVING_PRESSED)) {
                moveType = MoveType.MOVING;
            }
        }

        if (!scaleUpRect.contains(touchPos)) {
            scaleUpFlag = false;
        }

        if (!scaleDownRect.contains(touchPos)) {
            scaleDownFlag = false;
        }
    }

    private void updateButtonsFlags(Vector2 touchPos) {
        if (Gdx.input.justTouched()) {
            checkTouchedButtons(touchPos);
        } else if (!Gdx.input.isTouched()) {
            checkNotTouchedButtons(touchPos);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        Vector2 tmpVec = new Vector2();
        float widthK = screen.getCameraViewportWidth() / (float)Gdx.graphics.getWidth();
        float heightK = screen.getCameraViewportHeight() / (float)Gdx.graphics.getHeight();

        tmpVec.set(moveRect.x, moveRect.y);
        tmpVec = screen.fromScreenToWorldCoord(tmpVec);
        batch.draw(move, tmpVec.x, tmpVec.y,
                moveRect.width * widthK, moveRect.height * heightK);

        tmpVec.set(scaleUpRect.x, scaleUpRect.y);
        tmpVec = screen.fromScreenToWorldCoord(tmpVec);
        batch.draw(scaleUp, tmpVec.x, tmpVec.y,
                scaleUpRect.width * widthK, scaleUpRect.height * heightK);

        tmpVec.set(scaleDownRect.x, scaleDownRect.y);
        tmpVec = screen.fromScreenToWorldCoord(tmpVec);
        batch.draw(scaleDown, tmpVec.x, tmpVec.y,
                scaleDownRect.width * widthK, scaleDownRect.height * heightK);
    }

    @Override
    public void dispose() {
        for (StaticObject object : staticObjects) {
            System.out.println(object.getBodyRect());
        }

        super.dispose();
        move.dispose();
        scaleDown.dispose();
        scaleUp.dispose();
    }

    private enum MoveType {
        NOT_MOVING, NOT_MOVING_PRESSED, MOVING, MOVING_PRESSED
    }
}
