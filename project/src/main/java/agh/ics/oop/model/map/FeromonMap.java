package agh.ics.oop.model.map;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Feromon;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;


public class FeromonMap extends EarthMap{
    private final int probability; //from config
    private final int daysToDecrese; //from config
    private final int range; // from config
    Random random = new Random();
    private final Map<Vector2d, Feromon> feromons = new HashMap<>();

    public FeromonMap(int width, int height, int startGrass, int probability, int daysToDecrese, int range) {
        super(width, height, startGrass);
        this.probability = probability; // from config
        this.daysToDecrese = daysToDecrese; // from config
        this.range = range; // from config
    }

    @Override
    public void moveTo(Animal animal){
        Vector2d currentPos = animal.getCurrentPosition();
        // Do poprawienia na lepszą złożoność, narazie O(n^2), obsługa zapachów na granicach mapy
        if (random.nextDouble() < probability){
            Optional<Feromon> feromonOpt = feromons.entrySet().stream()
                    .filter(e -> e.getKey().manhattanMetricDistance(currentPos) <= range)
                    .map(Map.Entry::getValue)
                    .max(Comparator.naturalOrder());

            if (feromonOpt.isPresent()){
                removeAnimal(animal);
                Feromon feromon = feromonOpt.get();
                Vector2d unitOrientation = new Vector2d(Integer.compare(feromon.getCurrentPosition().getX(), currentPos.getX()),Integer.compare(feromon.getCurrentPosition().getY(), currentPos.getY()));

                Vector2d newPosition = currentPos.add(unitOrientation);
                MapDirection newDirection = unitOrientation.toMapDirection();
                animal.move(newPosition, newDirection);
                updateAnimal(animal);
            }
            else{
                super.moveTo(animal);
            }

        }
        else{
            super.moveTo(animal);
        }
    }
    public void addFeromon(Vector2d position){
        if (feromons.containsKey(position)){
            feromons.get(position).increaseFeromonValue();
        }
        else{
            this.feromons.put(position,new Feromon(position));
        }
    }

    public void decreseFeromons(){
        for (Feromon feromon : feromons.values()){
            if (feromon.getFeromonDay()%daysToDecrese == 0){
                feromon.decreseFeromonValue();
                if (feromon.getFeromonValue() == 0){
                    feromons.remove(feromon);
                }
            }
            feromon.increaseFeromonDay();

        }
    }
}
