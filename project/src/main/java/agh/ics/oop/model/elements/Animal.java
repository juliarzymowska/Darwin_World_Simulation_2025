package agh.ics.oop.model.elements;

import agh.ics.oop.configuration.ConfigAnimal;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

/*
 * Class representing an animal in the simulation.
 * Contains information about the animal's position, orientation, energy, age, genotype, and other statistics.
 * */
public class Animal implements WorldElement, Comparable<Animal> {
    private MapDirection currentOrientation;
    private Vector2d currentPosition;
    protected final static ConfigAnimal config = new ConfigAnimal(); // for testing purposes, later should be passed from outside

    private int currentEnergy, currentAge = 0, numberOfChildren = 0, numberOfEatenPlants = 0,
            numberOfDescendants = 0, dayOfBirth = 0, dayOfDeath = -1;
    private boolean isAlive = true;
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
        this.genotype = new Genotype(father, mother, config.minMutations(), config.maxMutations());
        this.currentOrientation = MapDirection.getRandomDirection();
        this.currentEnergy = 2 * config.energyToReproduce(); // energy from parents, apply reproduction cost from config
        this.dayOfBirth = currentDay;
    }

    /*
     * (for tests)
     * Constructor of Animal class.
     * @param position - initial position of animal on map
     * @param energy - initial energy of animal
     * @param genotype - genotype of animal
     * */
    public Animal(Vector2d position, int energy, Genotype genotype, MapDirection orientation) {
        this.currentPosition = position;
        this.genotype = genotype;
        this.currentEnergy = energy;
        this.currentOrientation = orientation;
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

    public boolean isAlive() {
        return isAlive;
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

    public int getDayOfBirth() {
        return dayOfBirth;
    }

    public int getNumberOfDescendants() {
        return numberOfDescendants;
    }

    /*
     * Setters (mostly for testing)
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

    public void setNumberOfDescendants(int numberOfDescendants) {
        this.numberOfDescendants = numberOfDescendants;
    }

    public void setCurrentAge(int currentAge) {
        this.currentAge = currentAge;
    }

    public void setOrientation(MapDirection mapDirection) {
        this.currentOrientation = mapDirection;
    }

    // visual representation of the animal based on its orientation
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
// not needed now
//    public boolean isAt(Vector2d position) {
//        return currentPosition.equals(position);
//    }
    /*
     * Methods
     * */

    public void updateAge() {
        if (isAlive) {
            this.currentAge += 1;
        }
    }

    // for movement
    private void decreaseEnergy() {
        // uważam, że najniższa możliwa energia powinna wynosić 0
        this.currentEnergy = max(0, currentEnergy - config.energyConsumedByMove());
    }

    // for reproduction
    public void reproduce() {
        this.currentEnergy -= config.energyToReproduce();
        this.numberOfChildren += 1;
//        this.children.add(); // TODO later for statistics
    }

    // check if animal can reproduce, used in MapElementsManager
    public boolean validateReproduction() {
        return this.isAlive && this.currentEnergy >= config.energyToReproduce();
    }

    // for reproduction
    public void reproduce() {
        this.currentEnergy -= config.energyToReproduce();
        this.numberOfChildren += 1;
//        this.children.add(); // TODO later for statistics
    }

    // check if animal can reproduce, used in MapElementsManager
    public boolean validateReproduction() {
        return this.isAlive && this.currentEnergy >= config.energyToReproduce();
    }

    // for eating
    public void gainEnergy() {
        this.currentEnergy += config.energyGainedByEating();
        numberOfEatenPlants += 1;
    }

    public void die(int currentDay) {
        if (!isAlive)
            return; // already dead
        this.isAlive = false;
        this.currentEnergy = 0;
        this.dayOfDeath = currentDay;
    }

    public void move(Vector2d position, MapDirection orientation) {
        updateAge(); // increase age on each move
        decreaseEnergy();

        this.currentOrientation = orientation;
        this.currentPosition = position;
        genotype.moveToNextGene();
    }

    @Override
    public int compareTo(Animal other) {
        // 1. Higher energy has priority
        int energyComparison = Integer.compare(other.currentEnergy, this.currentEnergy);
        if (energyComparison != 0) {
            return energyComparison;
        }

        // 2. Older animals have priority
        int ageComparison = Integer.compare(other.currentAge, this.currentAge);
        if (ageComparison != 0) {
            return ageComparison;
        }

        // 3. Animals with more children have priority
        int childrenComparison = Integer.compare(other.numberOfChildren, this.numberOfChildren);
        if (childrenComparison != 0) {
            return childrenComparison;
        }

        // 4. Random tie-breaking (using hash codes for pseudo-randomness)
        return Integer.compare(System.identityHashCode(other), System.identityHashCode(this));
    }
}