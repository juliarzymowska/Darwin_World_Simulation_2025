package agh.ics.oop.model;

public class Grass implements WorldElement {
    private final Vector2d grassPosition;

    public Grass(Vector2d grassPosition) {
        this.grassPosition = grassPosition;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public Vector2d getCurrentPosition() {
        return grassPosition;
    }

}
