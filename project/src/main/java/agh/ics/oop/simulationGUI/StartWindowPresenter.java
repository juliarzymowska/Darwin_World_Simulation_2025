package agh.ics.oop.simulationGUI;

import agh.ics.oop.configuration.ConfigAnimal;
import agh.ics.oop.configuration.ConfigBuilder;
import agh.ics.oop.configuration.ConfigLoadFromJSON;
import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.exception.*;
import agh.ics.oop.simulation.Simulation;
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
    private void handleStartSimulation() throws ConfigurationException {
        try {
            ConfigAnimal defaultAnimal = new ConfigAnimal();
            ConfigMap defaultMap = new ConfigMap();

            ConfigBuilder builder = ConfigBuilder.fromDefaults(defaultAnimal, defaultMap);

            // open configuration window
            openConfigurationWindow(builder);
        } catch (ConfigurationException e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Invalid Configuration");
            alert.setHeaderText("Error loading default configuration");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
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
//                System.out.println("Configuration loaded successfully!");

                openConfigurationWindow(builder);
            } catch (ConfigurationException | IOException e) {
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

            presenter.setStartSimulation(this::openSimulationWindow);

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
            // 1. Convert Builder to Records
            // (Assuming you don't have a builder.build() method, we construct them manually here)
            ConfigAnimal configAnimal = builder.buildAnimalConfig();
            ConfigMap configMap = builder.buildMapConfig();

            // 2. Create the Simulation
            Simulation simulation = new Simulation(configAnimal, configMap);

            // 3. Load the Simulation Window FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("simulationWindow/simulation-window.fxml")
            );
            Parent root = loader.load();

            // 4. Get the Presenter and pass the Simulation to it
            SimulationWindowPresenter presenter = loader.getController();
            presenter.setSimulation(simulation);

            // 5. Setup the Stage
            Stage stage = new Stage();
            stage.setTitle("Darwin World Simulation");
            stage.setScene(new Scene(root));

            // Ensure simulation stops when window closes
            stage.setOnCloseRequest(event -> presenter.onWindowClose());

            stage.show();

            // 6. Start the Simulation on a separate thread
            Thread simulationThread = new Thread(simulation);
            simulationThread.start();

        } catch (IOException e) {
            e.printStackTrace();
            // Ideally show an Alert here if FXML fails to load
        }
    }

    @FXML
    private void handleClose() {
        javafx.application.Platform.exit();
        System.exit(0);
    }
}
