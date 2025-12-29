package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class World {
    static void main(String[] args) {
        System.out.println("system wystartował");

        List<MoveDirection> directions = OptionParser.parse(args);
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4), new Vector2d(0, 0));
        List<Simulation> simulations = new ArrayList<>();
        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();

        for (int i = 0; i < 1200; i++) {
            AbstractWorldMap grassField = new GrassField(10);
            AbstractWorldMap rectangularMap = new RectangularMap(5, 5);

            grassField.addObserver(consoleMapDisplay);
            rectangularMap.addObserver(consoleMapDisplay);

            Simulation simulationGrassField = new Simulation(positions, directions, grassField);
            Simulation simulationRectangularMap = new Simulation(positions, directions, rectangularMap);
            simulations.add(simulationGrassField);
            simulations.add(simulationRectangularMap);
        }
        SimulationEngine simulationEngine = new SimulationEngine(simulations);
        simulationEngine.runAsyncInThreadPool();
        simulationEngine.awaitSimulationsEnd();
        System.out.println("system zakończył działanie");
    }

//    public static void run(MoveDirection[] args) {
//        System.out.println("Start");
//        for (MoveDirection move : args) {
//            String result = switch (move) {
//                case LEFT -> "skręca w lewo";
//                case RIGHT -> "skręca w prawo";
//                case FORWARD -> "idzie do przodu";
//                case BACKWARD -> "idzie do tyłu";
//            };
//            System.out.println("Zwierzak " + result);
//        }
//        System.out.println("Stop");
//    }
}