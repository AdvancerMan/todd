package com.company.todd.game.objs.static_objs;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.util.FloatCmp;

import static com.company.todd.game.process.GameProcess.toPix;

public abstract class StaticObject extends InGameObject {
    public StaticObject(ToddEthottGame game, MyAnimation animation, BodyInfo bodyInfo, Vector2 spriteSize) {
        super(game, BodyDef.BodyType.StaticBody, animation, bodyInfo, spriteSize);
    }

    public StaticObject(ToddEthottGame game, MyAnimation animation, float x, float y, float width, float height) {
        super(game, BodyDef.BodyType.StaticBody, animation, x, y, width, height);
    }

    public StaticObject(ToddEthottGame game, MyAnimation animation,
                        float x, float y, float bodyWidth, float bodyHeight,
                        float spriteWidth, float spriteHeight) {
        super(game, BodyDef.BodyType.StaticBody, animation, x, y, bodyWidth, bodyHeight, spriteWidth, spriteHeight);
    }

    @Override
    public boolean isGroundFor(Contact contact, InGameObject object) {
        Vector2[] points = contact.getWorldManifold().getPoints();
        Rectangle rect = getBodyAABB();

        for (int i = 0; i < contact.getWorldManifold().getNumberOfContactPoints(); i++) {
            toPix(points[i]);
            if (!(FloatCmp.equals(points[i].y, rect.y + rect.height, 1))) {
                return false;
            }
        }
        return true;
    }
}
