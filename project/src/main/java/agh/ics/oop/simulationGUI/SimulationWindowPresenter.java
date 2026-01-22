package agh.ics.oop.simulationGUI;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.map.EarthMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.observators.MapChangeListener;
import agh.ics.oop.model.observators.StatsChangeListener;
import agh.ics.oop.model.stats.StatsRecord;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.simulation.Simulation;
import com.sun.javafx.scene.control.SelectedItemsReadOnlyObservableList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.chart.LineChart;

import java.util.List;

/*
 * Class for controlling the simulation window GUI
 * It handles user interactions, updates the map rendering, and displays statistics.
 * */
public class SimulationWindowPresenter implements MapChangeListener, StatsChangeListener {
    @FXML
    private Canvas mapCanvas;
    @FXML
    private StackPane mapContainer;
    @FXML
    private ToggleButton preferredFieldsToggle, dominantGenotypeToggle;
    @FXML
    private ChoiceBox<String> statChoiceBox;
    @FXML
    private Button stopTrackingButton;
    @FXML
    private LineChart<Number, Number> statsChart;

    // package private for StatsPresenter
    @FXML
    Label dayLabel, animalCountLabel, plantCountLabel, emptyFieldsLabel, avgEnergyLabel, avgLifeSpanLabel, childrenCountLabel, mostCommonGenotypeLabel;
    @FXML
    Label trackedAnimalStatusLabel, trackedGenotypeLabel, trackedActiveGeneLabel, trackedEnergyLabel, trackedEatenLabel, trackedChildrenLabel, trackedDescendantsLabel, trackedAgeLabel, trackedDeathDayLabel;

    // debug mode
    @FXML
    private ListView<String> logListView;

    private boolean isDebugMode = false;

    // simulation state
    private WorldMap worldMap;
    private Simulation simulation;
    private Animal trackedAnimal = null;

    // Sub-components
    private MapRenderer mapRenderer;
    private StatsPresenter statsPresenter;

    private boolean isPaused = true;

    /*
     * Initialization logic
     * */
    @FXML
    public void initialize() {
        // Initialize helpers
        this.mapRenderer = new MapRenderer(mapCanvas);
        this.statsPresenter = new StatsPresenter(this, statsChart);

        // Layout logic
        mapCanvas.setManaged(false);
        mapCanvas.widthProperty().bind(mapContainer.widthProperty());
        mapCanvas.heightProperty().bind(mapContainer.heightProperty());

        mapContainer.widthProperty().addListener((o, old, val) -> drawMap());
        mapContainer.heightProperty().addListener((o, old, val) -> drawMap());

        mapCanvas.setOnMouseClicked(this::handleMapClick);

        stopTrackingButton.setDisable(true);

        statChoiceBox.getItems().addAll("Animal Count", "Plant Count", "Free Tiles", "Avg Energy", "Avg Lifespan", "Avg Children");
        statChoiceBox.setValue("Animal Count");
        statChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) statsPresenter.setChartStat(newVal);
        });
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.worldMap = simulation.getMap();

        worldMap.addObserver(this);
        simulation.getStats().addObserver(this);
        drawMap();
    }

    /*
     * handling clicking on map / buttons
     * */
    private void handleMapClick(MouseEvent event) {
        Vector2d position = mapRenderer.getClickPosition(event.getX(), event.getY());

        if (position != null && worldMap instanceof EarthMap map) {
            Animal clickedAnimal = map.getAnimalAt(position);
            if (clickedAnimal != null) {
                this.trackedAnimal = clickedAnimal;
                stopTrackingButton.setDisable(false);
                statsPresenter.updateTrackedAnimal();
                drawMap();
            }
        }
    }

    @FXML
    private void onStopTrackingClicked() {
        this.trackedAnimal = null;
        stopTrackingButton.setDisable(true);
        statsPresenter.clearTrackedStats();
        drawMap();
    }

    @FXML
    private void onPauseClicked() {
        simulation.pause();
        isPaused = true;
        drawMap();
    }

    @FXML
    private void onStartClicked() {
        simulation.resume();
        isPaused = false;
        drawMap();
    }

    @FXML
    private void onShowPreferredFieldsClicked() {
        drawMap();
    }

    @FXML
    private void onShowDominantGenotypeClicked() {
        drawMap();
    }

    /*
     * map drawing logic
     * */
    private void drawMap() {
        // Create context using current state
        RenderContext context = new RenderContext(
                trackedAnimal,
                statsPresenter.getTopGenotypes(), // Get latest genotypes from stats
                preferredFieldsToggle.isSelected(),
                dominantGenotypeToggle.isSelected()
        );

        mapRenderer.draw(worldMap, context);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(this::drawMap);
        logMessage(message);
    }

    // debug mode
    public void setDebugMode(boolean debugMode) {
        this.isDebugMode = debugMode;
        if (!debugMode) {
            logListView.setVisible(false);
            logListView.setManaged(false);
        }
    }

    /*
     * Others
     * */

    // helper for debug mode
    private void logMessage(String message) {
        if (!isDebugMode || message == null || message.isEmpty()) return;

        Platform.runLater(() -> {
            logListView.getItems().add(message);

            // Auto-scroll
            logListView.scrollTo(logListView.getItems().size() - 1);

            // Limit history -> better optimization
            if (logListView.getItems().size() > 100) {
                logListView.getItems().remove(0);
            }
        });
    }


    @Override
    public void statsChanged(StatsRecord stats) {
        Platform.runLater(() -> statsPresenter.updateStats(stats));
    }

    public Animal getTrackedAnimal() {
        return trackedAnimal;
    }

    @Override
    public void handleDeadAnimals(List<Animal> deadAnimals) {
        if (deadAnimals.isEmpty()) return;

        int count = deadAnimals.size();
        if (count == 0) {
            String message = "✝ No animals died.";
            logMessage(message);
        }
        if (count == 1) {
            String message = String.format("✝ %d animal died");
            logMessage(message);
        } else {
            String message = String.format("✝ %d animals died.", count);
            logMessage(message);
        }

    }

    public void onWindowClose() {
        simulation.shutDown();
    }
}