package com.capital7software.aoc.lib.geometry;

import com.capital7software.aoc.lib.util.Range;
import org.jetbrains.annotations.NotNull;

/**
 * A LineSegment3D represents a line between two Point3Ds. Supports points of various numeric
 * data types.
 *
 * @param start The start point of this LineSegment2D.
 * @param end   The end point of this LineSegment2D.
 * @param <T>   The type of the coordinates of the Point2Ds.
 */
public record LineSegment3D<T extends Number & Comparable<T>>(
    @NotNull Point3D<T> start,
    @NotNull Point3D<T> end
) {
  /**
   * Returns the length of this LineSegment3D.
   *
   * @return The length of this LineSegment3D.
   */
  public @NotNull T length() {
    var newPoint = start.minus(end);

    return newPoint.sum();
  }

  /**
   * Returns the Orientation3D of this LineSegment3D.
   *
   * @return The Orientation3D of this LineSegment3D.
   */
  public @NotNull Orientation3D orientation() {
    var newPoint = start.minus(end);

    if (newPoint.x().doubleValue() != 0) {
      return Orientation3D.HORIZONTAL_X;
    } else if (newPoint.y().doubleValue() != 0) {
      return Orientation3D.HORIZONTAL_Y;
    } else if (newPoint.z().doubleValue() != 0) {
      return Orientation3D.VERTICAL_Z;
    } else {
      return Orientation3D.UNKNOWN;
    }
  }

  /**
   * Determines if this LineSegment3D is below the other LineSegment3D. This method does not
   * consider the Z-axis when making the determination. This is due to the fact that we are
   * only concerned for collisions along the X-axis and Y-axis.
   *
   * @param other The LineSegment3D to compare this segment with
   * @return If this LineSegment3D is below the other LineSegment3D, along the X-axis and
   *     Y-axis only, true is returned; otherwise false is returned.
   */
  public boolean isBelow(LineSegment3D<T> other) {
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
