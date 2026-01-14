package agh.ics.oop;

import agh.ics.oop.configuration.ConfigAnimal;
import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.map.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    // Lists to keep track of alive and dead animals for keeping statistics
    private final List<Animal> aliveAnimals = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    private final WorldMap map;

    // some error for config file that doesn't let user make a genotypeLength = 0m!
    // error for config file when number of grass per day is bigger than map tiles number

    // (for tests) Constructor of Simulation class.
    public Simulation(List<Vector2d> positions, WorldMap map) {
        this.map = map;

        for (Vector2d pos : positions) {
            Animal newAnimal = new Animal(pos);
            map.placeAnimal(newAnimal);
            aliveAnimals.add(newAnimal);
        }
    }

    public Simulation(ConfigAnimal configAnimal, ConfigMap configMap, WorldMap map) {
        this.map = map;
        generateAnimalsOnMap(configAnimal, configMap, map);
    }

    public void run() {
        int currentDay = 0;
        while (true) {
            currentDay++;

            // 1. Remove dead animals
            removeDeadAnimals(currentDay);

            if (aliveAnimals.isEmpty()) {
                IO.println("All animals have died. Simulation ending.");
                break;
            }

            // 2. Move animals
            for (Animal animal : aliveAnimals) {
                map.moveTo(animal);
            }

            // 3. Consume plants
            map.consumePlants();
            // 4. TODO: Reproduce animals
            // 5. Grow new plants
            map.growPlants(10); // TODO: apply from config number of new plants per day
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                break;
//            }
        }
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
                aliveAnimals.add(newAnimal);
            } catch (Exception e) {
                System.err.println("Failed to place animal: " + e.getMessage());
            }
        }
    }

    private void removeDeadAnimals(int currentDay) {
        List<Animal> currentDayDeadAnimals = aliveAnimals.stream()
                .filter(animal -> !animal.isAlive())
                .toList();

        for (Animal animal : currentDayDeadAnimals) {
            animal.die(currentDay);
            map.removeAnimal(animal);
        }

        aliveAnimals.removeAll(currentDayDeadAnimals);
        deadAnimals.addAll(currentDayDeadAnimals);
    }

    public List<Animal> getAnimals() {
        return List.copyOf(aliveAnimals);
    }
}
