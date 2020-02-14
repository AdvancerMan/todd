package com.company.todd.game.objs.active_objs.creatures.enemy;

import com.badlogic.gdx.math.Vector2;
import com.company.todd.box2d.BodyInfo;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.active_objs.creatures.Creature;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.util.FloatCmp;

public class GuardEnemy extends Creature {
    private InGameObject player;
    private float x1, x2;

    public GuardEnemy(ToddEthottGame game, GameProcess gameProcess, MyAnimation animation,
                      InGameObject player, float x1, float x2,
                      float jumpPower, float runningSpeed,
                      float x, float y, float bodyWidth, float bodyHeight,
                      float spriteWidth, float spriteHeight) {
        super(game, gameProcess, animation, jumpPower, runningSpeed, x, y, bodyWidth, bodyHeight, spriteWidth, spriteHeight);
        this.player = player;
        this.x1 = x1;
        this.x2 = x2;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (FloatCmp.equals(player.getBody().getPosition().y, getBody().getPosition().y, getBodyAABB().height / 2)
                && isDirectedToRight() == FloatCmp.more(player.getBody().getPosition().x, getBody().getPosition().x)) {
            shoot();
        } else {
            run();
        }

        if (FloatCmp.less(getBody().getPosition().x, x1)) {
            setDirToRight(true);
            run();
        } else if (FloatCmp.more(getBody().getPosition().x, x2)) {
            setDirToRight(false);
            run();
        }
    }
}
