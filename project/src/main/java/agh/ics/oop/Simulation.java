package agh.ics.oop;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals = new ArrayList<>();
    private final List<MapDirection> moves;
    private final WorldMap map;

    public Simulation(List<Vector2d> positions, List<MapDirection> moves, WorldMap map) {
        this.moves = moves;
        this.map = map;

        for (Vector2d pos : positions) {
            Animal newAnimal = new Animal(pos);
            map.placeAnimal(newAnimal);
            animals.add(newAnimal);
        }
    }

    public void run() {
        if (animals.isEmpty()) {
            return;
        }
        int n = animals.size();
        for (int i = 0; i < moves.size(); i++) {
            Animal current = animals.get(i % n);
            MapDirection direction = moves.get(i);

            map.moveTo(current, direction);
        }
    }

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }
}
