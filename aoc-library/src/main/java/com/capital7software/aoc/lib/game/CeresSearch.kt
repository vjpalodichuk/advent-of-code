package com.capital7software.aoc.lib.game

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.grid.Grid2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * A class that contains the found word, the starting and ending points of where the word
 * was found within the word-find, and the direction of the word.
 *
 * @property target The word that was found.
 * @property start The [Point2D] that represents the coordinates where the first character of
 * the found word is located in the [CeresSearch] that contains the word-find.
 * @property end The [Point2D] that represent the coordinates where the last character of the
 * found word is located in the [CeresSearch] that contains the word-find.
 * @property direction The [Direction] the word was found in the Grid2D that contains the word-find.
 * The delta from the direction can be used to calculate the points of each character in the
 * found word in the Grid2D that contains the word-find.
 * @see CeresSearch
 * @see Direction
 * @see Point2D
 */
data class CeresSearchResult(
    val target: String,
    val start: Point2D<Int>,
    val end: Point2D<Int>,
    val direction: Direction,
)

/**
 * "Looks like the Chief's not here. Next!" One of The Historians pulls out a device and pushes
 * the only button on it. After a brief flash, you recognize the interior of the
 * [Ceres monitoring station](https://adventofcode.com/2019/day/10)!
 *
 * As the search for the Chief continues, a small Elf who lives on the station tugs on your shirt;
 * she'd like to know if you could help her with her **word search**.
 *
 * @param input The [List] of [String]s that make of the word-find grid. All strings must be of
 * equal length.
 */
class CeresSearch(input: List<String>) {
  private val grid: Grid2D<Char> = buildGrid(input)

  companion object {
    @SuppressFBWarnings
    private fun buildGrid(input: List<String>): Grid2D<Char> {
      assert(input.isNotEmpty()) { "The list of input strings cannot be empty" }

      val data = input.filter { it.isNotEmpty() }
          .flatMap { target -> target.toCharArray().toList() }

      val rows = input.size
      val columns = input[0].length
      val items = Array(rows * columns) { data[it] }
      return Grid2D<Char>(rows, columns, items)
    }
  }

  /**
   * This word search allows words to be horizontal, vertical, diagonal, written backwards, or
   * even overlapping other words. It's a little unusual, though, as you don't merely need to
   * find one instance of ```XMAS``` - you need to find **all of them**. Here are a few ways
   * ```XMAS``` might appear, where irrelevant characters have been replaced with ```.```:
   *
   * ```
   * ..X...
   * .SAMX.
   * .A..A.
   * XMAS.S
   * .X....
   * ```
   *
   * The actual word search will be full of letters instead. For example:
   *
   * ```
   * MMMSXXMASM
   * MSAMXMSMSA
   * AMXSXMAAMM
   * MSAMASMSMX
   * XMASAMXAMM
   * XXAMMXXAMA
   * SMSMSASXSS
   * SAXAMASAAA
   * MAMMMXMMMM
   * MXMXAXMASX
   * ```
   *
   * In this word search, ```XMAS``` occurs a total of **```18```** times; here's the same word
   * search again, but where letters not involved in any ```XMAS``` have been replaced with ```.```:
   *
   * ```
   * ....XXMAS.
   * .SAMXMS...
   * ...S..A...
   * ..A.A.MS.X
   * XMASAMX.MM
   * X.....XA.A
   * S.S.S.S.SS
   * .A.A.A.A.A
   * ..M.M.M.MM
   * .X.X.XMASX
   * ```
   * ```
   * @param target The word to find all instances of, in any [Direction], in this word-find.
   *
   * @return A set of the unique instances of the [target] found in this word-find.
   * @see CeresSearchResult
   * @see Direction
   */
  fun findAll(target: String = "XMAS"): Set<CeresSearchResult> {
    val found = hashSetOf<CeresSearchResult>()

    for (y in 0 until grid.rows) {
      for (x in 0 until grid.columns) {
        val point = Point2D(x, y)
        if (isStartOfTarget(point, target)) {
          val directions = getDirections(point, target)

          for (direction in directions) {
            val result = CeresSearchResult(
                target,
                point,
                calculateEndPointInDirection(point, direction, target),
                direction
            )

            if (!found.contains(result) && isFoundInDirection(point, direction, target)) {
              found.add(result)
            }
          }
        }
      }
    }
    return found
  }

  /**
   * This word search allows for finding pairs of the target word that form an X.
   * Using MAS as the target word, one way to achieve that is like this:
   *
   * ```
   * M.S
   * .A.
   * M.S
   * ```
   *
   * Irrelevant characters have again been replaced with ```.``` in the above diagram.
   * Within the ```X```, each ```MAS``` can be written forwards or backwards.
   *
   * Here's the same example from before, but this time all the ```X-MAS```es have been kept instead:
   *
   * ```
   * .M.S......
   * ..A..MSMS.
   * .M.S.MAA..
   * ..A.ASMSM.
   * .M.S.M....
   * ..........
   * S.S.S.S.S.
   * .A.A.A.A..
   * M.M.M.M.M.
   * ..........
   * ```
   *
   * In this example, an ```X-MAS``` appears **```9```** times.
   * ```
   * @param target The word to find all pairs that form an X in this word-find.
   * Please note that the number of characters in target should be odd in order for this to work
   * correctly.
   *
   * @return A set of the unique pairs of [target] found in this word-find that form an X.
   * @see CeresSearchResult
   */
  fun findAllX(target: String = "MAS"): Set<Set<CeresSearchResult>> {
    val foundAll = findAll(target)
    val foundX = hashSetOf<Set<CeresSearchResult>>()

    for (result in foundAll) {
      if (result.direction.isCardinal) {
        continue
      }

      val offset = result.target.length / 2
      val midX = result.start.x() + (offset * result.direction.dx())
      val midY = result.start.y() + (offset * result.direction.dy())
      for (direction in result.direction.perpendicular) {
        val aX = midX - (offset * direction.dx())
        val aY = midY - (offset * direction.dy())
        val pointA = Point2D(aX, aY)
        val pointB = calculateEndPointInDirection(pointA, direction, target)
        val resultA = CeresSearchResult(target, pointA, pointB, direction)

        if (foundAll.contains(resultA)) {
          val t = hashSetOf(result, resultA)
          foundX.add(t)
        }
      }
    }

    return foundX
  }

  @SuppressFBWarnings
  private fun getDirections(point: Point2D<Int>, target: String): Collection<Direction> {
    return Direction.ALL_DIRECTIONS.filter { fitsInDirection(point, it, target) }
  }

  private fun isFoundInDirection(
      start: Point2D<Int>,
      direction: Direction,
      target: String,
      reverse: Boolean = false,
  ): Boolean {
    var found = true
    val offset = target.length - 1
    var point = start

    for (i in target.indices) {
      val index = if (reverse) offset - i else i
      if (target[index] != grid[point]) {
          found = false
          break
      }
      if (i < offset) {
        point = Point2D.pointInDirection(point, direction)
      }
    }
    return found
  }

  private fun fitsInDirection(
      start: Point2D<Int>,
      direction: Direction,
      target: String
  ): Boolean {
    return grid.isOnGrid(calculateEndPointInDirection(start, direction, target))
  }

  private fun calculateEndPointInDirection(
      start: Point2D<Int>,
      direction: Direction,
      target: String
  ): Point2D<Int> {
    val delta = direction.delta()

    val newX = start.x() + (delta.x() * (target.length - 1))
    val newY = start.y() + (delta.y() * (target.length - 1))

    return Point2D(newX, newY)
  }

  private fun isStartOfTarget(point: Point2D<Int>, target: String): Boolean {
    return target.first() == grid.get(point)
  }
}
