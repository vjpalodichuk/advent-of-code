package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.collection.PriorityQueueSet
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Edge
import com.capital7software.aoc.lib.graph.Vertex
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.Properties
import java.util.concurrent.atomic.AtomicLong
import java.util.function.BiFunction
import java.util.function.Function

/**
 * This pathfinder will explore a path in a [Graph] until it hits
 * the [PathfinderProperties.MAX_COST] or it runs out of vertices to traverse. This pathfinder
 * **does not** build any actual paths. Instead, it calls the valid handler with a list of
 * all the unique vertices it encountered during its search. The invalid handler is ignored.
 *
 * The [PathfinderResult]:
 *
 * - The start and end vertices will be an instance of the same start [Vertex].
 * - There is no cost value.
 * - The [Edge] list will be empty.
 * - The [Vertex] list will contain the unique vertices encountered during the find operation.
 * All the vertices must have been encountered within the [PathfinderProperties.MAX_COST]
 * specified in the properties of the find operation.
 *
 * Required [PathfinderProperties]:
 *
 * - [PathfinderProperties.STARTING_VERTEX_ID] is required and the [Vertex] must already exist
 * in the [Graph] that is passed to the [find] operations.
 * - [PathfinderProperties.MAX_COST] is required and must be greater than 0. A find operation will
 * continue to explore a path up to this cost is reached.
 *
 * Performance:
 *
 * This implementation behaves in a Depth First Search (DFS) manner with a LIFO queue and limit.
 * The higher the limit the longer a search will take. A [Vertex] is only explored once unless a
 * lower cost to that [Vertex] is found. Although this implementation does not use a [Heuristic]
 * function, it does use a [PriorityQueueSet] and explores paths with a lower
 * [PathfinderState.cost] first.
 *
 */
class ExplorerPathfinder<T : Comparable<T>, E : Comparable<E>>
  : Pathfinder<PathfinderResult<T, E>, T, E>, DynamicPathfinder<PathfinderResult<T, E>, T, E> {
  override fun find(
      graph: Graph<T, E>,
      properties: Properties,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?
  ) {
    findInternal(graph, properties, graphExpander, valid)
  }

  override fun find(
      graph: Graph<T, E>,
      properties: Properties,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?
  ) {
    findInternal(graph, properties, null, valid)
  }

  private fun findInternal(
      graph: Graph<T, E>,
      properties: Properties,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>
  ) {
    require(!PathfinderProperties.notValid(properties)) {
      "The provided Properties are invalid: $properties"
    }

    // All wandering must have a beginning
    val startingVertex = PathfinderProperties.getStartingVertex(graph, properties)
    expandGraph(graph, startingVertex, graphExpander)

    // And an end!
    val maxCost = PathfinderProperties.getMaxCost(properties)

    check(maxCost > 0.0) { "maxCost must be a positive number: $maxCost"}

    val initialState = buildState(0, startingVertex)

    val vertices = wanderAbout(initialState, graph, maxCost, graphExpander)
    val result = PathfinderResult.buildPathResult(
        vertices, listOf(), 1, false, true
    )
    valid.apply(result)
  }

  @SuppressFBWarnings
  private fun wanderAbout(
      initialState: PathfinderState<T, E>,
      graph: Graph<T, E>,
      maxCost: Double,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?
  ): List<Vertex<T, E>> {
    val queue: PriorityQueueSet<PathfinderState<T, E>> = PriorityQueueSet()
    queue.offer(initialState)
    val scores = HashMap<PathfinderState<T, E>, PathfinderState<T, E>>()
    scores[initialState] = initialState
    val ids = AtomicLong(1)

    while (!queue.isEmpty) {
      val current = queue.poll()
      check(current != null) { "null PathfinderState" }

      if (maxCost < current.cost) {
        continue
      }

      for (state in nextStates(current, graph, graphExpander, ids)) {
        if (queue.contains(state)) {
          val existing = scores[state] ?: error("Missing saved state.")
          if (state.cost < existing.cost) {
            // Found a shorter path!
            val index = queue.indexOf(state)
            queue[index] = state
            scores.remove(state)
            scores[state] = state
          }
        } else {
          val tempScore = current.cost + current.vertex.get(state.vertex.id).asDouble()

          if (!scores.containsKey(state) || tempScore < scores[state]!!.cost) {
            queue.offer(state)
            scores.remove(state)
            scores[state] = state
          }
        }
      }
    }
    return scores.filter { it.key.cost <= maxCost }.map { it.value.vertex }
  }

  private fun expandGraph(
      graph: Graph<T, E>,
      vertex: Vertex<T, E>,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?
  ): Boolean = graphExpander?.apply(graph, vertex) ?: false

  private fun buildState(
      id: Long,
      vertex: Vertex<T, E>,
      cost: Double = 0.0,
      parent: PathfinderState<T, E>? = null
  ): PathfinderState<T, E> = PathfinderState(
      id, vertex, 0.0, cost, parent
  )

  private fun nextStates(
      current: PathfinderState<T, E>,
      graph: Graph<T, E>,
      graphExpander: BiFunction<Graph<T, E>, Vertex<T, E>, Boolean>?,
      ids: AtomicLong
  ): List<PathfinderState<T, E>> {
    expandGraph(graph, current.vertex, graphExpander)
    val answer = mutableListOf<PathfinderState<T, E>>()
    current.vertex.edges.forEach {
      val vertex = graph.get(it.target)
      val weight = it.asDouble()
      val cost = current.cost + weight
      answer.add(buildState(ids.getAndIncrement(), vertex, cost, current))
    }

    return answer
  }
}
