package com.company.todd.game.objs.active_objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.company.todd.game.objs.active_objs.dangerous.Bullet;
import com.company.todd.game.process.GameProcess;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

import static com.company.todd.util.FloatCmp.less;
import static com.company.todd.util.FloatCmp.lessOrEquals;
import static com.company.todd.util.FloatCmp.more;
import static com.company.todd.util.FloatCmp.moreOrEquals;

public abstract class ActiveObject extends InGameObject { // TODO animation
    protected float walkingSpeed;
    protected float runningSpeed;
    protected Vector2 force;

    protected TextureRegionInfo regionInfo;  // TODO delete this PLEASE

    public ActiveObject(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
                        float walkingSpeed, float runningSpeed,
                        float x, float y, float width, float height) {
        super(game, gameProcess, BodyDef.BodyType.DynamicBody, x, y, width, height);
        this.regionInfo = regionInfo;
        this.sprite.setRegion(regionInfo.getTextureRegion());

        this.walkingSpeed = walkingSpeed;
        this.runningSpeed = runningSpeed;
        this.force = new Vector2();
    }

    public ActiveObject(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
                        float speed,
                        float x, float y, float width, float height) {
        this(game, gameProcess, regionInfo, speed, speed, x, y, width, height);
    }

    public void walk(boolean toRight) {
        if (toRight) {
            force.set(walkingSpeed, force.y);
        }
        else {
            force.set(-walkingSpeed, force.y);
        }
    }

    public void run(boolean toRight) { // TODO energy consuming: run()
        if (toRight) {
            force.set(runningSpeed, force.y);
        }
        else {
            force.set(-runningSpeed, force.y);
        }
    }

    public void shoot() {  // TODO shoot()
        /*
        gameProcess.addObject(
                new Bullet(
                        game, gameProcess,
                        game.regionInfos.getRegionInfo("grassPlatformDown"),
                        getSpriteRect().getX(), getSpriteRect().getY(), 100, true
                        )
        );
        */
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        updatePosition(delta);
    }

    protected void updatePosition(float delta) {
        body.applyForceToCenter(force, true);
        force.set(0, 0);
    }

    @Override
    public void dispose() {
        regionInfo.dispose();
    }

/* dead code

    private static boolean segmentsIntersect(float x1, float size1, float x2, float size2) {
        return
                lessOrEquals(x1, x2) && more(x1 + size1, x2) ||
                        lessOrEquals(x1, x2 + size2 - 1) && more(x1 + size1, x2 + size2 - 1) ||

                        lessOrEquals(x2, x1) && more(x2 + size2, x1) ||
                        lessOrEquals(x2, x1 + size1 - 1) && more(x2 + size2, x1 + size1 - 1);
    }

    private static float calcCollisionTime(Rectangle activeRect, Rectangle staticRect, float vel, float yVel) {
        if (segmentsIntersect(activeRect.x, activeRect.width, staticRect.x, staticRect.width)) {
            return 2;
        }

        float time = 2;
        if (more(vel, 0) && lessOrEquals(activeRect.x + activeRect.width, staticRect.x)) {
            if (more(activeRect.x + activeRect.width + vel, staticRect.x)) {
                time = Math.abs((staticRect.x - (activeRect.x + activeRect.width)) / vel);

                if (!segmentsIntersect(staticRect.y, staticRect.height,
                        activeRect.y + yVel * time, activeRect.height)) {
                    time = 2;
                }
            }
        }
        else if (less(vel, 0) && moreOrEquals(activeRect.x, staticRect.x + staticRect.width)) {
            if (less(activeRect.x + vel, staticRect.x + staticRect.width)) {
                time = Math.abs((activeRect.x - (staticRect.x + staticRect.width)) / vel);

                if (!segmentsIntersect(staticRect.y, staticRect.height,
                        activeRect.y + yVel * time, activeRect.height)) {
                    time = 2;
                }
            }
        }

        return time;
    }

    /**
     *
     * @param object object to collide with
     * @param delta delta time between this frame and last frame
     * @return if this intersects with object
     *
    public boolean collideWith(InGameObject object, float delta) {
        Rectangle objectRect = object.getSpriteRect();
        Rectangle thisRect = this.getSpriteRect();

        float xTime = calcCollisionTime(thisRect, objectRect, velocity.x * delta, velocity.y * delta);

        // swapping x and y coordinates (+ width and height)
        // this action does not affect sprites' state
        objectRect.setPosition(objectRect.y, objectRect.x)
                .setSize(objectRect.height, objectRect.width);
        thisRect.setPosition(thisRect.y, thisRect.x)
                .setSize(thisRect.height, thisRect.width);

        float yTime = calcCollisionTime(thisRect, objectRect, velocity.y * delta, velocity.x * delta);

        if (FloatCmp.equals(xTime, yTime) && FloatCmp.equals(xTime, 2)) {
            return false;
        }
        else if (less(xTime, yTime)) {
            velocity.set(velocity.x * xTime, velocity.y);
        }
        else {
            velocity.set(velocity.x, velocity.y * yTime);
        }
        return true;
    }
*/

}
