package com.company.todd.game.process;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import com.company.todd.game.InGameObject;
import com.company.todd.game.active_objs.ActiveObject;
import com.company.todd.game.static_objs.StaticObject;
import com.company.todd.input.InGameInputHandler;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

import java.util.Iterator;

public class GameProcess implements Process {  // TODO GameProcess
    private float gravity;
    private float maxFallSpeed;

    private final ToddEthottGame game;
    private final InGameInputHandler inputHandler;
    private final MyScreen screen;

    private Array<ActiveObject> activeObjects;
    private Array<StaticObject> staticObjects;
    private Array<InGameObject> justCreatedObjects;

    public GameProcess(ToddEthottGame game, MyScreen screen) {
        this.game = game;
        this.screen = screen;

        inputHandler = new InGameInputHandler();

        gravity = 0.5f;
        maxFallSpeed = 10;

        activeObjects = new Array<ActiveObject>();
        staticObjects = new Array<StaticObject>();
        justCreatedObjects = new Array<InGameObject>();
    }

    private void handleInput(float delta) {
        if (Gdx.input.justTouched()) {
            if (inputHandler.isPaused()) {
                screen.pause();
            }
        }
    }

    @Override
    public void update(float delta) {
        Iterator<InGameObject> objectIterator = justCreatedObjects.iterator();
        while (objectIterator.hasNext()) {
            InGameObject object = objectIterator.next();

            if (object instanceof ActiveObject) {
                activeObjects.add((ActiveObject)object);
            }
            else if (object instanceof StaticObject) {
                staticObjects.add((StaticObject) object);
            }

            objectIterator.remove();
        }

        handleInput(delta);

        for (ActiveObject object : activeObjects) {
            object.update(delta);
        }
        for (StaticObject object : staticObjects) {
            object.update(delta);
        }
    }

    public void addObject(InGameObject object) {
        justCreatedObjects.add(object);
    }

    public void handleCollisions(ActiveObject object) {
        for (StaticObject staticObject : staticObjects) {
            object.collideWith(staticObject);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (ActiveObject object : activeObjects) {
            object.draw(batch, screen.getCameraRect());
        }
        for (StaticObject object : staticObjects) {
            object.draw(batch, screen.getCameraRect());
        }
    }

    public float getGravity() {
        return gravity;
    }

    public float getMaxFallSpeed() {
        return maxFallSpeed;
    }

    @Override
    public void dispose() {
        for (ActiveObject object : activeObjects) {
            object.dispose();
        }
        for (StaticObject object : staticObjects) {
            object.dispose();
        }
        for (InGameObject object : justCreatedObjects) {
            object.dispose();
        }
    }
}
