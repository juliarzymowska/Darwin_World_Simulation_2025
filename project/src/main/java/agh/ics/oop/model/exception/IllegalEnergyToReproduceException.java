package agh.ics.oop.model.exception;

public class IllegalEnergyToReproduceException extends ConfigurationException {
    public IllegalEnergyToReproduceException(int maxEnergy) {
        super("Energy needed to reproduce must exceed %d and be lower than max energy: %d"
                .formatted(0, maxEnergy));
    }
}
