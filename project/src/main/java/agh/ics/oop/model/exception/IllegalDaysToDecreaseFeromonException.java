package agh.ics.oop.model.exception;

public class IllegalDaysToDecreaseFeromonException extends ConfigurationException {
    public IllegalDaysToDecreaseFeromonException() {
        super("Days to decrease feromon number must exceed, or be equal to %d"
                .formatted(0));
    }
}
