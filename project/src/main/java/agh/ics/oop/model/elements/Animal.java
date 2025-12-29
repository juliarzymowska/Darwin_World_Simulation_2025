package agh.ics.oop.model.elements;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.MoveValidator;
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
            case NORTH -> "0";
            case NORTH_EAST -> "1";
            case EAST -> "2";
            case SOUTH_EAST -> "3";
            case SOUTH -> "4";
            case SOUTH_WEST -> "5";
            case WEST -> "6";
            case NORTH_WEST -> "7";
        };
    }

    public boolean isAt(Vector2d position) {
        return currentPosition.equals(position);
    }

    protected void move(MoveValidator validator, MapDirection direction) {
        if (direction == null)
            return;

        Vector2d newPosition = currentPosition.add(direction.toUnitVector());
        if (validator.canMoveTo(newPosition)) {
            currentPosition = newPosition;
            currentOrientation = direction;
        }
    }
}