package com.company.todd.game.objs.active_objs.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.objs.active_objs.creatures.friendly.Player;
import com.company.todd.game.objs.active_objs.dangerous.Bullet;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

import static com.company.todd.game.process.GameProcess.toPix;
import static com.company.todd.util.FloatCmp.less;
import static com.company.todd.util.FloatCmp.lessOrEquals;

public abstract class Creature extends ActiveObject {  // TODO Creature
    protected float jumpPower;

    private boolean onGround;

    protected float maxEnergyLevel;
    protected float energy;
    protected float maxHealthLevel;
    protected float health;

    private long lastMomentOfShoot;
    protected long cooldown;

    private boolean changedAnim;

    // TODO health, energy, cooldown
    // TODO hit(float damage);

    public Creature(ToddEthottGame game, MyAnimation animation,
                    float jumpPower, float runningSpeed,
                    BodyInfo bodyInfo, Vector2 spriteSize) {
        super(game, animation, runningSpeed, bodyInfo, spriteSize);
        this.jumpPower = jumpPower;

        onGround = false;

        // TODO health and energy
        maxHealthLevel = 100;
        maxEnergyLevel = 100;
        health = maxHealthLevel;
        energy = maxEnergyLevel;

        cooldown = 100000000;

        changedAnim = false;
    }

    public Creature(ToddEthottGame game, MyAnimation animation,
                    float jumpPower, float runningSpeed,
                    float x, float y,
                    float bodyWidth, float bodyHeight,
                    float spriteWidth, float spriteHeight) {
        this(game, animation, jumpPower, runningSpeed,
                new BodyInfo(x, y, bodyWidth, bodyHeight),
                new Vector2(spriteWidth, spriteHeight));
    }

    public Creature(ToddEthottGame game, MyAnimation animation,
                    float jumpPower, float runningSpeed,
                    float x, float y, float width, float height) {
        this(game, animation, jumpPower, runningSpeed, x, y, width, height, width, height);
    }

    public Creature(ToddEthottGame game, MyAnimation animation,
                    float jumpPower, float runningSpeed,
                    float x, float y,
                    float bodyRadius,
                    float spriteWidth, float spriteHeight) {
        this(game, animation, jumpPower, runningSpeed,
                new BodyInfo(x, y, bodyRadius),
                new Vector2(spriteWidth, spriteHeight));
    }

    public void jump() { // TODO energy consuming: jump()
        if (isOnGround()) {
            setPlayingAnimationName(MyAnimation.AnimationType.JUMP, true);
            velocity.set(velocity.x, jumpPower);  // TODO setVelocity(jumpPower) ?
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (!changedAnim) {
            setPlayingAnimationName(MyAnimation.AnimationType.STAY, false);
        }

        if (less(getLinearVelocity().y, 0) && !isOnGround()) {
            setPlayingAnimationName(MyAnimation.AnimationType.FALL, false);
        }

        changedAnim = false;
    }

    @Override
    public void preWorldUpdate(float delta) {
        super.preWorldUpdate(delta);
        onGround = false;
    }

    public void shoot() {  // TODO shoot()
        if (TimeUtils.nanoTime() - lastMomentOfShoot <= cooldown) return;
        lastMomentOfShoot = TimeUtils.nanoTime();

        setPlayingAnimationName(MyAnimation.AnimationType.SHOOT, true);

        Rectangle objectRect = getBodyAABB();  // TODO good place for bullet spawn
        float x, y;
        y = objectRect.y + objectRect.height / 2;  // TODO - bulletType.height / 2
        x = objectRect.x;

        if (toRight) {
            x += objectRect.width;
        } else {
            // TODO x -= bulletType.width + 1
        }

        // TODO BulletType!!!
        ArrayMap<MyAnimation.AnimationType, Array<TextureRegionInfo.TextureRegionGetter>> tmp = new ArrayMap<MyAnimation.AnimationType, Array<TextureRegionInfo.TextureRegionGetter>>();
        tmp.put(MyAnimation.AnimationType.RUN, new Array<TextureRegionInfo.TextureRegionGetter>(new TextureRegionInfo.TextureRegionGetter[] {game.regionInfos.getRegionInfo("grassPlatformDown").getRegionGetter()}));
        tmp.put(MyAnimation.AnimationType.STAY, new Array<TextureRegionInfo.TextureRegionGetter>(new TextureRegionInfo.TextureRegionGetter[] {game.regionInfos.getRegionInfo("grassPlatformDown").getRegionGetter()}));

        ArrayMap<MyAnimation.AnimationType, Float> tmpp = new ArrayMap<MyAnimation.AnimationType, Float>();
        tmpp.put(MyAnimation.AnimationType.RUN, 0.1f);
        tmpp.put(MyAnimation.AnimationType.STAY, 0.1f);

        ArrayMap<MyAnimation.AnimationType, Animation.PlayMode> tmppp = new ArrayMap<MyAnimation.AnimationType, Animation.PlayMode>();
        tmppp.put(MyAnimation.AnimationType.RUN, Animation.PlayMode.LOOP);
        tmppp.put(MyAnimation.AnimationType.STAY, Animation.PlayMode.LOOP);

        Bullet bul = new Bullet(
                game, this,
                new MyAnimation(tmpp, tmppp, tmp),
                x, y, 100, 20, toRight
        );
        bul.setPlayingAnimationName(MyAnimation.AnimationType.RUN, true);
        gameProcess.addObject(bul);
    }

    @Override
    public void setPlayingAnimationName(MyAnimation.AnimationType animType, boolean changeEquals) {
        if (isOnGround() || animType.equals(MyAnimation.AnimationType.SHOOT) ||
                animType.equals(MyAnimation.AnimationType.FALL)) {
            super.setPlayingAnimationName(animType, changeEquals);
        }
        changedAnim = true;
    }

    @Override
    public void takeDamage(float amount) {
        health -= amount;
        if (lessOrEquals(health, 0)) {
            kill();
        }
    }

    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public boolean isGroundFor(Contact contact, InGameObject object) {
        return false;
    }

    @Override
    public void contactPreSolve(Contact contact, Manifold oldManifold, InGameObject object) {
        super.contactPreSolve(contact, oldManifold, object);

        if (!onGround && object.isGroundFor(contact, this)) {
            onGround = true;
        }
    }
}
