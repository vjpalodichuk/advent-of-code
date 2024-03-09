package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.computer.SmallComputer
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.grid.WindingTubes
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 19: A Series of Tubes ---**
 *
 * Somehow, a network packet got lost and ended up here. It's trying to follow a routing
 * diagram (your puzzle input), but it's confused about where to go.
 *
 * Its starting point is just off the top of the diagram. Lines (drawn with |, -, and +)
 * show the path it needs to take, starting by going down onto the only line connected to
 * the top of the diagram. It needs to follow this path until it reaches the end
 * (located somewhere within the diagram) and stop there.
 *
 * Sometimes, the lines cross over each other; in these cases, it needs to continue going
 * the same direction, and only turn left or right when there's no other option.
 * In addition, someone has left **letters** on the line; these also don't change its direction,
 * but it can use them to keep track of where it's been. For example:
 *
 * ```
 *      |
 *      |  +--+
 *      A  |  C
 *  F---|----E|--+
 *      |  |  |  D
 *      +B-+  +--+
 * ```
 *
 * Given this diagram, the packet needs to take the following path:
 *
 * - Starting at the only line touching the top of the diagram, it must go down,
 * pass through A, and continue onward to the first +.
 * - Travel right, up, and right, passing through B in the process.
 * - Continue down (collecting C), right, and up (collecting D).
 * - Finally, go all the way left through E and stopping at F.
 *
 * Following the path to the end, the letters it sees on its path are ABCDEF.
 *
 * The little packet looks up at you, hoping you can help it find the way.
 * **What letters will it see** (in the order it would see them) if it follows the path?
 * (The routing diagram is very wide; make sure you view it without line wrapping.)
 *
 * Your puzzle answer was **MKXOIHZNBL**.
 *
 * **--- Part Two ---**
 *
 * The packet is curious how many steps it needs to go.
 *
 * For example, using the same routing diagram from the example above...
 *
 * ```
 *      |
 *      |  +--+
 *      A  |  C
 *  F---|--|-E---+
 *      |  |  |  D
 *      +B-+  +--+
 * ```
 *
 * ...the packet would go:
 *
 * - 6 steps down (including the first line at the top of the diagram).
 * - 3 steps right.
 * - 4 steps up.
 * - 3 steps right.
 * - 4 steps down.
 * - 3 steps right.
 * - 2 steps up.
 * - 13 steps left (including the F it stops on).
 *
 * This would result in a total of 38 steps.
 *
 * **How many steps** does the packet need to go?
 *
 * Your puzzle answer was **17872**.
 */
class Day19 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day19::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_19-01.txt"

  override fun runPart1(input: MutableList<String>) {
    val start = Instant.now()
    val answer = getLettersOnPath(input)
    val end = Instant.now()

    log.info("$answer is the letters and their order, encountered during the walk!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: MutableList<String>) {
    val start = Instant.now()
    val answer = getStepCountOfPath(input)
    val end = Instant.now()

    log.info("$answer is the number of steps to walk the path!")
    logTimings(log, start, end)
  }

  /**
   * Returns the letters encountered on the path in the order they were encountered.
   *
   * @param input The tube maze to parse and load.
   * @return The letters encountered on the path in the order they were encountered.
   */
  @SuppressFBWarnings
  fun getLettersOnPath(input: List<String>): String {
    val instance = WindingTubes(input)
    val builder = StringBuilder()
    instance.walk().second.map { it.first }.forEach { builder.append(it) }
    return builder.toString()
  }

  /**
   * Returns the number of steps needed in order to traverse the tube maze.
   *
   * @param input The tube maze to parse and load.
   * @return The number of steps needed in order to traverse the tube maze.
   */
  fun getStepCountOfPath(input: List<String>): Int {
    val instance = WindingTubes(input)
    return instance.walk().first
  }
}
