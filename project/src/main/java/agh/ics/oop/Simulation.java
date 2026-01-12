package agh.ics.oop;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.map.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals = new ArrayList<>();
    private final WorldMap map;
    // some error for config file that doesn't let user make a genotypeLength = 0!

    public Simulation(List<Vector2d> positions, WorldMap map) {
        this.map = map;

        for (Vector2d pos : positions) {
            Animal newAnimal = new Animal(pos);
            map.placeAnimal(newAnimal);
            animals.add(newAnimal);
        }
    }

    public void run() {
        int currentDay = 0;
        while (true) {
            currentDay++;

            // 1. Remove dead animals
            removeDeadAnimals(currentDay);

            if (animals.isEmpty()) {
                IO.println("All animals have died. Simulation ending.");
                break;
            }

            // 2. Move animals
            for (Animal animal : animals) {
                map.moveTo(animal);
            }

            // 3. TODO: Consume plants
            // 4. TODO: Reproduce animals
            // 5. TODO: Grow new plants
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void removeDeadAnimals(int currentDay) {
        List<Animal> deadAnimals = animals.stream()
                .filter(animal -> !animal.isAlive())
                .toList();

        for (Animal animal : deadAnimals) {
            animal.die(currentDay);
            map.removeAnimal(animal);
        }

        animals.removeAll(deadAnimals);
    }

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }
}
