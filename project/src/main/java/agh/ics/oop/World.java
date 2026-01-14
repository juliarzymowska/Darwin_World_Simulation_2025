package agh.ics.oop;

import agh.ics.oop.configuration.ConfigAnimal;
import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.*;
import agh.ics.oop.model.map.EarthMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.Vector2d;

import java.util.List;

public class World {
    static void main() {
//        List<MapDirection> directions = List.of(MapDirection.NORTH, MapDirection.NORTH_WEST, MapDirection.NORTH,
//                MapDirection.SOUTH_WEST, MapDirection.WEST);
//        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4), new Vector2d(0, 0));
        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
        ConfigMap configMap = new ConfigMap();
        ConfigAnimal configAnimal = new ConfigAnimal();
        WorldMap map = new EarthMap(configMap);
        map.addObserver(consoleMapDisplay);
        Simulation simulation = new Simulation(configAnimal, configMap, map);
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