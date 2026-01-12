package agh.ics.oop.model.elements;

import agh.ics.oop.configuration.ConfigAnimal;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

/*
 * Class representing an animal in the simulation.
 * Contains information about the animal's position, orientation, energy, age, genotype, and other statistics.
 * */
public class Animal implements WorldElement {
    private MapDirection currentOrientation;
    private Vector2d currentPosition;
    private final ConfigAnimal config = new ConfigAnimal();

    private int currentEnergy, currentAge = 0, numberOfChildren = 0, numberOfEatenPlants = 0,
            numberOfDescendants = 0, dayOfBirth = 0, dayOfDeath = -1;
    protected boolean isAlive = true;
    private final Genotype genotype;


    /*
     * Constructor of Animal class.
     * For animals to be placed on map at the beginning of simulation.
     * @param position - initial position of animal on map
     * */
    public Animal(Vector2d position) {
        this.currentPosition = position;
        this.genotype = new Genotype(config.genotypeLength());
        this.currentEnergy = config.initialEnergy();
        this.currentOrientation = MapDirection.getRandomDirection();
    }

    /*
     * Constructor of Animal class.
     * For animals to be placed on map via reproduction.
     * @param position - initial position of animal on map
     * @param energy - initial energy of animal
     * */
    public Animal(Animal father, Animal mother, int currentDay) {
        this.currentPosition = new Vector2d(father.getCurrentPosition().getX(), father.getCurrentPosition().getY());
        this.genotype = new Genotype(father, mother);
        this.currentOrientation = MapDirection.getRandomDirection();
        this.currentEnergy = 2 * config.energyToReproduce(); // energy from parents, apply reproduction cost from config
        this.dayOfBirth = currentDay;
    }

    /*
     * Getters
     * */
    public MapDirection getCurrentOrientation() {
        return currentOrientation;
    }

    public Vector2d getCurrentPosition() {
        return currentPosition;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public int getEnergy() {
        return currentEnergy;
    }

    public int getAge() {
        return currentAge;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public int getNumberOfEatenPlants() {
        return numberOfEatenPlants;
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    public int getNumberOfDescendants() {
        return numberOfDescendants;
    }

    /*
     * Setters
     * */
    public void setCurrentEnergy(int energy) {
        this.currentEnergy = energy;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public void setNumberOfEatenPlants(int numberOfEatenPlants) {
        this.numberOfEatenPlants = numberOfEatenPlants;
    }

    public void setDayOfDeath(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    public void setNumberOfDescendants(int numberOfDescendants) {
        this.numberOfDescendants = numberOfDescendants;
    }

    public void setCurrentAge(int currentAge) {
        this.currentAge = currentAge;
    }

    // for movement
    public void decreaseEnergy() {
        this.currentEnergy -= config.energyConsumedByMove();
    }

    @Override
    public String toString() {
        return switch (currentOrientation) {
            case NORTH -> "⭡";
            case NORTH_EAST -> "↗";
            case EAST -> "⭢";
            case SOUTH_EAST -> "↘";
            case SOUTH -> "⭣";
            case SOUTH_WEST -> "↙";
            case WEST -> "⭠";
            case NORTH_WEST -> "↖";
        };
    }

    public boolean isAt(Vector2d position) {
        return currentPosition.equals(position);
    }

    public void move(Vector2d newPosition, MapDirection newOrientation) {
        currentPosition = newPosition;
        currentOrientation = newOrientation;
    }

}