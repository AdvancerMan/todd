package com.company.todd.game.active_objs;

import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class Bullet extends ActiveObject {  // TODO Bullet
    public Bullet(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
                  float jumpPower, float walkingSpeed, float runningSpeed) {
        super(game, gameProcess, regionInfo, jumpPower, walkingSpeed, runningSpeed);
    }
}
