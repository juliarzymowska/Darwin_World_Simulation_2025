package agh.ics.oop.exception;

public class IllegalMapHeightException extends ConfigurationException {
    public IllegalMapHeightException(int maxHeight) {
        super("Map height must be between %d and %d"
                .formatted(0, maxHeight));
    }
}
