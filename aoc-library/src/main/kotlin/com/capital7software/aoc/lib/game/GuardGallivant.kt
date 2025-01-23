package com.capital7software.aoc.lib.game

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.grid.Grid2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * The Historians use their fancy [device](https://adventofcode.com/2024/day/4) again, this time
 * to whisk you all away to the North Pole prototype suit manufacturing lab... in the year
 * [1518](https://adventofcode.com/2018/day/5)! It turns out that having direct access to history
 * is very convenient for a group of historians.
 *
 * You still have to be careful of time paradoxes, and so it will be important to avoid anyone
 * from 1518 while The Historians search for the Chief. Unfortunately, a single **guard** is
 * patrolling this part of the lab.
 *
 * Maybe you can work out where the guard will go ahead of time so that The Historians can
 * search safely?
 *
 * While The Historians begin working around the guard's patrol route, you borrow their fancy
 * device and step outside the lab. From the safety of a supply closet, you time travel through
 * the last few months and
 * [record](https://adventofcode.com/2018/day/4) the nightly status of the lab's guard post on
 * the walls of the closet.
 *
 * Returning after what seems like only a few seconds to The Historians, they explain that
 * the guard's patrol area is simply too large for them to safely search the lab without
 * getting caught.
 *
 * @param input [List] of [String]s that represents the room that the guard is in.
 */
class GuardGallivant(input: List<String>) {
  companion object {
    private val START_DIRECTION = Direction.NORTH
    private const val START_SYMBOL = '^'
    private const val OBSTACLE_SYMBOL = '#'
    private const val EMPTY_SYMBOL = '.'

    @SuppressFBWarnings
    private fun buildGrid(input: List<String>): Grid2D<Char> {
      return Grid2D.buildCharacterGrid(input)
    }

    private fun findStart(grid2D: Grid2D<Char>): Point2D<Int> {
      val result: Point2D<Int>? = grid2D.findFirst(START_SYMBOL).orElse(null)

      checkNotNull(result) { "Unable to find the start point!" }

      return result
    }

    private fun findObstacles(grid2D: Grid2D<Char>): List<Point2D<Int>> {
      val result: List<Point2D<Int>> = grid2D.findAll(OBSTACLE_SYMBOL)

      return result
    }

    private fun obstacleInPath(
        point: Point2D<Int>,
        direction: Direction,
        obstaclesByColumn: Map<Int, List<Point2D<Int>>>,
        obstaclesByRow: Map<Int, List<Point2D<Int>>>
    ): Boolean {
      return when (direction) {
        Direction.NORTH -> {
          obstaclesByColumn
              .getOrDefault(point.x(), emptyList())
              .firstOrNull { it.y() < point.y() } != null
        }
        Direction.EAST -> {
          obstaclesByRow
              .getOrDefault(point.y(), emptyList())
              .firstOrNull { it.x() > point.x() } != null
        }
        Direction.SOUTH -> {
          obstaclesByColumn
              .getOrDefault(point.x(), emptyList())
              .firstOrNull { it.y() > point.y() } != null
        }
        else -> {
          obstaclesByRow
              .getOrDefault(point.y(), emptyList())
              .firstOrNull { it.x() < point.x() } != null
        }
      }
    }
  }

  private val grid: Grid2D<Char> by lazy { buildGrid(input) }
  private val start: Point2D<Int> by lazy { findStart(grid) }
  private val obstacles: List<Point2D<Int>> by lazy { findObstacles(grid) }

  /**
   * Calculates and returns the number of distinct position points the guard visits before leaving
   * the area.
   *
   * An example map the guard patrols:
   *
   * ```
   * ....#.....
   * .........#
   * ..........
   * ..#.......
   * .......#..
   * ..........
   * .#..^.....
   * ........#.
   * #.........
   * ......#...
   * ```
   *
   * The map shows the current position of the guard with ```^``` (to indicate the guard is
   * currently facing **up** from the perspective of the map). Any **obstructions** - crates,
   * desks, alchemical reactors, etc. - are shown as ```#```.
   *
   * Lab guards in 1518 follow a very strict patrol protocol which involves repeatedly following
   * these steps:
   *
   * - If there is something directly in front of you, turn right 90 degrees.
   * - Otherwise, take a step forward.
   *
   * Following the above protocol, the guard moves up several times until she reaches an obstacle
   * (in this case, a pile of failed suit prototypes):
   *
   * ```
   * ....#.....
   * ....^....#
   * ..........
   * ..#.......
   * .......#..
   * ..........
   * .#........
   * ........#.
   * #.........
   * ......#...
   * ```
   *
   * Because there is now an obstacle in front of the guard, she turns right before continuing
   * straight in her new facing direction:
   *
   * ```
   * ....#.....
   * ........>#
   * ..........
   * ..#.......
   * .......#..
   * ..........
   * .#........
   * ........#.
   * #.........
   * ......#...
   * ```
   *
   * Reaching another obstacle (a spool of several **very** long polymers), she turns right again
   * and continues downward:
   *
   * ```
   * ....#.....
   * .........#
   * ..........
   * ..#.......
   * .......#..
   * ..........
   * .#......v.
   * ........#.
   * #.........
   * ......#...
   * ```
   *
   * This process continues for a while, but the guard eventually leaves the mapped area
   * (after walking past a tank of universal solvent):
   *
   * ```
   * ....#.....
   * .........#
   * ..........
   * ..#.......
   * .......#..
   * ..........
   * .#........
   * ........#.
   * #.........
   * ......#v..
   * ```
   *
   * By predicting the guard's route, you can determine which specific positions in the lab will
   * be in the patrol path. **Including the guard's starting position**, the positions visited by
   * the guard before leaving the area are marked with an ```X```:
   *
   * ```
   * ....#.....
   * ....ABBBB#
   * ....A...C.
   * ..#.A...C.
   * ..EFAFF#C.
   * ..E.A.G.C.
   * .#DD^DDDC.
   * .IJJJJGJ#.
   * #HHHHHGK..
   * ......#K..
   * ```
   *
   * In this example, the guard will visit **```41```** distinct positions on your map.
   *
   * @return The number of distinct position points the guard visits before leaving the area.
   */
  fun countDistinctGuardPositionsByPoint(): Int {
    return getGuardPositions(grid).distinctBy { it.point }.size
  }

  /**
   * Returns a [List] of [GuardStep]. If the returned list is empty or if the last
   * step is not an exit, then the specified [layout] is a loop.
   *
   * @param layout The [Grid2D] that contains the layout of the room.
   * @return A [List] of [GuardStep]. If the returned list is empty or if the last
   * step is not an exit, then the specified [layout] is a loop.
   */
  private fun getGuardPositions(layout: Grid2D<Char>): List<GuardStep> {
    var currentDirection = START_DIRECTION
    var currentPoint = start
    val visited = mutableListOf<GuardStep>()
    val stepSet = hashSetOf<GuardStep>()
    var onGrid = true
    var added = true

    while (onGrid && added) {
      val newPoint = currentPoint.pointInDirection(currentDirection)

      if (layout.isOnGrid(newPoint)) {
        if (OBSTACLE_SYMBOL == layout[newPoint]) {
          val step = GuardStep(currentPoint, currentDirection, turn = true)
          visited.add(step)
          added = stepSet.add(step)
          // Rotate 90 degrees right
          currentDirection = currentDirection.right
          continue
        } else {
          val step = GuardStep(currentPoint, currentDirection)
          visited.add(step)
          added = stepSet.add(step)
        }
        currentPoint = newPoint
      } else {
        onGrid = false
        val step = GuardStep(currentPoint, currentDirection, exit = true)
        visited.add(step)
        added = stepSet.add(step)
      }
    }
    return when (visited.last().exit) {
      true -> visited
      else -> visited.apply { removeLast() }
    }
  }

  /**
   * Returns a [List] of [Point2D] that represent the points where a new obstruction can be
   * placed so that the guard gets caught in a loop.
   *
   * Fortunately, they are **pretty sure** that adding a single new obstruction **won't** cause a
   * time paradox. They'd like to place the new obstruction in such a way that the guard will
   * get **stuck in a loop**, making the rest of the lab safe to search.
   *
   * To have the lowest chance of creating a time paradox, The Historians would like to know **all**
   * possible positions for such an obstruction. The new obstruction can't be placed at the
   * guard's starting position - the guard is there right now and would notice.
   *
   * In the above example, there are only **```6```** different positions where a new obstruction
   * would cause the guard to get stuck in a loop. The diagrams of these six situations use ```O```
   * to mark the new obstruction, ```|``` to show a position where the guard moves up/down, ```-```
   * to show a position where the guard moves left/right, and ```+``` to show a position where the
   * guard moves both up/down and left/right.
   *
   * Option one, put a printing press next to the guard's starting position:
   *
   * ```
   * ....#.....
   * ....+---+#
   * ....|...|.
   * ..#.|...|.
   * ....|..#|.
   * ....|...|.
   * .#.O^---+.
   * ........#.
   * #.........
   * ......#...
   * ```
   *
   * Option two, put a stack of failed suit prototypes in the bottom right quadrant of the
   * mapped area:
   *
   * ```
   * ....#.....
   * ....+---+#
   * ....|...|.
   * ..#.|...|.
   * ..+-+-+#|.
   * ..|.|.|.|.
   * .#+-^-+-+.
   * ......O.#.
   * #.........
   * ......#...
   * ```
   *
   * Option three, put a crate of chimney-squeeze prototype fabric next to the standing desk in
   * the bottom right quadrant:
   *
   * ```
   * ....#.....
   * ....+---+#
   * ....|...|.
   * ..#.|...|.
   * ..+-+-+#|.
   * ..|.|.|.|.
   * .#+-^-+-+.
   * .+----+O#.
   * #+----+...
   * ......#...
   * ```
   *
   * Option four, put an alchemical retroencabulator near the bottom left corner:
   *
   * ```
   * ....#.....
   * ....+---+#
   * ....|...|.
   * ..#.|...|.
   * ..+-+-+#|.
   * ..|.|.|.|.
   * .#+-^-+-+.
   * ..|...|.#.
   * #O+---+...
   * ......#...
   * ```
   *
   * Option five, put the alchemical retroencabulator a bit to the right instead:
   *
   * ```
   * ....#.....
   * ....+---+#
   * ....|...|.
   * ..#.|...|.
   * ..+-+-+#|.
   * ..|.|.|.|.
   * .#+-^-+-+.
   * ....|.|.#.
   * #..O+-+...
   * ......#...
   * ```
   * Option six, put a tank of sovereign glue right next to the tank of universal solvent:
   *
   * ```
   * ....#.....
   * ....+---+#
   * ....|...|.
   * ..#.|...|.
   * ..+-+-+#|.
   * ..|.|.|.|.
   * .#+-^-+-+.
   * .+----++#.
   * #+----++..
   * ......#O..
   * ```
   *
   * It doesn't really matter what you choose to use as an obstacle so long as you and The
   * Historians can put it into position without the guard noticing. The important thing is having
   * enough options that you can find one that minimizes time paradoxes, and in this example,
   * there are **```6```** different positions you could choose.
   *
   * @return A [List] of [Point2D] that represent the points where a new obstruction can be
   * placed so that the guard gets caught in a loop.
   */
  fun possibleNewObstructionPositions(): Set<Point2D<Int>> {
    val guardPositions = getGuardPositions(grid)
    check(guardPositions.last().exit) { "The Guard is already stuck in a loop!" }

    val obstaclesByColumn = obstacles.groupBy { it.x() }
    val obstaclesByRow = obstacles.groupBy { it.y() }
    val results = hashSetOf<Point2D<Int>>()

    /*
      For each guard position the following is performed:

      We  check if the next position along the path in the current direction is an obstacle or the
      start position. If it is, we continue to the next position; otherwise, we continue processing.

      We then check if there is an obstacle to the right of the current
      position. If there isn't, we continue to the next position; otherwise, we continue processing.

      We then place a new obstacle at the position directly in front of the current position.

      We then test if a loop is formed to trap the guard. If it isn't, the new obstacle is removed,
      and we continue to the next position; otherwise, we continue processing.

      We then add the position of the new obstacle to our result list.

      We then remove the new obstacle that was added and continue to the next position.
     */
    val gridClone = grid.copy()

    for (position in guardPositions) {
      val nextPoint = position.point.pointInDirection(position.direction)

      if (!gridClone.isOnGrid(nextPoint)
          || gridClone[nextPoint] == START_SYMBOL
          || gridClone[nextPoint] == OBSTACLE_SYMBOL
          ) {
        continue
      }

      val rightDirection = position.direction.right

      if (!obstacleInPath(position.point, rightDirection, obstaclesByColumn, obstaclesByRow)) {
        continue
      }

      gridClone[nextPoint] = OBSTACLE_SYMBOL

      val newPositions = getGuardPositions(gridClone)

      if (newPositions.isNotEmpty() && !newPositions.last().exit) {
        results.add(nextPoint)
      }

      gridClone[nextPoint] = EMPTY_SYMBOL
    }

    return results
  }
}

/**
 * Represents a single step that a Guard may take while on patrol.
 *
 * If [turn] is true, then the next step after this one causes a change in direction. This means
 * the point adjacent to this step's point in the same direction is some kind of obstacle.
 *
 * If [exit] is true, this means the next step in the same direction as this step would
 * leave the current area.
 *
 * @property point The [Point2D] in space where this guard step took place.
 * @property direction The [Direction] the step is in.
 * @property turn If set to true, then the cell directly in front of this one is an obstacle and
 * so a change in direction is required for the next step. Defaults to false.
 * @property exit If set to true, then the next step in the same direction as this step leaves
 * the current area. Defaults to false.
 */
data class GuardStep(
    val point: Point2D<Int>,
    val direction: Direction,
    val turn: Boolean = false,
    val exit: Boolean = false,
)
