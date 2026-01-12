package agh.ics.oop.model.map;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.util.NormalPositionGenerator;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;
import java.util.stream.Collectors;

public class MapElementsManager {
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();

    public void addPlants(int n, int x, int y){
        NormalPositionGenerator randomPositionGenerator = new NormalPositionGenerator(n, x, y);
        for(Vector2d v : randomPositionGenerator) {
            if(!plants.containsKey(v)) {
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

    public void deleteDeadAnimals() {
        List<Animal> allAnimals = animals.values().stream()
                .flatMap(List::stream)
                .toList();
        for (Animal animal :allAnimals){
             if (animal.getEnergy() == 0) {
                 removeAnimal(animal);
             }
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

    public Optional<List<Animal>> animalAt(Vector2d position) {
        return Optional.ofNullable(animals.get(position));
    }

    public List<Animal> getAnimals() {
        return animals.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public WorldElement objectAt(Vector2d position) {
        List<Animal> list = animals.get(position);
        if (list != null && !list.isEmpty()) {
            return list.getFirst();
        } else {
            return plants.get(position);
        }
    }


}
