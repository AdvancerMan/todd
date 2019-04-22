package com.company.todd.game.objs.active_objs.dangerous;

import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.active_objs.ActiveObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public abstract class DangerousObject extends ActiveObject {  // TODO DangerousObject
    protected float damage;
    protected InGameObject owner;
    private SafetyType ownerSafe;

    public DangerousObject(ToddEthottGame game, InGameObject owner, TextureRegionInfo regionInfo,
                           float speed, float damage,
                           float x, float y, float width, float height) {
        super(game, regionInfo, speed, x, y, width, height);
        this.damage = damage;
        this.owner = owner;
        ownerSafe = SafetyType.SAFE;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (ownerSafe.equals(SafetyType.SAFE)) {
            ownerSafe = SafetyType.UPDATING;
        } else if (ownerSafe.equals(SafetyType.UPDATING)) {
            ownerSafe = SafetyType.DANGEROUS;
        }
    }

    public boolean hit(InGameObject object) {
        if (owner.equals(object) && !ownerSafe.equals(SafetyType.DANGEROUS)) {
            ownerSafe = SafetyType.SAFE;
            return false;
        }

        if (object instanceof ActiveObject) {
            ((ActiveObject)object).damage(damage);
        }

        kill();
        return true;
    }

    private enum SafetyType {
        SAFE, UPDATING, DANGEROUS
    }
}
