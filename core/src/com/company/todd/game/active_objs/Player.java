package com.company.todd.game.active_objs;

import com.badlogic.gdx.Gdx;

import com.company.todd.game.process.GameProcess;
import com.company.todd.input.InGameInputHandler;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class Player extends ActiveObject {
    private final InGameInputHandler inputHandler;

    public Player(ToddEthottGame game, GameProcess gameProcess,
                  TextureRegionInfo regionInfo, InGameInputHandler inputHandler) {
        super(game, gameProcess, regionInfo, 300, 50, 150);
        // TODO walkingSpeed runningSpeed jumpPower

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

    @Override
    public void update(float delta) {
        if (Gdx.input.isTouched()) {
            handleInput();
        }

        super.update(delta);
    }
}
