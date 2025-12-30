package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.EarthMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class World {
    static void main(String[] args) {
       List<MoveDirection> directions = OptionParser.parse(args);
       List<Vector2d> positions = new ArrayList<>();
       positions.add(new Vector2d(2,2));
       positions.add(new Vector2d(3,4));
       ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
       WorldMap map = new EarthMap(10, 10, 10);
       map.addObserver(consoleMapDisplay);
       Simulation sim = new Simulation(positions, directions, map);
       sim.run();
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