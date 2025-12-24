package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import com.capital7software.aoc.lib.grid.Grid2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import java.util.Objects
import java.util.Properties
import java.util.function.BiFunction

/**
 * You gain access to a massive storage cluster arranged in a grid; each storage node is
 * only connected to the four nodes directly adjacent to it (three if the node is on an
 * edge, two if it's in a corner).
 *
 * You can directly access data **only** on node /dev/grid/node-x0-y0, but you can perform
 * some limited actions on the other nodes:
 *
 * - You can get the disk usage of all nodes (via df). The result of doing this is in
 * your puzzle input.
 * - You can instruct a node to **move** (not copy) **all** of its data to an adjacent node (if
 * the destination node has enough space to receive the data). The sending node is left
 * empty after this operation.
 *
 * Nodes are named by their position: the node named node-x10-y10 is adjacent to nodes
 * node-x9-y10, node-x11-y10, node-x10-y9, and node-x10-y11.
 *
 * Before you begin, you need to understand the arrangement of data on these nodes.
 * Even though you can only move data between directly connected nodes, you're going
 * to need to rearrange a lot of the data to get access to the data you need.
 * Therefore, you need to work out how you might be able to shift data around.
 *
 * To do this, you'd like to count the number of **viable pairs** of nodes. A viable
 * pair is any two nodes (A,B), **regardless of whether they are directly connected,**
 * such that:
 *
 * - Node A is **not** empty (its Used is not zero).
 * - Nodes A and B are **not the same** node.
 * - The data on node A (its Used) **would fit** on node B (its Avail).
 *
 * @param input The [List] of [String] that contain the output from the df -h command.
 * @param copyFrom The [Grid2D] to copy and populate this grid from.
 */
class GridComputing @SuppressFBWarnings constructor(
  input: List<String>,
  copyFrom: Grid2D<GridNode>? = null,
) {
  private val grid: Grid2D<GridNode> = copyFrom?.copy() ?: buildGrid(input)
  private val expanded: MutableSet<String> = mutableSetOf()

  /**
   * The number of columns in this grid computing cluster.
   *
   */
  val columns: Int by lazy { grid.columns }

  /**
   * The number of rows in this grid computing cluster.
   *
   */
  val rows: Int by lazy { grid.rows }

  @Suppress("comments:UndocumentedPublicClass")
  companion object {
    private val GRID_NODE = ("node-x(?<x>\\d+)-y(?<y>\\d+)\\s+(?<size>\\d+)T\\s+(?<used>\\d+)T" +
        "\\s+(?<available>\\d+)T\\s+(?<percent>\\d+)").toRegex()
    private const val XCOORD = "x"
    private const val YCOORD = "y"
    private const val SIZE = "size"
    private const val USED = "used"
    private const val AVAILABLE = "available"
    private const val PERCENT = "percent"

    /**
     * An invalid node.
     */
    val DEAD_NODE = GridNode("dead", Point2D(-1, -1), Data(-1, -1, -1, -1))

    @SuppressFBWarnings
    private fun parseNode(line: String): GridNode? {
      val match = GRID_NODE.find(line)

      return if (match != null) {
        val x = match.groups[XCOORD]!!.value.toInt()
        val y = match.groups[YCOORD]!!.value.toInt()
        val size = match.groups[SIZE]!!.value.toInt()
        val used = match.groups[USED]!!.value.toInt()
        val available = match.groups[AVAILABLE]!!.value.toInt()
        val percent = match.groups[PERCENT]!!.value.toInt()
        val id = "node-x$x-y$y"
        val point = Point2D(x, y)
        GridNode(id, point, Data(size, used, available, percent))
      } else {
        null
      }
    }
  }

  /**
   * Represents the data held by a [GridNode].
   *
   * @param size The total amount of storage space on this node.
   * @param used The total amount of storage space that is currently in use.
   * @param available The total amount of free storage space on this node.
   * @param percent The percent of total storage that is currently in use (0 - 100).
   */
  data class Data(val size: Int, val used: Int, val available: Int, val percent: Int) {
    private companion object {
      private const val FACTOR = 100
      private const val HUGE_LIMIT = 100
    }

    /**
     * Adds other's data to this data and returns a new [Data] instance with the new sums.
     *
     * The size value of the returned instance is this object's size.
     *
     * If adding other's data to this data exceeds the size of this data, then an
     * IllegalStateException is thrown.
     *
     * @param other The data object to add to this data object.
     * @return A new [Data] object with the sum of this object and other.
     */
    operator fun plus(other: Data): Data {
      val newUsed = used + other.used
      val newAvailable = size - newUsed

      check(newUsed <= size) {
        "Adding the other data object's ${other.used} of data to " +
            "this data object would exceeded the size limit of this data object: $size."
      }

      return Data(size, newUsed, newAvailable, ((newUsed.toDouble() / size) * FACTOR).toInt())
    }

    /**
     * Subtracts the other data from this data and returns a new [Data] object with the difference.
     *
     * The size value of the returned instance is this object's size.
     *
     * If subtracting other's data from this object would cause the amount used to be less than
     * zero an IllegalStateException will be thrown.
     *
     * @param other The data object to add to this data object.
     * @return A new [Data] object with the difference of this object and other.
     */
    operator fun minus(other: Data): Data {
      val newUsed = used - other.used
      val newAvailable = size - newUsed

      check(newUsed >= 0) {
        "Subtracting the other data object's ${other.used} of data " +
            "from this data object would cause an underflow."
      }

      return Data(size, newUsed, newAvailable, ((newUsed.toDouble() / size) * FACTOR).toInt())
    }

    /**
     * Returns true if the data store is empty!
     *
     * @return True if the data store is empty!
     */
    fun isEmpty(): Boolean = used == 0 && available == size

    /**
     * Returns true if the capacity of this data is over 100 Terabytes.
     *
     * @return True if the capacity of this data is over 100 Terabytes.
     */
    fun isHuge(): Boolean = size > HUGE_LIMIT
  }

  /**
   * A node in the [GridComputing] cluster.
   *
   * @param id The unique ID of this node.
   * @param point The unique x and y coordinates of this node.
   * @param data The data held by this node.
   */
  data class GridNode(
      val id: String,
      val point: Point2D<Int>,
      val data: Data,
  ) : Comparable<GridNode> {
    /**
     * This node converted to a [Vertex].
     */
    val vertex: Vertex<GridNode, Int> by lazy { Vertex<GridNode, Int>(id, this) }

    /**
     * Returns true if this node is able to move its data to the specified node.
     *
     * @param node The node to check if this node's data can be moved to.
     * @return True if this node is able to move its data to the specified node.
     */
    fun canMoveTo(node: GridNode): Boolean =
        data.used > 0 && this != node && data.used <= node.data.available

    override operator fun compareTo(other: GridNode): Int = point.compareTo(other.point)

    /**
     * Moves the data from this object to the specified object. Returns a pair where the
     * first element is the specified node with this object's data added to it. The
     * second element is this object with its data removed.
     *
     * Please note that if the capacity of the specified node is exceeded an IllegalStateException
     * will be thrown.
     *
     * @param node The node to move the data to.
     * @return A [Pair] where the first element is the specified node with this object's data
     * added to it. The second element is this object with its data removed.
     */
    fun moveTo(node: GridNode): Pair<GridNode, GridNode> {
      val a = node.copy(data = node.data + data)
      val b = copy(data = data - data)

      return Pair(a, b)
    }

    /**
     * Returns true if this node's [Data] is empty.
     *
     * @return True if this node's [Data] is empty.
     */
    fun isEmpty(): Boolean = data.isEmpty()

    /**
     * Returns true if this node's [Data] is huge.
     *
     * @return True if this node's [Data] is huge.
     */
    fun isHuge(): Boolean = data.isHuge()

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is GridNode) return false

      if (id != other.id) return false

      return true
    }

    override fun hashCode(): Int {
      return id.hashCode()
    }
  }

  @SuppressFBWarnings
  private fun buildGrid(input: List<String>): Grid2D<GridNode> {
    var maxX = 0
    var maxY = 0
    val nodes = mutableListOf<GridNode>()
    var free = 0

    input.mapNotNull { parseNode(it) }
        .forEach { node ->
          if (node.point.x() > maxX) {
            maxX = node.point.x()
          }
          if (node.point.y() > maxY) {
            maxY = node.point.y()
          }
          if (node.isEmpty()) {
            free++
          }
          nodes.add(node)
        }

    check(free == 1) { "Exactly one node must be empty!" }

    maxX++
    maxY++

    val items = Array(maxX * maxY) { DEAD_NODE }
    for (node in nodes) {
      items[node.point.y() * maxX + node.point.x()] = node
    }

    return Grid2D(maxX, maxY, items)
  }

  /**
   * Calculates and returns a [List] of [Pair] where the elements of the pair are the viable
   * [GridNode]. The first element is able to move all its data over to the second element.
   *
   * @return A list of the viable [GridNode] pairs where the data from the first element can be
   * moved over to the grid node in the second element.
   */
  fun viablePairs(): List<Pair<GridNode, GridNode>> {
    val answer = mutableListOf<Pair<GridNode, GridNode>>()

    for (a in grid.iterator()) {
      for (b in grid.iterator()) {
        if (a.canMoveTo(b)) {
          answer.add(Pair(a, b))
        }
      }
    }

    return answer
  }

  /**
   * Returns the [GridNode] at the specified x and y coordinates.
   *
   * @param x The x coordinate or column.
   * @param y The y coordinate or row.
   * @return The [GridNode] at the specified x and y coordinates.
   */
  operator fun get(x: Int, y: Int): GridNode {
    Objects.checkIndex(x, grid.columns)
    Objects.checkIndex(y, grid.rows)

    return grid[x, y]
  }

  /**
   * Returns the [GridNode] at the specified x and y coordinates.
   *
   * @param point The [Point2D] to get data from.
   * @return The [GridNode] at the specified x and y coordinates.
   */
  operator fun get(point: Point2D<Int>): GridNode {
    return get(point.x(), point.y())
  }

  /**
   * Sets the [GridNode] at the specified x and y coordinates and also returns it.
   *
   * @param x The x coordinate or column.
   * @param y The y coordinate or row.
   * @param node The [GridNode] to set at the specified x and y coordinates.
   * @return The [GridNode] at the specified x and y coordinates.
   */
  operator fun set(x: Int, y: Int, node: GridNode): GridNode {
    Objects.checkIndex(x, grid.columns)
    Objects.checkIndex(y, grid.rows)
    grid[x, y] = node
    return grid[x, y]
  }

  /**
   * Returns the shortest path to move the data from the goal to the start node. The returned
   * pair contains the number of steps in the first element and the path in the second element.
   *
   * @param destination The [GridNode] to end at. It is the node that all commands are issued from,
   * and it is the node where we want to move the goal data to.
   * @param goal The [GridNode] that initially contains the goal data.
   * @return A [Pair] where the first element is the number of steps and the second element
   * is the path.
   */
  fun shortestPathToMoveData(
      destination: GridNode, goal: GridNode
  ): Pair<Int, List<String>> {
    val backup = grid.copy()
    val path = findPathToGoal(backup, goal, destination)
    check(path.first >= 0) { "Unable to find a path from the goal data to the destination!" }

    var emptyNode = getEmptyNode(backup)
    val moves = mutableListOf<String>()
    var goalDataAt = goal

    path.second.forEach { moveTo ->
      val emptyPath = moveEmptyTo(backup, emptyNode, goalDataAt, moveTo)
      moves.addAll(getEmptyMoves(emptyNode, emptyPath.second))
      check(backup[moveTo.point].isEmpty()) { "The empty space is not empty!" }
      val dataMoveTo = backup[moveTo.point]
      val (newTo, newFrom) = goalDataAt.moveTo(dataMoveTo)
      moves.add("Moved goal data from ${goalDataAt.id} to ${moveTo.id}")
      emptyNode = newFrom
      goalDataAt = newTo
      backup[newTo.point] = newTo
      backup[newFrom.point] = newFrom
    }
    check(goalDataAt == destination) { "Failed to moved goal data to its destination!" }

    return Pair(moves.size, moves)
  }

  private fun getEmptyMoves(empty: GridNode, path: List<GridNode>): List<String> {
    val answer = mutableListOf<String>()
    var emptyAt = empty

    path.forEach {
      answer.add("Moved empty space from ${emptyAt.id} to ${it.id}")
      emptyAt = it
    }

    return answer
  }

  private fun moveEmptyTo(
    gridToUse: Grid2D<GridNode>, empty: GridNode, goalDataAt: GridNode, goal: GridNode
  ): Pair<Int, List<GridNode>> {
    if (empty == goal) {
      return Pair(0, listOf())
    }

    val path = findPathToGoal(gridToUse, empty, goal, goalDataAt)
    check(path.first >= 0) { "Unable to find a path from the empty space to its goal space!" }

    var emptyAt = empty

    path.second.forEach { node ->
      val (newTo, newFrom) = node.moveTo(emptyAt)
      gridToUse[newTo.point] = newTo
      gridToUse[newFrom.point] = newFrom
      emptyAt = newFrom
    }

    check(emptyAt == goal) { "Unable to move the empty space to its goal space!" }

    return path
  }

  private fun pathResultToGridNodePair(
      result: PathfinderResult<GridNode, Int>?
  ): Pair<Int, List<GridNode>> {
    return when (result == null) {
      true -> Pair(-1, listOf())
      else -> {
        val moves = mutableListOf<GridNode>()
        var first = true
        for (vertex in result.vertices) {
          if (first) {
            first = false
            continue
          }
          moves.add(vertex.get())
        }

        Pair(result.cost, moves)
      }
    }
  }

  private fun buildPathToGoalExpander(
    gridToUse: Grid2D<GridNode>, ignore: GridNode? = null
  ): BiFunction<Graph<GridNode, Int>, Vertex<GridNode, Int>, Boolean> {
    return BiFunction<Graph<GridNode, Int>, Vertex<GridNode, Int>, Boolean> @SuppressFBWarnings
    { graph, vertex ->
      val current = vertex.get()!!

      if (expanded.contains(current.id)) {
        false
      } else {
        expanded.add(current.id)
        val next = nextStates(gridToUse, current, ignore)

        next.map { it.vertex }
            .forEach {
              graph.add(it)
              graph.add(current.id, it.id, "${current.id}-${it.id}", 1)
            }
        true
      }
    }
  }

  private fun buildPathToGoalHeuristic(
      goal: GridNode
  ): Heuristic<GridNode, Int> {
    val heuristic = Heuristic<GridNode, Int> { _, vertex ->
      val node = vertex.get()!!

      node.point.manhattanDistance(goal.point).toDouble()
    }
    return heuristic
  }

  private fun findPathToGoal(
    gridToUse: Grid2D<GridNode>, start: GridNode, finish: GridNode, ignore: GridNode? = null
  ): Pair<Int, List<GridNode>> {
    expanded.clear()
    val initialState = Vertex<GridNode, Int>(start.id, start)
    val goalState = Vertex<GridNode, Int>(finish.id, finish)
    val expander = buildPathToGoalExpander(gridToUse, ignore)
    val graph = Graph<GridNode, Int>("find-data-path-${Instant.now().nano}")
    val pathFinder = AlphaStarPathfinder<GridNode, Int>()
    val properties = Properties()
    var shortestPath: PathfinderResult<GridNode, Int>? = null

    graph.add(initialState)
    graph.add(goalState)
    properties[PathfinderProperties.SUM_PATH] = true
    properties[PathfinderProperties.STARTING_VERTEX_ID] = initialState.id
    properties[PathfinderProperties.ENDING_VERTEX_ID] = goalState.id
    properties[PathfinderProperties.HEURISTIC] = buildPathToGoalHeuristic(finish)

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
    return pathResultToGridNodePair(shortestPath)
  }

  /**
   * An available move is a node whose data isn't huge.
   *
   * @param gridNode The current state to build the next states from.
   * @param ignore Optional node to ignore and so no new states for that node will be generated.
   * @return A [List] of [GridNode] that this node can potentially swap data with.
   */
  private fun nextStates(
    gridToUse: Grid2D<GridNode>, gridNode: GridNode, ignore: GridNode? = null
  ): List<GridNode> {
    val answer = mutableListOf<GridNode>()

    val currentPoint = gridNode.point

    for (direction in Direction.CARDINAL_DIRECTIONS) {
      val point = currentPoint.pointInDirection(direction)

      if (!gridToUse.isOnGrid(point)
          || gridToUse[point] == DEAD_NODE
          || gridToUse[point] == ignore) {
        continue
      }

      val node = gridToUse[point]

      if (!node.isHuge()) {
        answer.add(node)
      }
    }
    return answer
  }

  private fun getEmptyNode(gridToUse: Grid2D<GridNode>): GridNode {
    var answer: GridNode? = null

    for (node in gridToUse) {
      if (node.isEmpty()) {
        answer = node
        break
      }
    }
    check(answer != null) { "Unable to find an empty node!" }

    return answer
  }
}
