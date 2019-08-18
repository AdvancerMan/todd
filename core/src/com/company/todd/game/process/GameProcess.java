package com.company.todd.game.process;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.company.todd.game.animations.MyAnimation;
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
import com.company.todd.texture.TextureRegionInfo;

import java.util.Iterator;

public class GameProcess implements Process {  // TODO GameProcess
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

        createPlayer();

        creatures = new Array<Creature>();
        dangerousObjects = new Array<DangerousObject>();
        staticObjects = new Array<StaticObject>();
        justCreatedObjects = new Array<InGameObject>();

        justCreatedObjects.add(player);
        setLevel(level);
    }

    private void createPlayer() { // TODO player in GameProcess
        player = new Player(game,
                game.animationInfos.getAnimation("player"),
                game.animationInfos.getAnimation("playerHands"),
                new Rectangle(50, 40, 20, 20),
                inputHandler, 500, 500, 50, 100);
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

    protected void updateObjectsFrom(Array<? extends InGameObject> objects, float delta) {
        for (InGameObject object : objects) {
            if (!object.isKilled()) {
                object.update(delta);
            }
        }
    }

    @Override
    public void update(float delta) {
        handleInput();

        updateObjectsFrom(dangerousObjects, delta);
        updateObjectsFrom(creatures, delta);
        updateObjectsFrom(staticObjects, delta);

        addJustCreatedObjectsToProcess();

        // TODO for (delta * 60 times) world.step(1f / 60, ..., ...) (does it optimize the game?)
        world.step(delta, 10, 10);  // TODO optimize iterations for world.step()

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
                object.dispose();
                iterator.remove();
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        drawObjectsFrom(staticObjects, batch, screen.getCameraRect());
        drawObjectsFrom(creatures, batch, screen.getCameraRect());
        drawObjectsFrom(dangerousObjects, batch, screen.getCameraRect());
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
                object.dispose();
            }
        }
    }

    @Override
    public void dispose() {
        disposeObjectsFrom(creatures);
        disposeObjectsFrom(dangerousObjects);
        disposeObjectsFrom(staticObjects);
        disposeObjectsFrom(justCreatedObjects);

        world.dispose();
    }

    private static final float metersPerPix = 1f / 30;

    public static float toPix(float value) {
        return value / metersPerPix;
    }

    public static Vector2 toPix(Vector2 vector) {
        return vector.scl(1f / metersPerPix);
    }

    public static Rectangle toPix(Rectangle rect) {
        rect.x /= metersPerPix;
        rect.y /= metersPerPix;
        rect.width /= metersPerPix;
        rect.height /= metersPerPix;
        return rect;
    }

    public static float toMeters(float value) {
        return value * metersPerPix;
    }

    public static Vector2 toMeters(Vector2 vector) {
        return vector.scl(metersPerPix);
    }

    public static Rectangle toMeters(Rectangle rect) {
        rect.x *= metersPerPix;
        rect.y *= metersPerPix;
        rect.width *= metersPerPix;
        rect.height *= metersPerPix;
        return rect;
    }

}
