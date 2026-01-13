package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.map.EarthMap;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class EarthMapTest {
    @Test
    void canMoveTo() {
        EarthMap map = new EarthMap(10, 10, 0);
        assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        assertTrue(map.canMoveTo(new Vector2d(9, 9)));
        assertFalse(map.canMoveTo(new Vector2d(10, 10)));
        assertFalse(map.canMoveTo(new Vector2d(-1, 5)));
    }

    @Test
    void positionOnBorder() {
        EarthMap map = new EarthMap(10, 10, 0);
        Vector2d pos1 = new Vector2d(2, 9);
        Vector2d pos2 = new Vector2d(4, 9);
        assertEquals(new Vector2d(7, 9), map.positionOnBorder(pos1));
        assertEquals(new Vector2d(5, 9), map.positionOnBorder(pos2));
    }

    @ParameterizedTest
    @CsvSource({
            "NORTH, SOUTH",
            "NORTH_EAST, SOUTH_EAST",
            "EAST, EAST",
            "SOUTH_EAST, NORTH_EAST",
            "SOUTH, NORTH",
            "SOUTH_WEST, NORTH_WEST",
            "WEST, WEST",
            "NORTH_WEST, SOUTH_WEST"
    })
    void directionOnBorder(MapDirection direction, MapDirection expectedDirection) {
        EarthMap map = new EarthMap(10, 10, 0);
        assertEquals(expectedDirection, map.directionOnBorder(direction));
    }


    @Test
    void animalPlacementAndRemoval() {
        EarthMap map = new EarthMap(10, 10, 0);
        Animal animal1 = new Animal(new Vector2d(2, 2));

        map.placeAnimal(animal1);
        assertTrue(map.isOccupied(new Vector2d(2, 2)));

        map.removeAnimal(animal1);
        assertFalse(map.isOccupied(new Vector2d(2, 2)));
    }

}
