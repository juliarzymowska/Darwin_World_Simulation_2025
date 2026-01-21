package agh.ics.oop.model.stats;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.map.MapElementsManager;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.observators.StatsChangeListener;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationStatsTracker implements MapChangeListener {
    WorldMap map;
    MapElementsManager elementsManager;
    private int deadAnimalCount = 0;
    private int totalLifeTime = 0;
    private List<StatsChangeListener> observers = new ArrayList<>();


    public SimulationStatsTracker(WorldMap map) {
        this.map = map;
        this.elementsManager = map.getElementsManager();
    }

    public void addObserver(StatsChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(StatsChangeListener observer) {
        observers.remove(observer);
    }

    private void statsChanged(StatsRecord statsRecord) {
        for (StatsChangeListener observer : observers) {
            observer.statsChanged(statsRecord);
        }
    }

    public void handleDeadAnimals(List<Animal> deadAnimals) {
        for (Animal animal : deadAnimals) {
            deadAnimalCount++;
            totalLifeTime += animal.getAge();
        }
    }

    public StatsRecord updateStats(int day) {
        StatsRecord record = new StatsRecord(
                day,
                getAnimalCount(),
                getPlantCount(),
                getFreeTilesCount(),
                getMostPopularGenotypes(),
                getAverageEnergy(),
                getAverageLifeTime(),
                getAverageKids());

        statsChanged(record);
        return record;
    }

    /// na potrzeby testów w konsoli
    public void printStats(int day) {
        String separator = "=".repeat(60);
        StatsRecord record = updateStats(day);
        String header = String.format("| %-15s | %-10s | %-10s | %-10s |",
                "STATYSTYKA", "WARTOŚĆ", "DZIEŃ " + record.day(), "");

        System.out.println("\n" + separator);
        System.out.println(header);
        System.out.println(separator);

        printRow("Liczba zwierząt", String.valueOf(record.animalCount()));
        printRow("Liczba roślin", String.valueOf(record.plantCount()));
        printRow("Wolne pola", String.valueOf(record.freeTilesCount()));
        printRow("Średnia energia", String.format("%.2f", record.averageEnergyLevel()));
        printRow("Średni wiek", String.format("%.2f", record.averageLifeTime()));
        printRow("Średnia dzieci", String.format("%.2f", record.averageKids()));

        System.out.println(separator);
        System.out.println("Top 3 Genotypy:");
        if (record.mostPopularGenotypes().isEmpty()) {
            System.out.println("  brak danych");
        } else {
            record.mostPopularGenotypes().forEach(g -> System.out.println("  - " + g));
        }
        System.out.println(separator + "\n");
    }

    private void printRow(String label, String value) {
        System.out.printf("| %-25s | %-29s |\n", label, value);
    }

    private int getAnimalCount() {
        return elementsManager.getAnimals().size();
    }

    private int getPlantCount() {
        return elementsManager.getPlants().size();
    }

    private int getFreeTilesCount() {
        Boundary boundary = map.getCurrentBounds();
        int totalTiles = (boundary.rightUpMapCorner().getX() + 1) * (boundary.rightUpMapCorner().getY() + 1);
        List<Vector2d> animalPos = elementsManager.getAnimals().stream()
                .map(Animal::getCurrentPosition).toList();
        List<Vector2d> plantPos = elementsManager.getPlants().stream()
                .map(Plant::getCurrentPosition).toList();
        int occupiedTiles = Stream.concat(animalPos.stream(), plantPos.stream())
                .collect(Collectors.toSet()).size();

        return totalTiles - occupiedTiles;
    }

    private List<Genotype> getMostPopularGenotypes() {
        return elementsManager.getAnimals().stream()
                .map(Animal::getGenotype)
                .collect(Collectors.groupingBy(g -> g, Collectors.counting())).entrySet().stream()
                .sorted(Map.Entry.<Genotype, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }

    private double getAverageEnergy() {
        List<Animal> animals = elementsManager.getAnimals();
        if (animals.isEmpty()) return 0;
        double energy = 0.0;
        for (Animal animal : animals) {
            energy += animal.getEnergy();
        }
        return energy / animals.size();
    }

    private double getAverageLifeTime() {
        if (deadAnimalCount == 0) return 0.0;
        else return (double) totalLifeTime / deadAnimalCount;
    }

    private double getAverageKids() {
        List<Animal> animals = elementsManager.getAnimals();
        if (animals.isEmpty()) return 0;
        double kids = 0.0;
        for (Animal animal : animals) {
            kids += animal.getNumberOfChildren();
        }
        return kids / animals.size();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {

    }
}
