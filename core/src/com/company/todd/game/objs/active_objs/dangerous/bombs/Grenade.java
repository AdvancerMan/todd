package com.company.todd.game.objs.active_objs.dangerous.bombs;

import com.badlogic.gdx.math.Vector2;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public class Grenade extends Bomb {
    public Grenade(ToddEthottGame game, GameProcess gameProcess,
                   InGameObject owner, MyAnimation animation,
                   Vector2 throwingVelocity, float damage, float x, float y,
                   float bodyRadius, float spriteWidth, float spriteHeight) {
        super(game, gameProcess, owner, animation, 0, damage, x, y, bodyRadius, spriteWidth, spriteHeight);
        setVelocity(throwingVelocity);
    }

    @Override
    protected void updatePosition(float delta) {}
}
