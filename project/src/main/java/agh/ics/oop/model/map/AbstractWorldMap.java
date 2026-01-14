package agh.ics.oop.model.map;

import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Vector2d leftDownMapCorner = new Vector2d(0, 0);
    protected final Vector2d rightUpMapCorner;
    private final MapElementsManager elementsManager = new MapElementsManager();
    protected final MapVisualizer vis = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();

    public AbstractWorldMap(ConfigMap configMap) {
        this.rightUpMapCorner = new Vector2d(configMap.width() - 1, configMap.height() - 1);
        elementsManager.addPlants(configMap.startPlantNumber(), rightUpMapCorner.getX(), rightUpMapCorner.getY());
    }

    @Override
    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getCurrentPosition();
        assert canMoveTo(position);
        elementsManager.placeAnimal(animal);
        mapChanged(this, "placed animal %s".formatted(position));
    }

    public void updateAnimal(Animal animal) {
        elementsManager.placeAnimal(animal);
    }

    @Override
    public void removeAnimal(Animal animal) {
        if (animal == null) return;
        elementsManager.removeAnimal(animal);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return elementsManager.objectAt(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public Optional<Plant> plantAt(Vector2d position) {
        return elementsManager.plantAt(position);
    }

    @Override
    public Optional<List<Animal>> animalAt(Vector2d position) {
        return elementsManager.animalAt(position);
    }

    @Override
    public void removePlant(Vector2d position) {
        elementsManager.removePlant(position);
    }

    @Override
    public List<Animal> getAnimals() {
        return elementsManager.getAnimals();
    }

    public List<Plant> getPlants() {
        return elementsManager.getPlants();

    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(leftDownMapCorner, rightUpMapCorner);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    @Override
    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    @Override
    public String toString() {
        Boundary currentBounds = this.getCurrentBounds();
        return vis.draw(currentBounds.leftDownMapCorner(), currentBounds.rightUpMapCorner());
    }

}
