package com.company.todd.game.active_objs;

import com.badlogic.gdx.math.Vector2;

import com.company.todd.game.InGameObject;
import com.company.todd.launcher.ToddEthottGame;

public abstract class ActiveObject extends InGameObject { // TODO collide
    public static final float DEFAULT_JUMP_POWER = 10;
    public static final float DEFAULT_WALKING_SPEED = 4;
    public static final float DEFAULT_RUNNING_SPEED = 7;

    protected Vector2 velocity; // TODO translation
    protected float jumpPower;
    protected float walkingSpeed;
    protected float runningSpeed;

    protected float maxEnergyLevel;
    protected float energy;
    protected float maxHealthLevel;
    protected float health;

    public ActiveObject(ToddEthottGame game, float jumpPower, float walkingSpeed, float runningSpeed) {
        super(game);
        velocity = new Vector2(0, 0);

        this.jumpPower = jumpPower;
        this.walkingSpeed = walkingSpeed;
        this.runningSpeed = runningSpeed;

        // TODO health and energy
        maxHealthLevel = 100;
        maxEnergyLevel = 100;
        health = maxHealthLevel;
        energy = maxEnergyLevel;
    }

    public ActiveObject(ToddEthottGame game) {
        this(game, DEFAULT_JUMP_POWER, DEFAULT_WALKING_SPEED, DEFAULT_RUNNING_SPEED);
    }

    public void jump() { // TODO energy consuming: jump()
        velocity.set(velocity.x, jumpPower);
    }

    public void fall(float gravity) {
        velocity.add(0, -gravity);
    }

    public void stand() {
        velocity.set(0, velocity.y);
    }

    public void walk(boolean toRight) {
        if (toRight) {
            velocity.set(walkingSpeed, velocity.y);
        }
        else {
            velocity.set(-walkingSpeed, velocity.y);
        }
    }

    public void run(boolean toRight) { // TODO energy consuming: run()
        if (toRight) {
            velocity.set(runningSpeed, velocity.y);
        }
        else {
            velocity.set(-runningSpeed, velocity.y);
        }
    }

    private void updatePosition(float delta) {
        sprite.translate(velocity.x * delta, velocity.y * delta);
    }
}
