package com.company.todd.game.objs.active_objs.creatures;

import com.company.todd.game.input.InGameInputHandler;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class Player extends Creature {
    private final InGameInputHandler inputHandler;

    public Player(ToddEthottGame game, GameProcess gameProcess,
                  TextureRegionInfo regionInfo, InGameInputHandler inputHandler,
                  float x, float y, float width, float height) {
        super(game, gameProcess, regionInfo, 100, 5f, 15f, x, y, width, height);
        // TODO walkingSpeed runningSpeed jumpPower

        this.inputHandler = inputHandler;
    }

    private void handleInput() {
        if (inputHandler.isGoingRight()) {
            run(true);
        }

        if (inputHandler.isGoingLeft()) {
            run(false);
        }

        if (inputHandler.isShooting()) {
            shoot();
        }

        if (inputHandler.isJumping()) {
            jump();
        }
    }

    @Override
    public void update(float delta) {
        handleInput();

        super.update(delta);
    }
}
