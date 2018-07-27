package com.company.todd.game.process;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Process {
    public void update(float delta);
    public void draw(SpriteBatch batch);
    public void dispose();
}
