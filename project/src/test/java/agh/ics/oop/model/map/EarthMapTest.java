package agh.ics.oop.model.map;

import agh.ics.oop.configuration.ConfigMap;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EarthMapTest {
    private EarthMap map;

    @BeforeEach
    void setUp() {
        map = new EarthMap(new ConfigMap(10,10,5, 0, MapType.EARTH_MAP,0, 0, 0));
    }

    @Test
    void shouldMoveAnimalForward() {
        Genotype genotype = new Genotype(List.of(0, 0, 0)); // genes pointing forward (NORTH)
        Animal animal = new Animal(new Vector2d(5, 5), 100, genotype, MapDirection.NORTH);
        animal.setCurrentEnergy(100);
        map.placeAnimal(animal);
        map.moveTo(animal);
        assertEquals(new Vector2d(5, 6), animal.getCurrentPosition());
    }

    @Test
    void shouldWrapAnimalAroundLeftEdge() {
        Genotype genotype = new Genotype(List.of(0, 0, 0)); // genes pointing forward (WEST)
        Animal animal = new Animal(new Vector2d(0, 5), 100, genotype, MapDirection.WEST);
        map.placeAnimal(animal);
        map.moveTo(animal);
        assertEquals(new Vector2d(9, 5), animal.getCurrentPosition());
    }

    @Test
    void shouldWrapAnimalAroundRightEdge() {
        Genotype genotype = new Genotype(List.of(0, 0, 0)); // genes pointing forward (EAST)
        Animal animal = new Animal(new Vector2d(9, 5), 100, genotype, MapDirection.EAST);
        map.placeAnimal(animal);
        map.moveTo(animal);
        assertEquals(new Vector2d(0, 5), animal.getCurrentPosition());
    }

    @Test
    void shouldBounceAnimalOffNorthernPole() {
        Genotype genotype = new Genotype(List.of(0, 0, 0)); // genes pointing forward (NORTH)
        Animal animal = new Animal(new Vector2d(5, 9), 100, genotype, MapDirection.NORTH);
        map.placeAnimal(animal);
        map.moveTo(animal);
        assertEquals(MapDirection.SOUTH, animal.getCurrentOrientation());
    }

    @Test
    void shouldBounceAnimalOffSouthernPole() {
        Genotype genotype = new Genotype(List.of(0, 0, 0)); // genes pointing forward (SOUTH)
        Animal animal = new Animal(new Vector2d(5, 0), 100, genotype, MapDirection.SOUTH);
        map.placeAnimal(animal);
        map.moveTo(animal);
        assertEquals(MapDirection.NORTH, animal.getCurrentOrientation());
    }

    @Test
    void shouldBounceAndFlipPositionAtNorthEastCorner() {
        Genotype genotype = new Genotype(List.of(0, 0, 0)); // genes pointing forward (NORTH_EAST)
        Animal animal = new Animal(new Vector2d(9, 9), 100, genotype, MapDirection.NORTH_EAST);
        map.placeAnimal(animal);
        map.moveTo(animal);
        assertEquals(new Vector2d(0, 9), animal.getCurrentPosition());
        assertEquals(MapDirection.SOUTH_EAST, animal.getCurrentOrientation());
    }

    @Test
    void shouldBounceAndFlipPositionAtSouthWestCorner() {
        Genotype genotype = new Genotype(List.of(0, 0, 0)); // genes pointing forward (SOUTH_WEST)
        Animal animal = new Animal(new Vector2d(0, 0), 100, genotype, MapDirection.SOUTH_WEST);
        map.placeAnimal(animal);
        map.moveTo(animal);
        assertEquals(new Vector2d(9, 0), animal.getCurrentPosition());
        assertEquals(MapDirection.NORTH_WEST, animal.getCurrentOrientation());
    }


    @Test
    void shouldValidateCanMoveTo() {
        assertTrue(map.canMoveTo(new Vector2d(5, 5)));
        assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        assertTrue(map.canMoveTo(new Vector2d(9, 9)));
        assertFalse(map.canMoveTo(new Vector2d(-1, 5)));
        assertFalse(map.canMoveTo(new Vector2d(10, 5)));
        assertFalse(map.canMoveTo(new Vector2d(5, 10)));
    }


    @Test
    void shouldCalculatePositionOnBorderCorrectly() {
        assertEquals(new Vector2d(9, 5), map.positionOnBorder(new Vector2d(0, 5)));
        assertEquals(new Vector2d(0, 5), map.positionOnBorder(new Vector2d(9, 5)));
        assertEquals(new Vector2d(5, 0), map.positionOnBorder(new Vector2d(4, 0)));
    }
}