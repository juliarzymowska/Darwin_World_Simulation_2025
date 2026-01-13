package agh.ics.oop.model;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.Plant;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.map.EarthMap;
import agh.ics.oop.model.map.MapElementsManager;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ElementsManagerTest {

    private MapElementsManager manager;

    @BeforeEach
    void setUp() {
        manager = new MapElementsManager();
    }

    @Test
    void addPlantsAddCorrectNumberOfPlants() {
        manager.addPlants(10, 5, 5);

        assertEquals(10, manager.getPlants().size());
    }

    @Test
    void addPlantsPlacePlantsWithinBounds() {
        int maxX = 3;
        int maxY = 3;
        manager.addPlants(9, maxX, maxY);

        for (Plant plant : manager.getPlants()) {
            Vector2d pos = plant.getCurrentPosition();
            assertTrue(pos.getX() >= 0 && pos.getX() <= maxX);
            assertTrue(pos.getY() >= 0 && pos.getY() <= maxY);
        }
    }

    @Test
    void plantAtReturnPlantWhenPositionIsOccupied() {
        Vector2d position = new Vector2d(1, 1);
        manager.addPlants(4, 2, 2);
        Optional<Plant> result = manager.plantAt(position);
        assertTrue(result.isPresent());
        }

    @Test
    void plantAtReturnEmptyOptionalWhenNoPlantPresent() {
        Vector2d emptyPosition = new Vector2d(10, 10);
        Optional<Plant> result = manager.plantAt(emptyPosition);
        assertTrue(result.isEmpty());
    }

    @Test
    void plantAtShouldReturnEmptyAfterPlantIsRemoved() {
        Vector2d position = new Vector2d(1, 1);
        manager.addPlants(1, 1, 1);
        manager.removePlant(position);
        Optional<Plant> result = manager.plantAt(position);
        assertTrue(result.isEmpty());
    }

    @Test
    void animalAtForOneAnimal(){
        Vector2d position = new Vector2d(2, 2);
        Animal animal = new Animal(position);
        manager.placeAnimal(animal);

        Optional<List<Animal>> result = manager.animalAt(position);

        assertTrue(result.isPresent());
        assertTrue(result.get().contains(animal));
        assertEquals(1, result.get().size());
    }

    @Test
    void animalAtForManyAnimal(){
        Vector2d position = new Vector2d(2, 2);
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        manager.placeAnimal(animal1);
        manager.placeAnimal(animal2);

        Optional<List<Animal>> result = manager.animalAt(position);

        assertTrue(result.isPresent());
        assertTrue(result.get().contains(animal1) && result.get().contains(animal2));
        assertEquals(2, result.get().size());
    }

    @Test
    void animalAtReturnEmptyListAfterRemovingAnimal() {
        EarthMap map = new EarthMap(10, 10, 0);
        Vector2d pos = new Vector2d(0, 0);
        Animal animal = new Animal(pos);
        map.placeAnimal(animal);

        map.removeAnimal(animal);

        assertTrue(map.animalAt(pos).isEmpty());
    }


    @Test
    void shouldPlaceMultipleAnimalsOnSamePosition() {
        Vector2d pos = new Vector2d(1, 1);
        Animal animal1 = new Animal(pos);
        Animal animal2 = new Animal(pos);

        manager.placeAnimal(animal1);
        manager.placeAnimal(animal2);

        Optional<List<Animal>> result = manager.animalAt(pos);

        assertTrue(result.isPresent());

        List<Animal> animalList = result.get();
        assertEquals(2, animalList.size());
        assertTrue(animalList.contains(animal1));
        assertTrue(animalList.contains(animal2));
    }

    @Test
    void removeOnlyDeadAnimals() {
        Vector2d pos1 = new Vector2d(1, 1);
        Vector2d pos2 = new Vector2d(2, 2);

        Animal deadAnimal = new Animal(pos1);
        Animal aliveAnimal = new Animal(pos2);

        deadAnimal.setCurrentEnergy(0);
        aliveAnimal.setCurrentEnergy(10);

        manager.placeAnimal(deadAnimal);
        manager.placeAnimal(aliveAnimal);

        manager.deleteDeadAnimals();

        assertTrue(manager.animalAt(pos1).isEmpty());
        Optional<List<Animal>> aliveList = manager.animalAt(pos2);
        assertTrue(aliveList.isPresent());
        assertEquals(1, aliveList.get().size());
        assertEquals(aliveAnimal, aliveList.get().getFirst());
    }

    @Test
    void removeKeyFromMapWhenLastAnimalIsRemoved() {
        Vector2d pos = new Vector2d(3, 3);
        Animal animal = new Animal(pos);
        manager.placeAnimal(animal);

        manager.removeAnimal(animal);

        Optional<List<Animal>> result = manager.animalAt(pos);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRemoveAnimalAndKeepOthersOnSamePosition() {
        Vector2d pos = new Vector2d(1, 1);
        Animal a1 = new Animal(pos);
        Animal a2 = new Animal(pos);
        manager.placeAnimal(a1);
        manager.placeAnimal(a2);

        manager.removeAnimal(a1);

        Optional<List<Animal>> result = manager.animalAt(pos);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertFalse(result.get().contains(a1));
        assertTrue(result.get().contains(a2));
    }

    @Test
    void returnPlantWhenNoAnimalsArePresent() {;
        Vector2d pos = new Vector2d(1, 1);
        manager.addPlants(6,2,2);

        WorldElement result = manager.objectAt(pos);

        assertNotNull(result);
        assertInstanceOf(Plant.class, result);
    }

    @Test
    void prioritizeAnimalOverPlant() {
        Vector2d pos = new Vector2d(1, 1);
        manager.addPlants(4,2,2);
        Animal animal = new Animal(pos);

        manager.placeAnimal(animal);

        WorldElement result = manager.objectAt(pos);

        assertInstanceOf(Animal.class, result);
        assertSame(animal, result);
    }

    @Test
    void shouldReturnNullWhenPositionIsEmpty() {
        Vector2d emptyPos = new Vector2d(10, 10);

        WorldElement result = manager.objectAt(emptyPos);

        assertNull(result);
    }
}
