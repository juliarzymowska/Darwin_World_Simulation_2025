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

import java.util.HashSet;
import java.util.List;
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

    private WorldMap worldMap;
    private Simulation simulation;
    private Animal trackedAnimal = null;
    private Set<Genotype> topGenotypes = new HashSet<>();
    private boolean isPaused = true; // Start paused usually

    private static final double CELL_SIZE = 20.0;
    private double mapWidth;
    private double mapHeight;

    private boolean showPreferredFields = false;
    private boolean showDominantGenotype = false;

    @FXML
    public void initialize() {
        mapContainer.widthProperty().addListener((obs, oldVal, newVal) -> drawMap());
        mapContainer.heightProperty().addListener((obs, oldVal, newVal) -> drawMap());

        // CLICK EVENT
        mapCanvas.setOnMouseClicked(this::handleMapClick);

        stopTrackingButton.setDisable(true);
    }

    private void handleMapClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        int col = (int) (x / CELL_SIZE);
        // The "Fixed" formula (without the -1)
        int row = (int) (mapHeight - (y / CELL_SIZE));

        // Safety check for bounds
        if (col < 0 || col >= mapWidth || row < 0 || row >= mapHeight) {
            return;
        }

        Vector2d position = new Vector2d(col, row);

        if (worldMap instanceof EarthMap map) {
            // Use your helper method to find the animal
            Animal clickedAnimal = map.getAnimalAt(position);

            if (clickedAnimal != null) {
                this.trackedAnimal = clickedAnimal;
                stopTrackingButton.setDisable(false);
                updateTrackedAnimalStats();
                drawMap(); // Redraw to show the gold ring
            }
        }
    }

    @FXML
    private void onStopTrackingClicked() {
        // 1. Wyczyszczenie referencji
        this.trackedAnimal = null;

        // 2. Zablokowanie przycisku
        stopTrackingButton.setDisable(true);

        // 3. Reset UI - tekstów informacyjnych
        trackedAnimalStatusLabel.setText("Status: not tracking");
        trackedAnimalStatusLabel.setStyle("-fx-text-fill: black;");

        clearTrackedStats(); // Wywołanie metody czyszczącej liczby

        // 4. Odświeżenie mapy (usunięcie złotego pierścienia)
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

        mapCanvas.setWidth(mapWidth * CELL_SIZE);
        mapCanvas.setHeight(mapHeight * CELL_SIZE);

        worldMap.addObserver(this);
        simulation.getStats().addObserver(this);
        drawMap();
    }

    // --- DRAWING LOGIC ---

    private void drawMap() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        // 1. Background
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        // 2. Preferred Fields (Jungle) - ONLY if toggled
        if (showPreferredFields) {
            drawPreferredFields(gc);
        }

        // 3. Pheromones
        drawPheromones(gc);

        // 4. Grid
        drawGrid(gc);

        // 5. Objects
        drawObjects(gc);

        autoScaleMap();
    }

    private void drawPreferredFields(GraphicsContext gc) {
        EarthMap map = (EarthMap) worldMap;
        // Darker Green for Jungle
        gc.setFill(Color.rgb(34, 139, 34, 0.5));

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (map.isPreferredPosition(new Vector2d(x, y))) {
                    double drawX = x * CELL_SIZE;
                    double drawY = (mapHeight - 1 - y) * CELL_SIZE;
                    gc.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void drawObjects(GraphicsContext gc) {
        EarthMap map = (EarthMap) worldMap;
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Vector2d pos = new Vector2d(x, y);
                WorldElement obj = map.objectAt(pos);

                if (obj == null || obj instanceof agh.ics.oop.model.elements.Feromon) continue;

                double drawX = x * CELL_SIZE;
                double drawY = (mapHeight - 1 - y) * CELL_SIZE;

                if (obj instanceof Plant) {
                    gc.setFill(Color.DARKGREEN);
                    gc.fillOval(drawX + 4, drawY + 4, CELL_SIZE - 8, CELL_SIZE - 8);
                } else if (obj instanceof Animal animal) {
                    gc.setFill(getEnergyColor(animal));
                    gc.fillOval(drawX, drawY, CELL_SIZE, CELL_SIZE);

                    // A. Tracked Animal Ring
                    if (animal.equals(trackedAnimal)) {
                        gc.setStroke(Color.GOLD);
                        gc.setLineWidth(3);
                        gc.strokeOval(drawX - 2, drawY - 2, CELL_SIZE + 4, CELL_SIZE + 4);
                    }

                    // B. Dominant Genotype Ring (Only if Paused + Toggled)
                    // (Requirement: "Wyróżnianie wizualne... (+2xp)")
                    if (showDominantGenotype && topGenotypes.contains(animal.getGenotype())) {
                        gc.setStroke(Color.MAGENTA);
                        gc.setLineWidth(2);
                        gc.strokeOval(drawX, drawY, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    private void drawPheromones(GraphicsContext gc) {
        if (worldMap instanceof FeromonMap feromonMap) {
            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    int intensity = feromonMap.getSmellValue(new Vector2d(x, y));
                    if (intensity > 0) {
                        double drawX = x * CELL_SIZE;
                        double drawY = (mapHeight - 1 - y) * CELL_SIZE;
                        double opacity = Math.min(intensity * 0.15, 0.6);
                        gc.setFill(Color.rgb(0, 0, 255, opacity));
                        gc.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    // --- STATS UPDATES ---

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

            // Update Top Genotypes for Highlighting
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

            // Update Tracked Animal
            updateTrackedAnimalStats();
        });
    }

    private void updateTrackedAnimalStats() {
        if (trackedAnimal != null) {
            // Podświetlenie statusu (Żyje/Martwy)
            if (trackedAnimal.getDayOfDeath() != -1) { // Zakładam, że -1 oznacza, że żyje
                trackedAnimalStatusLabel.setText("Status: DEAD (ID: " + trackedAnimal.hashCode() + ")");
                trackedAnimalStatusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                trackedDeathDayLabel.setText("Dzień śmierci: " + trackedAnimal.getDayOfDeath());
            } else {
                trackedAnimalStatusLabel.setText("Status: ALIVE (ID: " + trackedAnimal.hashCode() + ")");
                trackedAnimalStatusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                trackedDeathDayLabel.setText("Dzień śmierci: -");
            }

            // Aktualizacja genotypu i aktywnego genu
            trackedGenotypeLabel.setText("Genotype: " + trackedAnimal.getGenotype());
            // Upewnij się, że masz metodę getActiveGene() w klasie Genotype/Animal
            trackedActiveGeneLabel.setText("Active gen: " + trackedAnimal.getGenotype().getActiveGeneIndex());

            // Statystyki liczbowe
            trackedEnergyLabel.setText("Energy: " + trackedAnimal.getEnergy());
            trackedEatenLabel.setText("Eaten plants: " + trackedAnimal.getNumberOfEatenPlants());
            trackedChildrenLabel.setText("Children: " + trackedAnimal.getNumberOfChildren());
            trackedAgeLabel.setText("Age: " + trackedAnimal.getAge());

            trackedDescendantsLabel.setText("Descendants: " + trackedAnimal.getNumberOfDescendants());
        }
    }

    // --- CONTROLS ---

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

    // ... autoScaleMap, drawGrid, getEnergyColor helpers ...
    // (Keep them as they were in previous answers)

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.rgb(180, 255, 180));
        gc.setLineWidth(1);
        for (int x = 0; x <= mapWidth; x++) gc.strokeLine(x * CELL_SIZE, 0, x * CELL_SIZE, mapHeight * CELL_SIZE);
        for (int y = 0; y <= mapHeight; y++) gc.strokeLine(0, y * CELL_SIZE, mapWidth * CELL_SIZE, y * CELL_SIZE);
    }

    private Color getEnergyColor(Animal animal) {
        int energy = animal.getEnergy();
        if (energy < 10) return Color.RED;
        if (energy < 30) return Color.ORANGE;
        if (energy < 50) return Color.YELLOW;
        return Color.BLUE;
    }

    private void autoScaleMap() {
        if (mapContainer.getWidth() == 0 || mapContainer.getHeight() == 0) return;
        double widthRatio = mapContainer.getWidth() / mapCanvas.getWidth();
        double heightRatio = mapContainer.getHeight() / mapCanvas.getHeight();
        double scale = Math.min(widthRatio, heightRatio);
        mapCanvas.setScaleX(scale);
        mapCanvas.setScaleY(scale);
    }

    @Override
    public void handleDeadAnimals(List<Animal> deadAnimals) {
    }

    public void onWindowClose() {
        simulation.shutDown();
    }
}