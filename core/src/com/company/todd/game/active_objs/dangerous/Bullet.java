package com.company.todd.game.active_objs.dangerous;

import com.company.todd.game.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.TextureRegionInfo;

public class Bullet extends DangerousObject {  // TODO Bullet
    private boolean toRight;

    public Bullet(ToddEthottGame game, GameProcess gameProcess, TextureRegionInfo regionInfo, float speed, boolean toRight) {
        super(game, gameProcess, regionInfo, speed);

        this.toRight = toRight;
        if (!toRight) {
            sprite.rotate90(true);
            sprite.rotate90(true);
        }

        setSize(40, 10);
        if (toRight) setPosition(123, 123);
        else setPosition(500, 199);
    }

    @Override
    public boolean collideWith(InGameObject object, float delta) {
        if (super.collideWith(object, delta)) {
            kill();
            return true;
        }
        return false;
    }

    @Override
    public void update(float delta) {
        walk(toRight);
        super.update(delta);
    }
}
