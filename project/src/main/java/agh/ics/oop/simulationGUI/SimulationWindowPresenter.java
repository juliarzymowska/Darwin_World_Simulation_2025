package agh.ics.oop.simulationGUI;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

public class SimulationWindowPresenter implements MapChangeListener, StatsChangeListener {
    @FXML
    private Label mostCommonGenotypeLabel;
    @FXML
    private Label childrenCountLabel;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private StackPane mapContainer;

    // Stats Labels
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

    @FXML
    private LineChart<Number, Number> statsChart;
    private ChartManager chartManager;

    private WorldMap worldMap;
    private Simulation simulation;

    // Drawing constants
    private static final double CELL_SIZE = 20.0;

    // We calculate these based on the map size
    private double mapWidth;
    private double mapHeight;

    @FXML
    public void initialize() {
        // Auto-scaling logic (from your previous project)
        mapContainer.widthProperty().addListener((obs, oldVal, newVal) -> drawMap());
        mapContainer.heightProperty().addListener((obs, oldVal, newVal) -> drawMap());
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.worldMap = simulation.getMap();
        this.chartManager = new ChartManager(statsChart);

        // Initialize map dimensions for drawing
        // Assuming map is a rectangle from (0,0) to (width, height)
        this.mapWidth = worldMap.getCurrentBounds().rightUpMapCorner().getX() + 1;
        this.mapHeight = worldMap.getCurrentBounds().rightUpMapCorner().getY() + 1;

        // Resize canvas to fit the map exactly
        mapCanvas.setWidth(mapWidth * CELL_SIZE);
        mapCanvas.setHeight(mapHeight * CELL_SIZE);

        // Subscribe to events
        worldMap.addObserver(this);
        simulation.getStats().addObserver(this);

        // Initial Draw
        drawMap();
    }

    // --- Drawing Logic ---

    private void drawMap() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();

        // 1. Wipe and Fill Background (Grass)
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        // 2. Draw Pheromones (NEW STEP)
        drawPheromones(gc);

        // 3. Draw Grid Lines
        drawGrid(gc);

        // 4. Draw Objects (Animals/Plants)
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Vector2d position = new Vector2d(x, y);
                // Note: We cast to EarthMap to use the generic objectAt
                // If you are using FeromonMap, objectAt might return a Feromon if no animal is there,
                // but we handle drawing Feromones separately in step 2, so we can ignore them here
                // OR handle them if your objectAt returns them.
                WorldElement object = ((agh.ics.oop.model.map.EarthMap) worldMap).objectAt(position);

                // Important: Don't draw Feromon object again if it's returned by objectAt
                if (object != null && !(object instanceof agh.ics.oop.model.elements.Feromon)) {
                    drawObject(gc, object, x, y);
                }
            }
        }

        // 5. Scale
        autoScaleMap();
    }

    private void drawPheromones(GraphicsContext gc) {
        // Only run this logic if the map is actually a FeromonMap
        if (worldMap instanceof agh.ics.oop.model.map.FeromonMap feromonMap) {

            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    Vector2d position = new Vector2d(x, y);

                    // Use the new method we added to FeromonMap
                    int intensity = feromonMap.getSmellValue(position);

                    if (intensity > 0) {
                        double drawX = x * CELL_SIZE;
                        double drawY = (mapHeight - 1 - y) * CELL_SIZE;

                        // Calculate opacity:
                        // Low smell = transparent blue
                        // High smell = darker blue
                        // We cap it at 0.6 so we never completely hide the grass/grid
                        double opacity = Math.min(intensity * 0.15, 0.6);

                        gc.setFill(Color.rgb(0, 0, 255, opacity));
                        gc.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    private void drawObject(GraphicsContext gc, WorldElement object, int x, int y) {
        // Convert Map coordinates (Bottom-Left 0,0) to Canvas coordinates (Top-Left 0,0)
        // Canvas Y = Total Height - 1 - Map Y
        double drawX = x * CELL_SIZE;
        double drawY = (mapHeight - 1 - y) * CELL_SIZE;

        if (object instanceof Plant) {
            gc.setFill(Color.DARKGREEN);
            // Draw a square or flower shape
            gc.fillOval(drawX + 2, drawY + 2, CELL_SIZE - 4, CELL_SIZE - 4);

        } else if (object instanceof Animal animal) {
            gc.setFill(getEnergyColor(animal));
            // Draw a circle for animal
            gc.fillOval(drawX, drawY, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.rgb(180, 255, 180)); // Faint grid lines
        gc.setLineWidth(1);

        for (int x = 0; x <= mapWidth; x++) {
            gc.strokeLine(x * CELL_SIZE, 0, x * CELL_SIZE, mapHeight * CELL_SIZE);
        }
        for (int y = 0; y <= mapHeight; y++) {
            gc.strokeLine(0, y * CELL_SIZE, mapWidth * CELL_SIZE, y * CELL_SIZE);
        }
    }

    private Color getEnergyColor(Animal animal) {
        // Visualizing energy: Red (low) -> Yellow -> Blue (high)
        int energy = animal.getEnergy();
        if (energy < 10) return Color.RED;
        if (energy < 30) return Color.ORANGE;
        if (energy < 50) return Color.YELLOW;
        return Color.BLUE;
    }

    private void autoScaleMap() {
        // Safety check: if the container is not visible yet, skip
        if (mapContainer.getWidth() == 0 || mapContainer.getHeight() == 0) return;

        // 1. Calculate how much we need to shrink/zoom
        double widthRatio = mapContainer.getWidth() / mapCanvas.getWidth();
        double heightRatio = mapContainer.getHeight() / mapCanvas.getHeight();

        // 2. Choose the smaller ratio so the whole map fits
        double scale = Math.min(widthRatio, heightRatio);

        // 3. Apply the scale
        mapCanvas.setScaleX(scale);
        mapCanvas.setScaleY(scale);

        // CRITICAL: Do NOT setTranslateX or setTranslateY here.
        // The StackPane in your FXML automatically keeps the Canvas in the center.
    }

    // --- Event Handling ---

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        // Run on JavaFX Thread
        Platform.runLater(this::drawMap);
    }

//    @Override
//    public void statsChanged(StatsRecord stats) {
//        Platform.runLater(() -> {
//            dayLabel.setText("Day: " + stats.day());
//            animalCountLabel.setText("Animals: " + stats.animalCount());
//            plantCountLabel.setText("Plants: " + stats.plantCount());
//            emptyFieldsLabel.setText("Empty Fields: " + stats.freeTilesCount());
//            avgEnergyLabel.setText(String.format("Avg Energy: %.2f", stats.averageEnergyLevel()));
//            avgLifeSpanLabel.setText(String.format("Avg LifeSpan: %.2f", stats.averageLifeTime()));

    /// /            mostCommonGenotypeLabel.setText("Most Common Genotype: " + stats.());
//            childrenCountLabel.setText(String.format("Avg Children: %.2f", stats.averageKids()));
//
//            // LOGIC FOR GENOTYPE
//            if (stats.mostPopularGenotypes() == null || stats.mostPopularGenotypes().isEmpty()) {
//                mostCommonGenotypeLabel.setText("Top Genotype: -");
//            } else {
//                // Get the winner (index 0)
//                Genotype top = stats.mostPopularGenotypes().get(0);
//                mostCommonGenotypeLabel.setText("Top Genotype: " + top.toString());
//            }
//        });
//    }
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

            // --- CHANGED LOGIC FOR TOP 3 GENOTYPES ---
            updateGenotypeLabel(stats.mostPopularGenotypes());

            chartManager.updateChart(stats);
        });
    }

    private void updateGenotypeLabel(List<Genotype> genotypes) {
        if (genotypes == null || genotypes.isEmpty()) {
            mostCommonGenotypeLabel.setText("Top Genotypes: -");
            return;
        }

        StringBuilder sb = new StringBuilder("Top Genotypes:\n");
        for (int i = 0; i < genotypes.size(); i++) {
            // Format: "1. [0, 4, 2...]"
            sb.append(i + 1).append(". ").append(genotypes.get(i).toString()).append("\n");
        }

        mostCommonGenotypeLabel.setText(sb.toString());
    }

    @Override
    public void handleDeadAnimals(List<Animal> deadAnimals) {
        // We don't need to redraw here because 'mapChanged' will be called
        // at the end of the day cycle anyway.

        // However, if you wanted to animate them "fading out" or play a sound,
        // this is where you would do it.

        // Example logging (optional):
        // System.out.println("GUI Notified: " + deadAnimals.size() + " animals died.");
    }

    @FXML
    private void onPauseClicked() {
        simulation.pause();
    }

    @FXML
    private void onStartClicked() {
        simulation.resume();
    }

    public void onWindowClose() {
        simulation.shutDown();
    }
}