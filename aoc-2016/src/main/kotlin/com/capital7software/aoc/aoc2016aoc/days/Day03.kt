package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.geometry.TriangleValidator
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * --- Day 3: Squares With Three Sides ---
 *
 * Now that you can think clearly, you move deeper into the labyrinth of hallways and
 * office furniture that makes up this part of Easter Bunny HQ. This must be a graphic
 * design department; the walls are covered in specifications for triangles.
 *
 * Or are they?
 *
 * The design document gives the side lengths of each triangle it describes,
 * but... 5 10 25? Some of these aren't triangles. You can't help but mark the impossible ones.
 *
 * In a valid triangle, the sum of any two sides must be larger than the remaining side.
 * For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
 *
 * In your puzzle input, **how many** of the listed triangles are **possible**?
 *
 * Your puzzle answer was 993.
 *
 * --- Part Two ---
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
 *
 * Your puzzle answer was 1849.
 *
 */
class Day03 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day03::class.java)
  }

  override fun getDefaultInputFilename(): String {
    return "inputs/input_day_03-01.txt"
  }

  override fun runPart1(input: List<String>) {
    val start = Instant.now()
    val answer = calculateValidTriangles(input)
    val end = Instant.now()

    log.info("{} is the number of valid triangles!", answer)
    logTimings(log, start, end)
  }

  override fun runPart2(input: List<String>) {
    val start = Instant.now()
    val answer = calculateValidTrianglesVertically(input)
    val end = Instant.now()

    log.info("{} is the number of valid triangles vertically!", answer)
    logTimings(log, start, end)
  }

  /**
   * Calculates and returns the bathroom code from the specified instructions.
   *
   * @param input The [List] of [String] which are the instructions to parse.
   * @return The bathroom code from the specified instructions.
   */
  fun calculateValidTriangles(input: List<String>): Int {
    return TriangleValidator.valid(input).size
  }

  /**
   * Calculates and returns the bathroom code from the specified instructions.
   *
   * @param input The [List] of [String] which are the instructions to parse.
   * @return The bathroom code from the specified instructions.
   */
  fun calculateValidTrianglesVertically(input: List<String>): Int {
    return TriangleValidator.valid(input, true).size
  }
}
