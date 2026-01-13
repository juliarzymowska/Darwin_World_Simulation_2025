package agh.ics.oop.model.map;

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
        map = new EarthMap(10, 10, 5);
    }

    @Test
    void shouldCreateMapWithCorrectBounds() {
        var bounds = map.getCurrentBounds();
        assertEquals(new Vector2d(0, 0), bounds.leftDownMapCorner());
        assertEquals(new Vector2d(9, 9), bounds.rightUpMapCorner());
    }

    @Test
    void shouldPlaceInitialPlants() {
        List<Plant> plants = map.getPlants();
        assertEquals(5, plants.size());
    }

    @Test
    void shouldPlaceAnimalAtValidPosition() {
        Animal animal = new Animal(new Vector2d(5, 5));
        animal.setCurrentEnergy(100);
        map.placeAnimal(animal);
        assertTrue(map.isOccupied(new Vector2d(5, 5)));
    }

    @Test
    void shouldNotAllowPlacingAnimalOutsideBounds() {
        Animal animal = new Animal(new Vector2d(-1, 5));
        animal.setCurrentEnergy(100);
        assertThrows(AssertionError.class, () -> map.placeAnimal(animal));
    }

    @Test
    void shouldRemoveAnimal() {
        Animal animal = new Animal(new Vector2d(5, 5));
        animal.setCurrentEnergy(100);
        map.placeAnimal(animal);
        map.removeAnimal(animal);
        assertFalse(map.isOccupied(new Vector2d(5, 5)));
    }

    @Test
    void shouldHandleRemovingNullAnimal() {
        assertDoesNotThrow(() -> map.removeAnimal(null));
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
    void shouldReturnObjectAtPosition() {
        Animal animal = new Animal(new Vector2d(5, 5));
        animal.setCurrentEnergy(100);
        map.placeAnimal(animal);
        assertNotNull(map.objectAt(new Vector2d(5, 5)));
    }

    @Test
    void shouldReturnNullForEmptyPosition() {
        assertNull(map.objectAt(new Vector2d(3, 3)));
    }

    @Test
    void shouldReturnAnimalAtPosition() {
        Animal animal = new Animal(new Vector2d(5, 5));
        map.placeAnimal(animal);
        Optional<List<Animal>> animals = map.animalAt(new Vector2d(5, 5));
        assertTrue(animals.isPresent());
        assertEquals(1, animals.get().size());
    }

    @Test
    void shouldReturnEmptyForPositionWithNoAnimals() {
        Optional<List<Animal>> animals = map.animalAt(new Vector2d(3, 3));
        assertTrue(animals.isEmpty() || animals.get().isEmpty());
    }

    @Test
    void shouldRemovePlant() {
        Plant plant = new Plant(new Vector2d(2, 2));
        map.removePlant(new Vector2d(2, 2));
        assertFalse(map.plantAt(new Vector2d(2, 2)).isPresent());
    }

    @Test
    void shouldReturnAllAnimals() {
        Animal animal1 = new Animal(new Vector2d(1, 1));
        animal1.setCurrentEnergy(100);
        Animal animal2 = new Animal(new Vector2d(2, 2));
        animal2.setCurrentEnergy(100);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        assertEquals(2, map.getAnimals().size());
    }

    @Test
    void shouldHandleMultipleAnimalsAtSamePosition() {
        Animal animal1 = new Animal(new Vector2d(5, 5));
        animal1.setCurrentEnergy(100);
        Animal animal2 = new Animal(new Vector2d(5, 5));
        animal2.setCurrentEnergy(100);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        Optional<List<Animal>> animals = map.animalAt(new Vector2d(5, 5));
        assertTrue(animals.isPresent());
        assertEquals(2, animals.get().size());
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
    void shouldHaveUniqueId() {
        EarthMap map1 = new EarthMap(10, 10, 5);
        EarthMap map2 = new EarthMap(10, 10, 5);
        assertNotEquals(map1.getId(), map2.getId());
    }

    @Test
    void shouldCreateMapWithMinimumSize() {
        EarthMap tinyMap = new EarthMap(1, 1, 0);
        var bounds = tinyMap.getCurrentBounds();
        assertEquals(new Vector2d(0, 0), bounds.leftDownMapCorner());
        assertEquals(new Vector2d(0, 0), bounds.rightUpMapCorner());
    }

    @Test
    void shouldHandleZeroInitialPlants() {
        EarthMap emptyMap = new EarthMap(10, 10, 0);
        assertEquals(0, emptyMap.getPlants().size());
    }

    @Test
    void shouldCalculatePositionOnBorderCorrectly() {
        assertEquals(new Vector2d(9, 5), map.positionOnBorder(new Vector2d(0, 5)));
        assertEquals(new Vector2d(0, 5), map.positionOnBorder(new Vector2d(9, 5)));
        assertEquals(new Vector2d(5, 0), map.positionOnBorder(new Vector2d(4, 0)));
    }
}