package com.company.todd.game.objs.static_objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public class Spawner extends StaticObject {  // TODO Spawner
    public Spawner(ToddEthottGame game, GameProcess gameProcess, InGameObject spawningObject,
                   float x, float y) {
        super(game, gameProcess, x, y, 0, 0);
        spawningObject.kill();  // TODO do something with spawning object
        setSpawningObject(spawningObject);
    }

    @Override
    protected void createMyBody(BodyDef.BodyType bodyType) {
        body = null;
    }

    public void setSpawningObject(InGameObject object) {

    }

    public void spawn() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {

    }

    @Override
    public void dispose() {

    }
}
