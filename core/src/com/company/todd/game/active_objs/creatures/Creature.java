package com.company.todd.game.active_objs.creatures;

import com.company.todd.game.active_objs.ActiveObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public abstract class Creature extends ActiveObject {  // TODO Creature
    // TODO health, energy
    // TODO hit(float damage);

    public Creature(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
                    float jumpPower, float walkingSpeed, float runningSpeed) {
        super(game, gameProcess, regionInfo, jumpPower, walkingSpeed, runningSpeed);
    }
}
