package com.company.todd.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public abstract class InGameObject implements Disposable {
    protected final ToddEthottGame game;
    protected final GameProcess gameProcess;
    protected Sprite sprite;
    protected boolean collidable;
    private boolean alive;

    // Must set size of sprite before using
    public InGameObject(ToddEthottGame game, GameProcess gameProcess) {
        this.game = game;
        this.gameProcess = gameProcess;
        sprite = new Sprite();
        alive = true;
        collidable = true;
    }

    public abstract void update(float delta);

    public abstract void draw(SpriteBatch batch, Rectangle cameraRectangle);

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        sprite.setSize(width, height);
    }

    public Rectangle getRect() {
        return sprite.getBoundingRectangle();
    }

    public boolean overlaps(InGameObject obj) {
        return obj.getRect().overlaps(this.getRect());
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void kill() {
        alive = false;
    }

    /**
     * if object is not alive Process should dispose it after draw()
     * @return object is alive
     */
    public boolean isKilled() {
        return !alive;
    }

    @Override
    public abstract void dispose();
}
