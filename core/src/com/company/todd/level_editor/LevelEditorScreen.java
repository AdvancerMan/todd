package com.company.todd.level_editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.MyScreen;

public class LevelEditorScreen extends MyScreen {
    protected LevelEditorProcess process;

    public LevelEditorScreen(ToddEthottGame game) {
        super(game);
        process = new LevelEditorProcess(game, this);
    }

    @Override
    protected void update(float delta) {
        super.update(delta);
        process.update(delta);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch);

        batch.begin();

        process.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        process.dispose();
    }
}
