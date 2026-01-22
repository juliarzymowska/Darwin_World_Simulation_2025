package agh.ics.oop.exception;

public class IllegalMinMutationsException extends ConfigurationException {
    public IllegalMinMutationsException(int maxMutations) {
        super("Min mutations number cannot exceed max mutations number: %d and must be greater than, or equal %d"
                .formatted(maxMutations, 0));
    }
}
