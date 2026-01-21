package agh.ics.oop.simulationGUI;

import agh.ics.oop.configuration.ConfigBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ConfigurationWindowPresenter {

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

        // pre-fill fields
        initialAnimalCountField.setText(
                String.valueOf(builder.getInitialAnimalCount())
        );
        initialEnergyField.setText(
                String.valueOf(builder.getInitialEnergy())
        );
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
        // Update builder from fields (no validation yet)
        configBuilder.setInitialAnimalCount(
                Integer.parseInt(initialAnimalCountField.getText())
        );
        configBuilder.setInitialEnergy(
                Integer.parseInt(initialEnergyField.getText())
        );

        System.out.println("Config ready, starting simulation...");
        System.out.println(configBuilder);

        if (onStartSimulation != null) {
            onStartSimulation.accept(configBuilder);
        }
        handleClose();
    }
}
