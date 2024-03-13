package com.capital7software.aoc.lib.geometry

import com.capital7software.aoc.lib.math.MathOperations

/**
 * Represents a two-dimensional rectangle. The two points are the upper-left corner and the
 * lower right corner.
 *
 * Assumes that the y-axis increases from top to bottom.
 *
 * @param topLeft The upper-left hand corner of this rectangle.
 * @param bottomRight The bottom-right hand corner of this rectangle.
 */
data class Rectangle<T>(
    val topLeft: Point2D<T>,
    val bottomRight: Point2D<T>,
) where T : Number, T : Comparable<T> {
  /**
   * The width of this rectangle (inclusive).
   */
  val width: T by lazy {
    MathOperations.inc(
        MathOperations.abs(MathOperations.subtract(bottomRight.x(), topLeft.x()))
    )
  }

  /**
   * The height of this rectangle (inclusive).
   */
  val height: T by lazy {
    MathOperations.inc(
        MathOperations.abs(MathOperations.subtract(bottomRight.y(), topLeft.y()))
    )
  }

  /**
   * The area of this rectangle (width x height).
   */
  val area: T by lazy { MathOperations.multiply(width, height) }

  /**
   * Instantiates a new [Rectangle] with the specified coordinates and specified width and height.
   *
   * - The [x] and [y] coordinates represent the upper-left hand corner of this rectangle.
   * - The [width] minus one (1) is added to [x] to obtain the right coordinate.
   * - The [height] minus one (1) is added to [y] to obtain the bottom coordinate.
   *
   * @param x The left coordinate.
   * @param y The top coordinate.
   * @param width The length of the x-axis.
   * @param height The length of the y-axis.
   */
  constructor(x: T, y: T, width: T, height: T) : this(
      Point2D(x, y),
      Point2D(
          MathOperations.add(x, MathOperations.dec(width)),
          MathOperations.add(y, MathOperations.dec(height))
      )
  )

  /**
   * Returns the area of overlap as a new [Rectangle]. The rectangles must
   * overlap and not simply be adjacent in order for this to return a
   * non-null result.
   *
   * @param other The [Rectangle] to test for overlap with.
   * @return The area of overlap as a new [Rectangle] or null if there is no overlap.
   */
  fun overlap(other: Rectangle<T>): Rectangle<T>? {
    return if (topLeft.x() > other.bottomRight.x()) {
      null // Totally to the right of the other rectangle.
    } else if (bottomRight.x() < other.topLeft.x()) {
      null // Totally to the left of the other rectangle.
    } else if (topLeft.y() > other.bottomRight.y()) {
      null // Totally below the other rectangle.
    } else if (bottomRight.y() < other.topLeft.y()) {
      null // Totally above the other rectangle.
    } else {
      // Otherwise we have some kind of overlap.
      // Max of the left is the left coordinate of the overlap area.
      val x1 = maxOf(topLeft.x(), other.topLeft.x())
      // Min of the right is the right coordinate of the overlap area.
      val x2 = minOf(bottomRight.x(), other.bottomRight.x())
      // Max of the top is the top coordinate of the overlap area.
      val y1 = maxOf(topLeft.y(), other.topLeft.y())
      // Min of the bottom is the bottom coordinate of the overlap area.
      val y2 = minOf(bottomRight.y(), other.bottomRight.y())
      Rectangle(Point2D(x1, y1), Point2D(x2, y2))
    }
  }

  private fun maxOf(a: T, b: T): T {
    return if (a >= b) {
      a
    } else {
      b
    }
  }

  private fun minOf(a: T, b: T): T {
    return if (a <= b) {
      a
    } else {
      b
    }
  }
}
