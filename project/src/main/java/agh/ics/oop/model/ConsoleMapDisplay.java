package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int actionCounter = 1;

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        System.out.printf("Identyfikator mapy: %s%n", worldMap.getId());
        System.out.printf("W mapie miało miejsce zdarzenie: %s%n", message);
        System.out.println(worldMap.toString());
        System.out.println("To wydarzenie nr.: " + actionCounter + "!\n");
        System.out.println("===============");
        actionCounter++;
    }
}
