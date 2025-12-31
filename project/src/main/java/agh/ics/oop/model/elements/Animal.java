package agh.ics.oop.model.elements;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

public class Animal implements WorldElement {
    private MapDirection currentOrientation = MapDirection.NORTH;
    private Vector2d currentPosition;

    public Animal() {
        this.currentPosition = (new Vector2d(2, 2));
    }

    public Animal(Vector2d position) {
        this.currentPosition = position;
    }

    public MapDirection getCurrentOrientation() {
        return currentOrientation;
    }

    public Vector2d getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public String toString() {
        return switch (currentOrientation) {
            case NORTH -> "⭡";
            case NORTH_EAST -> "↗";
            case EAST -> "⭢";
            case SOUTH_EAST -> "↘";
            case SOUTH -> "⭣";
            case SOUTH_WEST -> "↙";
            case WEST -> "⭠";
            case NORTH_WEST -> "↖";
        };
    }

    public boolean isAt(Vector2d position) {
        return currentPosition.equals(position);
    }

    public void move(Vector2d newPosition, MapDirection newOrientation) {
            currentPosition = newPosition;
            currentOrientation = newOrientation;
    }

}