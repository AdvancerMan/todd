package com.company.todd.game.objs.active_objs.dangerous.bombs;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.util.FloatCmp.less;

public class HomingMissile extends Bomb {
    private InGameObject target;
    private Vector2 lastTargetPos;

    public HomingMissile(ToddEthottGame game, GameProcess gameProcess,
                         InGameObject owner, MyAnimation animation,
                         InGameObject target,
                         float speed, float damage,
                         float x, float y, float width, float height) {
        super(game, gameProcess, owner, animation, speed, damage, x, y, width, height);
        this.target = target;
        lastTargetPos = new Vector2();
    }

    @Override
    protected void createMyBody() {
        super.createMyBody();
        setFixedRotation(false);
        setGravityScale(0);
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

        applyForceToCenter(accel);

        // turning the body to the speed vector's direction
        float angle = new Vector2(1, 0).angleRad(getLinearVelocity());
        setBodyAngle(angle);
    }

    @Override
    public void hit(InGameObject object) {  // TODO some special effects without body
        if (owner != null && owner.equals(object) && ownerSafe || isKilled()) {  // TODO fire when hits
            return;
        }

        if (killer == null) {
            killer = object;
        }
        takeDamage(0);
    }
}
