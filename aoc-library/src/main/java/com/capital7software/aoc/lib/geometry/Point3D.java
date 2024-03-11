package com.capital7software.aoc.lib.geometry;

import com.capital7software.aoc.lib.math.MathOperations;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * A 3D point where the axis values are of the specified type.
 *
 * @param <T> The type of the Axis values.
 */
@SuppressWarnings("SerializableHasSerializationMethods")
public final class Point3D<T extends Number & Comparable<T>>
    implements Comparable<Point3D<T>>, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final @NotNull T x;
  private final @NotNull T y;
  private final @NotNull T z;
  private final @NotNull String id;

  /**
   * Instantiates a new instances with the specified values.
   *
   * @param x The X-Axis value.
   * @param y The Y-Axis value.
   * @param z The z-Axis value.
   */
  public Point3D(@NotNull T x, @NotNull T y, @NotNull T z) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.id = x + "," + y + "," + z;
  }

  /**
   * Subtracts the other point from this point and returns the result as a new 3D point.
   * This method differs from subtract as it returns the absolute value of the difference
   * while the subtract method does not.
   *
   * @param other The point to subtract from this point.
   * @return A new point
   */
  public @NotNull Point3D<T> minus(@NotNull Point3D<T> other) {
    T newX = MathOperations.abs(MathOperations.subtract(x, other.x));
    T newY = MathOperations.abs(MathOperations.subtract(y, other.y));
    T newZ = MathOperations.abs(MathOperations.subtract(z, other.z));

    return new Point3D<>(newX, newY, newZ);
  }

  /**
   * Adds to points and returns a new Point3D with the result.
   *
   * @param other The point to add to this point.
   * @return A new point
   */
  public @NotNull Point3D<T> plus(@NotNull Point3D<T> other) {
    return new Point3D<>(
        MathOperations.add(x, other.x),
        MathOperations.add(y, other.y),
        MathOperations.add(z, other.z)
    );
  }

  /**
   * Subtracts the other point from this point and returns the result as a new 3D point.
   * This method differs from minus as minus uses the absolute value of the difference
   * while this method does not.
   *
   * @param other The point to subtract from this point.
   * @return A new point
   */
  public @NotNull Point3D<T> subtract(@NotNull Point3D<T> other) {
    return new Point3D<>(
        MathOperations.subtract(x, other.x),
        MathOperations.subtract(y, other.y),
        MathOperations.subtract(z, other.z)
    );
  }

  /**
   * The cross-product between this point and the other point.
   *
   * @param other THe point to calculate the cross-product with.
   * @return A new point that is the result of the cross-product.
   */
  public @NotNull Point3D<T> cross(@NotNull Point3D<T> other) {
    return new Point3D<>(
        MathOperations.subtract(MathOperations.multiply(y, other.z),
                                MathOperations.multiply(z, other.y)),
        MathOperations.subtract(MathOperations.multiply(z, other.x),
                                MathOperations.multiply(x, other.z)),
        MathOperations.subtract(MathOperations.multiply(x, other.y),
                                MathOperations.multiply(y, other.x))
    );
  }

  /**
   * Returns the dot-product between this point and the other point.
   * A long is returned to avoid any overflow.
   *
   * @param other The point to calculate the dot-product with.
   * @return A long that represents the dot-product.
   */
  public double dot(@NotNull Point3D<T> other) {
    return (x.doubleValue() * other.x.doubleValue())
        + (y.doubleValue() * other.y.doubleValue()) + (z.doubleValue() * other.z.doubleValue());
  }

  /**
   * Returns true if this point and the other point are linearly independent of one another.
   *
   * @param other The point to test against.
   * @return True if this point and the other point are linearly independent of one another.
   */
  public boolean isLinearIndependent(@NotNull Point3D<T> other) {
    var point = cross(other);

    return point.x.doubleValue() != 0 || point.y.doubleValue() != 0 || point.z.doubleValue() != 0;
  }

  /**
   * Linearizes the specified points and returns a new Point3D with the result.
   *
   * @param va  The first coefficient.
   * @param pa  The first point.
   * @param vb  The second coefficient.
   * @param pb  The second point.
   * @param vc  The third coefficient.
   * @param pc  The third point.
   * @param <T> The type of the coordinates and coefficients.
   * @return A new linearized Point3D.
   */
  public static <T extends Number & Comparable<T>> @NotNull Point3D<T> linearize(
      T va,
      @NotNull Point3D<T> pa,
      T vb,
      @NotNull Point3D<T> pb,
      T vc,
      @NotNull Point3D<T> pc
  ) {
    var x = MathOperations.add(
        MathOperations.add(
            MathOperations.multiply(va, pa.x),
            MathOperations.multiply(vb, pb.x)
        ),
        MathOperations.multiply(vc, pc.x)
    );
    var y = MathOperations.add(
        MathOperations.add(
            MathOperations.multiply(va, pa.y),
            MathOperations.multiply(vb, pb.y)
        ),
        MathOperations.multiply(vc, pc.y)
    );
    var z = MathOperations.add(
        MathOperations.add(
            MathOperations.multiply(va, pa.z),
            MathOperations.multiply(vb, pb.z)
        ),
        MathOperations.multiply(vc, pc.z)
    );

    return new Point3D<>(x, y, z);
  }

  /**
   * Returns the sum of the coordinates that make up this Point3D.
   *
   * @return The sum of the coordinates that make up this Point3D.
   */
  public @NotNull T sum() {
    return sum(this);
  }

  /**
   * Returns the sum of the coordinates of the specified Point3D.
   *
   * @param point The point to sum.
   * @param <T>   The type of the coordinates of the Point3D.
   * @return The sum of the coordinates of the specified Point3D.
   */
  public static <T extends Number & Comparable<T>> @NotNull T sum(Point3D<T> point) {
    return MathOperations.add(MathOperations.add(point.x, point.y), point.z);
  }

  /**
   * Returns the absolute sum of the coordinates of this Point3D.
   * <br><br>
   * <code>
   * abs(x) + abs(y) + abs(z)
   * </code>>
   *
   * @return The sum of the coordinates of the specified Point3D.
   */
  public @NotNull T abssum() {
    return abssum(this);
  }

  /**
   * Returns the absolute sum of the coordinates of the specified Point3D.
   * <br><br>
   * <code>
   * abs(x) + abs(y) + abs(z)
   * </code>>
   *
   * @param point The point to sum.
   * @param <T>   The type of the coordinates of the Point3D.
   * @return The sum of the coordinates of the specified Point3D.
   */
  public static <T extends Number & Comparable<T>> @NotNull T abssum(Point3D<T> point) {
    return MathOperations.add(
        MathOperations.add(
            MathOperations.abs(point.x),
            MathOperations.abs(point.y)
        ),
        MathOperations.abs(point.z)
    );
  }

  /**
   * Returns the Manhattan Distance between two points. The Manhattan Distance is the sum of
   * the absolute values of the coordinate differences. In other words it is
   * abs(a.x() - b.x()) + abs(a.y() - b.y()) + abs(a.z() - b.z())
   *
   * @param a   The first point.
   * @param b   The second point.
   * @param <T> The type of the coordinate values.
   * @return The Manhattan Distance between two points.
   */
  public static @NotNull <T extends Number & Comparable<T>> T manhattanDistance(
      Point3D<T> a,
      Point3D<T> b
  ) {
    return MathOperations.add(
        MathOperations.add(
            MathOperations.abs(MathOperations.subtract(a.x, b.x)),
            MathOperations.abs(MathOperations.subtract(a.y, b.y))
        ),
        MathOperations.abs(MathOperations.subtract(a.z, b.z))
    );
  }

  /**
   * Returns the Manhattan Distance between two points. The Manhattan Distance is the sum of
   * the absolute values of the coordinate differences. In other words it is
   * abs(a.x() - b.x()) + abs(a.y() - b.y()) + abs(a.z() - b.z())
   *
   * @param other The point to get the distance from this point.
   * @return The Manhattan Distance between this point and the other point.
   */
  public @NotNull T manhattanDistance(Point3D<T> other) {
    return manhattanDistance(this, other);
  }

  @Override
  public int compareTo(@NotNull Point3D<T> o) {
    var result = minus(o);

    return result.x.compareTo(result.y);
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
   * Returns the z element. Used by Kotlin to support decomposing assignments.
   *
   * @return The z element.
   */
  public T component3() {
    return z;
  }

  /**
   * Returns the id element. Used by Kotlin to support decomposing assignments.
   *
   * @return The id element.
   */
  public String component4() {
    return id;
  }

  /**
   * Returns the x-coordinate.
   *
   * @return The x-coordinate.
   */
  public @NotNull T x() {
    return x;
  }

  /**
   * Returns the y-coordinate.
   *
   * @return The y-coordinate.
   */
  public @NotNull T y() {
    return y;
  }

  /**
   * Returns the z-coordinate.
   *
   * @return The z-coordinate.
   */
  public @NotNull T z() {
    return z;
  }

  /**
   * Returns the ID of this point in x,y,z format.
   *
   * @return The ID of this point in x,y,z format.
   */
  public String id() {
    return x + "," + y + "," + z;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Point3D<?> point3D)) {
      return false;
    }
    return Objects.equals(x, point3D.x)
        && Objects.equals(y, point3D.y) && Objects.equals(z, point3D.z);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    return "Point3D["
        + "x=" + x + ", "
        + "y=" + y + ", "
        + "z=" + z + ']';
  }

}
