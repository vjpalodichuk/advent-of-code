package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * Models a two-dimensional memory space where bytes fall into known coordinates and corrupt them.
 *
 * The memory space is a bounded [Grid2D] whose valid coordinates are:
 *
 * - `0 <= x < width`
 * - `0 <= y < height`
 *
 * Movement is allowed only in the four cardinal directions, and corrupted memory tiles cannot be
 * entered.
 *
 * @param width The width of the memory space.
 * @param height The height of the memory space.
 * @param fallingBytes The ordered list of byte coordinates that will become corrupted.
 * @param bytesToSimulate The number of bytes from [fallingBytes] to apply before pathfinding.
 */
@SuppressFBWarnings
class RamRun private constructor(
    val width: Int,
    val height: Int,
    val fallingBytes: List<FallingByte>,
    val bytesToSimulate: Int
) {
  private val start: Point2D<Int> = Point2D(0, 0)
  private val exit: Point2D<Int> = Point2D(width - 1, height - 1)
  private val grid: Grid2D<MemoryTile> = buildGrid()

  /**
   * Factory helpers for [RamRun].
   */
  companion object {
    private val ByteRegex = """^(?<x>\d+),(?<y>\d+)$""".toRegex()

    /**
     * Parses the provided byte coordinates into a [RamRun].
     *
     * @param input The raw byte coordinates in `x,y` form.
     * @param width The width of the memory space.
     * @param height The height of the memory space.
     * @param bytesToSimulate The number of falling bytes to apply before pathfinding.
     * @return A [RamRun] initialized from [input].
     */
    fun load(
        input: List<String>,
        width: Int = 71,
        height: Int = 71,
        bytesToSimulate: Int = 1024
    ): RamRun {
      require(width > 0) { "Width must be greater than zero: $width" }
      require(height > 0) { "Height must be greater than zero: $height" }
      require(bytesToSimulate >= 0) {
        "The number of bytes to simulate cannot be negative: $bytesToSimulate"
      }

      val bytes = input
          .filter { it.isNotBlank() }
          .mapIndexed { index, line -> parseByte(index, line, width, height) }

      return RamRun(width, height, bytes, bytesToSimulate)
    }

    private fun parseByte(
        index: Int,
        line: String,
        width: Int,
        height: Int
    ): FallingByte {
      val match = ByteRegex.matchEntire(line)
          ?: error("Unable to parse falling byte coordinate: $line")

      val x = match.groups["x"]?.value?.toInt()
          ?: error("Unable to parse x coordinate from: $line")
      val y = match.groups["y"]?.value?.toInt()
          ?: error("Unable to parse y coordinate from: $line")
      val point = Point2D(x, y)

      require(x in 0 until width && y in 0 until height) {
        "Falling byte $point is outside the memory space ${width}x$height"
      }

      return FallingByte(index, point)
    }
  }

  /**
   * Returns the minimum number of cardinal steps needed to move from the top-left corner to the
   * bottom-right corner after the configured number of bytes have corrupted memory.
   *
   * If the exit cannot be reached, `-1` is returned.
   *
   * @return The minimum number of steps to reach the exit, or `-1` if unreachable.
   */
  fun minimumStepsToExit(): Int {
    if (grid[start].corrupted || grid[exit].corrupted) {
      return -1
    }

    val queue = ArrayDeque<PathState>()
    val visited = mutableSetOf<Point2D<Int>>()

    queue.add(PathState(start, 0))
    visited.add(start)

    while (queue.isNotEmpty()) {
      val current = queue.removeFirst()

      if (current.point == exit) {
        return current.steps
      }

      nextSafePoints(current.point)
          .filter { it !in visited }
          .forEach { point ->
            visited.add(point)
            queue.add(PathState(point, current.steps + 1))
          }
    }

    return -1
  }

  /**
   * Returns the first falling byte that prevents the exit from being reachable from the starting
   * position, or `null` if the path is still reachable after all bytes have fallen.
   *
   * @return The first [FallingByte] that cuts off the path to the exit, or `null`.
   */
  fun firstBlockingByteOrNull(): FallingByte? {
    if (fallingBytes.isEmpty() || isReachableAfter(fallingBytes.size)) {
      return null
    }

    var low = 0
    var high = fallingBytes.size

    while (low < high) {
      val mid = low + (high - low) / 2

      if (isReachableAfter(mid)) {
        low = mid + 1
      } else {
        high = mid
      }
    }

    return fallingBytes[low - 1]
  }

  /**
   * Returns the first falling byte that prevents the exit from being reachable as `x,y`, or an empty
   * string if the path is still reachable after all bytes have fallen.
   *
   * @return The coordinate of the first blocking byte as `x,y`, or an empty string.
   */
  fun firstBlockingByteCoordinateOrEmpty(): String {
    val byte = firstBlockingByteOrNull() ?: return ""

    return "${byte.point.x()},${byte.point.y()}"
  }

  private fun isReachableAfter(byteCount: Int): Boolean {
    return RamRun(
        width = width,
        height = height,
        fallingBytes = fallingBytes,
        bytesToSimulate = byteCount
    ).minimumStepsToExit() >= 0
  }

  private fun buildGrid(): Grid2D<MemoryTile> {
    val tiles = Array(width * height) { index ->
      val x = index % width
      val y = index / width

      MemoryTile(Point2D(x, y))
    }
    val answer = Grid2D(width, height, tiles)

    fallingBytes
        .take(bytesToSimulate)
        .forEach { fallingByte ->
          answer[fallingByte.point] = MemoryTile(fallingByte.point, corrupted = true)
        }

    return answer
  }

  private fun nextSafePoints(point: Point2D<Int>): List<Point2D<Int>> {
    return Direction.CARDINALS
        .map { direction -> point.pointInDirection(direction) }
        .filter { next -> grid.isOnGrid(next) }
        .filter { next -> !grid[next].corrupted }
  }

  private data class PathState(
      val point: Point2D<Int>,
      val steps: Int
  )
}

/**
 * A byte that falls into the memory space and corrupts its [point].
 *
 * @param order The zero-based order in which this byte falls.
 * @param point The coordinate corrupted by this byte.
 */
data class FallingByte(
    val order: Int,
    val point: Point2D<Int>
)

/**
 * A single tile in the memory space.
 *
 * @param point The coordinate of this tile.
 * @param corrupted If true, this tile cannot be entered.
 */
data class MemoryTile(
    val point: Point2D<Int>,
    val corrupted: Boolean = false
)
