package com.company.todd.game.objs.active_objs.dangerous.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.util.FloatCmp.less;

public class HomingMissile extends DangerousObject {
    private float explosionTime;
    private InGameObject target;
    private Vector2 lastTargetPos;

    public HomingMissile(ToddEthottGame game, InGameObject owner, MyAnimation animation,
                         InGameObject target,
                         float speed, float damage, float explosionTime,
                         float x, float y, float width, float height) {
        super(game, owner, animation, speed, damage, x, y, width, height);
        this.explosionTime = explosionTime;
        this.target = target;
        lastTargetPos = new Vector2();
    }

    @Override
    protected void createMyBody() {
        super.createMyBody();
        body.setFixedRotation(false);
        body.setGravityScale(0);
    }

    @Override
    protected void updatePosition(float delta) {
        Vector2 targetPos = lastTargetPos;
        if (!target.isKilled()) targetPos = target.getBodyPosition();
        lastTargetPos = new Vector2(targetPos);

        // setting acceleration for missile
        Vector2 thisPos = getBodyPosition();
        Vector2 accel = new Vector2(targetPos);
        accel.sub(thisPos).nor().scl(runningSpeed);

        body.applyForceToCenter(accel.scl(body.getMass() * 2), true);

        // turning the body to the speed vector's direction
        float angle = new Vector2(1, 0).angleRad(body.getLinearVelocity());
        setAngle(angle);
    }

    @Override
    public void hit(InGameObject object) {  // TODO some special effects without body
        if (owner != null && owner.equals(object) && ownerSafe) {  // TODO fire when hits
            return;
        }

        if (killer == null) {
            killer = object;
        }
        takeDamage(0);
    }

    @Override
    public void takeDamage(float amount) {
        kill();

        Rectangle rect = getBodyAABB();
        Vector2 center = new Vector2();
        rect.getCenter(center);
        float radius = rect.height;
        if (less(radius, rect.width)) radius = rect.width;

        gameProcess.addObject(new Explosion(game, null,
                game.animationInfos.getAnimation("explosion"),
                damage, explosionTime, center.x, center.y, radius,
                radius * 2, radius * 2));
    }
}
