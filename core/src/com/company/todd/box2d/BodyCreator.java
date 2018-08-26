package com.company.todd.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.company.todd.game.process.GameProcess;
import com.company.todd.launcher.ToddEthottGame;
import com.company.todd.util.FloatCmp;

public class BodyCreator {
    protected static BodyDef bodyDef = new BodyDef();

    public static Body createBody(World world, BodyDef.BodyType type, Vector2 position,
                                  boolean fixedRotation, float angle, boolean isBullet) {
        bodyDef.type = type;
        bodyDef.position.set(position.scl(GameProcess.metersPerPix, GameProcess.metersPerPix));
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.angle = angle / FloatCmp.degsInRad;
        bodyDef.bullet = isBullet;

        return world.createBody(bodyDef);
    }

    public static Body createBody(World world, BodyDef.BodyType type, Vector2 position, boolean fixedRotation, float angle) {
        return createBody(world, type, position, fixedRotation, angle, false);
    }

    public static Body createBody(World world, BodyDef.BodyType type, Vector2 position, boolean isBullet) {
        return createBody(world, type, position, true, 0, isBullet);
    }

    public static Body createBody(World world, BodyDef.BodyType type, Vector2 position) {
        return createBody(world, type, position, true, 0);
    }

    public static void addBox(Body body, float width, float height, Vector2 center, float density, float angle) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width * GameProcess.metersPerPix, height * GameProcess.metersPerPix,
                center.scl(GameProcess.metersPerPix), angle / FloatCmp.degsInRad);

        body.createFixture(shape, density);
        shape.dispose();
    }

    public static void addBox(Body body, float width, float height, Vector2 center, float density) {
        addBox(body, width, height, center, density, 0);
    }

    public static void addBox(Body body, float width, float height, Vector2 center) {
        addBox(body, width, height, center, 1);
    }

    public static void addBox(Body body, float width, float height) {
        addBox(body, width, height, new Vector2(0, 0));
    }

    public static void addPolygon(Body body, float[] vertices, float density) {
        PolygonShape shape = new PolygonShape();

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] *= GameProcess.metersPerPix;
        }
        shape.set(vertices);

        body.createFixture(shape, density);
        shape.dispose();
    }

    public static void addPolygon(Body body, float[] vertices) {
        addPolygon(body, vertices, 1);
    }

    public static void addCircle(Body body, Vector2 center, float radius, float density) {
        CircleShape shape = new CircleShape();
        shape.setPosition(center.scl(GameProcess.metersPerPix));
        shape.setRadius(radius * GameProcess.metersPerPix);

        body.createFixture(shape, density);
        shape.dispose();
    }

    public static void addCircle(Body body, Vector2 center, float radius) {
        addCircle(body, center, radius, 1);
    }
}
