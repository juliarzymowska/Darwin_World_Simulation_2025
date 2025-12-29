package agh.ics.oop.model;

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
}
