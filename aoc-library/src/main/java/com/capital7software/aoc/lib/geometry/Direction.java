package com.capital7software.aoc.lib.geometry;

/**
 * An enum to assist with calculating new points in 2D space.
 */
public enum Direction {
    /**
     * North Direction
     */
    NORTH(0, -1) {
        @Override
        public Direction getLeft() {
            return WEST;
        }

        @Override
        public Direction getRight() {
            return EAST;
        }
    },
    /**
     * North-East Direction
     */
    NORTH_EAST(1, -1) {
        @Override
        public Direction getLeft() {
            return NORTH_WEST;
        }

        @Override
        public Direction getRight() {
            return SOUTH_EAST;
        }
    },
    /**
     * East Direction
     */
    EAST(1, 0) {
        @Override
        public Direction getLeft() {
            return NORTH;
        }

        @Override
        public Direction getRight() {
            return SOUTH;
        }
    },
    /**
     * South-East Direction
     */
    SOUTH_EAST(1, 1) {
        @Override
        public Direction getLeft() {
            return NORTH_EAST;
        }

        @Override
        public Direction getRight() {
            return SOUTH_WEST;
        }
    },
    /**
     * South Direction
     */
    SOUTH(0, 1) {
        @Override
        public Direction getLeft() {
            return EAST;
        }

        @Override
        public Direction getRight() {
            return WEST;
        }
    },
    /**
     * South-West Direction
     */
    SOUTH_WEST(-1, 1) {
        @Override
        public Direction getLeft() {
            return SOUTH_EAST;
        }

        @Override
        public Direction getRight() {
            return NORTH_WEST;
        }
    },
    /**
     * West Direction
     */
    WEST(-1, 0) {
        @Override
        public Direction getLeft() {
            return SOUTH;
        }

        @Override
        public Direction getRight() {
            return NORTH;
        }
    },
    /**
     * North-West Direction
     */
    NORTH_WEST(-1, -1) {
        @Override
        public Direction getLeft() {
            return SOUTH_WEST;
        }

        @Override
        public Direction getRight() {
            return NORTH_EAST;
        }
    };

    private final Point2D<Integer> delta;

    /**
     * Instantiates a new Direction instance with the specified offsets.
     *
     * @param dx The amount of change in the X-Axis for this Direction.
     * @param dy The amount of change in the Y-Axis for this Direction.
     */
    Direction(int dx, int dy) {
        this.delta = new Point2D<>(dx, dy);
    }

    /**
     * Returns the change in the X-Axis for this Direction.
     *
     * @return The change in the X-Axis for this Direction.
     */
    public int dx() {
        return delta.x();
    }

    /**
     * Returns the change in the Y-Axis for this Direction.
     *
     * @return The change in the Y-Axis for this Direction.
     */
    public int dy() {
        return delta.y();
    }

    /**
     * Returns the change offsets as a Point2D.
     *
     * @return The change offsets as a Point2D.
     */
    public Point2D<Integer> delta() {
        return delta;
    }

    /**
     * Returns the Direction to the left of this Direction.
     *
     * @return The Direction to the left of this Direction.
     */
    public Direction getLeft() {
        return null;
    }

    /**
     * Returns the Direction to the right of this Direction.
     *
     * @return The Direction to the right of this Direction.
     */
    public Direction getRight() {
        return null;
    }
}
