package agh.ics.oop.exception;

public class IllegalMoveToFeromonProbabilityException extends ConfigurationException {
    public IllegalMoveToFeromonProbabilityException() {
        super("Probability of animal moving to feromon must be between %f and %f"
                .formatted(0, 1));
    }
}
