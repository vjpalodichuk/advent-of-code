package com.capital7software.aoc.lib.geometry;

import com.capital7software.aoc.lib.math.MathOperations;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;

/**
 * A 2D point where the axis values are of the specified type.
 *
 * @param <T> The type of the Axis values.
 */
@SuppressWarnings("SerializableHasSerializationMethods")
public final class Point2D<T extends Number & Comparable<T>>
    implements Comparable<Point2D<T>>, Serializable {

  /**
   * If the type is Double or Float, then EPSILON can be used when
   * checking for equality.
   */
  public static final double EPSILON = 0.00000001;
  @Serial
  private static final long serialVersionUID = 1L;
  private final @NotNull T x;
  private final @NotNull T y;
  private final @NotNull String id;

  /**
   * Instantiates a new instances with the specified values.
   *
   * @param x   The X-Axis value.
   * @param y   The Y-Axis value.
   */
  public Point2D(@NotNull T x, @NotNull T y) {
    this.x = x;
    this.y = y;
    this.id = x + "," + y;
  }

  /**
   * Subtracts a point from this point and returns a new point with the result.
   *
   * @param o The point to subtract from this point.
   * @return A new point with the result.
   */
  public Point2D<T> minus(@NotNull Point2D<T> o) {
    return minus(this, o);
  }

  /**
   * Subtracts two points and returns a new point with the new result.
   *
   * @param first  The first point.
   * @param second The second point.
   * @param <E>    The type of the point axis values.
   * @return A new point with the result.
   */
  public static <E extends Number & Comparable<E>> Point2D<E> minus(
      @NotNull Point2D<E> first,
      @NotNull Point2D<E> second
  ) {
    return new Point2D<>(
        MathOperations.subtract(first.x, second.x), MathOperations.subtract(first.y, second.y)
    );
  }

  /**
   * Adds a point to this point and returns a new point with the result.
   *
   * @param o The point to add to this point.
   * @return A new point with the result.
   */
  public Point2D<T> plus(@NotNull Point2D<T> o) {
    return plus(this, o);
  }

  /**
   * Adds two points and returns a new point with the new result.
   *
   * @param first  The first point.
   * @param second The second point.
   * @param <E>    The type of the point axis values.
   * @return A new point with the result.
   */
  public static <E extends Number & Comparable<E>> Point2D<E> plus(
      @NotNull Point2D<E> first,
      @NotNull Point2D<E> second
  ) {
    return new Point2D<>(
        MathOperations.add(first.x, second.x), MathOperations.add(first.y, second.y)
    );
  }

  @Override
  public int compareTo(@NotNull Point2D<T> o) {
    var result = x.compareTo(o.x);

    if (result == 0) {
      result = y.compareTo(o.y);
    }

    return result;
  }

  /**
   * Returns a new Point2D in the direction from the specified point.
   *
   * @param point     The point to calculate the new point from.
   * @param direction The direction of the new point from the specified point.
   * @param <T>       The type of the coordinate values.
   * @return A new Point2D that is in the direction from the specified point.
   */
  @SuppressWarnings("unchecked")
  public static @NotNull <T extends Number & Comparable<T>> Point2D<T> pointInDirection(
      @NotNull Point2D<T> point,
      @NotNull Direction direction
  ) {
    return switch (point.x) {
      case Integer a when point.y instanceof Integer b -> new Point2D<>(
          (T) ((Integer) (a + direction.dx())),
          (T) ((Integer) (b + direction.dy()))
      );
      case Long a when point.y instanceof Long b ->
          new Point2D<>((T) ((Long) (a + direction.dx())), (T) ((Long) (b + direction.dy())));
      case Double a when point.y instanceof Double b ->
          new Point2D<>((T) ((Double) (a + direction.dx())), (T) ((Double) (b + direction.dy())));
      case Float a when point.y instanceof Float b ->
          new Point2D<>((T) ((Float) (a + direction.dx())), (T) ((Float) (b + direction.dy())));
      case BigInteger a when point.y instanceof BigInteger b -> new Point2D<>(
          (T) (a.add(BigInteger.valueOf(direction.dx()))),
          (T) (b.add(BigInteger.valueOf(direction.dy())))
      );
      case BigDecimal a when point.y instanceof BigDecimal b -> new Point2D<>(
          (T) (a.add(BigDecimal.valueOf(direction.dx()))),
          (T) (b.add(BigDecimal.valueOf(direction.dy())))
      );
      case AtomicInteger a when point.y instanceof AtomicInteger b -> new Point2D<>(
          (T) (new AtomicInteger(a.get() + direction.dx())),
          (T) (new AtomicInteger(b.get() + direction.dy()))
      );
      case AtomicLong a when point.y instanceof AtomicLong b -> new Point2D<>(
          (T) (new AtomicLong(a.get() + direction.dx())),
          (T) (new AtomicLong(b.get() + direction.dy()))
      );
      default -> throw new IllegalArgumentException("point is of an unknown type!");
    };
  }

  /**
   * Returns a new Point2D in the direction from this point.
   *
   * @param direction The direction of the new point from the specified point.
   * @return A new Point2D that is in the direction from this point.
   */
  public @NotNull Point2D<T> pointInDirection(Direction direction) {
    return pointInDirection(this, direction);
  }

  /**
   * Returns the Manhattan Distance between two points. The Manhattan Distance is the sum of
   * the absolute values of the coordinate differences. In other words it is
   * abs(a.x() - b.x()) + abs(a.y() - b.y())
   *
   * @param a   The first point.
   * @param b   The second point.
   * @param <T> The type of the coordinate values.
   * @return The Manhattan Distance between two points.
   */
  public static @NotNull <T extends Number & Comparable<T>> T manhattanDistance(
      Point2D<T> a,
      Point2D<T> b
  ) {
    return MathOperations.add(
        MathOperations.abs(MathOperations.subtract(a.x, b.x)),
        MathOperations.abs(MathOperations.subtract(a.y, b.y))
    );
  }

  /**
   * Returns the Manhattan Distance between two points. The Manhattan Distance is the sum of
   * the absolute values of the coordinate differences. In other words it is
   * abs(a.x() - b.x()) + abs(a.y() - b.y())
   *
   * @param other The point to get the distance from this point.
   * @return The Manhattan Distance between this point and the other point.
   */
  public @NotNull T manhattanDistance(Point2D<T> other) {
    return manhattanDistance(this, other);
  }

  /**
   * Returns the x element. Used by Kotlin to support decomposing assignments.
   *
   * @return The x element.
   */
  public T component1() {
    return x;
  }

  /**
   * Returns the y element. Used by Kotlin to support decomposing assignments.
   *
   * @return The y element.
   */
  public T component2() {
    return y;
  }

  /**
   * Returns the id element. Used by Kotlin to support decomposing assignments.
   *
   * @return The id element.
   */
  public String component3() {
    return id;
  }

  /**
   * Returns the x-ccordinate.
   *
   * @return The x-ccordinate.
   */
  public @NotNull T x() {
    return x;
  }

  /**
   * Returns the y-ccordinate.
   *
   * @return The y-ccordinate.
   */
  public @NotNull T y() {
    return y;
  }

  /**
   * Returns the ID of this point in x,y format.
   *
   * @return The ID of this point in x,y format.
   */
  public String id() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Point2D<?> point2D)) {
      return false;
    }
    return Objects.equals(x, point2D.x) && Objects.equals(y, point2D.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "Point2D["
        + "x=" + x + ", "
        + "y=" + y + ']';
  }
}
