package com.company.todd.game.objs.static_objs.interactive;

import com.badlogic.gdx.physics.box2d.Contact;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.objs.static_objs.walkable.HalfCollidedPlatform;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public class Jumper extends HalfCollidedPlatform {
    private float power;

    public Jumper(ToddEthottGame game, GameProcess gameProcess, MyAnimation animation,
                  float power, float x, float y, float width, float height) {
        super(game, gameProcess, animation, x, y, width, height);

        this.power = power;
    }

    @Override
    public void beginContact(Contact contact, InGameObject object) {
        super.beginContact(contact, object);

        // TODO animation of jumping
        if (object instanceof ActiveObject && !notCollidingObjects.contains(object, false)) {
            object.setYVelocity(power);
        }
    }

    @Override
    public boolean isGroundFor(Contact contact, InGameObject object) {
        return false;
    }
}
