package com.company.todd.game.objs.active_objs.dangerous.bombs;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.util.FloatCmp;

public class Explosion extends DangerousObject {
    private Array<ActiveObject> damaged;
    private float timeAlive;

    public Explosion(ToddEthottGame game, GameProcess gameProcess,
                     InGameObject owner, MyAnimation animation,
                     float damage, float timeAlive,
                     float x, float y, float explosionRadius,
                     float spriteWidth, float spriteHeight) {
        super(game, gameProcess, owner, animation, 0, damage,
                new BodyInfo(x, y, explosionRadius),
                new Vector2(spriteWidth, spriteHeight));

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
        getBody().setGravityScale(0);
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
            object.takeDamage(damage);
            damaged.add((ActiveObject) object);
        }
    }

    @Override
    protected boolean isContactDisabled(InGameObject object) {
        return true;
    }
}
