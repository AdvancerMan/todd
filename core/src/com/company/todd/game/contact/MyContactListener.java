package com.company.todd.game.contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import com.company.todd.game.process.GameProcess;

public class MyContactListener implements ContactListener {  // TODO ContactListener
    protected GameProcess gameProcess;

    public MyContactListener(GameProcess gameProcess) {
        this.gameProcess = gameProcess;
        gameProcess.getWorld().setContactListener(this);
    }

    @Override
    public void beginContact(Contact contact) {

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
