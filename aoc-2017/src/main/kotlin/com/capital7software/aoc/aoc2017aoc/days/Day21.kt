package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.grid.FractalArt
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 21: Fractal Art ---**
 *
 * You find a program trying to generate some art. It uses a strange process that
 * involves repeatedly enhancing the detail of an image through a set of rules.
 *
 * The image consists of a two-dimensional square grid of pixels that are either
 * on (#) or off (.). The program always begins with this pattern:
 *
 * ```
 * .#.
 * ..#
 * ###
 * ```
 *
 * Because the pattern is both 3 pixels wide and 3 pixels tall, it is said to have a **size** of 3.
 *
 * Then, the program repeats the following process:
 *
 * - If the size is evenly divisible by 2, break the pixels up into 2x2 squares, and
 * convert each 2x2 square into a 3x3 square by following the corresponding **enhancement rule**.
 * - Otherwise, the size is evenly divisible by 3; break the pixels up into 3x3 squares, and
 * convert each 3x3 square into a 4x4 square by following the corresponding **enhancement rule**.
 *
 * Because each square of pixels is replaced by a larger one, the image gains pixels
 * and so its **size** increases.
 *
 * The artist's book of enhancement rules is nearby (your puzzle input); however,
 * it seems to be missing rules. The artist explains that sometimes, one must **rotate**
 * or **flip** the input pattern to find a match. (Never rotate or flip the output
 * pattern, though.) Each pattern is written concisely: rows are listed as single
 * units, ordered top-down, and separated by slashes. For example, the following
 * rules correspond to the adjacent patterns:
 *
 * ```
 * ../.#  =  ..
 *           .#
 *
 *                 .#.
 * .#./..#/###  =  ..#
 *                 ###
 *
 *                         #..#
 * #..#/..../#..#/.##.  =  ....
 *                         #..#
 *                         .##.
 * ```
 *
 * When searching for a rule to use, rotate and flip the pattern as necessary.
 * For example, all the following patterns match the same rule:
 *
 * ```
 * .#.   .#.   #..   ###
 * ..#   #..   #.#   ..#
 * ###   ###   ##.   .#.
 * ```
 *
 * Suppose the book contained the following two rules:
 *
 * ```
 * ../.# => ##./#../...
 * .#./..#/### => #..#/..../..../#..#
 * ```
 *
 * As before, the program begins with this pattern:

 * ```
 * .#.
 * ..#
 * ###
 * ```
 *
 * The size of the grid (3) is not divisible by 2, but it is divisible by 3.
 * It divides evenly into a single square; the square matches the second rule, which produces:
 *
 * ```
 * #..#
 * ....
 * ....
 * #..#
 * ```
 *
 * The size of this enhanced grid (4) is evenly divisible by 2, so that rule
 * is used. It divides evenly into four squares:
 *
 * ```
 * #.|.#
 * ..|..
 * --+--
 * ..|..
 * #.|.#
 * ```
 *
 * Each of these squares matches the same rule (../.# => ##./#../...), three of
 * which require some flipping and rotation to line up with the rule. The output
 * for the rule is the same in all four cases:
 *
 * ```
 * ##.|##.
 * #..|#..
 * ...|...
 * ---+---
 * ##.|##.
 * #..|#..
 * ...|...
 * ```
 *
 * Finally, the squares are joined into a new grid:
 *
 * ```
 * ##.##.
 * #..#..
 * ......
 * ##.##.
 * #..#..
 * ......
 * ```
 *
 * Thus, after 2 iterations, the grid contains 12 pixels that are **on**.
 *
 * **How many pixels stay on** after 5 iterations?
 *
 * Your puzzle answer was **173**.
 *
 * **--- Part Two ---**
 *
 * **How many pixels stay on** after 18 iterations?
 *
 * Your puzzle answer was **2456178**.
 */
class Day21 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day21::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_21-01.txt"

  override fun runPart1(input: MutableList<String>) {
    val iterations = 5
    val start = Instant.now()
    val answer = getFractalCounts(input, iterations)
    val end = Instant.now()

    log.info("${answer.first} is number of pixels in the on position after $iterations iterations!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: MutableList<String>) {
    val iterations = 18
    val start = Instant.now()
    val answer = getFractalCounts(input, iterations)
    val end = Instant.now()

    log.info("${answer.first} is number of pixels in the on position after $iterations iterations!")
    logTimings(log, start, end)
  }

  /**
   * Returns the number of pixels that are in the on position of the [FractalArt] after the
   * specified number of iterations are performed.
   *
   * @param input The fractal generation rules.
   * @param iterations The number of iterations to perform.
   * @return The number of pixels that are in the on position of the [FractalArt] after the
   * specified number of iterations are performed.
   */
  @SuppressFBWarnings
  fun getFractalCounts(input: List<String>, iterations: Int): Pair<Int, Int> {
    val instance = FractalArt(input)
    instance.reset()
    instance.generate(iterations)
    return Pair(instance.onCount, instance.offCount)
  }

  /**
   * Returns the number of pixels that are in the on and off positions of the [FractalArt]
   * after the specified number of iterations are performed. The first element is the on count
   * and the second element is the off count.
   *
   * Logs the fractal as it evolves.
   *
   * @param input The fractal generation rules.
   * @param iterations The number of iterations to perform.
   * @return The number of pixels that are in the on and off positions of the [FractalArt]
   * after the specified number of iterations are performed. The first element is the on count
   * and the second element is the off count.
   */
  @SuppressFBWarnings
  fun logFractalAndCounts(input: List<String>, iterations: Int): Pair<Int, Int> {
    val instance = FractalArt(input)
    instance.reset()
    log.info(instance.print())
    var i = 0
    while (i < iterations) {
      instance.generate()
      log.info(instance.print())
      i++
    }
    return Pair(instance.onCount, instance.offCount)
  }
}
