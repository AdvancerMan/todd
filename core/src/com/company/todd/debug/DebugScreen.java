package com.company.todd.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.company.todd.game.level.Level;
import com.company.todd.game.level.levels.arena.MainArenaLevel;
import com.company.todd.game.objs.static_objs.HalfCollidedPlatform;
import com.company.todd.game.objs.static_objs.Jumper;
import com.company.todd.game.objs.static_objs.PlatformWithUpperLayer;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

public class DebugScreen extends MyScreen {
    GameProcess gameProcess;
    PlatformWithUpperLayer.Types platformTypes;
    Box2DDebugRenderer debugRenderer;

    boolean pressedPlay;

    public DebugScreen(ToddEthottGame game) {
        super(game);

        pressedPlay = true;
        debugRenderer = new Box2DDebugRenderer(true, true, false, true, true, true);

        platformTypes = new PlatformWithUpperLayer.Types(game);

        Level level = new MainArenaLevel(game);

        float[][] pls = {  // x, y, width, height
                {100, 100, 100, 100},
                {200, 25, 500, 100},
                {700, 125, 500, 100},
                {123, 321, 123, 317},
                {10, 3, 500, 1},
                {10, 3, 500, 1},
                {-10, -10, 2000, 10},
                {-10, -10, 10, 1000},
                {1990, -10, 10, 1000},
                {-10, 990, 2000, 10},
        };

        /*
        for (int i = 0; i < pls.length; i++) {
            level.addObject(new PlatformWithUpperLayer(game,
                    platformTypes.getPlatformType("grassPlatform"),
                    pls[i][0], pls[i][1], pls[i][2], pls[i][3]));
        }
        */

        level.addObject(new HalfCollidedPlatform(game, null, 500, 350, 500, 50));
        level.addObject(new Jumper(game, null,10, 500, 400, 100, 20));


        gameProcess = new GameProcess(game, this, level);
    }

    @Override
    protected void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            pressedPlay = !pressedPlay;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.W) || pressedPlay) {
            super.update(delta);
            gameProcess.update(delta);
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        debugRenderer.render(gameProcess.getWorld(),
                getCameraProjectionMatrix().cpy().scl(1 / GameProcess.METERS_PER_PIX));

        batch.begin();

        game.mainFont.draw(batch, (int)(1f / Gdx.graphics.getDeltaTime()) + " fps",
                getCameraRect().x + 5, getCameraRect().y + getCameraRect().height - 10);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameProcess.resizeInputHandler(width, height);
    }

    @Override
    public void dispose() {
        gameProcess.dispose();
    }
}
