package com.company.todd.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.company.todd.game.level.Level;
import com.company.todd.game.objs.static_objs.interactive.Portal;
import com.company.todd.game.objs.static_objs.walkable.HalfCollidedPlatform;
import com.company.todd.game.objs.static_objs.interactive.Jumper;
import com.company.todd.game.objs.static_objs.walkable.PlatformWithUpperLayer;
import com.company.todd.game.objs.static_objs.walkable.ViscousPlatform;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

import static com.company.todd.game.process.GameProcess.toPix;

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

        Level level = new Level(game);

        float[][] pls = {  // x, y, width, height
                {0, -1f / 30, 1000f / 30, 1f / 30},
                {-1000f / 30, -1f / 30, 1000f / 30, 1f / 30},

                /*
                {100, 100, 100, 100},
                {200, 25, 500, 100},
                {700, 125, 500, 100},
                {123, 321, 123, 317},
                {10, 3, 500, 1},
                {10, 3, 500, 1},
                 */
        };


        for (int i = 0; i < pls.length; i++) {
            level.addObject(new PlatformWithUpperLayer(game, null,
                    platformTypes.getPlatformType("grassPlatform"),
                    pls[i][0], pls[i][1], pls[i][2], pls[i][3]));
        }

        /*
        level.addObject(new HalfCollidedPlatform(game, null, -500, 350, 500, 50));
        level.addObject(new ViscousPlatform(game, null, 1f, 500, 350, 500, 50, 500, 50));
        level.addObject(new ViscousPlatform(game, null, 1f, -500, 500, 500, 500, 500, 50));
        level.addObject(new Jumper(game, null,300, 500, 400, 100, 20));
        */
        /*
        level.addObject(new HalfCollidedPlatform(game, null, -500, 350, 500, 50));
        level.addObject(new ViscousPlatform(game, null, 1f, 500, 350, 500, 50, 500, 50));
        level.addObject(new ViscousPlatform(game, null, 1f, -500, 500, 500, 500, 500, 50));
        */


        for (float x = -800f / 30; x <= 701f / 30; x += 300f / 30) {
            level.addObject(new Jumper(game, null, null, 30, x, 0, 100f / 30, 20f / 30));
        }

        level.addObject(new HalfCollidedPlatform(game, null, null, -1000f / 30, 350f / 30, 400f / 30, 50f / 30));
        level.addObject(new HalfCollidedPlatform(game, null, null, -500f / 30, 350f / 30, 400f / 30, 50f / 30));

        level.addObject(new ViscousPlatform(game, null, null, .003f, -950f / 30, 580f / 30, 200f / 30, 50f / 30, 500, 50));
        level.addObject(new ViscousPlatform(game, null, null, .003f, -650f / 30, 580f / 30, 200f / 30, 50f / 30, 500, 50));
        level.addObject(new ViscousPlatform(game, null, null, .003f, -350f / 30, 580f / 30, 200f / 30, 50f / 30, 500, 50));

        level.addObject(new Jumper(game, null, null, 23, -670f / 30, 400f / 30, 50f / 30, 20f / 30));
        level.addObject(new Jumper(game, null, null, 23, -480f / 30, 400f / 30, 50f / 30, 20f / 30));

        level.addObject(new Portal(game, null, null, 0, 500f / 30, -18, 18, 0.2f, 100f / 30, 123, 123));

        gameProcess = new GameProcess(game, this, level);
    }

    @Override
    protected void preUpdate(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            pressedPlay = !pressedPlay;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.W) || pressedPlay) {
            super.preUpdate(delta);
            gameProcess.preUpdate(delta);
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        debugRenderer.render(gameProcess.getWorld(), toPix(getCameraProjectionMatrix().cpy()));

        batch.begin();

        game.mainFont.draw(batch, (int)(1f / Gdx.graphics.getDeltaTime()) + " fps",
                getCameraRect().x + 5, getCameraRect().y + getCameraRect().height - 10);

        batch.end();
    }

    @Override
    protected void postUpdate(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.W) || pressedPlay) {
            super.postUpdate(delta);
            gameProcess.postUpdate(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameProcess.resizeInputHandler(width, height);
    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        gameProcess.dispose();
    }
}
