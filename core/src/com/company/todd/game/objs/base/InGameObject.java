package com.company.todd.game.objs.base;

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

public abstract class InGameObject extends PixelBody implements Disposable {
    private boolean dirToRight;
    private Sprite sprite;
    private MyAnimation animation;

    private boolean alive;

    private static int lastId = 0;
    private int id;

    public InGameObject(ToddEthottGame game, GameProcess gameProcess,
                        BodyDef.BodyType bodyType, MyAnimation animation,
                        BodyInfo bodyInfo, Vector2 spriteSize) {
        super(game, gameProcess, bodyType, bodyInfo);

        sprite = new Sprite();
        sprite.setSize(spriteSize.x, spriteSize.y);
        this.animation = animation;
        if (animation == null) {
            this.animation = new MyAnimation();
        }
        setPlayingAnimationName(MyAnimation.AnimationType.STAY, false);
        dirToRight = true;

        alive = true;

        lastId += 1;
        id = lastId;
    }

    public InGameObject(ToddEthottGame game, GameProcess gameProcess,
                        BodyDef.BodyType bodyType, MyAnimation animation,
                        float x, float y,
                        float bodyWidth, float bodyHeight,
                        float spriteWidth, float spriteHeight) {
        this(game, gameProcess, bodyType, animation,
                new BodyInfo(x, y, bodyWidth, bodyHeight),
                new Vector2(spriteWidth, spriteHeight));
    }

    public InGameObject(ToddEthottGame game, GameProcess gameProcess,
                        BodyDef.BodyType bodyType, MyAnimation animation,
                        float x, float y, float width, float height) {
        this(game, gameProcess, bodyType, animation, x, y, width, height, width, height);
    }

    public InGameObject(ToddEthottGame game, GameProcess gameProcess,
                        BodyDef.BodyType bodyType, MyAnimation animation,
                        float x, float y,
                        float bodyRadius,
                        float spriteWidth, float spriteHeight) {
        this(game, gameProcess, bodyType, animation,
                new BodyInfo(x, y, bodyRadius),
                new Vector2(spriteWidth, spriteHeight));
    }

    public void update(float delta) {
        animation.update(delta);
    }

    public void preWorldUpdate(float delta) {}
    public void postDrawUpdate(float delta) {}

    public void postWorldPreDrawUpdate(float delta) {
        Vector2 pos = getBodyPosition();
        sprite.setCenter(pos.x, pos.y);

        if (!isFixedRotation()) {
            sprite.setOriginCenter();
            sprite.setRotation(getBodyAngle() * FloatCmp.DEGS_IN_RAD);
        }
    }

    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
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

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        sprite.setSize(width, height);  // TODO sprite.setSize()
    }

    public boolean isDirectedToRight() {
        return dirToRight;
    }

    public Rectangle getSpriteBoundingRect() {
        return sprite.getBoundingRectangle();
    }

    public Vector2 getSpritePosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public Vector2 getSpriteSize() {
        return new Vector2(sprite.getWidth(), sprite.getHeight());
    }

    public abstract boolean isGroundFor(Contact contact, InGameObject object);

    // TODO invulnerability for some time after taking damage
    public void takeDamage(float amount) {}

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
        return !alive || !isGameProcessInitiated();
    }

    @Override
    public void dispose() {
        if (alive) {
            System.out.println("wtf destroying");  // TODO log destroying if alive
        }

        super.dispose();
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
