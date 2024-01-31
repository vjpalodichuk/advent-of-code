package com.capital7software.aoc.lib.graph.path;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Graph;
import com.capital7software.aoc.lib.graph.Vertex;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * A Hamiltonian Path is a path in a Graph where every Vertex is visited exactly once!
 * A Hamiltonian Cycle is a Hamiltonian Path where the last Vertex in the path has an Edge that
 * connects back to the first Vertex in the path.
 *
 * <p><br>
 * This implementation is capable of finding both Hamiltonian Paths and Hamiltonian Cycles.
 *
 * <p><br>
 * This class has three Properties that can be set: Props.DETECT_CYCLES, Props.STARTING_VERTICES,
 * and Props.SUM_PATH.
 *
 * <p><br>
 * DETECT_CYCLES: Set to Boolean.TRUE if Hamiltonian Cycles instead of Paths should be built.
 *
 * <p><br>
 * STARTING_VERTICES: Accepts a List of Vertices to build paths for. Please note that the
 * specified vertices must exist in the Graph specified in the find call.
 *
 * <p><br>
 * SUM_PATH: Sums the edge weights and stores the cost for valid paths only. If DETECT_CYCLES
 * is set to TRUE, then the Edge weight from the last Vertex to the first Vertex is added
 * to the sum of the cost as well.
 *
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public class HamiltonianPathFinder<T extends Comparable<T>, E extends Comparable<E>>
    implements PathFinder<PathFinderResult<T, E>, T, E> {

  /**
   * Instantiates a new and empty path builder instance.
   */
  public HamiltonianPathFinder() {

  }

  /**
   * The properties that this pathfinder instance accepts.
   */
  public enum Props {
    /**
     * Set to true to only return paths that make a cycle.
     */
    DETECT_CYCLES,
    /**
     * Set the vertices to find paths for.
     */
    STARTING_VERTICES,
    /**
     * Set to true to have this pathfinder sum the weights of the paths it finds.
     */
    SUM_PATH
  }

  @Override
  public void find(
      @NotNull Graph<T, E> graph,
      @NotNull Properties properties,
      @NotNull Function<PathFinderResult<T, E>, PathFinderStatus> valid,
      Function<PathFinderResult<T, E>, PathFinderStatus> invalid
  ) {
    if (!validateProperties(properties)) {
      throw new IllegalArgumentException("The provided Properties are invalid: " + properties);
    }

    var vertexMap = graph.getVertexMap();

    // Paths will be investigated for these nodes.
    var startingVertices = getStartingVertices(vertexMap, properties);

    // Does a cycle need to be created?
    var cycleRequired = isCycleRequired(properties);
    var sumRequired = isSumRequired(properties);
    var pathId = new AtomicInteger(0);
    var size = vertexMap.values().size();

    for (var vertex : startingVertices) {
      var vertices = new ArrayList<>(vertexMap
                                         .values()
                                         .stream()
                                         .filter(it -> !it.getId().equals(vertex.getId()))
                                         .toList());
      var pathSoFar = new ArrayList<Vertex<T, E>>(size);
      var edgesSoFar = new ArrayList<Edge<E>>(size - 1);
      var idsSoFar = new HashSet<String>(size);

      pathSoFar.add(vertex); // Add the starting Vertex to the path.
      idsSoFar.add(vertex.getId());

      if (findPath(
          vertices,
          size,
          pathId,
          cycleRequired,
          sumRequired,
          pathSoFar,
          edgesSoFar,
          idsSoFar,
          valid,
          invalid
      ) == PathFinderStatus.FINISHED) {
        break;
      }
    }
  }

  /**
   * If the properties are invalid false should be returned to cause an IllegalArgumentException
   * to be thrown.
   *
   * <p><br>
   * Implementations that use Properties should override this method to ensure the Properties are
   * properly validated.
   *
   * @param properties The properties to validate.
   * @return True if the properties are valid, false if there is something wrong with them.
   */
  protected boolean validateProperties(@NotNull Properties properties) {
    if (properties.contains(Props.DETECT_CYCLES)) {
      if (!(properties.get(Props.DETECT_CYCLES) instanceof Boolean)) {
        return false;
      }
    }
    if (properties.contains(Props.STARTING_VERTICES)) {
      if ((properties.get(Props.STARTING_VERTICES)) instanceof List<?> list) {
        if (list.isEmpty() || !(list.getFirst() instanceof Vertex<?, ?>)) {
          return false;
        }
      } else {
        return false;
      }
    }
    if (properties.contains(Props.SUM_PATH)) {
      return properties.get(Props.SUM_PATH) instanceof Boolean;
    }

    return true;
  }

  /**
   * If this method returns True then a path is only valid if there is an Edge that leads from
   * the end Vertex in the path back to the start Vertex.
   *
   * <p><br>
   * Implementations that require a cycle for a path to be valid should override this method and
   * have it return true.
   *
   * <p><br>
   * Properties are provided should the implementation elect to allow the caller to specify if
   * a cycle should be required.
   *
   * <p><br>
   * This implementation will check for the Props.DETECT_CYCLES property and use it if present;
   * otherwise this method returns false.
   *
   * @param properties The properties to use for this invocation.
   * @return True if cycles should be detected; otherwise false.
   */
  protected boolean isCycleRequired(Properties properties) {
    if (properties.get(Props.DETECT_CYCLES) instanceof Boolean cycle) {
      return cycle;
    }
    return false;
  }

  /**
   * Returns the List of Vertices to build paths for. This default implementation simply returns
   * a List of all the Vertices in the Graph.
   *
   * <p><br>
   * Implementations should override this method if they support having
   * the caller specify the Vertices to build paths for via a Properties instance.
   *
   * @param vertexMap  The Map that contains all the vertices in the Graph.
   * @param properties The properties to use for this invocation.
   * @return The List of starting Vertices.
   */
  @SuppressWarnings("unchecked")
  @NotNull
  protected List<Vertex<T, E>> getStartingVertices(
      @NotNull Map<String, Vertex<T, E>> vertexMap,
      @NotNull Properties properties
  ) {
    if (properties.get(Props.STARTING_VERTICES) instanceof List<?> list) {
      List<Vertex<T, E>> results = new ArrayList<>(list.size());

      for (var vertex : list) {
        results.add((Vertex<T, E>) vertex);
      }
      return results;
    }
    return new ArrayList<>(vertexMap.values());
  }

  /**
   * If this method returns True then for valid paths, the weight of the Edges will be added to the
   * result the callback receives. If a cycle is also required, then the weight of the Edge from
   * the last vertex to the first vertex will be included in the sum.
   *
   * <p><br>
   * Implementations that require a sum for a valid path should override this method and have it
   * return true.
   *
   * <p><br>
   * Properties are provided should the implementation elect to allow the caller to specify if
   * a cycle should be required.
   *
   * <p><br>
   * This implementation will check for the Props.SUM_PATH property and use it if present;
   * otherwise this method returns false.
   *
   * @param properties The properties to use for this invocation.
   * @return True if a sum should be done for a valid path; otherwise false.
   */
  protected boolean isSumRequired(Properties properties) {
    if (properties.get(Props.SUM_PATH) instanceof Boolean sumPath) {
      return sumPath;
    }
    return false;
  }

  @NotNull
  private PathFinderStatus findPath(
      List<Vertex<T, E>> vertices,
      int requiredCount,
      AtomicInteger pathId,
      boolean cycleRequired,
      boolean sumRequired,
      List<Vertex<T, E>> path,
      List<Edge<E>> edges,
      Set<String> visitedIds,
      @NotNull Function<PathFinderResult<T, E>, PathFinderStatus> validCallback,
      Function<PathFinderResult<T, E>, PathFinderStatus> invalidCallback) {
    // Check the stopping conditions.
    if (path.size() == requiredCount) {
      if (cycleRequired) {
        // Validate we can get from the last vertex back to the starting vertex!
        var lastVertex = path.getLast();
        var firstVertex = path.getFirst();
        var edge = lastVertex.getEdge(firstVertex.getId());
        if (edge.isPresent()) {
          return validCallback.apply(
              buildPathResult(path, edges, pathId.getAndIncrement(), true, sumRequired)
          );
        } else if (invalidCallback != null) {
          return invalidCallback.apply(buildPathResult(path, edges, pathId.getAndIncrement()));
        } else {
          return PathFinderStatus.CONTINUE;
        }
      } else {
        return validCallback.apply(
            buildPathResult(path, edges, pathId.getAndIncrement(), false, sumRequired)
        );
      }
    }

    PathFinderStatus lastStatus = null;

    // Go through all the vertices
    for (var vertex : vertices) {
      if (!visitedIds.contains(vertex.getId())
          && path.getLast().getEdge(vertex.getId()).isPresent()) {
        // Add the vertex and edge to the path and mark the vertex as visited.
        edges.add(path.getLast().getEdge(vertex.getId()).get());
        visitedIds.add(vertex.getId());
        path.add(vertex);

        var status = findPath(
            vertices,
            requiredCount,
            pathId,
            cycleRequired,
            sumRequired,
            path,
            edges,
            visitedIds,
            validCallback,
            invalidCallback
        );
        lastStatus = status;

        // Clean-up
        path.removeLast();
        visitedIds.remove(vertex.getId());
        edges.removeLast();

        if (status != PathFinderStatus.CONTINUE) {
          return status;
        }
      }
    }

    if (lastStatus == null) {
      // Failed to find any vertices to add to the path!
      if (invalidCallback != null) {
        invalidCallback.apply(buildPathResult(path, edges, pathId.get()));
      }
      return PathFinderStatus.CONTINUE;
    }
    return lastStatus;
  }

  @NotNull
  private PathFinderResult<T, E> buildPathResult(
      @NotNull List<Vertex<T, E>> path,
      @NotNull List<Edge<E>> edges,
      int pathId
  ) {
    return buildPathResult(path, edges, pathId, false, false);
  }

  @NotNull
  private PathFinderResult<T, E> buildPathResult(
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
      return new PathFinderResult<>(
          pathId, start, end, new ArrayList<>(path), new ArrayList<>(edges)
      );
    } else {
      return new PathFinderResult<>(
          pathId, start, end, new ArrayList<>(path), new ArrayList<>(edges), sum
      );
    }
  }

  @SuppressWarnings("unchecked")
  private E calculateSumOfEdges(
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
}
