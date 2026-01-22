package agh.ics.oop.exception;

public class EarthClassException extends ConfigurationException {
    public EarthClassException() {
        super("Parametry feromonowe muszą być równe 0 dla mapy EARTH");
    }
}
