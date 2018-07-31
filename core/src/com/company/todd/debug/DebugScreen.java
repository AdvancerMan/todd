package com.company.todd.debug;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.company.todd.game.active_objs.Player;
import com.company.todd.game.process.GameProcess;
import com.company.todd.game.static_objs.Platform;
import com.company.todd.input.InGameInputHandler;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

public class DebugScreen extends MyScreen {
    GameProcess gameProcess;
    InGameInputHandler inputHandler;

    public DebugScreen(ToddEthottGame game) {
        super(game);

        inputHandler = new InGameInputHandler();

        gameProcess = new GameProcess(game, this);

        Player pl = new Player(game, gameProcess,
                game.regionInfos.getRegionInfo("buttonClicked"), inputHandler);
        pl.setSize(50, 100);
        pl.setPosition(400, 500);

        gameProcess.addObject(pl);

        int[][] pls = {
                {100, 100, 100, 100},
                {200, 25, 500, 100},
                {123, 321, 123, 317},
                {10, 3, 500, 1}
        };
        for (int i = 0; i < pls.length; i++) {
            gameProcess.addObject(new Platform(game, gameProcess, new Platform.PlatformType(
                    game.regionInfos.getRegionInfo("buttonClicked"),
                    game.regionInfos.getRegionInfo("buttonNotClicked")
            ), pls[i][0], pls[i][1], pls[i][2], pls[i][3]));
        }
    }

    @Override
    protected void update(float delta) {
        super.update(delta);
        inputHandler.setNewTouchPosition();
        gameProcess.update(delta);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        batch.begin();

        gameProcess.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        gameProcess.dispose();
    }
}
