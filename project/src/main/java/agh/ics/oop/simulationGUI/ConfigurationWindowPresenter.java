package agh.ics.oop.simulationGUI;

import agh.ics.oop.configuration.ConfigBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConfigurationWindowPresenter {

    @FXML
    private TextField initialAnimalCountField;

    @FXML
    private TextField initialEnergyField;

    @FXML
    private Button startSimulationButton;

    private ConfigBuilder configBuilder;

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

    @FXML
    private Button closeButton;

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

        // NEXT STEP:
        // open simulation window
        // close config window
    }
}
