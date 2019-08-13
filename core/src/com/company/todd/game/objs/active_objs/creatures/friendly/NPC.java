package com.company.todd.game.objs.active_objs.creatures.friendly;

import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class NPC extends Creature {  // TODO NPC
    public NPC(ToddEthottGame game, MyAnimation animation,
               float jumpPower, float runningSpeed,
               float x, float y, float width, float height) {
        super(game, animation, jumpPower, runningSpeed, x, y, width, height);
    }
}
