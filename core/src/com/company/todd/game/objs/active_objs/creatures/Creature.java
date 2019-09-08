package com.company.todd.game.objs.active_objs.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.objs.active_objs.dangerous.Bullet;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

import static com.company.todd.game.process.GameProcess.toPix;
import static com.company.todd.util.FloatCmp.less;
import static com.company.todd.util.FloatCmp.lessOrEquals;

public abstract class Creature extends ActiveObject {  // TODO Creature
    protected float jumpPower;

    private static final float JUMP_COOLDOWN = 0.1f;
    private Array<InGameObject> grounds;
    private float timeFromLastJump;

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
                    float x, float y, float width, float height) {
        super(game, animation, runningSpeed, x, y, width, height);
        this.jumpPower = jumpPower;

        grounds = new Array<InGameObject>();
        timeFromLastJump = JUMP_COOLDOWN;

        // TODO health and energy
        maxHealthLevel = 100;
        maxEnergyLevel = 100;
        health = maxHealthLevel;
        energy = maxEnergyLevel;

        cooldown = 100000000;

        changedAnim = false;
    }

    public void jump() { // TODO energy consuming: jump()
        if (isOnGround()) {
            timeFromLastJump = 0;
            setPlayingAnimationName(MyAnimation.AnimationType.JUMP, true);
            velocity.set(velocity.x, jumpPower);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        timeFromLastJump += delta;

        if (!changedAnim) {
            setPlayingAnimationName(MyAnimation.AnimationType.STAY, false);
        }

        if (less(body.getLinearVelocity().y, 0) && !isOnGround()) {
            setPlayingAnimationName(MyAnimation.AnimationType.FALL, false);
        }

        changedAnim = false;
    }

    public void shoot() {  // TODO shoot()
        if (TimeUtils.nanoTime() - lastMomentOfShoot <= cooldown) return;
        lastMomentOfShoot = TimeUtils.nanoTime();

        setPlayingAnimationName(MyAnimation.AnimationType.SHOOT, true);

        Rectangle objectRect = getBodyRect();  // TODO good place for bullet spawn
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

    public void addGround(InGameObject ground) {
        grounds.add(ground);
    }

    public void removeGround(InGameObject object) {
        grounds.removeValue(object, false);
    }

    public boolean isOnGround() {
        return grounds != null && grounds.size > 0 && timeFromLastJump > JUMP_COOLDOWN;
    }

    protected boolean checkGround(Vector2[] points, int pointsCount) {
        switch (pointsCount) {
            case (0):
                break;
            case (2):
                if (!FloatCmp.equals(points[1].y, -getBodyRect().height / 2, 1)) {
                    break;
                }
            case (1):
                if (!FloatCmp.equals(points[0].y, -getBodyRect().height / 2, 1)) {
                    break;
                }
                return true;
        }

        return false;
    }

    @Override
    public void beginContact(Contact contact, InGameObject object) {
        super.beginContact(contact, object);

        if (object.isAvailableToBeGround()) {
            Vector2[] points = contact.getWorldManifold().getPoints();

            points = new Vector2[] {
                    toPix(points[0].cpy()).sub(getBodyPosition()),
                    toPix(points[1].cpy()).sub(getBodyPosition())
            };

            if (checkGround(points, contact.getWorldManifold().getNumberOfContactPoints())) {
                addGround(object);
            }
        }
    }

    @Override
    public void endContact(Contact contact, InGameObject object) {
        super.endContact(contact, object);

        removeGround(object);  // FIXME removing another ground (let's store fixture wrappers?)
    }
}
