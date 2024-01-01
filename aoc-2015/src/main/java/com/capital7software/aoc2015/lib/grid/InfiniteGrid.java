package com.capital7software.aoc2015.lib.grid;

import com.capital7software.aoc2015.lib.geometry.Direction;
import com.capital7software.aoc2015.lib.geometry.Point2D;

/**
 * An infinite Grid that is capable of navigating 2D space.
 */
public class InfiniteGrid {
    /**
     * Returns a new Point2D in the direction from the specified point.
     * <p>
     * @param point The point to calculate the new point from.
     * @param direction The direction of the new point from the specified point.
     * @return A new Point2D that is in the direction from the specified point.
     */
    public Point2D<Long> pointInDirection(Point2D<Long> point, Direction direction) {
        return new Point2D<>(point.x() + direction.dx(), point.y() + direction.dy());
    }
}
