package agh.ics.oop.simulationGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/*
 * Class for launching the JavaFX application
 * */
public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("startWindow/start-window.fxml"));
        BorderPane root = loader.load();
        configureStage(primaryStage, root);
        primaryStage.show();
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/startWindow/icon.png")));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Icon not found.");
        }
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin World Simulation app");
    }

    @Override
    public void stop() {
        // Force kill all background threads (simulations)
        System.exit(0);
    }
}