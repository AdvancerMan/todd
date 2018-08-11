package com.company.todd.game.static_objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.company.todd.game.InGameObject;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;

public class Spawner extends StaticObject {  // TODO Spawner
    public Spawner(ToddEthottGame game, GameProcess gameProcess, InGameObject spawningObject) {
        super(game, gameProcess);
        setSpawningObject(spawningObject);
        // setPosition(x, y);
        // this.isCollidable = false;  // TODO isCollidable
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
