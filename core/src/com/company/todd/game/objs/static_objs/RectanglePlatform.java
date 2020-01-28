package com.company.todd.game.objs.static_objs;

import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public class RectanglePlatform extends StaticObject {
    public RectanglePlatform(ToddEthottGame game, GameProcess gameProcess, MyAnimation animation,
                             float x, float y,
                             float bodyWidth, float bodyHeight,
                             float spriteWidth, float spriteHeight) {
        super(game, gameProcess, animation, x, y, bodyWidth, bodyHeight, spriteWidth, spriteHeight);
    }

    public RectanglePlatform(ToddEthottGame game, GameProcess gameProcess, MyAnimation animation,
                             float x, float y,
                             float width, float height) {
        super(game, gameProcess, animation, x, y, width, height);
    }
}
