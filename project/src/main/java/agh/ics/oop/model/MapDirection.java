package agh.ics.oop.model;

/*
* Class representing position of an animal on map.
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
    * */
    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1,-1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1,1);
        };
    }
}
