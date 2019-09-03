package com.company.todd.game.objs.static_objs;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.launcher.ToddEthottGame;

public class Jumper extends HalfCollidedPlatform {
    private float power;

    public Jumper(ToddEthottGame game, MyAnimation animation,
                  float power, float x, float y, float width, float height) {
        super(game, animation, x, y, width, height);

        this.power = power;
    }

    @Override
    public void beginContact(Contact contact, InGameObject object) {
        super.beginContact(contact, object);

        // TODO animation of jumping
        if (object instanceof ActiveObject && !notCollidingObjects.contains(object, false)) {
            System.out.println("kek");
            object.setYVelocity(power);
        }
    }
}
