package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.collection.PriorityQueueSet
import com.capital7software.aoc.lib.graph.Edge
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import java.util.Properties
import java.util.concurrent.atomic.AtomicLong
import java.util.function.BiFunction
import java.util.function.Function

/**
 * This class implements both the [Pathfinder] and [DynamicPathfinder] interfaces. It is an
 * implementation of the A* Pathfinding Algorithm which can be found at
 * [Wikipedia](https://en.wikipedia.org/wiki/A*_search_algorithm). Its sole purpose is to find
 * the shortest path from the start [Vertex] to the end [Vertex] and immediately stops
 * searching once a valid path is found.
 *
 * Required [PathfinderProperties]:
 *
 * - [PathfinderProperties.STARTING_VERTEX_ID] is required and the [Vertex] must already exist
 * in the [Graph] that is passed to the [find] operations.
 * - [PathfinderProperties.ENDING_VERTEX_ID] is required and the [Vertex] must already exist
 * in the [Graph] that is passed to the [find] operations.
 * - [PathfinderProperties.HEURISTIC] is required and needs to be set to an instance of an
 * object that implements the [Heuristic] interface.
 *
 * Optional [PathfinderProperties]:
 *
 * - [PathfinderProperties.SUM_PATH] is optional. It controls if the cost of the path will be
 * provided in the [PathfinderResult] that is passed to the valid path handler when a path is
 * found.
 *
 * Performance:
 *
 * This implementation uses a [PriorityQueueSet] to prioritize which paths to continue to expand.
 * The [PriorityQueueSet] allows for contains operations to be performed in O(1) time.
 * Additionally, if a path with a lower cost is discovered, and it is currently in the queue, it
 * is updated via [PriorityQueueSet.set] which updates the existing element in O(1) time and
 * then adjusts the placement of the updated path in the queue in O(log n) time.
 *
 * The amount of time it takes to find the shortest path is heavily dependent on the [Heuristic]
 * function and the weight of the edges between adjacent vertices. If the [Edge] doesn't contain a
 * numerical weight, then a value of 1 is used as the weight. The [Heuristic] is the estimated
 * remaining cost from the current [Vertex] to the end [Vertex] and it is used to prioritize
 * which paths to expand along with the cost of the path up to that point. A poor [Heuristic]
 * function will lead to poor performance in finding the shortest path.
 *
 * Halting:
 *
 * If a path exists from start to end, one will be found and returned. The shortest path is only
 * guaranteed to be found if the [Heuristic] function is
 * [admissible](https://en.wikipedia.org/wiki/Admissible_heuristic). If the [Heuristic] is also
 * [consistent](https://en.wikipedia.org/wiki/Consistent_heuristic) then a [Vertex] will only be
 * explored once. If no path from start to end is found, then the find operation will complete
 * when no intermediate path through any of the vertices is found to be shorter.
 */
class AlphaStarPathfinder<T : Comparable<T>, E : Comparable<E>>
  : Pathfinder<PathfinderResult<T, E>, T, E>, DynamicPathfinder<PathfinderResult<T, E>, T, E> {

  override fun find(
      graph: Graph<T, E>,
      properties: Properties,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?
  ) {
    findInternal(graph, properties, graphExpander, valid, invalid)
  }

  override fun find(
      graph: Graph<T, E>,
      properties: Properties,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?
  ) {
    findInternal(graph, properties, null, valid, invalid)
  }

  private fun findInternal(
      graph: Graph<T, E>,
      properties: Properties,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?
  ) {
    require(!PathfinderProperties.notValid(properties)) {
      "The provided Properties are invalid: $properties"
    }

    // Paths will be investigated for these nodes.
    val startingVertex = PathfinderProperties.getStartingVertex(graph, properties)
    val finishVertex = PathfinderProperties.getEndingVertex(graph, properties)
    expandGraph(graph, startingVertex, graphExpander)
    expandGraph(graph, finishVertex, graphExpander)

    // Sum the edge weights?
    val sumRequired = PathfinderProperties.isSumRequired(properties)

    // Get the Heuristic function.
    val heuristic: Heuristic<T, E> = PathfinderProperties.getHeuristic(properties)
    val initialState = buildState(0, graph, startingVertex, heuristic)

    val finalState = findShortestPath(initialState, graph, finishVertex, heuristic, graphExpander)

    if (finalState != null) {
      val path = finalState.toList()
      val edges = getEdges(path)
      val result = PathfinderResult.buildPathResult(
          path, edges, finalState.id.toInt(), false, sumRequired
      )
      valid.apply(result)
    } else if (invalid != null) {
      val result = PathfinderResult(-1, startingVertex, finishVertex, listOf(), listOf())
      invalid.apply(result)
    }
  }

  private fun findShortestPath(
      initialState: PathfinderState<T, E>,
      graph: Graph<T, E>,
      finish: Vertex<T, E>,
      heuristic: Heuristic<T, E>,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?
  ): PathfinderState<T, E>? {
    val queue = PriorityQueueSet<PathfinderState<T, E>>()
    queue.offer(initialState)
    val scores = HashMap<PathfinderState<T, E>, PathfinderState<T, E>>()
    scores[initialState] = initialState
    val ids = AtomicLong(1)

    while (!queue.isEmpty) {
      val current = queue.poll()
      check(current != null) { "null PathfinderState" }

      if (finish == current.vertex) {
        return current
      }

      for (state in nextStates(current, graph, heuristic, graphExpander, ids)) {
        if (queue.contains(state)) {
          val existing = scores[state] ?: error("Missing existing saved state.")
          if (state.cost < existing.cost) {
            // Found a shorter path!
            val index = queue.indexOf(state)
            queue[index] = state
            scores.remove(state) // Free the old state!
            scores[state] = state
          }
        } else {
          val tempScore = current.cost + current.vertex.get(state.vertex.id).asDouble()

          if (!scores.containsKey(state) || tempScore < scores[state]!!.cost) {
            scores.remove(state) // Free the old state!
            scores[state] = state
            queue.offer(state)
          }
        }
      }
    }
    return null
  }

  private fun expandGraph(
      graph: Graph<T, E>,
      vertex: Vertex<T, E>,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?
  ): Boolean = graphExpander?.apply(graph, vertex) ?: false

  private fun buildState(
      id: Long,
      graph: Graph<T, E>,
      vertex: Vertex<T, E>,
      heuristic: Heuristic<T, E>,
      cost: Double = 0.0,
      parent: PathfinderState<T, E>? = null
  ): PathfinderState<T, E> = PathfinderState(
      id, vertex, heuristic.calculate(graph, vertex), cost, parent
  )

  private fun nextStates(
      current: PathfinderState<T, E>,
      graph: Graph<T, E>,
      heuristic: Heuristic<T, E>,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?,
      ids: AtomicLong
  ): List<PathfinderState<T, E>> {
    expandGraph(graph, current.vertex, graphExpander)
    val answer = mutableListOf<PathfinderState<T, E>>()
    current.vertex.edges.forEach {
      val vertex = graph.get(it.target)
      val weight = it.asDouble()
      val cost = current.cost + weight
      answer.add(buildState(ids.getAndIncrement(), graph, vertex, heuristic, cost, current))
    }

    return answer
  }

  private fun getEdges(path: List<Vertex<T, E>>): List<Edge<E>> {
    val answer = mutableListOf<Edge<E>>()

    for (i in 0 until path.size - 1) {
      answer.add(path[i].get(path[i + 1].id))
    }

    return answer
  }
}
