package com.company.todd.game.process;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Process {
    void update(float delta);
    void draw(SpriteBatch batch);
    void dispose();
}
