package com.capital7software.aoc.lib.graph.network;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Graph;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * A SpanningTree implemented using Kruskal's greedy algorithm.
 *
 * <p><br>
 * <code>
 * Initialize an empty edge set T.
 * Sort all graph edges by the ascending order of their weight values.
 * foreach edge in the sorted edge list
 * Check whether it will create a cycle with the edges inside T.
 * If the edge doesn't introduce any cycles, add it into T.
 * If T has (V-1) edges, exit the loop.
 * return T
 * </code>
 *
 * <p><br>
 * See <a href="https://www.baeldung.com/java-spanning-trees-kruskal">...</a> for a detailed
 * explanation of how Kruskal's algorithm works.
 *
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public class MaximumSpanningTreeKruskal<T extends Comparable<T>, E extends Comparable<E>>
    extends AbstractSpanningTree<T, E> {
  /**
   * Instantiates a new and empty spanning tree builder instance.
   */
  public MaximumSpanningTreeKruskal() {
    super();
  }

  @Override
  public @NotNull Collection<Edge<E>> build(@NotNull Graph<T, E> graph) {
    return buildSpanningTree(graph, true);
  }
}
