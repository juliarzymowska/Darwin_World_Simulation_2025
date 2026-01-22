package agh.ics.oop.model.stats;

import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.observators.StatsChangeListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/*
 * Class for saving simulation statistics to a CSV file.
 * The file is named "simulation_<mapId>.csv" where <mapId> is the unique identifier of the map.
 * Each time the statistics change, a new line is appended to the CSV file with the following format:
 * day;animals;plants;freeFields;avgEnergy;avgLifespan;avgChildren;mostPopularGenotype
 * The class ensures that the header is written only once at the beginning of the file.
 * */
public class CSVSaver implements StatsChangeListener {
    private final Path csvFilePath;
    private static final String HEADER = "day;animals;plants;freeFields;avgEnergy;avgLifespan;avgChildren;mostPopularGenotype";
    private int lastDay = -1;
    private WorldMap map;

    public CSVSaver(WorldMap map) {
        this.csvFilePath = Paths.get("simulation_" + map.getId() + ".csv");
        this.map = map;
        initializeFile();
    }

    private void initializeFile() {
        try {
            Files.write(csvFilePath, (HEADER + System.lineSeparator()).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatGenotype(Genotype genotype) {
        if (genotype == null) return "\"none\"";
        return "\"" + genotype.toString().replaceAll("\\s+", "") + "\"";
    }

    private String formatDecimal(double value) {
        return String.format("%.2f", value);
    }

    public void statsChanged(StatsRecord stats) {
        try {
            if (stats.day() == lastDay) {
                return;
            }

            lastDay = stats.day();

            String mostPopularGenotype = stats.mostPopularGenotypes().isEmpty() ?
                    "\"none\"" :
                    formatGenotype(stats.mostPopularGenotypes().getFirst());

            String csvLine = String.format("%d;%d;%d;%d;%s;%s;%s;%s",
                    stats.day(),
                    stats.animalCount(),
                    stats.plantCount(),
                    stats.freeTilesCount(),
                    formatDecimal(stats.averageEnergyLevel()),
                    formatDecimal(stats.averageLifeTime()),
                    formatDecimal(stats.averageKids()),
                    mostPopularGenotype
            );

            Files.write(
                    csvFilePath,
                    (csvLine + System.lineSeparator()).getBytes(),
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
