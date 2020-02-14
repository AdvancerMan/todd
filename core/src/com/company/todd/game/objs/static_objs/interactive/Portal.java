package com.company.todd.game.objs.static_objs.interactive;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import java.util.Iterator;

public class Portal extends StaticObject {
    private float toX, toY, maxDelay;
    private ArrayMap<InGameObject, Float> delays;

    public Portal(ToddEthottGame game, GameProcess gameProcess, MyAnimation animation,
                  float x, float y, float toX, float toY, float maxDelay,
                  float bodyRadius, float spriteWidth, float spriteHeight) {
        super(game, gameProcess, animation, x, y, bodyRadius, spriteWidth, spriteHeight);
        this.toX = toX;
        this.toY = toY;
        this.maxDelay = maxDelay;
        delays = new ArrayMap<>();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (int i = 0; i < delays.size; i++) {
            delays.setValue(i, delays.getValueAt(i) + delta);
        }

        for (Iterator<ObjectMap.Entry<InGameObject, Float>> it = delays.iterator(); it.hasNext();) {
            ObjectMap.Entry<InGameObject, Float> entry = it.next();
            if (entry.value > maxDelay) {
                entry.key.setPosition(toX, toY, false);
                it.remove();
            }
        }
    }

    @Override
    public void beginContact(Contact contact, InGameObject object) {
        super.beginContact(contact, object);
        if (!delays.containsKey(object)) {
            delays.put(object, 0f);
        }
    }

    @Override
    public void contactPreSolve(Contact contact, Manifold oldManifold, InGameObject object) {
        super.contactPreSolve(contact, oldManifold, object);
        contact.setEnabled(false);
    }
}
