package agh.ics.oop.exception;

public class IllegalGenotypeLengthException extends ConfigurationException {
    public IllegalGenotypeLengthException() {
        super("Genotype length must exceed %d and be lower than %d"
                .formatted(0,20));
    }
}
