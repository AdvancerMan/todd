package com.company.todd.game.objs.base;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
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
import static com.company.todd.game.process.GameProcess.toMeters;
import static com.company.todd.game.process.GameProcess.toPix;

public class PixelBody implements Disposable {  // TODO toMeters() toPix() review of functions
    protected final ToddEthottGame game;
    protected GameProcess gameProcess;

    private Body body;
    private BodyInfo bodyInfo;  // FIXME delete this because you dont need it everywhere (or make static)
    private BodyDef.BodyType bodyType;

    public PixelBody(ToddEthottGame game, GameProcess gameProcess,
                     BodyDef.BodyType bodyType, BodyInfo bodyInfo) {
        this.game = game;
        this.bodyType = bodyType;
        this.bodyInfo = bodyInfo;
        setGameProcess(gameProcess);
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

    /**
     * @param impulse in kg * pix * pix / s
     */
    public void applyAngularImpulse(float impulse) {
        body.applyAngularImpulse(toMeters(toMeters(impulse)), true);
    }

    /**
     * @param force in kg * pix / s / s
     * @param point coordinates in pixels
     */
    public void applyForce(Vector2 force, Vector2 point) {
        body.applyForce(toMeters(force.cpy()), toMeters(point.cpy()), true);
    }

    /**
     * @param forceX in kg * pix / s / s
     * @param forceY in kg * pix / s / s
     * @param pointX in pixels
     * @param pointY in pixels
     */
    public void applyForce(float forceX, float forceY, float pointX, float pointY) {
        body.applyForce(toMeters(forceX), toMeters(forceY),
                toMeters(pointX), toMeters(pointY), true);
    }

    /**
     * @param force in kg * pix / s / s
     */
    public void applyForceToCenter(Vector2 force) {
        body.applyForceToCenter(toMeters(force.cpy()), true);
    }

    /**
     * @param forceX in kg * pix / s / s
     * @param forceY in kg * pix / s / s
     */
    public void applyForceToCenter(float forceX, float forceY) {
        body.applyForceToCenter(toMeters(forceX), toMeters(forceY), true);
    }

    /**
     * @param impulse in kg * pix / s
     * @param point in pixels
     */
    public void applyLinearImpulse(Vector2 impulse, Vector2 point) {
        body.applyLinearImpulse(toMeters(impulse.cpy()), toMeters(point.cpy()), true);
    }

    /**
     * @param impulseX in kg * pix / s
     * @param impulseY in kg * pix / s
     * @param pointX in pixels
     * @param pointY in pixels
     */
    public void applyLinearImpulse(float impulseX, float impulseY, float pointX, float pointY) {
        body.applyLinearImpulse(toMeters(impulseX), toMeters(impulseY),
                toMeters(pointX), toMeters(pointY), true);
    }

    /**
     * @param impulse in kg * pix / s
     */
    public void applyLinearImpulseToCenter(Vector2 impulse) {
        body.applyLinearImpulse(toMeters(impulse.cpy()), body.getWorldCenter(), true);
    }

    /**
     * @param impulseX in kg * pix / s
     * @param impulseY in kg * pix / s
     */
    public void applyLinearImpulseToCenter(float impulseX, float impulseY) {
        body.applyLinearImpulse(toMeters(new Vector2(impulseX, impulseY)),
                body.getWorldCenter(), true);
    }

    /**
     * @param torque in kg * pix * pix / s / s
     */
    public void applyTorque(float torque) {
        body.applyTorque(toMeters(toMeters(torque)), true);
    }

    public void setBodyActive(boolean flag) {
        body.setActive(flag);
    }

    /**
     * @param velocity in rad / s
     */
    public void setAngularVelocity(float velocity) {
        body.setAngularVelocity(velocity);
    }

    public void setAwake(boolean flag) {
        body.setAwake(flag);
    }

    public void setBullet(boolean flag) {
        body.setBullet(flag);
    }

    public void setFixedRotation(boolean flag) {
        body.setFixedRotation(flag);
    }

    public void setGravityScale(float scale) {
        body.setGravityScale(scale);
    }

    public void setSleepingAllowed(boolean flag) {
        body.setSleepingAllowed(flag);
    }

    /**
     * @param v velocity (in pixels / s)
     */
    public void setVelocity(Vector2 v) {
        body.setLinearVelocity(toMeters(v.cpy()));
    }

    /**
     * @param yVel y-axis velocity (in pixels / s)
     */
    public void setYVelocity(float yVel) {
        body.setLinearVelocity(body.getLinearVelocity().x, toMeters(yVel));
    }

    /**
     * @param xVel x-axis velocity (in pixels / s)
     */
    public void setXVelocity(float xVel) {
        body.setLinearVelocity(toMeters(xVel), body.getLinearVelocity().y);
    }

    /**
     * @param x in pixels
     * @param y in pixels
     */
    public void setCenterPosition(float x, float y) {
        if (body != null) {
            body.setLinearVelocity(0, 0);
            body.setTransform(toMeters(x), toMeters(y), getBodyAngle());
        }
    }

    /**
     * @param x in pixels
     * @param y in pixels
     */
    public void setPosition(float x, float y) {
        if (body != null) {
            Vector2 pos = getBodyPosition();
            setCenterPosition(x + pos.x / 2, y + pos.y / 2);
        }
    }

    /**
     * @param angle in radians
     */
    public void setBodyAngle(float angle) {
        if (body != null) {
            body.setAngularVelocity(0);
            body.setTransform(body.getPosition(), angle);
        }
    }

    /**
     * @param width in pixels
     * @param height in pixels
     */
    public void setSize(float width, float height) {  // FIXME weird logic
        bodyInfo.setSize(new Vector2(width, height));

        if (body != null) {
            Vector2 pos = getBodyPosition();
            bodyInfo.setCenter(pos);  // TODO toMeters() or toPix() ?
            destroyBody();
            createMyBody();
        }
    }

    public void setMassData(MassData data) {
        body.setMassData(data);
    }

    public void resetMassData() {
        body.resetMassData();
    }

    /**
     * @return angle in radians
     */
    public float getBodyAngle() {
        return body.getAngle();
    }

    /**
     * @return velocity in rad / s
     */
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    public Array<Fixture> getFixtureList() {
        return body.getFixtureList();
    }

    public float getGravityScale() {
        return body.getGravityScale();
    }

    /**
     * @return inertia in kg * pix * pix
     */
    public float getInertia() {
        return toPix(toPix(body.getInertia()));
    }

    /**
     * @return velocity in pix / s
     */
    public Vector2 getLinearVelocity() {
        return toPix(body.getLinearVelocity().cpy());
    }

    /**
     * @param point local point in pixels
     * @return velocity in pix / s
     */
    public Vector2 getLinearVelocityFromLocalPoint(Vector2 point) {
        return toPix(body.getLinearVelocityFromLocalPoint(toMeters(point.cpy())).cpy());
    }

    /**
     * @param point world point in pixels
     * @return velocity in pix / s
     */
    public Vector2 getLinearVelocityFromWorldPoint(Vector2 point) {
        return toPix(body.getLinearVelocityFromWorldPoint(toMeters(point.cpy())).cpy());
    }

    /**
     * @return local center of mass (coordinates in pixels)
     */
    public Vector2 getLocalCenterOfMass() {
        return toPix(body.getLocalCenter().cpy());
    }

    /**
     * @param point coordinates in pixels
     * @return local point (coordinates in pixels)
     */
    public Vector2 getLocalPoint(Vector2 point) {
        return toPix(body.getLocalPoint(toMeters(point.cpy())).cpy());
    }

    /**
     * @param vector coordinates in pixels
     * @return local vector (coordinates in pixels)
     */
    public Vector2 getLocalVector(Vector2 vector) {
        return toPix(body.getLocalVector(toMeters(vector.cpy())).cpy());
    }  // TODO test for differences getLocalVector() and getLocalPoint()

    public float getMass() {
        return body.getMass();
    }

    public MassData getMassData() {
        MassData result = new MassData();
        MassData bodyData = body.getMassData();
        result.center.x = bodyData.center.x;
        result.center.y = bodyData.center.y;
        result.I = bodyData.I;
        result.mass = bodyData.mass;
        return result;
    }

    /**
     * @return position (coordinates in pixels)
     */
    public Vector2 getBodyPosition() {
        return toPix(body.getPosition().cpy());
    }

    public Vector2 getWorldCenterOfMass() {
        return toPix(body.getWorldCenter().cpy());
    }

    public Vector2 getWorldPoint(Vector2 point) {
        return toPix(body.getWorldPoint(point.cpy()).cpy());
    }

    public Vector2 getWorldVector(Vector2 vector) {
        return toPix(body.getWorldVector(vector.cpy()).cpy());
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

        toPix(lCorner);
        toPix(rCorner);

        lCorner.add(getBodyPosition());
        rCorner.add(getBodyPosition());

        return new Rectangle(lCorner.x, lCorner.y, rCorner.x - lCorner.x, rCorner.y - lCorner.y);
    }

    public boolean isBodyActive() {
        return body.isActive();
    }

    public boolean isAwake() {
        return body.isAwake();
    }

    public boolean isBullet() {
        return body.isBullet();
    }

    public boolean isFixedRotation() {
        return body.isFixedRotation();
    }

    public boolean isSleepingAllowed() {
        return body.isSleepingAllowed();
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
