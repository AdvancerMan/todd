package com.company.todd.game.level.levels.arena;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;
import com.company.todd.game.level.Level;
import com.company.todd.game.objs.base.InGameObject;
import com.company.todd.game.objs.static_objs.walkable.HalfCollidedPlatform;
import com.company.todd.game.objs.static_objs.StaticObject;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.texture.AnimationInfo;
import com.company.todd.util.FloatCmp;
import com.company.todd.util.GeometrySolver;

import static com.company.todd.box2d.BodyCreator.addPolygon;
import static com.company.todd.box2d.BodyCreator.createBody;

public class MainArenaLevel extends Level {
    public MainArenaLevel(ToddEthottGame game) {
        super(game);

        createLevel();
    }

    private void createLevel() {
        AnimationInfo halfColAnim = game.animationInfos.getAnimationInfo("tmp");
        // todo halfCol anim

        Array<StaticObject> objects = new Array<StaticObject>(
            new StaticObject[] {
                new HalfCollidedPlatform(game, null, null, -1000f / 30, -10f / 30, 2000f / 30, 10f / 30),

                // TODO sticky StaticObjects (so you won't suddenly jump when you're running to the top of hill)
                new StaticObject(game, null, null, 0, 0, 0, 0, 500, 750) {
                    private Vector2 rampStart = new Vector2(225f / 30, -150f / 30);
                    private Vector2 rampEnd = new Vector2(-75f / 30, 150f / 30);

                    @Override
                    protected void createMyBody() {
                        Body body = createBody(gameProcess.getWorld(),
                                BodyDef.BodyType.StaticBody, new Vector2(-1125f / 30, 290f / 30));
                        addPolygon(body, new float[] {
                                -225f / 30, -300f / 30,
                                225f / 30, -300f / 30,
                                225f / 30, -150f / 30,
                                -75f / 30, 150f / 30,
                                -225f / 30, 150f / 30
                        });
                        addPolygon(body, new float[] {
                                -75f / 30, 150f / 30,
                                -75f / 30, 300f / 30,
                                -225f / 30, 300f / 30,
                                -225f / 30, 150f / 30
                        });
                        body.setUserData(this);

                        setBody(body);
                    }

                    // FIXME rewrite this weird code
                    // TODO check for corners?
                    @Override
                    public boolean isGroundFor(Contact contact, InGameObject object) {
//                        if (!contact.isTouching()) {
//                            return false;
//                        }
//                        Rectangle objectRect = object.getBodyAABB();
//
//                        Vector2[] points = contact.getWorldManifold().getPoints();
//                        for (int i = 0;
//                             i < contact.getWorldManifold().getNumberOfContactPoints();
//                             i++
//                        ) {
//                            Vector2 point = toPix(points[i].cpy());
//                            if (!FloatCmp.equals(point.y, objectRect.y, 1f / 30)) {
//                                continue;
//                            }
//                            point = getLocalPoint(point);
//                            if (GeometrySolver.isSegmentContainsPoint(rampStart, rampEnd, point, 1f / 30)) {
//                                return true;
//                            }
//                            if (FloatCmp.equals(point.y, 300f / 30, 1f / 30)) {
//                                return true;
//                            }
//                        }
                        return false;
                    }
                },

                new StaticObject(game, null, null, 0, 0, 0, 0, 500, 750) {
                    private Vector2 rampStart = new Vector2(-225f / 30, -150f / 30);
                    private Vector2 rampEnd = new Vector2(75f / 30, 150f / 30);

                    @Override
                    protected void createMyBody() {
                        Body body = createBody(gameProcess.getWorld(),
                                BodyDef.BodyType.StaticBody, new Vector2(1125f / 30, 290f / 30));
                        addPolygon(body, new float[] {
                                225f / 30, -300f / 30,
                                -225f / 30, -300f / 30,
                                -225f / 30, -150f / 30,
                                75f / 30, 150f / 30,
                                225f / 30, 150f / 30
                        });
                        addPolygon(body, new float[] {
                                75f / 30, 150f / 30,
                                75f / 30, 300f / 30,
                                225f / 30, 300f / 30,
                                225f / 30, 150f / 30
                        });
                        body.setUserData(this);

                        setBody(body);
                    }

                    @Override
                    public boolean isGroundFor(Contact contact, InGameObject object) {
//                        if (!contact.isTouching()) {
//                            return false;
//                        }
//                        Rectangle objectRect = object.getBodyAABB();
//
//                        Vector2[] points = contact.getWorldManifold().getPoints();
//                        for (int i = 0;
//                             i < contact.getWorldManifold().getNumberOfContactPoints();
//                             i++
//                        ) {
//                            Vector2 point = toPix(points[i].cpy());
//                            if (!FloatCmp.equals(point.y, objectRect.y, 1f / 30)) {
//                                continue;
//                            }
//                            point = getLocalPoint(point);
//                            if (GeometrySolver.isSegmentContainsPoint(rampStart, rampEnd, point, 1f / 30)) {
//                                return true;
//                            }
//                            if (FloatCmp.equals(point.y, 300f / 30, 1f / 30)) {
//                                return true;
//                            }
//                        }
                        return false;
                    }
                }
            }
        );

        for (StaticObject object : objects) {
            addObject(object);
        }
    }
}
