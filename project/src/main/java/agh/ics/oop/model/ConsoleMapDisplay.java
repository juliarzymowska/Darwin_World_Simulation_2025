package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.observators.MapChangeListener;

import java.util.List;

public class ConsoleMapDisplay implements MapChangeListener {
    private int actionCounter = 1;

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        System.out.printf("Map ID: %s%n", worldMap.getId());
        System.out.println(message);
        System.out.println(worldMap);
        System.out.println("Even counter: " + actionCounter + "!\n");
        System.out.println("===============");
        actionCounter++;
    }

    @Override
    public void handleDeadAnimals(List<Animal> animals) {

    }
}
