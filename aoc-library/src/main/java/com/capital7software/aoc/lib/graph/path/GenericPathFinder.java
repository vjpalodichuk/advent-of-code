package com.capital7software.aoc.lib.graph.path;

import static com.capital7software.aoc.lib.graph.path.HamiltonianPathFinder.buildPathResult;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Graph;
import com.capital7software.aoc.lib.graph.Vertex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class GenericPathFinder<T extends Comparable<T>, E extends Comparable<E>>
    implements PathFinder<PathFinderResult<T, E>, T, E> {
  /**
   * Instantiates a new and empty path builder instance.
   */
  public GenericPathFinder() {

  }

  /**
   * The properties that this pathfinder instance accepts.
   */
  public enum Props {
    /**
     * Set to the Vertex ID where all paths will start from.
     */
    STARTING_VERTEX_ID,
    /**
     * Set to the Vertex ID where all valid paths will end at.
     */
    ENDING_VERTEX_ID,
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

    final var vertexMap = graph.getVertexMap();

    // Paths will be investigated for these nodes.
    final var startingVertex = getStartingVertex(vertexMap, properties);
    final var endingVertex = getEndingVertex(vertexMap, properties);

    // Does a cycle need to be created?
    var sumRequired = isSumRequired(properties);
    var pathId = new AtomicInteger(0);
    var size = vertexMap.values().size();

    var vertices = new ArrayList<>(vertexMap
                                       .values()
                                       .stream()
                                       .filter(it -> !it.getId().equals(startingVertex.getId()))
                                       .toList());
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
    if (properties.contains(Props.STARTING_VERTEX_ID)) {
      if (!(properties.get(Props.STARTING_VERTEX_ID) instanceof String)) {
        return false;
      }
    }
    if (properties.contains(Props.ENDING_VERTEX_ID)) {
      if (!(properties.get(Props.ENDING_VERTEX_ID) instanceof String)) {
        return false;
      }
    }
    if (properties.contains(Props.SUM_PATH)) {
      return properties.get(Props.SUM_PATH) instanceof Boolean;
    }

    return true;
  }

  /**
   * Returns the starting Vertex to build paths for.
   *
   * @param vertexMap  The Map that contains all the vertices in the Graph.
   * @param properties The properties to use for this invocation.
   * @return The List of starting Vertices.
   */
  @NotNull
  protected Vertex<T, E> getStartingVertex(
      @NotNull Map<String, Vertex<T, E>> vertexMap,
      @NotNull Properties properties
  ) {
    if (properties.get(Props.STARTING_VERTEX_ID) instanceof String vertex) {
      return vertexMap.get(vertex);
    }

    throw new IllegalStateException("No starting Vertex ID found in the properties!");
  }

  /**
   * Returns the ending Vertex to build paths to.
   *
   * @param vertexMap  The Map that contains all the vertices in the Graph.
   * @param properties The properties to use for this invocation.
   * @return The List of starting Vertices.
   */
  @NotNull
  protected Vertex<T, E> getEndingVertex(
      @NotNull Map<String, Vertex<T, E>> vertexMap,
      @NotNull Properties properties
  ) {
    if (properties.get(Props.ENDING_VERTEX_ID) instanceof String vertex) {
      return vertexMap.get(vertex);
    }

    throw new IllegalStateException("No ending Vertex ID found in the properties!");
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
      AtomicInteger pathId,
      boolean sumRequired,
      Vertex<T, E> endingVertex,
      List<Vertex<T, E>> path,
      List<Edge<E>> edges,
      Set<String> visitedIds,
      @NotNull Function<PathFinderResult<T, E>, PathFinderStatus> validCallback,
      Function<PathFinderResult<T, E>, PathFinderStatus> invalidCallback) {
    // Check the stopping conditions.
    if (path.getLast().equals(endingVertex)) {
      return validCallback.apply(
          buildPathResult(path, edges, pathId.getAndIncrement(), false, sumRequired)
      );
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

}
