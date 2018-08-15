package com.company.todd.game.static_objs;

import com.company.todd.game.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public abstract class StaticObject extends InGameObject {  // TODO StaticObject
    public StaticObject(ToddEthottGame game, GameProcess gameProcess, float x, float y, float width, float height) {
        super(game, gameProcess, x, y, width, height);
    }

    public StaticObject(ToddEthottGame game, GameProcess gameProcess, float width, float height) {
        super(game, gameProcess, width, height);
    }
}
