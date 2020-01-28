package com.company.todd.game.objs.active_objs.dangerous.objects;

import com.badlogic.gdx.math.Vector2;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public class AirBomb extends DangerousObject {
    public AirBomb(ToddEthottGame game, GameProcess gameProcess, InGameObject owner, MyAnimation animation,
                   float damage, float spriteWidth, float spriteHeight,
                   float x, float y, float bodyWidth, float bodyHeight) {
        super(game, gameProcess, owner, animation, 0, damage,
                new BodyInfo(x, y, bodyWidth, bodyHeight),
                new Vector2(spriteWidth, spriteHeight));
    }

    public AirBomb(ToddEthottGame game, GameProcess gameProcess,
                   InGameObject owner, MyAnimation animation,
                   float damage, float x, float y, float width, float height) {
        this(game, gameProcess, owner, animation, damage, width, height, x, y, width, height);
    }

    @Override
    public void takeDamage(float amount) {
        kill();
    }
}
