package com.company.todd.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

public class BodyInfo {
    private Shape.Type type;

    private Vector2 pos;

    // if body is rectangle
    private Vector2 size;

    // if body is circle
    private float radius;

    public BodyInfo() {
        this.pos = new Vector2();
        this.size = new Vector2();
        this.radius = 0;
        this.type = Shape.Type.Polygon;
    }

    public BodyInfo(Vector2 pos, Vector2 size) {
        this.pos = pos;
        this.size = size;
        this.radius = 0;
        this.type = Shape.Type.Polygon;
    }

    public BodyInfo(Vector2 pos, float radius) {
        this.pos = pos;
        this.size = new Vector2();
        this.radius = radius;
        this.type = Shape.Type.Circle;
    }

    public void setType(Shape.Type type) {
        this.type = type;
    }

    public void setPosition(Vector2 pos) {
        this.pos = pos;
    }

    public void setCenter(Vector2 center) {
        setPosition(new Vector2(center).add(-size.x / 2, -size.y / 2));
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Shape.Type getType() {
        return type;
    }

    public Vector2 getPosition() {
        return pos;
    }

    public Vector2 getSize() {
        return size;
    }

    public float getRadius() {
        return radius;
    }
}
