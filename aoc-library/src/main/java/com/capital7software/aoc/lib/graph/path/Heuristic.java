package com.capital7software.aoc.lib.graph.path;

import com.capital7software.aoc.lib.graph.Graph;
import com.capital7software.aoc.lib.graph.Vertex;

/**
 * Used by informed search / pathfinding algorithms to decide which paths to expand.
 *
 * <p><br>
 * The way the Heuristic is used is implementation dependent and expectations are
 * described by the interface or class that uses the Heuristic.
 *
 * @param <T> The type of the value held by the Vertices in the Graph.
 * @param <E> The type of the weight of an Edge.
 */
public interface Heuristic<T extends Comparable<T>, E extends Comparable<E>> {
  /**
   * Calculates and returns a value for the specified Graph and Vertex.
   *
   * @param graph The Graph being used.
   * @param vertex The Vertex being operated on.
   * @return The Heuristic value.
   */
  double calculate(Graph<T, E> graph, Vertex<T, E> vertex);
}
