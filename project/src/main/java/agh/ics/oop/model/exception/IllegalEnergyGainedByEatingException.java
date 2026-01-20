package agh.ics.oop.model.exception;

public class IllegalEnergyGainedByEatingException extends Exception {
    public IllegalEnergyGainedByEatingException(int maxEnergy) {
        super("Energy gained by eating must exceed %d and be lower than max energy: %d"
                .formatted(0, maxEnergy));
    }
}
