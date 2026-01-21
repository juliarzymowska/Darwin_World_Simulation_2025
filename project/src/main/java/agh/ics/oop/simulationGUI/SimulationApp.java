package agh.ics.oop.simulationGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("startWindow/start-window.fxml"));
        BorderPane root = loader.load();
        configureStage(primaryStage, root);
        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darwin World Simulation app");
    }

    @Override
    public void stop() throws Exception {
        // This method is called when the application is stopped
        System.out.println("Shutting down application...");

        // Force kill all background threads (simulations)
        // Without this, your IntelliJ might show the app as still "Running" after you close the window
        System.exit(0);
    }
}