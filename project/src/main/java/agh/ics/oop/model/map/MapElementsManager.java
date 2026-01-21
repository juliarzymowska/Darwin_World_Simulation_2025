package agh.ics.oop.model.map;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.util.NormalPositionGenerator;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;
import java.util.stream.Collectors;

/*
 * MapElementsManager class.
 * Manages the elements (animals and plants) on the map.
 * MANAGES ONLY AT POSITION LEVEL, NOT THE WHOLE MAP LOGIC!!!
 * Reproduction, movement and other logic should be handled in AbstractWorldMap or its subclasses.
 * */
public class MapElementsManager {
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();

    /*
     * PLANTS LOGIC
     * */

    public void addPlants(int n, int x, int y) {
        NormalPositionGenerator randomPositionGenerator = new NormalPositionGenerator(n, x, y);
        for (Vector2d v : randomPositionGenerator) {
            if (!plants.containsKey(v)) {
                this.plants.put(v, new Plant(v));
            }
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

    // get all positions where there are both animals and plants
    // used in EarthMap.consumePlants()
    public List<Vector2d> getPositionsWithAnimalsAndPlants() {
        return animals.keySet().stream()
                .filter(plants::containsKey)
                .toList();
    }


    /*
     * ANIMAL LOGIC
     * */

    public void placeAnimal(Animal animal) {
        Vector2d position = animal.getCurrentPosition();
        if (animals.containsKey(position)) {
            animals.get(position).add(animal);
        } else {
            List<Animal> list = new ArrayList<>();
            list.add(animal);
            animals.put(position, list);
        }
    }

    public void removeAnimal(Animal animal) {
        if (animal == null) return;
        Vector2d position = animal.getCurrentPosition();
        List<Animal> list = animals.get(position);

        if (list == null) return;

        list.remove(animal);
        if (list.isEmpty()) animals.remove(position);
    }

    protected Optional<Animal> reproduceAtPosition(Vector2d position, int currentDay) {
        List<Animal> animalsAtPosition = animals.get(position);
        if (animalsAtPosition.size() < 2) {
            return Optional.empty(); // Not enough animals to reproduce
        }

        // Filter animals with enough energy to reproduce
        List<Animal> eligibleAnimals = animalsAtPosition.stream()
                .filter(Animal::validateReproduction) // alive and enough energy
                .sorted() // Uses compareTo for priority
                .toList();

        if (eligibleAnimals.size() < 2) {
            return Optional.empty();
        }

        // Take top 2 animals
        Animal father = eligibleAnimals.get(0);
        Animal mother = eligibleAnimals.get(1);

        // Remove energy and update stats
        father.reproduce();
        mother.reproduce();

        return Optional.of(new Animal(father, mother, currentDay));
    }


    public Optional<List<Animal>> animalAt(Vector2d position) {
        return Optional.ofNullable(animals.get(position));
    }

    // get all positions where there are at least 2 animals
    // used for reproduction
    protected List<Vector2d> getPositionsWithMultipleAnimals() {
        return getAnimals().stream()
                .collect(Collectors.groupingBy(Animal::getCurrentPosition))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .map(Map.Entry::getKey)
                .toList();
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
            return list.getFirst();
        } else {
            return plants.get(position);
        }
    }
}
