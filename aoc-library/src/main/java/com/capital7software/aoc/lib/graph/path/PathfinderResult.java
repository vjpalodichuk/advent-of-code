package com.capital7software.aoc.lib.graph.path;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Vertex;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;

/**
 * Passed to the valid and invalid handlers of the Pathfinder.
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
public record PathfinderResult<T extends Comparable<T>, E extends Comparable<E>>(
    long id,
    @NotNull Vertex<T, E> start,
    @NotNull Vertex<T, E> end,
    @NotNull Collection<Vertex<T, E>> vertices,
    @NotNull Collection<Edge<E>> edges,
    E cost
) {
  /**
   * Calculates and returns the sum of the specified edges in the specified path.
   *
   * @param path          The path to include.
   * @param edges         The edges in the path to include.
   * @param cycleRequired Set to true to require an edge from the last Vertex in the path
   *                      to the first Vertex in the path.
   * @param <T>           The type of the value held by a Vertex in this Path.
   * @param <E>           The type of the weight of the Edges in this Path.
   * @return The sum of the specified edges in the specified path.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<T>, E extends Comparable<E>> E calculateSumOfEdges(
      List<Vertex<T, E>> path,
      List<Edge<E>> edges,
      boolean cycleRequired
  ) {
    var size = path.size();
    var weights = new ArrayList<E>(size);

    weights.addAll(
        edges.stream().map(Edge::getWeight).filter(Optional::isPresent).map(Optional::get).toList()
    );

    if (cycleRequired) {
      var lastVertex = path.getLast();
      var firstVertex = path.getFirst();
      var edge = lastVertex.getEdge(firstVertex.getId());

      if (edge.isEmpty()) {
        throw new RuntimeException(
            "Cycle required passed but no edge from last vertex back to first vertex in path!"
        );
      } else {
        edge.get().getWeight().ifPresent(weights::add);
      }
    }

    E realTotal = null;

    if (!weights.isEmpty()) {
      for (var realWeight : weights) {
        if (realWeight instanceof Integer weight) {
          if (realTotal == null) {
            realTotal = (E) weight;
          } else {
            if (realTotal instanceof Integer total) {
              realTotal = (E) ((Integer) (total + weight));
            }
          }
        } else if (realWeight instanceof Long weight) {
          if (realTotal == null) {
            realTotal = (E) weight;
          } else {
            if (realTotal instanceof Long total) {
              realTotal = (E) ((Long) (total + weight));
            }
          }
        } else if (realWeight instanceof Double weight) {
          if (realTotal == null) {
            realTotal = (E) weight;
          } else {
            if (realTotal instanceof Double total) {
              realTotal = (E) ((Double) (total + weight));
            }
          }
        } else if (realWeight instanceof Float weight) {
          if (realTotal == null) {
            realTotal = (E) weight;
          } else {
            if (realTotal instanceof Float total) {
              realTotal = (E) ((Float) (total + weight));
            }
          }
        } else if (realWeight instanceof AtomicInteger weight) {
          if (realTotal == null) {
            realTotal = (E) weight;
          } else {
            if (realTotal instanceof AtomicInteger total) {
              total.set(total.get() + weight.get());
            }
          }
        } else if (realWeight instanceof AtomicLong weight) {
          if (realTotal == null) {
            realTotal = (E) weight;
          } else {
            if (realTotal instanceof AtomicLong total) {
              total.set(total.get() + weight.get());
            }
          }
        } else if (realWeight instanceof BigInteger weight) {
          if (realTotal == null) {
            realTotal = (E) weight;
          } else {
            if (realTotal instanceof BigInteger total) {
              realTotal = (E) total.add(weight);
            }
          }
        } else if (realWeight instanceof BigDecimal weight) {
          if (realTotal == null) {
            realTotal = (E) weight;
          } else {
            if (realTotal instanceof BigDecimal total) {
              realTotal = (E) total.add(weight);
            }
          }
        }
      }
    }

    return realTotal;
  }

  /**
   * Creates and returns a PathFinderResult based on the specified values.
   *
   * @param path   The path to include.
   * @param edges  The edges in the path to include.
   * @param pathId The unique ID of the path.
   * @param <T>    The type of the value held by a Vertex in this Path.
   * @param <E>    The type of the weight of the Edges in this Path.
   * @return A PathFinderResult based on the specified values.
   */
  @NotNull
  static <T extends Comparable<T>, E extends Comparable<E>> PathfinderResult<T, E> buildPathResult(
      @NotNull List<Vertex<T, E>> path,
      @NotNull List<Edge<E>> edges,
      int pathId
  ) {
    return buildPathResult(path, edges, pathId, false, false);
  }

  /**
   * Creates and returns a PathFinderResult based on the specified values.
   *
   * @param path          The path to include.
   * @param edges         The edges in the path to include.
   * @param pathId        The unique ID of the path.
   * @param cycleRequired Set to true to require an edge from the last Vertex in the path
   *                      to the first Vertex in the path.
   * @param sumRequired   Set to true to have the edge weights summed up and set as the path
   *                      cost.
   * @param <T>           The type of the value held by a Vertex in this Path.
   * @param <E>           The type of the weight of the Edges in this Path.
   * @return A PathFinderResult based on the specified values.
   */
  @NotNull
  static <T extends Comparable<T>, E extends Comparable<E>> PathfinderResult<T, E> buildPathResult(
      @NotNull List<Vertex<T, E>> path,
      @NotNull List<Edge<E>> edges,
      int pathId,
      boolean cycleRequired,
      boolean sumRequired
  ) {
    var start = path.getFirst();
    var end = path.size() > 1 ? path.getLast() : path.getFirst();
    E sum = null;

    if (sumRequired) {
      sum = calculateSumOfEdges(path, edges, cycleRequired);
    }

    if (sum == null) {
      return new PathfinderResult<>(
          pathId, start, end, new ArrayList<>(path), new ArrayList<>(edges)
      );
    } else {
      return new PathfinderResult<>(
          pathId, start, end, new ArrayList<>(path), new ArrayList<>(edges), sum
      );
    }
  }

  /**
   * Passed to the valid and invalid handlers of the Pathfinder. Provides the details of
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
  public PathfinderResult(
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
   * Passed to the valid and invalid handlers of the Pathfinder. Provides the details of the
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
  public PathfinderResult(
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
