package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.graph.Vertex
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * Used by [Pathfinder] and [DynamicPathfinder] to save state information during find
 * operations.
 *
 * The natural order is as follows:
 *
 * - The heuristic + cost is compared first.
 * - If the heuristics + cost are equal, then the IDs are compared. The comparison of the
 * IDs is done in reverse order such that recent states are preferred over existing states. This
 * mimics the behavior of a LIFO queue.
 *
 * **Two states are considered equal if they have the same vertex.**
 *
 * The heuristic and cost tend to have an inverse relationship.
 *
 * - As a find operation progresses, states with lower heuristic + cost scores are considered
 * first as it represents an estimate of the total cost to reach the goal from the start state.
 * - The closer the goal is the lower the heuristic should be but, the cost tends to go up as
 * the find operation progresses as it represents the sum of the path up to this point.
 * - If two states have the same heuristic + cost the state with the higher id is selected
 * first so that the queue operates in a LIFO manner.
 *
 * @param id The id used for this state.
 * @param vertex The [Vertex] this state is for. Two states are equal if their vertices
 * are equal.
 * @param heuristic The heuristic value of this state.
 * @param cost The total cost of the path up to this point.
 * @param parent The parent of this state, which will be null for the initial state.
 */
@SuppressFBWarnings
data class PathfinderState<T : Comparable<T>, E : Comparable<E>>(
    val id: Long,
    val vertex: Vertex<T, E>,
    val heuristic: Double = 0.0,
    val cost: Double = 0.0,
    val parent: PathfinderState<T, E>? = null
) : Comparable<PathfinderState<T, E>> {
  /**
   * The cost + heuristic
   *
   * @return The cost + heuristic
   */
  fun score(): Double = cost + heuristic

  override fun compareTo(other: PathfinderState<T, E>): Int {
    val comp = score().compareTo(other.score())

    return if (comp != 0) {
      comp
    } else {
      other.id.compareTo(id)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is PathfinderState<*, *>) return false

    if (vertex != other.vertex) return false

    return true
  }

  override fun hashCode(): Int {
    return vertex.hashCode()
  }

  /**
   * Traverses the parent vertices and returns an ordered list of vertices that make up
   * the path up to this point.
   *
   * @return An ordered list of vertices that make up the path up to this point.
   */
  fun toList(): List<Vertex<T, E>> {
    val answer = arrayListOf<Vertex<T, E>>()

    toList(answer)

    return answer
  }

  private fun toList(list: MutableList<Vertex<T, E>>) {
    parent?.toList(list)
    list.add(vertex)
  }
}
