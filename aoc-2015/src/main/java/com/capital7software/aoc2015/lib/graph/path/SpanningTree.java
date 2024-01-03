package com.capital7software.aoc2015.lib.graph.path;

import com.capital7software.aoc2015.lib.graph.Graph;
import org.jetbrains.annotations.NotNull;

/**
 * Algorithms that build spanning trees implement this interface.
 *
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public interface SpanningTree<T extends Comparable<T>, E extends Comparable<E>> {
    /**
     * Builds and returns a Graph that contains all the same Nodes as the source
     * Graph but with the minimum number of Edges that connect every Node in the source
     * Graph. How this is done and the specific characteristics of the SpanningTree is
     * implementation dependent.
     *
     * @param graph The source Graph to build a SpanningTree from.
     * @return A Graph that represents a SpanningTree of the Graph.
     */
    @NotNull
    Graph<T, E> spanningTree(@NotNull Graph<T, E> graph);
}
