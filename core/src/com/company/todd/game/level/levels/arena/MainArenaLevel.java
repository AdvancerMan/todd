package com.company.todd.game.level.levels.arena;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.company.todd.game.animations.MyAnimation;
import com.company.todd.game.level.Level;
import com.company.todd.game.objs.InGameObject;
import com.company.todd.game.objs.static_objs.HalfCollidedPlatform;
import com.company.todd.game.objs.static_objs.PlatformWithUpperLayer;
import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.AnimationInfo;

import static com.company.todd.box2d.BodyCreator.addPolygon;
import static com.company.todd.box2d.BodyCreator.createBody;

public class MainArenaLevel extends Level {
    public MainArenaLevel(ToddEthottGame game) {
        super(game);

        createLevel();
    }

    private void createLevel() {
        AnimationInfo halfColAnim = game.animationInfos.getAnimationInfo("tmp"); // todo halfCol anim

        Array<StaticObject> objects = new Array<StaticObject>(new StaticObject[]{
                new HalfCollidedPlatform(game, null, -1000, -10, 2000, 10),
                new StaticObject(game, null, 0, 0, 0, 0, 500, 750) {
                    @Override
                    protected void createMyBody() {
                        Body body = createBody(gameProcess.getWorld(),
                                BodyDef.BodyType.StaticBody, new Vector2(-1125, 290));
                        addPolygon(body, new float[] {
                                -225, -300,
                                225, -300,
                                225, -150,
                                -75, 150,
                                -225, 150
                        });
                        addPolygon(body, new float[] {
                                -75, 150,
                                -75, 300,
                                -225, 300,
                                -225, 150
                        });
                        body.setUserData(this);

                        setBody(body);
                    }
                },
                new StaticObject(game, null, 0, 0, 0, 0, 500, 750) {
                    @Override
                    protected void createMyBody() {
                        Body body = createBody(gameProcess.getWorld(),
                                BodyDef.BodyType.StaticBody, new Vector2(1125, 290));
                        addPolygon(body, new float[] {
                                225, -300,
                                -225, -300,
                                -225, -150,
                                75, 150,
                                225, 150
                        });
                        addPolygon(body, new float[] {
                                75, 150,
                                75, 300,
                                225, 300,
                                225, 150
                        });
                        body.setUserData(this);

                        setBody(body);
                    }
                }
        });

        for (StaticObject object : objects) {
            addObject(object);
        }
    }
}
