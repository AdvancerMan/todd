package com.company.todd.game.active_objs.creatures;

import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class NPC extends Creature {  // TODO NPC
    public NPC(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
               float jumpPower, float walkingSpeed, float runningSpeed,
               float x, float y, float width, float height) {
        super(game, gameProcess, regionInfo, jumpPower, walkingSpeed, runningSpeed, x, y, width, height);
    }
}
