package com.company.todd.level_editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import com.company.todd.game.process.GameProcess;
import com.company.todd.game.static_objs.Platform;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.GameScreen;
import com.company.todd.texture.TextureRegionInfo;

// TODO LevelEditor extends GameProcess
public class LevelEditor extends ApplicationAdapter {  // TODO LevelEditor
    Array<Platform> platforms;
    TextureRegionInfo upperPlatform, downPlatform;
    Platform platformNow;
    ToddEthottGame game;
    GameProcess process;
    Rectangle screenRect;
    OrthographicCamera camera;
    Vector3 touchPos;
    Vector2 firstTouchPos;
    boolean created;

    @Override
    public void create() {
        game = new ToddEthottGame();
        game.create();

        GameScreen screen = new GameScreen(game);
        screenRect = screen.getCameraRect();
        process = new GameProcess(game, screen);

        upperPlatform = game.regionInfos.getRegionInfo("buttonClicked");
        downPlatform = game.regionInfos.getRegionInfo("buttonNotClicked");

        platforms = new Array<Platform>();
        platformNow = null;

        created = false;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.update();

        touchPos = new Vector3();
        firstTouchPos = new Vector2();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Очищаем экран
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);

        if (Gdx.input.justTouched()) {
            platformNow = new Platform(game, process, new Platform.PlatformType(upperPlatform, downPlatform),
                    touchPos.x, touchPos.y, 0, 0);
            firstTouchPos.set(touchPos.x, touchPos.y);
        }
        if (Gdx.input.isTouched() && platformNow != null) {
            created = false;
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
        } else {
            created = true;
        }

        if (created && platformNow != null) {
            platforms.add(platformNow);
            platformNow = null;
        }

        /*
        long startTime;
        if (ToddEthottGame.DEBUG) {
            startTime = System.currentTimeMillis();
        }
        */

        game.batch.begin();

        for (Platform pl : platforms) {
            pl.draw(game.batch, screenRect);
        }
        if (platformNow != null) {
            platformNow.draw(game.batch, screenRect);
        }

        if (ToddEthottGame.DEBUG) {
            game.mainFont.draw(game.batch, "" + (1f / Gdx.graphics.getDeltaTime()), 0, 400);
        }

        game.batch.end();
        /*
        if (ToddEthottGame.DEBUG) {
            System.out.println((double)(System.currentTimeMillis() - startTime) / 1000.);
            System.out.println(platforms.size);
        }
        */
    }

    public void dispose() {
        for (Platform pl : platforms) {
            pl.dispose();
        }
        if (platformNow != null) {
            platformNow.dispose();
        }
    }
}
