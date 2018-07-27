package com.company.todd.game.process;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.company.todd.game.active_objs.Player;
import com.company.todd.input.InGameInputHandler;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

public class GameProcess implements Process {  // TODO GameProcess
    public static int GRAVITY = 1;
    public static int MAX_FALL_SPEED = 50;

    private final ToddEthottGame game;
    private InGameInputHandler inputHandler;
    private TextureRegion texture;
    private Player player;
    private final MyScreen screen;

    public GameProcess(ToddEthottGame game, MyScreen screen) {
        this.game = game;
        this.screen = screen;

        inputHandler = new InGameInputHandler();

        texture = game.textureManager.getTextureRegion("badlogic.jpg", 10, 10, 64, 64);
        player = new Player(game, inputHandler);
        player.setPosition(500, 400);
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
        player.update(delta);
        handleInput(delta);
    }

    @Override
    public void draw(SpriteBatch batch) {
        player.draw(batch, screen.getCameraRect());
    }

    @Override
    public void dispose() {
        game.textureManager.disposeTexture("badlogic.jpg", 1);
        player.dispose();
    }
}
