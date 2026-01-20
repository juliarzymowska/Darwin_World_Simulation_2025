package agh.ics.oop.simulationGUI;

import agh.ics.oop.configuration.ConfigBuilder;
import agh.ics.oop.configuration.ConfigLoadFromJSON;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class StartWindowPresenter {
    @FXML
    private Button closeButton;
    @FXML
    private Button loadFromJSON;
    @FXML
    private Button startButton;

    @FXML
    private void handleStartSimulation() {
        // TODO: Open configuration window or start with default config
        System.out.println("Start simulation clicked");
    }

    @FXML
    private void handleLoadFromJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load configuration");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        File selectedFile = fileChooser.showOpenDialog(loadFromJSON.getScene().getWindow());
        if (selectedFile != null) {
            try {
                ConfigLoadFromJSON parser = new ConfigLoadFromJSON(selectedFile.getAbsolutePath());
                ConfigBuilder builder = parser.loadConfig();
                System.out.println("Configuration loaded successfully!");

                // TODO: pass builder to configuration window or start simulation
            } catch (Exception e) {
                // TODO: show error alert
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClose() {
        javafx.application.Platform.exit();
        System.exit(0);
    }
}
