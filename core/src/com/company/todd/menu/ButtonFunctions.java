package com.company.todd.menu;

import com.company.todd.game.Save;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.screen.GameScreen;
import com.company.todd.screen.MainMenuScreen;

public class ButtonFunctions { // TODO more ButtonFunctions
    public static class RemoveScreenFunction implements ButtonFunction<ToddEthottGame> {
        @Override
        public void click(ToddEthottGame game) {
            game.screenManager.removeThisScreen();
        }
    }

    public static class AddGameScreenFunction implements ButtonFunction<ToddEthottGame> {
        @Override
        public void click(ToddEthottGame game) {
            game.screenManager.setNextScreen(new GameScreen(game));
        }
    }

    public static class AddSavedGameScreenFunction implements ButtonFunction<ToddEthottGame> {
        @Override
        public void click(ToddEthottGame game) {
            game.screenManager.setNextScreen(new GameScreen(game, new Save()));
        }
    }

    public static class AddMainMenuScreenFunction implements ButtonFunction<ToddEthottGame> {
        @Override
        public void click(ToddEthottGame game) {
            game.screenManager.setNextScreen(new MainMenuScreen(game));
        }
    }
}
