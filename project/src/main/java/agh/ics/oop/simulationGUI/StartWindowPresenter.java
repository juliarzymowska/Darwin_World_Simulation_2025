package agh.ics.oop.simulationGUI;

import agh.ics.oop.configuration.ConfigBuilder;
import agh.ics.oop.configuration.ConfigLoadFromJSON;
import agh.ics.oop.model.exception.ConfigurationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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

                openConfigurationWindow(builder);
            } catch (ConfigurationException | IOException e) {
                // TODO: Show exception message in alert (after merge with exceptions branch and adding custom
                //  exceptions to ConfigBuilder and ConfigLoadFromJSON)
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Invalid Configuration");
                alert.setHeaderText("Error loading JSON");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
//                e.printStackTrace();
            }
        }
    }

    private void openConfigurationWindow(ConfigBuilder builder) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(
                    getClass().getClassLoader().getResource(
                            "configurationWindow/configuration-window.fxml"
                    )
            );
            Parent root = loader.load();

            ConfigurationWindowPresenter presenter = loader.getController();
            presenter.setConfigBuilder(builder);
            Scene scene = new Scene(root);
            scene.getAccelerators().put( // Escape to close
                    new KeyCodeCombination(KeyCode.ESCAPE),
                    () -> ((Stage) scene.getWindow()).close()
            );

            presenter.setStartSimulation(finalBuilder -> {
                openSimulationWindow(builder);
            });

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Configuration");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openSimulationWindow(ConfigBuilder builder) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource(
                            "simulationWindow/simulation-window.fxml"
                    )
            );
            Parent root = loader.load();

            SimulationWindowPresenter presenter = loader.getController();
            presenter.setConfig(builder);


            Stage stage = new Stage();
            stage.setTitle("Simulation");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        javafx.application.Platform.exit();
        System.exit(0);
    }
}
