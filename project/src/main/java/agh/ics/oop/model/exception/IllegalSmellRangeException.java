package agh.ics.oop.model.exception;

public class IllegalSmellRangeException extends ConfigurationException {
    public IllegalSmellRangeException() {
        super("Smell range must exceed %d"
                .formatted(0));
    }
}
