package com.company.todd.level_editor;

import com.company.todd.launcher.ToddEthottGame;

public class LevelEditor extends ToddEthottGame {
    @Override
    public void create() {
        super.create();
        screenManager.setNextScreen(new LevelEditorScreen(this));
    }
}
