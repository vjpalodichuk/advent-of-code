package com.capital7software.aoc2015.lib.graph.path;

import com.capital7software.aoc2015.lib.graph.Graph;
import org.jetbrains.annotations.NotNull;

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
    @Override
    public @NotNull Graph<T, E> spanningTree(@NotNull Graph<T, E> graph) {
        return new Graph<>("TODO");
    }
}
