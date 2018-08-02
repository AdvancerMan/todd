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
    private boolean alive;

    // Must set size of sprite before using
    public InGameObject(ToddEthottGame game, GameProcess gameProcess) {
        this.game = game;
        this.gameProcess = gameProcess;
        sprite = new Sprite();
        alive = true;
    }

    public void update(float delta) {
        if (!isAlive()) {
            return;
        }
    }

    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        if (!isAlive()) {
            return;
        }
    }

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

    public void kill() {
        alive = false;
        dispose();
    }

    /**
     * if object is not alive Process should delete it
     * @return object is alive
     */
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void dispose() {
        if (!isAlive()) {
            return;
        }
    }
}
