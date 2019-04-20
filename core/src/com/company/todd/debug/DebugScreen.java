package com.company.todd.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.company.todd.game.level.Level;
import com.company.todd.game.objs.static_objs.Platform;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

public class DebugScreen extends MyScreen {
    GameProcess gameProcess;
    Platform.Types platformTypes;

    public DebugScreen(ToddEthottGame game) {
        super(game);

        platformTypes = new Platform.Types(game);

        Level level = new Level(game);

        float[][] pls = {  // x, y, width, height
                {100, 100, 100, 100},
                {200, 25, 500, 100},
                {700, 125, 500, 100},
                {123, 321, 123, 317},
                {10, 3, 500, 1}
        };
        for (int i = 0; i < pls.length; i++) {
            level.addObject(new Platform(game,
                    platformTypes.getPlatformType("grassPlatform"),
                    pls[i][0], pls[i][1], pls[i][2], pls[i][3]));
        }


        gameProcess = new GameProcess(game, this, level);

/*
        gameProcess.addObject(new Bullet(game, gameProcess, game.regionInfos.getRegionInfo("buttonNotClicked"), 123, true));
        gameProcess.addObject(new Bullet(game, gameProcess, game.regionInfos.getRegionInfo("buttonNotClicked"), 123, false));
*/
    }

    @Override
    protected void update(float delta) {
        super.update(delta);
        gameProcess.update(delta);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        batch.begin();

        gameProcess.draw(batch);

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
