package com.capital7software.aoc2015.lib;

public class InfiniteGrid {
    public Point2D<Long> pointInDirection(Point2D<Long> point, Direction direction) {
        return new Point2D<>(point.x() + direction.dx(), point.y() + direction.dy());
    }
}
