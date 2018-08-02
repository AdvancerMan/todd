package com.company.todd.game.active_objs.dangerous;

import com.company.todd.game.active_objs.ActiveObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public abstract class DangerousObject extends ActiveObject {  // TODO DangerousObject
    public DangerousObject(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo, float speed) {
        super(game, gameProcess, regionInfo, 0, speed, speed);
    }
}
