package agh.ics.oop.model.util;

import java.util.Random;

/*
 * Class representing position of an animal on map and its direction.
 * It turns animal by 45 degrees to right.
 * */
public enum MapDirection {
    /*
     * All directions with according number from genotype.
     * */
    NORTH, // 0
    NORTH_EAST, // 1
    EAST, // 2
    SOUTH_EAST, // 3
    SOUTH, // 4
    SOUTH_WEST, // 5
    WEST, // 6
    NORTH_WEST; // 7

    private static final MapDirection[] DIRECTIONS = MapDirection.values();
    private static final Integer LENGTH = DIRECTIONS.length;
    private static final Random random = new Random();

    /*
     * (for tests)
     * @return random direction
     * */
    public static MapDirection getRandomDirection() {
        return DIRECTIONS[random.nextInt(LENGTH)];
    }

    /*
     * (for tests)
     * Makes the animal turn by 45 degrees to the right.
     * @param no_turns - number of turns for animal to make
     * @return direction of animal after turns
     * */
    public MapDirection turn(int no_turns) {
        return DIRECTIONS[(this.ordinal() + no_turns) % LENGTH];
    }

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        };
    }

    /*
     * Converts directions to Vector2d positions on map.
     * (new position already moves animal forward in according direction!)
     * @return new Vector2d according to new position of animal on map
     * */
    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }

    public int toInt() {
        return switch (this) {
            case NORTH -> 0;
            case NORTH_EAST -> 1;
            case EAST -> 2;
            case SOUTH_EAST -> 3;
            case SOUTH -> 4;
            case SOUTH_WEST -> 5;
            case WEST -> 6;
            case NORTH_WEST -> 7;
        };
    }
}
