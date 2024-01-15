package com.capital7software.aoc.lib.geometry;

/**
 * An enum to assist with calculating new points in 2D space.
 */
public enum Direction {
    /**
     * North Direction
     */
    NORTH(0, -1),
    /**
     * North-East Direction
     */
    NORTH_EAST(1, -1),
    /**
     * East Direction
     */
    EAST(1, 0),
    /**
     * South-East Direction
     */
    SOUTH_EAST(1, 1),
    /**
     * South Direction
     */
    SOUTH(0, 1),
    /**
     * South-West Direction
     */
    SOUTH_WEST(-1, 1),
    /**
     * West Direction
     */
    WEST(-1, 0),
    /**
     * North-West Direction
     */
    NORTH_WEST(-1, -1);

    private final Point2D<Integer> delta;

    /**
     * Instantiates a new Direction instance with the specified offsets.
     * @param dx The amount of change in the X-Axis for this Direction.
     * @param dy The amount of change in the Y-Axis for this Direction.
     */
    Direction(int dx, int dy) {
        this.delta = new Point2D<>(dx, dy);
    }

    /**
     * Returns the change in the X-Axis for this Direction.
     * @return The change in the X-Axis for this Direction.
     */
    public int dx() {
        return delta.x();
    }

    /**
     * Returns the change in the Y-Axis for this Direction.
     * @return The change in the Y-Axis for this Direction.
     */
    public int dy() {
        return delta.y();
    }
}
