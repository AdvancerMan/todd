package com.company.todd.game.objs.active_objs.creatures.enemy;

import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class TestEnemy extends Creature {
    public TestEnemy(ToddEthottGame game, TextureRegionInfo regionInfo, float jumpPower, float walkingSpeed, float runningSpeed, float x, float y, float width, float height) {
        super(game, regionInfo, jumpPower, walkingSpeed, runningSpeed, x, y, width, height);
    }
}
