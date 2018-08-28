package com.company.todd.game.contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.process.GameProcess;

public class MyContactListener implements ContactListener {  // TODO ContactListener
    protected GameProcess gameProcess;

    public MyContactListener(GameProcess gameProcess) {
        this.gameProcess = gameProcess;
        gameProcess.getWorld().setContactListener(this);
    }

    @Override
    public void beginContact(Contact contact) {  // TODO check DangerousObject
        InGameObject objectA = (InGameObject) contact.getFixtureA().getBody().getUserData();
        InGameObject objectB = (InGameObject) contact.getFixtureB().getBody().getUserData();

        if (objectA instanceof DangerousObject) {
            ((DangerousObject) objectA).hit(objectB);
        } else if (objectB instanceof DangerousObject) {
            ((DangerousObject) objectB).hit(objectA);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
