package com.company.todd.game.objs.static_objs;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.game.process.GameProcess.toPix;

public class HalfCollidedPlatform extends StaticObject {
    protected Array<InGameObject> notCollidingObjects;

    public HalfCollidedPlatform(ToddEthottGame game, GameProcess gameProcess, MyAnimation animation,
                                float x, float y,
                                float bodyWidth, float bodyHeight,
                                float spriteWidth, float spriteHeight) {
        super(game, gameProcess, animation, x, y, bodyWidth, bodyHeight, spriteWidth, spriteHeight);

        notCollidingObjects = new Array<InGameObject>();
    }

    public HalfCollidedPlatform(ToddEthottGame game, GameProcess gameProcess, MyAnimation animation,
                                float x, float y, float width, float height) {
        this(game, gameProcess, animation, x, y, width, height, width, height);
    }

    @Override
    public void beginContact(Contact contact, InGameObject object) {
        super.beginContact(contact, object);

        float normalAngle = contact.getWorldManifold().getNormal().angle();

        if (!(89f < normalAngle && normalAngle < 91f)) {  // TODO normal angle is bad
            notCollidingObjects.add(object);
        }
    }

    @Override
    public void contactPreSolve(Contact contact, Manifold oldManifold, InGameObject object) {
        super.contactPreSolve(contact, oldManifold, object);

        if (notCollidingObjects.contains(object, false)) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void endContact(Contact contact, InGameObject object) {
        super.endContact(contact, object);

        while (notCollidingObjects.removeValue(object, false));  // TODO optimize
    }
}
