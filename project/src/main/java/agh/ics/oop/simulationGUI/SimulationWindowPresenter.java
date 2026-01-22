package agh.ics.oop.simulationGUI;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.map.EarthMap;
import agh.ics.oop.model.map.FeromonMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.observators.StatsChangeListener;
import agh.ics.oop.model.stats.StatsRecord;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.simulation.Simulation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ChoiceBox;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimulationWindowPresenter implements MapChangeListener, StatsChangeListener {
    @FXML
    private Canvas mapCanvas;
    @FXML
    private StackPane mapContainer;

    @FXML
    private ToggleButton preferredFieldsToggle;
    @FXML
    private ToggleButton dominantGenotypeToggle;

    @FXML
    private Button stopTrackingButton;

    @FXML
    private Label dayLabel, animalCountLabel, plantCountLabel, emptyFieldsLabel, avgEnergyLabel, avgLifeSpanLabel, childrenCountLabel, mostCommonGenotypeLabel;
    @FXML
    private Label trackedAnimalStatusLabel, trackedGenotypeLabel, trackedActiveGeneLabel, trackedEnergyLabel, trackedEatenLabel, trackedChildrenLabel, trackedDescendantsLabel, trackedAgeLabel, trackedDeathDayLabel;

    @FXML
    private ChoiceBox<String> statChoiceBox;

    @FXML
    private LineChart<Number, Number> statsChart;
    private ChartManager chartManager;
    private double cellSize;    // Calculated dynamically
    private double offsetX;     // To center the map horizontally
    private double offsetY;     // To center the map vertically

    private WorldMap worldMap;
    private Simulation simulation;
    private Animal trackedAnimal = null;
    private Set<Genotype> topGenotypes = new HashSet<>();
    private boolean isPaused = true; // Start paused usually

    private double mapWidth;
    private double mapHeight;

    private final Color[] pheromoneColors = new Color[20];

    private boolean showPreferredFields = false;
    private boolean showDominantGenotype = false;

    @FXML
    public void initialize() {
        mapCanvas.setManaged(false);
        mapContainer.widthProperty().addListener((obs, oldVal, newVal) -> drawMap());
        mapContainer.heightProperty().addListener((obs, oldVal, newVal) -> drawMap());

        mapCanvas.widthProperty().bind(mapContainer.widthProperty());
        mapCanvas.heightProperty().bind(mapContainer.heightProperty());

        // CLICK EVENT
        mapCanvas.setOnMouseClicked(this::handleMapClick);

        for (int i = 0; i < 20; i++) {
            double opacity = Math.min(i * 0.15, 0.6);
            pheromoneColors[i] = Color.rgb(0, 0, 255, opacity);
        }

        stopTrackingButton.setDisable(true);

        statChoiceBox.getItems().addAll(
                "Animal Count",
                "Plant Count",
                "Free Tiles",
                "Avg Energy",
                "Avg Lifespan",
                "Avg Children"
        );
        statChoiceBox.setValue("Animal Count");

        statChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && chartManager != null) {
                chartManager.setObservedStat(newVal);
            }
        });
    }

    private void handleMapClick(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double mapX = mouseX - offsetX;
        double mapY = mouseY - offsetY;

        if (mapX < 0 || mapX >= mapWidth * cellSize || mapY < 0 || mapY >= mapHeight * cellSize) {
            return;
        }

        int col = (int) (mapX / cellSize);

        int row = (int) (mapHeight - (mapY / cellSize));

        if (row >= mapHeight) row = (int) mapHeight - 1;
        if (row < 0) row = 0;

        Vector2d position = new Vector2d(col, row);

        if (worldMap instanceof EarthMap map) {
            Animal clickedAnimal = map.getAnimalAt(position);

            if (clickedAnimal != null) {
                this.trackedAnimal = clickedAnimal;
                stopTrackingButton.setDisable(false);
                updateTrackedAnimalStats();
                drawMap();
            }
        }
    }

    @FXML
    private void onStopTrackingClicked() {
        this.trackedAnimal = null;

        stopTrackingButton.setDisable(true);

        trackedAnimalStatusLabel.setText("Status: not tracking");
        trackedAnimalStatusLabel.setStyle("-fx-text-fill: black;");

        clearTrackedStats();

        drawMap();
    }

    private void clearTrackedStats() {
        trackedGenotypeLabel.setText("Genotype: -");
        trackedActiveGeneLabel.setText("Active Gene: -");
        trackedEnergyLabel.setText("Energy: -");
        trackedEatenLabel.setText("Eaten: -");
        trackedChildrenLabel.setText("Children: -");
        trackedDescendantsLabel.setText("Descendants: -");
        trackedAgeLabel.setText("Age: -");
        trackedDeathDayLabel.setText("Death Day: -");
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.worldMap = simulation.getMap();
        this.mapWidth = worldMap.getCurrentBounds().rightUpMapCorner().getX() + 1;
        this.mapHeight = worldMap.getCurrentBounds().rightUpMapCorner().getY() + 1;
        this.chartManager = new ChartManager(statsChart);


        worldMap.addObserver(this);
        simulation.getStats().addObserver(this);
        drawMap();
    }

    // --- DRAWING LOGIC ---

    private void drawMap() {
        if (worldMap == null) return;

        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        double canvasW = mapCanvas.getWidth();
        double canvasH = mapCanvas.getHeight();

        gc.clearRect(0, 0, canvasW, canvasH);

        double sizeX = canvasW / mapWidth;
        double sizeY = canvasH / mapHeight;
        this.cellSize = Math.min(sizeX, sizeY);

        this.offsetX = (canvasW - mapWidth * cellSize) / 2;
        this.offsetY = (canvasH - mapHeight * cellSize) / 2;

        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(offsetX, offsetY, mapWidth * cellSize, mapHeight * cellSize);

        if (showPreferredFields) drawPreferredFields(gc);
        drawPheromones(gc);
        drawGrid(gc);
        drawObjects(gc);
    }

    private void drawPreferredFields(GraphicsContext gc) {
        EarthMap map = (EarthMap) worldMap;
        gc.setFill(Color.rgb(34, 139, 34, 0.5)); // Jungle Color

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (map.isPreferredPosition(new Vector2d(x, y))) {
                    // CALCULATE POSITION
                    double drawX = offsetX + x * cellSize;
                    double drawY = offsetY + (mapHeight - 1 - y) * cellSize;

                    gc.fillRect(drawX, drawY, cellSize, cellSize);
                }
            }
        }
    }

    private void drawObjects(GraphicsContext gc) {
        if (!(worldMap instanceof EarthMap map)) return;

        for (Plant plant : map.getElementsManager().getPlants()) {
            if (plant == null || plant.getCurrentPosition() == null) continue;

            Vector2d pos = plant.getCurrentPosition();
            if (pos.getX() < mapWidth && pos.getY() < mapHeight) {
                double drawX = offsetX + (pos.getX() * cellSize);
                double drawY = offsetY + ((mapHeight - 1 - pos.getY()) * cellSize);
                gc.setFill(Color.DARKGREEN);
                gc.fillOval(drawX + cellSize * 0.2, drawY + cellSize * 0.2, cellSize * 0.6, cellSize * 0.6);
            }
        }

        for (Animal animal : map.getElementsManager().getAnimals()) {
            if (animal == null || animal.getCurrentPosition() == null) continue; // SKIP NULLS

            Vector2d pos = animal.getCurrentPosition();
            if (pos.getX() < mapWidth && pos.getY() < mapHeight) {
                double drawX = offsetX + (pos.getX() * cellSize);
                double drawY = offsetY + ((mapHeight - 1 - pos.getY()) * cellSize);

                gc.setFill(getEnergyColor(animal));
                gc.fillOval(drawX, drawY, cellSize, cellSize);

                // Highlights
                if (animal.equals(trackedAnimal)) {
                    gc.setStroke(Color.GOLD);
                    gc.setLineWidth(Math.max(1.5, cellSize * 0.1));
                    gc.strokeOval(drawX, drawY, cellSize, cellSize);
                }

                if (showDominantGenotype && topGenotypes.contains(animal.getGenotype())) {
                    gc.setStroke(Color.MAGENTA);
                    gc.setLineWidth(Math.max(1.0, cellSize * 0.08));
                    gc.strokeOval(drawX, drawY, cellSize, cellSize);
                }
            }
        }
    }

    private void drawPheromones(GraphicsContext gc) {
        if (worldMap instanceof FeromonMap feromonMap) {

            Map<Vector2d, Integer> activeSmells = feromonMap.getActiveFeromons();

            for (Map.Entry<Vector2d, Integer> entry : activeSmells.entrySet()) {
                Vector2d pos = entry.getKey();
                int intensity = entry.getValue();

                if (pos.getX() >= 0 && pos.getX() < mapWidth &&
                        pos.getY() >= 0 && pos.getY() < mapHeight) {

                    double drawX = offsetX + (pos.getX() * cellSize);
                    double drawY = offsetY + ((mapHeight - 1 - pos.getY()) * cellSize);

                    int index = Math.min(intensity, 19); // Clamp to array size
                    gc.setFill(pheromoneColors[index]);
                    gc.fillRect(drawX, drawY, cellSize, cellSize);
                }
            }
        }
    }


    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(this::drawMap);
    }

    @Override
    public void statsChanged(StatsRecord stats) {
        Platform.runLater(() -> {
            dayLabel.setText("Day: " + stats.day());
            animalCountLabel.setText("Animals: " + stats.animalCount());
            plantCountLabel.setText("Plants: " + stats.plantCount());
            emptyFieldsLabel.setText("Empty Fields: " + stats.freeTilesCount());
            avgEnergyLabel.setText(String.format("Avg Energy: %.2f", stats.averageEnergyLevel()));
            avgLifeSpanLabel.setText(String.format("Avg LifeSpan: %.2f", stats.averageLifeTime()));
            childrenCountLabel.setText(String.format("Avg Children: %.2f", stats.averageKids()));

            this.topGenotypes.clear();
            if (stats.mostPopularGenotypes() != null) {
                this.topGenotypes.addAll(stats.mostPopularGenotypes());

                // Update Label text
                StringBuilder sb = new StringBuilder("Top Genotypes:\n");
                for (int i = 0; i < stats.mostPopularGenotypes().size(); i++) {
                    sb.append(i + 1).append(". ").append(stats.mostPopularGenotypes().get(i)).append("\n");
                }
                mostCommonGenotypeLabel.setText(sb.toString());
            }

            updateTrackedAnimalStats();

            if (chartManager != null) {
                chartManager.updateChart(stats);
            }
        });
    }

    private void updateTrackedAnimalStats() {
        if (trackedAnimal != null) {
            if (trackedAnimal.getDayOfDeath() != -1) {
                trackedAnimalStatusLabel.setText("Status: DEAD (ID: " + trackedAnimal.hashCode() + ")");
                trackedAnimalStatusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                trackedDeathDayLabel.setText("Death Day: " + trackedAnimal.getDayOfDeath());
            } else {
                trackedAnimalStatusLabel.setText("Status: ALIVE (ID: " + trackedAnimal.hashCode() + ")");
                trackedAnimalStatusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                trackedDeathDayLabel.setText("Death Day: -");
            }

            trackedGenotypeLabel.setText("Genotype: " + trackedAnimal.getGenotype());
            trackedActiveGeneLabel.setText("Active gen: " + trackedAnimal.getGenotype().getActiveGeneIndex());

            trackedEnergyLabel.setText("Energy: " + trackedAnimal.getEnergy());
            trackedEatenLabel.setText("Eaten plants: " + trackedAnimal.getNumberOfEatenPlants());
            trackedChildrenLabel.setText("Children: " + trackedAnimal.getNumberOfChildren());
            trackedAgeLabel.setText("Age: " + trackedAnimal.getAge());

            trackedDescendantsLabel.setText("Descendants: " + trackedAnimal.getNumberOfDescendants());
        }
    }

    /*
     * Controls
     * */

    @FXML
    private void onPauseClicked() {
        simulation.pause();
        isPaused = true;
        drawMap(); // redraw to enable highlights that work only on pause
    }

    @FXML
    private void onStartClicked() {
        simulation.resume();
        isPaused = false;
        drawMap();
    }

    @FXML
    private void onShowPreferredFieldsClicked() {
        this.showPreferredFields = preferredFieldsToggle.isSelected();
        drawMap();
    }

    @FXML
    private void onShowDominantGenotypeClicked() {
        this.showDominantGenotype = dominantGenotypeToggle.isSelected();
        drawMap();
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.rgb(180, 255, 180));
        gc.setLineWidth(Math.max(0.5, cellSize * 0.05));

        double gridWidth = mapWidth * cellSize;
        double gridHeight = mapHeight * cellSize;

        for (int x = 0; x <= mapWidth; x++) {
            double xPos = offsetX + (x * cellSize);
            gc.strokeLine(xPos, offsetY, xPos, offsetY + gridHeight);
        }

        for (int y = 0; y <= mapHeight; y++) {
            double yPos = offsetY + (y * cellSize);
            gc.strokeLine(offsetX, yPos, offsetX + gridWidth, yPos);
        }
    }

    private Color getEnergyColor(Animal animal) {
        int energy = animal.getEnergy();
        double hue = (double) (energy * 240) / animal.getMaxEnergy();
        return Color.hsb(hue, 1, 1);
    }

    // empty implementation, not used in GUI
    @Override
    public void handleDeadAnimals(List<Animal> deadAnimals) {
    }

    public void onWindowClose() {
        simulation.shutDown();
    }
}