package com.capital7software.aoc2015.lib.graph.path;

import com.capital7software.aoc2015.lib.graph.Edge;
import com.capital7software.aoc2015.lib.graph.Vertex;

import java.util.Collection;

public record PathFinderResult<T extends Comparable<T>, E extends Comparable<E>>(
        Vertex<T, E> start,
        Vertex<T, E> end,
        Collection<Edge<T, E>> edges
) {
}
