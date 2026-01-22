package agh.ics.oop.model.map;

import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.util.*;

import java.util.*;

import static agh.ics.oop.model.util.MapDirection.directionOnBorder;

public class EarthMap implements WorldMap {
    protected final Vector2d leftDownMapCorner = new Vector2d(0, 0);
    protected final Vector2d rightUpMapCorner;
    private final int dailyPlantNumber;
    private final MapElementsManager elementsManager = new MapElementsManager();
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID id = UUID.randomUUID();

    public EarthMap(ConfigMap configMap) {
        this.rightUpMapCorner = new Vector2d(configMap.width() - 1, configMap.height() - 1);
        this.dailyPlantNumber = configMap.dailyPlantNumber();
        elementsManager.addPlants(configMap.startPlantNumber(), rightUpMapCorner.getX(), rightUpMapCorner.getY());
    }

    public MapElementsManager getElementsManager() {
        return elementsManager;
    }

    /*
     * PLANTS LOGIC
     * */

    public void consumePlants() {
        List<Vector2d> positionsWithAnimalsAndPlants = elementsManager.getPositionsWithAnimalsAndPlants();

        for (Vector2d position : positionsWithAnimalsAndPlants) {
            Optional<List<Animal>> animalsAtPosition = elementsManager.animalAt(position);
            Optional<Plant> plantAtPosition = elementsManager.plantAt(position);

            if (animalsAtPosition.isPresent() && plantAtPosition.isPresent()) {
                List<Animal> animalList = animalsAtPosition.get();
                List<Animal> sortedAnimals = animalList.stream()
                        .sorted()
                        .toList();

                // Get the strongest animal (first in sorted list)
                Animal strongest = sortedAnimals.get(0);
                strongest.gainEnergy();

                elementsManager.removePlant(position);

                mapChanged(this, "plant at %s eaten by animal".formatted(position));
            }
        }
    }

    public void growDailyPlants() {
        elementsManager.addPlants(dailyPlantNumber, rightUpMapCorner.getX(), rightUpMapCorner.getY());
        mapChanged(this, "growing %d plants".formatted(dailyPlantNumber));
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

    @Override
    public void reproduceAnimals(int currentDay) {
        List<Vector2d> positionsWithMultipleAnimals = elementsManager.getPositionsWithMultipleAnimals();
        for (Vector2d position : positionsWithMultipleAnimals) {
            handleReproductionAtPosition(position, currentDay);
        }
    }

    protected void handleReproductionAtPosition(Vector2d position, int currentDay) {
        Optional<Animal> child = elementsManager.reproduceAtPosition(position, currentDay);
        if (child.isPresent()) {
            elementsManager.placeAnimal(child.get());
            mapChanged(this, "reproduced animals at %s".formatted(position));
        } else {
            mapChanged(this, "no reproduction at %s due to insufficient energy".formatted(position));
        }
    }

    @Override
    public void removeDeadAnimals(int currentDay) {
        List<Animal> animals = elementsManager.getAnimals();
        List<Animal> deadAnimals = animals.stream()
                .filter(animal -> animal.getEnergy() <= 0)
                .toList();

        for (Animal animal : deadAnimals) {
            animal.die(currentDay);
            elementsManager.removeAnimal(animal);
        }
        notifyAnimalsRemoved(deadAnimals);
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

        animal.move(newPosition, newOrientation);

        elementsManager.placeAnimal(animal);
        mapChanged(this, "moved animal %s to %s".formatted(currentPosition, newPosition));
    }

    private Vector2d calculateNewPosition(Animal animal) {
        Vector2d currentPosition = animal.getCurrentPosition();
        int currentGene = animal.getGenotype().getActiveGene();
        MapDirection nextOrientation = animal.getCurrentOrientation().turn(currentGene);
        Vector2d targetPosition = currentPosition.add(nextOrientation.toUnitVector());

        // NORTH / SOUTH handling
        if (targetPosition.getY() > rightUpMapCorner.getY() || targetPosition.getY() < leftDownMapCorner.getY()) {
            return positionOnBorder(currentPosition);
        }

        // EAST / WEST handling
        int newX = targetPosition.getX();
        int minX = leftDownMapCorner.getX();
        int maxX = rightUpMapCorner.getX();

        if (newX > maxX) {
            newX = minX;
        } else if (newX < minX) {
            newX = maxX;
        }

        return new Vector2d(newX, targetPosition.getY());
    }

    private MapDirection calculateNewOrientation(Animal animal) {
        int currentGene = animal.getGenotype().getActiveGene();
        MapDirection nextOrientation = animal.getCurrentOrientation().turn(currentGene);
        Vector2d targetPosition = animal.getCurrentPosition().add(nextOrientation.toUnitVector());

        if (targetPosition.getY() > rightUpMapCorner.getY() || targetPosition.getY() < leftDownMapCorner.getY()) {
            return directionOnBorder(nextOrientation);
        }

        return nextOrientation;
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
     * For simulation window
     * */

    //   returns the first animal at the given position, or null if none exist, for clicking to track animal
    public Animal getAnimalAt(Vector2d position) {
        Optional<List<Animal>> animals = elementsManager.animalAt(position);

        if (animals.isPresent() && !animals.get().isEmpty()) {
            return animals.get().get(0); // return the first animal found
        }

        return null; // No animal found here
    }

    public boolean isPreferredPosition(Vector2d position) {
        // it's always 20% of the map height in the middle - jungle
        int height = rightUpMapCorner.getY() + 1;
        int jungleHeight = (int) (height * 0.2);
        int jungleStartY = (height - jungleHeight) / 2;
        int jungleEndY = jungleStartY + jungleHeight;

        return position.getY() >= jungleStartY && position.getY() <= jungleEndY;
    }

    /*
     * Getters
     * */

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(leftDownMapCorner, rightUpMapCorner);
    }

    @Override
    public UUID getId() {
        return id;
    }

    /*
     * Observers
     * */

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

    protected void notifyAnimalsRemoved(List<Animal> animals) {
        for (MapChangeListener obs : observers) {
            obs.handleDeadAnimals(animals);
        }
    }

    /*
     * Others
     * */

    public WorldElement objectAt(Vector2d position) {
        return elementsManager.objectAt(position);
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }
}
