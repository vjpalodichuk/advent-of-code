package com.capital7software.aoc.lib.geometry;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * An enum to assist with calculating new points in 2D space.
 */
public enum Direction {
  /**
   * North Direction.
   */
  NORTH(0, -1, true) {
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
    public List<Direction> getPerpendicular() {
      return WEST_AND_EAST;
    }
  },
  /**
   * North-East Direction.
   */
  NORTH_EAST(1, -1, false) {
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
    public List<Direction> getPerpendicular() {
      return NORTH_WEST_AND_SOUTH_EAST;
    }
  },
  /**
   * East Direction.
   */
  EAST(1, 0, true) {
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
    public List<Direction> getPerpendicular() {
      return NORTH_AND_SOUTH;
    }
  },
  /**
   * South-East Direction.
   */
  SOUTH_EAST(1, 1, false) {
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
    public List<Direction> getPerpendicular() {
      return NORTH_EAST_AND_SOUTH_WEST;
    }
  },
  /**
   * South Direction.
   */
  SOUTH(0, 1, true) {
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
    public List<Direction> getPerpendicular() {
      return EAST_AND_WEST;
    }
  },
  /**
   * South-West Direction.
   */
  SOUTH_WEST(-1, 1, false) {
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
    public List<Direction> getPerpendicular() {
      return SOUTH_EAST_AND_NORTH_WEST;
    }
  },
  /**
   * West Direction.
   */
  WEST(-1, 0, true) {
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
    public List<Direction> getPerpendicular() {
      return SOUTH_AND_NORTH;
    }

  },
  /**
   * North-West Direction.
   */
  NORTH_WEST(-1, -1, false) {
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
    public List<Direction> getPerpendicular() {
      return SOUTH_WEST_AND_NORTH_EAST;
    }
  };

  private static final List<Direction> NORTH_AND_SOUTH = List.of(NORTH, SOUTH);
  private static final List<Direction> SOUTH_AND_NORTH = List.of(SOUTH, NORTH);
  private static final List<Direction> EAST_AND_WEST = List.of(EAST, WEST);
  private static final List<Direction> WEST_AND_EAST = List.of(WEST, EAST);
  private static final List<Direction> NORTH_EAST_AND_SOUTH_WEST = List.of(NORTH_EAST, SOUTH_WEST);
  private static final List<Direction> SOUTH_WEST_AND_NORTH_EAST = List.of(SOUTH_WEST, NORTH_EAST);
  private static final List<Direction> NORTH_WEST_AND_SOUTH_EAST = List.of(NORTH_WEST, SOUTH_EAST);
  private static final List<Direction> SOUTH_EAST_AND_NORTH_WEST = List.of(SOUTH_EAST, NORTH_WEST);

  /**
   * The four cardinal directions of North, South, East, and West.
   */
  public static final Set<Direction> CARDINAL_DIRECTIONS = Set.of(NORTH, SOUTH, EAST, WEST);

  /**
   * All directions.
   */
  public static final Set<Direction> ALL_DIRECTIONS = Set.of(Direction.values());

  private final Point2D<Integer> delta;

  public final boolean isCardinal;

  /**
   * Instantiates a new Direction instance with the specified offsets.
   *
   * @param dx         The amount of change in the X-Axis for this Direction.
   * @param dy         The amount of change in the Y-Axis for this Direction.
   * @param isCardinal If true, then this is a cardinal direction
   */
  Direction(int dx, int dy, boolean isCardinal) {
    this.isCardinal = isCardinal;
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
   * Returns the List of Directions that are perpendicular (90 degrees) from this Direction.
   *
   * @return The List of Directions that are perpendicular (90 degrees) from this Direction.
   */
  public List<Direction> getPerpendicular() {
    return Collections.emptyList();
  }

  /**
   * Returns the delta along the x-axis. Used by Kotlin to support decomposing assignments.
   *
   * @return The delta along the x-axis.
   */
  public int component1() {
    return dx();
  }

  /**
   * Returns the delta along the y-axis. Used by Kotlin to support decomposing assignments.
   *
   * @return The delta along the y-axis.
   */
  public int component2() {
    return dy();
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
   * @param alphaLabel The label to convert to a numeric label.
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
