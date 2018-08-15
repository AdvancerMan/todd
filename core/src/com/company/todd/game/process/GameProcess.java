package com.company.todd.game.process;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.game.objs.active_objs.creatures.Player;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.objs.static_objs.StaticObject;
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

    private Player player;
    private Array<Creature> creatures;
    private Array<DangerousObject> dangerousObjects;
    private Array<StaticObject> staticObjects;
    private Array<InGameObject> justCreatedObjects;

    public GameProcess(ToddEthottGame game, MyScreen screen) {  // TODO level + save
        this.game = game;
        this.screen = screen;

        inputHandler = new InGameInputHandler();

        gravity = 9.8f;  // TODO gravity
        maxFallSpeed = 150;  // TODO maxFallSpeed

        // TODO player in GameProcess
        player = new Player(game, this, game.regionInfos.getRegionInfo("player"), inputHandler, 500, 500, 50, 100);

        creatures = new Array<Creature>();
        creatures.add(player);
        dangerousObjects = new Array<DangerousObject>();
        staticObjects = new Array<StaticObject>();
        justCreatedObjects = new Array<InGameObject>();
    }

    private void handleInput(float delta) {
        inputHandler.setNewTouchPosition();

        if (inputHandler.isPaused()) {
            screen.pause();
        }
    }

    private void addJustCreatedObjectsToProcess() {
        Iterator<InGameObject> objectIterator = justCreatedObjects.iterator();
        while (objectIterator.hasNext()) {
            InGameObject object = objectIterator.next();

            if (object instanceof Creature) {
                creatures.add((Creature) object);
            }
            else if (object instanceof DangerousObject) {
                dangerousObjects.add((DangerousObject) object);
            }
            else if (object instanceof StaticObject) {
                staticObjects.add((StaticObject) object);
            }

            objectIterator.remove();
        }
    }

    @Override
    public void update(float delta) {
        addJustCreatedObjectsToProcess();
        handleInput(delta);

        for (Creature creature : creatures) {
            creature.update(delta);
        }

        for (DangerousObject object : dangerousObjects) {
            object.update(delta);
        }

        for (StaticObject object : staticObjects) {
            object.update(delta);
        }

        screen.centerCameraAt(player);
    }

    public void addObject(InGameObject object) {
        justCreatedObjects.add(object);
    }

    public void handleCollisions(ActiveObject object, float delta) {
        for (StaticObject staticObject : staticObjects) {
            object.collideWith(staticObject, delta);
        }
    }

    private void drawObjectsFrom(Array<? extends InGameObject> objects, SpriteBatch batch, Rectangle cameraRect) {
        Iterator<? extends InGameObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            InGameObject object = iterator.next();

            object.draw(batch, cameraRect);
            if (object.isKilled()) {
                object.dispose();
                iterator.remove();
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        /*
        long startTime;
        if (ToddEthottGame.DEBUG) {
            startTime = System.currentTimeMillis();
        }
        */

        drawObjectsFrom(creatures, batch, screen.getCameraRect());

        drawObjectsFrom(dangerousObjects, batch, screen.getCameraRect());

        drawObjectsFrom(staticObjects, batch, screen.getCameraRect());

        /*
        if (ToddEthottGame.DEBUG) {
            System.out.print("draw all: ");
            System.out.println((double) (System.currentTimeMillis() - startTime) / 1000.);
        }
        */
    }

    public float getGravity() {
        return gravity;
    }

    public float getMaxFallSpeed() {
        return maxFallSpeed;
    }

    @Override
    public void dispose() {
        for (Creature creature : creatures) {
            if (!creature.isKilled()) {
                creature.dispose();
            }
        }
        for (DangerousObject object : dangerousObjects) {
            if (!object.isKilled()) {
                object.dispose();
            }
        }
        for (StaticObject object : staticObjects) {
            if (!object.isKilled()) {
                object.dispose();
            }
        }
        for (InGameObject object : justCreatedObjects) {
            if (!object.isKilled()) {
                object.dispose();
            }
        }
    }
}
