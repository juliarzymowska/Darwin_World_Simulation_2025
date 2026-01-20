package agh.ics.oop.model.exception;

public class IllegalEnergyConsumedByMoveException extends Exception {
    IllegalEnergyConsumedByMoveException(int maxEnergy) {
        super("Energy consumed by move must exceed %d and  be lower than max energy: %d"
                .formatted(0, maxEnergy));
    }
}
