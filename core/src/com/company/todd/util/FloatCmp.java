package com.company.todd.util;

public class FloatCmp {
    public static float EPS = 1e-6f;

    public static boolean equals(float a, float b, float eps) {
        return Math.abs(a - b) <= eps;
    }

    public static boolean less(float a, float b, float eps) {
        return !equals(a, b, eps) && (a < b);
    }

    public static boolean lessOrEquals(float a, float b, float eps) {
        return equals(a, b, eps) || (a < b);
    }

    public static boolean more(float a, float b, float eps) {
        return less(b, a, eps);
    }

    public static boolean moreOrEquals(float a, float b, float eps) {
        return lessOrEquals(b, a, eps);
    }

    public static boolean equals(float a, float b) {
        return equals(a, b, EPS);
    }

    public static boolean less(float a, float b) {
        return less(a, b, EPS);
    }

    public static boolean lessOrEquals(float a, float b) {
        return lessOrEquals(a, b, EPS);
    }

    public static boolean more(float a, float b) {
        return more(a, b, EPS);
    }

    public static boolean moreOrEquals(float a, float b) {
        return moreOrEquals(a, b, EPS);
    }
}
