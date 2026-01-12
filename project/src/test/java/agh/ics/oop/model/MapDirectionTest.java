package agh.ics.oop.model;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class MapDirectionTest {
    @ParameterizedTest
    @CsvSource({
            "NORTH, N",
            "NORTH_EAST, NE",
            "EAST, E",
            "SOUTH_EAST, SE",
            "SOUTH, S",
            "SOUTH_WEST, SW",
            "WEST, W",
            "NORTH_WEST, NW"
    })
    void toString(MapDirection direction, String expected) {
        String result = direction.toString();
        assertEquals(expected, result);
    }
    @ParameterizedTest
    @CsvSource({
            "NORTH, 0,1",
            "NORTH_EAST, 1,1",
            "EAST, 1,0",
            "SOUTH_EAST, 1,-1",
            "SOUTH, 0,-1",
            "SOUTH_WEST, -1,-1",
            "WEST, -1,0",
            "NORTH_WEST, -1,1"
    })
    void toUnit(MapDirection direction, int x, int y) {
        Vector2d expected = new Vector2d(x,y);
        Vector2d result = direction.toUnitVector();
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "NORTH, 1, NORTH_EAST",
            "WEST, 2, NORTH",
            "SOUTH, 4, NORTH",
            "WEST, 0, WEST",
            "SOUTH_WEST, 5, EAST",
            "NORTH_WEST, 7, WEST"
    })
    void turn(MapDirection start, int turns, MapDirection expected) {
        assertEquals(expected, start.turn(turns));
    }

    @Test
    void randomDirectionIsValid() {
        MapDirection direction = MapDirection.NORTH.getRandomDirection();
        assertNotNull(direction);
        assertInstanceOf(MapDirection.class, direction);
    }
}
