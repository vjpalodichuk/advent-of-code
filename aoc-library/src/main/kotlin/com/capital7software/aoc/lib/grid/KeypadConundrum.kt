package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.ArrayDeque

/**
 * Solver for keypad indirection problems where one keypad is controlled by another keypad.
 *
 * This implementation is intentionally reusable:
 * - [Keypad] is a generic finite keypad backed by [Grid2D].
 * - Movement is expressed with [Direction] and [Point2D].
 * - Every shortest path between keys is precomputed once per keypad.
 * - Sequence length expansion is memoized by keypad type, transition, and remaining controller depth.
 *
 * A path from one key to another is represented as the sequence of directional buttons required to
 * move there, followed by `A` to press the destination key.
 */
class KeypadConundrum private constructor(
    private val codes: List<String>
) {
  /**
   * Contains the [parse] static method to create this [KeypadConundrum].
   */
  @SuppressFBWarnings
  companion object {
    private const val GAP = ' '
    private const val ACTIVATE = 'A'

    private val DIRECTION_BUTTONS = mapOf(
        Direction.NORTH to '^',
        Direction.SOUTH to 'v',
        Direction.WEST to '<',
        Direction.EAST to '>'
    )

    private val NUMERIC_KEYPAD = Keypad(
        id = "numeric",
        rows = listOf(
            "789",
            "456",
            "123",
            " 0A"
        )
    )

    private val DIRECTIONAL_KEYPAD = Keypad(
        id = "directional",
        rows = listOf(
            " ^A",
            "<v>"
        )
    )

    /**
     * Parses the puzzle input.
     *
     * @param input The door codes.
     * @return A new [KeypadConundrum].
     */
    fun parse(input: List<String>): KeypadConundrum {
      return KeypadConundrum(input.filter { it.isNotBlank() })
    }

    /**
     * Solves part one.
     *
     * @param input The door codes.
     * @return The sum of complexities with two directional robots.
     */
    fun part1(input: List<String>): Long {
      return parse(input).sumOfComplexities(directionalRobots = 2)
    }

    /**
     * Solves part two.
     *
     * @param input The door codes.
     * @return The sum of complexities with twenty-five directional robots.
     */
    fun part2(input: List<String>): Long {
      return parse(input).sumOfComplexities(directionalRobots = 25)
    }
  }

  private val lengthCache = mutableMapOf<TransitionCacheKey, Long>()

  /**
   * Returns the sum of complexities for all door codes.
   *
   * @param directionalRobots The number of directional keypad robots between the human and the
   * numeric keypad robot. For part one this is `2`; for part two this is `25`.
   * @return The sum of complexities for all codes.
   */
  fun sumOfComplexities(directionalRobots: Int): Long {
    return codes.sumOf { code ->
      shortestHumanInputLength(code, directionalRobots) * numericPortion(code)
    }
  }

  /**
   * Returns the shortest number of buttons the human must press to cause [code] to be typed on the
   * numeric keypad.
   *
   * @param code The code to type on the numeric keypad.
   * @param directionalRobots The number of directional keypad robots above the numeric keypad robot.
   * @return The shortest human input length.
   */
  fun shortestHumanInputLength(code: String, directionalRobots: Int): Long {
    return sequenceLength(NUMERIC_KEYPAD, code, directionalRobots)
  }

  private fun sequenceLength(keypad: Keypad, sequence: String, controllersAbove: Int): Long {
    var current = ACTIVATE
    var total = 0L

    for (target in sequence) {
      total += transitionLength(keypad, current, target, controllersAbove)
      current = target
    }

    return total
  }

  private fun transitionLength(
      keypad: Keypad,
      from: Char,
      to: Char,
      controllersAbove: Int
  ): Long {
    val cacheKey = TransitionCacheKey(keypad.id, from, to, controllersAbove)

    return lengthCache.getOrPut(cacheKey) {
      val paths = keypad.shortestPressSequences(from, to)

      if (controllersAbove == 0) {
        paths.minOf { it.length.toLong() }
      } else {
        paths.minOf { path ->
          sequenceLength(DIRECTIONAL_KEYPAD, path, controllersAbove - 1)
        }
      }
    }
  }

  private fun numericPortion(code: String): Long {
    return code.filter { it.isDigit() }.toLong()
  }

  private data class TransitionCacheKey(
      val keypadId: String,
      val from: Char,
      val to: Char,
      val controllersAbove: Int
  )

  /**
   * A reusable keypad abstraction backed by [Grid2D].
   *
   * Gap cells are represented with a space and are never traversed.
   */
  private class Keypad(
      val id: String,
      rows: List<String>
  ) {
    private val grid: Grid2D<Char>
    private val positions: Map<Char, Point2D<Int>>
    private val shortestPaths: Map<Pair<Char, Char>, List<String>>

    init {
      require(rows.isNotEmpty()) { "A keypad must contain at least one row." }

      val width = rows.maxOf { it.length }
      val height = rows.size
      val items = Array(width * height) { GAP }

      rows.forEachIndexed { y, row ->
        row.forEachIndexed { x, value ->
          items[y * width + x] = value
        }
      }

      grid = Grid2D(width, height, items)
      positions = buildPositions()
      shortestPaths = buildShortestPaths()
    }

    fun shortestPressSequences(from: Char, to: Char): List<String> {
      return shortestPaths[Pair(from, to)]
          ?: error("No path exists from '$from' to '$to' on keypad '$id'.")
    }

    private fun buildPositions(): Map<Char, Point2D<Int>> {
      val answer = mutableMapOf<Char, Point2D<Int>>()

      for (y in 0 until grid.rows()) {
        for (x in 0 until grid.columns()) {
          val value = grid[x, y]

          if (value != GAP) {
            answer[value] = Point2D(x, y)
          }
        }
      }

      return answer
    }

    private fun buildShortestPaths(): Map<Pair<Char, Char>, List<String>> {
      val answer = mutableMapOf<Pair<Char, Char>, List<String>>()

      for (from in positions.keys) {
        for (to in positions.keys) {
          answer[Pair(from, to)] = findShortestPressSequences(from, to)
        }
      }

      return answer
    }

    @SuppressFBWarnings
    private fun findShortestPressSequences(from: Char, to: Char): List<String> {
      val start = positions[from] ?: error("Unknown key '$from' on keypad '$id'.")
      val finish = positions[to] ?: error("Unknown key '$to' on keypad '$id'.")
      val queue = ArrayDeque<PathState>()
      val bestDistance = mutableMapOf<Point2D<Int>, Int>()
      val paths = mutableListOf<String>()
      var shortest: Int? = null

      queue.add(PathState(start, ""))
      bestDistance[start] = 0

      while (queue.isNotEmpty()) {
        val state = queue.removeFirst()
        val currentShortest = shortest

        if (currentShortest != null && state.path.length > currentShortest) {
          continue
        }

        if (state.point == finish) {
          shortest = state.path.length
          paths.add(state.path + ACTIVATE)
          continue
        }

        for (direction in Direction.CARDINALS) {
          val next = state.point.pointInDirection(direction)

          if (!grid.isOnGrid(next) || grid[next] == GAP) {
            continue
          }

          val nextDistance = state.path.length + 1
          val knownBest = bestDistance[next]

          if (knownBest == null || nextDistance <= knownBest) {
            bestDistance[next] = nextDistance
            queue.add(PathState(next, state.path + DIRECTION_BUTTONS.getValue(direction)))
          }
        }
      }

      check(paths.isNotEmpty()) {
        "Unable to find a path from '$from' to '$to' on keypad '$id'."
      }

      return paths
    }

    private data class PathState(
        val point: Point2D<Int>,
        val path: String
    )
  }

}
