package agh.ics.oop.model.map;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MoveValidator;
import agh.ics.oop.model.util.Vector2d;

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
    void moveTo(Animal animal);

    WorldElement objectAt(Vector2d position);

    boolean isOccupied(Vector2d position);

    void removeDeadAnimals(int currentDay);

    Boundary getCurrentBounds();

    UUID getId();

    void removeObserver(MapChangeListener observer);

    void addObserver(MapChangeListener observer);

    void mapChanged(WorldMap map, String message);

    /*
     * Remove plants that are eaten by animals according to the simulation rules.
     * */
    void consumePlants();

    /*
     * Grow new plants on the map according to the simulation rules.
     * */
    void growPlants(int n);

    MapElementsManager getElementsManager();
}