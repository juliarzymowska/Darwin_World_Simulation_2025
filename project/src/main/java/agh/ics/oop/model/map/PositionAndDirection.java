package agh.ics.oop.model.map;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

public class PositionAndDirection {
    private Vector2d position;
    private MapDirection direction;
    
    public PositionAndDirection(Vector2d position, MapDirection direction) {
        this.position = position;
        this.direction = direction;
    }
    
    public Vector2d getPosition() {
        return position;
    }
    
    public MapDirection getDirection() {
        return direction;
    }
}
