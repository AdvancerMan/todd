package com.company.todd.util;

import com.badlogic.gdx.math.Vector2;

public class GeometrySolver {
    public static float pointToSegmentDist2(
            Vector2 segmentStart, Vector2 segmentEnd, Vector2 point
    ) {
        Vector2 segmentVector = segmentEnd.cpy().sub(segmentStart);
        Vector2 pointFromStartVector = point.cpy().sub(segmentStart);

        float dot = segmentVector.dot(pointFromStartVector);
        if (dot < 0) {
            return point.dst2(segmentStart);
        }

        float len2 = segmentVector.len2();
        if (len2 < dot) {
            return point.dst2(segmentEnd);
        }

        float crs = segmentVector.crs(pointFromStartVector);
        return Math.abs(crs * crs / len2);
    }

    public static double pointToSegmentDist(
            Vector2 segmentStart, Vector2 segmentEnd, Vector2 point
    ) {
        return Math.sqrt((double) pointToSegmentDist2(segmentStart, segmentEnd, point));
    }

    public static boolean isSegmentContainsPoint(
            Vector2 segmentStart, Vector2 segmentEnd, Vector2 point, float eps
    ) {
        return pointToSegmentDist2(segmentStart, segmentEnd, point) < eps * eps;
    }

    public static boolean isSegmentContainsPoint(
            Vector2 segmentStart, Vector2 segmentEnd, Vector2 point
    ) {
        return isSegmentContainsPoint(segmentStart, segmentEnd, point, FloatCmp.EPS);
    }
}
