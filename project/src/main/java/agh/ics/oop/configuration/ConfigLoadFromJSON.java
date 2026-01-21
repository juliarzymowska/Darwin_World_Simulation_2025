package agh.ics.oop.configuration;

import agh.ics.oop.model.exception.ConfigurationException;
import agh.ics.oop.model.exception.EarthClassException;
import agh.ics.oop.model.map.MapType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigLoadFromJSON {
    private final String filePath;
    private final ObjectMapper objectMapper;

    public ConfigLoadFromJSON(String filePath) {
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();
    }

    public ConfigBuilder loadConfig() throws IOException, ConfigurationException {
        File file = new File(filePath);
        JsonNode jsonNode = objectMapper.readTree(file);

        ConfigBuilder builder = new ConfigBuilder();

        // Load Animal config
        builder.setInitialAnimalCount(jsonNode.get("initialAnimalCount").asInt());
        builder.setMaxEnergy(jsonNode.get("maxEnergy").asInt());
        builder.setInitialEnergy(jsonNode.get("initialEnergy").asInt());
        builder.setEnergyToReproduce(jsonNode.get("energyToReproduce").asInt());
        builder.setEnergyConsumedByMove(jsonNode.get("energyConsumedByMove").asInt());
        builder.setEnergyGainedByEating(jsonNode.get("energyGainedByEating").asInt());
        builder.setGenotypeLength(jsonNode.get("genotypeLength").asInt());
        builder.setMaxMutations(jsonNode.get("maxMutations").asInt());
        builder.setMinMutations(jsonNode.get("minMutations").asInt());


        // Load Map config
        builder.setWidth(jsonNode.get("width").asInt());
        builder.setHeight(jsonNode.get("height").asInt());
        builder.setStartPlantNumber(jsonNode.get("startPlantNumber").asInt());
        builder.setDailyPlantNumber(jsonNode.get("dailyPlantNumber").asInt());

        MapType mapType = MapType.valueOf(jsonNode.get("mapType").asText());
        builder.setMapType(mapType);

        double moveToFeromonProbability = jsonNode.get("moveToFeromonProbability").asDouble();
        int daysToDecreaseFeromon = jsonNode.get("daysToDecreaseFeromon").asInt();
        int smellRange = jsonNode.get("smellRange").asInt();
        // Walidacja dla mapy EARTH
        if (mapType == MapType.EARTH_MAP) {
            if (moveToFeromonProbability != 0.0 || daysToDecreaseFeromon != 0 || smellRange != 0) {
                throw new EarthClassException();
            }
        }
        builder.setMoveToFeromonProbability(jsonNode.get("moveToFeromonProbability").asDouble());
        builder.setDaysToDecreaseFeromon(jsonNode.get("daysToDecreaseFeromon").asInt());
        builder.setSmellRange(jsonNode.get("smellRange").asInt());

        return builder;
    }
}