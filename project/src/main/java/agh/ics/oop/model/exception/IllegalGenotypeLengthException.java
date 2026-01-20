package agh.ics.oop.model.exception;

public class IllegalGenotypeLengthException extends Exception {
    public IllegalGenotypeLengthException() {
        super("Genotype length must exceed %d"
                .formatted(0));
    }
}
