package com.company.todd.game.objs.active_objs.dangerous.bombs;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.active_objs.dangerous.DangerousObject;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

import static com.company.todd.util.FloatCmp.less;

public abstract class Bomb extends DangerousObject {
    public static final float EXPLOSION_TIME = 0.5f;

    public Bomb(ToddEthottGame game, GameProcess gameProcess,
                InGameObject owner, MyAnimation animation,
                float speed, float damage,
                BodyInfo bodyInfo, Vector2 spriteSize) {
        super(game, gameProcess, owner, animation, speed, damage, bodyInfo, spriteSize);
    }

    public Bomb(ToddEthottGame game, GameProcess gameProcess,
                InGameObject owner, MyAnimation animation,
                float speed, float damage, float x, float y,
                float bodyWidth, float bodyHeight,
                float spriteWidth, float spriteHeight) {
        super(game, gameProcess, owner, animation, speed, damage, x, y, bodyWidth, bodyHeight, spriteWidth, spriteHeight);
    }

    public Bomb(ToddEthottGame game, GameProcess gameProcess,
                InGameObject owner, MyAnimation animation,
                float speed, float damage, float x, float y,
                float width, float height) {
        super(game, gameProcess, owner, animation, speed, damage, x, y, width, height);
    }

    public Bomb(ToddEthottGame game, GameProcess gameProcess,
                InGameObject owner, MyAnimation animation,
                float speed, float damage, float x, float y,
                float bodyRadius, float spriteWidth, float spriteHeight) {
        super(game, gameProcess, owner, animation, speed, damage, x, y, bodyRadius, spriteWidth, spriteHeight);
    }

    @Override
    public void takeDamage(float amount) {
        super.takeDamage(amount);

        // FIXME if victim body is not active bomb should not be killed
        kill();
    }

    @Override
    public void kill() {
        super.kill();

        Rectangle rect = getBodyAABB();
        Vector2 center = rect.getCenter(new Vector2());
        float radius = rect.height;
        if (less(radius, rect.width)) radius = rect.width;

        gameProcess.addObject(new Explosion(game, null, null,
                game.animationInfos.getAnimation("explosion"),
                damage, EXPLOSION_TIME, center.x, center.y, radius,
                radius * 2, radius * 2)
        );
    }
}
