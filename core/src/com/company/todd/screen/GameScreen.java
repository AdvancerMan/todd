package com.company.todd.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.company.todd.game.level.levels.arena.MainArenaLevel;
import com.company.todd.game.process.GameProcess;
import com.company.todd.game.process.PauseProcess;
import com.company.todd.game.process.Process;
import com.company.todd.game.save.Save;
import com.company.todd.launcher.ToddEthottGame;

public class GameScreen extends MyScreen { // TODO GameScreen
    protected GameProcess gameProcess;
    protected PauseProcess pauseProcess;
    protected Process usingProcess;
    protected boolean gamePaused;

    public GameScreen(ToddEthottGame game) {
        super(game);
        gameProcess = new GameProcess(game, this, new MainArenaLevel(game));  // TODO Level loading
        pauseProcess = new PauseProcess(game, this, gameProcess);

        usingProcess = gameProcess;
        gamePaused = false;

        Save save = new Save();  // TODO save
    }

    @Override
    protected void preUpdate(float delta) {
        super.preUpdate(delta);

        usingProcess.preUpdate(delta);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        batch.begin();

        usingProcess.draw(batch);

        batch.end();
    }

    @Override
    protected void postUpdate(float delta) {
        super.postUpdate(delta);

        usingProcess.postUpdate(delta);

        if (gamePaused) {
            usingProcess = pauseProcess;
        } else {
            usingProcess = gameProcess;
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameProcess.resizeInputHandler(width, height);
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
