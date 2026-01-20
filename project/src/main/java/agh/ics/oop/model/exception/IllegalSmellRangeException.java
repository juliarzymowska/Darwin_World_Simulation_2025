package agh.ics.oop.model.exception;

public class IllegalSmellRangeException extends Exception {
    IllegalSmellRangeException() {
        super("Smell range must exceed %d"
                .formatted(0));
    }
}
