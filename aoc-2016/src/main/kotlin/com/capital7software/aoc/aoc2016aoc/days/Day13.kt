package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.graph.path.Maze
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * --- Day 13: A Maze of Twisty Little Cubicles ---
 *
 * You arrive at the first floor of this new building to discover a much less welcoming
 * environment than the shiny atrium of the last one. Instead, you are in a maze of
 * twisty little cubicles, all alike.
 *
 * Every location in this area is addressed by a pair of non-negative integers (x,y).
 * Each such coordinate is either a wall or an open space. You can't move diagonally.
 * The cube maze starts at 0,0 and seems to extend infinitely toward **positive** x and y;
 * negative values are **invalid**, as they represent a location outside the building.
 * You are in a small waiting area at 1,1.
 *
 * While it seems chaotic, a nearby morale-boosting poster explains, the layout is
 * actually quite logical. You can determine whether a given x,y coordinate will
 * be a wall or an open space using a simple system:
 *
 * - Find ```x*x + 3*x + 2*x*y + y + y*y.```
 * - Add the office designer's favorite number (your puzzle input).
 * - Find the binary representation of that sum; count the **number** of bits that are 1.
 *     - If the number of bits that are 1 is **even**, it's an **open space.**
 *     - If the number of bits that are 1 is **odd**, it's a **wall.**
 *
 * For example, if the office designer's favorite number were 10, drawing walls as # and
 * open spaces as ., the corner of the building containing 0,0 would look like this:
 * ```
 *   0123456789
 * 0 .#.####.##
 * 1 ..#..#...#
 * 2 #....##...
 * 3 ###.#.###.
 * 4 .##..#..#.
 * 5 ..##....#.
 * 6 #...##.###
 * ```
 * Now, suppose you wanted to reach 7,4. The shortest route you could take is marked as O:
 * ```
 *   0123456789
 * 0 .#.####.##
 * 1 .O#..#...#
 * 2 #OOO.##...
 * 3 ###O#.###.
 * 4 .##OO#OO#.
 * 5 ..##OOO.#.
 * 6 #...##.###
 * ```
 * Thus, reaching 7,4 would take a minimum of 11 steps (starting from your current location, 1,1).
 *
 * What is the **fewest number of steps required** for you to reach 31,39?
 *
 * Your puzzle input is 1362.
 *
 * Your puzzle answer was 82.
 *
 * --- Part Two ---
 *
 * **How many locations** (distinct x,y coordinates, including your starting location)
 * can you reach in at most 50 steps?
 *
 * Your puzzle answer was 138.
 */
class Day13 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day13::class.java)
  }

  override fun getDefaultInputFilename(): String {
    return "inputs/input_day_13-01.txt"
  }

  override fun runPart1(input: List<String>) {
    val startX = 1L
    val startY = 1L
    val x = 31L
    val y = 39L
    val favorite = input.first().toLong()
    val start = Instant.now()
    val answer = fewestNumberOfSteps(startX, startY, x, y, favorite)
    val end = Instant.now()

    log.info("$answer is the fewest number of steps to reach $x,$y!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: List<String>) {
    val startX = 1L
    val startY = 1L
    val favorite = input.first().toLong()
    val limit = 50.0
    val start = Instant.now()
    val answer = distinctPointsReachedWithinLimit(startX, startY, favorite, limit)
    val end = Instant.now()

    log.info("$answer is the unique number of points reached within $limit steps!")
    logTimings(log, start, end)
  }

  /**
   * Finds the shortest path through the [Maze] from the start point through the finish point.
   *
   * @param startX The x-coordinate of the starting space.
   * @param startY The y-coordinate of the starting space.
   * @param finishX The x-coordinate of the ending space.
   * @param finishY The y-coordinate of the ending space.
   * @param favorite The designer's favorite number.
   * @return The fewest number of steps needed in order to reach the end space.
   */
  @SuppressFBWarnings
  fun fewestNumberOfSteps(
      startX: Long,
      startY: Long,
      finishX: Long,
      finishY: Long,
      favorite: Long
  ): Long {
    val maze = Maze.buildMaze(Point2D(startX, startY), Point2D(finishX, finishY), favorite)

    val shortestPath = maze.findShortestPath()

    return shortestPath.steps.toLong()
  }

  /**
   * Runs the specified program and returns the value of the specified register after the
   * program has run.
   *
   * @param startX The x-coordinate of the starting space.
   * @param startY The y-coordinate of the starting space.
   * @param favorite The designer's favorite number.
   * @param limit A path in the [Maze] will be explored until this many steps have been taken.
   * @return The number of distinct tiles encountered during the search.
   */
  fun distinctPointsReachedWithinLimit(
      startX: Long,
      startY: Long,
      favorite: Long,
      limit: Double
  ): Long {
    val maze = Maze.buildMaze(
        Point2D(startX, startY),
        Point2D((limit * 2).toLong(), (limit * 2).toLong()),
        favorite
    )

    val distinct = maze.findDistinct(limit)

    return distinct.size.toLong()
  }
}
