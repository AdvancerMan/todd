package com.company.todd.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.company.todd.game.other.Save;
import com.company.todd.game.process.GameProcess;
import com.company.todd.game.process.PauseProcess;
import com.company.todd.game.process.Process;
import com.company.todd.launcher.ToddEthottGame;

public class GameScreen extends MyScreen { // TODO GameScreen
    private GameProcess gameProcess;
    private PauseProcess pauseProcess;
    private Process usingProcess;
    private boolean gamePaused;

    public GameScreen(ToddEthottGame game) {
        super(game);
        gameProcess = new GameProcess(game, this);
        pauseProcess = new PauseProcess(game, this, gameProcess);

        usingProcess = gameProcess;
        gamePaused = false;

        Save save = new Save();  // TODO save
    }

    @Override
    protected void update(float delta) {
        super.update(delta);

        usingProcess.update(delta);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        batch.begin();

        usingProcess.draw(batch);

        batch.end();

        if (gamePaused) {
            usingProcess = pauseProcess;
        }
        else {
            usingProcess = gameProcess;
        }
    }

    @Override
    public void pause() {
        super.pause();
        gamePaused = true;
    }

    @Override
    public void resume() {
        super.resume();
        gamePaused = false;
    }

    @Override
    public void dispose() {
        gameProcess.dispose();
        pauseProcess.dispose();
    }
}
