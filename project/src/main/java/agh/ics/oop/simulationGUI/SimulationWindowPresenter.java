package agh.ics.oop.simulationGUI;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.observators.StatsChangeListener;
import agh.ics.oop.model.stats.StatsRecord;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.simulation.Simulation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class SimulationWindowPresenter implements MapChangeListener, StatsChangeListener {

    @FXML
    private GridPane mapGrid;

    @FXML
    private Label dayLabel;
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label plantCountLabel;
    @FXML
    private Label emptyFieldsLabel;
    @FXML
    private Label avgEnergyLabel;
    @FXML
    private Label avgLifeSpanLabel;

    private WorldMap worldMap;
    private Simulation simulation;
    private int width;
    private int height;
    private static final int CELL_SIZE = 20; // Size of one cell in pixels

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.worldMap = simulation.getMap();
        this.worldMap.addObserver(this); // Subscribe to updates
        this.simulation.getStats().addObserver(this); // Subscribe to stats updates

        // Initialize grid dimensions based on map size
        this.width = worldMap.getCurrentBounds().rightUpMapCorner().getX() + 1;
        this.height = worldMap.getCurrentBounds().rightUpMapCorner().getY() + 1;

        setupGrid();
    }

    private void setupGrid() {
        mapGrid.getChildren().clear();
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();

        // Create columns and rows
        for (int i = 0; i < width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
        }
        for (int i = 0; i < height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        }

        drawMap(); // Initial draw
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        // Run UI updates on the JavaFX Application Thread
        Platform.runLater(this::drawMap);
    }

    private void drawMap() {
        mapGrid.getChildren().clear(); // Clear old state

        // This is a naive implementation (clearing all).
        // For very large maps, you might want to update only changed cells.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Vector2d pos = new Vector2d(x, y);
                StackPane cell = new StackPane();

                // 1. Background (Dirt/Grass)
                Rectangle bg = new Rectangle(CELL_SIZE, CELL_SIZE);
                bg.setFill(Color.LIGHTGREEN); // Default grass
                cell.getChildren().add(bg);

                // 2. Check for objects
                WorldElement object = ((agh.ics.oop.model.map.EarthMap) worldMap).objectAt(pos);

                if (object instanceof Plant) {
                    Circle plant = new Circle(CELL_SIZE / 3.0, Color.DARKGREEN);
                    cell.getChildren().add(plant);
                } else if (object instanceof Animal animal) {
                    // Visualize energy with color saturation or different colors
                    Color animalColor = getEnergyColor(animal);
                    Circle animalShape = new Circle(CELL_SIZE / 2.5, animalColor);
                    cell.getChildren().add(animalShape);
                }

                // Invert Y axis for display (0,0 is usually bottom-left in math, top-left in JavaFX)
                // If your map logic treats (0,0) as bottom-left, use: height - 1 - y
                mapGrid.add(cell, x, height - 1 - y);
            }
        }
    }

    private Color getEnergyColor(Animal animal) {
        // Example: Red = Low Energy, Blue = High Energy
        int energy = animal.getEnergy();
        if (energy < 10) return Color.RED;
        if (energy < 30) return Color.ORANGE;
        if (energy < 50) return Color.YELLOW;
        return Color.BLUE;
    }

    @Override
    public void statsChanged(StatsRecord stats) {
        // UI updates must happen on JavaFX Application Thread
        Platform.runLater(() -> updateStats(stats));
    }

    // 4. Update the labels using the Record data
    private void updateStats(StatsRecord stats) {
        dayLabel.setText(String.valueOf(stats.day()));
        animalCountLabel.setText(String.valueOf(stats.animalCount()));
        plantCountLabel.setText(String.valueOf(stats.plantCount()));
        emptyFieldsLabel.setText(String.valueOf(stats.freeTilesCount()));

        // formatting to 2 decimal places for averages
        avgEnergyLabel.setText(String.format("%.2f", stats.averageEnergyLevel()));
        avgLifeSpanLabel.setText(String.format("%.2f", stats.averageLifeTime()));
    }

    @FXML
    private void onStartClicked() {
        simulation.resume();
    }

    @FXML
    private void onPauseClicked() {
        simulation.pause();
    }

    // Cleanup when window closes
    public void onWindowClose() {
        simulation.shutDown();
    }

    @Override
    public void handleDeadAnimals(List<Animal> animals) {
        // handled in stats usually
    }
}