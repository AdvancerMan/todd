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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

import static com.company.todd.box2d.BodyCreator.addBox;
import static com.company.todd.box2d.BodyCreator.addCircle;
import static com.company.todd.box2d.BodyCreator.createBody;
import static com.company.todd.game.process.GameProcess.toPix;

public abstract class InGameObject implements Disposable {
    protected final ToddEthottGame game;
    protected GameProcess gameProcess;

    private Sprite sprite;
    private boolean dirToRight;
    private MyAnimation animation;

    protected Body body;
    protected BodyInfo bodyInfo;
    private BodyDef.BodyType bodyType;
    private boolean alive;

    public static int lastId = 0;
    private int id;

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        Vector2 spriteSize, BodyInfo bodyInfo) {
        this.game = game;
        this.gameProcess = null;

        sprite = new Sprite();
        sprite.setSize(spriteSize.x, spriteSize.y);
        this.animation = animation;
        setPlayingAnimationName(MyAnimation.AnimationType.STAY, false);
        dirToRight = true;

        body = null;
        this.bodyInfo = bodyInfo;
        this.bodyType = bodyType;

        alive = true;

        lastId += 1;
        id = lastId;
    }

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        float spriteWidth, float spriteHeight,
                        float bodyX, float bodyY, float bodyWidth, float bodyHeight) {
        this(game, bodyType, animation,
                new Vector2(spriteWidth, spriteHeight),
                new BodyInfo(bodyX, bodyY, bodyWidth, bodyHeight));
    }

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        float x, float y, float width, float height) {
        this(game, bodyType, animation, width, height, x, y, width, height);
    }

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        float spriteWidth, float spriteHeight,
                        float x, float y, float bodyRadius) {
        this(game, bodyType, animation, new Vector2(spriteWidth, spriteHeight),
                new BodyInfo(x, y, bodyRadius));
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

    // TODO setCenterPosition()
    public void setPosition(float x, float y) {  // FIXME weird logic
        bodyInfo.setPosition(new Vector2(x, y));

        if (body != null) {
            createMyBody();
        }
    }

    public void setSize(float width, float height) {  // FIXME weird logic
        bodyInfo.setSize(new Vector2(width, height));
        sprite.setSize(width, height);  // TODO sprite.setSize()

        if (body != null) {
            Vector2 pos = getBodyPosition();
            bodyInfo.setCenter(pos);
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

    public Rectangle getBodyRect() {
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

    public Vector2 getSpritePosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }
    public Vector2 getSpriteSize() {
        return new Vector2(sprite.getWidth(), sprite.getHeight());
    }

    protected boolean isDirectedToRight() {
        return dirToRight;
    }

    public void beginContact(Contact contact, InGameObject object) {}
    public void endContact(Contact contact, InGameObject object) {}
    public void contactPreSolve(Contact contact, Manifold oldManifold, InGameObject object) {}
    public void contactPostSolve(Contact contact, ContactImpulse impulse, InGameObject object) {}

    public void kill() {
        alive = false;
    }

    /**
     * if object is not alive Process should dispose it after draw()
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
