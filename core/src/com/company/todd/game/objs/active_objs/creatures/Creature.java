package com.company.todd.game.objs.active_objs.creatures;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.objs.active_objs.dangerous.Bullet;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

import static com.company.todd.util.FloatCmp.lessOrEquals;

public abstract class Creature extends ActiveObject {  // TODO Creature
    protected float jumpPower;
    protected boolean isOnGround;

    protected float maxEnergyLevel;
    protected float energy;
    protected float maxHealthLevel;
    protected float health;

    private long lastMomentOfShoot;
    protected long coolDown;

    // TODO health, energy, cooldown
    // TODO hit(float damage);

    public Creature(ToddEthottGame game, TextureRegionInfo regionInfo,
                    float jumpPower, float walkingSpeed, float runningSpeed,
                    float x, float y, float width, float height) {
        super(game, regionInfo, walkingSpeed, runningSpeed, x, y, width, height);
        this.jumpPower = jumpPower;

        this.isOnGround = false;

        // TODO health and energy
        maxHealthLevel = 100;
        maxEnergyLevel = 100;
        health = maxHealthLevel;
        energy = maxEnergyLevel;

        coolDown = 1000000000;
    }

    public void jump() { // TODO energy consuming: jump()
        if (isOnGround) {  // TODO isOnGround
            velocity.set(velocity.x, jumpPower);
        }
    }

    private float prevYVel = 1;

    @Override
    public void update(float delta) {
        super.update(delta);

        float nowYVel = body.getLinearVelocity().y;
        if (FloatCmp.equals(nowYVel, 0) && FloatCmp.lessOrEquals(prevYVel, 0)) {
            isOnGround = true;
        } else {
            isOnGround = false;
        }
        prevYVel = nowYVel;
    }

    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }

    public void shoot() {  // TODO shoot()
        if (TimeUtils.nanoTime() - lastMomentOfShoot <= coolDown) return;
        lastMomentOfShoot = TimeUtils.nanoTime();

        Rectangle objectRect = getObjectRect();  // TODO good place for bullet spawn
        float x, y;
        y = objectRect.y + objectRect.height / 2;  // TODO - bulletType.height / 2
        x = objectRect.x;

        if (toRight) {
            x += objectRect.width - 20;
            y -= 20;
        } else {
            // TODO x -= bulletType.width + 1
        }

        gameProcess.addObject(
                new Bullet(
                        game, this,
                        game.regionInfos.getRegionInfo("grassPlatformDown"),
                        x, y, 100, 20, toRight
                )
        );
    }

    @Override
    public void damage(float amount) {
        health -= amount;
        if (lessOrEquals(health, 0)) {
            kill();
        }
    }
}
