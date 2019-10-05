package com.company.todd.game.process;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

public class PauseProcess implements Process {  // TODO PauseProcess
    private final ToddEthottGame game;
    private final MyScreen screen;
    private final GameProcess gameProcess;

    public PauseProcess(ToddEthottGame game, MyScreen screen, GameProcess gameProcess) {
        this.game = game;
        this.screen = screen;
        this.gameProcess = gameProcess;
    }

    @Override
    public void preUpdate(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        gameProcess.draw(batch);
        // TODO draw some menu buttons
    }

    @Override
    public void postUpdate(float delta) {

    }

    @Override
    public void dispose() {

    }
}
