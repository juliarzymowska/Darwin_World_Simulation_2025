package agh.ics.oop.model.map;

import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.util.*;

import java.util.*;

public class EarthMap implements WorldMap {
    //Zrobić funkcje nextday() usuwającą zwierzęta, a w feromonmap zmniejszające intensywnośc zapachów. (ja bym w
    // przeniosła removeDeadAnimals (działa tak samo niezależnie od warianty mapy) do MapELemetsManager i wywoływałą
    // ją w performDayCycle() w Simulation)

    protected final Vector2d leftDownMapCorner = new Vector2d(0, 0);
    protected final Vector2d rightUpMapCorner;
    private final MapElementsManager elementsManager = new MapElementsManager();
    protected final MapVisualizer vis = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();

    public EarthMap(ConfigMap configMap) {
        this.rightUpMapCorner = new Vector2d(configMap.width() - 1, configMap.height() - 1);
        elementsManager.addPlants(configMap.startPlantNumber(), rightUpMapCorner.getX(), rightUpMapCorner.getY());
    }

    public MapElementsManager getElementsManager() {
        return elementsManager;
    }

    /*
     * PLANTS LOGIC
     * */

    //TODO: remake it so in MapElementsManager is only consumePlantAtPosition
    public void consumePlants() {
        elementsManager.consumePlants(this);
    }

    public void growPlants(int n) {
        elementsManager.addPlants(n, rightUpMapCorner.getX(), rightUpMapCorner.getY());
        mapChanged(this, "growing %d plants".formatted(n));
    }

    /*
     * ANIMALS LOGIC
     * */

    @Override
    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getCurrentPosition();
        assert canMoveTo(position);
        elementsManager.placeAnimal(animal);
        mapChanged(this, "placed animal %s".formatted(position));
    }


    public void removeDeadAnimals(int currentDay) {
        List<Animal> animals = elementsManager.getAnimals();
        List<Animal> deadAnimals = animals.stream()
                .filter(animal -> !animal.isAlive())
                .toList();

        for (Animal animal : deadAnimals) {
            animal.die(currentDay);
            elementsManager.removeAnimal(animal);
        }
    }

    /*
     * MOVEMENT
     * */
    @Override
    public void moveTo(Animal animal) {
        elementsManager.removeAnimal(animal);

        Vector2d currentPosition = animal.getCurrentPosition();
        MapDirection newOrientation = calculateNewOrientation(animal);
        Vector2d newPosition = calculateNewPosition(animal);

        boolean isAlive = animal.move(newPosition, newOrientation);

        if (isAlive) {
            elementsManager.placeAnimal(animal);
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
            if (isOnVerticalBorder(newPosition)) {
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
            return newOrientation.directionOnBorder(newOrientation);
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

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.precedes(rightUpMapCorner) && position.follows(leftDownMapCorner));
    }

    /*
     * OTHERS
     * */

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

    // for console display purposes (MapVisualizer)
    public WorldElement objectAt(Vector2d position) {
        return elementsManager.objectAt(position);
    }

    // same as above
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public String toString() {
        Boundary currentBounds = this.getCurrentBounds();
        return vis.draw(currentBounds.leftDownMapCorner(), currentBounds.rightUpMapCorner());
    }
}
