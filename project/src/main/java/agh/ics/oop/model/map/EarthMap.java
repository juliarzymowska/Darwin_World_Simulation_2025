package agh.ics.oop.model.map;

import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.util.*;

import java.util.*;

public class EarthMap extends AbstractWorldMap {
    //Zrobić funkcje nextday() usuwającą zwierzęta, a w feromonmap zmniejszające intensywnośc zapachów.

    public EarthMap(ConfigMap configMap) {
        super(configMap);
    }

    @Override
    public void moveTo(Animal animal) {
        removeAnimal(animal);

        Vector2d currentPosition = animal.getCurrentPosition();
        MapDirection newOrientation = calculateNewOrientation(animal);
        Vector2d newPosition = calculateNewPosition(animal);

        boolean isAlive = animal.move(newPosition, newOrientation);

        if (isAlive) {
            super.placeAnimal(animal);
            mapChanged(this, "moved animal %s to %s".formatted(currentPosition, newPosition));
        } else {
            mapChanged(this, "animal died %s at %s".formatted(currentPosition, newPosition));
        }
    }

    private Vector2d calculateNewPosition(Animal animal) {
        Vector2d currentPosition = animal.getCurrentPosition();
        int currentGene = animal.getGenotype().getActiveGene();
        MapDirection newOrientation = animal.getCurrentOrientation().turn(currentGene);
        Vector2d newPosition = currentPosition.add(newOrientation.toUnitVector());

        if (canMoveTo(newPosition)) {
            return newPosition;
        } else {
            if (currentPosition.getY() == leftDownMapCorner.getY() || currentPosition.getY() == rightUpMapCorner.getY()) {
                return positionOnBorder(currentPosition);
            } else {
                return new Vector2d(positionOnBorder(currentPosition).getX(), newPosition.getY());
            }

        }
    }

    private MapDirection calculateNewOrientation(Animal animal) {
        int currentGene = animal.getGenotype().getActiveGene();
        MapDirection newOrientation = animal.getCurrentOrientation().turn(currentGene);

        if (isOnVerticalBorder(animal.getCurrentPosition())) {
            return directionOnBorder(newOrientation);
        }
        return newOrientation;
    }

    private boolean isOnVerticalBorder(Vector2d position) {
        return position.getY() == leftDownMapCorner.getY() || position.getY() == rightUpMapCorner.getY();
    }

    public Vector2d positionOnBorder(Vector2d position) {
        int minX = leftDownMapCorner.getX();
        int maxX = rightUpMapCorner.getX();

        int x = position.getX();
        int y = position.getY();

        int new_x = minX + maxX - x;

        return new Vector2d(new_x, y);
    }

    public MapDirection directionOnBorder(MapDirection direction) {
        return switch (direction) {
            case NORTH_EAST -> MapDirection.SOUTH_EAST;
            case EAST, WEST -> direction;
            case SOUTH_EAST -> MapDirection.NORTH_EAST;
            case NORTH, SOUTH -> direction.turn(4);
            case SOUTH_WEST -> MapDirection.NORTH_WEST;
            case NORTH_WEST -> MapDirection.SOUTH_WEST;
        };
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.precedes(rightUpMapCorner) && position.follows(leftDownMapCorner));
    }
}
