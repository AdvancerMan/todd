package com.company.todd.game.contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.process.GameProcess;

public class MyContactListener implements ContactListener {
    protected GameProcess gameProcess;

    public MyContactListener(GameProcess gameProcess) {
        this.gameProcess = gameProcess;
        gameProcess.getWorld().setContactListener(this);
    }

    @Override
    public void beginContact(Contact contact) {
        InGameObject objectA = (InGameObject) contact.getFixtureA().getBody().getUserData();
        InGameObject objectB = (InGameObject) contact.getFixtureB().getBody().getUserData();

        objectA.beginContact(contact, objectB);
        objectB.beginContact(contact, objectA);
    }

    @Override
    public void endContact(Contact contact) {
        InGameObject objectA = (InGameObject) contact.getFixtureA().getBody().getUserData();
        InGameObject objectB = (InGameObject) contact.getFixtureB().getBody().getUserData();

        objectA.endContact(contact, objectB);
        objectB.endContact(contact, objectA);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        InGameObject objectA = (InGameObject) contact.getFixtureA().getBody().getUserData();
        InGameObject objectB = (InGameObject) contact.getFixtureB().getBody().getUserData();

        objectA.contactPreSolve(contact, oldManifold, objectB);
        objectB.contactPreSolve(contact, oldManifold, objectA);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        InGameObject objectA = (InGameObject) contact.getFixtureA().getBody().getUserData();
        InGameObject objectB = (InGameObject) contact.getFixtureB().getBody().getUserData();

        objectA.contactPostSolve(contact, impulse, objectB);
        objectB.contactPostSolve(contact, impulse, objectA);
    }
}
