package agh.ics.oop.model.map;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.util.*;

import java.util.*;

public class EarthMap implements WorldMap {
    private final Vector2d leftDownMapCorner = new Vector2d(0, 0);
    private final Vector2d rightUpMapCorner;
    private final MapElementsManager elementsManager = new MapElementsManager();
    protected final MapVisualizer vis = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();

    public EarthMap(int width, int height, int startGrass) {
        this.rightUpMapCorner = new Vector2d(width - 1, height - 1);
        elementsManager.addPlants(startGrass, rightUpMapCorner.getX(),  rightUpMapCorner.getY());
    }

    //Usunęłam wyjątek, bo jak będą się pojawiać złe pozycje to jest to błąd programisty i nie chcemy tego obsługiwać w trakcie
    @Override
    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getCurrentPosition();
        assert canMoveTo(position);
        elementsManager.placeAnimal(animal);
//        mapChanged(this, "placed animal");
    }

    @Override
    public void removeAnimal(Animal animal) {
        if (animal == null) return;
        elementsManager.removeAnimal(animal);
    }

//    @Override
//    public AbstractMap.SimpleEntry<Vector2d, MapDirection> moveTo(Animal animal, MapDirection direction) {
    @Override
    public AbstractMap.SimpleEntry<Vector2d, MapDirection> moveTo(Animal animal, MapDirection direction) {
        removeAnimal(animal);
        animal.move(this, direction);
        placeAnimal(animal);
        mapChanged(this, "moved animal");
        return null;
    }

    public AbstractMap.SimpleEntry<Vector2d,MapDirection> positionAndDirectionOnBorder(Animal animal) {
        Vector2d position = animal.getCurrentPosition();
        return null;
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
    public boolean canMoveTo(Vector2d position) {
        return (position.precedes(rightUpMapCorner) && position.follows(leftDownMapCorner));
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
            observer.mapChanged(this,message);
        }
    }

    @Override
    public String toString() {
        Boundary currentBounds = this.getCurrentBounds();
        return vis.draw(currentBounds.leftDownMapCorner(), currentBounds.rightUpMapCorner());
    }
}
