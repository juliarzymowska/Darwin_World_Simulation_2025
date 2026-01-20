package agh.ics.oop.model.observators;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.map.WorldMap;

import java.util.List;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, String message);

    void handleDeadAnimals(List<Animal> animals);
}
