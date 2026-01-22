package agh.ics.oop.simulationGUI;

import agh.ics.oop.configuration.ConfigBuilder;
import agh.ics.oop.model.exception.ConfigurationException;
import agh.ics.oop.model.map.MapType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ConfigurationWindowPresenter {
    @FXML
    private Spinner<Integer> moveDelaySpinner;
    @FXML
    private Spinner<Integer> maxEnergySpinner;
    @FXML
    private Spinner<Integer> energyToReproduceSpinner;
    @FXML
    private Spinner<Integer> energyConsumedByMoveSpinner;
    @FXML
    private Spinner<Integer> energyGainedByEatingSpinner;
    @FXML
    private Spinner<Integer> minMutationsSpinner;
    @FXML
    private Spinner<Integer> maxMutationsSpinner;
    @FXML
    private Spinner<Integer> genotypeLengthSpinner;

    @FXML
    private Spinner<Integer> widthSpinner;
    @FXML
    private Spinner<Integer> heightSpinner;
    @FXML
    private Spinner<Integer> startPlantNumberSpinner;
    @FXML
    private Spinner<Integer> dailyPlantNumberSpinner;
    @FXML
    private ChoiceBox<MapType> mapTypeChoiceBox;
    @FXML
    private Spinner<Double> moveToFeromonProbabilitySpinner;
    @FXML
    private Spinner<Integer> daysToDecreaseFeromonSpinner;
    @FXML
    private Spinner<Integer> smellRangeSpinner;
    @FXML
    private Spinner<Integer> initialAnimalCountSpinner;
    @FXML
    private CheckBox saveToCSVCheckBox;

    @FXML
    private Spinner<Integer> initialEnergySpinner;

    @FXML
    private Button startSimulationButton;

    @FXML
    private Button stopSimulationButton;

    @FXML
    private Button closeButton;

    private ConfigBuilder configBuilder;

    // Consumer is a functional interface representing a function that takes one argument and returns no result.
    private Consumer<ConfigBuilder> onStartSimulation; // callback to notify when simulation should start

    /**
     * This is called manually AFTER FXMLLoader.load()
     */
    public void setConfigBuilder(ConfigBuilder builder) {
        this.configBuilder = builder;

        maxEnergySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, builder.getMaxEnergy()));
        configureSpinner(maxEnergySpinner);

        initialAnimalCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, builder.getInitialAnimalCount()));
        configureSpinner(initialAnimalCountSpinner);

        initialEnergySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, builder.getInitialEnergy()));
        configureSpinner(initialEnergySpinner);

        energyToReproduceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, builder.getEnergyToReproduce()));
        configureSpinner(energyToReproduceSpinner);

        energyConsumedByMoveSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, builder.getEnergyConsumedByMove()));
        configureSpinner(energyConsumedByMoveSpinner);

        energyGainedByEatingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, builder.getEnergyGainedByEating()));
        configureSpinner(energyGainedByEatingSpinner);

        minMutationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, builder.getMinMutations()));
        configureSpinner(minMutationsSpinner);

        maxMutationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, builder.getMaxMutations()));
        configureSpinner(maxMutationsSpinner);

        genotypeLengthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, builder.getGenotypeLength()));
        configureSpinner(genotypeLengthSpinner);

        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, builder.getWidth()));
        configureSpinner(widthSpinner);

        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, builder.getHeight()));
        configureSpinner(heightSpinner);

        startPlantNumberSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, builder.getStartPlantNumber()));
        configureSpinner(startPlantNumberSpinner);

        dailyPlantNumberSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, builder.getDailyPlantNumber()));
        configureSpinner(dailyPlantNumberSpinner);

        daysToDecreaseFeromonSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, builder.getDaysToDecreaseFeromon()));
        configureSpinner(daysToDecreaseFeromonSpinner);

        smellRangeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, builder.getSmellRange()));
        configureSpinner(smellRangeSpinner);

        moveToFeromonProbabilitySpinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1.0, builder.getMoveToFeromonProbability(), 0.05)
        );
        configureDoubleSpinner(moveToFeromonProbabilitySpinner);

        moveDelaySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2000, builder.getMoveDelay(), 100)
        );
        configureSpinner(moveDelaySpinner);

        mapTypeChoiceBox.getItems().setAll(MapType.values());
        mapTypeChoiceBox.setValue(builder.getMapType());

        saveToCSVCheckBox.setSelected(builder.isSaveToCSV());
    }

    public void setStartSimulation(Consumer<ConfigBuilder> callback) {
        this.onStartSimulation = callback;
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSaveConfiguration() {
        try {
            // Only save if configuration is valid
            if (updateConfigFromUI()) {
                javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
                fileChooser.setTitle("Save Configuration");
                fileChooser.getExtensionFilters().add(
                        new javafx.stage.FileChooser.ExtensionFilter("JSON files", "*.json")
                );

                java.io.File file = fileChooser.showSaveDialog(maxEnergySpinner.getScene().getWindow());

                if (file != null) {
                    saveToFile(file, configBuilder);
                    System.out.println("Configuration saved to: " + file.getAbsolutePath());
                }
            }
        } catch (java.io.IOException e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("Failed to save the file to disk");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleStartSimulation() {
        // Only proceed if configuration is valid
        if (updateConfigFromUI()) {
            if (onStartSimulation != null) {
                onStartSimulation.accept(configBuilder);
            }
            handleClose();
        }
    }

    private boolean updateConfigFromUI() {
        try {
            configBuilder.setMaxEnergy(maxEnergySpinner.getValue());
            configBuilder.setInitialAnimalCount(initialAnimalCountSpinner.getValue());
            configBuilder.setInitialEnergy(initialEnergySpinner.getValue());
            configBuilder.setEnergyToReproduce(energyToReproduceSpinner.getValue());
            configBuilder.setEnergyConsumedByMove(energyConsumedByMoveSpinner.getValue());
            configBuilder.setEnergyGainedByEating(energyGainedByEatingSpinner.getValue());
            configBuilder.setGenotypeLength(genotypeLengthSpinner.getValue());
            configBuilder.setMaxMutations(maxMutationsSpinner.getValue());
            configBuilder.setMinMutations(minMutationsSpinner.getValue());
            configBuilder.setWidth(widthSpinner.getValue());
            configBuilder.setHeight(heightSpinner.getValue());
            configBuilder.setStartPlantNumber(startPlantNumberSpinner.getValue());
            configBuilder.setDailyPlantNumber(dailyPlantNumberSpinner.getValue());
            configBuilder.setMapType(mapTypeChoiceBox.getValue());
            configBuilder.setMoveToFeromonProbability(moveToFeromonProbabilitySpinner.getValue());
            configBuilder.setDaysToDecreaseFeromon(daysToDecreaseFeromonSpinner.getValue());
            configBuilder.setSmellRange(smellRangeSpinner.getValue());
            configBuilder.setMoveDelay(moveDelaySpinner.getValue());
            configBuilder.setSaveToCSV(saveToCSVCheckBox.isSelected());

            return true; // Success!

        } catch (ConfigurationException e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Invalid Configuration");
            alert.setHeaderText("Error in configuration");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

            return false; // Failure!
        }
    }

    private void saveToFile(java.io.File file, ConfigBuilder config) throws java.io.IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(file, config);
    }

    /**
     * Metoda pomocnicza konfigurująca Spinner
     */
    private void configureSpinner(Spinner<Integer> spinner) {
        spinner.setEditable(true);

        TextFormatter<Integer> formatter = new TextFormatter<>(
                spinner.getValueFactory().getConverter(),
                spinner.getValueFactory().getValue(),
                change -> {
                    if (change.getControlNewText().matches("-?\\d*")) {
                        return change;
                    }
                    return null;
                }
        );

        spinner.getEditor().setTextFormatter(formatter);
        spinner.getValueFactory().valueProperty().bindBidirectional(formatter.valueProperty());

        spinner.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                spinner.increment(0);
            }
        });
    }

    private void configureDoubleSpinner(Spinner<Double> spinner) {
        spinner.setEditable(true);

        TextFormatter<Double> formatter = new TextFormatter<>(
                spinner.getValueFactory().getConverter(),
                spinner.getValueFactory().getValue(),
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("-?([0-9]*)?(\\.[0-9]*)?")) {
                        return change;
                    }
                    return null;
                }
        );

        spinner.getEditor().setTextFormatter(formatter);
        spinner.getValueFactory().valueProperty().bindBidirectional(formatter.valueProperty());

        spinner.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                spinner.increment(0);
            }
        });
    }

    private void commitSpinner(Spinner<?> spinner) {
        if (spinner.isEditable()) {
            // This little trick forces the Spinner to read the text field
            // and update its internal value immediately.
            spinner.increment(0);
        }
    }
}
