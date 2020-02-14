package com.company.todd.game.objs.static_objs.walkable;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.util.FloatCmp;
import com.company.todd.util.GeometrySolver;

public class ViscousPlatform extends HalfCollidedPlatform {
    private float maxObjectSpeed;
    private float platformBottom;

    public ViscousPlatform(ToddEthottGame game, GameProcess gameProcess, MyAnimation animation,
                           float maxObjectSpeed,
                           float x, float y,
                           float bodyWidth, float bodyHeight,
                           float spriteWidth, float spriteHeight) {
        super(game, gameProcess, animation, x, y, bodyWidth, bodyHeight, spriteWidth, spriteHeight);
        this.maxObjectSpeed = -maxObjectSpeed;
        platformBottom = y;
    }

    @Override
    public void contactPreSolve(Contact contact, Manifold oldManifold, InGameObject object) {
        super.contactPreSolve(contact, oldManifold, object);

        contact.setEnabled(false);

        float objectSpeed = object.getBody().getLinearVelocity().y;
        float objectBottom = object.getBodyAABB().y;
        if (platformBottom < objectBottom && FloatCmp.less(objectSpeed, 0)) {
            object.setYVelocity(maxObjectSpeed +
                    (objectSpeed - maxObjectSpeed) / GeometrySolver.goldenRatio);
        }
    }

    @Override
    public boolean isGroundFor(Contact contact, InGameObject object) {
        if (FloatCmp.more(object.getBody().getLinearVelocity().y, 0, 1f / 30)) {
            return false;
        }

        Rectangle platformRect = getBodyAABB();
        Rectangle objectRect = object.getBodyAABB();

        for (int i = 0; i < contact.getWorldManifold().getNumberOfContactPoints(); i++) {
            if (!(FloatCmp.moreOrEquals(objectRect.x + objectRect.width, platformRect.x, 1f / 30) &&
                    FloatCmp.lessOrEquals(objectRect.x,
                                        platformRect.x + platformRect.width, 1f / 30) &&
                    FloatCmp.moreOrEquals(objectRect.y, platformRect.y, 1f / 30) &&
                    FloatCmp.lessOrEquals(objectRect.y,
                                        platformRect.y + platformRect.height, 1f / 30))) {
                return false;
            }
        }
        return true;
    }
}
