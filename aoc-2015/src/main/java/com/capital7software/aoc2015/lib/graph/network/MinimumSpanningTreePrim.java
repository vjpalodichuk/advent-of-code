package com.capital7software.aoc2015.lib.graph.network;

import com.capital7software.aoc2015.lib.collection.PriorityQueue;
import com.capital7software.aoc2015.lib.graph.Edge;
import com.capital7software.aoc2015.lib.graph.Graph;
import com.capital7software.aoc2015.lib.graph.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An implementation of Prim's MST algorithm.
 * <p>
 *     The algorithm can be informally described as:
 * <code>
 *     <ol>
 *         <li>
 *             Initialize a tree with a single vertex, chosen arbitrarily from the graph.
 *         </li>
 *         <li>
 *             Grow the tree by one edge: Of the edges that connect the tree to vertices not yet in the tree,
 *             find the minimum-weight edge, and transfer it to the tree.
 *         </li>
 *         <li>
 *             Repeat step 2 (until all vertices are in the tree).
 *         </li>
 *     </ol>
 * </code>
 * <p>
 *     A more formal description:
 *     <code>
 *         <ol>
 *             <li>
 *                 Associate with each vertex v of the graph a number C[v] (the cheapest cost of a connection to v)
 *                 and an edge E[v] (the edge providing that cheapest connection). To initialize these values, set
 *                 all values of C[v] to +∞ (or to any number larger than the maximum edge weight) and set each E[v]
 *                 to a special flag value indicating that there is no edge connecting v to earlier vertices.
 *             </li>
 *             <li>
 *                 Initialize an empty forest F and a set Q of vertices that have not yet been included
 *                 in F (initially, all vertices).
 *             </li>
 *             <li>
 *                 Repeat the following steps until Q is empty:
 *                 <ol>
 *                     <li>
 *                         Find and remove a vertex v from Q having the minimum possible value of C[v]
 *                     </li>
 *                     <li>
 *                         Add v to F
 *                     </li>
 *                     <li>
 *                         Loop over the edges vw connecting v to other vertices w. For each such edge,
 *                         if w still belongs to Q and vw has smaller weight than C[w], perform the following steps:
 *                     </li>
 *                     <li>
 *                         <ol>
 *                             <li>
 *                                 Set C[w] to the cost of edge vw
 *                             </li>
 *                             <li>
 *                                 Set E[w] to point to edge vw.
 *                             </li>
 *                         </ol>
 *                     </li>
 *                 </ol>
 *             </li>
 *             <li>
 *                 Return F, which specifically includes the corresponding edges in E
 *             </li>
 *         </ol>
 *     </code>
 * @param <T> The type of the value held by Vertices in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public class MinimumSpanningTreePrim<T extends Comparable<T>, E extends Comparable<E>> extends AbstractSpanningTreeKruskal<T, E> {
    private static class PrimVertex<T extends Comparable<T>, E extends Comparable<E>> implements Comparable<PrimVertex<T, E>> {
        private final Vertex<T, E> vertex;
        private Vertex<T, E> previous;
        private E key;

        public PrimVertex(Vertex<T, E> vertex) {
            this.vertex = vertex;
        }

        public Vertex<T, E> getVertex() {
            return vertex;
        }

        public Vertex<T, E> getPrevious() {
            return previous;
        }

        public void setPrevious(Vertex<T, E> previous) {
            this.previous = previous;
        }

        public E getKey() {
            return key;
        }

        public void setKey(E key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PrimVertex<?, ?> primVertex)) return false;
            return getVertex().getId().equals(primVertex.getVertex().getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getVertex());
        }

        @Override
        public String toString() {
            return "PrimVertex{" +
                    "vertex=" + vertex +
                    ", previous=" + previous +
                    ", key=" + key +
                    '}';
        }

        @Override
        public int compareTo(@NotNull MinimumSpanningTreePrim.PrimVertex<T, E> o) {
            return key.compareTo(o.getKey());
        }
    }

    private final E minValue;
    private final E maxValue;

    /**
     * Instantiates this instance where minValue is the lowest assignable value to a Vertex and maxValue is the
     * largest value to assign to a Vertex.
     * <p>
     * When build is called, all Vertices in the Graph are assigned the maxValue except for one that is picked as
     * random and is assigned the minValue. The Vertex assigned the minValue will be selected first and all other
     * Vertices will have their value updated as the algorithm progresses.
     * <p>
     *
     * @param minValue The lowest initial value to assign to the starting Vertex.
     * @param maxValue The highest initial value to assign to all other Vertices.
     */
    public MinimumSpanningTreePrim(E minValue, E maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public @NotNull Collection<Edge<T, E>> build(@NotNull Graph<T, E> graph) {
        var vertices = graph.getVertices();
        var vertexMap = new HashMap<String, PrimVertex<T, E>>(vertices.size());
        var vertexQueue = new PriorityQueue<PrimVertex<T, E>>(vertices.size());
        var edges = new ArrayList<Edge<T, E>>();
        var visited = new HashSet<String>(vertices.size());
        var first = new AtomicBoolean(true);

        for (var vertex : vertices) {
            vertex.getLeastWeightedEdgeValue().ifPresent(weight -> {
                var n = new PrimVertex<>(vertex);
                if (first.get()) {
                    n.setKey(minValue);
                    first.set(false);
                } else {
                    n.setKey(maxValue);
                }
                vertexMap.put(vertex.getId(), n);
                vertexQueue.offer(n);
            });
        }

        while (!vertexQueue.isEmpty()) {
            var vertex = Objects.requireNonNull(vertexQueue.poll());

            if (visited.contains(vertex.getVertex().getId())) {
                continue;
            }

            visited.add(vertex.getVertex().getId());

            if (vertex.getPrevious() != null) {
                var edge = vertex.getPrevious().getEdge(vertex.getVertex().getId());

                edge.ifPresent(edges::add);

                if (edges.size() == vertices.size() - 1) {
                    break; // We are done!
                }
            }

            for (var edge : vertex.getVertex().getEdgeSet()) {
                var weight = edge.getWeight();
                if (weight.isEmpty()) {
                    continue;
                }

                if (!visited.contains(edge.getTarget().getId())) {
                    var vertex2 = vertexMap.get(edge.getTarget().getId());
                    var realWeight = weight.get();

                    if (realWeight.compareTo(vertex2.getKey()) < 0) {
                      vertex2.setPrevious(edge.getSource());
                      vertex2.setKey(realWeight);
                      vertexQueue.adjustTopUp(vertex2);
                  }
                }
            }
        }
        return edges;
    }
}
