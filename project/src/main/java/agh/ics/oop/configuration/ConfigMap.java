package agh.ics.oop.configuration;

import agh.ics.oop.model.map.MapType;

/*
 * Class for configuring map parameters, including width, height, plant numbers, map type, and pheromone-related settings.
 */
public record ConfigMap(
        int width,
        int height,
        int startPlantNumber,
        int dailyPlantNumber,
        MapType mapType,
        double moveToFeromonProbability,
        int daysToDecreaseFeromon,
        int smellRange
) {
    public ConfigMap() {
        this(5, 5, 5, 5, MapType.FEROMON_MAP, 0.1, 2, 2);
    }

}

