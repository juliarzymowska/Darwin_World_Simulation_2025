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
    public void testAnimalCreation() {
        // given & when
        Animal animal = new Animal(startPosition);

        // then
        assertEquals(startPosition, animal.getCurrentPosition());
        assertEquals(config.initialEnergy(), animal.getEnergy());
        assertEquals(0, animal.getAge());
        assertTrue(animal.isAlive());
        assertEquals(0, animal.getNumberOfChildren());
        assertEquals(0, animal.getNumberOfEatenPlants());
        assertEquals(-1, animal.getDayOfDeath());
    }

    @Test
    public void testAnimalReproductionConstructor() {
        // given
        Animal father = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        Animal mother = new Animal(startPosition, 100, new Genotype(8), MapDirection.SOUTH);
        int currentDay = 5;

        // when
        Animal child = new Animal(father, mother, currentDay);

        // then
        assertEquals(startPosition, child.getCurrentPosition());
        assertEquals(2 * config.energyToReproduce(), child.getEnergy());
        assertEquals(currentDay, child.getDayOfBirth()); // dayOfBirth is set but age is still 0
        assertTrue(child.isAlive());
        assertNotNull(child.getGenotype());
    }

    @Test
    public void testEnergyGainFromEating() {
        // given
        Animal animal = new Animal(startPosition, 50, new Genotype(8), MapDirection.NORTH);
        int initialEnergy = animal.getEnergy();

        // when
        animal.gainEnergy();

        // then
        assertEquals(initialEnergy + config.energyGainedByEating(), animal.getEnergy());
        assertEquals(1, animal.getNumberOfEatenPlants());

        // when - eat again
        animal.gainEnergy();

        // then
        assertEquals(initialEnergy + 2 * config.energyGainedByEating(), animal.getEnergy());
        assertEquals(2, animal.getNumberOfEatenPlants());
    }

    @Test
    public void testEnergyLossFromMovement() {
        // given
        Animal animal = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        int initialEnergy = animal.getEnergy();
        Vector2d newPosition = new Vector2d(2, 3);

        // when
        animal.move(newPosition, MapDirection.NORTH);

        // then
        assertEquals(initialEnergy - config.energyConsumedByMove(), animal.getEnergy());
        assertEquals(newPosition, animal.getCurrentPosition());
        assertEquals(1, animal.getAge());
    }

    @Test
    public void testReproduction() {
        // given
        Animal animal = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        int initialEnergy = animal.getEnergy();
        int initialChildren = animal.getNumberOfChildren();

        // when
        animal.reproduce();

        // then
        assertEquals(initialEnergy - config.energyToReproduce(), animal.getEnergy());
        assertEquals(initialChildren + 1, animal.getNumberOfChildren());
    }

    @Test
    public void testValidateReproductionWithSufficientEnergy() {
        // given
        Animal animal = new Animal(startPosition, config.energyToReproduce(), new Genotype(8), MapDirection.NORTH);

        // when & then
        assertTrue(animal.validateReproduction());
    }

    @Test
    public void testValidateReproductionWithInsufficientEnergy() {
        // given
        Animal animal = new Animal(startPosition, config.energyToReproduce() - 1, new Genotype(8), MapDirection.NORTH);

        // when & then
        assertFalse(animal.validateReproduction());
    }

    @Test
    public void testValidateReproductionWhenDead() {
        // given
        Animal animal = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        animal.die(5);

        // when & then
        assertFalse(animal.validateReproduction());
    }

    @Test
    public void testDie() {
        // given
        Animal animal = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        int currentDay = 10;

        // when
        animal.die(currentDay);

        // then
        assertFalse(animal.isAlive());
        assertEquals(0, animal.getEnergy());
        assertEquals(currentDay, animal.getDayOfDeath());
    }

    @Test
    public void testDieWhenAlreadyDead() {
        // given
        Animal animal = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        animal.die(5);
        int firstDayOfDeath = animal.getDayOfDeath();

        // when
        animal.die(10);

        // then
        assertFalse(animal.isAlive());
        assertEquals(firstDayOfDeath, animal.getDayOfDeath()); // day of death doesn't change
    }

    @Test
    public void testUpdateAge() {
        // given
        Animal animal = new Animal(startPosition);

        // when
        animal.updateAge();

        // then
        assertEquals(1, animal.getAge());

        // when
        animal.updateAge();
        animal.updateAge();

        // then
        assertEquals(3, animal.getAge());
    }

    @Test
    public void testUpdateAgeWhenDead() {
        // given
        Animal animal = new Animal(startPosition);
        animal.die(5);
        int ageAtDeath = animal.getAge();

        // when
        animal.updateAge();

        // then
        assertEquals(ageAtDeath, animal.getAge()); // age doesn't increase when dead
    }

    @Test
    public void testCompareTo_EnergyPriority() {
        // given
        Animal higherEnergy = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        Animal lowerEnergy = new Animal(startPosition, 50, new Genotype(8), MapDirection.NORTH);

        // when & then
        assertTrue(higherEnergy.compareTo(lowerEnergy) < 0); // higher energy comes first
        assertTrue(lowerEnergy.compareTo(higherEnergy) > 0);
    }

    @Test
    public void testCompareTo_AgePriority() {
        // given
        Animal older = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        older.setCurrentAge(10);
        Animal younger = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        younger.setCurrentAge(5);

        // when & then
        assertTrue(older.compareTo(younger) < 0); // older comes first when energy is equal
        assertTrue(younger.compareTo(older) > 0);
    }

    @Test
    public void testCompareTo_ChildrenPriority() {
        // given
        Animal moreChildren = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        moreChildren.setNumberOfChildren(5);
        Animal fewerChildren = new Animal(startPosition, 100, new Genotype(8), MapDirection.NORTH);
        fewerChildren.setNumberOfChildren(2);

        // when & then
        assertTrue(moreChildren.compareTo(fewerChildren) < 0); // more children comes first
        assertTrue(fewerChildren.compareTo(moreChildren) > 0);
    }

    @Test
    public void testMultipleReproductions() {
        // given
        Animal animal = new Animal(startPosition, 200, new Genotype(8), MapDirection.NORTH);

        // when
        animal.reproduce();
        animal.reproduce();
        animal.reproduce();

        // then
        assertEquals(200 - 3 * config.energyToReproduce(), animal.getEnergy());
        assertEquals(3, animal.getNumberOfChildren());
    }

    @Test
    public void testOrientationChanges() {
        // given
        Animal animal = new Animal(startPosition, 1000, new Genotype(8), MapDirection.NORTH);

        // when
        animal.move(new Vector2d(2, 3), MapDirection.EAST);

        // then
        assertEquals(MapDirection.EAST, animal.getCurrentOrientation());

        // when
        animal.move(new Vector2d(3, 3), MapDirection.SOUTH);

        // then
        assertEquals(MapDirection.SOUTH, animal.getCurrentOrientation());
    }
}