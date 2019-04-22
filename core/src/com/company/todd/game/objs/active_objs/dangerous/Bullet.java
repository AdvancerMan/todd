package com.company.todd.game.objs.active_objs.dangerous;

import com.company.todd.game.objs.InGameObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class Bullet extends DangerousObject {  // TODO Bullet
    public Bullet(ToddEthottGame game, InGameObject owner, TextureRegionInfo regionInfo,
                  float x, float y,
                  float speed, float damage, boolean toRight) {
        super(game, owner, regionInfo, speed, damage, x, y, 40, 10);  // TODO bullet size

        changeDirection(toRight);
    }

    @Override
    protected void createMyBodyAtCenter(float x, float y) {
        super.createMyBodyAtCenter(x, y);
        body.setBullet(true);
    }

    @Override
    public void update(float delta) {
        walk(toRight);
        super.update(delta);
    }

    @Override
    public void damage(float amount) {
        kill();
    }
}  // TODO BulletType
