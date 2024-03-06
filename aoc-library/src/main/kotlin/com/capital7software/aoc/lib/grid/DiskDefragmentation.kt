package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.crypt.KnotHash
import com.capital7software.aoc.lib.crypt.hexToBin
import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * **--- Day 14: Disk Defragmentation ---**
 *
 * Suddenly, a scheduled job activates the system's disk defragmenter.
 * Were the situation different, you might sit and watch it for a while, but today,
 * you just don't have that kind of time. It's soaking up valuable system resources
 * that are needed elsewhere, and so the only option is to help it finish its task
 * as soon as possible.
 *
 * The disk in question consists of a 128x128 grid; each square of the grid is
 * either **free** or **used**. On this disk, the state of the grid is tracked by the
 * bits in a sequence of knot hashes.
 *
 * A total of 128 knot hashes are calculated, each corresponding to a single
 * row in the grid; each hash contains 128 bits which correspond to individual
 * grid squares. Each bit of a hash indicates whether that square
 * is **free** (0) or **used** (1).
 *
 * The hash inputs are a key string (your puzzle input), a dash, and a number
 * from 0 to 127 corresponding to the row. For example, if your key string
 * were flqrgnkx, then the first row would be given by the bits of the knot
 * hash of flqrgnkx-0, the second row from the bits of the knot hash of flqrgnkx-1,
 * and so on until the last row, flqrgnkx-127.
 *
 * The output of a knot hash is traditionally represented by 32 hexadecimal digits;
 * each of these digits correspond to 4 bits, for a total of 4 * 32 = 128 bits.
 * To convert to bits, turn each hexadecimal digit to its equivalent binary value,
 * high-bit first: 0 becomes 0000, 1 becomes 0001, e becomes 1110, f becomes 1111,
 * and so on; a hash that begins with a0c2017... in hexadecimal would begin
 * with 10100000110000100000000101110000... in binary.
 *
 * Continuing this process, the **first 8 rows and columns** for key flqrgnkx appear
 * as follows, using # to denote used squares, and . to denote free ones:
 *
 * ```
 * ##.#.#..-->
 * .#.#.#.#
 * ....#.#.
 * #.#.##.#
 * .##.#...
 * ##..#..#
 * .#...#..
 * ##.#.##.-->
 * |      |
 * V      V
 * ```
 *
 * In this example, 8108 squares are used across the entire 128x128 grid.
 *
 * Now, all the defragmenter needs to know is the number of **regions**. A region is
 * a group of **used** squares that are all **adjacent**, not including diagonals. Every
 * used square is in exactly one region: lone used squares form their own isolated
 * regions, while several adjacent squares all count as a single region.
 *
 * In the example above, the following nine regions are visible, each marked with
 * a distinct digit:
 *
 * ```
 * 11.2.3..-->
 * .1.2.3.4
 * ....5.6.
 * 7.8.55.9
 * .88.5...
 * 88..5..8
 * .8...8..
 * 88.8.88.-->
 * |      |
 * V      V
 * ```
 *
 * Of particular interest is the region marked 8; while it does not appear contiguous
 * in this small view, all the squares marked 8 are connected when considering the
 * whole 128x128 grid. In total, in this example, 1242 regions are present.
 *
 * @param key The key used to generate the [KnotHash] for each row in the grid.
 */
class DiskDefragmentation(val key: String) {
  private companion object {
    private const val DEFAULT_LENGTH: Int = 128
    private const val HASH_LENGTH: Int = 32
  }

  private val grid: Grid2D<Boolean> = Grid2D(
      DEFAULT_LENGTH,
      DEFAULT_LENGTH,
      Array(DEFAULT_LENGTH * DEFAULT_LENGTH) { false }
  )

  /**
   * The results of defragmentation process. The first element in the [Pair] is the number of
   * used squares and the second element is the number of free squares.
   */
  val results: Pair<Int, Int> by lazy { defragmentation() }

  /**
   * The number of regions after the disk defragmentation.
   */
  val regions: Int by lazy { calculateRegions() }

  @SuppressFBWarnings
  private fun defragmentation(): Pair<Int, Int> {
    val hashes = (0..<DEFAULT_LENGTH).map { KnotHash("$key-$it").hash }
    val binHashes = hashes.map {
      check(it.length == HASH_LENGTH) { "$it is not $HASH_LENGTH in length!" }
      it.hexToBin()
    }

    binHashes.withIndex().forEach { (y, hash) ->
      hash.withIndex().forEach { (x, char) ->
        grid[x, y] = char == '1'
      }
    }

    val used = grid.count { it }
    val free = grid.size() - used

    return Pair(used, free)
  }

  private fun calculateRegions(): Int {
    val regionGrid = Grid2D(
        DEFAULT_LENGTH,
        DEFAULT_LENGTH,
        Array(DEFAULT_LENGTH * DEFAULT_LENGTH) { 0 }
    )
    if (results.first == 0) {
      return 0
    }

    var currentGroup = 0

    for (x in 0 ..< grid.columns) {
      for (y in 0 ..< grid.rows) {
        if (grid[x, y] && regionGrid[x, y] == 0) {
          markGroup(x, y, ++currentGroup, regionGrid)
        }
      }
    }

    return currentGroup
  }

  private fun markGroup(x: Int, y: Int, groupId: Int, regionGrid: Grid2D<Int>) {
    val queue = mutableListOf<Point2D<Int>>()

    queue.add(Point2D(x, y))
    regionGrid[x, y] = groupId

    while (queue.isNotEmpty()) {
      val point = queue.removeFirst()
      grid.getNeighbors(point, Direction.CARDINAL_DIRECTIONS) { value, point2D ->
        // Only include neighbors that are used and not in a group!
        value && regionGrid[point2D] == 0
      }.forEach { (direction, value) ->
        val newPoint = point.pointInDirection(direction)
        if (value) {
          regionGrid[newPoint] = groupId
          queue.add(newPoint)
        }
      }
    }
  }
}
