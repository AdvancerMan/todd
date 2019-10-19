package com.company.todd.game.objs.active_objs.dangerous.flows;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.company.todd.game.objs.InGameObject;

import static com.company.todd.game.process.GameProcess.toPix;

class FlowRayCastCallback implements RayCastCallback {
    private Vector2 intersectionPoint;
    private InGameObject intersectedObject;

    public FlowRayCastCallback() {
        intersectionPoint = new Vector2();
        intersectedObject = null;
    }

    public Vector2 getIntersectionPoint() {
        return new Vector2(intersectionPoint);
    }

    public InGameObject getIntersectedObject() {
        return intersectedObject;
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        intersectionPoint = toPix(new Vector2(point));
        intersectedObject = (InGameObject) fixture.getBody().getUserData();
        return fraction;
    }
}
