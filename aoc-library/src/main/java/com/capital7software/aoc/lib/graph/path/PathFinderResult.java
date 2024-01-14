package com.capital7software.aoc.lib.graph.path;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public record PathFinderResult<T extends Comparable<T>, E extends Comparable<E>>(
        long id,
        @NotNull Vertex<T, E> start,
        @NotNull Vertex<T, E> end,
        @NotNull Collection<Vertex<T, E>> vertices,
        @NotNull Collection<Edge<T, E>> edges,
        E cost
) {
    public PathFinderResult(
            long id,
            @NotNull Vertex<T, E> start,
            @NotNull Vertex<T, E> end,
            @NotNull Collection<Vertex<T, E>> vertices,
            @NotNull Collection<Edge<T, E>> edges,
            E cost
    ) {
        this.id = id;
        this.start = start.copy();
        this.end = end.copy();
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
        this.cost = cost;
    }

    public PathFinderResult(
            long id,
            @NotNull Vertex<T, E> start,
            @NotNull Vertex<T, E> end,
            @NotNull Collection<Vertex<T, E>> vertices,
            @NotNull Collection<Edge<T, E>> edges
    ) {
        this(id, start, end, vertices, edges, null);
    }

    @Override
    public Collection<Edge<T, E>> edges() {
        return Collections.unmodifiableCollection(edges);
    }

    @Override
    public Collection<Vertex<T, E>> vertices() {
        return Collections.unmodifiableCollection(vertices);
    }

    @Override
    public Vertex<T, E> start() {
        return start.copy();
    }

    @Override
    public Vertex<T, E> end() {
        return end.copy();
    }
}
