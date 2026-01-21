package agh.ics.oop.model.map;

import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Feromon;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;


public class FeromonMap extends EarthMap {
    private final double probability;
    private final int daysToDecrease;
    private final int smellRange;
    Random random = new Random();
    private final MapElementsManager elementsManager = super.getElementsManager();
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
                    .filter(e -> !e.getKey().equals(currentPos))
                    .filter(e -> e.getKey().manhattanMetricDistance(currentPos) <= smellRange)
                    .map(Map.Entry::getValue)
                    .max(Comparator.naturalOrder());

            if (feromonOpt.isPresent()) {
                getElementsManager().removeAnimal(animal);
                Feromon feromon = feromonOpt.get();
                Vector2d unitOrientation = new Vector2d(Integer.compare(feromon.getCurrentPosition().getX(), currentPos.getX()), Integer.compare(feromon.getCurrentPosition().getY(), currentPos.getY()));
                Vector2d newPosition = currentPos.add(unitOrientation);
                MapDirection newDirection = unitOrientation.toMapDirection();
                animal.move(newPosition, newDirection);
                elementsManager.placeAnimal(animal);
            } else {
                super.moveTo(animal);
            }

        } else {
            super.moveTo(animal);
        }
    }

    @Override
    protected void handleReproductionAtPosition(Vector2d position, int currentDay) {
        super.handleReproductionAtPosition(position, currentDay);
        addFeromon(position);
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

    public int getSmellValue(Vector2d position) {
        if (feromons.containsKey(position)) {
            return feromons.get(position).getFeromonValue();
        }
        return 0;
    }

    public void decreaseFeromons() {
        List<Feromon> toRemove = new ArrayList<>();
        for (Feromon feromon : feromons.values()) {
            if (feromon.getFeromonDay() % daysToDecrease == 0) {
                feromon.decreseFeromonValue();
                if (feromon.getFeromonValue() == 0) {
                    toRemove.add(feromon);
                }
            }
            feromon.increaseFeromonDay();
        }
        for (Feromon feromon : toRemove) {
            feromons.remove(feromon.getCurrentPosition());
        }
    }

    @Override
    public WorldElement objectAt(Vector2d pos) {
        if (super.objectAt(pos) != null) {
            return super.objectAt(pos);
        }
        return feromons.get(pos);
    }
}
