package agh.ics.oop;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.stats.CSVSaver;
import agh.ics.oop.model.stats.SimulationStatsTracker;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.map.WorldMap;

import java.util.List;

public class Simulation implements Runnable {
    //    private final List<Animal> animals = new ArrayList<>();
    private final WorldMap map;
    private final SimulationStatsTracker statsTracker;
    // some error for config file that doesn't let user make a genotypeLength = 0!
    // error for config file when number of grass per day is bigger than map tiles number
    // random animal position generation

    // TODO: make new Simulation with ConfigAnimal (and random animal placement on map) and ConfigMap!

    // (for testing) Constructor of Simulation class.
    public Simulation(List<Vector2d> positions, WorldMap map) {
        this.map = map;
        this.statsTracker = new SimulationStatsTracker(map);
        map.addObserver(statsTracker);

        for (Vector2d pos : positions) {
            Animal newAnimal = new Animal(pos);
            map.placeAnimal(newAnimal);
        }
        //CSV saver obserwuje zmiany statystyk
        statsTracker.addObserver(new CSVSaver());

    }

    /*
     * Runs the simulation until all animals are dead.
     * Each day consists of the following steps:
     * 1. Remove dead animals from the map.
     * 2. Move all animals to new positions.
     * 3. Animals consume plants at their new positions.
     * 4. (TODO) Animals reproduce if they have enough energy.
     * 5. Grow new plants on the map.
     * */
    public void run() {
        int currentDay = 0;
        while (true) {
            currentDay++;
            if (performDayCycle(currentDay)) {
                IO.println("Day %d completed.".formatted(currentDay));
            } else {
                break;
            }
        }
    }

    // Performs a single day cycle of the simulation.
    private boolean performDayCycle(int currentDay) {
        // 1. Remove dead animals
        map.removeDeadAnimals(currentDay);
        List<Animal> animals = map.getElementsManager().getAnimals();

        // 2. Move animals
        for (Animal animal : animals) {
            map.moveTo(animal);
        }

        // 3. Consume plants
        map.consumePlants();
        // 4. TODO: Reproduce animals
        // 5. Grow new plants
        map.growPlants(10); // TODO: apply from config number of new plants per day

        statsTracker.printStats(currentDay);
        if (animals.isEmpty()) {
            IO.println("All animals have died. Simulation ending.");
            return false;
        }

        return true;
    }

    public List<Animal> getAnimals() {
        return List.copyOf(map.getElementsManager().getAnimals());
    }
}
