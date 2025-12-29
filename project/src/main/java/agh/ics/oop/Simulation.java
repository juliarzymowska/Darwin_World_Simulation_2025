package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals = new ArrayList<>();
    private final List<MoveDirection> moves;
    private final WorldMap map;

    public Simulation(List<Vector2d> positions, List<MoveDirection> moves, WorldMap map) {
        this.moves = moves;
        this.map = map;

        for (Vector2d pos : positions) {
            Animal newAnimal = new Animal(pos);
            try {
                if (map.place(newAnimal))
                    animals.add(newAnimal);
            } catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        if (animals.isEmpty()) {
            return;
        }
        int n = animals.size();
        for (int i = 0; i < moves.size(); i++) {
            Animal current = animals.get(i % n);
            MoveDirection direction = moves.get(i);

            map.move(current, direction);
        }
    }

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }
}
