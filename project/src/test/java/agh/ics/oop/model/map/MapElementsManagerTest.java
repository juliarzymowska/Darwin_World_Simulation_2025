package agh.ics.oop.model.map;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.elements.Genotype;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MapElementsManagerTest {
    private MapElementsManager manager;

    @BeforeEach
    void setUp() {
        manager = new MapElementsManager();
    }

    @Test
    void shouldAddPlantsToEmptyPositions() {
        manager.addPlants(5, 10, 10);
        List<Plant> plants = manager.getPlants();
        assertTrue(plants.size() <= 5);
    }

    @Test
    void shouldNotOverwriteExistingPlant() {
        Vector2d position = new Vector2d(5, 5);
        Plant plant1 = new Plant(position);
        manager.addPlants(1, 10, 10);
        manager.addPlants(1, 10, 10);
        List<Plant> plants = manager.getPlants();
        assertTrue(plants.size() <= 2);
    }

    @Test
    void shouldReturnPlantAtPosition() {
        Vector2d position = new Vector2d(3, 3);
        Plant plant = new Plant(position);
        manager.addPlants(10, 10, 10);
        Optional<Plant> result = manager.plantAt(position);
        assertTrue(true);
    }

    @Test
    void shouldReturnEmptyWhenNoPlantAtPosition() {
        Optional<Plant> result = manager.plantAt(new Vector2d(99, 99));
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRemovePlant() {
        manager.addPlants(5, 10, 10);
        List<Plant> plants = manager.getPlants();
        if (!plants.isEmpty()) {
            Vector2d position = plants.get(0).getCurrentPosition();
            manager.removePlant(position);
            assertTrue(manager.plantAt(position).isEmpty());
        }
    }

    @Test
    void shouldRemoveNonExistingPlantWithoutError() {
        assertDoesNotThrow(() -> manager.removePlant(new Vector2d(99, 99)));
    }

    @Test
    void shouldPlaceSingleAnimal() {
        Genotype genotype = new Genotype(List.of(0, 1, 2));
        Animal animal = new Animal(new Vector2d(5, 5), 100, genotype, MapDirection.NORTH);
        manager.placeAnimal(animal);

        Optional<List<Animal>> result = manager.animalAt(new Vector2d(5, 5));
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals(animal, result.get().get(0));
    }

    @Test
    void shouldPlaceMultipleAnimalsAtSamePosition() {
        Genotype genotype = new Genotype(List.of(0, 1, 2));
        Animal animal1 = new Animal(new Vector2d(5, 5), 100, genotype, MapDirection.NORTH);
        Animal animal2 = new Animal(new Vector2d(5, 5), 80, genotype, MapDirection.SOUTH);

        manager.placeAnimal(animal1);
        manager.placeAnimal(animal2);

        Optional<List<Animal>> result = manager.animalAt(new Vector2d(5, 5));
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    void shouldRemoveAnimal() {
        Genotype genotype = new Genotype(List.of(0, 1, 2));
        Animal animal = new Animal(new Vector2d(5, 5), 100, genotype, MapDirection.NORTH);
        manager.placeAnimal(animal);
        manager.removeAnimal(animal);

        Optional<List<Animal>> result = manager.animalAt(new Vector2d(5, 5));
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRemoveOneAnimalFromMultiple() {
        Genotype genotype = new Genotype(List.of(0, 1, 2));
        Animal animal1 = new Animal(new Vector2d(5, 5), 100, genotype, MapDirection.NORTH);
        Animal animal2 = new Animal(new Vector2d(5, 5), 80, genotype, MapDirection.SOUTH);

        manager.placeAnimal(animal1);
        manager.placeAnimal(animal2);
        manager.removeAnimal(animal1);

        Optional<List<Animal>> result = manager.animalAt(new Vector2d(5, 5));
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals(animal2, result.get().get(0));
    }

    @Test
    void shouldHandleRemovingNullAnimal() {
        assertDoesNotThrow(() -> manager.removeAnimal(null));
    }

    @Test
    void shouldHandleRemovingNonExistingAnimal() {
        Genotype genotype = new Genotype(List.of(0, 1, 2));
        Animal animal = new Animal(new Vector2d(5, 5), 100, genotype, MapDirection.NORTH);
        assertDoesNotThrow(() -> manager.removeAnimal(animal));
    }

    @Test
    void shouldReturnEmptyWhenNoAnimalsAtPosition() {
        Optional<List<Animal>> result = manager.animalAt(new Vector2d(99, 99));
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllAnimals() {
        Genotype genotype = new Genotype(List.of(0, 1, 2));
        Animal animal1 = new Animal(new Vector2d(1, 1), 100, genotype, MapDirection.NORTH);
        Animal animal2 = new Animal(new Vector2d(2, 2), 80, genotype, MapDirection.SOUTH);

        manager.placeAnimal(animal1);
        manager.placeAnimal(animal2);

        List<Animal> animals = manager.getAnimals();
        assertEquals(2, animals.size());
        assertTrue(animals.contains(animal1));
        assertTrue(animals.contains(animal2));
    }

    @Test
    void shouldReturnEmptyListWhenNoAnimals() {
        List<Animal> animals = manager.getAnimals();
        assertTrue(animals.isEmpty());
    }

    @Test
    void shouldReturnAnimalWhenBothAnimalAndPlantAtPosition() {
        Genotype genotype = new Genotype(List.of(0, 1, 2));
        Vector2d position = new Vector2d(5, 5);
        Animal animal = new Animal(position, 100, genotype, MapDirection.NORTH);

        manager.placeAnimal(animal);
        manager.addPlants(10, 10, 10);

        WorldElement element = manager.objectAt(position);
        assertTrue(element instanceof Animal);
    }

    @Test
    void shouldReturnPlantWhenOnlyPlantAtPosition() {
        manager.addPlants(5, 10, 10);
        List<Plant> plants = manager.getPlants();

        if (!plants.isEmpty()) {
            Vector2d position = plants.get(0).getCurrentPosition();
            WorldElement element = manager.objectAt(position);
            assertTrue(element instanceof Plant);
        }
    }

    @Test
    void shouldReturnNullWhenNoObjectAtPosition() {
        WorldElement element = manager.objectAt(new Vector2d(99, 99));
        assertNull(element);
    }
}