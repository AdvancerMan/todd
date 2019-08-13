package com.company.todd.game.objs.active_objs.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.company.todd.game.animations.MyAnimation;
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

    private boolean changedAnim;

    // TODO health, energy, cooldown
    // TODO hit(float damage);

    public Creature(ToddEthottGame game, MyAnimation animation,
                    float jumpPower, float runningSpeed,
                    float x, float y, float width, float height) {
        super(game, animation, runningSpeed, x, y, width, height);
        this.jumpPower = jumpPower;

        this.isOnGround = false;

        // TODO health and energy
        maxHealthLevel = 100;
        maxEnergyLevel = 100;
        health = maxHealthLevel;
        energy = maxEnergyLevel;

        coolDown = 100000000;

        changedAnim = false;
    }

    public void jump() { // TODO energy consuming: jump()
        if (isOnGround) {  // TODO isOnGround
            setPlayingAnimationName("jump", true);
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

        if (!changedAnim) {
            setPlayingAnimationName("stay", false);
        }
        changedAnim = false;
    }

    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }

    public void shoot() {  // TODO shoot()
        if (TimeUtils.nanoTime() - lastMomentOfShoot <= coolDown) return;
        lastMomentOfShoot = TimeUtils.nanoTime();

        setPlayingAnimationName("shoot", true);

        Rectangle objectRect = getObjectRect();  // TODO good place for bullet spawn
        float x, y;
        y = objectRect.y + objectRect.height / 2;  // TODO - bulletType.height / 2
        x = objectRect.x;

        if (toRight) {
            x += objectRect.width;
        } else {
            // TODO x -= bulletType.width + 1
        }

        // TODO BulletType!!!
        ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>> tmp = new ArrayMap<String, Array<TextureRegionInfo.TextureRegionGetter>>();
        tmp.put("run", new Array<TextureRegionInfo.TextureRegionGetter>(new TextureRegionInfo.TextureRegionGetter[] {game.regionInfos.getRegionInfo("grassPlatformDown").getRegionGetter()}));

        ArrayMap<String, Float> tmpp = new ArrayMap<String, Float>();
        tmpp.put("run", 0.1f);

        ArrayMap<String, Animation.PlayMode> tmppp = new ArrayMap<String, Animation.PlayMode>();
        tmppp.put("run", Animation.PlayMode.LOOP);

        Bullet bul = new Bullet(
                game, this,
                new MyAnimation(tmpp, tmppp, tmp),
                x, y, 100, 20, toRight
        );
        bul.setPlayingAnimationName("walk", true);
        gameProcess.addObject(bul);
    }

    @Override
    public void setPlayingAnimationName(String animationName, boolean changeEquals) {
        if (isOnGround || animationName.equals("shoot")) {
            super.setPlayingAnimationName(animationName, changeEquals);
        }
        changedAnim = true;
    }

    @Override
    public void damage(float amount) {
        health -= amount;
        if (lessOrEquals(health, 0)) {
            kill();
        }
    }
}
