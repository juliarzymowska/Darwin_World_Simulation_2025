package agh.ics.oop.model.exception;

public class IllegalMaxMutationsException extends Exception {
    public IllegalMaxMutationsException(int genotypeLength) {
        super("Max mutations number cannot exceed genotype length: %d"
                .formatted(genotypeLength));
    }
}
