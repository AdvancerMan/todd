package com.company.todd.game.objs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
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

    protected Sprite sprite;
    private MyAnimation animation;

    protected Body body;
    private Rectangle objectRect;
    private BodyDef.BodyType bodyType;
    private boolean alive;

    public static int lastId = 0;
    private int id;

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType, MyAnimation animation,
                        float x, float y, float width, float height) {
        this.game = game;
        this.gameProcess = null;

        sprite = new Sprite();
        sprite.setBounds(x, y, width, height);  // TODO set bounds for sprite
        this.animation = animation;

        body = null;
        objectRect = new Rectangle(x, y, width, height);
        this.bodyType = bodyType;

        alive = true;

        lastId += 1;
        id = lastId;
    }

    protected void createMyBody() {
        destroyMyBody();

        float width = objectRect.width, height = objectRect.height;
        float x = objectRect.x + width / 2, y = objectRect.y + height / 2;

        body = createBody(gameProcess.getWorld(), bodyType, new Vector2(x, y));
        addBox(body, width / 2, height / 2);  // TODO size / 2 ?
        body.setUserData(this);
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
        Vector2 pos = toPix(body.getPosition().cpy());
        sprite.setCenter(pos.x, pos.y);

        if (!body.isFixedRotation()) {
            sprite.setOriginCenter();
            sprite.setRotation(body.getAngle() * FloatCmp.degsInRad);
        }

        if (sprite.getBoundingRectangle().overlaps(cameraRectangle)) {
            sprite.setRegion(animation.getFrame());
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

    public void setGameProcess(GameProcess gameProcess) {
        destroyMyBody();
        this.gameProcess = gameProcess;
        createMyBody();
    }

    public void setPosition(float x, float y) {
        objectRect.setPosition(x, y);

        if (body != null) {
            createMyBody();
        }
    }

    public void setSize(float width, float height) {
        objectRect.setSize(width, height);
        sprite.setSize(width, height);  // TODO sprite.setSize()

        if (body != null) {
            Vector2 pos = toPix(body.getPosition().cpy());
            objectRect.setCenter(pos);
            createMyBody();
        }
    }

    public Rectangle getObjectRect() {  // TODO with rotation
        if (body != null) {
            Vector2 pos = toPix(body.getPosition().cpy());
            objectRect.setCenter(pos);
        }

        return new Rectangle(objectRect);
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
