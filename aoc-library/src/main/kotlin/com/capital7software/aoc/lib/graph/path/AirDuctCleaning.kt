package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.graph.Edge
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import com.capital7software.aoc.lib.grid.Grid2d
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.LinkedList
import java.util.Properties
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
    var spaces = 0
    var targets = 0

    input.forEach { line ->
      if (line.length > columns) {
        columns = line.length
      }
      for (i in line.indices) {
        val c = line[i]
        val tile = Tile.parse(c, i, rows)

        if (tile is Tile.Space) {
          spaces++
        } else if (tile is Tile.Target) {
          targets++
        }

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
    val start: Tile = findNextWalkableFromUpperLeft() ?: targets[0]!!
    val visited = hashSetOf<Tile>()

    queue.offer(start)

    while (queue.isNotEmpty()) {
      val tile = queue.poll()

      if (tile == null || visited.contains(tile)) {
        continue // Don't explore a tile more than once!
      }

      visited.add(tile)
      graph.add(tile.vertex.copy())

      for (direction in Direction.CARDINAL_DIRECTIONS) {
        val point = tile.point.pointInDirection(direction)

        if (!grid.isOnGrid(point) || !grid[point].walkable) {
          continue
        }

        val intersection = getNextIntersection(tile, direction) ?: continue

        val srcId = tile.vertex.id
        val target = intersection.first
        val tgtId = target.vertex.id
        graph.add(target.vertex.copy())
        graph.add(srcId, tgtId, "$srcId-$tgtId", intersection.second)

        if (!visited.contains(target)) {
          queue.offer(target)
        }
      }
    }
    return graph
  }

  /**
   * Returns a Pair where the first element is the intersection tile found in the specified
   * direction and the second element is the length from the specified tile to the
   * intersection tile. **Please note that a point in the specified direction must exist
   * or else a NullPointerException will be thrown.**
   *
   * @param tile      The tile to start our walk from.
   * @param direction The direction of the walk.
   * @return A Pair with the first element being the intersection tile and the second the
   *     number of steps to get from the source tile to the intersection tile.
   */
  private fun getNextIntersection(tile: Tile, direction: Direction): Pair<Tile, Int>? {
    var point = tile.point.pointInDirection(direction)

    if (!grid.isOnGrid(point) || !grid[point].walkable) {
      return null
    }

    val queue: Queue<Tile> = LinkedList()

    queue.offer(tile)

    var count = 0
    var current: Tile = tile

    while (queue.isNotEmpty()) {
      current = queue.poll() ?: break
      point = current.point.pointInDirection(direction)

      if (!grid.isOnGrid(point) || !grid[point].walkable) {
        break
      }

      val target = grid[point]
      count++
      current = target

      if (isIntersection(target)) {
        break
      }
      queue.offer(target)
    }

    return if (count == 0) {
      null
    } else {
      Pair(current, count)
    }
  }

  /**
   * Returns true if this tile is an intersection tile. An Intersection tile is defined as a
   * tile that is a [Tile.Target] tile or a tile that contains at least two walkable tiles in
   * two directions that are perpendicular to each other. In short, if the walkable tiles form
   * a right-angle then it is an intersection. Please note that all tiles must be walkable!
   *
   * @param tile A walkable tile to determine if it is an intersection or not.
   * @return True is returned if the specified tile is an intersection tile.
   */
  @SuppressFBWarnings
  private fun isIntersection(tile: Tile): Boolean {
    if (!tile.walkable) {
      return false
    }

    if (tile.target) {
      return true
    }

    val neighbors = grid.getNeighbors(
        tile.point,
        Direction.CARDINAL_DIRECTIONS
    ) { it.walkable }

    if (neighbors.size >= 3 || neighbors.size < 2) {
      return true
    }

    val directions = neighbors.map { it.first() }.toHashSet()

    for (direction in directions) {
      for (perpendicular in direction.perpendicular) {
        if (directions.contains(perpendicular)) {
          return true
        }
      }
    }

    return false
  }

  private fun findNextWalkableFromUpperLeft(): Tile? {
    for (i in 0..<grid.columns) {
      for (j in 0..<grid.rows) {
        if (grid.isOnGrid(i, j) && grid[i, j].walkable) {
          return grid[i, j]
        }
      }
    }
    return null
  }

  /**
   * Using the intersection graph, we find the shortest path from every target
   * to every other target. The resulting graph will be a complete graph where
   * the edges are the shortest distances between any two targets.
   *
   * This assumes that the shortest path from a to b is also the shortest path
   * from b to a. Meaning that we only calculate the shortest distance between
   * two vertices a single time making the resulting graph undirected.
   *
   * @return The target graph where all the vertices are targets and the edges
   * are the shortest paths between the targets.
   */
  private fun buildTargetGraph(): Graph<Tile, Int> {
    val graph: Graph<Tile, Int> = Graph("air-duct-targets")
    val shortestPaths: MutableMap<Int, MutableMap<Int, Pair<Int, List<Edge<Int>>>>> = mutableMapOf()

    targets.keys.forEach { sourceId ->
      val sourceTile = targets[sourceId] ?: error("Missing target with ID: $sourceId")
      val sourceMap = shortestPaths.computeIfAbsent(sourceTile.id) { mutableMapOf() }
      graph.add(Vertex(sourceTile.vertex.id, sourceTile))

      targets.forEach { (key, tile) ->
        if (key != sourceId) {
          val path = findShortestPath(intersectionGraph, sourceTile.vertex, tile.vertex)

          if (path.first > 0 && !sourceMap.containsKey(tile.id)) {
            sourceMap.putIfAbsent(tile.id, path)
            val targetMap = shortestPaths.computeIfAbsent(tile.id) { mutableMapOf() }
            targetMap.putIfAbsent(sourceTile.id, path)

            graph.add(Vertex(tile.vertex.id, tile))
            graph.add(
                sourceTile.vertex.id,
                tile.vertex.id,
                "${sourceTile.vertex.id}-${tile.vertex.id}",
                path.first
            )
            graph.add(
                tile.vertex.id,
                sourceTile.vertex.id,
                "${tile.vertex.id}-${sourceTile.vertex.id}",
                path.first
            )
          }
        }
      }
    }

    return graph
  }

  private fun findShortestPath(
      graph: Graph<Tile, Int>, start: Vertex<Tile, Int>, target: Vertex<Tile, Int>
  ): Pair<Int, List<Edge<Int>>> {
    val pathFinder = AlphaStarPathfinder<Tile, Int>()
    val properties = Properties()
    var shortestPath: PathfinderResult<Tile, Int>? = null

    properties[PathfinderProperties.SUM_PATH] = true
    properties[PathfinderProperties.STARTING_VERTEX_ID] = start.id
    properties[PathfinderProperties.ENDING_VERTEX_ID] = target.id
    properties[PathfinderProperties.HEURISTIC] = Heuristic<Tile, Int> { _, vertex ->
      vertex.get().point.manhattanDistance(target.get().point).toDouble()
    }

    pathFinder.find(
        graph,
        properties,
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

    return pathToPair(shortestPath)
  }

  private fun pathToPair(
      pathfinderResult: PathfinderResult<Tile, Int>?
  ) : Pair<Int, List<Edge<Int>>> {
    return if (pathfinderResult == null) {
      Pair(-1, listOf())
    } else {
      Pair(pathfinderResult.cost, pathfinderResult.edges.toList())
    }
  }

  /**
   * Finds the shortest route through the air ducts starting at the specified ID
   * and visiting every [Tile.Target] at least once. The start ID must exist in the map.
   * The returned path may include visiting a [Tile.Target] more than once. When
   * this happens it should be treated like any other [Edge]. If no shortest
   * route could be found, the returned [Pair] will contain -1 in the first element.
   *
   * Repeatedly use [AlphaStarPathfinder] to construct the shortest paths to each target
   * and then uses [HamiltonianPathfinder] to find the shortest path that starts at startId.
   *
   * @param startId The id where the search starts from. Must be a digit 0 - 9.
   * @return A [Pair] where the first element is the length of the shortest route and
   * the second element is the [List] of [Edge] that make up the shortest route.
   */
  fun findShortestRoute(startId: Int): Pair<Int, List<Edge<Int>>> {
    val target = getTarget(startId)
    return getShortestHamiltonPath(target)
  }


  /**
   * Finds the shortest route through the air ducts starting at the specified ID
   * and visiting every [Tile.Target] at least once. The start ID must exist in the map.
   * The returned path may include visiting a [Tile.Target] more than once. When
   * this happens it should be treated like any other [Edge]. If no shortest
   * route could be found, the returned [Pair] will contain -1 in the first element.
   *
   * Repeatedly use [AlphaStarPathfinder] to construct the shortest paths to each target
   * and then uses [HamiltonianPathfinder] to find the shortest cycle that starts and
   * ends at the tile with the startId.
   *
   * @param startId The id where the search starts from. Must be a digit 0 - 9.
   * @return A [Pair] where the first element is the length of the shortest route and
   * the second element is the [List] of [Edge] that make up the shortest route.
   */
  fun findShortestCycle(startId: Int): Pair<Int, List<Edge<Int>>> {
    val target = getTarget(startId)
    return getShortestHamiltonPath(target, true)
  }

  private fun getShortestHamiltonPath(
      target: Vertex<Tile, Int>, cycles: Boolean = false
  ): Pair<Int, List<Edge<Int>>> {
    val pathfinder = HamiltonianPathfinder<Tile, Int>()

    val props = getHamiltonProperties(listOf(target), cycles)
    var shortestPath: PathfinderResult<Tile, Int>? = null
    pathfinder.find(targetGraph, props, {
      val temp = shortestPath
      if (temp == null || it.cost < temp.cost) {
        shortestPath = it
      }
      PathfinderStatus.CONTINUE
    }, null)

    return pathToPair(shortestPath)
  }

  private fun getTarget(id: Int): Vertex<Tile, Int> = targetGraph[targets[id]!!.vertex.id]!!
  private fun getHamiltonProperties(
      start: List<Vertex<Tile, Int>>,
      cycles: Boolean = false)
  : Properties {
    val props = Properties()
    props[PathfinderProperties.SUM_PATH] = true
    props[PathfinderProperties.STARTING_VERTICES] = start
    if (cycles) {
      props[PathfinderProperties.DETECT_CYCLES] = true
    }

    return props
  }
}
