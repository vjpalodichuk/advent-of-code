package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * **--- Day 19: A Series of Tubes ---**
 *
 * Somehow, a network packet got lost and ended up here. It's trying to follow a routing
 * diagram (your puzzle input), but it's confused about where to go.
 *
 * Its starting point is just off the top of the diagram. Lines (drawn with |, -, and +)
 * show the path it needs to take, starting by going down onto the only line connected to
 * the top of the diagram. It needs to follow this path until it reaches the end
 * (located somewhere within the diagram) and stop there.
 *
 * Sometimes, the lines cross over each other; in these cases, it needs to continue going
 * the same direction, and only turn left or right when there's no other option.
 * In addition, someone has left **letters** on the line; these also don't change its direction,
 * but it can use them to keep track of where it's been. For example:
 *
 * ```
 *      |
 *      |  +--+
 *      A  |  C
 *  F---|----E|--+
 *      |  |  |  D
 *      +B-+  +--+
 * ```
 *
 * Given this diagram, the packet needs to take the following path:
 *
 * - Starting at the only line touching the top of the diagram, it must go down,
 * pass through A, and continue onward to the first +.
 * - Travel right, up, and right, passing through B in the process.
 * - Continue down (collecting C), right, and up (collecting D).
 * - Finally, go all the way left through E and stopping at F.
 *
 * Following the path to the end, the letters it sees on its path are ABCDEF.
 *
 * @param grid2d The [Grid2D] instance that is loaded with the map to follow.
 */
class WindingTubes private constructor(grid2d: Grid2D<Char>) {
  private companion object {
    private const val VERTICAL_LINE = '|'
    private const val HORIZONTAL_LINE = '-'
    private const val CORNER = '+'
    private val LINES: Set<Char> = setOf(VERTICAL_LINE, HORIZONTAL_LINE, CORNER)
  }

  private val grid = grid2d

  /**
   * Instantiates a new [WindingTubes] instance loaded with the specified map.
   *
   * @param input The map to parse and load.
   */
  constructor(input: List<String>) : this(
      Grid2D<Char>(
          input.maxBy {it.length }.length,
          input.size,
          Array<Char>(input.maxBy {it.length }.length * input.size) { ' ' }
      )
  ) {
    input.withIndex().forEach { (y, value) ->
      value.toCharArray().withIndex().forEach { (x, value) ->
        grid[x, y] = value
      }
    }
  }

  /**
   * Walks the map and returns a [List] of the letters that were encountered in the order they
   * were encountered.
   *
   * @return A list of letters and the order they were encountered in.
   */
  fun walk(): Pair<Int, List<Pair<Char, Point2D<Int>>>> {
    val start = getStart() ?: return Pair(-1, emptyList())
    val answer: Pair<Int, List<Pair<Char, Point2D<Int>>>>?

    val direction = if (start.first == VERTICAL_LINE
        && isWalkable(grid[start.second.pointInDirection(Direction.SOUTH)])) {
      Direction.SOUTH
    } else if (isWalkable(grid[start.second.pointInDirection(Direction.WEST)])) {
      Direction.WEST
    } else {
      Direction.EAST
    }

    answer = walk(direction, start.first, start.second)

    return answer
  }

  @SuppressFBWarnings
  private fun walk(
      direction: Direction, space: Char, point: Point2D<Int>
  ): Pair<Int, List<Pair<Char, Point2D<Int>>>> {
    val visited = mutableListOf<Triple<Direction, Char, Point2D<Int>>>()
    val queue = ArrayDeque<Triple<Direction, Char, Point2D<Int>>>()

    queue.addLast(Triple(direction, space, point))

    while (queue.isNotEmpty()) {
      val item = queue.removeFirstOrNull() ?: break

      if (item in visited) {
        continue
      }

      visited.add(item)
      var potentialPoint = item.third.pointInDirection(item.first)
      val newItem: Triple<Direction, Char, Point2D<Int>>? =
          if (isWalkable(potentialPoint)) {
            Triple(item.first, grid[potentialPoint], potentialPoint)
          } else if (isWalkable(item.third.pointInDirection(item.first.left))) {
            potentialPoint = item.third.pointInDirection(item.first.left)
            Triple(item.first.left, grid[potentialPoint], potentialPoint)
          } else if (isWalkable(grid[item.third.pointInDirection(item.first.right)])) {
            potentialPoint = item.third.pointInDirection(item.first.right)
            Triple(item.first.right, grid[potentialPoint], potentialPoint)
          } else {
            null
          }

      newItem?.let {
        queue.addLast(it)
        it
      }
    }

    return Pair(
        visited.size,
        visited.filter { it.second.isLetter() }.map { Pair(it.second, it.third) }
    )
  }

  private fun isWalkable(point: Point2D<Int>): Boolean {
    return grid.isOnGrid(point) && isWalkable(grid[point])
  }

  private fun isWalkable(space: Char): Boolean {
    return space in LINES || space.isLetter()
  }

  private fun getStart(): Pair<Char, Point2D<Int>>? {
    for (x in 0..<grid.rows) {
      val space = grid[x, 0]
      if (isWalkable(space)) {
        return Pair(space, Point2D(x, 0))
      }
    }
    return null
  }
}
