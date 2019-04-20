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
import static com.company.todd.game.process.GameProcess.toPix;

public abstract class InGameObject implements Disposable {
    protected final ToddEthottGame game;
    protected GameProcess gameProcess;

    protected Sprite sprite;

    protected Body body;
    private Vector2 size;
    private BodyDef.BodyType bodyType;
    private boolean alive;

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType,
                        float x, float y, float width, float height) {
        this.game = game;
        this.gameProcess = null;

        sprite = new Sprite();
        sprite.setBounds(x, y, width, height);  // TODO set bounds for sprite

        body = null;
        size = new Vector2(width, height);
        this.bodyType = bodyType;

        alive = true;
    }

    protected void createMyBodyAtCorner(float x, float y) {
        createMyBodyAtCenter(x + size.x / 2, y + size.y / 2);
    }

    protected void createMyBodyAtCenter(float x, float y) {
        float width = size.x, height = size.y;

        body = createBody(gameProcess.getWorld(), bodyType, new Vector2(x, y));
        addBox(body, width / 2, height / 2);  // TODO size / 2 ?
        body.setUserData(this);
    }

    public abstract void update(float delta);

    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        Vector2 tmpVec = toPix(body.getPosition().cpy());
        sprite.setCenter(tmpVec.x, tmpVec.y);
        if (!body.isFixedRotation()) {
            sprite.setOriginCenter();
            sprite.setRotation(body.getAngle() * FloatCmp.degsInRad);
        }

        if (sprite.getBoundingRectangle().overlaps(cameraRectangle)) {
            sprite.draw(batch);
        }
    }

    public void setBodyActive(boolean bodyActive) {
        body.setActive(bodyActive);
    }

    public void setGameProcess(GameProcess gameProcess) {
        this.gameProcess = gameProcess;
        createMyBodyAtCorner(sprite.getX(), sprite.getY());  // TODO position
    }

    public void setPosition(float x, float y) {
        gameProcess.getWorld().destroyBody(body);
        createMyBodyAtCorner(x, y);
    }

    public void setSize(float width, float height) {
        size.set(width, height);
        Vector2 lastPos = body.getPosition();
        gameProcess.getWorld().destroyBody(body);
        createMyBodyAtCenter(toPix(lastPos.x), toPix(lastPos.y));
        sprite.setSize(width, height);  // TODO sprite.setSize()
    }

    public Rectangle getObjectRect() {  // TODO with rotation
        Vector2 pos = toPix(body.getPosition().cpy());
        pos.x -= size.x / 2;
        pos.y -= size.y / 2;
        return new Rectangle(pos.x, pos.y, size.x, size.y);
    }

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

        if (body != null) {
            gameProcess.getWorld().destroyBody(body);
        }
    }
}
