package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.sqrt

/**
 * You come across an experimental new kind of memory stored on an infinite two-dimensional grid.
 *
 * @param cellWithData The square that contains the data.
 */
class SpiralMemory(private val cellWithData: Int) {
  private companion object {
    private const val MIN_LENGTH: Int = 20
  }

  /**
   * The number of steps required to move the data back to square 1.
   *
   * Each square on the grid is allocated in a spiral pattern starting at a location marked 1
   * and then counting up while spiraling outward. For example, the first
   * few squares are allocated like this:
   *
   * ```
   * 17  16  15  14  13
   * 18   5   4   3  12
   * 19   6   1   2  11
   * 20   7   8   9  10
   * 21  22  23---> ...
   * ```
   *
   * While this is very space-efficient (no squares are skipped), requested data must be
   * carried back to square 1 (the location of the only access port for this memory system)
   * by programs that can only move up, down, left, or right. They always take the
   * shortest path: the Manhattan Distance between the location of the data and square 1.
   *
   * For example:
   *
   * - Data from square 1 is carried 0 steps, since it's at the access port.
   * - Data from square 12 is carried 3 steps, such as: down, left, left.
   * - Data from square 23 is carried only 2 steps: up twice.
   * - Data from square 1024 must be carried 31 steps.
   */
  val steps: Int by lazy { stepsToMoveDataToSquare1() }

  /**
   * The first value written that is larger than [cellWithData].
   *
   * As a stress test on the system, the programs here clear the grid and then store the
   * value 1 in square 1. Then, in the same allocation order as shown above, they store
   * the sum of the values in all adjacent squares, including diagonals.
   *
   * So, the first few squares' values are chosen as follows:
   *
   * - Square 1 starts with the value 1.
   * - Square 2 has only one adjacent filled square (with value 1), so it also stores 1.
   * - Square 3 has both of the above squares as neighbors and stores the sum of their values, 2.
   * - Square 4 has all three of the aforementioned squares as neighbors and stores the sum
   * of their values, 4.
   * - Square 5 only has the first and fourth squares as neighbors, so it gets the value 5.
   *
   * Once a square is written, its value does not change. Therefore, the first few squares would
   * receive the following values:
   *
   * ```
   * 147  142  133  122   59
   * 304    5    4    2   57
   * 330   10    1    1   54
   * 351   11   23   25   26
   * 362  747  806--->   ...
   * ```
   */
  val first: Int by lazy { firstLargerThanData() }

  /**
   * The length of a side in this square grid based on the cell the data is in.
   *
   * ```
   * length x length = total number of cells.
   * ```
   */
  private val length: Int by lazy { ceil((sqrt(cellWithData.toDouble()))).toInt() }

  /**
   * The center of the grid based on the [length] which is dependent on the cell the data is in.
   */
  private val center: Int by lazy { ceil((length - 1) / 2.0).toInt() }

  private fun stepsToMoveDataToSquare1(): Int =
      max(0, center - 1 + abs(center - cellWithData % length))

  /**
   * Finds the first number that is written to the grid that is larger than [cellWithData].
   *
   * - Start by taking one step east and then turn left.
   * - Then take one step forward and turn left again.
   * - Now take two steps forward and then go left.
   * - Then take two steps forward and then go left again.
   * - Increase from two steps to three steps forward and turn left.
   * - Take three steps forward and turn left.
   * - Increase from three to four steps forward and turn left.
   *
   * Essentially, every forward-left-forward-left cycle, the number of steps
   * forward increases by one.
   *
   */
  private fun firstLargerThanData(): Int {
    val realLength = max(MIN_LENGTH, 2 * length) + 1 // Make it odd!
    val grid = Grid2D<Int>(realLength, realLength, Array(realLength * realLength) { 0 })
    val realCenter = realLength / 2
    var current = Point2D<Int>(realCenter, realCenter)
    var direction = Direction.EAST
    var steps = 0
    var stepsSinceNewDirection = 0
    var directionsSinceIncrease = 0
    var maxStepsForward = 1
    var lastWritten = 1

    // The value of 1 starts at the center.
    grid[current] = 1

    while (lastWritten <= cellWithData) {
      // start by taking a step in the current direction
      current = current.pointInDirection(direction)
      steps++
      stepsSinceNewDirection++

      // sum the neighbors
      lastWritten = grid.getNeighbors(current).sumOf { it.second() }

      // store it in the grid
      grid[current] = lastWritten

      // update direction if need be.
      if (stepsSinceNewDirection >= maxStepsForward) {
        direction = direction.left
        stepsSinceNewDirection = 0
        directionsSinceIncrease++
      }

      // Update max steps if need be.
      if (directionsSinceIncrease >= 2) {
        maxStepsForward++
        directionsSinceIncrease = 0
      }
    }

    return lastWritten
  }
}
