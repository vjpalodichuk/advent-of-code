package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.grid.HexEd
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 11: Hex Ed ---**
 *
 * Crossing the bridge, you've barely reached the other side of the stream when a
 * program comes up to you, clearly in distress. "It's my child process," she says,
 * "he's gotten lost in an infinite grid!"
 *
 * Fortunately for her, you have plenty of experience with infinite grids.
 *
 * Unfortunately for you, it's a hex grid.
 *
 * The hexagons ("hexes") in this grid are aligned such that adjacent hexes can be
 * found to the north, northeast, southeast, south, southwest, and northwest:
 *
 * ```
 *   \ n  /
 * nw +--+ ne
 *   /    \
 * -+      +-
 *   \    /
 * sw +--+ se
 *   / s  \
 * ```
 *
 * You have the path the child process took. Starting where he started, you need to
 * determine the fewest number of steps required to reach him. (A "step" means to
 * move from the hex you are in to any adjacent hex.)
 *
 * For example:
 *
 * - ne,ne,ne is 3 steps away.
 * - ne,ne,sw,sw is 0 steps away (back where you started).
 * - ne,ne,s,s is 2 steps away (se,se).
 * - se,sw,se,sw,sw is 3 steps away (s,s,sw).
 *
 * Your puzzle answer was **705**.
 *
 * **--- Part Two ---**
 *
 * **How many steps away** is the **furthest** he ever got from his starting position?
 *
 * Your puzzle answer was **1469**.
 */
class Day11 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day11::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_11-01.txt"

  override fun runPart1(input: MutableList<String>) {
    val start = Instant.now()
    val answer = fewestStepsToReachChild(input.first())
    val end = Instant.now()

    log.info("$answer is the fewest steps to reach the child process in the hex grid!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: MutableList<String>) {
    val start = Instant.now()
    val answer = furthestDistanceReached(input.first())
    val end = Instant.now()

    log.info("$answer is the furthest the child process got from the starting position!")
    logTimings(log, start, end)
  }

  /**
   * Returns the fewest number of steps to reach the child process.
   *
   * @param input The [String] path that the child process took.
   * @return The fewest number of steps to reach the child process.
   */
  fun fewestStepsToReachChild(input: String): Int {
    val instance = HexEd(input)
    return instance.stepsAndDistance.first
  }

  /**
   * Returns the furthest distance that the child process got from his starting position.
   *
   * @param input The [String] path that the child process took.
   * @return The furthest distance that the child process got from his starting position.
   */
  fun furthestDistanceReached(input: String): Int {
    val instance = HexEd(input)
    return instance.stepsAndDistance.second
  }
}
