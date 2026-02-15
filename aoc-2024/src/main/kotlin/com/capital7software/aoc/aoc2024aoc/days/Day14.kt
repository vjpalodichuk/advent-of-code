package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.math.RestroomRedoubt
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.system.measureNanoTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * ***--- Day 14: Restroom Redoubt ---***
 *
 * One of The Historians needs to use the bathroom; fortunately, you know there's a bathroom near
 * an unvisited location on their list, and so you're all quickly teleported directly to the lobby
 * of Easter Bunny Headquarters.
 *
 * Unfortunately, EBHQ seems to have "improved" bathroom security **again** after your last
 * [visit](https://adventofcode.com/2016/day/2). The area outside the bathroom is swarming with
 * robots!
 *
 * To get The Historian safely to the bathroom, you'll need a way to predict where the robots will
 * be in the future. Fortunately, they all seem to be moving on the tile floor in predictable
 * **straight lines**.
 *
 * You make a list (your puzzle input) of all of the robots' current **positions** (p) and
 * **velocities** (v), one robot per line. For example:
 * ```
 * p=0,4 v=3,-3
 * p=6,3 v=-1,-3
 * p=10,3 v=-1,2
 * p=2,0 v=2,-1
 * p=0,0 v=1,3
 * p=3,0 v=-2,-2
 * p=7,6 v=-1,-3
 * p=3,0 v=-1,-2
 * p=9,3 v=2,3
 * p=7,3 v=-1,2
 * p=2,4 v=2,-3
 * p=9,5 v=-3,-3
 * ```
 * Each robot's position is given as `p=x,y` where `x` represents the number of tiles the robot is
 * from the left wall and `y` represents the number of tiles from the top wall (when viewed from
 * above). So, a position of `p=0,0` means the robot is all the way in the top-left corner.
 *
 * Each robot's velocity is given as `v=x,y` where `x` and `y` are given in **tiles per second**.
 * Positive `x` means the robot is moving to the **right**, and positive `y` means the robot is
 * moving **down**. So, a velocity of `v=1,-2` means that each second, the robot moves `1` tile to
 * the right and `2` tiles up.
 *
 * The robots outside the actual bathroom are in a space which is 11011 tiles wide and 11031 tiles
 * tall (when viewed from above). However, in this example, the robots are in a space which is only
 * `11` tiles wide and `7` tiles tall.
 *
 * The robots are good at navigating over/under each other (due to a combination of springs,
 * extendable legs, and quadcopters), so they can share the same tile and don't interact with each
 * other. Visually, the number of robots on each tile in this example looks like this:
 * ```
 * 1.12.......
 * ...........
 * ...........
 * ......11.11
 * 1.1........
 * .........1.
 * .......1...
 * ```
 * These robots have a unique feature for maximum bathroom security: they can **teleport**. When a
 * robot would run into an edge of the space they're in, they instead **teleport to the other
 * side**, effectively wrapping around the edges. Here is what robot `p=2,4 v=2,-3` does for the
 * first few seconds:
 * ```
 * Initial state:
 * ...........
 * ...........
 * ...........
 * ...........
 * ..1........
 * ...........
 * ...........
 *
 * After 1 second:
 * ...........
 * ....1......
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 *
 * After 2 seconds:
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 * ......1....
 * ...........
 *
 * After 3 seconds:
 * ...........
 * ...........
 * ........1..
 * ...........
 * ...........
 * ...........
 * ...........
 *
 * After 4 seconds:
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 * ..........1
 *
 * After 5 seconds:
 * ...........
 * ...........
 * ...........
 * .1.........
 * ...........
 * ...........
 * ...........
 * ```
 * The Historian can't wait much longer, so you don't have to simulate the robots for very long.
 * Where will the robots be after `100` seconds?
 *
 * In the above example, the number of robots on each tile after 100 seconds has elapsed looks like
 * this:
 * ```
 * ......2..1.
 * ...........
 * 1..........
 * .11........
 * .....1.....
 * ...12......
 * .1....1....
 * ```
 * To determine the safest area, count the **number of robots in each quadrant** after 100 seconds.
 * Robots that are exactly in the middle (horizontally or vertically) don't count as being in any
 * quadrant, so the only relevant robots are:
 * ```
 * ..... 2..1.
 * ..... .....
 * 1.... .....
 *
 * ..... .....
 * ...12 .....
 * .1... 1....
 * ```
 * In this example, the quadrants contain `1`, `3`, `4`, and `1` robot. Multiplying these together
 * gives a total **safety factor** of **`12`**.
 *
 * Predict the motion of the robots in your list within a space which is `101` tiles wide and `103`
 * tiles tall. **What will the safety factor be after exactly 100 seconds have elapsed?**
 *
 * Your puzzle answer was **`228457125`**.
 *
 * **--- Part Two ---**
 *
 * During the bathroom break, someone notices that these robots seem awfully similar to ones built
 * and used at the North Pole. If they're the same type of robots, they should have a hard-coded
 * Easter egg: very rarely, most of the robots should arrange themselves into **a picture of a
 * Christmas tree**.
 *
 * **What is the fewest number of seconds that must elapse for the robots to display the Easter
 * egg?**
 *
 * Your puzzle answer was **`6493`**.
 */
class Day14 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day14::class.java)
    private const val WIDTH = 101
    private const val HEIGHT = 103
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_14-01.txt"

  override fun runPart1(input: List<String>) {
    var answer: Long
    val elapsed = measureNanoTime {
      answer = safetyFactorAfterTime(input, WIDTH, HEIGHT, 100)
    }
    log.info("$answer is the Safety Factor after exactly 100 seconds have elapsed!")
    logTimings(log, elapsed)
  }

  override fun runPart2(input: List<String>) {
    var answer: Int
    val elapsed = measureNanoTime {
      answer = secondsToFindEasterEgg(input, WIDTH, HEIGHT, 7_000)
    }
    log.info("$answer is the minimum number of seconds to find the Easter egg!")
    logTimings(log, elapsed)
  }

  /**
   * Returns the Safety Factor after exactly .
   *
   * @param input The [List] of [String]s that represent the robots' position and velocity.
   * @param width The number of tiles wide the space is.
   * @param height The number of tiles high the space is.
   * @param seconds The time to find the safety factor for.
   * @return The Safety Factor after the exact number of seconds has elapsed.
   */
  @SuppressFBWarnings
  fun safetyFactorAfterTime(input: List<String>, width: Int, height: Int, seconds: Int): Long {
    val restroom = RestroomRedoubt.load(input, width, height)
    val result = restroom.safetyFactor(seconds)

    return result
  }

  /**
   * Returns the minimum number of seconds to find the Easter egg.
   *
   * @param width The number of tiles wide the space is.
   * @param height The number of tiles high the space is.
   * @param seconds The time to find the safety factor for.
   * @return The minimum number of seconds to find the Easter egg or -1 if no egg found.
   */
  fun secondsToFindEasterEgg(input: List<String>, width: Int, height: Int, seconds: Int): Int {
    val restroom = RestroomRedoubt.load(input, width, height)
    val result = restroom.easterEgg(maxSeconds = seconds)

    return result
  }
}
