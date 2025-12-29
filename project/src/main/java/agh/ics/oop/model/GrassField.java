package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grassMap = new HashMap<>();

    public GrassField(int n) {
        Random random = new Random();

        random.setSeed(2173);

        while (grassMap.size() < n) {
            int x = random.nextInt((int) sqrt(n * 10));
            int y = random.nextInt((int) sqrt(n * 10));

            Vector2d position = new Vector2d(x, y);

            if (!grassMap.containsKey(position)) {
                grassMap.put(position, new Grass(position));
            }
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        Animal animal = animals.get(position);
        if (animal == null)
            return grassMap.get(position);
        return animal;
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d leftDownGrassFieldCorner = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Vector2d rightUpGrassFieldCorner = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);

        Collection<WorldElement> worldElements = this.getElements();
        if (worldElements == null || worldElements.isEmpty())
            return new Boundary(new Vector2d(0, 0), new Vector2d(0, 0));

        for (WorldElement we : worldElements) {
            Vector2d pos = we.getCurrentPosition();

            leftDownGrassFieldCorner = leftDownGrassFieldCorner.lowerLeft(pos);
            rightUpGrassFieldCorner = rightUpGrassFieldCorner.upperRight(pos);
        }
        return new Boundary(leftDownGrassFieldCorner, rightUpGrassFieldCorner);
    }
    
}
