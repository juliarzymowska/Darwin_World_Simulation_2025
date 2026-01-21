package agh.ics.oop.simulationGUI;

import agh.ics.oop.configuration.ConfigBuilder;
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

    public void setConfigBuilder(ConfigBuilder builder) {
        this.configBuilder = builder;

        initialAnimalCountField.setText(String.valueOf(builder.getInitialAnimalCount()));
        initialEnergyField.setText(String.valueOf(builder.getInitialEnergy()));
        maxEnergyField.setText(String.valueOf(builder.getMaxEnergy()));
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
        configBuilder.setInitialAnimalCount(Integer.parseInt(initialAnimalCountField.getText()));
        configBuilder.setInitialEnergy(Integer.parseInt(initialEnergyField.getText()));
        configBuilder.setMaxEnergy(Integer.parseInt(maxEnergyField.getText()));
        configBuilder.setEnergyToReproduce(Integer.parseInt(energyToReproduceField.getText()));
        configBuilder.setEnergyConsumedByMove(Integer.parseInt(energyConsumedByMoveField.getText()));
        configBuilder.setEnergyGainedByEating(Integer.parseInt(energyGainedByEatingField.getText()));
        configBuilder.setMinMutations(Integer.parseInt(minMutationsField.getText()));
        configBuilder.setMaxMutations(Integer.parseInt(maxMutationsField.getText()));
        configBuilder.setGenotypeLength(Integer.parseInt(genotypeLengthField.getText()));

        configBuilder.setWidth(Integer.parseInt(widthField.getText()));
        configBuilder.setHeight(Integer.parseInt(heightField.getText()));
        configBuilder.setStartPlantNumber(Integer.parseInt(startPlantNumberField.getText()));
        configBuilder.setDailyPlantNumber(Integer.parseInt(dailyPlantNumberField.getText()));
        configBuilder.setMapType(mapTypeChoiceBox.getValue());
        configBuilder.setMoveToFeromonProbability(Double.parseDouble(moveToFeromonProbabilityField.getText()));
        configBuilder.setDaysToDecreaseFeromon(Integer.parseInt(daysToDecreaseFeromonField.getText()));
        configBuilder.setSmellRange(Integer.parseInt(smellRangeField.getText()));

//        System.out.println("Config ready, starting simulation...");
//        System.out.println(configBuilder);

        if (onStartSimulation != null) {
            onStartSimulation.accept(configBuilder);
        }
        handleClose();
    }
}
