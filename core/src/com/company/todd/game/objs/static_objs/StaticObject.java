package com.company.todd.game.objs.static_objs;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.launcher.ToddEthottGame;

public abstract class StaticObject extends InGameObject {  // TODO StaticObject
    public StaticObject(ToddEthottGame game, float x, float y, float width, float height) {
        super(game, BodyDef.BodyType.StaticBody, x, y, width, height);
    }
}
