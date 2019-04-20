package com.company.todd.game.objs.active_objs.dangerous;

import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public abstract class DangerousObject extends ActiveObject {  // TODO DangerousObject
    protected float damage;

    public DangerousObject(ToddEthottGame game, TextureRegionInfo regionInfo,
                           float speed, float damage,
                           float x, float y, float width, float height) {
        super(game, regionInfo, speed, x, y, width, height);
        this.damage = damage;
    }

    public void hit(InGameObject object) {
        if (object instanceof ActiveObject) {
            ((ActiveObject)object).damage(damage);
        }

        kill();
    }
}
