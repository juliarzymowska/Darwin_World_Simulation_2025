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
        while (true) {
            for (Animal animal : animals) {
                map.moveTo(animal);
                animal.getGenotype().moveToNextGene(); // next movements here/in animal/in earthmap????
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }
}
