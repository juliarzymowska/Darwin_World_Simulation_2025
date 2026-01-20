package agh.ics.oop.model.exception;

public class IllegalAnimalCountException extends Exception {
    public IllegalAnimalCountException(int animalCount, int maxAnimalCount) {
        super("Animal number must exceed %d and be lower than %d"
                .formatted(0, maxAnimalCount));
    }
}
