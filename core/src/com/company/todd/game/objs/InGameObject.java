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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;
import com.company.todd.util.FloatCmp;

import static com.company.todd.box2d.BodyCreator.addBox;
import static com.company.todd.box2d.BodyCreator.createBody;
import static com.company.todd.game.process.GameProcess.toPix;

public abstract class InGameObject implements Disposable {
    protected final ToddEthottGame game;
    protected GameProcess gameProcess;

    private Sprite sprite;  // TODO arms sprite
    private boolean dirToRight;
    private MyAnimation animation;

    protected Body body;
    private Rectangle bodySize;
    private BodyDef.BodyType bodyType;
    private boolean alive;

    public static int lastId = 0;
    private int id;

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        Rectangle spriteRect, Rectangle bodyRect) {
        this.game = game;
        this.gameProcess = null;

        sprite = new Sprite();
        sprite.setBounds(spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);
        this.animation = animation;
        setPlayingAnimationName("stay", false);
        dirToRight = true;

        body = null;
        bodySize = new Rectangle(bodyRect);
        this.bodyType = bodyType;

        alive = true;

        lastId += 1;
        id = lastId;
    }

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        float spriteX, float spriteY, float spriteWidth, float spriteHeight,
                        float bodyX, float bodyY, float bodyWidth, float bodyHeight) {
        this(game, bodyType, animation,
                new Rectangle(spriteX, spriteY, spriteWidth, spriteHeight),
                new Rectangle(bodyX, bodyY, bodyWidth, bodyHeight));
    }

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        float x, float y, float width, float height) {
        this(game, bodyType, animation, new Rectangle(x, y, width, height), new Rectangle(x, y, width, height));
    }

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        Rectangle objectRect) {
        this(game, bodyType, animation, objectRect, objectRect);
    }

    protected void createMyBody(Rectangle bodySize) {
        destroyMyBody();
        this.bodySize = bodySize;

        float width = bodySize.width, height = bodySize.height;
        float x = bodySize.x + width / 2, y = bodySize.y + height / 2;

        body = createBody(gameProcess.getWorld(), bodyType, new Vector2(x, y));
        addBox(body, width / 2, height / 2);  // TODO size / 2 ?
        body.setUserData(this);
    }

    protected void createMyBody() {
        createMyBody(bodySize);
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

        if (getSpriteRect().overlaps(cameraRectangle)) {
            sprite.setRegion(animation.getFrame());
            if (!dirToRight) {
                sprite.flip(true, false);
            }
            sprite.draw(batch);
        }
    }


    public void addAnimation(String animName, Array<TextureRegionInfo.TextureRegionGetter> getters,
                             float frameDuration, Animation.PlayMode playMode) {
        animation.addAnimation(animName, getters, frameDuration, playMode);
    }

    public void deleteAnimation(String animName) {
        animation.deleteAnimation(animName);
    }

    public void setAnimation(String animName, Array<TextureRegionInfo.TextureRegionGetter> getters,
                             float frameDuration, Animation.PlayMode playMode) {
        animation.setAnimation(animName, getters, frameDuration, playMode);
    }

    public void setAnimationFrameDuration(String animName, float frameDuration) {
        animation.setFrameDuration(animName, frameDuration);
    }

    public void setAnimationPlayMode(String animName, Animation.PlayMode playMode) {
        animation.setPlayMode(animName, playMode);
    }

    public void setPlayingAnimationName(String animationName, boolean changeEquals) {
        animation.setPlayingAnimationName(animationName, changeEquals);
    }

    public void setDirToRight(boolean dirToRight) {
        this.dirToRight = dirToRight;
    }

    public void setGameProcess(GameProcess gameProcess) {
        destroyMyBody();
        this.gameProcess = gameProcess;
        createMyBody();
    }

    public void setPosition(float x, float y) {  // FIXME weird logic
        bodySize.setPosition(x, y);

        if (body != null) {
            createMyBody();
        }
    }

    public void setSize(float width, float height) {  // FIXME weird logic
        bodySize.setSize(width, height);
        sprite.setSize(width, height);  // TODO sprite.setSize()

        if (body != null) {
            Vector2 pos = getBodyPosition();
            bodySize.setCenter(pos);
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
        Vector2 lCorner = new Vector2(getBodyPosition());
        Vector2 rCorner = new Vector2(getBodyPosition());

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

        lCorner.add(getBodyPosition());
        rCorner.add(getBodyPosition());
        return new Rectangle(lCorner.x, lCorner.y, rCorner.x - lCorner.x, rCorner.y - lCorner.y);
    }

    public Rectangle getSpriteRect() {
        return sprite.getBoundingRectangle();
    }

    public Vector2 getBodyPosition() {
        return toPix(body.getPosition().cpy());
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
