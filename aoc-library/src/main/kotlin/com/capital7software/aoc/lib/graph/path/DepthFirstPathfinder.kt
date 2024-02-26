package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.graph.Edge
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import com.capital7software.aoc.lib.graph.path.PathfinderStatus.FINISHED
import java.util.Properties
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BiFunction
import java.util.function.Function


/**
 * This class implements both the [Pathfinder] and [DynamicPathfinder] interfaces. It is an
 * implementation of a Depth-first Search which can be found at
 * [Wikipedia](https://en.wikipedia.org/wiki/Depth-first_search).
 * Its sole purpose is to find every path from the start [Vertex] to the end [Vertex] and
 * continues until all paths are exhausted or until [FINISHED] is returned by the valid or
 * invalid handlers.
 *
 *
 * Required [PathfinderProperties]:
 *
 *
 * - [PathfinderProperties.STARTING_VERTEX_ID] is required and the [Vertex] must already exist
 * in the [Graph] that is passed to the [find] operations.
 * - [PathfinderProperties.ENDING_VERTEX_ID] is required and the [Vertex] must already exist
 * in the [Graph] that is passed to the [find] operations.
 *
 *
 * Optional [PathfinderProperties]:
 *
 *
 * - [PathfinderProperties.SUM_PATH] is optional. It controls if the cost of the path will be
 * provided in the [PathfinderResult] that is passed to the valid path handler when a path is
 * found.
 *
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
class DepthFirstPathfinder<T : Comparable<T>, E : Comparable<E>>
/**
 * Instantiates a new and empty path builder instance.
 */
  : Pathfinder<PathfinderResult<T, E>, T, E>, DynamicPathfinder<PathfinderResult<T, E>, T, E> {
  override fun find(
      graph: Graph<T, E>,
      properties: Properties,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?
  ) {
    findInternal(graph, properties, valid, invalid, graphExpander)
  }

  override fun find(
      graph: Graph<T, E>,
      properties: Properties,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?
  ) {
    findInternal(graph, properties, valid, invalid, null)
  }

  private fun findInternal(
      graph: Graph<T, E>,
      properties: Properties,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?,
      expander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?,
  ) {
    require(!PathfinderProperties.notValid(properties)) {
      "The provided Properties are invalid: $properties"
    }

    // Paths will be investigated for these nodes.
    val startingVertex = PathfinderProperties.getStartingVertex(graph, properties)
    val endingVertex = PathfinderProperties.getEndingVertex(graph, properties)

    // Does a sum of the Edge weights need to be calculated?
    val sumRequired = PathfinderProperties.isSumRequired(properties)
    val pathId = AtomicInteger(0)

    val pathSoFar = ArrayList<Vertex<T, E>>()
    val edgesSoFar = ArrayList<Edge<E>>()
    val idsSoFar = HashSet<String>()

    pathSoFar.add(startingVertex) // Add the starting Vertex to the path.
    idsSoFar.add(startingVertex.id)

    findPath(
        startingVertex,
        pathId,
        sumRequired,
        endingVertex,
        pathSoFar,
        edgesSoFar,
        idsSoFar,
        valid,
        invalid,
        graph,
        expander
    )
  }

  private fun findPath(
      currentVertex: Vertex<T, E>,
      pathId: AtomicInteger,
      sumRequired: Boolean,
      endingVertex: Vertex<T, E>,
      path: MutableList<Vertex<T, E>>,
      edges: MutableList<Edge<E>>,
      visitedIds: MutableSet<String>,
      validCallback: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalidCallback: Function<PathfinderResult<T, E>, PathfinderStatus>?,
      graph: Graph<T, E>,
      expander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?,
  ): PathfinderStatus {
    // Check the stopping conditions.
    if (currentVertex == endingVertex) {
      return validCallback.apply(
          PathfinderResult.buildPathResult(
              path, edges, pathId.getAndIncrement(), false, sumRequired
          )
      )
    }

    var lastStatus: PathfinderStatus? = null
    expandGraph(graph, currentVertex, expander)

    // Go through all the neighbors
    for (vertex in neighbors(graph, currentVertex)) {
      if (!visitedIds.contains(vertex.id)
          && path.last().getEdge(vertex.id).isPresent
      ) {
        // Add the vertex and edge to the path and mark the vertex as visited.
        edges.add(path.last().getEdge(vertex.id).get())
        visitedIds.add(vertex.id)
        path.add(vertex)

        lastStatus = findPath(
            vertex,
            pathId,
            sumRequired,
            endingVertex,
            path,
            edges,
            visitedIds,
            validCallback,
            invalidCallback,
            graph,
            expander
        )

        // Clean-up
        path.removeLast()
        visitedIds.remove(vertex.id)
        edges.removeLast()

        if (lastStatus != PathfinderStatus.CONTINUE) {
          return lastStatus
        }
      }
    }

    if (lastStatus == null) {
      // Failed to find any vertices to add to the path!
      invalidCallback?.apply(PathfinderResult.buildPathResult(path, edges, pathId.get()))
      return PathfinderStatus.CONTINUE
    }
    return lastStatus
  }

  private fun expandGraph(
      graph: Graph<T, E>,
      vertex: Vertex<T, E>,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?
  ): Boolean = graphExpander?.apply(graph, vertex) ?: false

  private fun neighbors(graph: Graph<T, E>, vertex: Vertex<T, E>): List<Vertex<T, E>> {
    val answer = mutableListOf<Vertex<T, E>>()

    vertex.edges.forEach { answer.add(graph[it.target]) }

    return answer
  }
}
