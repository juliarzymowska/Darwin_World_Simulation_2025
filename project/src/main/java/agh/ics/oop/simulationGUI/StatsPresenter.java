package agh.ics.oop.simulationGUI;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.stats.StatsRecord;
import javafx.scene.chart.LineChart;

import java.util.HashSet;
import java.util.Set;

public class StatsPresenter {
    // Reference to the main controller to access FXML fields
    private final SimulationWindowPresenter view;
    private final ChartManager chartManager;
    private final Set<agh.ics.oop.model.elements.Genotype> topGenotypes = new HashSet<>();

    public StatsPresenter(SimulationWindowPresenter view, LineChart<Number, Number> chart) {
        this.view = view;
        this.chartManager = new ChartManager(chart);
    }

    public void updateStats(StatsRecord stats) {
        view.dayLabel.setText("Day: " + stats.day());
        view.animalCountLabel.setText("Animals: " + stats.animalCount());
        view.plantCountLabel.setText("Plants: " + stats.plantCount());
        view.emptyFieldsLabel.setText("Empty Fields: " + stats.freeTilesCount());
        view.avgEnergyLabel.setText(String.format("Avg Energy: %.2f", stats.averageEnergyLevel()));
        view.avgLifeSpanLabel.setText(String.format("Avg LifeSpan: %.2f", stats.averageLifeTime()));
        view.childrenCountLabel.setText(String.format("Avg Children: %.2f", stats.averageKids()));

        updateGenotypes(stats);
        updateTrackedAnimal();
        chartManager.updateChart(stats);
    }

    private void updateGenotypes(StatsRecord stats) {
        topGenotypes.clear();
        if (stats.mostPopularGenotypes() != null) {
            topGenotypes.addAll(stats.mostPopularGenotypes());
            StringBuilder sb = new StringBuilder("Top Genotypes:\n");
            for (int i = 0; i < stats.mostPopularGenotypes().size(); i++) {
                sb.append(i + 1).append(". ").append(stats.mostPopularGenotypes().get(i)).append("\n");
            }
            view.mostCommonGenotypeLabel.setText(sb.toString());
        }
    }

    public void updateTrackedAnimal() {
        Animal tracked = view.getTrackedAnimal(); // We will add this getter to Presenter
        if (tracked != null) {
            updateTrackedLabels(tracked);
        }
    }

    public void clearTrackedStats() {
        view.trackedGenotypeLabel.setText("Genotype: -");
        view.trackedActiveGeneLabel.setText("Active Gene: -");
        view.trackedEnergyLabel.setText("Energy: -");
        view.trackedEatenLabel.setText("Eaten: -");
        view.trackedChildrenLabel.setText("Children: -");
        view.trackedDescendantsLabel.setText("Descendants: -");
        view.trackedAgeLabel.setText("Age: -");
        view.trackedDeathDayLabel.setText("Death Day: -");
        view.trackedAnimalStatusLabel.setText("Status: not tracking");
        view.trackedAnimalStatusLabel.setStyle("-fx-text-fill: black;");
    }

    private void updateTrackedLabels(Animal animal) {
        if (animal.getDayOfDeath() != -1) {
            view.trackedAnimalStatusLabel.setText("Status: DEAD (ID: " + animal.hashCode() + ")");
            view.trackedAnimalStatusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            view.trackedDeathDayLabel.setText("Death Day: " + animal.getDayOfDeath());
        } else {
            view.trackedAnimalStatusLabel.setText("Status: ALIVE (ID: " + animal.hashCode() + ")");
            view.trackedAnimalStatusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            view.trackedDeathDayLabel.setText("Death Day: -");
        }

        view.trackedGenotypeLabel.setText("Genotype: " + animal.getGenotype());
        view.trackedActiveGeneLabel.setText("Active gen: " + animal.getGenotype().getActiveGeneIndex());
        view.trackedEnergyLabel.setText("Energy: " + animal.getEnergy());
        view.trackedEatenLabel.setText("Eaten plants: " + animal.getNumberOfEatenPlants());
        view.trackedChildrenLabel.setText("Children: " + animal.getNumberOfChildren());
        view.trackedAgeLabel.setText("Age: " + animal.getAge());
        view.trackedDescendantsLabel.setText("Descendants: " + animal.getNumberOfDescendants());
    }

    public Set<agh.ics.oop.model.elements.Genotype> getTopGenotypes() {
        return topGenotypes;
    }

    public void setChartStat(String statName) {
        chartManager.setObservedStat(statName);
    }
}