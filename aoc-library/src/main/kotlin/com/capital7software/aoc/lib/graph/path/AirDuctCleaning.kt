package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.graph.Edge
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import com.capital7software.aoc.lib.grid.Grid2d
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.LinkedList
import java.util.Queue

/**
 * You've finally met your match; the doors that provide access to the roof are locked tight,
 * and all the controls and related electronics are inaccessible. You simply can't reach them.
 *
 * The robot that cleans the air ducts, however, **can.**
 *
 * It's not a very fast little robot, but you reconfigure it to be able to interface with
 * some of the exposed wires that have been routed through the HVAC system. If you can
 * direct it to each of those locations, you should be able to bypass the security controls.
 *
 * You extract the duct layout for this area from some blueprints you acquired and create a
 * map with the relevant locations marked (your puzzle input). 0 is your current location,
 * from which the cleaning robot embarks; the other numbers are (in **no particular order**)
 * the locations the robot needs to visit at least once each. Walls are marked as #, and
 * open passages are marked as .. Numbers behave like open passages.
 *
 * For example, suppose you have a map like the following:
 *
 * ```
 * ###########
 * #0.1.....2#
 * #.#######.#
 * #4.......3#
 * ###########
 * ```
 *
 * To reach all the points of interest as quickly as possible, you would have the
 * robot take the following path:
 *
 * - 0 to 4 (2 steps)
 * - 4 to 1 (4 steps; it can't move diagonally)
 * - 1 to 2 (6 steps)
 * - 2 to 3 (2 steps)
 *
 * Since the robot isn't very fast, you need to find it the **shortest route.** This path is the
 * fewest steps (in the above example, a total of 14) required to start at 0 and then visit
 * every other location at least once.
 *
 * **Please note that the maximum number of targets in a map is ten (0 - 9).**
 *
 * @param input The Map of the air ducts to parse and load.
 * @param copy If set to true the specified [Grid2d] will be used as the source.
 * @param grid2d If copy set to true and this is not null, then this grid is used
 * as the source of targets for this instance.
 */
class AirDuctCleaning(
    input: List<String>,
    copy: Boolean = false,
    grid2d: Grid2d<Tile>? = null,
) {
  /**
   * A [Tile] in the [AirDuctCleaning] map.
   *
   * @param point The [Point2D] location of this tile in 2D space.
   */
  sealed class Tile(val point: Point2D<Int>) : Comparable<Tile> {
    /**
     * This tile as a [Vertex].
     */
    val vertex: Vertex<Tile, Int> by lazy { Vertex(point.id(), this) }

    /**
     * The single [Char] label can be used for parsing and serialization
     * as a type indicator. Also, it is useful for output or as input.
     */
    abstract val label: Char

    /**
     * Indicates if this tile can be walked on / passed-through.
     */
    abstract val walkable: Boolean

    /**
     * Indicates if this tile is a target tile when building / searching routes.
     */
    abstract val target: Boolean

    override fun compareTo(other: Tile): Int {
      return when (other) {
        is Target -> {
          if (this is Target) {
            this.id.compareTo(other.id)
          } else {
            point.compareTo(other.point)
          }
        }

        else -> point.compareTo(other.point)
      }
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Tile) return false

      if (point != other.point) return false

      return true
    }

    override fun hashCode(): Int {
      return point.hashCode()
    }

    /**
     * An impassable [Tile].
     *
     * @param point The [Point2D] location of this tile in 2D space.
     */
    class Wall(point: Point2D<Int>) : Tile(point) {
      companion object {
        /**
         * All [Wall] tiles share the same label.
         */
        const val LABEL: Char = '#'
      }

      override val label: Char
        get() = LABEL
      override val walkable: Boolean
        get() = false
      override val target: Boolean
        get() = false

      override fun toString(): String {
        return "Wall(label=$label, point=$point, walkable=$walkable, target=$target)"
      }
    }

    /**
     * A walkable / passable [Tile].
     *
     * @param point The [Point2D] location of this tile in 2D space.
     */
    class Space(point: Point2D<Int>) : Tile(point) {
      companion object {
        /**
         * All [Space] tiles share the same label.
         */
        const val LABEL: Char = '.'
      }

      override val label: Char
        get() = LABEL
      override val walkable: Boolean
        get() = true
      override val target: Boolean
        get() = false

      override fun toString(): String {
        return "Space(label=$label, point=$point, walkable=$walkable, target=$target)"
      }
    }

    /**
     * A tile that can be used as a target in a search. The label of this
     * tile is the [String] representation of this tile's ID. All [Target]
     * tiles are walkable / passable [Tile].
     *
     * @param id The ID of this [Target] [Tile]. Must be between 0 and 9.
     * @param point The [Point2D] location of this tile in 2D space.
     */
    class Target(val id: Int, point: Point2D<Int>) : Tile(point) {
      override val label: Char by lazy { id.toString().first() }
      override val walkable: Boolean
        get() = true
      override val target: Boolean
        get() = true

      override fun toString(): String {
        return "Target(id=$id, point=$point, label=$label, walkable=$walkable, target=$target)"
      }
    }

    companion object {
      /**
       * A factory function for creating new [Tile] instances.
       *
       * @param c The character must match a [Tile.label] or be
       * a digit.
       * @param x The x-coordinate this tile occupies in 2D space.
       * @param y The y-coordinate this tile occupies in 2D space.
       * @return A [Tile] instance that corresponds to [c].
       * @throws IllegalStateException if [c] is not a valid type indicator.
       *
       */
      fun parse(c: Char, x: Int, y: Int): Tile {
        val point = Point2D(x, y)
        return if (c.isDigit()) {
          Target(c.digitToInt(), point)
        } else if (c == Wall.LABEL) {
          Wall(point)
        } else if (c == Space.LABEL) {
          Space(point)
        } else {
          error("Unknown tile type: $c at $point")
        }
      }
    }

  }

  private val grid: Grid2d<Tile> = if (copy && grid2d != null) {
    grid2d.copy()
  } else {
    buildGrid(input)
  }

  private val targets: Map<Int, Tile.Target> by lazy { buildTargetMap() }

  private val intersectionGraph: Graph<Tile, Int> by lazy { buildIntersectionGraph() }

  private val compressedGraph: Graph<Tile, Int> by lazy { buildCompressedGraph() }

  private val targetGraph: Graph<Tile, Int> by lazy { buildTargetGraph() }

  /**
   * Builds and returns a new grid loaded from the specified list.
   *
   * The map must follow this format:
   *
   * - # is a wall.
   * - . is a walkable space.
   * - 0-N is a sequential target ID that is zero based.
   *
   * A small but valid example:
   *
   * ```
   * ###########
   * #0.1.....2#
   * #.#######.#
   * #4.......3#
   * ###########
   * ```
   *
   * @param input The map to parse and load.
   */
  @SuppressFBWarnings
  private fun buildGrid(input: List<String>): Grid2d<Tile> {
    var columns = 0
    var rows = 0
    val tiles = mutableListOf<Tile>()

    input.forEach { line ->
      if (line.length > columns) {
        columns = line.length
      }
      for (i in line.indices) {
        val c = line[i]
        val tile = Tile.parse(c, i, rows)

        tiles.add(tile)
      }
      rows++
    }

    val items: Array<Tile> = Array(columns * rows) { Tile.Wall(Point2D<Int>(-1, -1)) }

    tiles.forEach { tile ->
      items[tile.point.y * columns + tile.point.x] = tile
    }
    return Grid2d(columns, rows, items)
  }

  /**
   * Scans the grid for the [Tile.Target] and builds and returns a map
   * where the key is the ID of the target and the value is the target tile.
   *
   * @return A [Map] where the key is the ID of the [Tile.Target] and the value
   * is the target tile.
   */
  private fun buildTargetMap(): Map<Int, Tile.Target> {
    val targets = mutableMapOf<Int, Tile.Target>()

    grid.forEach { tile ->
      if (tile is Tile.Target) {
        targets[tile.id] = tile
      }
    }

    return targets
  }

  private fun buildIntersectionGraph(): Graph<Tile, Int> {
    val graph = Graph<Tile, Int>("air-duct-intersections")
    val queue: Queue<Tile> = LinkedList()
    val start: Tile = findWalkableFrom(0, 0) ?: targets[0]!!
    val visited = hashSetOf<Tile>()

    queue.offer(start)

    while (queue.isNotEmpty()) {
      val tile = queue.poll()

      if (tile == null || visited.contains(tile)) {
        continue // Don't explore a tile more than once!
      }

      visited.add(tile)
      graph.add(tile.vertex)
    }
    return graph
  }

  private fun findWalkableFrom(x: Int, y: Int): Tile? {
    for (i in x ..< grid.columns) {
      for (j in y ..< grid.rows) {
        if (grid.isOnGrid(i, j) && grid[i, j].walkable) {
          return grid[i, j]
        }
      }
    }
    return null
  }

  private fun buildCompressedGraph(): Graph<Tile, Int> {
    TODO("Not yet implemented")
  }

  private fun buildTargetGraph(): Graph<Tile, Int> {
    TODO("Not yet implemented")
  }

  /**
   * Finds the shortest route through the air ducts starting at the specified ID
   * and visiting every [Tile.Target] at least once. The start ID must exist in the map.
   * The returned path may include visiting a [Tile.Target] more than once. When
   * this happens it should be treated like any other [Edge]. If no shortest
   * route could be found, the returned [Pair] will contain -1 in the first element.
   *
   * @param startId The id where the search starts from. Must be a digit 0 - 9.
   * @return A [Pair] where the first element is the length of the shortest route and
   * the second element is the [List] of [Edge] that make up the shortest route.
   */
  fun findShortestRoute(startId: Int): Pair<Int, List<Edge<Int>>> {
    return Pair(startId, listOf())
  }
}
