package com.company.todd.game.contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.process.GameProcess;

import java.net.CacheRequest;

public class MyContactListener implements ContactListener {  // TODO ContactListener
    protected GameProcess gameProcess;

    public MyContactListener(GameProcess gameProcess) {
        this.gameProcess = gameProcess;
        gameProcess.getWorld().setContactListener(this);
    }

    public void setWorld(World world) {
        world.setContactListener(this);
    }

    protected enum Side {
        top, down, left, right
    }

    protected Side getContactSide(Fixture fixtureA, Fixture fixtureB) {
        return Side.down;  // TODO getContactSide()
    }

    protected void checkCreatureOnGround(Fixture fixtureA, Fixture fixtureB, boolean fixturesContact) {
        InGameObject objectA = (InGameObject) fixtureA.getBody().getUserData();
        InGameObject objectB = (InGameObject) fixtureB.getBody().getUserData();

        if (!(objectA instanceof Creature)) {
            Object tmp = objectA;
            objectA = objectB;
            objectB = (InGameObject) tmp;

            tmp = fixtureA;
            fixtureA = fixtureB;
            fixtureB = (Fixture) tmp;
        }

        if (objectA instanceof Creature && objectB instanceof StaticObject && getContactSide(fixtureA, fixtureB) == Side.down) {
            ((Creature)objectA).setOnGround(fixturesContact);
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        checkCreatureOnGround(fixtureA, fixtureB, true);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        checkCreatureOnGround(fixtureA, fixtureB, false);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
