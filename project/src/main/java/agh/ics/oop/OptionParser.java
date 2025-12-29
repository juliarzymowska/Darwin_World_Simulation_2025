package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionParser {
    public static List<MoveDirection> parse(String[] directions) {
        List<MoveDirection> moves = new ArrayList<>();

        for (String direction : directions) {
            switch (direction) {
                case "f" -> moves.add(MoveDirection.FORWARD);
                case "b" -> moves.add(MoveDirection.BACKWARD);
                case "r" -> moves.add(MoveDirection.RIGHT);
                case "l" -> moves.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(direction + " is not legal move specification");
            }
        }
        return moves;
    }
}