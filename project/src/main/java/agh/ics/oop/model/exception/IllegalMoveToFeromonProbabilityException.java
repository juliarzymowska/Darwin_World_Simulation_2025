package agh.ics.oop.model.exception;

public class IllegalMoveToFeromonProbabilityException extends Exception{
    public IllegalMoveToFeromonProbabilityException() {
        super("Probability of animal moving to feromon must be between %d and %d"
                .formatted(0, 1));
    }
}
