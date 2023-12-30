package com.capital7software.aoc2015.lib;

import java.util.Arrays;
import java.util.function.Function;

public record Grid2D<T>(int columns, int rows, T[] items) {
    public Grid2D(int columns, int rows, T[] items, T initialValue) {
        this(columns, rows, items);
        fill(initialValue);
    }

    public void fill(T value) {
        Arrays.fill(items, value);
    }

    public T get(int x, int y) {
        return items[getOffset(x, y)];
    }

    public T get(Point2D<Integer> point) {
        return get(point.x(), point.y());
    }

    public void set(int x, int y, T value) {
        items[getOffset(x, y)] = value;
    }

    public void set(Point2D<Integer> point, T value) {
        set(point.x(), point.y(), value);
    }

    public void set(int x1, int y1, int x2, int y2, T value) {
        var minX = Math.min(x1, x2);
        var maxX = Math.max(x1, x2);
        var minY = Math.min(y1, y2);
        var maxY = Math.max(y1, y2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                set(x, y, value);
            }
        }
    }

    public void set(Point2D<Integer> point1, Point2D<Integer> point2, T value) {
        set(point1.x(), point1.y(), point2.x(), point2.y(), value);
    }

    public void toggle(int x1, int y1, int x2, int y2, T value1, T value2) {
        var minX = Math.min(x1, x2);
        var maxX = Math.max(x1, x2);
        var minY = Math.min(y1, y2);
        var maxY = Math.max(y1, y2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (value1.equals(get(x, y))) {
                    set(x, y, value2);
                } else {
                    set(x, y, value1);
                }
            }
        }
    }


    public void toggle(Point2D<Integer> point1, Point2D<Integer> point2, T value1, T value2) {
        toggle(point1.x(), point1.y(), point2.x(), point2.y(), value1, value2);
    }

    public void adjustBy(int x1, int y1, int x2, int y2, Function<T, T> adjuster) {
        var minX = Math.min(x1, x2);
        var maxX = Math.max(x1, x2);
        var minY = Math.min(y1, y2);
        var maxY = Math.max(y1, y2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                set(x, y, adjuster.apply(get(x, y)));
            }
        }
    }

    public void adjustBy(Point2D<Integer> point1, Point2D<Integer> point2, Function<T, T> adjuster) {
        adjustBy(point1.x(), point1.y(), point2.x(), point2.y(), adjuster);
    }

    public int getOffset(int x, int y) {
        return x + y * columns;
    }

    public int getOffset(Point2D<Integer> point) {
        return getOffset(point.x(), point.y());
    }

    public boolean isOnGrid(int x, int y) {
        return x >= 0 && x < columns && y >= 0 && y < rows;
    }

    public boolean isOnGrid(Point2D<Integer> point) {
        return isOnGrid(point.x(), point.y());
    }
}
