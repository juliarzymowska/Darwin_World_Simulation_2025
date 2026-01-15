package agh.ics.oop.model.map;

import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Feromon;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;


public class FeromonMap extends EarthMap {
    private final double probability;
    private final int daysToDecrease;
    private final int smellRange;
    Random random = new Random();
    ConfigMap configMap = new ConfigMap();
    private final Map<Vector2d, Feromon> feromons = new HashMap<>();

    public FeromonMap(ConfigMap configMap) {
        super(configMap);
        this.probability = configMap.moveToFeromonProbability();
        this.daysToDecrease = configMap.daysToDecreaseFeromon();
        this.smellRange = configMap.smellRange();
    }

    /*
     * MAP LOGIC
     * */

    @Override
    public void moveTo(Animal animal) {
        Vector2d currentPos = animal.getCurrentPosition();
        // Do poprawienia na lepszą złożoność, narazie O(n^2), obsługa zapachów na granicach mapy
        if (random.nextDouble() < probability) {
            Optional<Feromon> feromonOpt = feromons.entrySet().stream()
                    .filter(e -> e.getKey().manhattanMetricDistance(currentPos) <= smellRange)
                    .map(Map.Entry::getValue)
                    .max(Comparator.naturalOrder());

            if (feromonOpt.isPresent()) {
                removeAnimal(animal);
                Feromon feromon = feromonOpt.get();
                Vector2d unitOrientation = new Vector2d(Integer.compare(feromon.getCurrentPosition().getX(), currentPos.getX()), Integer.compare(feromon.getCurrentPosition().getY(), currentPos.getY()));

                Vector2d newPosition = currentPos.add(unitOrientation);
                MapDirection newDirection = unitOrientation.toMapDirection();
                animal.move(newPosition, newDirection);
                updateAnimal(animal);
            } else {
                super.moveTo(animal);
            }

        } else {
            super.moveTo(animal);
        }
    }

    /*
     * FEROMON LOGIC
     * */
    public void addFeromon(Vector2d position) {
        if (feromons.containsKey(position)) {
            feromons.get(position).increaseFeromonValue();
        } else {
            this.feromons.put(position, new Feromon(position));
        }
    }

    public void decreaseFeromons() {
        for (Feromon feromon : feromons.values()) {
            if (feromon.getFeromonDay() % daysToDecrease == 0) {
                feromon.decreseFeromonValue();
                if (feromon.getFeromonValue() == 0) {
                    feromons.remove(feromon);
                }
            }
            feromon.increaseFeromonDay();

        }
    }
}
