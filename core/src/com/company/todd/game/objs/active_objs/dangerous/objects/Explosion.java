package com.company.todd.game.objs.active_objs.dangerous.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.util.FloatCmp;

public class Explosion extends DangerousObject {
    private Array<ActiveObject> damaged;
    private float timeAlive;

    public Explosion(ToddEthottGame game, InGameObject owner, MyAnimation animation,
                     float damage, float timeAlive,
                     float x, float y, float explosionRadius,
                     float spriteWidth, float spriteHeight) {
        super(game, owner, animation, 0, damage,
                new Vector2(spriteWidth, spriteHeight), new BodyInfo(x, y, explosionRadius));

        setAnimationDuration(MyAnimation.AnimationType.STAY, timeAlive);
        damaged = new Array<ActiveObject>();
        this.timeAlive = timeAlive;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        timeAlive -= delta;
        if (FloatCmp.lessOrEquals(timeAlive, 0)) {
            kill();
        }
    }

    @Override
    protected void createMyBody() {
        super.createMyBody();
        body.setGravityScale(0);
    }

    @Override
    public void takeDamage(float amount) {}

    @Override
    public void hit(InGameObject object) {  // TODO hit()
        if (owner != null && owner.equals(object) && ownerSafe) {
            return;
        }

        if (object instanceof ActiveObject &&
                !damaged.contains((ActiveObject)object, false)) {
            ((ActiveObject)object).takeDamage(damage);
            damaged.add((ActiveObject)object);
        }
    }

    @Override
    protected boolean isContactDisabled(InGameObject object) {
        return true;
    }
}
