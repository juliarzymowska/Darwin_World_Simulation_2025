package agh.ics.oop.exception;

public class IllegalMapWidthException extends ConfigurationException {
    public IllegalMapWidthException(int maxWidth) {
        super("Map width must be between %d and %d"
                .formatted(0, maxWidth));
    }
}
