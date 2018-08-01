package com.company.todd.desktop.level_editor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.launcher.ToddEthottGame.HEIGHT;
import static com.company.todd.launcher.ToddEthottGame.TITLE;
import static com.company.todd.launcher.ToddEthottGame.WIDTH;

public class LevelEditorDesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = WIDTH;
        config.height = HEIGHT;
        config.title = TITLE;
        new LwjglApplication(new LevelEditor(), config);
    }
}
