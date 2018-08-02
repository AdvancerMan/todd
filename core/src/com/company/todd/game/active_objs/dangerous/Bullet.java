package com.company.todd.game.active_objs.dangerous;

import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class Bullet extends DangerousObject {  // TODO Bullet
    public Bullet(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo, float speed) {
        super(game, gameProcess, regionInfo, speed);
    }
}
