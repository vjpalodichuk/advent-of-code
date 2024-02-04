package com.capital7software.aoc.lib.geometry

import com.capital7software.aoc.lib.util.Triple
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

private const val TRIANGLE_SIDES = 3

/**
 * The design document gives the side lengths of each triangle it describes,
 * but... 5 10 25? Some of these aren't triangles. You can't help but mark the impossible ones.
 *
 * In a valid triangle, the sum of any two sides must be larger than the remaining side.
 * For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
 *
 * In your puzzle input, how many of the listed triangles are possible?
 *
 * Now that you've helpfully marked up their design documents, it occurs to you that
 * triangles are specified in groups of three vertically. Each set of three numbers
 * in a column specifies a triangle. Rows are unrelated.
 *
 * For example, given the following specification, numbers with the same hundreds digit
 * would be part of the same triangle:
 *
 *    101 301 501
 *    102 302 502
 *    103 303 503
 *    201 401 601
 *    202 402 602
 *    203 403 603
 *
 * In your puzzle input, and instead reading by columns, how many of the listed
 * triangles are possible?
 */
@SuppressFBWarnings
object TriangleValidator {
  /**
   * Returns true if the sum of any two sides is greater than the remaining side. All pairs of
   * sides must pass this test
   *
   * @param a The length of the first side.
   * @param b The length of the second side.
   * @param c The length of the third side.
   * @return True if the sum of any two sides is greater than the remaining side.
   */
  fun <T : Number> isValid(a: T, b: T, c: T): Boolean {
    val doubleA = a.toDouble()
    val doubleB = b.toDouble()
    val doubleC = c.toDouble()

    return doubleA + doubleB > doubleC
        && doubleA + doubleC > doubleB
        && doubleB + doubleC > doubleA
  }

  /**
   * Returns a [List] of [Triple] where the properties of the [Triple] are the sides of
   * the triangles that are valid.
   *
   * @param input The [List] of [String] to parse and test for triangle.
   * @return A [List] of [Triple] where the properties of the [Triple] are the sides of
   * the triangles that are valid.
   */
  fun valid(input: List<String>, vertical: Boolean = false): List<Triple<Int, Int, Int>> {
    val valid = mutableListOf<Triple<Int, Int, Int>>()
    val tempA = mutableListOf<Int>()
    val tempB = mutableListOf<Int>()
    val tempC = mutableListOf<Int>()

    if (vertical) {
      input
          .forEach { line -> processVertically(line, valid, tempA, tempB, tempC) }
    } else {
      input
          .forEach { line -> processHorizontally(line, valid) }
    }

    return valid
  }

  private fun processHorizontally(
      line: String,
      valid: MutableList<Triple<Int, Int, Int>>,
  ) {
    val splitString = line.trim().split("\\s+".toRegex())
    val split = splitString.map { it.toInt() }
    val triple = Triple(split[0], split[1], split[2])
    if (isValid(triple.first(), triple.second(), triple.third())) {
      valid.add(triple)
    }
  }

  private fun processVertically(
      line: String,
      valid: MutableList<Triple<Int, Int, Int>>,
      tempA: MutableList<Int>,
      tempB: MutableList<Int>,
      tempC: MutableList<Int>
  ) {
    val splitString = line.trim().split("\\s+".toRegex())
    val split = splitString.map { it.toInt() }
    tempA.add(split[0])
    tempB.add(split[1])
    tempC.add(split[2])

    if (tempA.size == TRIANGLE_SIDES) {
      var triple = Triple(tempA[0], tempA[1], tempA[2])
      if (isValid(triple.first(), triple.second(), triple.third())) {
        valid.add(triple)
      }
      triple = Triple(tempB[0], tempB[1], tempB[2])
      if (isValid(triple.first(), triple.second(), triple.third())) {
        valid.add(triple)
      }
      triple = Triple(tempC[0], tempC[1], tempC[2])
      if (isValid(triple.first(), triple.second(), triple.third())) {
        valid.add(triple)
      }
      tempA.clear()
      tempB.clear()
      tempC.clear()
    }
  }
}
