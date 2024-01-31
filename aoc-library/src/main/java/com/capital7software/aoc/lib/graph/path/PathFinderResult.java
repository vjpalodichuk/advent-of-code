package com.capital7software.aoc.lib.graph.path;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * Passed to the valid and invalid handlers of the PathFinder.
 * Provides the details of the most recent path.
 *
 * @param id       The ID of the path.
 * @param start    The starting Vertex of the path.
 * @param end      The last Vertex of the path. If this is a cycle, then there is an
 *                 Edge in the end Vertex
 *                 that leads directly to the start Vertex.
 * @param vertices All the Vertices in this Path.
 * @param edges    All the Edges in this Path.
 * @param cost     The sum of the weights of the Edges in this Path.
 * @param <T>      The type of the value held by a Vertex in this Path.
 * @param <E>      The type of the weight of the Edges in this Path.
 */
public record PathFinderResult<T extends Comparable<T>, E extends Comparable<E>>(
    long id,
    @NotNull Vertex<T, E> start,
    @NotNull Vertex<T, E> end,
    @NotNull Collection<Vertex<T, E>> vertices,
    @NotNull Collection<Edge<E>> edges,
    E cost
) {
  /**
   * Passed to the valid and invalid handlers of the PathFinder. Provides the details of
   * the most recent path.
   *
   * @param id       The ID of the path.
   * @param start    The starting Vertex of the path.
   * @param end      The last Vertex of the path. If this is a cycle, then there is an
   *                 Edge in the end Vertex
   *                 that leads directly to the start Vertex.
   * @param vertices All the Vertices in this Path.
   * @param edges    All the Edges in this Path.
   * @param cost     The sum of the weights of the Edges in this Path.
   */
  public PathFinderResult(
      long id,
      @NotNull Vertex<T, E> start,
      @NotNull Vertex<T, E> end,
      @NotNull Collection<Vertex<T, E>> vertices,
      @NotNull Collection<Edge<E>> edges,
      E cost
  ) {
    this.id = id;
    this.start = start.copy();
    this.end = end.copy();
    this.vertices = new ArrayList<>(vertices);
    this.edges = new ArrayList<>(edges);
    this.cost = cost;
  }

  /**
   * Passed to the valid and invalid handlers of the PathFinder. Provides the details of the
   * most recent path.
   *
   * @param id       The ID of the path.
   * @param start    The starting Vertex of the path.
   * @param end      The last Vertex of the path. If this is a cycle, then there is an Edge
   *                 in the end Vertex
   *                 that leads directly to the start Vertex.
   * @param vertices All the Vertices in this Path.
   * @param edges    All the Edges in this Path.
   */
  public PathFinderResult(
      long id,
      @NotNull Vertex<T, E> start,
      @NotNull Vertex<T, E> end,
      @NotNull Collection<Vertex<T, E>> vertices,
      @NotNull Collection<Edge<E>> edges
  ) {
    this(id, start, end, vertices, edges, null);
  }

  /**
   * Returns an unmodifiable List of Edges in this Path.
   *
   * @return An unmodifiable List of Edges in this Path.
   */
  @Override
  public Collection<Edge<E>> edges() {
    return edges.stream().map(Edge::copy).toList();
  }

  /**
   * Returns an unmodifiable List of Vertices in this Path.
   *
   * @return An unmodifiable List of Vertices in this Path.
   */
  @Override
  public Collection<Vertex<T, E>> vertices() {
    return vertices.stream().map(Vertex::copy).toList();
  }

  /**
   * Returns a copy of the Start Vertex in this Path.
   *
   * @return A copy of the Start Vertex in this Path.
   */
  @Override
  public Vertex<T, E> start() {
    return start.copy();
  }

  /**
   * Returns a copy of the End Vertex in this Path.
   *
   * @return A copy of the End Vertex in this Path.
   */
  @Override
  public Vertex<T, E> end() {
    return end.copy();
  }
}
