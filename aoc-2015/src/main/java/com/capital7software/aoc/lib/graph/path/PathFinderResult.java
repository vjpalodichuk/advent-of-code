package com.capital7software.aoc.lib.graph.path;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Vertex;

import java.util.Collection;

public record PathFinderResult<T extends Comparable<T>, E extends Comparable<E>>(
        long id,
        Vertex<T, E> start,
        Vertex<T, E> end,
        Collection<Vertex<T, E>> vertices,
        Collection<Edge<T, E>> edges,
        E cost
) {
    public PathFinderResult(
            long id,
            Vertex<T, E> start,
            Vertex<T, E> end,
            Collection<Vertex<T, E>> vertices,
            Collection<Edge<T, E>> edges
    ) {
        this(id, start, end, vertices, edges, null);
    }
}
