package agh.ics.oop.model.exception;

public class IllegalDaysToDecreaseFeromonException extends Exception {
    public IllegalDaysToDecreaseFeromonException() {
        super("Days to decrease feromon number must exceed %d"
                .formatted(0));
    }
}
