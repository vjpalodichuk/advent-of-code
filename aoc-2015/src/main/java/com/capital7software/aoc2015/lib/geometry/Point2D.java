package com.capital7software.aoc2015.lib.geometry;

/**
 * A 2D point where the axis values are of the specified type.
 * @param x The X-Axis value.
 * @param y The Y-Axis value.
 * @param <T> The type of the Axis values.
 */
public record Point2D<T extends Number>(T x, T y) {
}
