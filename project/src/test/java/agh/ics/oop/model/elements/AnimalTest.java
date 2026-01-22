package agh.ics.oop.model.elements;

import agh.ics.oop.configuration.ConfigAnimal;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    private ConfigAnimal config;
    private Vector2d startPosition;

    @BeforeEach
    public void setUp() {
        config = new ConfigAnimal();
        startPosition = new Vector2d(2, 2);
    }

    @Test
    public void animalCreation() {
        Animal animal = new Animal(startPosition, config);

        assertEquals(startPosition, animal.getCurrentPosition());
        assertEquals(config.initialEnergy(), animal.getEnergy());
        assertEquals(0, animal.getAge());
        assertTrue(animal.isAlive());
        assertEquals(0, animal.getNumberOfChildren());
    }

    @Test
    public void animalReproductionConstructor() {
        Animal father = new Animal(startPosition, config);
        Animal mother = new Animal(startPosition, config);
        int currentDay = 5;

        Animal child = new Animal(father, mother, currentDay);

        assertEquals(startPosition, child.getCurrentPosition());
        assertEquals(2 * config.energyToReproduce(), child.getEnergy());
        assertEquals(currentDay, child.getDayOfBirth());
        assertEquals(father, child.getFather());
        assertEquals(mother, child.getMother());
    }

    @Test
    public void energyGainFromEating() {
        Animal animal = new Animal(startPosition, config);
        animal.setCurrentEnergy(50);
        int initialEnergy = animal.getEnergy();

        animal.gainEnergy();

        assertEquals(initialEnergy + config.energyGainedByEating(), animal.getEnergy());
        assertEquals(1, animal.getNumberOfEatenPlants());
    }

    @Test
    public void energyLossFromMovement() {
        Animal animal = new Animal(startPosition, config);
        int initialEnergy = animal.getEnergy();
        Vector2d newPosition = new Vector2d(2, 3);

        animal.move(newPosition, MapDirection.NORTH);

        assertEquals(initialEnergy - config.energyConsumedByMove(), animal.getEnergy());
        assertEquals(newPosition, animal.getCurrentPosition());
        assertEquals(1, animal.getAge());
    }

    @Test
    public void validateReproduction() {
        Animal animal = new Animal(startPosition, config);

        animal.setCurrentEnergy(config.energyToReproduce());
        assertTrue(animal.validateReproduction());

        animal.setCurrentEnergy(config.energyToReproduce() - 1);
        assertFalse(animal.validateReproduction());

        animal.setCurrentEnergy(100);
        animal.die(1);
        assertFalse(animal.validateReproduction());
    }

    @Test
    public void compareTo() {
        Animal strong = new Animal(startPosition, config);
        strong.setCurrentEnergy(150);

        Animal weak = new Animal(startPosition, config);
        weak.setCurrentEnergy(50);

        assertTrue(strong.compareTo(weak) < 0);

        weak.setCurrentEnergy(150);
        strong.setCurrentAge(10);
        weak.setCurrentAge(5);
        assertTrue(strong.compareTo(weak) < 0);
    }

    @Test
    public void energyGainNotExceedingMax() {
        Animal animal = new Animal(startPosition, config);
        animal.setCurrentEnergy(config.maxEnergy() - 5);

        animal.gainEnergy();

        assertEquals(config.maxEnergy(), animal.getEnergy(), "Energy should not exceed maxEnergy after eating");
    }

    @Test
    public void familyRelations() {
        Animal grandfather = new Animal(startPosition, config);
        Animal grandmother = new Animal(startPosition, config);
        Animal father = new Animal(grandfather, grandmother, 1);
        Animal mother = new Animal(startPosition, config);

        Animal child = new Animal(father, mother, 2);

        assertEquals(father, child.getFather());
        assertEquals(mother, child.getMother());
        assertEquals(grandfather, child.getFather().getFather());
    }

    @Test
    public void compareToIdenticalStats() {
        Animal a1 = new Animal(startPosition, config);
        Animal a2 = new Animal(startPosition, config);

        int result = a1.compareTo(a2);

        assertNotEquals(0, result, "Same energy and age should not result in equality due to unique IDs");
    }

}