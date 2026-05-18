package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.collection.PriorityQueue
import com.capital7software.aoc.lib.graph.Edge
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.util.isNearlyEqual
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.Properties
import java.util.function.Function

/**
 * A [Pathfinder] implementation that finds all vertices and edges that participate in at least one
 * shortest path from the configured starting vertex to the configured ending vertex.
 *
 * This pathfinder calls the valid handler at most once. The returned [PathfinderResult] contains:
 *
 * - the configured starting vertex as the start
 * - the configured ending vertex as the end
 * - all vertices that appear on at least one shortest path
 * - all edges that appear on at least one shortest path
 *
 * The [PathfinderResult.cost] is not populated because this implementation supports any edge weight
 * type whose [Edge.asDouble] value can be used for path-cost comparison.
 *
 * Required [PathfinderProperties]:
 *
 * - [PathfinderProperties.STARTING_VERTEX_ID]
 * - [PathfinderProperties.ENDING_VERTEX_ID]
 *
 * Edge weights are interpreted using [Edge.asDouble]. If an edge has no numeric weight, it is treated
 * as having weight 1.0.
 */
class AllShortestPathsPathfinder<T : Comparable<T>, E : Comparable<E>>
  : Pathfinder<PathfinderResult<T, E>, T, E> {
  @SuppressFBWarnings
  override fun find(
      graph: Graph<T, E>,
      properties: Properties,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?
  ) {
    require(!PathfinderProperties.notValid(properties)) {
      "The provided Properties are invalid: $properties"
    }

    val start = PathfinderProperties.getStartingVertex(graph, properties)
    val end = PathfinderProperties.getEndingVertex(graph, properties)
    val distanceFromStart = shortestDistances(graph, start.id)
    val distanceToEnd = shortestDistances(graph, end.id, reverse = true)
    val shortestCost = distanceFromStart[end.id] ?: Double.POSITIVE_INFINITY

    if (shortestCost == Double.POSITIVE_INFINITY) {
      invalid?.apply(PathfinderResult(-1, start, end, listOf(), listOf()))
      return
    }

    val shortestPathVertexIds = linkedSetOf<String>()
    val shortestPathEdges = mutableListOf<Edge<E>>()

    graph.edges.forEach { edge ->
      val sourceDistance = distanceFromStart[edge.source] ?: Double.POSITIVE_INFINITY
      val targetDistance = distanceToEnd[edge.target] ?: Double.POSITIVE_INFINITY
      val weight = edge.asDouble()
      val totalDistance = sourceDistance + weight + targetDistance

      if (
          sourceDistance != Double.POSITIVE_INFINITY
          && targetDistance != Double.POSITIVE_INFINITY
          && totalDistance isNearlyEqual shortestCost
      ) {
        shortestPathVertexIds.add(edge.source)
        shortestPathVertexIds.add(edge.target)
        shortestPathEdges.add(edge)
      }
    }

    val shortestPathVertices = shortestPathVertexIds
        .mapNotNull { graph.getVertex(it).orElse(null) }

    val result = PathfinderResult(
        0,
        start,
        end,
        shortestPathVertices,
        shortestPathEdges
    )

    valid.apply(result)
  }

  @SuppressFBWarnings
  private fun shortestDistances(
      graph: Graph<T, E>,
      startingVertexId: String,
      reverse: Boolean = false
  ): Map<String, Double> {
    val distances = graph.vertexIds.associateWith { Double.POSITIVE_INFINITY }.toMutableMap()
    val queue = PriorityQueue<QueueEntry>(graph.vertices.size, compareBy { it.cost })
    val incomingEdges = if (reverse) buildIncomingEdges(graph) else emptyMap()

    distances[startingVertexId] = 0.0
    queue.offer(QueueEntry(startingVertexId, 0.0))

    while (queue.isNotEmpty()) {
      val current = queue.poll()
      val knownDistance = distances[current.vertexId] ?: Double.POSITIVE_INFINITY

      if (!(current.cost isNearlyEqual knownDistance)) {
        continue
      }

      val edges = if (reverse) {
        incomingEdges[current.vertexId].orEmpty()
      } else {
        graph.getEdges(current.vertexId).map { SearchEdge(it.target, it.asDouble()) }
      }

      edges.forEach { edge ->
        val nextCost = current.cost + edge.weight
        val previousCost = distances[edge.targetId] ?: Double.POSITIVE_INFINITY

        if (nextCost < previousCost) {
          distances[edge.targetId] = nextCost
          queue.offer(QueueEntry(edge.targetId, nextCost))
        }
      }
    }

    return distances
  }

  private fun buildIncomingEdges(graph: Graph<T, E>): Map<String, List<SearchEdge>> {
    val incoming = mutableMapOf<String, MutableList<SearchEdge>>()

    graph.edges.forEach { edge ->
      incoming
          .computeIfAbsent(edge.target) { mutableListOf() }
          .add(SearchEdge(edge.source, edge.asDouble()))
    }

    return incoming
  }

  private data class QueueEntry(
      val vertexId: String,
      val cost: Double
  )

  private data class SearchEdge(
      val targetId: String,
      val weight: Double
  )
}
