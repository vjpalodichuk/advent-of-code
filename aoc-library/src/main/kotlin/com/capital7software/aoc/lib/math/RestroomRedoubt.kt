package com.capital7software.aoc.lib.math

import com.capital7software.aoc.lib.geometry.Point2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

typealias Position = Point2D<Int>
typealias Velocity = Point2D<Int>

/**
 * A bathroom security robot that has an initial position and velocity.
 *
 * @param id A unique identifier for the robot.
 * @param position The initial x, y coordinates of this Robot.
 * @param velocity The x, y velocity measured in tiles per second.
 */
data class RestroomRobot(val id: Int, val position: Position, val velocity: Velocity) {
  /**
   * Returns this Robot's absolute position after the specified number of [seconds] has elapsed
   *
   * @param seconds The time to find this Robot's position.
   * @return This Robot's absolute position after the elapsed time.
   */
  fun position(seconds: Int): Position {
    return Position(
        seconds * velocity.x() + position.x(),
        seconds * velocity.y() + position.y()
    )
  }

  /**
   * This Robot's toroidal position after the specified [seconds] based on the specified
   * [width] and [height]
   *
   * @param seconds The time to find this Robot's position.
   * @param width The width of the space this Robot is in.
   * @param height The height of the space this Robot is in.
   * @return This Robot's toroidal position after the elapsed time.
   */
  fun position(seconds: Int, width: Int, height: Int): Position {
    val prime = position(seconds)

    return Position(
        ((prime.x() % width) + width) % width,
        ((prime.y() % height) + height) % height
    )
  }
}

/**
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
 * @param width The number of tiles wide to restrict the space to.
 * @param height The number of tiles high to restrict the space to.
 * @param restroomRobots The [List] of [RestroomRobot]s that are confined to the specified space.
 */
@SuppressFBWarnings
class RestroomRedoubt private constructor(
    val width: Int,
    val height: Int,
    val restroomRobots: List<RestroomRobot>
) {
  /**
   * Provides the static [load] method.
   */
  companion object {
    private const val POSITION_X = "xpos"
    private const val POSITION_Y = "ypos"
    private const val VELOCITY_X = "xvel"
    private const val VELOCITY_Y = "yvel"

    private val RobotRegEx = """^p=(?<xpos>\d+),(?<ypos>\d+).+v=(?<xvel>-?\d+),(?<yvel>-?\d+)$"""
        .toRegex()

    /**
     * Parses the specified [inputs] into [RestroomRobot]s and confines them to a space of the specified
     * [width] and [height]
     *
     * @param inputs A [List] of [String]s to parse into [RestroomRobot]s.
     * @param width The number of tiles wide the space is.
     * @param height The number of tiles high the space is.
     * @return A [RestroomRedoubt] configured with the parsed [RestroomRobot]s and confined to the specified
     * [width] and [height]
     */
    fun load(inputs: List<String>, width: Int, height: Int): RestroomRedoubt {
      val restroomRobots = mutableListOf<RestroomRobot>()

      inputs.forEachIndexed { index, input ->
        val matchResult = RobotRegEx.matchEntire(input)

        if (matchResult != null) {
          val posX = matchResult.groups[POSITION_X]?.value?.toInt() ?: 0
          val posY = matchResult.groups[POSITION_Y]?.value?.toInt() ?: 0
          val velocityX = matchResult.groups[VELOCITY_X]?.value?.toInt() ?: 0
          val velocityY = matchResult.groups[VELOCITY_Y]?.value?.toInt() ?: 0

          restroomRobots.add(RestroomRobot(
              index,
              Position(posX, posY),
              Velocity(velocityX, velocityY)
          ))
        }
      }
      return RestroomRedoubt(width, height, restroomRobots)
    }
  }

  /**
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
   * @param seconds The time to determine the Safety Factor for.
   * @return The calculated Safety Factor.
   */
  fun safetyFactor(seconds: Int): Long {
    val ignoreColumn = width / 2
    val ignoreRow = height / 2
    val quadrants = restroomRobots.map { it.position(seconds, width, height) }
        .filter { it.x() != ignoreColumn && it.y() != ignoreRow }
        .map { position ->
          when {
            position.x() < ignoreColumn && position.y() < ignoreRow -> 0 to position
            position.x() > ignoreColumn && position.y() < ignoreRow -> 1 to position
            position.x() < ignoreColumn && position.y() > ignoreRow -> 2 to position
            position.x() > ignoreColumn && position.y() > ignoreRow -> 3 to position
            else -> error("Invalid position")
          }
        }
        .groupBy { it.first }

    return quadrants.map { it.value.size }.fold(1) { acc, num -> acc * num }
  }

  /**
   * During the bathroom break, someone notices that these robots seem awfully similar to ones built
   * and used at the North Pole. If they're the same type of robots, they should have a hard-coded
   * Easter egg: very rarely, most of the robots should arrange themselves into **a picture of a
   * Christmas tree**.
   *
   * @param startSeconds The second to start searching at.
   * @param maxSeconds The maximum amount of time to search for the Easter egg.
   * @return The minimum number of seconds to find the egg or -1 if no egg found in the allotted
   * time.
   */
  fun easterEgg(startSeconds: Int = 1, maxSeconds: Int): Int {
    var minSafetyFactor = Long.MAX_VALUE
    var minSeconds = -1

    for (i in startSeconds until maxSeconds) {
      val sf = safetyFactor(i)

      if (sf < minSafetyFactor) {
        minSafetyFactor = sf
        minSeconds = i
      }
    }
    return minSeconds
  }
}
