package agh.ics.oop.model.exception;

public class IllegalGenotypeLengthException extends ConfigurationException {
    public IllegalGenotypeLengthException() {
        super("Genotype length must exceed %d"
                .formatted(0));
    }
}
