package agh.ics.oop.model.elements;

import agh.ics.oop.model.util.Vector2d;

/*
 * Class representing a feromon in the world.
 * Feromons have a position, a value indicating their strength, and a day counter.
 * They can increase or decrease their value and day count.
 * Feromons can be compared based on their value.
 * */
public class Feromon implements WorldElement, Comparable<Feromon> {
    private final Vector2d feromonPosition;
    private int feromonValue;
    private int feromonDay;

    public Feromon(Vector2d feromonPosition) {
        this.feromonPosition = feromonPosition;
        this.feromonValue = 1;
        this.feromonDay = 1;
    }

//    @Override
//    public String toString() {
//        return "~";
//    }

    @Override
    public Vector2d getCurrentPosition() {
        return feromonPosition;
    }

    public void decreseFeromonValue() {
        this.feromonValue = feromonValue - 1;
    }

    public void increaseFeromonValue() {
        this.feromonValue = feromonValue + 1;
    }

    public void increaseFeromonDay() {
        this.feromonDay = feromonDay + 1;
    }

    public int getFeromonDay() {
        return feromonDay;
    }

    public int getFeromonValue() {
        return feromonValue;
    }

    @Override
    public int compareTo(Feromon other) {
        return Integer.compare(this.feromonValue, other.feromonValue);
    }
}
