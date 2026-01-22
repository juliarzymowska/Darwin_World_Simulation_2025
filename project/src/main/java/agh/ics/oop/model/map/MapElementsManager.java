package agh.ics.oop.model.map;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.util.NormalPositionGenerator;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/*
 * Class for managing map elements (animals and plants).
 * (should be) thread-safe
 * MANAGES PLANTS AND ANIMALS ON POSITION NOT THE WHOLE MAP
 * */
public class MapElementsManager {
    // Thread-safe map for animal lists
    private final Map<Vector2d, List<Animal>> animals = new ConcurrentHashMap<>();

    // Thread-safe map for plants
    private final Map<Vector2d, Plant> plants = new ConcurrentHashMap<>();

    /*
     * PLANTS LOGIC
     * */

    public void addPlants(int n, int x, int y) {
        NormalPositionGenerator randomPositionGenerator = new NormalPositionGenerator(n, x, y);
        for (Vector2d v : randomPositionGenerator) {
            // putIfAbsent is atomic and thread-safe : )
            plants.putIfAbsent(v, new Plant(v));
        }
    }

    public Optional<Plant> plantAt(Vector2d position) {
        return Optional.ofNullable(plants.get(position));
    }

    public void removePlant(Vector2d position) {
        plants.remove(position);
    }

    public List<Plant> getPlants() {
        return new ArrayList<>(plants.values());
    }

    List<Vector2d> getPositionsWithAnimalsAndPlants() {
        List<Vector2d> result = new ArrayList<>();
        for (Vector2d pos : animals.keySet()) {
            if (plants.containsKey(pos)) {
                result.add(pos);
            }
        }
        return result;
    }

    /*
     * ANIMAL LOGIC
     * */

    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getCurrentPosition();
        // computeIfAbsent to handle concurrent additions
        animals.computeIfAbsent(position, k -> new CopyOnWriteArrayList<>()).add(animal);
    }

    public void removeAnimal(Animal animal) {
        if (animal == null) return;
        Vector2d position = animal.getCurrentPosition();

        List<Animal> cellAnimals = animals.get(position);
        if (cellAnimals != null) {
            cellAnimals.remove(animal);
            if (cellAnimals.isEmpty()) {
                animals.remove(position);
            }
        }
    }

    protected Optional<Animal> reproduceAtPosition(Vector2d position, int currentDay) {
        List<Animal> animalsAtPosition = animals.get(position);

        if (animalsAtPosition == null || animalsAtPosition.size() < 2) {
            return Optional.empty();
        }

        List<Animal> sortedAnimals = new ArrayList<>(animalsAtPosition); // local copy to sort

        List<Animal> eligibleAnimals = sortedAnimals.stream()
                .filter(Animal::validateReproduction)
                .sorted()
                .toList();

        if (eligibleAnimals.size() < 2) {
            return Optional.empty();
        }

        Animal father = eligibleAnimals.get(0);
        Animal mother = eligibleAnimals.get(1);

        father.reproduce();
        mother.reproduce();

        // Update descendants
        Set<Animal> visited = new HashSet<>();
        updateAncestorsRecursive(mother, visited);
        updateAncestorsRecursive(father, visited);

        return Optional.of(new Animal(father, mother, currentDay));
    }

    private void updateAncestorsRecursive(Animal animal, Set<Animal> visited) {
        if (animal == null || visited.contains(animal)) {
            return;
        }
        visited.add(animal);
        animal.updateNumberOfDescendents();
        updateAncestorsRecursive(animal.getMother(), visited);
        updateAncestorsRecursive(animal.getFather(), visited);
    }

    public Optional<List<Animal>> animalAt(Vector2d position) {
        List<Animal> list = animals.get(position);
        return list == null ? Optional.empty() : Optional.of(new ArrayList<>(list)); // return a copy
    }

    public List<Vector2d> getPositionsWithMultipleAnimals() {
        List<Vector2d> result = new ArrayList<>();
        for (Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            if (entry.getValue().size() >= 2) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public List<Animal> getAnimals() {
        return animals.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /*
     * OTHERS
     * */

    public WorldElement objectAt(Vector2d position) {
        List<Animal> list = animals.get(position);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return plants.get(position);
    }
}