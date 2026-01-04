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
        mapChanged(this, "placed animal");
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
    public void moveTo(Animal animal, MapDirection direction) {
        removeAnimal(animal);
        Vector2d currentPosition = animal.getCurrentPosition();
        MapDirection currentOrientation = animal.getCurrentOrientation().turn(direction.toInt());
        Vector2d newPosition = currentPosition.add(currentOrientation.toUnitVector());
        if (canMoveTo(newPosition)) {
            animal.move(newPosition, direction);
        }
        else{
            if (currentPosition.getY() == leftDownMapCorner.getY() || currentPosition.getY() == rightUpMapCorner.getY()){
                animal.move(positionOnBorder(currentPosition), directionOnBorder(currentOrientation));
            }
            else{
                newPosition = new Vector2d(positionOnBorder(currentPosition).getX(), newPosition.getY());
                animal.move(newPosition, currentOrientation);
            }

        }
        updateAnimal(animal);
        mapChanged(this, "moved animal");
    }

    public Vector2d positionOnBorder(Vector2d position) {
        int minX = leftDownMapCorner.getX();
        int maxX = rightUpMapCorner.getX();

        int x = position.getX();
        int y = position.getY();

        int new_x = minX + maxX - x;

        return new Vector2d(new_x,y);
    }

    private MapDirection directionOnBorder(MapDirection direction) {
        return switch (direction){
            case NORTH_EAST -> MapDirection.SOUTH_EAST;
            case EAST, WEST-> direction;
            case SOUTH_EAST -> MapDirection.NORTH_EAST;
            case NORTH, SOUTH -> direction.turn(4);
            case SOUTH_WEST -> MapDirection.NORTH_WEST;
            case NORTH_WEST ->  MapDirection.SOUTH_WEST;
        };
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
