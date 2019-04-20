package com.company.todd.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.company.todd.util.FloatCmp;

import static com.company.todd.game.process.GameProcess.toMeters;

public class BodyCreator {
    protected static BodyDef bodyDef = new BodyDef();
    protected static FixtureDef fixtureDef = new FixtureDef();

    public static Body createBody(World world, BodyDef.BodyType type, Vector2 position,
                                  boolean fixedRotation, float angle, boolean isBullet) {
        bodyDef.type = type;
        bodyDef.position.set(toMeters(position));
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

    protected static void createFixture(Body body, Shape shape, float density, float friction, float restitution) {
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
    }

    public static void addBox(Body body, float width, float height, Vector2 center,
                              float density, float friction, float restitution, float angle) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(toMeters(width), toMeters(height),
                toMeters(center), angle / FloatCmp.degsInRad);  // TODO addPolygon() - smooth box

        createFixture(body, shape, density, friction, restitution);
        shape.dispose();
    }

    public static void addBox(Body body, float width, float height, Vector2 center,
                              float density, float friction, float restitution) {
        addBox(body, width, height, center, density, friction, restitution, 0);
    }

    public static void addBox(Body body, float width, float height, Vector2 center) {
        addBox(body, width, height, center, 1, 0f, 0);
    }

    public static void addBox(Body body, float width, float height) {
        addBox(body, width, height, new Vector2(0, 0));
    }

    public static void addPolygon(Body body, float[] vertices, float density, float friction, float restitution) {
        PolygonShape shape = new PolygonShape();

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = toMeters(vertices[i]);
        }
        shape.set(vertices);

        createFixture(body, shape, density, friction, restitution);
        shape.dispose();
    }

    public static void addPolygon(Body body, float[] vertices) {
        addPolygon(body, vertices, 1, 0f, 0);
    }

    public static void addCircle(Body body, Vector2 center, float radius, float density, float friction, float restitution) {
        CircleShape shape = new CircleShape();
        shape.setPosition(toMeters(center));
        shape.setRadius(toMeters(radius));

        createFixture(body, shape, density, friction, restitution);
        shape.dispose();
    }

    public static void addCircle(Body body, Vector2 center, float radius) {
        addCircle(body, center, radius, 1, 0f, 0);
    }
}
