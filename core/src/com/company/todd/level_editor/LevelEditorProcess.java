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
    }

    private boolean scaleUpFlag = false, scaleDownFlag = false;
    private byte moveFlag = 0;
    public void update(float delta) {
        if (scaleUpFlag || scaleDownFlag) {
            updateButtons(delta, new Vector2());
        } else if (Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            screen.toScreenCoord(touchPos);

            if (moveFlag > 0 || Gdx.input.justTouched() && intrWithButtons(touchPos)) {
                updateButtons(delta, touchPos);
                return;
            }

            screen.fromScreenToWorldCoord(touchPos);

            if (platformNow == null) {
                platformNow = new Platform(game, platformTypes.getPlatformType("grassPlatform"),

                        touchPos.x, touchPos.y, 0, 0);
                addObject(platformNow);
                addJustCreatedObjectsToProcess();
                platformNow.setGameProcess(this);
                firstTouchPos = touchPos;
            } else {
                platformNow.setSize(Math.abs(firstTouchPos.x - touchPos.x), Math.abs(firstTouchPos.y - touchPos.y));

                if (firstTouchPos.x >= touchPos.x &&  firstTouchPos.y <= touchPos.y) {
                    platformNow.setPosition(touchPos.x, firstTouchPos.y);
                } else if (firstTouchPos.x <= touchPos.x &&  firstTouchPos.y >= touchPos.y) {
                    platformNow.setPosition(firstTouchPos.x, touchPos.y);
                } else if (firstTouchPos.x >= touchPos.x &&  firstTouchPos.y >= touchPos.y) {
                    platformNow.setPosition(touchPos.x, touchPos.y);
                } else {
                    platformNow.setPosition(firstTouchPos.x, firstTouchPos.y);
                }
            }
        } else {
            if (platformNow != null) {
                platformNow = null;
            }
        }
    }

    private Vector2 moveTouchPos = new Vector2();
    private void updateButtons(float delta, Vector2 touchPos) {
        if (moveFlag > 0) {
            if (moveFlag > 1 && Gdx.input.justTouched() && moveRect.contains(touchPos)) {
                moveFlag = 0;
                return;
            } else if (Gdx.input.justTouched()) {
                moveTouchPos.set(touchPos);
            } else if (Gdx.input.isTouched()) {
                screen.translateCamera(moveTouchPos.x - touchPos.x, moveTouchPos.y - touchPos.y);
                moveTouchPos.set(touchPos);
            }
            moveFlag = 2;
        } else if (scaleDownFlag) {
            if (Gdx.input.isTouched()) {
                screen.changeCameraViewportSize(-1, -1);
            } else {
                scaleDownFlag = false;
            }
        } else if (scaleUpFlag) {
            if (Gdx.input.isTouched()) {
                screen.changeCameraViewportSize(1, 1);
            } else {
                scaleUpFlag = false;
            }
        }
    }

    private boolean intrWithButtons(Vector2 touchPos) {

        if (moveRect.contains(touchPos)) {
            moveFlag = 1;
        } else if (scaleUpRect.contains(touchPos)) {
            scaleUpFlag = true;
        } else if (scaleDownRect.contains(touchPos)) {
            scaleDownFlag = true;
        } else {
            return false;
        }

        return true;
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
}
