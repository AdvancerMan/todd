package com.company.todd.game.objs;

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
    protected final GameProcess gameProcess;
    protected Sprite sprite;
    protected Body body;
    private boolean alive;

    public InGameObject(ToddEthottGame game, GameProcess gameProcess, BodyDef.BodyType bodyType,
                        float x, float y, float width, float height) {
        this.game = game;
        this.gameProcess = gameProcess;

        sprite = new Sprite();
        sprite.setBounds(x, y, width, height);

        createMyBody(bodyType);

        alive = true;
    }

    protected void createMyBody(BodyDef.BodyType bodyType) {
        Rectangle rectangle = getSpriteRect();
        float x = rectangle.x, y = rectangle.y;
        float width = rectangle.width, height = rectangle.height;

        body = createBody(gameProcess.getWorld(), bodyType, new Vector2(x + width / 2, y + height / 2));
        addBox(body, width, height);
    }

    private void updateMyBody() {
        BodyDef.BodyType bodyType = body.getType();
        gameProcess.getWorld().destroyBody(body);
        createMyBody(bodyType);
    }

    public void update(float delta) {
        Vector2 tmpVec = body.getPosition();
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
        updateMyBody();
    }

    public void setSize(float width, float height) {
        sprite.setSize(width, height);
        updateMyBody();
    }

    public Rectangle getSpriteRect() {
        return sprite.getBoundingRectangle();
    }

    public void kill() {
        gameProcess.getWorld().destroyBody(body);
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
    public abstract void dispose();
}
