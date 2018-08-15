package com.company.todd.game.active_objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.company.todd.game.active_objs.dangerous.Bullet;
import com.company.todd.game.process.GameProcess;
import com.company.todd.game.InGameObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

import static com.company.todd.util.FloatCmp.less;
import static com.company.todd.util.FloatCmp.lessOrEquals;
import static com.company.todd.util.FloatCmp.more;
import static com.company.todd.util.FloatCmp.moreOrEquals;

public abstract class ActiveObject extends InGameObject { // TODO animation
    protected Vector2 velocity;
    protected float walkingSpeed;
    protected float runningSpeed;

    protected TextureRegionInfo regionInfo;

    public ActiveObject(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
                        float walkingSpeed, float runningSpeed,
                        float x, float y, float width, float height) {
        super(game, gameProcess, x, y, width, height);
        this.regionInfo = regionInfo;
        this.sprite.setRegion(regionInfo.getTextureRegion());

        velocity = new Vector2(0, 0);

        this.walkingSpeed = walkingSpeed;
        this.runningSpeed = runningSpeed;
    }

    public ActiveObject(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
                        float speed,
                        float x, float y, float width, float height) {
        this(game, gameProcess, regionInfo, speed, speed, x, y, width, height);
    }

    public void fall(float gravity) {
        velocity.add(0, -gravity);

        if (less(velocity.y, -gameProcess.getMaxFallSpeed())) {
            velocity.set(velocity.x, -gameProcess.getMaxFallSpeed());
        }
    }

    public void stand() {
        velocity.set(0, velocity.y);
    }

    public void walk(boolean toRight) {
        if (toRight) {
            velocity.set(walkingSpeed, velocity.y);
        }
        else {
            velocity.set(-walkingSpeed, velocity.y);
        }
    }

    public void run(boolean toRight) { // TODO energy consuming: run()
        if (toRight) {
            velocity.set(runningSpeed, velocity.y);
        }
        else {
            velocity.set(-runningSpeed, velocity.y);
        }
    }

    public void shoot() {  // TODO shoot()
        gameProcess.addObject(
                new Bullet(
                        game, gameProcess,
                        game.regionInfos.getRegionInfo("grassPlatformDown"),
                        getRect().getX(), getRect().getY(), 100, true
                        )
        );
    }

    @Override
    public void update(float delta) {
        gameProcess.handleCollisions(this, delta);

        updatePosition(delta);
    }

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
     */
    public boolean collideWith(InGameObject object, float delta) {
        if (!object.isCollidable()) {
            return false;
        }

        Rectangle objectRect = object.getRect();
        Rectangle thisRect = this.getRect();

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

    protected void updatePosition(float delta) {
        sprite.translate(velocity.x * delta, velocity.y * delta);
    }

    @Override
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        if (getRect().overlaps(cameraRectangle)) {
            sprite.draw(batch);
        }
    }

    @Override
    public void dispose() {
        regionInfo.dispose();
    }
}
