package com.company.todd.game.objs.active_objs.dangerous;

import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public abstract class DangerousObject extends ActiveObject {  // TODO DangerousObject
    protected float damage;
    protected InGameObject owner;
    protected boolean ownerSafe;

    public DangerousObject(ToddEthottGame game, InGameObject owner, TextureRegionInfo regionInfo,
                           float speed, float damage,
                           float x, float y, float width, float height) {
        super(game, regionInfo, speed, x, y, width, height);
        this.damage = damage;
        this.owner = owner;
        this.ownerSafe = true;
    }

    public boolean isOwner(InGameObject object) {
        return object.equals(owner);
    }

    public void endContactWith(InGameObject object) {
        if (object.equals(owner)) {
            ownerSafe = false;
        }
    }

    public void hit(InGameObject object) {
        if (owner.equals(object) && ownerSafe) {
            return;
        }

        if (object instanceof ActiveObject) {
            ((ActiveObject)object).damage(damage);
        }

        kill();
    }
}
