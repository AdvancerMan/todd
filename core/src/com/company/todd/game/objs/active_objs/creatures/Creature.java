package com.company.todd.game.objs.active_objs.creatures;

import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

import static com.company.todd.util.FloatCmp.less;

public abstract class Creature extends ActiveObject {  // TODO Creature
    protected float jumpPower;
    protected boolean isOnGround;

    protected float maxEnergyLevel;
    protected float energy;
    protected float maxHealthLevel;
    protected float health;

    // TODO health, energy
    // TODO hit(float damage);

    public Creature(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
                    float jumpPower, float walkingSpeed, float runningSpeed,
                    float x, float y, float width, float height) {
        super(game, gameProcess, regionInfo, walkingSpeed, runningSpeed, x, y, width, height);
        this.jumpPower = jumpPower;

        this.isOnGround = false;

        // TODO health and energy
        maxHealthLevel = 100;
        maxEnergyLevel = 100;
        health = maxHealthLevel;
        energy = maxEnergyLevel;
    }

    public void jump() { // TODO energy consuming: jump()
        if (isOnGround) {  // TODO isOnGround
            velocity.set(velocity.x, jumpPower);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }
}
