package com.company.todd.game.process;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.company.todd.game.contact.MyContactListener;
import com.company.todd.game.input.InGameInputHandler;
import com.company.todd.game.level.Level;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.game.objs.active_objs.creatures.friendly.Player;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

import java.util.Iterator;

public class GameProcess implements Process {  // TODO GameProcess
    public static long MAX_DISTANCE2_FROM_CENTER = 20000L * 20000L;

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

    private Level newLevel;

    public GameProcess(ToddEthottGame game, MyScreen screen, Level level) {  // TODO level + save
        this.game = game;
        this.screen = screen;

        world = new World(new Vector2(0, -20f), false);  // TODO optimize gravity
        contactListener = new MyContactListener(this);

        inputHandler = new InGameInputHandler();

        createPlayer();

        creatures = new Array<Creature>();
        dangerousObjects = new Array<DangerousObject>();
        staticObjects = new Array<StaticObject>();
        justCreatedObjects = new Array<InGameObject>();

        setLevel(level);
    }

    private void createPlayer() { // TODO player in GameProcess
        player = new Player(game,
                game.animationInfos.getAnimation("player"),
                game.animationInfos.getAnimation("playerHands"),
                new Rectangle(50, 40, 20, 20),
                inputHandler, 0, 0, 50, 100);
    }

    public void setLevel(Level level) {
        this.newLevel = level;
    }

    protected void setLevel() {
        disposeObjectsFrom(justCreatedObjects);
        disposeObjectsFrom(staticObjects);
        disposeObjectsFrom(dangerousObjects);

        creatures.removeValue(player, false);
        disposeObjectsFrom(creatures);

        newLevel.unpackTo(justCreatedObjects);
        justCreatedObjects.add(player);
        player.setPosition(0, 0);
        addJustCreatedObjectsToProcess();

        newLevel = null;
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
            object.setGameProcess(this);

            if (object instanceof Creature) {
                creatures.add((Creature) object);
            } else if (object instanceof DangerousObject) {
                dangerousObjects.add((DangerousObject) object);
            } else if (object instanceof StaticObject) {
                staticObjects.add((StaticObject) object);
            }

            objectIterator.remove();
        }
    }

    protected void checkLifeIn(Array<? extends InGameObject> objects) {
        Iterator<? extends InGameObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            InGameObject object = iterator.next();

            if (!object.isKilled() && object.getBodyPosition().dst2(0, 0) > MAX_DISTANCE2_FROM_CENTER) {
                object.kill();
            }

            if (object.isKilled()) {
                object.dispose();
                iterator.remove();
            }
        }
    }

    protected void updateObjectsFrom(Array<? extends InGameObject> objects, float delta) {
        for (InGameObject object : objects) {
            if (!object.isKilled()) {
                object.update(delta);
            }
        }
    }

    protected void preWorldUpdateObjectsFrom(Array<? extends InGameObject> objects, float delta) {
        for (InGameObject object : objects) {
            if (!object.isKilled()) {
                object.preWorldUpdate(delta);
            }
        }
    }

    protected void postWorldPreDrawUpdateObjectsFrom(Array<? extends InGameObject> objects, float delta) {
        for (InGameObject object : objects) {
            if (!object.isKilled()) {
                object.postWorldPreDrawUpdate(delta);
            }
        }
    }

    @Override
    public void preUpdate(float delta) {
        if (newLevel != null) {
            setLevel();
        }

        handleInput();

        checkLifeIn(dangerousObjects);
        checkLifeIn(creatures);
        checkLifeIn(staticObjects);

        // main update of objects
        updateObjectsFrom(dangerousObjects, delta);
        updateObjectsFrom(creatures, delta);
        updateObjectsFrom(staticObjects, delta);

        addJustCreatedObjectsToProcess();

        // preparations for world.step()
        preWorldUpdateObjectsFrom(dangerousObjects, delta);
        preWorldUpdateObjectsFrom(creatures, delta);
        preWorldUpdateObjectsFrom(staticObjects, delta);

        // TODO for (delta * 60 times) world.step(1f / 60, ..., ...) (does it optimize the game?)
        world.step(delta, 10, 10);  // TODO optimize iterations for world.step()

        // processing results of world.step()
        // preparing for drawing
        postWorldPreDrawUpdateObjectsFrom(dangerousObjects, delta);
        postWorldPreDrawUpdateObjectsFrom(creatures, delta);
        postWorldPreDrawUpdateObjectsFrom(staticObjects, delta);

        screen.centerCameraAt(player);
    }

    public void addObject(InGameObject object) {
        justCreatedObjects.add(object);
    }

    protected void drawObjectsFrom(Array<? extends InGameObject> objects, SpriteBatch batch, Rectangle cameraRect) {
        for (InGameObject object : objects) {
            object.draw(batch, cameraRect);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        drawObjectsFrom(staticObjects, batch, screen.getCameraRect());
        drawObjectsFrom(creatures, batch, screen.getCameraRect());
        drawObjectsFrom(dangerousObjects, batch, screen.getCameraRect());
    }

    protected void postDrawUpdateObjectsFrom(Array<? extends InGameObject> objects, float delta) {
        for (InGameObject object : objects) {
            if (!object.isKilled()) {
                object.postDrawUpdate(delta);
            }
        }
    }

    public void postUpdate(float delta) {
        // processing results of draw
        // making some conclusions of this iteration
        postDrawUpdateObjectsFrom(dangerousObjects, delta);
        postDrawUpdateObjectsFrom(creatures, delta);
        postDrawUpdateObjectsFrom(staticObjects, delta);
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
                object.kill();
            }
            object.dispose();
        }
        objects.clear();
    }

    @Override
    public void dispose() {
        disposeObjectsFrom(creatures);
        disposeObjectsFrom(dangerousObjects);
        disposeObjectsFrom(staticObjects);
        disposeObjectsFrom(justCreatedObjects);

        world.dispose();
    }

    private static final float METERS_PER_PIX = 1f / 30;

    /**
     * shouldn't be used as way to get pixels from meters or vice versa
     * @return METERS_PER_PIX constant
     */
    public static float getMetersPerPix() {
        return METERS_PER_PIX;
    }

    public static float toPix(float value) {
        return value / METERS_PER_PIX;
    }

    public static Vector2 toPix(Vector2 vector) {
        return vector.scl(1f / METERS_PER_PIX);
    }

    public static Rectangle toPix(Rectangle rect) {
        rect.x /= METERS_PER_PIX;
        rect.y /= METERS_PER_PIX;
        rect.width /= METERS_PER_PIX;
        rect.height /= METERS_PER_PIX;
        return rect;
    }

    public static Matrix4 toPix(Matrix4 matrix) {
        return matrix.scl(1 / METERS_PER_PIX);
    }

    public static float toMeters(float value) {
        return value * METERS_PER_PIX;
    }

    public static Vector2 toMeters(Vector2 vector) {
        return vector.scl(METERS_PER_PIX);
    }

    public static Rectangle toMeters(Rectangle rect) {
        rect.x *= METERS_PER_PIX;
        rect.y *= METERS_PER_PIX;
        rect.width *= METERS_PER_PIX;
        rect.height *= METERS_PER_PIX;
        return rect;
    }

    public static Matrix4 toMeters(Matrix4 matrix) {
        return matrix.scl(METERS_PER_PIX);
    }

}
