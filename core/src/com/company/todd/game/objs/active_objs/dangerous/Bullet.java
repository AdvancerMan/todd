package com.company.todd.game.objs.active_objs.dangerous;

import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public class Bullet extends DangerousObject {  // TODO Bullet
    public Bullet(ToddEthottGame game, GameProcess gameProcess,
                  InGameObject owner, MyAnimation animation,
                  float x, float y,
                  float speed, float damage, boolean toRight) {
        super(game, gameProcess, owner, animation, speed, damage, x, y, 40, 10);  // TODO bullet size

        changeDirection(toRight);
    }

    @Override
    public void update(float delta) {
        run(toRight);
        super.update(delta);
    }

    @Override
    public void takeDamage(float amount) {
        kill();
    }
}  // TODO BulletType
