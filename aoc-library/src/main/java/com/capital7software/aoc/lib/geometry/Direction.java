package com.capital7software.aoc.lib.geometry;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

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

        @Override
        public Direction opposite() {
            return SOUTH;
        }

        @Override
        public Set<Direction> getPerpendicular() {
            return EAST_AND_WEST;
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

        @Override
        public Direction opposite() {
            return SOUTH_WEST;
        }

        @Override
        public Set<Direction> getPerpendicular() {
            return NORTH_WEST_AND_SOUTH_EAST;
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

        @Override
        public Direction opposite() {
            return WEST;
        }

        @Override
        public Set<Direction> getPerpendicular() {
            return NORTH_AND_SOUTH;
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

        @Override
        public Direction opposite() {
            return NORTH_WEST;
        }

        @Override
        public Set<Direction> getPerpendicular() {
            return NORTH_EAST_AND_SOUTH_WEST;
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

        @Override
        public Direction opposite() {
            return NORTH;
        }

        @Override
        public Set<Direction> getPerpendicular() {
            return EAST_AND_WEST;
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

        @Override
        public Direction opposite() {
            return NORTH_EAST;
        }

        @Override
        public Set<Direction> getPerpendicular() {
            return NORTH_WEST_AND_SOUTH_EAST;
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

        @Override
        public Direction opposite() {
            return EAST;
        }

        @Override
        public Set<Direction> getPerpendicular() {
            return NORTH_AND_SOUTH;
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

        @Override
        public Direction opposite() {
            return SOUTH_EAST;
        }

        @Override
        public Set<Direction> getPerpendicular() {
            return NORTH_EAST_AND_SOUTH_WEST;
        }
    };

    private static final Set<Direction> NORTH_AND_SOUTH = Set.of(NORTH, SOUTH);
    private static final Set<Direction> EAST_AND_WEST = Set.of(EAST, WEST);
    private static final Set<Direction> NORTH_EAST_AND_SOUTH_WEST = Set.of(NORTH_EAST, SOUTH_WEST);
    private static final Set<Direction> NORTH_WEST_AND_SOUTH_EAST = Set.of(NORTH_WEST, SOUTH_EAST);

    /**
     * The four cardinal directions of North, South, East, and West!
     */
    public static final Set<Direction> CARDINAL_DIRECTIONS = Set.of(NORTH, SOUTH, EAST, WEST);

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

    /**
     * Returns the Direction that is 180 degrees from this Direction.
     *
     * @return The Direction that is 180 degrees from this Direction.
     */
    public Direction opposite() {
        return null;
    }

    /**
     * Returns the Set of Directions that are perpendicular (90 degrees) from this Direction.
     * @return The Set of Directions that are perpendicular (90 degrees) from this Direction.
     */
    public Set<Direction> getPerpendicular() {
        return Collections.emptySet();
    }

    /**
     * Returns a cardinal Direction based on the specified single character label.<br>
     * <ul>
     *     <li>
     *         A label of U or 3 returns NORTH.
     *     </li>
     *     <li>
     *         A label of D or 1 returns SOUTH.
     *     </li>
     *     <li>
     *         A label of L or 2 returns WEST.
     *     </li>
     *     <li>
     *         A label of R or 0 returns EAST.
     *     </li>
     * </ul>
     *
     * @param label The label to get a Direction from.
     * @return A cardinal Direction based on the specified single character label.
     */
    public static @NotNull Direction fromLabel(@NotNull String label) {
        return switch (label) {
            case "U", "3" -> NORTH;
            case "D", "1" -> SOUTH;
            case "L", "2" -> WEST;
            case "R", "0" -> EAST;
            default -> throw new RuntimeException("Unknown direction: " + label);
        };
    }

    /**
     * Returns a numeric label from an alpha label.<br>
     * <ul>
     *     <li>
     *         An alpha label of U returns 3.
     *     </li>
     *     <li>
     *         An alpha label of D returns 1.
     *     </li>
     *     <li>
     *         An alpha label of L returns 2.
     *     </li>
     *     <li>
     *         An alpha label of R returns 0.
     *     </li>
     * </ul>
     *
     * @param alphaLabel The label to convert to a numeric label..
     * @return A numeric label from an alpha label.
     */
    public static String toNumericLabel(String alphaLabel) {
        return switch (alphaLabel) {
            case "U" -> "3";
            case "D" -> "1";
            case "L" -> "2";
            case "R" -> "0";
            default -> throw new RuntimeException("Unknown direction: " + alphaLabel);
        };
    }
}
