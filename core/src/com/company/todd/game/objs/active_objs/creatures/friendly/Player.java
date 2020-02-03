package com.company.todd.game.objs.active_objs.creatures.friendly;

import com.badlogic.gdx.math.Rectangle;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.input.InGameInputHandler;
import com.company.todd.game.objs.active_objs.creatures.CreatureWithHands;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public class Player extends CreatureWithHands {
    private final InGameInputHandler inputHandler;

    public Player(ToddEthottGame game, GameProcess gameProcess,
                  MyAnimation animation, MyAnimation handsAnimation, Rectangle handsRectangle,
                  InGameInputHandler inputHandler,
                  float x, float y, float width, float height) {
        super(game, gameProcess, animation, handsAnimation, handsRectangle, 650, 450f, x, y, width, height);
        // TODO walkingSpeed runningSpeed jumpPower

        this.inputHandler = inputHandler;
    }

    private void handleInput() {
        if (inputHandler.isGoingRight()) {
            setDirToRight(true);
            run();
        }

        if (inputHandler.isGoingLeft()) {
            setDirToRight(false);
            run();
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
