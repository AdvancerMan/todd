package com.company.todd.game.objs.static_objs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.launcher.ToddEthottGame;

public class Spawner extends StaticObject {  // TODO Spawner
    public Spawner(ToddEthottGame game, MyAnimation animation, InGameObject spawningObject,
                   float x, float y) {
        super(game, animation, x, y, 0, 0);
        spawningObject.kill();  // TODO do something with spawning object
        setSpawningObject(spawningObject);
    }

    @Override
    protected void createMyBody() {
        destroyMyBody();
    }

    public void setSpawningObject(InGameObject object) {

    }

    public void spawn() {

    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void draw(SpriteBatch batch, Rectangle cameraRectangle) {
        super.draw(batch, cameraRectangle);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
