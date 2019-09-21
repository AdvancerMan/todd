package com.company.todd.game.objs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.ToddException;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

import static com.company.todd.box2d.BodyCreator.addBox;
import static com.company.todd.box2d.BodyCreator.addCircle;
import static com.company.todd.box2d.BodyCreator.createBody;
import static com.company.todd.game.process.GameProcess.toMeters;
import static com.company.todd.game.process.GameProcess.toPix;

public abstract class InGameObject implements Disposable {  // TODO toMeters() toPix() review of functions
    protected final ToddEthottGame game;
    protected GameProcess gameProcess;

    private Sprite sprite;
    private boolean dirToRight;
    private MyAnimation animation;

    protected Body body;  // TODO make body private
    protected BodyInfo bodyInfo;
    private BodyDef.BodyType bodyType;
    private boolean alive;

    public static int lastId = 0;
    private int id;

    protected boolean availableToBeGround;

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        BodyInfo bodyInfo, Vector2 spriteSize) {
        this.game = game;
        this.gameProcess = null;

        sprite = new Sprite();
        sprite.setSize(spriteSize.x, spriteSize.y);
        this.animation = animation;
        if (animation == null) {
            this.animation = new MyAnimation();
        }
        setPlayingAnimationName(MyAnimation.AnimationType.STAY, false);
        dirToRight = true;

        body = null;
        this.bodyInfo = bodyInfo;
        this.bodyType = bodyType;

        alive = true;

        availableToBeGround = true;

        lastId += 1;
        id = lastId;
    }

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        float x, float y,
                        float bodyWidth, float bodyHeight,
                        float spriteWidth, float spriteHeight) {
        this(game, bodyType, animation,
                new BodyInfo(x, y, bodyWidth, bodyHeight),
                new Vector2(spriteWidth, spriteHeight));
    }

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        float x, float y, float width, float height) {
        this(game, bodyType, animation, x, y, width, height, width, height);
    }

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        float x, float y,
                        float bodyRadius,
                        float spriteWidth, float spriteHeight) {
        this(game, bodyType, animation, new BodyInfo(x, y, bodyRadius),
                new Vector2(spriteWidth, spriteHeight));
    }

    private void createMyCircleBody(Vector2 pos, float radius) {
        destroyMyBody();

        body = createBody(gameProcess.getWorld(), bodyType, pos);
        addCircle(body, pos, radius);
        body.setUserData(this);
    }

    private void createMyRectangleBody(Vector2 pos, Vector2 size) {
        destroyMyBody();

        float width = size.x, height = size.y;
        float x = pos.x + width / 2, y = pos.y + height / 2;

        body = createBody(gameProcess.getWorld(), bodyType, new Vector2(x, y));
        addBox(body, width / 2, height / 2);  // TODO size / 2 ?
        body.setUserData(this);
    }

    protected void createMyBody() {
        if (bodyInfo.getType().equals(Shape.Type.Circle)) {
            createMyCircleBody(bodyInfo.getPosition(), bodyInfo.getRadius());
        } else if (bodyInfo.getType().equals(Shape.Type.Polygon)) {
            createMyRectangleBody(bodyInfo.getPosition(), bodyInfo.getSize());
        } else {
            throw new ToddException("bodyInfo is null");
        }
    }

    protected void destroyMyBody() {
        if (body != null) {
            if (gameProcess != null) {
                gameProcess.getWorld().destroyBody(body);
            }
            body = null;
        }
    }


    public void update(float delta) {
        animation.update(delta);
    }

    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        Vector2 pos = getBodyPosition();
        sprite.setCenter(pos.x, pos.y);

        if (!body.isFixedRotation()) {
            sprite.setOriginCenter();
            sprite.setRotation(body.getAngle() * FloatCmp.degsInRad);
        }

        if (getSpriteBoundingRect().overlaps(cameraRectangle)) {
            sprite.setRegion(animation.getFrame());
            if (!isDirectedToRight()) {
                sprite.flip(true, false);
            }
            sprite.draw(batch);
        }
    }

    public void addAnimation(MyAnimation.AnimationType animType,
                             Array<TextureRegionInfo.TextureRegionGetter> getters,
                             float frameDuration, Animation.PlayMode playMode) {
        animation.addAnimation(animType, getters, frameDuration, playMode);
    }

    public void deleteAnimation(MyAnimation.AnimationType animType) {
        animation.deleteAnimation(animType);
    }

    public void setAnimation(MyAnimation.AnimationType animType,
                             Array<TextureRegionInfo.TextureRegionGetter> getters,
                             float frameDuration, Animation.PlayMode playMode) {
        animation.setAnimation(animType, getters, frameDuration, playMode);
    }

    public void setAnimationFrameDuration(MyAnimation.AnimationType animType, float frameDuration) {
        animation.setFrameDuration(animType, frameDuration);
    }

    public void setAnimationDuration(MyAnimation.AnimationType animType, float duration) {
        animation.setAnimationDuration(animType, duration);
    }

    public void setAnimationPlayMode(MyAnimation.AnimationType animType, Animation.PlayMode playMode) {
        animation.setPlayMode(animType, playMode);
    }

    public void setPlayingAnimationName(MyAnimation.AnimationType animType, boolean changeEquals) {
        animation.setPlayingAnimationType(animType, changeEquals);
    }

    public void setDirToRight(boolean dirToRight) {
        this.dirToRight = dirToRight;
    }

    public void setGameProcess(GameProcess gameProcess) {
        destroyMyBody();
        this.gameProcess = gameProcess;
        createMyBody();
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
        body.applyForce(toMeters(force), toMeters(point), true);
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
        body.applyForceToCenter(toMeters(force), true);
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
        body.applyLinearImpulse(toMeters(impulse), toMeters(point), true);
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
        body.applyLinearImpulse(toMeters(impulse), body.getWorldCenter(), true);
    }

    /**
     * @param impulseX in kg * pix / s
     * @param impulseY in kg * pix / s
     */
    public void applyLinearImpulseToCenter(float impulseX, float impulseY) {
        body.applyLinearImpulse(toMeters(new Vector2(impulseX, impulseY)),
                body.getWorldCenter(), true);  // TODO not body.
    }

    /**
     * @param torque in kg * pix * pix / s / s
     */
    public void applyTorque(float torque) {
        body.applyTorque(toMeters(toMeters(torque)), true);
    }

    /*
    TODO make new methods:
    body.setActive();
    body.setAngularVelocity();
    body.setAwake();
    body.setBullet();
    body.setFixedRotation();
    body.setGravityScale();
    body.setLinearVelocity();
    body.setSleepingAllowed();
    */

    /**
     * @param v velocity (in pixels / s)
     */
    public void setVelocity(Vector2 v) {
        Vector2 vel = toMeters(new Vector2(v)).sub(body.getLinearVelocity());
        applyLinearImpulseToCenter(vel.scl(body.getMass()));
    }

    /**
     * @param yVel y-axis velocity (in pixels / s)
     */
    public void setYVelocity(float yVel) {
        // TODO body.setLinearVelocity(body.getLinearVelocity().x, yVel); ?

        Vector2 vel = toMeters(new Vector2(0, yVel - body.getLinearVelocity().y));
        applyLinearImpulseToCenter(vel.scl(body.getMass()));
    }

    /**
     * @param xVel x-axis velocity (in pixels / s)
     */
    public void setXVelocity(float xVel) {
        Vector2 vel = toMeters(new Vector2(xVel - body.getLinearVelocity().x, 0));
        applyLinearImpulseToCenter(vel.scl(body.getMass()));
    }

    /**
     * @param x in pixels
     * @param y in pixels
     */
    public void setCenterPosition(float x, float y) {
        if (body != null) {
            body.setTransform(toMeters(x), toMeters(y), body.getAngle());
        }
    }

    /**
     * @param x in pixels
     * @param y in pixels
     */
    public void setPosition(float x, float y) {
        if (body != null) {
            Rectangle bodyRect = getBodyAABB();
            setCenterPosition(x + bodyRect.x / 2, y + bodyRect.y / 2);
        }
    }

    /**
     * @param angle in radians
     */
    public void setAngle(float angle) {
        if (body != null) {
            body.setTransform(body.getPosition(), angle);
        }
    }

    /**
     * @param width in pixels
     * @param height in pixels
     */
    public void setSize(float width, float height) {  // FIXME weird logic
        bodyInfo.setSize(new Vector2(width, height));
        sprite.setSize(width, height);  // TODO sprite.setSize()

        if (body != null) {
            Vector2 pos = getBodyPosition();
            bodyInfo.setCenter(pos);  // TODO toMeters() or toPix() ?
            createMyBody();
        }
    }

    public void setMassData(MassData data) {
        body.setMassData(data);
    }

    public void resetMassData() {
        body.resetMassData();
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

    /*
    TODO new methods:
    body.getAngle();
    body.getAngularVelocity();
    body.getGravityScale();
    body.getInertia();
    body.getLinearVelocity();
    body.getLinearVelocityFromLocalPoint();
    body.getLinearVelocityFromWorldPoint();
    body.getLocalCenter();
    body.getLocalPoint();
    body.getLocalVector();
    body.getMass();
    body.getMassData();
    body.getPosition();
    body.getTransform();
    body.getWorldCenter();
    body.getWorldPoint();
    body.getWorldVector();
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

    public Rectangle getSpriteBoundingRect() {
        return sprite.getBoundingRectangle();
    }

    public Vector2 getBodyPosition() {
        return toPix(body.getPosition().cpy());
    }

    public Vector2 getLinearVelocity() {
        return toPix(body.getLinearVelocity().cpy());
    }

    public Vector2 getSpritePosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public Vector2 getSpriteSize() {
        return new Vector2(sprite.getWidth(), sprite.getHeight());
    }

    public boolean isActive() {
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

    protected boolean isDirectedToRight() {
        return dirToRight;
    }

    public boolean isAvailableToBeGround() {
        return availableToBeGround;
    }

    public void beginContact(Contact contact, InGameObject object) {}
    public void endContact(Contact contact, InGameObject object) {}
    public void contactPreSolve(Contact contact, Manifold oldManifold, InGameObject object) {}
    public void contactPostSolve(Contact contact, ContactImpulse impulse, InGameObject object) {}

    public void kill() {
        alive = false;
    }

    /**
     * if object is not alive Process should dispose it after drawing
     * @return object is killed
     */
    public boolean isKilled() {
        return !alive;
    }

    @Override
    public void dispose() {
        if (alive) {
            System.out.println("wtf destroying");  // TODO log destroying if alive
        }

        destroyMyBody();
        animation.dispose();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InGameObject)) return false;
        if (o == this) return true;
        return hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
