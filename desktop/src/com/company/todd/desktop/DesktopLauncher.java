package com.company.todd.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.launcher.ToddEthottGame.STANDART_HEIGHT;
import static com.company.todd.launcher.ToddEthottGame.TITLE;
import static com.company.todd.launcher.ToddEthottGame.STANDART_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = STANDART_WIDTH;
        config.height = STANDART_HEIGHT;
        config.title = TITLE;
		new LwjglApplication(new ToddEthottGame(), config);
	}
}
