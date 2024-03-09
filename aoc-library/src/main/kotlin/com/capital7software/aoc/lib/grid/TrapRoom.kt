package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Point2D

/**
 * As you enter this room, you hear a loud click! Some of the tiles in the floor here seem
 * to be pressure plates for traps, and the trap you just triggered has run out of...
 * whatever it tried to do to you. You doubt you'll be so lucky next time.
 *
 * Upon closer examination, the traps and safe tiles in this room seem to follow a pattern.
 * The tiles are arranged into rows that are all the same width; you take note of
 * the safe tiles (.) and traps (^) in the first row (your puzzle input).
 *
 * The type of tile (trapped or safe) in each row is based on the types of the tiles in
 * the same position, and to either side of that position, in the previous row.
 * (If either side is off either end of the row, it counts as "safe" because there
 * isn't a trap embedded in the wall.)
 *
 * For example, suppose you know the first row (with tiles marked by letters) and want
 * to determine the next row (with tiles marked by numbers):
 *
 * ABCDE
 * 12345
 *
 * The type of tile 2 is based on the types of tiles A, B, and C; the type of tile 5 is
 * based on tiles D, E, and an imaginary "safe" tile. Let's call these three tiles
 * from the previous row the **left**, **center**, and **right** tiles, respectively.
 * Then, a new tile is a **trap** only in one of the following situations:
 *
 * - Its **left** and **center** tiles are traps, but its **right** tile is not.
 * - Its **center** and **right** tiles are traps, but its **left** tile is not.
 * - Only its **left** tile is a trap.
 * - Only its **right** tile is a trap.
 *
 * In any other situation, the new tile is safe.
 *
 * Then, starting with the row ..^^., you can determine the next row by applying
 * those rules to each new tile:
 *
 * - The leftmost character on the next row considers the left (nonexistent, so we assume "safe"),
 * center (the first ., which means "safe"), and right (the second ., also "safe") tiles on the
 * previous row. Because all of the trap rules require a trap in at least one of the previous
 * three tiles, the first tile on this new row is also safe, ..
 * - The second character on the next row considers its left (.), center (.), and right (^)
 * tiles from the previous row. This matches the fourth rule: only the right tile is a trap.
 * Therefore, the next tile in this new row is a trap, ^.
 * - The third character considers .^^, which matches the second trap rule: its center and
 * right tiles are traps, but its left tile is not. Therefore, this tile is also a trap, ^.
 * - The last two characters in this new row match the first and third rules, respectively,
 * and so they are both also traps, ^.
 *
 * After these steps, we now know the next row of tiles in the room: .^^^^.
 * Then, we continue on to the next row, using the same rules, and get ^^..^.
 * After determining two new rows, our map looks like this:
 *
 * ```
 * ..^^.
 * .^^^^
 * ^^..^
 * ```
 * Here's a larger example with ten tiles per row and ten rows:
 *
 * ```
 * .^^.^.^^^^
 * ^^^...^..^
 * ^.^^.^.^^.
 * ..^^...^^^
 * .^^^^.^^.^
 * ^^..^.^^..
 * ^^^^..^^^.
 * ^..^^^^.^^
 * .^^^..^.^^
 * ^^.^^^..^^
 * ```
 * In ten rows, this larger example has 38 safe tiles.
 *
 * Starting with the map in your puzzle input, in a total of 40 rows
 * (including the starting row), **how many safe tiles** are there?
 *
 * @param grid2d The backing [Grid2D] for this [TrapRoom].
 */
class TrapRoom private constructor(grid2d: Grid2D<Char>) {
  private companion object {
    private val TRAPS = setOf("^^.", ".^^", "^..", "..^")
  }

  private val grid = grid2d

  /**
   * Instantiates a new [TrapRoom] starting with the specified row and computing the other rows
   * until the specified row count has been reached.
   *
   * @param input The [String] that contains the first row of the map.
   * @param rows The total number of rows this trap room will have (including the initial row).
   */
  constructor(input: String, rows: Int) : this(
    Grid2D<Char>(
      input.length,
      rows,
      Array<Char>(input.length * rows) { '_' })
  ) {
    // Copy the input string to the new grid.
    for (i in input.indices) {
      grid[Point2D(i, 0)] = input[i]
    }

    for (i in 0 ..< grid.rows - 1) {
      for (j in 0 ..< grid.columns) {
        grid[Point2D(j, i + 1)] = getNextTile(j, i)
      }
    }
  }

  private fun getNextTile(column: Int, row: Int): Char {
    val left = if (column == 0) '.' else grid[Point2D(column - 1, row)]
    val center = grid[Point2D(column, row)]
    val right = if (column >= grid.columns - 1) '.' else grid[Point2D(column + 1, row)]

    val test = "$left$center$right"

    return when (TRAPS.contains(test)) {
      true -> '^'
      else -> '.'
    }
  }

  /**
   * Returns the number of safe tiles.
   *
   * @return The number of safe tiles.
   */
  fun countSafeTiles(): Int {
    var count = 0

    for (item in grid) {
      if (item == '.') {
        count++
      }
    }

    return count
  }
}
