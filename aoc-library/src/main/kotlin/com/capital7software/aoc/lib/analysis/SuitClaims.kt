package com.capital7software.aoc.lib.analysis

import com.capital7software.aoc.lib.geometry.Rectangle
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings


/**
 * The Elves managed to locate the chimney-squeeze prototype fabric for Santa's suit
 * (thanks to someone who helpfully wrote its box IDs on the wall of the warehouse
 * in the middle of the night). Unfortunately, anomalies are still affecting them -
 * nobody can even agree on how to **cut** the fabric.
 *
 * The whole piece of fabric they're working on is a very large square -
 * at least 1000 inches on each side.
 *
 * Each Elf has made a **claim** about which area of fabric would be ideal for Santa's suit.
 * All claims have an ID and consist of a single rectangle with edges parallel to the edges
 * of the fabric. Each claim's rectangle is defined as follows:
 *
 * - The number of inches between the left edge of the fabric and the left edge of the rectangle.
 * - The number of inches between the top edge of the fabric and the top edge of the rectangle.
 * - The width of the rectangle in inches.
 * - The height of the rectangle in inches.
 *
 * A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a rectangle 3 inches from
 * the left edge, 2 inches from the top edge, 5 inches wide, and 4 inches tall. Visually,
 * it claims the square inches of fabric represented by # (and ignores the square inches
 * of fabric represented by .) in the diagram below:
 *
 * ```
 * ...........
 * ...........
 * ...#####...
 * ...#####...
 * ...#####...
 * ...#####...
 * ...........
 * ...........
 * ...........
 * ```
 *
 * The problem is that many of the claims **overlap**, causing two or more claims to cover part
 * of the same areas. For example, consider the following claims:
 *
 * ```
 * #1 @ 1,3: 4x4
 * #2 @ 3,1: 4x4
 * #3 @ 5,5: 2x2
 * ```
 *
 * Visually, these claim the following areas:
 *
 * ```
 * ........
 * ...2222.
 * ...2222.
 * .11XX22.
 * .11XX22.
 * .111133.
 * .111133.
 * ........
 * ```
 *
 * The four square inches marked with X are claimed by **both 1 and 2**. (Claim 3, while
 * adjacent to the others, does not overlap either of them.)
 *
 * Amidst the chaos, you notice that exactly one claim doesn't overlap by even a single
 * square inch of fabric with any other claim. If you can somehow draw attention to it,
 * maybe the Elves will be able to make Santa's suit after all!
 *
 * For example, in the claims above, only claim 3 is intact after all claims are made.
 *
 * @param input The list of claims to process.
 */
class SuitClaims @SuppressFBWarnings constructor(input: List<String>) {
  /**
   * A claim to section of the prototype fabric.
   *
   * @param id The ID of the claim.
   * @param rectangle The [Rectangle] that represents the area of the claim on the fabric.
   */
  data class Claim(val id: String, val rectangle: Rectangle<Int>) {
    /**
     * If the other claim overlaps this claim, then a new claim is returned with the overlapping
     * area as its rectangle. If there is no overlap, then null is returned.
     *
     * @param other The claim to test for overlap with.
     * @return The overlapping area or null if there is no overlap.
     */
    fun overlap(other: Claim): Claim? {
      val intersection = rectangle.overlap(other.rectangle)

      return if (intersection != null) {
        Claim("$id-${other.id}", intersection)
      } else {
        null
      }
    }

    /**
     * Returns a list of all points covered by the [Rectangle] that defines this [Claim].
     *
     * @return A list of all points covered by the [Rectangle] that defines this [Claim].
     */
    @SuppressFBWarnings
    fun area(): List<Pair<Int, Int>> {
      return (0 + rectangle.topLeft.x()..rectangle.bottomRight.x()).flatMap { width ->
        (0 + rectangle.topLeft.y()..rectangle.bottomRight.y()).map { height ->
          Pair(width, height)
        }
      }
    }
  }

  private val claims: List<Claim> = input.map { parseRectangle(it) }.sortedBy { it.rectangle.topLeft }

  /**
   * Calculates and returns the area of the overlapping claims.
   */
  val overlapArea: Int by lazy { calculateAreaOfOverlap(claims) }

  /**
   * Only the first claim that doesn't overlap any other claims is set here, if any.
   */
  val noOverlap: Claim? by lazy { claims.firstOrNull { claim ->
    claims.minus(claim).none { it.overlap(claim) != null }
  } }

  private companion object {
    private val EXTRACT_REGEX: Regex =
        "#(?<id>\\w+) @ (?<x>\\d+),(?<y>\\d+): (?<width>\\d+)x(?<height>\\d+)".toRegex()
    private const val ID: String = "id"
    private const val X_COORDINATE: String = "x"
    private const val Y_COORDINATE: String = "y"
    private const val WIDTH: String = "width"
    private const val HEIGHT: String = "height"

    @SuppressFBWarnings
    private fun calculateAreaOfOverlap(claims: List<Claim>): Int =
        claims
            .flatMap { it.area() }
            .groupBy { it }
            .count { it.value.size > 1 }

  }

  private fun parseRectangle(input: String): Claim {
    val match = EXTRACT_REGEX.find(input)
    check(match != null) { "Unable to parse $input into a Claim." }

    val id = match.groups[ID]!!.value
    val x = match.groups[X_COORDINATE]!!.value.toInt()
    val y = match.groups[Y_COORDINATE]!!.value.toInt()
    val width = match.groups[WIDTH]!!.value.toInt()
    val height = match.groups[HEIGHT]!!.value.toInt()

    return Claim(id, Rectangle(x, y, width, height))
  }
}
