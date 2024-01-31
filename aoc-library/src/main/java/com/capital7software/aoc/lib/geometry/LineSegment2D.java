package com.capital7software.aoc.lib.geometry;

import com.capital7software.aoc.lib.math.MathOperations;
import com.capital7software.aoc.lib.util.Range;
import org.jetbrains.annotations.NotNull;

/**
 * A LineSegment2D represents a line between two Point2Ds. Supports points of various numeric
 * data types.
 *
 * @param start The start point of this LineSegment2D.
 * @param end   The end point of this LineSegment2D.
 * @param <T>   The type of the coordinates of the Point2Ds.
 */
public record LineSegment2D<T extends Number & Comparable<T>>(
    @NotNull Point2D<T> start,
    @NotNull Point2D<T> end
) {
  /**
   * Returns the length of this LineSegment2D
   * .
   *
   * @return The length of this LineSegment2D
   */
  public double length() {
    return LineSegment2D.length(start, end);
  }

  /**
   * Returns the length of the line represented by the specified Point2Ds.
   *
   * @param a   The first point in the line.
   * @param b   The second point in the line.
   * @param <T> The type of the coordinates in the Point2Ds.
   * @return The length of the line represented by the specified Point2Ds.
   */
  public static <T extends Number & Comparable<T>> double length(Point2D<T> a, Point2D<T> b) {
    return Math.sqrt(
        Math.pow(MathOperations.subtract(b.x(), a.x()).doubleValue(), 2)
            + Math.pow(MathOperations.subtract(b.y(), a.y()).doubleValue(), 2)
    );
  }

  /**
   * Returns the cross-product of the start and end points of this LineSegment2D.
   *
   * @return The cross-product of the start and end points of this LineSegment2D.
   */
  public double cross() {
    return cross(start, end);
  }

  /**
   * Returns the cross-product of the two specified Point2Ds.
   *
   * @param a   The first point.
   * @param b   The second point.
   * @param <T> The type of the coordinates in the Point2Ds.
   * @return The cross-product of the two specified Point2Ds.
   */
  public static <T extends Number & Comparable<T>> double cross(Point2D<T> a, Point2D<T> b) {
    return MathOperations.subtract(
        MathOperations.multiply(a.x(), b.y()),
        MathOperations.multiply(b.x(), a.y())
    ).doubleValue();
  }

  /**
   * When this LineSegment2D and other LineSegment2D are on the same line and overlap, the point
   * that overlaps is returned based on the following criteria.
   * <ol>
   *     <li>If other's start is on this LineSegment2D, other's start is returned.</li>
   *     <li>Else if other's end is on this LineSegment2D, other's end is returned.</li>
   *     <li>Else if this LineSegment2D is totally inside other, then this start is returned.</li>
   *     <li>Else null is returned.</li>
   * </ol>
   *
   * @param other The non-null LineSegment2D to test
   * @return The point of intersection or null if they don't intersect as defined above.
   */
  public Point2D<T> intersect(final LineSegment2D<T> other) {
    if (!isOnSameLine(other)) {
      return null;
    }

    if (isPointOnSegment(other.start)) {
      return other.start;
    } else if (isPointOnSegment(other.end)) {
      return other.end;
    } else if (other.isOnSegment(this)) {
      return start;
    } else {
      return null;
    }
  }

  /**
   * Assumes that other is collinear to this LineSegment2D and checks if other falls on
   * this LineSegment2D.
   *
   * @param other The other point to test
   * @return If other is on this LineSegment2D then true; else false.
   */
  public boolean isPointOnSegment(Point2D<T> other) {
    var thisLength = length();
    var startToOther = LineSegment2D.length(start, other);
    var otherToEnd = LineSegment2D.length(other, end);

    var difference = Math.abs(thisLength - (startToOther + otherToEnd));

    return difference >= 0 && difference < Point2D.EPSILON;
  }

  /**
   * Assumes that other is collinear to this LineSegment2D and checks if other falls completely
   * within this LineSegment2D.
   *
   * @param other The other segment to test
   * @return If other is fully within this LineSegment2D then true; else false.
   */
  public boolean isOnSegment(LineSegment2D<T> other) {
    var thisLength = length();

    var startToOther = LineSegment2D.length(start, other.start);
    var otherLength = other.length();
    var otherToEnd = LineSegment2D.length(other.end, end);

    var difference = Math.abs(thisLength - (startToOther + otherLength + otherToEnd));
    return difference >= 0 && difference < Point2D.EPSILON;
  }

  /**
   * Returns true if the start and end point of the other LineSegment2D is collinear to
   * this LineSegment2D.
   *
   * @param other The LineSegment2D to test against.
   * @return If the other LineSegment2D is collinear to this LineSegment2D.
   */
  public boolean isOnSameLine(final LineSegment2D<T> other) {
    return orientation(other.start) == Orientation2D.COLLINEAR
        && orientation(other.end) == Orientation2D.COLLINEAR;
  }

  /**
   * Returns the Orientation2D of other as it relates to this LineSegment2D.
   *
   * @param other The point the orientation is calculated in relation to
   * @return The Orientation2D
   */
  public Orientation2D orientation(Point2D<T> other) {
    double orientation = MathOperations.subtract(
        MathOperations.multiply(
            MathOperations.subtract(end.y(), start.y()), MathOperations.subtract(other.x(), end.x())
        ),
        MathOperations.multiply(
            MathOperations.subtract(end.x(), start.x()), MathOperations.subtract(other.y(), end.y())
        )
    ).doubleValue();

    if (Math.abs(orientation) >= 0 && orientation < Point2D.EPSILON) {
      return Orientation2D.COLLINEAR;
    }

    return orientation > 0 ? Orientation2D.CLOCKWISE : Orientation2D.COUNTERCLOCKWISE;
  }

  /**
   * Determines if this LineSegment2D is below the other LineSegment2D.
   *
   * @param other The LineSegment2D to compare this segment with
   * @return If this LineSegment2D is below the other LineSegment2D, along the X-axis and
   *     Y-axis only, true is returned; otherwise false is returned.
   */
  public boolean isBelow(LineSegment2D<T> other) {
    var otherX = new Range<>(other.start.x().longValue(), other.end.x().longValue() + 1);
    var otherY = new Range<>(other.start.y().longValue(), other.end.y().longValue() + 1);
    var rangeX = new Range<>(start.x().longValue(), end.x().longValue() + 1);
    var rangeY = new Range<>(start.y().longValue(), end.y().longValue() + 1);
    var inOtherX = otherX.contains(start.x().longValue()) || otherX.contains(end.x().longValue());
    var inOtherY = otherY.contains(start.y().longValue()) || otherY.contains(end.y().longValue());
    var otherInX = rangeX.contains(other.start.x().longValue())
        || rangeX.contains(other.end.x().longValue());
    var otherInY = rangeY.contains(other.start.y().longValue())
        || rangeY.contains(other.end.y().longValue());

    return (inOtherX || otherInX) && (inOtherY || otherInY);
  }
}
