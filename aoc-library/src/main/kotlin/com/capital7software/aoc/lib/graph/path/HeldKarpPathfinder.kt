package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import java.util.Properties
import java.util.function.Function

/**
 * This class implements the [Pathfinder] interface. It doesn't support the [DynamicPathfinder]
 * interface as all vertices and their shortest paths must already be in the graph. It is an
 * implementation of the Held-Karp algorithm, which can be found at
 * [Wikipedia](https://en.wikipedia.org/wiki/Held–Karp_algorithm). Its sole purpose is to solve
 * the Traveling Salesman problem
 * [Wikipedia](https://en.wikipedia.org/wiki/Traveling_salesman_problem) and it does so in
 * exponential time, which is better than using brute-force which is factorial time.
 *
 * [Graph] Requirements:
 *
 * In order for this pathfinder to guarantee the path returned is the shortest, the graph must be
 * [complete](https://en.wikipedia.org/wiki/Complete_graph), and either directed or undirected.
 *
 * Required [PathfinderProperties]:
 *
 * - [PathfinderProperties.STARTING_VERTEX_ID] is required and the [Vertex] must already exist
 * in the [Graph] that is passed to the [find] operations.
 *
 * Optional [PathfinderProperties]:
 *
 * - [PathfinderProperties.SUM_PATH] is optional. It controls if the cost of the path will be
 * provided in the [PathfinderResult] that is passed to the valid path handler when a path is
 * found.
 *
 * - [PathfinderProperties.DETECT_CYCLES] is optional. It controls if the tour must be a cycle
 * that finishes where it started from.
 *
 * Performance:
 *
 * Time performance is O(n²*2ⁿ) and space is also O(n*2ⁿ).
 */
class HeldKarpPathfinder<T : Comparable<T>, E : Comparable<E>>
  : Pathfinder<PathfinderResult<T, E>, T, E> {
  override fun find(
      graph: Graph<T, E>,
      properties: Properties,
      valid: Function<PathfinderResult<T, E>, PathfinderStatus>,
      invalid: Function<PathfinderResult<T, E>, PathfinderStatus>?
  ) {
    TODO("Not yet implemented")
  }
}
