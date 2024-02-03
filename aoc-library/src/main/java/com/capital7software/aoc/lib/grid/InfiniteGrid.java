package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

/**
 * An infinite Grid that is capable of navigating 2D space.
 */
public class InfiniteGrid {
  /**
   * Instantiates a new and empty InfiniteGrid instance.
   */
  private InfiniteGrid() {

  }

  /**
   * Returns a new Point2D in the direction from the specified point.
   *
   * @param point     The point to calculate the new point from.
   * @param direction The direction of the new point from the specified point.
   * @return A new Point2D that is in the direction from the specified point.
   */
  public static @NotNull Point2D<Long> pointInDirection(
      @NotNull Point2D<Long> point,
      @NotNull Direction direction
  ) {
    return new Point2D<>(point.x() + direction.dx(), point.y() + direction.dy());
  }

  /**
   * Returns the Manhattan Distance between two points. The Manhattan Distance is the sum of
   * the absolute values of the coordinate differences. In other words it is
   * abs(a.x() - b.x()) + abs(a.y() - b.y())
   *
   * @param a The first point.
   * @param b The second point.
   * @return The Manhattan Distance between two points.
   */
  public static @NotNull Long manhattanDistance(
      @NotNull Point2D<Long> a,
      @NotNull Point2D<Long> b
  ) {
    return Math.abs((a.x() - b.x())) + Math.abs(a.y() - b.y());
  }
}
