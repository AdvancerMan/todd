package com.company.todd.game.objs.active_objs.creatures.friendly;

import com.badlogic.gdx.math.Rectangle;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.input.InGameInputHandler;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.game.objs.active_objs.creatures.CreatureWithHands;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class Player extends CreatureWithHands {
    private final InGameInputHandler inputHandler;

    public Player(ToddEthottGame game,
                  MyAnimation animation, MyAnimation handsAnimation, Rectangle handsRectangle,
                  InGameInputHandler inputHandler,
                  float x, float y, float width, float height) {
        super(game, animation, handsAnimation, handsRectangle, 500, 450f, x, y, width, height);
        // TODO walkingSpeed runningSpeed jumpPower

        this.inputHandler = inputHandler;
    }

    private void handleInput() {
        if (inputHandler.isGoingRight()) {
            run(true);
            changeDirection(true);
        }

        if (inputHandler.isGoingLeft()) {
            run(false);
            changeDirection(false);
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
