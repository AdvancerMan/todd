package com.company.todd.game.active_objs;

import com.badlogic.gdx.math.Vector2;

import com.company.todd.game.process.GameProcess;
import com.company.todd.game.InGameObject;
import com.company.todd.launcher.ToddEthottGame;

public abstract class ActiveObject extends InGameObject { // TODO collide
    public static final float DEFAULT_JUMP_POWER = 100;
    public static final float DEFAULT_WALKING_SPEED = 40;
    public static final float DEFAULT_RUNNING_SPEED = 70;

    protected Vector2 velocity; // TODO translation
    protected float jumpPower;
    protected float walkingSpeed;
    protected float runningSpeed;

    protected float maxEnergyLevel;
    protected float energy;
    protected float maxHealthLevel;
    protected float health;

    public ActiveObject(ToddEthottGame game, GameProcess gameProcess,
                        float jumpPower, float walkingSpeed, float runningSpeed) {
        super(game, gameProcess);
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

    public ActiveObject(ToddEthottGame game, GameProcess gameProcess) {
        this(game, gameProcess, DEFAULT_JUMP_POWER, DEFAULT_WALKING_SPEED, DEFAULT_RUNNING_SPEED);
    }

    public void jump() { // TODO energy consuming: jump()
        velocity.set(velocity.x, jumpPower);
    }

    public void fall(float gravity) {
        velocity.add(0, -gravity);

        if (velocity.y < -gameProcess.getMaxFallSpeed()) {
            velocity.set(velocity.x, -gameProcess.getMaxFallSpeed());
        }
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

    @Override
    public void update(float delta) {
        fall(gameProcess.getGravity());

        checkCollisions();

        updatePosition(delta);
    }

    protected void checkCollisions() {
        // TODO checkCollisions
    }

    protected void updatePosition(float delta) {
        sprite.translate(velocity.x * delta, velocity.y * delta);
    }
}
