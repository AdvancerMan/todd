package com.company.todd.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import com.company.todd.launcher.ToddEthottGame;

public abstract class InGameObject implements Disposable {
    final protected ToddEthottGame game;
    protected Sprite sprite;

    public InGameObject(ToddEthottGame game) {
        this.game = game;
        sprite = new Sprite();
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
}
