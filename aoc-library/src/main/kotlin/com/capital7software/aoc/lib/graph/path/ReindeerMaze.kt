package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import com.capital7software.aoc.lib.grid.Grid2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.Properties

/**
 * ***--- Day 16: Reindeer Maze ---***
 *
 * Solves the Reindeer Maze by treating each reachable maze state as a graph vertex.
 *
 * A state is made up of:
 *
 * - the current point in the maze
 * - the direction the reindeer is facing
 *
 * This allows movement and rotation to be modeled as normal weighted graph edges:
 *
 * - rotating clockwise or counterclockwise costs 1000
 * - moving forward one tile costs 1
 *
 * The start state is the tile marked S facing East. The end tile can be reached while facing
 * any direction, so all four end states connect to one synthetic finish vertex with cost 0.
 */
class ReindeerMaze(input: List<String>) {
  private val grid: Grid2D<Char> = buildGrid(input)
  private val start: Point2D<Int> = findRequired('S')
  private val end: Point2D<Int> = findRequired('E')
  private val graph: Graph<MazeState, Long> = buildGraph()

  /**
   * Calculates and returns the lowest possible score to get from S to E.
   *
   * This uses [AlphaStarPathfinder] with a zero heuristic, making it equivalent to Dijkstra's
   * algorithm for this weighted graph.
   *
   * @return The lowest score, or -1 if no path exists.
   */
  fun lowestScore(): Long {
    return findShortestPath()?.cost ?: -1
  }

  /**
   * Calculates and returns the number of distinct tiles that are part of at least one best path.
   *
   * The graph vertices are directional states, but the puzzle asks for distinct maze tiles. This
   * method uses [AllShortestPathsPathfinder] to find every state that participates in at least one
   * shortest path, then counts the distinct [Point2D] values for those states.
   *
   * @return The number of distinct non-wall tiles on at least one best path, or 0 if no path exists.
   */
  @SuppressFBWarnings
  fun bestPathTileCount(): Long {
    val pathFinder = AllShortestPathsPathfinder<MazeState, Long>()
    val properties = Properties()
    var shortestPathTiles: PathfinderResult<MazeState, Long>? = null

    properties[PathfinderProperties.STARTING_VERTEX_ID] = MazeState(start, Direction.EAST).id
    properties[PathfinderProperties.ENDING_VERTEX_ID] = FINISH_ID

    pathFinder.find(
        graph,
        properties,
        {
          shortestPathTiles = it
          PathfinderStatus.FINISHED
        },
        null
    )

    return shortestPathTiles
        ?.vertices
        ?.mapNotNull { vertex -> vertex.value.orElse(null)?.point }
        ?.toSet()
        ?.size
        ?.toLong()
        ?: 0L
  }

  private fun findShortestPath(): PathfinderResult<MazeState, Long>? {
    val pathFinder = AlphaStarPathfinder<MazeState, Long>()
    val properties = Properties()
    var shortestPath: PathfinderResult<MazeState, Long>? = null

    properties[PathfinderProperties.SUM_PATH] = true
    properties[PathfinderProperties.STARTING_VERTEX_ID] = MazeState(start, Direction.EAST).id
    properties[PathfinderProperties.ENDING_VERTEX_ID] = FINISH_ID
    properties[PathfinderProperties.HEURISTIC] = Heuristic<MazeState, Long> { _, _ -> 0.0 }

    pathFinder.find(
        graph,
        properties,
        {
          shortestPath = it
          PathfinderStatus.FINISHED
        },
        null
    )

    return shortestPath
  }


  private fun buildGraph(): Graph<MazeState, Long> {
    val result = Graph<MazeState, Long>("reindeer-maze")

    for (y in 0..<grid.rows) {
      for (x in 0..<grid.columns) {
        if (grid[x, y] == WALL) {
          continue
        }

        val point = Point2D(x, y)

        Direction.CARDINALS.forEach { direction ->
          val state = MazeState(point, direction)
          result.add(state.vertex)
        }
      }
    }

    result.add(Vertex(FINISH_ID))

    for (y in 0..<grid.rows) {
      for (x in 0..<grid.columns) {
        if (grid[x, y] == WALL) {
          continue
        }

        val point = Point2D(x, y)

        Direction.CARDINALS.forEach { direction ->
          val state = MazeState(point, direction)

          addTurnEdge(result, state, direction.left)
          addTurnEdge(result, state, direction.right)
          addForwardEdge(result, state)

          if (point == end) {
            result.add(state.id, FINISH_ID, "${state.id}-$FINISH_ID", 0)
          }
        }
      }
    }

    return result
  }

  private fun addTurnEdge(
      graph: Graph<MazeState, Long>,
      state: MazeState,
      targetDirection: Direction
  ) {
    val target = MazeState(state.point, targetDirection)
    graph.add(state.id, target.id, "${state.id}-${target.id}", TURN_COST)
  }

  private fun addForwardEdge(graph: Graph<MazeState, Long>, state: MazeState) {
    val targetPoint = state.point.pointInDirection(state.direction)

    if (!grid.isOnGrid(targetPoint) || grid[targetPoint] == WALL) {
      return
    }

    val target = MazeState(targetPoint, state.direction)
    graph.add(state.id, target.id, "${state.id}-${target.id}", FORWARD_COST)
  }

  private fun buildGrid(input: List<String>): Grid2D<Char> {
    require(input.isNotEmpty()) { "Input cannot be empty!" }

    val rows = input.size
    val columns = input.first().length

    require(input.all { it.length == columns }) {
      "All input rows must have the same length!"
    }

    val items = Array(columns * rows) { WALL }

    input.forEachIndexed { y, line ->
      line.forEachIndexed { x, char ->
        items[y * columns + x] = char
      }
    }

    return Grid2D(columns, rows, items)
  }

  private fun findRequired(target: Char): Point2D<Int> {
    return grid.findFirst(target).orElseThrow {
      IllegalStateException("Unable to find required tile: $target")
    }
  }

  private data class MazeState(
      val point: Point2D<Int>,
      val direction: Direction
  ) : Comparable<MazeState> {
    val id: String = "${point.id()},${direction.name}"
    val vertex: Vertex<MazeState, Long> = Vertex(id, this)

    override fun compareTo(other: MazeState): Int {
      val pointCompare = point.compareTo(other.point)

      return if (pointCompare != 0) {
        pointCompare
      } else {
        direction.compareTo(other.direction)
      }
    }
  }

  private companion object {
    private const val WALL = '#'
    private const val FINISH_ID = "finish"
    private const val FORWARD_COST = 1L
    private const val TURN_COST = 1000L
  }
}
