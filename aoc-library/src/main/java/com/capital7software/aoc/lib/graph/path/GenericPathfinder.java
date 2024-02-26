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
 * A Pathfinder implementation that finds paths from a starting Vertex to an ending Vertex.
 *
 * <p><br>
 * This class has three Properties that can be set: Props.STARTING_VERTEX, Props.ENDING_VERTEX,
 * and Props.SUM_PATH.
 *
 * <p><br>
 * STARTING_VERTEX: Accepts a String ID of the starting Vertex. This is required!.
 *
 * <p><br>
 * ENDING_VERTEX: Accepts a String ID of the ending Vertex. This is required!.
 *
 * <p><br>
 * SUM_PATH: Sums the edge weights and stores the cost for valid paths only.
 *
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public class GenericPathfinder<T extends Comparable<T>, E extends Comparable<E>>
    implements Pathfinder<PathfinderResult<T, E>, T, E> {
  /**
   * Instantiates a new and empty path builder instance.
   */
  public GenericPathfinder() {
  }

  @Override
  public void find(
      @NotNull Graph<T, E> graph,
      @NotNull Properties properties,
      @NotNull Function<PathfinderResult<T, E>, PathfinderStatus> valid,
      Function<PathfinderResult<T, E>, PathfinderStatus> invalid
  ) {
    findInternal(graph, properties, valid, invalid);
  }

  private void findInternal(
      @NotNull Graph<T, E> graph,
      @NotNull Properties properties,
      @NotNull Function<PathfinderResult<T, E>, PathfinderStatus> valid,
      Function<PathfinderResult<T, E>, PathfinderStatus> invalid
  ) {
    if (PathfinderProperties.notValid(properties)) {
      throw new IllegalArgumentException("The provided Properties are invalid: " + properties);
    }

    // Paths will be investigated for these nodes.
    final var startingVertex = PathfinderProperties.getStartingVertex(graph, properties);
    final var endingVertex = PathfinderProperties.getEndingVertex(graph, properties);

    // Does a sum of the Edge weights need to be calculated?
    var sumRequired = PathfinderProperties.isSumRequired(properties);
    var pathId = new AtomicInteger(0);
    var size = graph.size();

    var vertices = graph.getVertices(it -> !it.getId().equals(startingVertex.getId()));
    var pathSoFar = new ArrayList<Vertex<T, E>>(size);
    var edgesSoFar = new ArrayList<Edge<E>>(size - 1);
    var idsSoFar = new HashSet<String>(size);

    pathSoFar.add(startingVertex); // Add the starting Vertex to the path.
    idsSoFar.add(startingVertex.getId());

    findPath(
        vertices,
        pathId,
        sumRequired,
        endingVertex,
        pathSoFar,
        edgesSoFar,
        idsSoFar,
        valid,
        invalid
    );
  }

  @NotNull
  private PathfinderStatus findPath(
      List<Vertex<T, E>> vertices,
      AtomicInteger pathId,
      boolean sumRequired,
      Vertex<T, E> endingVertex,
      List<Vertex<T, E>> path,
      List<Edge<E>> edges,
      Set<String> visitedIds,
      @NotNull Function<PathfinderResult<T, E>, PathfinderStatus> validCallback,
      Function<PathfinderResult<T, E>, PathfinderStatus> invalidCallback
  ) {
    // Check the stopping conditions.
    if (path.getLast().equals(endingVertex)) {
      return validCallback.apply(
          PathfinderResult.buildPathResult(
              path, edges, pathId.getAndIncrement(), false, sumRequired
          )
      );
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
            pathId,
            sumRequired,
            endingVertex,
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
        invalidCallback.apply(PathfinderResult.buildPathResult(path, edges, pathId.get()));
      }
      return PathfinderStatus.CONTINUE;
    }
    return lastStatus;
  }

}
