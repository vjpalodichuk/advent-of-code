package com.capital7software.aoc2015.lib.graph.path;

import com.capital7software.aoc2015.lib.collection.PriorityQueue;
import com.capital7software.aoc2015.lib.graph.Edge;
import com.capital7software.aoc2015.lib.graph.Graph;
import com.capital7software.aoc2015.lib.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
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
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public class MinimumSpanningTreePrim<T extends Comparable<T>, E extends Comparable<E>> extends AbstractSpanningTreeKruskal<T, E> {
    private static class PrimNode<T extends Comparable<T>, E extends Comparable<E>> implements Comparable<PrimNode<T, E>> {
        private final Node<T, E> node;
        private Node<T, E> previous;
        private E key;

        public PrimNode(Node<T, E> node) {
            this.node = node;
        }

        public Node<T, E> getNode() {
            return node;
        }

        public Node<T, E> getPrevious() {
            return previous;
        }

        public void setPrevious(Node<T, E> previous) {
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
            if (!(o instanceof PrimNode<?, ?> primNode)) return false;
            return getNode().getId().equals(primNode.getNode().getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getNode());
        }

        @Override
        public String toString() {
            return "PrimNode{" +
                    "node=" + node +
                    ", previous=" + previous +
                    ", key=" + key +
                    '}';
        }

        @Override
        public int compareTo(@NotNull MinimumSpanningTreePrim.PrimNode<T, E> o) {
            return key.compareTo(o.getKey());
        }
    }

    private final E minValue;
    private final E maxValue;

    public MinimumSpanningTreePrim(E minValue, E maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public @NotNull Graph<T, E> spanningTree(@NotNull Graph<T, E> graph) {
        var nodes = graph.getNodes();
        var nodeMap = new HashMap<String, PrimNode<T, E>>(nodes.size());
        var nodeQueue = new PriorityQueue<PrimNode<T, E>>(nodes.size());
        var edges = new ArrayList<Edge<T, E>>();
        var spanningTree = new Graph<T, E>("prim-spanning-tree-" + graph.getName());
        var visited = new HashSet<String>(nodes.size());
        var first = new AtomicBoolean(true);

        for (var node : nodes) {
            node.getLeastWeightedEdgeValue().ifPresent(weight -> {
                var n = new PrimNode<>(node);
                if (first.get()) {
                    n.setKey(minValue);
                    first.set(false);
                } else {
                    n.setKey(maxValue);
                }
                nodeMap.put(node.getId(), n);
                nodeQueue.offer(n);
            });
        }

        while (!nodeQueue.isEmpty()) {
            var node = Objects.requireNonNull(nodeQueue.poll());

            if (visited.contains(node.getNode().getId())) {
                continue;
            }

            visited.add(node.getNode().getId());

            if (node.getPrevious() != null) {
                var edge = node.getPrevious().getEdge(node.getNode().getId());

                edge.ifPresent(edges::add);

                if (edges.size() == nodes.size() - 1) {
                    break; // We are done!
                }
            }

            for (var edge : node.getNode().getEdgeSet()) {
                var weight = edge.getWeight();
                if (weight.isEmpty()) {
                    continue;
                }

                if (!visited.contains(edge.getTarget().getId())) {
                    var node2 = nodeMap.get(edge.getTarget().getId());
                    var realWeight = weight.get();

                    if (realWeight.compareTo(node2.getKey()) < 0) {
                      node2.setPrevious(edge.getSource());
                      node2.setKey(realWeight);
                      nodeQueue.adjustTopUp(node2);
                  }
                }
            }
        }

        edges.forEach(spanningTree::addAsNew);

        return spanningTree;
    }
}
