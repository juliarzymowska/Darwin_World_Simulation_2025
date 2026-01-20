package agh.ics.oop.model.exception;

public class IllegalInitialEnergyException extends Exception {
    public IllegalInitialEnergyException(int maxEnergy) {
        super("Initial energy must exceed %d and  be lower than max energy: %d"
                .formatted(0, maxEnergy));
    }
}
