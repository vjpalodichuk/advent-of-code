package com.capital7software.aoc.lib.geometry;

import org.jetbrains.annotations.NotNull;

/**
 * A 2D point where the axis values are of the specified type.
 *
 * @param x   The X-Axis value.
 * @param y   The Y-Axis value.
 * @param <T> The type of the Axis values.
 */
public record Point2D<T extends Number & Comparable<T>>(@NotNull T x, @NotNull T y) {
  /**
   * If the type is Double or Float, then EPSILON can be used when
   * checking for equality.
   */
  public static final double EPSILON = 0.00000001;
}
