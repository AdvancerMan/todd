package com.company.todd.game.objs.static_objs;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.util.FloatCmp;
import com.company.todd.util.GeometrySolver;

public class ViscousPlatform extends HalfCollidedPlatform {
    private float maxObjectSpeed;
    private float platformBottom;

    public ViscousPlatform(ToddEthottGame game, MyAnimation animation,
                           float maxObjectSpeed,
                           float x, float y,
                           float bodyWidth, float bodyHeight,
                           float spriteWidth, float spriteHeight) {
        super(game, animation, x, y, bodyWidth, bodyHeight, spriteWidth, spriteHeight);
        this.maxObjectSpeed = -maxObjectSpeed;
        platformBottom = y;
    }

    @Override
    public void contactPreSolve(Contact contact, Manifold oldManifold, InGameObject object) {
        super.contactPreSolve(contact, oldManifold, object);

        contact.setEnabled(false);

        float objectSpeed = object.getLinearVelocity().y;
        float objectBottom = object.getBodyAABB().y;
        if (platformBottom < objectBottom && FloatCmp.less(objectSpeed, 0)) {
            System.out.println(objectSpeed + " " + maxObjectSpeed);
            object.setYVelocity(maxObjectSpeed +
                    (objectSpeed - maxObjectSpeed) / GeometrySolver.goldenRatio);
        }
    }

    @Override
    public boolean isGroundFor(Contact contact, InGameObject object) {
        if (FloatCmp.more(object.getLinearVelocity().y, 0, 1)) {
            return false;
        }

        Rectangle platformRect = getBodyAABB();
        Rectangle objectRect = object.getBodyAABB();

        for (int i = 0; i < contact.getWorldManifold().getNumberOfContactPoints(); i++) {
            if (!(FloatCmp.moreOrEquals(objectRect.x + objectRect.width, platformRect.x, 1) &&
                    FloatCmp.lessOrEquals(objectRect.x,
                                        platformRect.x + platformRect.width, 1) &&
                    FloatCmp.moreOrEquals(objectRect.y, platformRect.y, 1) &&
                    FloatCmp.lessOrEquals(objectRect.y,
                                        platformRect.y + platformRect.height, 1))) {
                return false;
            }
        }
        return true;
    }
}
