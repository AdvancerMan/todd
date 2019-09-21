package com.company.todd.game.objs.active_objs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ArrayMap;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

public abstract class ActiveObject extends InGameObject {
    protected boolean toRight;

    protected float runningSpeed;
    protected Vector2 velocity;

    public ActiveObject(ToddEthottGame game, MyAnimation animation,
                        float runningSpeed,
                        Vector2 spriteSize, BodyInfo bodyInfo) {
        super(game, BodyDef.BodyType.DynamicBody, animation, bodyInfo, spriteSize);

        this.runningSpeed = runningSpeed;
        this.velocity = new Vector2();

        toRight = true;

    }

    public ActiveObject(ToddEthottGame game, MyAnimation animation,
                        float runningSpeed,
                        float x, float y, float width, float height) {
        this(game, animation, runningSpeed, new Vector2(width, height),
                new BodyInfo(x, y, width, height));
    }

    public void changeDirection(boolean toRight) {
        if (toRight == this.toRight) {
            return;
        }

        this.toRight = toRight;
        setDirToRight(toRight);
    }

    public void run(boolean toRight) {
        if (toRight) {
            velocity.set(runningSpeed, velocity.y);
        } else {
            velocity.set(-runningSpeed, velocity.y);
        }
        setPlayingAnimationName(MyAnimation.AnimationType.RUN, false);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        updatePosition(delta);
    }

    protected void updatePosition(float delta) {
        for (Fixture fixture : getFixtureList()) {
            if (FloatCmp.equals(velocity.x, 0)) {
                fixture.setFriction(10);
            } else {
                fixture.setFriction(0);
            }
        }

        applyLinearImpulseToCenter(new Vector2(velocity.x - getLinearVelocity().x, velocity.y));  // TODO delta * impulse
        velocity.set(0, 0);
    }

    public abstract void takeDamage(float amount);

    @Override
    public void dispose() {
        super.dispose();
    }
}
