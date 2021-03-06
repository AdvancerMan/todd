package com.company.todd.game.objs.active_objs.dangerous;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.game.process.GameProcess.toPix;

public abstract class DangerousObject extends ActiveObject {  // TODO DangerousObject
    protected float damage;
    protected InGameObject owner;
    protected InGameObject killer;
    protected boolean ownerSafe;

    public DangerousObject(ToddEthottGame game, GameProcess gameProcess, InGameObject owner, MyAnimation animation,
                           float speed, float damage,
                           BodyInfo bodyInfo, Vector2 spriteSize) {
        super(game, gameProcess, animation, speed, bodyInfo, spriteSize);
        this.damage = damage;
        this.owner = owner;
        this.killer = null;
        this.ownerSafe = true;
    }

    public DangerousObject(ToddEthottGame game, GameProcess gameProcess, InGameObject owner, MyAnimation animation,
                        float speed, float damage,
                        float x, float y,
                        float bodyWidth, float bodyHeight,
                        float spriteWidth, float spriteHeight) {
        this(game, gameProcess, owner, animation, speed, damage,
                new BodyInfo(x, y, bodyWidth, bodyHeight),
                new Vector2(spriteWidth, spriteHeight));
    }

    public DangerousObject(ToddEthottGame game, GameProcess gameProcess,
                           InGameObject owner, MyAnimation animation,
                           float speed, float damage,
                           float x, float y, float width, float height) {
        this(game, gameProcess, owner, animation, speed, damage, x, y, width, height, toPix(width), toPix(height));
    }

    public DangerousObject(ToddEthottGame game, GameProcess gameProcess,
                           InGameObject owner, MyAnimation animation,
                           float speed, float damage,
                           float x, float y,
                           float bodyRadius,
                           float spriteWidth, float spriteHeight) {
        this(game, gameProcess, owner, animation, speed, damage,
                new BodyInfo(x, y, bodyRadius),
                new Vector2(spriteWidth, spriteHeight));
    }

    @Override
    protected void createMyBody() {
        super.createMyBody();
        getBody().setBullet(true);
    }

    public void hit(InGameObject object) {  // TODO some special effects without body
        if (owner != null && owner.equals(object) && ownerSafe) {  // TODO fire when hits
            return;
        }

        if (object instanceof ActiveObject) {
            ((ActiveObject)object).takeDamage(damage);
        }

        if (killer == null) {
            killer = object;
        }
        takeDamage(0);
    }

    @Override
    public void beginContact(Contact contact, InGameObject object) {
        super.beginContact(contact, object);
        if (!isKilled()) {
            hit(object);
        }
    }

    @Override
    public void endContact(Contact contact, InGameObject object) {
        super.endContact(contact, object);
        if (object.equals(owner)) {
            ownerSafe = false;
        }
    }

    protected boolean isContactDisabled(InGameObject object) {
        return isKilled() && !object.equals(killer) || object.equals(owner) && ownerSafe;
    }

    @Override
    public void contactPreSolve(Contact contact, Manifold oldManifold, InGameObject object) {
        super.contactPreSolve(contact, oldManifold, object);

        if (isContactDisabled(object)) {
            contact.setEnabled(false);
        }
    }

    @Override
    public boolean isGroundFor(Contact contact, InGameObject object) {
        return false;
    }
}
