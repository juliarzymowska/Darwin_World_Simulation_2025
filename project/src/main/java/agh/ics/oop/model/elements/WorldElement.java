package agh.ics.oop.model.elements;

import agh.ics.oop.model.util.Vector2d;

public interface WorldElement {
    @Override
    String toString();

    Vector2d getCurrentPosition();
}
