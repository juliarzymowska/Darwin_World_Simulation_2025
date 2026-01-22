package agh.ics.oop.exception;

public class IllegalEnergyConsumedByMoveException extends ConfigurationException {
    public IllegalEnergyConsumedByMoveException(int maxEnergy) {
        super("Energy consumed by move must exceed %d and be lower than max energy: %d"
                .formatted(0, maxEnergy));
    }
}
