package com.capital7software.aoc.lib.geometry;

import com.capital7software.aoc.lib.math.MathOperations;
import org.jetbrains.annotations.NotNull;

/**
 * A 3D point where the axis values are of the specified type.
 *
 * @param x   The X-Axis value.
 * @param y   The Y-Axis value.
 * @param z   The z-Axis value.
 * @param <T> The type of the Axis values.
 */
public record Point3D<T extends Number & Comparable<T>>(@NotNull T x, @NotNull T y, @NotNull T z) {
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
                MathOperations.subtract(MathOperations.multiply(y, other.z), MathOperations.multiply(z, other.y)),
                MathOperations.subtract(MathOperations.multiply(z, other.x), MathOperations.multiply(x, other.z)),
                MathOperations.subtract(MathOperations.multiply(x, other.y), MathOperations.multiply(y, other.x))
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
        return (x.doubleValue() * other.x.doubleValue()) +
                (y.doubleValue() * other.y.doubleValue()) +
                (z.doubleValue() * other.z.doubleValue());
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
}
