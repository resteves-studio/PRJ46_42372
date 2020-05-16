package com.gdx.tdl.util.map;

import com.badlogic.gdx.math.Vector2;

public class Point {
    private final float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float distanceFrom(Point other) {
        float dx = Math.round(x - other.x);
        float dy = Math.round(y - other.y);
        float x = (float) Math.sqrt(dx * dx + dy * dy);
        return Math.round(x);
    }

    public static Point vector2Point(Vector2 vector) {
        return new Point(Math.round(vector.x), Math.round(vector.y));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Point point = (Point) other;
        return x == point.x && y == point.y;
    }

    @Override
    public String toString() {
        return String.format("(%.4f, %.4f)", x, y);
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
