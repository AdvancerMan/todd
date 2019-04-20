package com.company.todd.game.objs.static_objs;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public abstract class StaticObject extends InGameObject {  // TODO StaticObject
    public StaticObject(ToddEthottGame game, GameProcess gameProcess, float x, float y, float width, float height) {
        super(game, gameProcess, BodyDef.BodyType.StaticBody, x, y, width, height);
    }
}
