package com.capital7software.aoc2015.lib.graph.network;

import com.capital7software.aoc2015.lib.graph.Edge;
import com.capital7software.aoc2015.lib.graph.Graph;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * See <a href="https://www.baeldung.com/java-spanning-trees-kruskal">...</a> for a detailed
 * explanation of how Kruskal's algorithm works.
 *
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public class MaximumSpanningTreeKruskal<T extends Comparable<T>, E extends Comparable<E>> extends AbstractSpanningTreeKruskal<T, E> {
    @Override
    public @NotNull Collection<Edge<T, E>> build(@NotNull Graph<T, E> graph) {
        return buildSpanningTree(graph, true);
    }
}
