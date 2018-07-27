package com.company.todd.game.process;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.company.todd.game.active_objs.Player;
import com.company.todd.input.InGameInputHandler;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

public class GameProcess implements Process {  // TODO GameProcess
    private int gravity;
    private int maxFallSpeed;

    private final ToddEthottGame game;
    private InGameInputHandler inputHandler;
    private final MyScreen screen;

    public GameProcess(ToddEthottGame game, MyScreen screen) {
        this.game = game;
        this.screen = screen;

        inputHandler = new InGameInputHandler();

        gravity = 1;
        maxFallSpeed = 50;
    }

    private void handleInput(float delta) {
        if (Gdx.input.justTouched()) {
            if (inputHandler.isPaused()) {
                game.screenManager.removeThisScreen();
            }
        }
    }

    @Override
    public void update(float delta) {
        handleInput(delta);
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void dispose() {

    }

    public int getGravity() {
        return gravity;
    }

    public int getMaxFallSpeed() {
        return maxFallSpeed;
    }
}
