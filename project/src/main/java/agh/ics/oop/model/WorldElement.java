package agh.ics.oop.model;

public interface WorldElement {
    @Override
    String toString();

    Vector2d getCurrentPosition();
}
