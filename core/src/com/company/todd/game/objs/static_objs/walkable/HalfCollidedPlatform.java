package com.company.todd.game.objs.static_objs.walkable;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.util.FloatCmp;

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
        this(game, gameProcess, animation, x, y, width, height, toPix(width), toPix(height));
    }

    @Override
    public void beginContact(Contact contact, InGameObject object) {
        super.beginContact(contact, object);

        Rectangle aabb = getBodyAABB();
        if (!contact.getWorldManifold().getNormal().isPerpendicular(new Vector2(1, 0)) ||
                FloatCmp.less(contact.getWorldManifold().getPoints()[0].y, aabb.y + aabb.height)
        ) {
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
