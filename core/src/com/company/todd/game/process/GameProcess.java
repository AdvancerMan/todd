package com.company.todd.game.process;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.company.todd.game.contact.MyContactListener;
import com.company.todd.game.level.Level;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.game.objs.active_objs.creatures.Player;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.game.input.InGameInputHandler;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

import java.util.Iterator;

public class GameProcess implements Process {  // TODO GameProcess
    public static final float metersPerPix = 1f / 30;

    protected final ToddEthottGame game;
    protected final InGameInputHandler inputHandler;
    protected final MyScreen screen;
    protected final MyContactListener contactListener;

    protected World world;
    protected Player player;
    protected Array<Creature> creatures;
    protected Array<DangerousObject> dangerousObjects;
    protected Array<StaticObject> staticObjects;
    protected Array<InGameObject> justCreatedObjects;

    public GameProcess(ToddEthottGame game, MyScreen screen, Level level) {  // TODO level + save
        this.game = game;
        this.screen = screen;

        world = new World(new Vector2(0, -20f), false);  // TODO optimize gravity
        contactListener = new MyContactListener(this);

        inputHandler = new InGameInputHandler();

        // TODO player in GameProcess
        player = new Player(game, game.regionInfos.getRegionInfo("player"), inputHandler, 500, 500, 50, 100);

        creatures = new Array<Creature>();
        dangerousObjects = new Array<DangerousObject>();
        staticObjects = new Array<StaticObject>();
        justCreatedObjects = new Array<InGameObject>();

        justCreatedObjects.add(player);
        setLevel(level);
    }

    public void setLevel(Level level) {  // TODO GameProcess.setLevel()
        level.unpackTo(justCreatedObjects);
    }

    protected void handleInput() {
        if (inputHandler.isPaused()) {
            screen.pause();
        }
    }

    public void resizeInputHandler(int width, int height) {
        inputHandler.resize(width, height);
    }

    protected void addJustCreatedObjectsToProcess() {
        Iterator<InGameObject> objectIterator = justCreatedObjects.iterator();
        while (objectIterator.hasNext()) {
            InGameObject object = objectIterator.next();
            object.init(this);

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

    protected void updateObjectsFrom(Array<? extends InGameObject> objects, float delta) {
        for (InGameObject object : objects) {
            if (!object.isKilled()) {
                object.update(delta);
            }
        }
    }

    @Override
    public void update(float delta) {
        addJustCreatedObjectsToProcess();
        handleInput();

        // TODO for (delta * 60 times) world.step(1f / 60, ..., ...) (does it optimize the game?)
        world.step(delta, 10, 10);  // TODO optimize iterations for world.step()

        updateObjectsFrom(creatures, delta);
        updateObjectsFrom(dangerousObjects, delta);
        updateObjectsFrom(staticObjects, delta);

        screen.centerCameraAt(player);
    }

    public void addObject(InGameObject object) {
        justCreatedObjects.add(object);
    }

    protected void drawObjectsFrom(Array<? extends InGameObject> objects, SpriteBatch batch, Rectangle cameraRect) {
        Iterator<? extends InGameObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            InGameObject object = iterator.next();

            object.draw(batch, cameraRect);
            if (object.isKilled()) {
                object.destroyBody();
                object.dispose();
                iterator.remove();
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        drawObjectsFrom(creatures, batch, screen.getCameraRect());
        drawObjectsFrom(dangerousObjects, batch, screen.getCameraRect());
        drawObjectsFrom(staticObjects, batch, screen.getCameraRect());
    }

    public World getWorld() {
        return world;
    }

    public Vector2 getGravity() {
        return world.getGravity();
    }

    protected void disposeObjectsFrom(Array<? extends InGameObject> objects) {
        for (InGameObject object : objects) {
            if (!object.isKilled()) {
                object.dispose();
            }
        }
    }

    @Override
    public void dispose() {
        world.dispose();

        disposeObjectsFrom(creatures);
        disposeObjectsFrom(dangerousObjects);
        disposeObjectsFrom(staticObjects);
        disposeObjectsFrom(justCreatedObjects);
    }
}
