package com.company.todd.game.objs.active_objs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.ArrayMap;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public abstract class ActiveObject extends InGameObject {
    protected boolean toRight;

    protected float walkingSpeed;
    protected float runningSpeed;
    protected Vector2 velocity;

    public ActiveObject(ToddEthottGame game, MyAnimation animation,
                        float walkingSpeed, float runningSpeed,
                        float x, float y, float width, float height) {
        super(game, BodyDef.BodyType.DynamicBody, animation, x, y, width, height);

        setPlayingAnimationName("stay", false);

        this.walkingSpeed = walkingSpeed;
        this.runningSpeed = runningSpeed;
        this.velocity = new Vector2();

        toRight = true;
    }

    public ActiveObject(ToddEthottGame game, MyAnimation animation,
                        float speed,
                        float x, float y, float width, float height) {
        this(game, animation, speed, speed, x, y, width, height);
    }

    public void changeDirection(boolean toRight) {
        if (toRight == this.toRight) {
            return;
        }

        this.toRight = toRight;
        setDirToRight(toRight);
    }

    public void walk(boolean toRight) {
        if (toRight) {
            velocity.set(walkingSpeed, velocity.y);
        } else {
            velocity.set(-walkingSpeed, velocity.y);
        }
        setPlayingAnimationName("walk", false);
    }

    public void run(boolean toRight) { // TODO energy consuming: run()
        if (toRight) {
            velocity.set(runningSpeed, velocity.y);
        } else {
            velocity.set(-runningSpeed, velocity.y);
        }
        setPlayingAnimationName("run", false);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        updatePosition(delta);
    }

    protected void updatePosition(float delta) {
        for (Fixture fixture : body.getFixtureList()) {
            if (velocity.x == 0) {
                fixture.setFriction(1000);
            } else {
                fixture.setFriction(0);
            }
        }

        body.applyLinearImpulse(new Vector2(velocity.x - body.getLinearVelocity().x, velocity.y),
                body.getWorldCenter(), true);  // TODO delta * impulse
        velocity.set(0, 0);
    }

    public abstract void damage(float amount);

    @Override
    public void dispose() {
        super.dispose();
    }
}
