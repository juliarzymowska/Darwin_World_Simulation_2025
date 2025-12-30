package agh.ics.oop.model.elements;

import agh.ics.oop.model.util.Vector2d;

public class Plant implements WorldElement {
    private final Vector2d plantPosition;

    public Plant(Vector2d grassPosition) {
        this.plantPosition = grassPosition;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public Vector2d getCurrentPosition() {
        return plantPosition;
    }

}
