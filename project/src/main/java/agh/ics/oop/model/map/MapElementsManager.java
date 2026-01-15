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

    // move to maps? depends on map variant (unless pheromone can be placed at plant position)
    // instead make a addPant method here that will be called from maps?
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


    public void consumePlants(WorldMap map) {
    // TODO: move to AbstractWorldMap, make consumePlantAtPosition here instead
        Random rand = new Random();

        // for each plant, check if there are animals at that position
        List<Plant> plants = getPlants();
        for (Plant plant : plants) {
            Vector2d position = plant.getCurrentPosition();
            Optional<List<Animal>> animalsAtPosition = animalAt(position); // get animals at the plant's position

            if (animalsAtPosition.isPresent()) {
                List<Animal> animalList = animalsAtPosition.get();
                if (!animalList.isEmpty()) {
                    // find the highest energy level among the animals
                    int maxEnergy = animalList.stream()
                            .mapToInt(Animal::getEnergy)
                            .max()
                            .orElse(0);

                    // filter animals that have the highest energy
                    List<Animal> strongestAnimals = animalList.stream()
                            .filter(animal -> animal.getEnergy() == maxEnergy)
                            .toList();

                    // randomly select one of the strongest animals to eat the plant
                    Animal eater = strongestAnimals.get(rand.nextInt(strongestAnimals.size()));
                    eater.gainEnergy();

                    // remove the plant from the map
                    removePlant(position);

                    map.mapChanged(map, "plant at %s eaten by animal".formatted(position));
                }
            }
        }
    }

    /*
     * ANIMAL LOGIC
     * */

    //Usunęłam wyjątek, bo jak będą się pojawiać złe pozycje to jest to błąd programisty i nie chcemy tego obsługiwać w trakcie
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


    protected static Optional<Animal> reproduceAtPosition(List<Animal> animalsAtPosition, int currentDay) {
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
