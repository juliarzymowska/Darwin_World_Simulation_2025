package agh.ics.oop;

import agh.ics.oop.configuration.ConfigAnimal;
import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.simulation.Simulation;

import java.util.List;

public class World {
    static void main() {
//        List<MapDirection> directions = List.of(MapDirection.NORTH, MapDirection.NORTH_WEST, MapDirection.NORTH,
//                MapDirection.SOUTH_WEST, MapDirection.WEST);
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(1, 1), new Vector2d(0, 0));
        ConfigMap configMap = new ConfigMap();
        ConfigAnimal configAnimal = new ConfigAnimal();
        Simulation simulation = new Simulation(configAnimal, configMap, 1000);
//        Simulation simulation = new Simulation(positions, map);
        simulation.run();
        /*
        List<MoveDirection> directions = OptionParser.parse(args);
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
        */
    }
}