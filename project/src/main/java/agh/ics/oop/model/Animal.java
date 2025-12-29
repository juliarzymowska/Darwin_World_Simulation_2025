package agh.ics.oop.model;

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
            case NORTH -> "^";
            case SOUTH -> "v";
            case EAST -> ">";
            case WEST -> "<";
        };
    }

    public boolean isAt(Vector2d position) {
        return currentPosition.equals(position);
    }

    protected void move(MoveValidator validator, MoveDirection direction) {
        if (direction == null)
            return;

        Vector2d newPosition = currentPosition;
        switch (direction) {
            case MoveDirection.LEFT:
                currentOrientation = currentOrientation.previous();
                break;
            case MoveDirection.RIGHT:
                currentOrientation = currentOrientation.next();
                break;
            case MoveDirection.FORWARD:
                newPosition = currentPosition.add(currentOrientation.toUnitVector());
                break;
            case MoveDirection.BACKWARD:
                newPosition = currentPosition.subtract(currentOrientation.toUnitVector());
                break;
        }
        if (validator.canMoveTo(newPosition)) {
            currentPosition = newPosition;
        }
    }
}