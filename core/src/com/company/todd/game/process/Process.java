package com.company.todd.game.process;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Process {
    void preUpdate(float delta);
    void draw(SpriteBatch batch);
    void postUpdate(float delta);
    void dispose();
}
