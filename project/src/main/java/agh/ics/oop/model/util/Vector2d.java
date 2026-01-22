package agh.ics.oop.model.util;

import java.util.Objects;

import static java.lang.Math.abs;

/*
 * Class representing a 2D vector with integer coordinates.
 * It provides methods for vector operations such as addition, subtraction,
 * comparison, and calculating the Manhattan distance.
 * */
public class Vector2d {
    private final int x;
    private final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d other) {
        return x <= other.getX() && y <= other.getY();
    }

    public boolean follows(Vector2d other) {
        return x >= other.getX() && y >= other.getY();
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.getX(), y + other.getY());
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.getX(), y - other.getY());
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(x, other.getX()), Math.max(y, other.getY()));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(x, other.getX()), Math.min(y, other.getY()));
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }

    public int manhattanMetricDistance(Vector2d other) {
        return abs(this.x - other.getX()) + abs(this.y - other.getY());
    }

    public MapDirection toMapDirection() {
        if (this.x == 0 && this.y == 1) return MapDirection.NORTH;
        if (this.x == 0 && this.y == -1) return MapDirection.SOUTH;
        if (this.x == 1 && this.y == 0) return MapDirection.EAST;
        if (this.x == -1 && this.y == 0) return MapDirection.WEST;
        if (this.x == 1 && this.y == -1) return MapDirection.SOUTH_EAST;
        if (this.x == -1 && this.y == -1) return MapDirection.SOUTH_WEST;
        if (this.x == -1 && this.y == 1) return MapDirection.NORTH_WEST;
        if (this.x == 1 && this.y == 1) return MapDirection.NORTH_EAST;
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d that))
            return false;
        return (x == that.getX() && y == that.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
