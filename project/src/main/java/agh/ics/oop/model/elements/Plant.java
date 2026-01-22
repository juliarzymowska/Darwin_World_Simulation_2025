package agh.ics.oop.model.elements;

import agh.ics.oop.model.util.Vector2d;

/*
 * Class for representing a plant in the world.
 * Plants are static elements that can be eaten by animals.
 * They are represented by a position in the world and a string representation.
 * */
public class Plant implements WorldElement {
    private final Vector2d plantPosition;

    public Plant(Vector2d grassPosition) {
        this.plantPosition = grassPosition;
    }

//    @Override
//    public String toString() {
//        return "*";
//    }

    @Override
    public Vector2d getCurrentPosition() {
        return plantPosition;
    }

}
