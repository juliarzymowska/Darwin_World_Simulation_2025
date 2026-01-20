package agh.ics.oop.model.exception;

public class IllegalMinMutationsException extends Exception {
    public IllegalMinMutationsException(int maxMutations) {
        super("Min mutations number cannot exceed max mutations number: %d"
                .formatted(maxMutations));
    }
}
