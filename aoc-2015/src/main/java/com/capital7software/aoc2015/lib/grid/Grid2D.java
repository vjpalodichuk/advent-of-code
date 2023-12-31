package com.capital7software.aoc2015.lib.grid;

import com.capital7software.aoc2015.lib.geometry.Direction;
import com.capital7software.aoc2015.lib.geometry.Point2D;
import com.capital7software.aoc2015.lib.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * A Finite 2D Grid that is capable of storing information at each point in the grid.
 *
 * @param columns The number of columns in this grid.
 * @param rows The number of rows in this grid.
 * @param items The array, whose size is columns x rows, to store the data of this grid.
 * @param <T> The type for the items stored in this grid.
 */
public record Grid2D<T>(int columns, int rows, T[] items) {
    /**
     * A Finite 2D Grid that is capable of storing information at each point in the grid.
     *
     * @param columns The number of columns in this grid.
     * @param rows The number of rows in this grid.
     * @param items The array, whose size is columns x rows, to store the data of this grid.
     * @param initialValue The initial value to fill this grid with.
     */
    public Grid2D(int columns, int rows, T[] items, T initialValue) {
        this(columns, rows, items);
        fill(initialValue);
    }

    /**
     * Fills all spaces in this grid with the specified value.
     *
     * @param value The value to fill this grid with.
     */
    public void fill(T value) {
        Arrays.fill(items, value);
    }

    /**
     * Retrieves the value at the specified space.
     *
     * @param x The column of the space.
     * @param y The row of the space.
     * @return The value at the specified space.
     */
    public T get(int x, int y) {
        return items[getOffset(x, y)];
    }

    /**
     * Retrieves the value at the specified space.
     *
     * @param point The column and row of the space.
     * @return The value at the specified space.
     */
    public T get(Point2D<Integer> point) {
        return get(point.x(), point.y());
    }

    /**
     * Sets the value at the specified space to the specified value, which may be null.
     *
     * @param x The column of the space.
     * @param y The row of the space.
     * @param value The new value to store in the space.
     */
    public void set(int x, int y, T value) {
        items[getOffset(x, y)] = value;
    }

    /**
     * Sets the value at the specified space to the specified value, which may be null.
     *
     * @param point The column and row of the space.
     * @param value The new value to store in the space.
     */
    public void set(Point2D<Integer> point, T value) {
        set(point.x(), point.y(), value);
    }


    /**
     * Sets the value of each space represented by the specified rectangle to the specified value.
     * The value may be null.
     *
     * @param x1 The column of the upper left corner of the rectangle.
     * @param y1 The row of the upper left corner of the rectangle.
     * @param x2 The column of the lower right corner of the rectangle.
     * @param y2 The row of the lower right corner of the rectangle.
     * @param value The new value to store in the spaces represented by the specified rectangle.
     */
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

    /**
     * Sets the value of each space represented by the specified rectangle to the specified value.
     * The value may be null.
     *
     * @param point1 The column and row of the upper left corner of the rectangle.
     * @param point2 The column and row of the lower right corner of the rectangle.
     * @param value The new value to store in the spaces represented by the specified rectangle.
     */
    public void set(Point2D<Integer> point1, Point2D<Integer> point2, T value) {
        set(point1.x(), point1.y(), point2.x(), point2.y(), value);
    }

    /**
     * Toggles the values of each space represented by the specified rectangle. If a space
     * contains value1 then value2 is stored at that space. If a space contains value2 then value1
     * is stored at that space.
     *
     * @param x1 The column of the upper left corner of the rectangle.
     * @param y1 The row of the upper left corner of the rectangle.
     * @param x2 The column of the lower right corner of the rectangle.
     * @param y2 The row of the lower right corner of the rectangle.
     * @param value1 The value to toggle in the spaces represented by the specified rectangle.
     * @param value2 The other value to toggle in the spaces represented by the specified rectangle.
     */
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

    /**
     * Toggles the value of each space represented by the specified rectangle. If a space
     * contains value1 then value2 is stored at that space. If a space contains value2 then value1
     * is stored at that space.
     *
     * @param point1 The column and row of the upper left corner of the rectangle.
     * @param point2 The column and row of the lower right corner of the rectangle.
     * @param value1 The value to toggle in the spaces represented by the specified rectangle.
     * @param value2 The other value to toggle in the spaces represented by the specified rectangle.
     */
    public void toggle(Point2D<Integer> point1, Point2D<Integer> point2, T value1, T value2) {
        toggle(point1.x(), point1.y(), point2.x(), point2.y(), value1, value2);
    }

    /**
     * Adjusts the value of each space represented by the specified rectangle. For each space, the adjuster
     * function is called and the result of the call is stored in the space.
     *
     * @param x1 The column of the upper left corner of the rectangle.
     * @param y1 The row of the upper left corner of the rectangle.
     * @param x2 The column of the lower right corner of the rectangle.
     * @param y2 The row of the lower right corner of the rectangle.
     * @param adjuster The function that is called for each space and whose return value is stored in the space.
     */
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

    /**
     * Adjusts the value of each space represented by the specified rectangle. For each space, the adjuster
     * function is called and the result of the call is stored in the space.
     *
     * @param point1 The column and row of the upper left corner of the rectangle.
     * @param point2 The column and row of the lower right corner of the rectangle.
     * @param adjuster The function that is called for each space and whose return value is stored in the space.
     */
    public void adjustBy(Point2D<Integer> point1, Point2D<Integer> point2, Function<T, T> adjuster) {
        adjustBy(point1.x(), point1.y(), point2.x(), point2.y(), adjuster);
    }

    /**
     * Returns the offset into the items array for the specified column and row.
     *
     * @param x The column to get the offset for.
     * @param y The row to get the offset for.
     * @return The offset into the items array for the specified column and row.
     */
    public int getOffset(int x, int y) {
        return x + y * columns;
    }

    /**
     * Returns the offset into the items array for the specified column and row.
     *
     * @param point The column and row to get the offset for.
     * @return The offset into the items array for the specified column and row.
     */
    public int getOffset(Point2D<Integer> point) {
        return getOffset(point.x(), point.y());
    }

    /**
     * Returns true if the specified column and row are on this grid.
     *
     * @param x The column to test.
     * @param y The row to test.
     * @return True if the specified column and row are on this grid.
     */
    public boolean isOnGrid(int x, int y) {
        return x >= 0 && x < columns && y >= 0 && y < rows;
    }

    /**
     * Returns true if the specified column and row are on this grid.
     *
     * @param point The column and row to test.
     * @return True if the specified column and row are on this grid.
     */
    public boolean isOnGrid(Point2D<Integer> point) {
        return isOnGrid(point.x(), point.y());
    }

    /**
     * Returns an independent copy of this Grid2D.
     *
     * @return An independent copy of this Grid2D.
     */
    public @NotNull Grid2D<T> copy() {
        return new Grid2D<>(columns, rows, Arrays.copyOf(items, items.length));
    }

    /**
     * Returns a new Point2D in the direction from the specified point.
     * <p>
     * @param x The x point to calculate the new point from.
     * @param y The y point to calculate the new point from.
     * @param direction The direction of the new point from the specified point.
     * @return A new Point2D that is in the direction from the specified point.
     */
    public Point2D<Integer> pointInDirection(int x, int y, Direction direction) {
        return new Point2D<>(x + direction.dx(), y + direction.dy());
    }

    /**
     * Returns a new Point2D in the direction from the specified point.
     * <p>
     * @param point The point to calculate the new point from.
     * @param direction The direction of the new point from the specified point.
     * @return A new Point2D that is in the direction from the specified point.
     */
    public Point2D<Integer> pointInDirection(Point2D<Integer> point, Direction direction) {
        return pointInDirection(point.x(), point.y(), direction);
    }

    /**
     * Returns all valid neighbors for the specified point and their current values in this Grid2D. Each
     * point may have upto eight neighbors.
     *
     * @param x The x point to calculate the neighbors from.
     * @param y The y point to calculate the neighbors from.
     * @return A list of Pairs where the first property is the point of the neighbor on this Grid2D and the
     * second property is the value at that point in this Grid2D.
     */
    public @NotNull List<Pair<Point2D<Integer>, T>> getAllNeighbors(int x, int y) {
        List<Pair<Point2D<Integer>, T>> answer = new ArrayList<>(Direction.values().length);

        for (var direction : Direction.values()) {
            var newPoint = pointInDirection(x, y, direction);

            if (isOnGrid(newPoint)) {
                var value = get(newPoint);
                answer.add(new Pair<>(newPoint, value));
            }
        }

        return answer;
    }

    /**
     * Returns all valid neighbors for the specified point and their current values in this Grid2D. Each
     * point may have upto eight neighbors.
     *
     * @param point The point to get the neighbors of.
     * @return A list of Pairs where the first property is the point of the neighbor on this Grid2D and the
     * second property is the value at that point in this Grid2D.
     */
    public @NotNull List<Pair<Point2D<Integer>, T>> getAllNeighbors(Point2D<Integer> point) {
        return getAllNeighbors(point.x(), point.y());
    }
}
