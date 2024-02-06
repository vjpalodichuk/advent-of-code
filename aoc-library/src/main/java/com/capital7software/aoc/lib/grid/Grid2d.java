package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import com.capital7software.aoc.lib.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * A Finite 2D Grid that is capable of storing information at each point in the grid.
 *
 * @param columns The number of columns in this grid.
 * @param rows    The number of rows in this grid.
 * @param items   The array, whose size is columns x rows, to store the data of this grid.
 * @param <T>     The type for the items stored in this grid.
 */
public record Grid2d<T>(int columns, int rows, @NotNull T[] items) implements Iterable<T> {
  /**
   * An iterator that iterates in-order over the elements of this Grid2D.
   */
  public class Grid2dIterator implements Iterator<T> {
    private int cursor;

    /**
     * Instantiates a new iterator instance.
     */
    public Grid2dIterator() {
      cursor = 0;
    }

    @Override
    public boolean hasNext() {
      return cursor != items.length;
    }

    @Override
    public T next() throws NoSuchElementException {
      if (cursor >= items.length) {
        throw new NoSuchElementException("next called when this Iterator has no next element!");
      }
      return items[cursor++];
    }
  }

  /**
   * Instantiates a new Grid2D that owns the specified items array.
   *
   * @param columns The number of columns in this grid.
   * @param rows    The number of rows in this grid.
   * @param items   The array, whose size is columns x rows, to store the data of this grid.
   */
  public Grid2d(int columns, int rows, @NotNull T[] items) {
    this.columns = columns;
    this.rows = rows;
    this.items = Arrays.copyOf(items, items.length);
  }

  /**
   * A Finite 2D Grid that is capable of storing information at each point in the grid.
   *
   * @param columns      The number of columns in this grid.
   * @param rows         The number of rows in this grid.
   * @param items        The array, whose size is columns x rows, to store the data of this grid.
   * @param initialValue The initial value to fill this grid with.
   */
  public Grid2d(int columns, int rows, @NotNull T[] items, T initialValue) {
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
   * Returns the value in the first cell of this Grid2D.
   *
   * @return The value in the first cell of this Grid2D.
   */
  public T getFirst() {
    return items[0];
  }

  /**
   * Returns the value in the last cell of this Grid2D.
   *
   * @return The value in the last cell of this Grid2D.
   */
  public T getLast() {
    return items[size() - 1];
  }

  /**
   * Sets the value at the specified space to the specified value, which may be null.
   *
   * @param x     The column of the space.
   * @param y     The row of the space.
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
   * @param x1    The column of the upper left corner of the rectangle.
   * @param y1    The row of the upper left corner of the rectangle.
   * @param x2    The column of the lower right corner of the rectangle.
   * @param y2    The row of the lower right corner of the rectangle.
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
   * @param value  The new value to store in the spaces represented by the specified rectangle.
   */
  public void set(Point2D<Integer> point1, Point2D<Integer> point2, T value) {
    set(point1.x(), point1.y(), point2.x(), point2.y(), value);
  }

  /**
   * Toggles the values of each space represented by the specified rectangle. If a space
   * contains value1 then value2 is stored at that space. If a space contains value2 then value1
   * is stored at that space.
   *
   * @param x1     The column of the upper left corner of the rectangle.
   * @param y1     The row of the upper left corner of the rectangle.
   * @param x2     The column of the lower right corner of the rectangle.
   * @param y2     The row of the lower right corner of the rectangle.
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
   * Adjusts the value of each space represented by the specified rectangle. For each space,
   * the adjuster function is called and the result of the call is stored in the space.
   *
   * @param x1       The column of the upper left corner of the rectangle.
   * @param y1       The row of the upper left corner of the rectangle.
   * @param x2       The column of the lower right corner of the rectangle.
   * @param y2       The row of the lower right corner of the rectangle.
   * @param adjuster The function that is called for each space and whose return value is
   *                 stored in the space. The current value of the space is passed as a parameter.
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
   * Adjusts the value of each space represented by the specified rectangle. For each space,
   * the adjuster function is called and the result of the call is stored in the space.
   *
   * @param point1   The column and row of the upper left corner of the rectangle.
   * @param point2   The column and row of the lower right corner of the rectangle.
   * @param adjuster The function that is called for each space and whose return value is
   *                 stored in the space. The current value of the space is passed as a parameter.
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
  public @NotNull Grid2d<T> copy() {
    return new Grid2d<>(columns, rows, Arrays.copyOf(items, items.length));
  }

  /**
   * Returns a new Point2D in the direction from the specified point.
   *
   * @param x         The x point to calculate the new point from.
   * @param y         The y point to calculate the new point from.
   * @param direction The direction of the new point from the specified point.
   * @return A new Point2D that is in the direction from the specified point.
   */
  public static @NotNull Point2D<Integer> pointInDirection(int x, int y, Direction direction) {
    return new Point2D<>(x + direction.dx(), y + direction.dy());
  }

  /**
   * Returns a new Point2D in the direction from the specified point.
   *
   * @param point     The point to calculate the new point from.
   * @param direction The direction of the new point from the specified point.
   * @return A new Point2D that is in the direction from the specified point.
   */
  public static @NotNull Point2D<Integer> pointInDirection(
      Point2D<Integer> point,
      Direction direction
  ) {
    return pointInDirection(point.x(), point.y(), direction);
  }

  /**
   * Translates the specified point into a point that exists on this Grid2D and returns it.
   *
   * @param point The point to translate.
   * @return A new point that exists on this Grid2D.
   */
  public Point2D<Integer> virtualToReal(Point2D<Integer> point) {
    var x = ((point.x() % columns) + columns) % columns;
    var y = ((point.y() % rows) + rows) % rows;

    return new Point2D<>(x, y);
  }

  /**
   * Returns all valid neighbors for the specified point and their current values in this Grid2D.
   * Each point may have upto eight neighbors.
   *
   * @param x The x point to calculate the neighbors from.
   * @param y The y point to calculate the neighbors from.
   * @return A list of Pairs where the first property is the point of the neighbor on this
   *     Grid2D and the second property is the value at that point in this Grid2D.
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
   * Returns all valid neighbors for the specified point and their current values in this Grid2D.
   * Each point may have upto eight neighbors.
   *
   * @param point The point to get the neighbors of.
   * @return A list of Pairs where the first property is the point of the neighbor on this
   *     Grid2D and the second property is the value at that point in this Grid2D.
   */
  @NotNull
  public List<Pair<Point2D<Integer>, T>> getAllNeighbors(Point2D<Integer> point) {
    return getAllNeighbors(point.x(), point.y());
  }

  /**
   * Returns a copy of the items held by this Grid2D in a new array.
   *
   * @return A copy of the items held by this Grid2D in a new array.
   */
  @NotNull
  @Override
  public T[] items() {
    return Arrays.copyOf(items, items.length);
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return new Grid2dIterator();
  }

  /**
   * Returns a Stream of the items in this Grid2D.
   *
   * @return A Stream of the items in this Grid2D.
   */
  @NotNull
  public Stream<T> stream() {
    return Stream.of(items);
  }


  /**
   * Returns the size of this Grid2D. The size is calculated by multiplying columns * rows.
   *
   * @return The size of this Grid2D.
   */
  public int size() {
    return rows * columns;
  }

  private boolean targetColumn(Direction direction) {
    return direction == Direction.NORTH || direction == Direction.SOUTH;
  }

  private boolean targetRow(Direction direction) {
    return direction == Direction.EAST || direction == Direction.WEST;
  }

  private void throwIfOutOfBounds(int index, int max) {
    if (index < 0 || index >= max) {
      throw new IndexOutOfBoundsException("Column index out of range: " + index);
    }
  }

  /**
   * Returns the specified row.
   *
   * @param index The row to get.
   * @return The specified row.
   */
  public @NotNull List<T> getRow(int index) {
    throwIfOutOfBounds(index, rows);

    var list = new ArrayList<T>(columns);
    for (int i = 0; i < columns; i++) {
      list.add(get(i, index));
    }
    return list;
  }

  /**
   * Returns the specified column.
   *
   * @param index The column to get.
   * @return The specified column.
   */
  public @NotNull List<T> getColumn(int index) {
    throwIfOutOfBounds(index, columns);

    var list = new ArrayList<T>(rows);
    for (int i = 0; i < rows; i++) {
      list.add(get(index, i));
    }
    return list;
  }

  /**
   * Gets a row or column as a List that then can be easily compared.
   *
   * @param direction The cardinal Direction indicates the axis to select.
   * @param index     The row or column index to retrieve.
   * @return A List that contains the elements from the specified row or column.
   */
  public @NotNull List<T> getRowOrColumn(@NotNull Direction direction, int index) {
    if (targetColumn(direction)) {
      return getColumn(index);
    } else if (targetRow(direction)) {
      return getRow(index);
    } else {
      throw new RuntimeException("Unknown direction specified: " + direction);
    }
  }

  /**
   * Sets the specified row to the specified values.
   *
   * @param index The row to update.
   * @param data  The updated data to apply to this grid.
   */
  public void setRow(int index, @NotNull List<T> data) {
    throwIfOutOfBounds(index, rows);

    for (int i = 0; i < columns; i++) {
      set(i, index, data.get(i));
    }
  }

  /**
   * Sets the specified column to the specified values.
   *
   * @param index The column to update.
   * @param data  The updated data to apply to this grid.
   */
  public void setColumn(int index, @NotNull List<T> data) {
    throwIfOutOfBounds(index, columns);

    for (int i = 0; i < rows; i++) {
      set(index, i, data.get(i));
    }
  }

  /**
   * Sets a row or column to the specified values. The specified List must contain the
   * same number of elements as the length of the row or column that is being updated.
   *
   * @param direction The cardinal Direction indicates the axis to select.
   * @param index     The row or column index to set.
   * @param data      The updated data to apply to this grid.
   */
  public void setRowOrColumn(@NotNull Direction direction, int index, @NotNull List<T> data) {
    if (targetColumn(direction)) {
      setColumn(index, data);
    } else if (targetRow(direction)) {
      setRow(index, data);
    } else {
      throw new RuntimeException("Unknown direction specified: " + direction);
    }
  }

  /**
   * Returns the number of rows or columns in this grid. Which one to return is based on
   * the specified cardinal Direction.
   *
   * @param direction The cardinal Direction indicates the axis to select.
   * @return The number of rows or columns in this grid.
   */
  public int getRowOrColumnLength(@NotNull Direction direction) {
    return switch (direction) {
      case NORTH, SOUTH -> columns;
      case EAST, WEST -> rows;
      default -> throw new RuntimeException("Unknown direction: " + direction);
    };
  }

  /**
   * Returns true if the specified x and y represent a corner of this grid.
   *
   * @param x The x to check.
   * @param y The y to check.
   * @return True if the specified x and y represent a corner of this grid.
   */
  public boolean isCorner(int x, int y) {
    return (x == 0 && y == 0)
        || (x == 0 && y == rows - 1)
        || (x == columns - 1 && y == 0)
        || (x == columns - 1 && y == rows - 1);
  }

  /**
   * Returns true if the specified point represents a corner of this grid.
   *
   * @param point The point to check.
   * @return True if the specified point represents a corner of this grid.
   */
  public boolean isCorner(Point2D<Integer> point) {
    return isCorner(point.x(), point.y());
  }
}
