package agh.ics.oop.model.exception;

public class IllegalMapHeightException extends Exception {
    public IllegalMapHeightException(int maxHeight) {
        super("Map height must be between %d and %d"
                .formatted(0, maxHeight));
    }
}
