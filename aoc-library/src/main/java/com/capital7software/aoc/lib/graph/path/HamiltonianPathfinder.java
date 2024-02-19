package com.capital7software.aoc.lib.graph.path;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Graph;
import com.capital7software.aoc.lib.graph.Vertex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
 * This class uses three Properties that can be set: PathfinderProperties.DETECT_CYCLES,
 * PathfinderProperties.STARTING_VERTICES, and PathfinderProperties.SUM_PATH.
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
public class HamiltonianPathfinder<T extends Comparable<T>, E extends Comparable<E>>
    implements Pathfinder<PathfinderResult<T, E>, T, E> {

  /**
   * Instantiates a new and empty path builder instance.
   */
  public HamiltonianPathfinder() {

  }

  @Override
  public void find(
      @NotNull Graph<T, E> graph,
      @NotNull Properties properties,
      @NotNull Function<PathfinderResult<T, E>, PathfinderStatus> valid,
      Function<PathfinderResult<T, E>, PathfinderStatus> invalid
  ) {
    if (PathfinderProperties.notValid(properties)) {
      throw new IllegalArgumentException("The provided Properties are invalid: " + properties);
    }

    // Paths will be investigated for these nodes.
    var startingVertices = PathfinderProperties.startVertices(graph, properties);

    // Does a cycle need to be created?
    var cycleRequired = PathfinderProperties.isCycleRequired(properties);
    var sumRequired = PathfinderProperties.isSumRequired(properties);
    var pathId = new AtomicInteger(0);
    var size = graph.size();

    for (var vertex : startingVertices) {
      var vertices = graph.getVertices(it -> !it.getId().equals(vertex.getId()));
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
      ) == PathfinderStatus.FINISHED) {
        break;
      }
    }
  }

  @NotNull
  private PathfinderStatus findPath(
      List<Vertex<T, E>> vertices,
      int requiredCount,
      AtomicInteger pathId,
      boolean cycleRequired,
      boolean sumRequired,
      List<Vertex<T, E>> path,
      List<Edge<E>> edges,
      Set<String> visitedIds,
      @NotNull Function<PathfinderResult<T, E>, PathfinderStatus> validCallback,
      Function<PathfinderResult<T, E>, PathfinderStatus> invalidCallback
  ) {
    // Check the stopping conditions.
    if (path.size() == requiredCount) {
      if (cycleRequired) {
        // Validate we can get from the last vertex back to the starting vertex!
        var lastVertex = path.getLast();
        var firstVertex = path.getFirst();
        var edge = lastVertex.getEdge(firstVertex.getId());
        if (edge.isPresent()) {
          return validCallback.apply(
              PathfinderResult.buildPathResult(
                  path, edges, pathId.getAndIncrement(), true, sumRequired
              )
          );
        } else if (invalidCallback != null) {
          return invalidCallback.apply(
              PathfinderResult.buildPathResult(
                  path, edges, pathId.getAndIncrement()
              )
          );
        } else {
          return PathfinderStatus.CONTINUE;
        }
      } else {
        return validCallback.apply(
            PathfinderResult.buildPathResult(
                path, edges, pathId.getAndIncrement(), false, sumRequired
            )
        );
      }
    }

    PathfinderStatus lastStatus = null;

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

        if (status != PathfinderStatus.CONTINUE) {
          return status;
        }
      }
    }

    if (lastStatus == null) {
      // Failed to find any vertices to add to the path!
      if (invalidCallback != null) {
        invalidCallback.apply(
            PathfinderResult.buildPathResult(path, edges, pathId.get())
        );
      }
      return PathfinderStatus.CONTINUE;
    }
    return lastStatus;
  }

}
