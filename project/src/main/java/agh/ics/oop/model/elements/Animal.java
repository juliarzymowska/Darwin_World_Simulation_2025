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

    private int currentEnergy, currentAge = 0, numberOfChildren = 0, numberOfEatenPlants = 0, dayOfDeath = -1,
            numberOfDescendants = 0;
    private List<Integer> genotype = new ArrayList<>(); // TODO: make RandomGenotypeGenerator
    private int genotypeLength;
    private List<Animal> parents = null;


    /*
     * Konstruktory klasy Animal.
     * */
    public Animal(Vector2d position) {
        this.currentPosition = position;
        this.genotype = List.of(0, 2); // default genotype
        this.genotypeLength = this.genotype.size();
        this.currentEnergy = 100; // default energy
        this.currentOrientation = MapDirection.getRandomDirection();
    }

    public Animal(Vector2d position, int energy) {
        this.currentPosition = position;
        this.currentEnergy = energy;
        this.currentOrientation = MapDirection.getRandomDirection();
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

    public List<Integer> getGenotype() {
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