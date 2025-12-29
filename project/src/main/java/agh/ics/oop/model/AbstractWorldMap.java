package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    protected final MapVisualizer vis;
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();

    public AbstractWorldMap() {
        this.vis = new MapVisualizer(this);
    }

    @Override
    public boolean place(Animal animal) throws IncorrectPositionException {
        Vector2d position = animal.getCurrentPosition();

        if (canMoveTo(position)) {
            animals.put(position, animal);
            mapChanged("Zwierzak %s ustawiony na pozycji %s.".formatted(animal, position.toString()));
            return true;
        } else
            throw new IncorrectPositionException(position);

    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        if (animal == null || !animals.containsValue(animal))
            return;

        Vector2d oldPosition = animal.getCurrentPosition();
        animal.move(this, direction);

        Vector2d newPosition = animal.getCurrentPosition();
        if (!oldPosition.equals(newPosition)) {
            animals.remove(oldPosition);
            animals.put(newPosition, animal);
            mapChanged("Zwierzak %s zmienił pozycję z %s na pozycję  %s.".formatted(animal, oldPosition.toString(),
                    newPosition, toString()));
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !animals.containsKey(position);
    }

    @Override
    public List<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }

    public abstract Boundary getCurrentBounds();

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    private void mapChanged(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        Boundary currentBounds = this.getCurrentBounds();
        return vis.draw(currentBounds.leftDownMapCorner(), currentBounds.rightUpMapCorner());
    }
}