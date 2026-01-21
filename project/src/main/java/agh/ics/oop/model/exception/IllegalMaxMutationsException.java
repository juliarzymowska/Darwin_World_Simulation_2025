package agh.ics.oop.model.exception;

public class IllegalMaxMutationsException extends ConfigurationException {
    public IllegalMaxMutationsException(int genotypeLength) {
        super("Max mutations number cannot exceed genotype length: %d and must be greater than, or equal %d"
                .formatted(genotypeLength, 0));
    }
}
