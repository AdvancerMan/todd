package com.company.todd.game.objs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Disposable;

import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.util.FloatCmp;

import static com.company.todd.box2d.BodyCreator.addBox;
import static com.company.todd.box2d.BodyCreator.createBody;

public abstract class InGameObject implements Disposable {
    protected final ToddEthottGame game;
    protected GameProcess gameProcess;
    protected Sprite sprite;
    protected Body body;
    private BodyDef.BodyType bodyType;
    private boolean alive;

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType,
                        float x, float y, float width, float height) {
        this.game = game;
        this.gameProcess = null;

        sprite = new Sprite();
        sprite.setBounds(x, y, width, height);

        body = null;
        this.bodyType = bodyType;

        alive = true;
    }

    public void init(GameProcess gameProcess) {
        this.gameProcess = gameProcess;
        createMyBody();
    }

    protected void createMyBody() {
        Rectangle rectangle = getSpriteRect();
        float x = rectangle.x, y = rectangle.y;
        float width = rectangle.width, height = rectangle.height;

        body = createBody(gameProcess.getWorld(), bodyType, new Vector2(x + width / 2, y + height / 2));
        addBox(body, width / 2, height / 2);
        body.setUserData(this);
    }

    public void setBodyActive(boolean bodyActive) {
        if (body != null) {
            body.setActive(bodyActive);
        }
    }

    public void update(float delta) {
        Vector2 tmpVec = body.getPosition().scl(1 / GameProcess.metersPerPix);
        sprite.setCenter(tmpVec.x, tmpVec.y);
        if (!body.isFixedRotation()) {
            sprite.setRotation(body.getAngle() * FloatCmp.degsInRad);
        }
    }

    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        if (getSpriteRect().overlaps(cameraRectangle)) {
            sprite.draw(batch);
        }
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
        // TODO updateMyBody();
    }

    public void setSize(float width, float height) {  // TODO  body.setTransform()
        sprite.setSize(width, height);
        // TODO updateMyBody();
    }

    public Rectangle getSpriteRect() {
        return sprite.getBoundingRectangle();
    }

    public void kill() {
        alive = false;
    }

    public void destroyBody() {
        if (alive) {
            System.out.println("wtf destroying");  // TODO log destroying if alive
        }

        if (body != null) {
            gameProcess.getWorld().destroyBody(body);
        }
    }

    /**
     * if object is not alive Process should dispose it after draw()
     * @return object is killed
     */
    public boolean isKilled() {
        return !alive;
    }

    @Override
    public abstract void dispose();
}
