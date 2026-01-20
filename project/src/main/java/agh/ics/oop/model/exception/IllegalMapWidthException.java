package agh.ics.oop.model.exception;

public class IllegalMapWidthException extends Exception {
    public IllegalMapWidthException(int maxWidth) {
        super("Map width must be between %d and %d"
                .formatted(0, maxWidth));
    }
}
