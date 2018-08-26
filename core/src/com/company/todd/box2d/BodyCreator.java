package com.company.todd.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyCreator {
    protected static BodyDef bodyDef = new BodyDef();

    public static Body createBody(World world, BodyDef.BodyType type, Vector2 position,
                                  boolean fixedRotation, float angle, boolean isBullet) {
        bodyDef.type = type;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.angle = angle;
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

    public static Body addBox(Body body, float width, float height, Vector2 center, float density, float angle) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height, center, angle);

        body.createFixture(shape, density);
        shape.dispose();

        return body;
    }

    public static Body addBox(Body body, float width, float height, Vector2 center, float density) {
        return addBox(body, width, height, center, density, 0);
    }

    public static Body addBox(Body body, float width, float height, Vector2 center) {
        return addBox(body, width, height, center, 1);
    }

    public static Body addBox(Body body, float width, float height) {
        return addBox(body, width, height, new Vector2(0, 0));
    }

    public static Body addPolygon(Body body, float[] vertices, float density) {
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        body.createFixture(shape, density);
        shape.dispose();

        return body;
    }

    public static Body addPolygon(Body body, float[] vertices) {
        return addPolygon(body, vertices, 1);
    }

    public static Body addCircle(Body body, Vector2 center, float radius, float density) {
        CircleShape shape = new CircleShape();
        shape.setPosition(center);
        shape.setRadius(radius);

        body.createFixture(shape, density);
        shape.dispose();

        return body;
    }

    public static Body addCircle(Body body, Vector2 center, float radius) {
        return addCircle(body, center, radius, 1);
    }
}
