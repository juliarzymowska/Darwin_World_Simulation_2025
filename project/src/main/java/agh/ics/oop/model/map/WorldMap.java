package agh.ics.oop.model.map;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.Vector2d;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place a new animal on the map.
     *
     * @param animal The animal to be placed on the map.

     */
    void placeAnimal(Animal animal);

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect null.
     */
    AbstractMap.SimpleEntry<Vector2d, MapDirection> moveTo(Animal animal, MapDirection direction);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);

    boolean isOccupied(Vector2d position);

    Optional<Plant> plantAt(Vector2d position);

    Optional<List<Animal>> animalAt(Vector2d position);

    List<Animal> getAnimals();

    Boundary getCurrentBounds();

    UUID getId();

    void removeObserver(MapChangeListener observer);

    void addObserver(MapChangeListener observer);

    void removeAnimal(Animal animal);

    void removePlant(Vector2d position);

    void mapChanged(WorldMap map, String message);
}