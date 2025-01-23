package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import com.capital7software.aoc.lib.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
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
public record Grid2D<T>(int columns, int rows, @NotNull T[] items) implements Iterable<T> {
  /**
   * Creates a new Grid2D that contains characters in each cell. The grid is constructed from
   * the specified list of strings. Each string in the list is one row and all strings must be of
   * equal length.
   *
   * @param input The list of strings to process into a Grid2D
   * @return A new Grid2D that contains the characters from the input in each cell.
   */
  public static Grid2D<Character> buildCharacterGrid(List<String> input) {
    assert input != null && !input.isEmpty();

    var items = stringsToCharArray(input);

    var rows = input.size();
    var columns = input.getFirst().length();

    return new Grid2D<>(rows, columns, items);
  }

  private static Character[] stringsToCharArray(List<String> strings) {
    StringBuilder sb = new StringBuilder();
    for (String str : strings) {
      sb.append(str);
    }
    var chars = sb.toString().toCharArray();
    var characters = new Character[chars.length];

    for (int i = 0; i < chars.length; i++) {
      characters[i] = chars[i];
    }

    return characters;
  }

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
  public Grid2D(int columns, int rows, @NotNull T[] items) {
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
  public Grid2D(int columns, int rows, @NotNull T[] items, T initialValue) {
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
   * Returns a list with the values from startX, startY inclusive to lengthX, lengthY exclusive.
   *
   * @param startX  The starting x-coordinate.
   * @param startY  The starting y-coordinate.
   * @param lengthX The length in columns.
   * @param lengthY The length in rows.
   * @return A list with the values from startX, startY inclusive to lengthX, lengthY exclusive.
   */
  public List<T> get(int startX, int startY, int lengthX, int lengthY) {
    var answer = new ArrayList<T>();
    var endY = startY + lengthY;
    var endX = startX + lengthX;

    for (var y = startY; y < endY; y++) {
      for (var x = startX; x < endX; x++) {
        answer.add(get(x, y));
      }
    }

    return answer;
  }

  /**
   * Returns a list with the values from startX, startY inclusive to lengthX, lengthY exclusive.
   *
   * @param point   The starting x-coordinate and y-coordinate.
   * @param lengthX The length in columns.
   * @param lengthY The length in rows.
   * @return A list with the values from point inclusive to lengthX, lengthY exclusive.
   */
  public List<T> get(Point2D<Integer> point, int lengthX, int lengthY) {
    return get(point.x(), point.y(), lengthX, lengthY);
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
   * Sets the value of each space represented by the specified rectangle to the specified values.
   * The number of values must match the number of spaces in the specified rectangle.
   *
   * @param startX  The column of the upper left corner of the rectangle.
   * @param startY  The row of the upper left corner of the rectangle.
   * @param lengthX The column of the lower right corner of the rectangle.
   * @param lengthY The row of the lower right corner of the rectangle.
   * @param values  The new values to store in the spaces represented by the specified rectangle.
   */
  public void set(int startX, int startY, int lengthX, int lengthY, T[] values) {
    var i = 0;
    var endX = startX + lengthX;
    var endY = startY + lengthY;

    for (int y = startY; y < endY; y++) {
      for (int x = startX; x < endX; x++) {
        if (i < values.length) {
          set(x, y, values[i]);
        }
        i++;
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
   * Sets the value of each space represented by the specified rectangle to the specified values.
   * The number of values must match the number of spaces in the specified rectangle.
   *
   * @param point   The point of the upper left corner of the rectangle.
   * @param lengthX The column of the lower right corner of the rectangle.
   * @param lengthY The row of the lower right corner of the rectangle.
   * @param values  The new values to store in the spaces represented by the specified rectangle.
   */
  public void set(Point2D<Integer> point, int lengthX, int lengthY, T[] values) {
    set(point.x(), point.y(), lengthX, lengthY, values);
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
  public @NotNull Grid2D<T> copy() {
    return new Grid2D<>(columns, rows, Arrays.copyOf(items, items.length));
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
   * Returns all valid neighbors for the specified point and directions
   * and their current values in this Grid2D.
   *
   * @param x          The x point to calculate the neighbors from.
   * @param y          The y point to calculate the neighbors from.
   * @param directions The directions to get the neighbors of.
   * @param predicate  The filter that determines which neighbors to include.
   * @return A list of Pairs where the first property is the direction traveled to get to
   *     the neighbor on this Grid2D and the second property is the value at that point
   *     in this Grid2D.
   */
  public @NotNull List<Pair<Direction, T>> getNeighbors(
      int x, int y, Collection<Direction> directions, BiPredicate<T, Point2D<Integer>> predicate
  ) {
    List<Pair<Direction, T>> answer = new ArrayList<>(directions.size());

    for (var direction : directions) {
      var newPoint = pointInDirection(x, y, direction);

      if (isOnGrid(newPoint)) {
        var value = get(newPoint);
        if (predicate.test(value, newPoint)) {
          answer.add(new Pair<>(direction, value));
        }
      }
    }

    return answer;
  }

  /**
   * Returns all valid neighbors for the specified point and directions
   * and their current values in this Grid2D.
   *
   * @param x          The x point to calculate the neighbors from.
   * @param y          The y point to calculate the neighbors from.
   * @param directions The directions to get the neighbors of.
   * @return A list of Pairs where the first property is the direction traveled to get to
   *     the neighbor on this Grid2D and the second property is the value at that point
   *     in this Grid2D.
   */
  public @NotNull List<Pair<Direction, T>> getNeighbors(
      int x, int y, Collection<Direction> directions
  ) {
    return getNeighbors(x, y, directions, (value, point) -> true);
  }

  /**
   * Returns all valid neighbors for the specified point and directions
   * and their current values in this Grid2D.
   *
   * @param point      The point to get the neighbors of.
   * @param directions The directions to get the neighbors of.
   * @param predicate  Items that pass the predicate are included.
   * @return A list of Pairs where the first property is the direction traveled to get to
   *     the neighbor on this Grid2D and the second property is the value at that point
   *     in this Grid2D.
   */
  public @NotNull List<Pair<Direction, T>> getNeighbors(
      Point2D<Integer> point,
      Collection<Direction> directions,
      BiPredicate<T, Point2D<Integer>> predicate
  ) {
    return getNeighbors(point.x(), point.y(), directions, predicate);
  }

  /**
   * Returns all valid neighbors for the specified point and directions
   * and their current values in this Grid2D.
   *
   * @param point      The point to get the neighbors of.
   * @param directions The directions to get the neighbors of.
   * @return A list of Pairs where the first property is the direction traveled to get to
   *     the neighbor on this Grid2D and the second property is the value at that point
   *     in this Grid2D.
   */
  public @NotNull List<Pair<Direction, T>> getNeighbors(
      Point2D<Integer> point, Collection<Direction> directions
  ) {
    return getNeighbors(point.x(), point.y(), directions, (value, point2D) -> true);
  }

  /**
   * Returns all valid neighbors for the specified point and their current values in this Grid2D.
   * Each point may have a neighbor for each Direction.
   *
   * @param x The x point to calculate the neighbors from.
   * @param y The y point to calculate the neighbors from.
   * @return A list of Pairs where the first property is the direction traveled to get to
   *     the neighbor on this Grid2D and the second property is the value at that point
   *     in this Grid2D.
   */
  public @NotNull List<Pair<Direction, T>> getNeighbors(int x, int y) {
    return getNeighbors(x, y, Direction.ALL_DIRECTIONS, (value, point2D) -> true);
  }

  /**
   * Returns all valid neighbors for the specified point and their current values in this Grid2D.
   * Each point may have a neighbor for each Direction.
   *
   * @param point The point to get the neighbors of.
   * @return A list of Pairs where the first property is the direction traveled to get to
   *     the neighbor on this Grid2D and the second property is the value at that point
   *     in this Grid2D.
   */
  @NotNull
  public List<Pair<Direction, T>> getNeighbors(Point2D<Integer> point) {
    return getNeighbors(point.x(), point.y(), Direction.ALL_DIRECTIONS, (value, point2D) -> true);
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

  @Override
  public String toString() {
    return "Grid2D{"
        + "columns=" + columns
        + ", rows=" + rows
        + ", items=" + Arrays.toString(items)
        + '}';
  }

  /**
   * Prints the content of this grid into a String. The returned String is formatted to
   * represent the contents of this grid.
   *
   * @return The content of this grid into a String.
   */
  public String print() {
    var builder = new StringBuilder();

    for (int y = 0; y < rows; y++) {
      builder.append("\n");
      getRow(y).forEach(builder::append);
    }

    return builder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Grid2D<?> grid2D)) {
      return false;
    }
    return columns == grid2D.columns && rows == grid2D.rows && Arrays.equals(items, grid2D.items);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(columns, rows);
    result = 31 * result + Arrays.hashCode(items);
    return result;
  }

  /**
   * Flips the data of this grid horizontally around the origin and returns a new Grid2D
   * that contains the results.
   * <br><br>
   * This grid is not affected by this operation.
   *
   * @return A new grid flipped horizontally around the origin.
   */
  public Grid2D<T> flipHorizontal() {
    var answer = copy();
    var count = columns();
    for (int i = 0; i < count; i++) {
      answer.setColumn(count - i - 1, getColumn(i));
    }

    return answer;
  }

  /**
   * Flips the data of this grid vertically around the origin and returns a new Grid2D
   * that contains the results.
   * <br><br>
   * This grid is not affected by this operation.
   *
   * @return A new grid flipped vertically around the origin.
   */
  public Grid2D<T> flipVertical() {
    var answer = copy();
    var count = rows();
    for (int i = 0; i < count; i++) {
      answer.setRow(count - i - 1, getRow(i));
    }

    return answer;
  }

  /**
   * Rotates this grid 90 degrees clockwise and returns a new Grid2D instance with the results.
   * <br><br>
   * This grid is not affected by this operation.
   * <br><br>
   * Please note that if this grid is not square, an IllegalStateException will be thrown.
   *
   * @return A new Grid2D instance with a copy of this grid's data rotated 90 degrees
   *     clockwise.
   */
  public Grid2D<T> rotateRight() {
    assert columns == rows;

    var answer = copy();
    var count = rows();
    for (int i = 0; i < count; i++) {
      answer.setColumn(count - i - 1, getRow(i));
    }

    return answer;
  }

  /**
   * Rotates this grid 90 degrees counter-clockwise and returns a new Grid2D
   * instance with the results.
   * <br><br>
   * This grid is not affected by this operation.
   * <br><br>
   * Please note that if this grid is not square, an IllegalStateException will be thrown.
   *
   * @return A new Grid2D instance with a copy of this grid's data rotated 90 degrees
   *     counter-clockwise.
   */
  public Grid2D<T> rotateLeft() {
    assert columns == rows;

    var answer = copy();
    var count = rows();
    for (int i = 0; i < count; i++) {
      answer.setColumn(i, getRow(i).reversed());
    }

    return answer;
  }

  /**
   * Returns the Point2D of the first instance of the target that is found. The search starts at
   * 0, 0 and goes from left to right and top to bottom.
   *
   * @param target The particular value to find.
   *
   * @return The Point2D of the first instance of the target that is found.
   */
  public Optional<Point2D<Integer>> findFirst(T target) {
    Optional<Point2D<Integer>> answer = Optional.empty();

    for (int i = 0; i < rows(); i++) {
      for (int j = 0; j < columns(); j++) {
        if (get(j, i).equals(target)) {
          answer = Optional.of(new Point2D<>(j, i));
          break;
        }
      }
      if (answer.isPresent()) {
        break;
      }
    }
    return answer;
  }

  /**
   * Returns a list containing a Point2D for each instance of the target that is found.
   *
   * @param target The particular value to find.
   *
   * @return A list containing a Point2D for each instance of the target that is found.
   */
  public List<Point2D<Integer>> findAll(T target) {
    List<Point2D<Integer>> answer = new ArrayList<>();
    for (int i = 0; i < rows(); i++) {
      for (int j = 0; j < columns(); j++) {
        if (get(j, i).equals(target)) {
          answer.add(new Point2D<>(j, i));
        }
      }
    }
    return answer;
  }
}
