package com.company.todd.game.active_objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.company.todd.game.process.GameProcess;
import com.company.todd.game.InGameObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public abstract class ActiveObject extends InGameObject { // TODO animation
    protected Vector2 velocity;
    protected float jumpPower;
    protected float walkingSpeed;
    protected float runningSpeed;

    protected float maxEnergyLevel;
    protected float energy;
    protected float maxHealthLevel;
    protected float health;

    protected TextureRegionInfo regionInfo;

    public ActiveObject(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
                        float jumpPower, float walkingSpeed, float runningSpeed) {
        super(game, gameProcess);
        this.regionInfo = regionInfo;
        this.sprite.setRegion(regionInfo.getTextureRegion());

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

        gameProcess.handleCollisions(this);

        updatePosition(delta);
    }

    public void collideWith(InGameObject object) {
        // Rectangle objectRect = object.getRect();
        // Rectangle thisRect = this.getRect();
        // TODO collideWith
    }

    protected void updatePosition(float delta) {
        sprite.translate(velocity.x * delta, velocity.y * delta);
    }

    @Override
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        if (getRect().overlaps(cameraRectangle)) {
            sprite.draw(batch);
        }
    }

    @Override
    public void dispose() {
        regionInfo.dispose();
    }
}
