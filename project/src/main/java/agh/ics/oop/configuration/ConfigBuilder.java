package agh.ics.oop.configuration;

import agh.ics.oop.model.exception.*;
import agh.ics.oop.model.map.MapType;

/*
 * Class ConfigBuilder is responsible for building configuration objects for Animal and Map.
 * It contains fields for various configuration parameters and provides methods to set these parameters.
 * */
public class ConfigBuilder {
    private int moveDelay = 300; // Default 300ms
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

    private boolean saveToCSV = false;

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

    public static ConfigBuilder fromDefaults(ConfigAnimal animal, ConfigMap map) throws ConfigurationException {
        ConfigBuilder builder = new ConfigBuilder();

        // Animal config
        builder.setMaxEnergy(animal.maxEnergy());
        builder.setInitialAnimalCount(animal.initialAnimalCount());
        builder.setInitialEnergy(animal.initialEnergy());
        builder.setEnergyToReproduce(animal.energyToReproduce());
        builder.setEnergyConsumedByMove(animal.energyConsumedByMove());
        builder.setEnergyGainedByEating(animal.energyGainedByEating());
        builder.setGenotypeLength(animal.genotypeLength());
        builder.setMaxMutations(animal.maxMutations());
        builder.setMinMutations(animal.minMutations());

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
    public void setInitialAnimalCount(int initialAnimalCount) throws IllegalAnimalCountException {
        if (initialAnimalCount <= 0 || initialAnimalCount > 200) {
            throw new IllegalAnimalCountException(initialAnimalCount, 200);
        }
        this.initialAnimalCount = initialAnimalCount;
    }

    public void setInitialEnergy(int initialEnergy) throws IllegalInitialEnergyException {
        if (initialEnergy <= 0 || initialEnergy > maxEnergy) {
            throw new IllegalInitialEnergyException(maxEnergy);
        }
        this.initialEnergy = initialEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public void setEnergyToReproduce(int energyToReproduce) throws IllegalEnergyToReproduceException {
        if (energyToReproduce <= 0 || energyToReproduce > maxEnergy) {
            throw new IllegalEnergyToReproduceException(maxEnergy);
        }
        this.energyToReproduce = energyToReproduce;
    }

    public void setEnergyConsumedByMove(int energyConsumedByMove) throws IllegalEnergyConsumedByMoveException {
        if (energyConsumedByMove <= 0 || energyConsumedByMove > maxEnergy) {
            throw new IllegalEnergyConsumedByMoveException(maxEnergy);
        }
        this.energyConsumedByMove = energyConsumedByMove;
    }

    public void setEnergyGainedByEating(int energyGainedByEating) throws IllegalEnergyGainedByEatingException {
        if (energyGainedByEating <= 0 || energyGainedByEating > maxEnergy) {
            throw new IllegalEnergyGainedByEatingException(maxEnergy);
        }
        this.energyGainedByEating = energyGainedByEating;
    }

    public void setMinMutations(int minMutations) throws IllegalMinMutationsException {
        if (minMutations < 0 || minMutations > maxMutations) {
            throw new IllegalMinMutationsException(maxMutations);
        }
        this.minMutations = minMutations;
    }

    public void setMaxMutations(int maxMutations) throws IllegalMaxMutationsException {
        if (maxMutations < 0 || maxMutations > genotypeLength || maxMutations < minMutations) {
            throw new IllegalMaxMutationsException(genotypeLength);
        }
        this.maxMutations = maxMutations;
    }

    public void setGenotypeLength(int genotypeLength) throws IllegalGenotypeLengthException {
        if (genotypeLength <= 0) {
            throw new IllegalGenotypeLengthException();
        }
        this.genotypeLength = genotypeLength;
    }

    // Setters for Map config
    public void setWidth(int width) throws IllegalMapWidthException {
        if (width <= 0 || width > 200) {
            throw new IllegalMapWidthException(200);
        }
        this.width = width;
    }

    public void setHeight(int height) throws IllegalMapHeightException {
        if (height <= 0 || height > 200) {
            throw new IllegalMapHeightException(200);
        }
        this.height = height;
    }

    public void setStartPlantNumber(int startPlantNumber) throws IllegalNumberOfPlantsException {
        if (startPlantNumber < 0 || startPlantNumber > width * height) {
            throw new IllegalNumberOfPlantsException(width, height);
        }
        this.startPlantNumber = startPlantNumber;
    }

    public void setDailyPlantNumber(int dailyPlantNumber) throws IllegalNumberOfPlantsException {
        if (dailyPlantNumber < 0 || dailyPlantNumber > width * height) {
            throw new IllegalNumberOfPlantsException(width, height);
        }
        this.dailyPlantNumber = dailyPlantNumber;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public void setMoveToFeromonProbability(double moveToFeromonProbability) throws IllegalMoveToFeromonProbabilityException {
        if (moveToFeromonProbability < 0 || moveToFeromonProbability > 1) {
            throw new IllegalMoveToFeromonProbabilityException();
        }
        this.moveToFeromonProbability = moveToFeromonProbability;
    }

    public void setDaysToDecreaseFeromon(int daysToDecreaseFeromon) throws IllegalDaysToDecreaseFeromonException {
        if (daysToDecreaseFeromon < 0) {
            throw new IllegalDaysToDecreaseFeromonException();
        }
        this.daysToDecreaseFeromon = daysToDecreaseFeromon;
    }

    public void setSmellRange(int smellRange) throws IllegalSmellRangeException {
        if (smellRange < 0) {
            throw new IllegalSmellRangeException();
        }
        this.smellRange = smellRange;
    }

    public void setSaveToCSV(boolean saveToCSV) {
        this.saveToCSV = saveToCSV;
    }

    public int getInitialAnimalCount() {
        return initialAnimalCount;
    }

    public int getInitialEnergy() {
        return initialEnergy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getEnergyToReproduce() {
        return energyToReproduce;
    }

    public int getEnergyConsumedByMove() {
        return energyConsumedByMove;
    }

    public int getEnergyGainedByEating() {
        return energyGainedByEating;
    }

    public int getMinMutations() {
        return minMutations;
    }

    public int getMaxMutations() {
        return maxMutations;
    }

    public int getGenotypeLength() {
        return genotypeLength;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartPlantNumber() {
        return startPlantNumber;
    }

    public int getDailyPlantNumber() {
        return dailyPlantNumber;
    }

    public MapType getMapType() {
        return mapType;
    }

    public double getMoveToFeromonProbability() {
        return moveToFeromonProbability;
    }

    public int getDaysToDecreaseFeromon() {
        return daysToDecreaseFeromon;
    }

    public int getSmellRange() {
        return smellRange;
    }

    public int getMoveDelay() {
        return moveDelay;
    }

    public void setMoveDelay(int moveDelay) {
        this.moveDelay = moveDelay;
    }

    public boolean isSaveToCSV() {
        return saveToCSV;
    }
}
