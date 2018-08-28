package com.company.todd.game.objs.active_objs.dangerous;

import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class Bullet extends DangerousObject {  // TODO Bullet
    private boolean toRight;

    public Bullet(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo,
                  float x, float y,
                  float speed, float damage, boolean toRight) {
        super(game, gameProcess, regionInfo, speed, damage, x, y, 40, 10);  // TODO bullet size
        body.setBullet(true);

        this.toRight = toRight;
        if (!toRight) {
            sprite.rotate90(true);
            sprite.rotate90(true);
        }
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
