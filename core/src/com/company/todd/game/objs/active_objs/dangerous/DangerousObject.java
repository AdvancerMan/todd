package com.company.todd.game.objs.active_objs.dangerous;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public abstract class DangerousObject extends ActiveObject {  // TODO DangerousObject
    protected float damage;
    protected InGameObject owner;
    protected InGameObject killer;
    protected boolean ownerSafe;

    public DangerousObject(ToddEthottGame game, InGameObject owner, TextureRegionInfo regionInfo,
                           float speed, float damage,
                           float x, float y, float width, float height) {
        super(game, regionInfo, speed, x, y, width, height);
        this.damage = damage;
        this.owner = owner;
        this.killer = null;
        this.ownerSafe = true;
    }

    public void hit(InGameObject object) {
        if (owner.equals(object) && ownerSafe) {
            return;
        }

        if (object instanceof ActiveObject) {
            ((ActiveObject)object).damage(damage);
        }

        if (killer == null) {
            killer = object;
        }
        kill();
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

    @Override
    public void contactPreSolve(Contact contact, Manifold oldManifold, InGameObject object) {
        super.contactPreSolve(contact, oldManifold, object);

        if (isKilled() && !object.equals(killer) || object.equals(owner) && ownerSafe) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void contactPostSolve(Contact contact, ContactImpulse impulse, InGameObject object) {
        super.contactPostSolve(contact, impulse, object);

        if (isKilled() && !object.equals(killer) || object.equals(owner) && ownerSafe) {
            contact.setEnabled(false);
        }
    }
}
