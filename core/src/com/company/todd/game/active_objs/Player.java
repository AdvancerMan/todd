package com.company.todd.game.active_objs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.company.todd.game.process.GameProcess;
import com.company.todd.input.InGameInputHandler;
import com.company.todd.launcher.ToddEthottGame;

public class Player extends ActiveObject {
    private final InGameInputHandler inputHandler;

    public Player(ToddEthottGame game, InGameInputHandler inputHandler) {
        super(game);

        this.inputHandler = inputHandler;
    }

    private void handleInput() {
        if (inputHandler.isGoingRight()) {
            walk(true);
        }

        if (inputHandler.isGoingLeft()) {
            walk(false);
        }

        if (inputHandler.isJumping()) {
            jump();
        }
    }

    //private void checkCollisions()

    @Override
    public void update(float delta) {
        if (Gdx.input.isTouched()) {
            handleInput();
        }
        fall(GameProcess.GRAVITY);

        //checkCollisions();

        updatePosition(delta);
        System.out.println(sprite.getX() + " " + sprite.getY());
    }

    @Override
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {

    }

    @Override
    public void dispose() {

    }
}
