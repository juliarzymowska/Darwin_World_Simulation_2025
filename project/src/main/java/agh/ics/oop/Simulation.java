package agh.ics.oop;

import agh.ics.oop.configuration.ConfigAnimal;
import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.map.WorldMap;

import java.util.List;

public class Simulation implements Runnable {
    private final WorldMap map;
    // TODO: some error for config file that doesn't let user make a genotypeLength = 0! fix genotypeLength 1
    // TODO: error for config file when number of grass per day is bigger than map tiles number

    // (for testing) Constructor of Simulation class.
    public Simulation(List<Vector2d> positions, WorldMap map) {
        this.map = map;

        for (Vector2d pos : positions) {
            Animal newAnimal = new Animal(pos);
            map.placeAnimal(newAnimal);
        }
    }

    public Simulation(ConfigAnimal configAnimal, ConfigMap configMap, WorldMap map) {
        this.map = map;
        generateAnimalsOnMap(configAnimal, configMap, map);
    }

    /*
     * Runs the simulation until all animals are dead.
     * Each day consists of the following steps:
     * 1. Remove dead animals from the map.
     * 2. Move all animals to new positions.
     * 3. Animals consume plants at their new positions.
     * 4. Animals reproduce if they have enough energy.
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

        if (animals.isEmpty()) {
            IO.println("All animals have died. Simulation ending.");
            return false;
        }


        // 2. Move animals
        for (Animal animal : animals) {
            map.moveTo(animal);
        }

        // 3. Consume plants
        map.consumePlants();
        // 4. Reproduce animals
        map.reproduceAnimals(currentDay);
        // 5. Grow new plants
        map.growPlants(10); // TODO: apply from config number of new plants per day
        return true;
    }


    private void generateAnimalsOnMap(ConfigAnimal configAnimal, ConfigMap configMap, WorldMap map) {
        for (int i = 0; i < configAnimal.initialAnimalCount(); i++) {
            // Random position generation, could lead to infinite loop if map is full
            // Can place multiple animals on the same position
            Vector2d position = new Vector2d(
                    (int) (Math.random() * configMap.width()),
                    (int) (Math.random() * configMap.height())
            );
            Animal newAnimal = new Animal(position);
            try {
                map.placeAnimal(newAnimal);
            } catch (Exception e) {
                System.err.println("Failed to place animal: " + e.getMessage());
            }
        }
    }

    public List<Animal> getAnimals() {
        return List.copyOf(map.getElementsManager().getAnimals());
    }
}
