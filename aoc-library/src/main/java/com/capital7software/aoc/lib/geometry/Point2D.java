package com.capital7software.aoc.lib.geometry;

import com.capital7software.aoc.lib.math.MathOperations;
import org.jetbrains.annotations.NotNull;

/**
 * A 2D point where the axis values are of the specified type.
 *
 * @param x   The X-Axis value.
 * @param y   The Y-Axis value.
 * @param <T> The type of the Axis values.
 */
public record Point2D<T extends Number & Comparable<T>>(@NotNull T x, @NotNull T y)
    implements Comparable<Point2D<T>> {
  /**
   * If the type is Double or Float, then EPSILON can be used when
   * checking for equality.
   */
  public static final double EPSILON = 0.00000001;

  /**
   * Subtracts a point from this point and returns a new point with the result.
   *
   * @param o The point to subtract from this point.
   * @return A new point with the result.
   */
  public Point2D<T> subtract(@NotNull Point2D<T> o) {
    return subtract(this, o);
  }

  /**
   * Subtracts two points and returns a new point with the new result.
   *
   * @param first  The first point.
   * @param second The second point.
   * @param <E>    The type of the point axis values.
   * @return A new point with the result.
   */
  public static <E extends Number & Comparable<E>> Point2D<E> subtract(
      @NotNull Point2D<E> first,
      @NotNull Point2D<E> second
  ) {
    return new Point2D<>(
        MathOperations.subtract(first.x, second.x), MathOperations.subtract(first.y, second.y)
    );
  }

  @Override
  public int compareTo(@NotNull Point2D<T> o) {
    var result = subtract(o);

    return result.x.compareTo(result.y);
  }
}
