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
    private Rectangle objectRect;
    private BodyDef.BodyType bodyType;
    private boolean alive;

    public static int lastId = 0;
    private int id;

    public InGameObject(ToddEthottGame game, BodyDef.BodyType bodyType,
                        float x, float y, float width, float height) {
        this.game = game;
        this.gameProcess = null;

        sprite = new Sprite();
        sprite.setBounds(x, y, width, height);  // TODO set bounds for sprite

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

    public abstract void update(float delta);

    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        Vector2 pos = toPix(body.getPosition().cpy());
        sprite.setCenter(pos.x, pos.y);

        if (!body.isFixedRotation()) {
            sprite.setOriginCenter();
            sprite.setRotation(body.getAngle() * FloatCmp.degsInRad);
        }

        if (sprite.getBoundingRectangle().overlaps(cameraRectangle)) {
            sprite.draw(batch);
        }
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
