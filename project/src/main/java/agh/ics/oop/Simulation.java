package agh.ics.oop;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.map.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals = new ArrayList<>();
    private final HashMap<Animal, List<Integer>> moves; // dla każedego zwierzęcia jego genotyp
    private final WorldMap map;

    public Simulation(List<Vector2d> positions, WorldMap map) {
        this.map = map;
        this.moves = new HashMap<>();

        for (Vector2d pos : positions) {
            Animal newAnimal = new Animal(pos);
            moves.put(newAnimal, newAnimal.getGenotype());
            map.placeAnimal(newAnimal);
            animals.add(newAnimal);
        }
    }

    public void run() {
        if (moves.isEmpty()) {
            return;
        }
        int current_genome = 0;
        while (true) {
            for (Animal animal : animals) {
                List<Integer> genotype = moves.get(animal);
                if (genotype == null || genotype.isEmpty()) {
                    continue;
                }
                int turn = genotype.get(current_genome % genotype.size());
                MapDirection newDirection = animal.getCurrentOrientation().turn(turn);
                map.moveTo(animal, newDirection);
            }
            current_genome++;
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
