package com.company.todd.game.objs.active_objs.dangerous.rays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.company.todd.box2d.BodyCreator;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.box2d.BodyCreator.createBody;

public abstract class DangerousRay extends DangerousObject implements RayCastCallback {
    public static final float MIN_RAY_LENGTH = .03f;

    private Vector2 startPosition, maxEndPosition, realEndPosition;
    private boolean interactWithAllColliding;
    private boolean keepAlive;

    protected Vector2 firstObjectPosition;
    protected InGameObject firstObject;

    // TODO draw start and end animations
    protected DangerousRay(ToddEthottGame game, GameProcess gameProcess,
                           InGameObject owner, MyAnimation animation,
                           float damage, float flowLength, boolean interactWithAllColliding,
                           float x0, float y0,
                           float angle, float spriteWidth) {
        super(game, gameProcess, owner, animation, 0, damage, x0, y0, spriteWidth, 0);

        this.interactWithAllColliding = interactWithAllColliding;
        startPosition = new Vector2(x0, y0);
        maxEndPosition = new Vector2(Math.max(MIN_RAY_LENGTH, flowLength), 0).setAngleRad(angle).add(startPosition);
        realEndPosition = new Vector2(startPosition);
        createMyBody(); // body was not created because vectors were nulls

        firstObject = null;
        firstObjectPosition = new Vector2(maxEndPosition);

        keepAlive = true;
    }

    // FIXME remove that if == null (idk how to fix)
    @Override
    protected void createMyBody() {
        if (startPosition == null || realEndPosition == null) {
            return;
        }
        Body body = createBody(gameProcess.getWorld(), BodyDef.BodyType.StaticBody, new Vector2());
        BodyCreator.addEdge(body, startPosition, realEndPosition);
        body.setActive(false);
        body.setUserData(this);
        setBody(body);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        rayCast();

        realEndPosition = new Vector2(firstObjectPosition);
        if (!interactWithAllColliding) {
            setSize(realEndPosition.len(), 0);
        }

        if (realEndPosition.dst2(startPosition) < MIN_RAY_LENGTH * MIN_RAY_LENGTH) {
            realEndPosition.set(maxEndPosition).sub(startPosition).setLength(MIN_RAY_LENGTH).add(startPosition);
        }

        if (firstObject != null && !interactWithAllColliding) {
            interactWith(firstObject);
        }

        // FIXME object should not live for 1 frame
        if (!keepAlive) {
            kill();
        }
        keepAlive = false;
    }

    private void rayCast() {
        realEndPosition.set(maxEndPosition);
        firstObject = null;
        gameProcess.getWorld().rayCast(this, startPosition, maxEndPosition);
    }

    @Override
    public void setPosition(float x, float y, boolean resetSpeed) {
        maxEndPosition.sub(startPosition);
        startPosition.set(x, y);
        maxEndPosition.add(startPosition);
    }

    public void setAngle(float angle) {
        maxEndPosition.sub(startPosition).setAngleRad(angle).add(startPosition);
    }

    @Override
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        super.draw(batch, cameraRectangle);
        // TODO draw for Flow
    }

    @Override
    public void takeDamage(float amount) {}

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        InGameObject object = (InGameObject) fixture.getBody().getUserData();

        // FIXME if victim body is not active collision should not be reported

        if (firstObjectPosition.dst2(startPosition) > point.dst2(startPosition)) {
            firstObjectPosition = point.cpy();
            firstObject = object;
        }

        if (interactWithAllColliding) {
            interactWith(object);
        }

        return 1;
    }

    private void keepAlive() {
        keepAlive = true;
    }

    protected abstract void interactWith(InGameObject object);
}
