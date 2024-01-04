package com.capital7software.aoc2015.lib.graph.network;

import com.capital7software.aoc2015.lib.graph.Edge;
import com.capital7software.aoc2015.lib.graph.Graph;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Algorithms that build spanning trees implement this interface.
 *
 * @param <T> The type of the value held by Vertices in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public interface SpanningTree<T extends Comparable<T>, E extends Comparable<E>> {
    /**
     * Builds and returns a collection of edges that contains all the same Vertices as the source
     * Graph but with the minimum number of Edges that connect every Vertex in the source
     * Graph. How this is done and the specific characteristics of the SpanningTree is
     * implementation dependent.
     *
     * @param graph The source Graph to build a SpanningTree from.
     * @return A collection of the Edges in the SpanningTree in they order they were added.
     */
    @NotNull
    Collection<Edge<T, E>> build(@NotNull Graph<T, E> graph);
}
