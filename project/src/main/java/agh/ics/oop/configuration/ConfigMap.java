package agh.ics.oop.configuration;

import agh.ics.oop.model.map.MapType;

public record ConfigMap (
        int width,
        int height,
        int startPlantNumber,
        int dailyPlantNumber,
        MapType mapType,
        double moveToFeromonProbability, //wprowadzać jako int w procentach, czy double?
        int daysToDecreaseFeromon,
        int smellRange
) {
    public ConfigMap() {
        this(3, 3, 0,0, MapType.FEROMON_MAP, 0.1, 2, 2);
    }

    public ConfigMap(int width, int height, int startPlantNumber, int dailyPlantNumber, MapType mapType, double moveToFeromonProbability, int daysToDecreaseFeromon, int smellRange) {
        this.width = width;
        this.height = height;
        this.startPlantNumber = startPlantNumber;
        this.dailyPlantNumber = dailyPlantNumber;
        this.mapType = mapType;
        this.moveToFeromonProbability = moveToFeromonProbability;
        this.daysToDecreaseFeromon = daysToDecreaseFeromon;
        this.smellRange = smellRange;
    }
}

