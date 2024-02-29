package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.graph.Edge
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import com.capital7software.aoc.lib.graph.path.Maze.Tile
import com.capital7software.aoc.lib.graph.path.Maze.Tile.Space
import com.capital7software.aoc.lib.graph.path.Maze.Tile.Wall
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import java.util.Properties
import java.util.function.BiFunction


/**
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
 * ``
 * Thus, reaching 7,4 would take a minimum of 11 steps (starting from your current location, 1,1).
 *
 * @param start The [Tile] to start from.
 * @param finish The [Tile] to find a path to.
 * @param favorite The designer's favorite number.
 * @param name The name for the [Graph] that is generated.
 */
class Maze private constructor(
    private val start: Tile,
    private val finish: Tile,
    private val favorite: Long,
    name: String = "maze-${Instant.now().nano}",
) : Heuristic<Tile, Int> {
  private val graph: Graph<Tile, Int> = Graph<Tile, Int>(name).apply {
    // Add the start and finish tiles to this graph.
    add(start.vertex)
    add(finish.vertex)
  }
  private val expanded: MutableSet<Point2D<Long>> = mutableSetOf()
  private val expander: BiFunction<Graph<Tile, Int>, Vertex<Tile, Int>, Boolean> =
      BiFunction<Graph<Tile, Int>, Vertex<Tile, Int>, Boolean>
      { g: Graph<Tile, Int>, v: Vertex<Tile, Int> ->
        val tile: Tile = v.get()

        return@BiFunction if (expanded.contains(tile.point)) {
          false
        } else {
          neighbors(tile, favorite)
              .forEach {
                val targetId = it.id
                g.add(v.get().vertex)
                g.add(it.vertex)
                g.add(v.id, targetId, "${v.id}-$targetId", 1)
                g.add(targetId, v.id, "$targetId-${v.id}", 1)
              }
          expanded.add(tile.point)
          true
        }
      }

  companion object {
    /**
     * Builds and returns a new [Maze] using the specified favorite number to determine which
     * type of [Tile] to generate. If the tiles generated for the start and finish points are not
     * both walkable an [IllegalStateException] will be thrown.
     *
     * @param startPoint The [Point2D] to start from.
     * @param finishPoint The [Point2D] to find a path to.
     * @param favorite The designer's favorite number.
     * @param name The name for the [Graph] that is generated.
     */
    @JvmStatic
    @JvmOverloads
    fun buildMaze(
        startPoint: Point2D<Long>,
        finishPoint: Point2D<Long>,
        favorite: Long,
        name: String = "maze-${Instant.now().nano}"
    ): Maze {
      val start = Tile.from(startPoint.x, startPoint.y, favorite)
      val finish = Tile.from(finishPoint.x, finishPoint.y, favorite)

      check(start.isWalkable()) { "$start is not walkable" }
      check(finish.isWalkable()) { "$finish is not walkable" }

      return Maze(start, finish, favorite, name)
    }

    /**
     * Returns a [List] of [Tile] that are adjacent and walkable from the specified tile.
     *
     * Please note that only tiles with an x and y coordinate that are greater to or equal to
     * zero are considered.
     *
     * Only the [Direction.CARDINAL_DIRECTIONS] are considered as navigation is restricted to
     * vertical and horizontal.
     *
     * @param tile The [Tile] to get the walkable neighbors for.
     * @param favorite The designer's favorite number.
     * @return A [List] of [Tile] that are adjacent and walkable from the specified tile.
     */
    @SuppressFBWarnings
    @JvmStatic
    fun neighbors(tile: Tile, favorite: Long): List<Tile> {
      return Direction.CARDINAL_DIRECTIONS
          .map { direction -> tile.point.pointInDirection(direction) }
          .filter { point -> point.x >= 0 && point.y >= 0 }
          .map { point -> Tile.from(point.x, point.y, favorite) }
          .filter { mazeTile -> mazeTile.isWalkable() }
    }
  }

  /**
   * A space within the [Maze]. A [Tile] may represent an impassible [Wall] or a walkable
   * [Space] that can be passed through.
   *
   * @param point The [Point2D] that represents this [Tile] in 2D space.
   */
  sealed class Tile(val point: Point2D<Long>) : Comparable<Tile> {
    companion object {
      /**
       * You can determine whether a given x,y coordinate will be a wall or an open
       * space using a simple system:
       * - Find ```x*x + 3*x + 2*x*y + y + y*y.```
       * - Add the office designer's favorite number (your puzzle input).
       * - Find the binary representation of that sum; count the **number** of bits that are 1.
       *     - If the number of bits that are 1 is **even**, it's an **open space.**
       *     - If the number of bits that are 1 is **odd**, it's a **wall.**
       *
       * For example, if the office designer's favorite number were 10, drawing walls as # and
       * open spaces as ., the corner of the building containing 0,0 would look like this:
       *
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
       *
       * Now, suppose you wanted to reach 7,4. The shortest route you could take is marked as O:
       *
       * ```
       *   0123456789
       * 0 .#.####.##
       * 1 .O#..#...#
       * 2 #OOO.##...
       * 3 ###O#.###.
       * 4 .##OO#OO#.
       * 5 ..##OOO.#.
       * 6 #...##.###
       * ``
       *
       * Thus, reaching 7,4 would take a minimum of 11 steps (starting from your
       * current location, 1,1).
       *
       * @param x The x-axis.
       * @param y The y-axis.
       * @param favorite The maze designer's favorite number.
       */
      fun from(x: Long, y: Long, favorite: Long): Tile {
        val total: Long = ((x * x) + (3 * x) + (2 * x * y) + y + (y * y)) + favorite
        return when (total.countOneBits() % 2 != 0) {
          true -> Wall(x, y)
          else -> Space(x, y)
        }
      }
    }

    /**
     * Returns the coordinates of this tile in x,y format.
     *
     * @return The coordinates of this tile in x,y format.
     */
    val id: String by lazy { "${point.x},${point.y}" }

    /**
     * Converts this [Tile] into a new [Vertex] that can be added to a [Graph].
     *
     * @return A new [Vertex] created from this [Tile].
     */
    val vertex: Vertex<Tile, Int> by lazy { Vertex(id, this) }

    override fun compareTo(other: Tile): Int {
      return point.compareTo(other.point)
    }

    /**
     * Returns true if this [Tile] can be walked on / traveled through.
     *
     * @return True if this [Tile] can be walked on / traveled through.
     */
    abstract fun isWalkable(): Boolean

    /**
     * The label is a single character [String] representation of this tile. Primarily used
     * to identify a tile when reading a grid.
     *
     * @return The label of this tile.
     */
    abstract fun label(): String

    /**
     * Returns a short description of the type of tile this is.
     *
     * @return A short description of the type of tile this is.
     */
    abstract fun description(): String

    override fun toString(): String {
      return "Tile(point=$point, isWalkable=${isWalkable()}, " +
          "label=${label()}, description=${description()})"
    }

    /**
     * A Space [Tile] is a tile that can be walked on / traveled through.
     */
    class Space(x: Long, y: Long) : Tile(Point2D(x, y)) {
      companion object {
        private const val LABEL: String = "."
        private const val DESCRIPTION: String = "open-space"
      }

      override fun isWalkable(): Boolean = true
      override fun label(): String = LABEL
      override fun description(): String = DESCRIPTION
    }

    /**
     * A Wall [Tile] is a tile that cannot be walked on / traveled through. In short, it is an
     * obstacle.
     */
    class Wall(x: Long, y: Long) : Tile(Point2D(x, y)) {
      companion object {
        private const val LABEL: String = "#"
        private const val DESCRIPTION: String = "wall"
      }

      override fun isWalkable(): Boolean = false
      override fun label(): String = LABEL
      override fun description(): String = DESCRIPTION
    }
  }

  /**
   * A solution to the generated maze.
   *
   * @param id The unique ID of this solution.
   * @param steps The number of steps to complete the [Maze].
   * @param path The ordered [List] of [Tile] where
   * `path[0] == start and path[steps] == finish`.
   * @param edges The [List] of [Edge] to enable calculating the cost between steps.
   */
  @SuppressFBWarnings
  data class MazeSolution(
      val id: Long = -1,
      val steps: Int = -1,
      val path: List<Tile> = listOf(),
      val edges: List<Edge<Int>> = listOf()
  ) {
    companion object {
      /**
       * Builds and returns a new [MazeSolution] from the specified [PathfinderResult]. If
       * the specified path is null, then an empty MazeSolution is returned.
       *
       * @param path The [PathfinderResult] to process. If it is null, then a default empty
       * [MazeSolution] is returned.
       * @return A new [MazeSolution] from the specified [PathfinderResult].
       */
      @SuppressFBWarnings
      fun from(path: PathfinderResult<Tile, Int>?): MazeSolution {
        return if (path == null) {
          MazeSolution()
        } else {
          MazeSolution(path.id, path.cost, path.vertices.mapNotNull { it.get() }.toList())
        }
      }
    }
  }

  /**
   * Calculates and returns the Manhattan Distance between the specified [Vertex] and
   * the finish [Tile] of this [Maze].
   *
   * @param graph The Graph being used.
   * @param vertex The Vertex being operated on.
   * @return The Heuristic value.
   */
  override fun calculate(graph: Graph<Tile, Int>, vertex: Vertex<Tile, Int>): Double {
    return vertex.get().point.manhattanDistance(finish.point).toDouble()
  }

  /**
   * Calculates and returns a [MazeSolution] that contains the cost and path for the
   * shortest path through this [Maze].
   *
   * @return A [MazeSolution] that contains the cost and path for the shortest path.
   */
  fun findShortestPath(): MazeSolution {
    val pathFinder = AlphaStarPathfinder<Tile, Int>()
    val properties = Properties()
    properties[PathfinderProperties.SUM_PATH] = true
    properties[PathfinderProperties.STARTING_VERTEX_ID] = start.id
    properties[PathfinderProperties.ENDING_VERTEX_ID] = finish.id
    properties[PathfinderProperties.HEURISTIC] = this
    var shortestPath: PathfinderResult<Tile, Int>? = null

    pathFinder.find(
        graph,
        properties,
        expander,
        {
          val current = shortestPath

          if (current == null) {
            shortestPath = it
          } else if (current.cost > it.cost) {
            shortestPath = it
          }
          // AlphaStar is greedy and only calls the valid handler a single time!
          PathfinderStatus.FINISHED
        },
        null
    )

    return MazeSolution.from(shortestPath)
  }

  /**
   * Explorers the paths within this [Maze]. A path is explored until it runs out of neighbors or
   * the limit is reached.
   *
   * @param limit A value greater than zero that limits the maximum cost of a path.
   * @return A set of [Tile] that were reached during the exploration.
   */
  @SuppressFBWarnings
  fun findDistinct(limit: Double = 1.0): Set<Tile> {
    val pathFinder = ExplorerPathfinder<Tile, Int>()
    val properties = Properties()
    properties[PathfinderProperties.STARTING_VERTEX_ID] = start.id
    properties[PathfinderProperties.MAX_COST] = limit
    var distinct: PathfinderResult<Tile, Int>? = null

    pathFinder.find(
        graph,
        properties,
        expander,
        {
          distinct = it

          // Explorer is greedy and only calls the valid handler a single time!
          PathfinderStatus.FINISHED
        },
        null
    )

    return distinct?.vertices?.mapNotNull { it.get() }?.toSet() ?: setOf()
  }
}
