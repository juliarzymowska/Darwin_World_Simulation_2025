package agh.ics.oop.model.elements;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

/*
 * Klasa reprezentująca zwierzę na mapie.
 * Zwierzę posiada orientację, pozycję oraz statystyki takie jak energia, wiek, liczba dzieci i liczba zjedzonych roślin.
 * Zwierzę może się poruszać na mapie zgodnie z określonymi zasadami walidacji ruchu.
 * TODO: przenieść statystyki do osobnej klasy AnimalStats
 * */
public class Animal implements WorldElement {
    private MapDirection currentOrientation;
    private Vector2d currentPosition;

    private int currentEnergy, currentAge = 0, numberOfChildren = 0, numberOfEatenPlants = 0,
            numberOfDescendants = 0, dayOfBirth = 0, dayOfDeath = -1;
    protected boolean isAlive = true;
    private final Genotype genotype;
    private int genotypeLength; // apply from config
    private List<Animal> parents = null;


    /*
     * Constructor of Animal class.
     * For animals to be placed on map at the beginning of simulation.
     * @param position - initial position of animal on map
     * */
    public Animal(Vector2d position) {
        this.currentPosition = position;
        this.genotypeLength = 3;
        this.genotype = new Genotype(genotypeLength);
        this.currentEnergy = 100; // default energy, apply from config
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
        this.parents = List.of(father, mother);
        this.genotype = new Genotype(father, mother);
        this.currentOrientation = MapDirection.getRandomDirection();
        this.currentEnergy = 2 * 50; // energy from parents, apply reproduction cost from config
        this.dayOfBirth = currentDay;
    }

    /*
     * Gettery
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

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public int getCurrentAge() {
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
     * Settery
     * */
    public int setCurrentEnergy(int energy) {
        this.currentEnergy = energy;
        return this.currentEnergy;
    }

    public int setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
        return this.numberOfChildren;
    }

    public int setNumberOfEatenPlants(int numberOfEatenPlants) {
        this.numberOfEatenPlants = numberOfEatenPlants;
        return this.numberOfEatenPlants;
    }

    public int setDayOfDeath(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
        return this.dayOfDeath;
    }

    public int setNumberOfDescendants(int numberOfDescendants) {
        this.numberOfDescendants = numberOfDescendants;
        return this.numberOfDescendants;
    }

    public int setCurrentAge(int currentAge) {
        this.currentAge = currentAge;
        return this.currentAge;
    }

    public void decreaseEnergy(int amount) {
        this.currentEnergy -= amount;
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