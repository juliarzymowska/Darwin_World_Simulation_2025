package agh.ics.oop.simulationGUI;

import agh.ics.oop.configuration.ConfigBuilder;
import agh.ics.oop.model.exception.ConfigurationException;
import agh.ics.oop.model.map.MapType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ConfigurationWindowPresenter {
    // TODO: change TextField to Spinner
    @FXML
    private TextField maxEnergyField;
    @FXML
    private TextField energyToReproduceField;
    @FXML
    private TextField energyConsumedByMoveField;
    @FXML
    private TextField energyGainedByEatingField;
    @FXML
    private TextField minMutationsField;
    @FXML
    private TextField maxMutationsField;
    @FXML
    private TextField genotypeLengthField;

    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField startPlantNumberField;
    @FXML
    private TextField dailyPlantNumberField;
    @FXML
    private ChoiceBox<MapType> mapTypeChoiceBox;
    @FXML
    private TextField moveToFeromonProbabilityField;
    @FXML
    private TextField daysToDecreaseFeromonField;
    @FXML
    private TextField smellRangeField;
    @FXML
    private TextField initialAnimalCountField;

    @FXML
    private TextField initialEnergyField;

    @FXML
    private Button startSimulationButton;

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

        maxEnergyField.setText(String.valueOf(builder.getMaxEnergy()));
        initialAnimalCountField.setText(String.valueOf(builder.getInitialAnimalCount()));
        initialEnergyField.setText(String.valueOf(builder.getInitialEnergy()));
        energyToReproduceField.setText(String.valueOf(builder.getEnergyToReproduce()));
        energyConsumedByMoveField.setText(String.valueOf(builder.getEnergyConsumedByMove()));
        energyGainedByEatingField.setText(String.valueOf(builder.getEnergyGainedByEating()));
        minMutationsField.setText(String.valueOf(builder.getMinMutations()));
        maxMutationsField.setText(String.valueOf(builder.getMaxMutations()));
        genotypeLengthField.setText(String.valueOf(builder.getGenotypeLength()));

        widthField.setText(String.valueOf(builder.getWidth()));
        heightField.setText(String.valueOf(builder.getHeight()));
        startPlantNumberField.setText(String.valueOf(builder.getStartPlantNumber()));
        dailyPlantNumberField.setText(String.valueOf(builder.getDailyPlantNumber()));
        mapTypeChoiceBox.getItems().setAll(MapType.values());
        mapTypeChoiceBox.setValue(builder.getMapType());
        moveToFeromonProbabilityField.setText(String.valueOf(builder.getMoveToFeromonProbability()));
        daysToDecreaseFeromonField.setText(String.valueOf(builder.getDaysToDecreaseFeromon()));
        smellRangeField.setText(String.valueOf(builder.getSmellRange()));
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
    private void handleStartSimulation() {
        try {
            int maxEnergy = parseOrDefault(maxEnergyField.getText(), configBuilder.getMaxEnergy());
            int initialEnergy = parseOrDefault(initialEnergyField.getText(), configBuilder.getInitialEnergy());

            configBuilder.setMaxEnergy(maxEnergy);
            configBuilder.setInitialAnimalCount(parseOrDefault(initialAnimalCountField.getText(), configBuilder.getInitialAnimalCount()));
            configBuilder.setInitialEnergy(initialEnergy);

            configBuilder.setEnergyToReproduce(parseOrDefault(energyToReproduceField.getText(), configBuilder.getEnergyToReproduce()));
            configBuilder.setEnergyConsumedByMove(parseOrDefault(energyConsumedByMoveField.getText(), configBuilder.getEnergyConsumedByMove()));
            configBuilder.setEnergyGainedByEating(parseOrDefault(energyGainedByEatingField.getText(), configBuilder.getEnergyGainedByEating()));
            configBuilder.setGenotypeLength(parseOrDefault(genotypeLengthField.getText(), configBuilder.getGenotypeLength()));
            configBuilder.setMaxMutations(parseOrDefault(maxMutationsField.getText(), configBuilder.getMaxMutations()));
            configBuilder.setMinMutations(parseOrDefault(minMutationsField.getText(), configBuilder.getMinMutations()));
            configBuilder.setWidth(parseOrDefault(widthField.getText(), configBuilder.getWidth()));
            configBuilder.setHeight(parseOrDefault(heightField.getText(), configBuilder.getHeight()));
            configBuilder.setStartPlantNumber(parseOrDefault(startPlantNumberField.getText(), configBuilder.getStartPlantNumber()));
            configBuilder.setDailyPlantNumber(parseOrDefault(dailyPlantNumberField.getText(), configBuilder.getDailyPlantNumber()));
            configBuilder.setMapType(mapTypeChoiceBox.getValue());
            configBuilder.setMoveToFeromonProbability(parseOrDefaultDouble(moveToFeromonProbabilityField.getText(), configBuilder.getMoveToFeromonProbability()));
            configBuilder.setDaysToDecreaseFeromon(parseOrDefault(daysToDecreaseFeromonField.getText(), configBuilder.getDaysToDecreaseFeromon()));
            configBuilder.setSmellRange(parseOrDefault(smellRangeField.getText(), configBuilder.getSmellRange()));

        } catch (ConfigurationException e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Invalid Configuration");
            alert.setHeaderText("Error in configuration");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        if (onStartSimulation != null) {
            onStartSimulation.accept(configBuilder);
        }
        handleClose();
    }

    // helper methods
    private int parseOrDefault(String text, int defaultValue) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseOrDefaultDouble(String text, double defaultValue) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
