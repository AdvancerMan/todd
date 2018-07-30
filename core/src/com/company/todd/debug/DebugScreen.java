package com.company.todd.debug;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.company.todd.game.active_objs.Player;
import com.company.todd.game.process.GameProcess;
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
