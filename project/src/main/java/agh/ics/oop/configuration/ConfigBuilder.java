package agh.ics.oop.configuration;

import agh.ics.oop.model.map.MapType;

/*
 * Class ConfigBuilder is responsible for building configuration objects for Animal and Map.
 * It contains fields for various configuration parameters and provides methods to set these parameters.
 * */
public class ConfigBuilder {
    // Animal
    private int initialAnimalCount;
    private int initialEnergy;
    private int maxEnergy;
    private int energyToReproduce;
    private int energyConsumedByMove;
    private int energyGainedByEating;
    private int minMutations;
    private int maxMutations;
    private int genotypeLength;
    // Map
    private int width;
    private int height;
    private int startPlantNumber;
    private int dailyPlantNumber;
    private MapType mapType;
    private double moveToFeromonProbability;
    private int daysToDecreaseFeromon;
    private int smellRange;

    public ConfigBuilder builder() {
        return new ConfigBuilder();
    }

    public ConfigAnimal buildAnimalConfig() {
        return new ConfigAnimal(
                initialAnimalCount,
                initialEnergy,
                maxEnergy,
                energyToReproduce,
                energyConsumedByMove,
                energyGainedByEating,
                minMutations,
                maxMutations,
                genotypeLength
        );
    }

    public ConfigMap buildMapConfig() {
        return new ConfigMap(
                width,
                height,
                startPlantNumber,
                dailyPlantNumber,
                mapType,
                moveToFeromonProbability,
                daysToDecreaseFeromon,
                smellRange
        );
    }

    public static ConfigBuilder fromDefaults(ConfigAnimal animal, ConfigMap map) {
        ConfigBuilder builder = new ConfigBuilder();

        // Animal config
        builder.setInitialAnimalCount(animal.initialAnimalCount());
        builder.setInitialEnergy(animal.initialEnergy());
        builder.setMaxEnergy(animal.maxEnergy());
        builder.setEnergyToReproduce(animal.energyToReproduce());
        builder.setEnergyConsumedByMove(animal.energyConsumedByMove());
        builder.setEnergyGainedByEating(animal.energyGainedByEating());
        builder.setMinMutations(animal.minMutations());
        builder.setMaxMutations(animal.maxMutations());
        builder.setGenotypeLength(animal.genotypeLength());

        // Map config
        builder.setWidth(map.width());
        builder.setHeight(map.height());
        builder.setStartPlantNumber(map.startPlantNumber());
        builder.setDailyPlantNumber(map.dailyPlantNumber());
        builder.setMapType(map.mapType());
        builder.setMoveToFeromonProbability(map.moveToFeromonProbability());
        builder.setDaysToDecreaseFeromon(map.daysToDecreaseFeromon());
        builder.setSmellRange(map.smellRange());

        return builder;
    }

    // Setters for Animal config
    public void setInitialAnimalCount(int initialAnimalCount) {
        this.initialAnimalCount = initialAnimalCount;
    }

    public void setInitialEnergy(int initialEnergy) {
        this.initialEnergy = initialEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public void setEnergyToReproduce(int energyToReproduce) {
        this.energyToReproduce = energyToReproduce;
    }

    public void setEnergyConsumedByMove(int energyConsumedByMove) {
        this.energyConsumedByMove = energyConsumedByMove;
    }

    public void setEnergyGainedByEating(int energyGainedByEating) {
        this.energyGainedByEating = energyGainedByEating;
    }

    public void setMinMutations(int minMutations) {
        this.minMutations = minMutations;
    }

    public void setMaxMutations(int maxMutations) {
        this.maxMutations = maxMutations;
    }

    public void setGenotypeLength(int genotypeLength) {
        this.genotypeLength = genotypeLength;
    }

    // Setters for Map config
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setStartPlantNumber(int startPlantNumber) {
        this.startPlantNumber = startPlantNumber;
    }

    public void setDailyPlantNumber(int dailyPlantNumber) {
        this.dailyPlantNumber = dailyPlantNumber;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public void setMoveToFeromonProbability(double moveToFeromonProbability) {
        this.moveToFeromonProbability = moveToFeromonProbability;
    }

    public void setDaysToDecreaseFeromon(int daysToDecreaseFeromon) {
        this.daysToDecreaseFeromon = daysToDecreaseFeromon;
    }

    public void setSmellRange(int smellRange) {
        this.smellRange = smellRange;
    }

    public int getInitialAnimalCount() {
        return initialAnimalCount;
    }

    public int getInitialEnergy() {
        return initialEnergy;
    }
}
