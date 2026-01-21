package agh.ics.oop.simulationGUI;

import agh.ics.oop.configuration.ConfigBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SimulationWindowPresenter {

    @FXML
    private Label initialAnimalCountLabel;

    @FXML
    private Label initialEnergyLabel;

    public void setConfig(ConfigBuilder config) {
        initialAnimalCountLabel.setText(
                "Initial animals: " + config.getInitialAnimalCount()
        );
        initialEnergyLabel.setText(
                "Initial energy: " + config.getInitialEnergy()
        );
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) initialAnimalCountLabel.getScene().getWindow();
        stage.close();
    }
}
