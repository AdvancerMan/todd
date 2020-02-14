package com.company.todd.game.objs.base;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.ToddException;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.box2d.BodyCreator.addBox;
import static com.company.todd.box2d.BodyCreator.addCircle;
import static com.company.todd.box2d.BodyCreator.createBody;

public class MyBody implements Disposable {
    protected final ToddEthottGame game;
    protected GameProcess gameProcess;

    private Body body;
    private BodyInfo bodyInfo;  // FIXME delete this because you dont need it everywhere (or make static)
    private BodyDef.BodyType bodyType;

    public MyBody(ToddEthottGame game, GameProcess gameProcess,
                  BodyDef.BodyType bodyType, BodyInfo bodyInfo) {
        this.game = game;
        this.bodyType = bodyType;
        this.bodyInfo = bodyInfo;
        setGameProcess(gameProcess);
    }

    public boolean isGameProcessInitiated() {
        return gameProcess != null;
    }

    // TODO reset() to reset all parameters relative to the process (e.g. coordinates, health)
    public void setGameProcess(GameProcess gameProcess) {
        destroyBody();
        this.gameProcess = gameProcess;
        if (gameProcess != null) {
            createMyBody();
        }
    }

    private void createMyCircleBody(Vector2 pos, float radius) {
        Body body = createBody(gameProcess.getWorld(), bodyType, pos);
        addCircle(body, new Vector2(0, 0), radius);
        body.setUserData(this);

        setBody(body);
    }

    private void createMyRectangleBody(Vector2 pos, Vector2 size) {
        float width = size.x, height = size.y;
        float x = pos.x + width / 2, y = pos.y + height / 2;

        Body body = createBody(gameProcess.getWorld(), bodyType, new Vector2(x, y));
        addBox(body, width / 2, height / 2);  // TODO size / 2 ?
        body.setUserData(this);

        setBody(body);
    }

    /**
     * you should redefine this method in case
     * you want to create your own body
     */
    protected void createMyBody() {
        if (bodyInfo.getType().equals(Shape.Type.Circle)) {
            createMyCircleBody(bodyInfo.getPosition(), bodyInfo.getRadius());
        } else if (bodyInfo.getType().equals(Shape.Type.Polygon)) {
            createMyRectangleBody(bodyInfo.getPosition(), bodyInfo.getSize());
        } else {
            throw new ToddException("bodyInfo has incorrect type");
        }
    }

    protected void setBody(Body body) {
        destroyBody();
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public void applyLinearImpulseToCenter(Vector2 impulse) {
        body.applyLinearImpulse(impulse.cpy(), body.getWorldCenter(), true);
    }

    public void setVelocity(Vector2 v) {
        body.setLinearVelocity(v);
    }

    public void setYVelocity(float yVel) {
        body.setLinearVelocity(body.getLinearVelocity().x, yVel);
    }

    public void setXVelocity(float xVel) {
        body.setLinearVelocity(xVel, body.getLinearVelocity().y);
    }

    public void setCenterPosition(float x, float y, boolean resetLinearVelocity) {
        if (body != null) {
            body.setTransform(x, y, body.getAngle());
            if (resetLinearVelocity) {
                body.setLinearVelocity(0, 0);
            }
        }
    }

    public void setPosition(float x, float y, boolean resetSpeed) {
        if (body != null) {
            Rectangle aabb = getBodyAABB();
            setCenterPosition(x + aabb.width / 2, y + aabb.height / 2, resetSpeed);
        }
    }

    public void setBodyAngle(float angle, boolean resetAngularVelocity) {
        if (body != null) {
            if (resetAngularVelocity) {
                body.setAngularVelocity(0);
            }
            body.setTransform(body.getPosition(), angle);
        }
    }

    public void setSize(float width, float height) {  // FIXME weird logic
        bodyInfo.setSize(new Vector2(width, height));

        if (body != null) {
            Vector2 pos = body.getPosition();
            bodyInfo.setCenter(pos);
            destroyBody();
            createMyBody();
        }
    }

    private void updateCorners(Vector2 lCorner, Vector2 rCorner, Vector2 vertex) {
        lCorner.x = Math.min(lCorner.x, vertex.x);
        lCorner.y = Math.min(lCorner.y, vertex.y);
        rCorner.x = Math.max(rCorner.x, vertex.x);
        rCorner.y = Math.max(rCorner.y, vertex.y);
    }

    private void updateCorners(Vector2 lCorner, Vector2 rCorner, Rectangle rect) {
        lCorner.x = Math.min(lCorner.x, rect.x);
        lCorner.y = Math.min(lCorner.y, rect.y);
        rCorner.x = Math.max(rCorner.x, rect.x + rect.width);
        rCorner.y = Math.max(rCorner.y, rect.y + rect.height);
    }

    /**
     * @return AABB (coordinates in pixels)
     */
    public Rectangle getBodyAABB() {
        Vector2 lCorner = new Vector2();
        Vector2 rCorner = new Vector2();

        Array<Fixture> fixtures = body.getFixtureList();
        for (Fixture fixture : fixtures) {
            Vector2 vertex = new Vector2();

            switch (fixture.getType()) {
                case Edge:
                    EdgeShape edgeShape = (EdgeShape) fixture.getShape();

                    edgeShape.getVertex1(vertex);
                    updateCorners(lCorner, rCorner, vertex);
                    edgeShape.getVertex2(vertex);
                    updateCorners(lCorner, rCorner, vertex);
                    break;

                case Circle:
                    CircleShape circleShape = (CircleShape) fixture.getShape();

                    Rectangle shapeRect = new Rectangle();
                    shapeRect.setCenter(circleShape.getPosition().add(circleShape.getRadius(), circleShape.getRadius()));
                    shapeRect.setSize(2 * circleShape.getRadius()); // TODO test it
                    updateCorners(lCorner, rCorner, shapeRect);
                    break;

                case Polygon:
                    PolygonShape polygonShape = (PolygonShape) fixture.getShape();

                    for (int i = 0; i < polygonShape.getVertexCount(); i++) {
                        polygonShape.getVertex(i, vertex);
                        updateCorners(lCorner, rCorner, vertex);
                    }
                    break;

                case Chain:
                    ChainShape chainShape = (ChainShape) fixture.getShape();

                    for (int i = 0; i < chainShape.getVertexCount(); i++) {
                        chainShape.getVertex(i, vertex);
                        updateCorners(lCorner, rCorner, vertex);
                    }
                    break;
            }
        }

        lCorner.add(body.getPosition());
        rCorner.add(body.getPosition());

        return new Rectangle(lCorner.x, lCorner.y, rCorner.x - lCorner.x, rCorner.y - lCorner.y);
    }

    private void destroyBody() {
        if (body != null) {
            if (gameProcess != null) {
                gameProcess.getWorld().destroyBody(body);
            }
            body = null;
        }
    }

    @Override
    public void dispose() {
        destroyBody();
    }
}
