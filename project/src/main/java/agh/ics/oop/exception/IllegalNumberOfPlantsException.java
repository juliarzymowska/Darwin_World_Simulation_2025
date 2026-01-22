package agh.ics.oop.exception;

public class IllegalNumberOfPlantsException extends ConfigurationException {
    private static final String ERROR_MESSAGE = "Number of plants for map of size: %d x %d cannot exceed %d and must be greater than %d";

    public IllegalNumberOfPlantsException(int mapWidth, int mapHeight) {
        super(ERROR_MESSAGE.formatted(mapWidth, mapHeight, mapWidth * mapHeight, 0));
    }
}
