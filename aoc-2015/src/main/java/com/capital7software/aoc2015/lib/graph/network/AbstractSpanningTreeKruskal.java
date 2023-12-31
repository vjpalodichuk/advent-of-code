package com.capital7software.aoc2015.lib.graph.network;


import com.capital7software.aoc2015.lib.graph.Edge;
import com.capital7software.aoc2015.lib.graph.Graph;
import com.capital7software.aoc2015.lib.graph.cycle.CycleDetectorDisjointSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A SpanningTree implemented using Kruskal's greedy algorithm.
 * <p>
 * <code>
 * Initialize an empty edge set T.
 * Sort all graph edges by the ascending order of their weight values.
 * foreach edge in the sorted edge list
 *     Check whether it will create a cycle with the edges inside T.
 *     If the edge doesn't introduce any cycles, add it into T.
 *     If T has (V-1) edges, exit the loop.
 * return T
 * </code>
 * See <a href="https://www.baeldung.com/java-spanning-trees-kruskal">...</a> for a detailed
 * explanation of how Kruskal's algorithm works.
 *
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
abstract public class AbstractSpanningTreeKruskal<T extends Comparable<T>, E extends Comparable<E>>
        implements SpanningTree<T, E> {

    /**
     * The workhorse of the algorithm. It builds the SpanningTree one Edge at a time. The returned
     * SpanningTree with either be a Minimum or Maximum distance SpanningTree depending on the value
     * passed in for maximum.
     *
     * @param graph The Graph to build the SpanningTree from.
     * @param maximum If false, a minimum SpanningTree is returned; otherwise a maximum one is returned.
     * @return THe SpanningTree of the specified graph.
     */
    @NotNull
    protected Collection<Edge<T, E>> buildSpanningTree(@NotNull Graph<T, E> graph, boolean maximum) {
        var edges = Objects.requireNonNull(graph).getEdges();
        List<Edge<T, E>> edgeList;

        if (maximum) {
            edgeList = edges.stream()
                    .filter(edge -> edge.getWeight().isPresent())
                    .sorted(Collections.reverseOrder(Comparator.comparing(edge -> edge.getWeight().get())))
                    .toList();
        } else {
            edgeList = edges.stream()
                    .filter(edge -> edge.getWeight().isPresent())
                    .sorted(Comparator.comparing(edge -> edge.getWeight().get()))
                    .toList();
        }

        var vertexIds = graph.getVertexIds();
        var detector = new CycleDetectorDisjointSet(vertexIds);

        // We are done when the number of edges in the spanning tree is one less than the number of Nodes!
        int edgeCount = 0;

        var spanningTree = new Graph<T, E>("kruskal-spanning-tree-" + graph.getName());
        final var result = new ArrayList<Edge<T, E>>(vertexIds.size());

        for (var edge : edgeList) {
            if (detector.detect(edge.getSource().getId(), edge.getTarget().getId())) {
                continue;
            }
            spanningTree.addAsNew(edge);
            edgeCount++;
            result.add(edge);
            if (edgeCount == vertexIds.size() - 1) {
                break; // We are all done!!
            }
        }
        return result;
    }
}
