package com.company.todd.game.objs.active_objs.dangerous.flows;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.box2d.BodyCreator.createBody;
import static com.company.todd.game.process.GameProcess.toMeters;

public abstract class DangerousFlow extends DangerousObject {
    public static final float MIN_FLOW_LENGTH = 1;

    private Vector2 startPosition, endPosition, resultingEndPosition;
    private FlowRayCastCallback callback;

    // TODO draw start and end animations
    public DangerousFlow(ToddEthottGame game, InGameObject owner, MyAnimation animation,
                         float damage, float flowLength, float x0, float y0, float angle, float spriteWidth) {
        super(game, owner, animation, 0, damage, x0, y0, spriteWidth, 0);

        callback = new FlowRayCastCallback();

        startPosition = new Vector2(x0, y0);
        endPosition = new Vector2(Math.max(MIN_FLOW_LENGTH, flowLength), 0).setAngleRad(angle).add(startPosition);
        resultingEndPosition = new Vector2(endPosition);
    }

    @Override
    protected void createMyBody() {
        Body body = createBody(gameProcess.getWorld(), BodyDef.BodyType.StaticBody, new Vector2());
        body.setActive(false);
        body.setUserData(this);
        setBody(body);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        rayCast();

        resultingEndPosition = callback.getIntersectionPoint();
        if (resultingEndPosition.dst2(startPosition) < MIN_FLOW_LENGTH * MIN_FLOW_LENGTH) {
            resultingEndPosition.sub(startPosition).setLength(MIN_FLOW_LENGTH).add(startPosition);
        }

        interactWith(callback.getIntersectedObject());
    }

    private void rayCast() {
        gameProcess.getWorld().rayCast(callback,
                toMeters(new Vector2(startPosition)),
                toMeters(new Vector2(endPosition)));
    }

    @Override
    public void setPosition(float x, float y) {
        endPosition.sub(startPosition);
        startPosition.set(x, y);
        endPosition.add(startPosition);
    }

    public void setAngle(float angle) {
        endPosition.sub(startPosition).setAngleRad(angle).add(startPosition);
    }

    @Override
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        super.draw(batch, cameraRectangle);
        // TODO draw for Flow
    }

    @Override
    public void takeDamage(float amount) {}

    protected abstract void interactWith(InGameObject object);
}
