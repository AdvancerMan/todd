// EXPERIMENTAL

package com.company.todd.game.objs.active_objs.dangerous.flows;

import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.launcher.ToddEthottGame;

public class DamagingRay extends DangerousRay {
    private float secsAlive = 0;

    public DamagingRay(ToddEthottGame game, InGameObject owner, MyAnimation animation,
                       float damage, float flowLength,
                       float x0, float y0, float angle, float spriteWidth) {
        super(game, owner, animation, damage, flowLength, false, x0, y0, angle, spriteWidth);
    }

    @Override
    protected void interactWith(InGameObject object) {
        hit(object);
        System.out.println(hashCode() + " hitted " + object.hashCode());
    }
}
