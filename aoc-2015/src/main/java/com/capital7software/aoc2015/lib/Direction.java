package com.capital7software.aoc2015.lib;

public enum Direction {
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1);

    private final Point2D<Integer> delta;

    Direction(int dx, int dy) {
        this.delta = new Point2D<>(dx, dy);
    }

    public int dx() {
        return delta.x();
    }

    public int dy() {
        return delta.y();
    }
}
