package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

public class RectangularMap extends AbstractWorldMap {
    private final Vector2d leftDownMapCorner = new Vector2d(0, 0);
    private final Vector2d rightUpMapCorner;

    public RectangularMap(int width, int height) {
        this.rightUpMapCorner = new Vector2d(width - 1, height - 1);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.precedes(rightUpMapCorner) && position.follows(leftDownMapCorner) && !isOccupied(position));
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(leftDownMapCorner, rightUpMapCorner);
    }
}
