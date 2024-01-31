package com.capital7software.aoc.lib.graph.network;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Graph;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;


/**
 * See <a href="https://www.baeldung.com/java-spanning-trees-kruskal">...</a> for a detailed
 * explanation of how Kruskal's algorithm works.
 *
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public class MinimumSpanningTreeKruskal<T extends Comparable<T>, E extends Comparable<E>>
    extends AbstractSpanningTreeKruskal<T, E> {
  /**
   * Instantiates a new and empty spanning tree builder instance.
   */
  public MinimumSpanningTreeKruskal() {
    super();
  }

  @Override
  public @NotNull Collection<Edge<E>> build(@NotNull Graph<T, E> graph) {
    return buildSpanningTree(graph, false);
  }
}
