package com.capital7software.aoc.lib.graph.path;


import com.capital7software.aoc.lib.graph.Graph;
import com.capital7software.aoc.lib.graph.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

/**
 * The properties that are used by FathFinder and DynamicPathfinder instances.
 */
public enum PathfinderProperties {
  /**
   * Set to true to only return paths that make a cycle.
   */
  DETECT_CYCLES,
  /**
   * Set the vertices to find paths for.
   */
  STARTING_VERTICES,
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
  SUM_PATH,
  /**
   * The Heuristic is an object that implements the [Heuristic] interface.
   */
  HEURISTIC,
  /**
   * An Integer that limits the total cost of a valid path. Even if the destination vertex wasn't
   * reached, as long as a path ends on or before the specified maximum it is counted as valid.
   *
   * <p><br>
   * This only applies if this property has been set to a value greater than 0. Alternatively,
   * an implementation may depend on this value being within a certain range. Be sure to read the
   * documentation of the finder implementation for details on how to use this property.
   */
  MAX_COST;

  /**
   * If the properties are invalid true is returned; otherwise false is returned.
   *
   * @param properties The properties to validate.
   * @return True if the properties are invalid; otherwise false.
   */
  public static boolean notValid(@NotNull Properties properties) {
    if (properties.contains(PathfinderProperties.STARTING_VERTEX_ID)) {
      if (!(properties.get(PathfinderProperties.STARTING_VERTEX_ID) instanceof String)) {
        return true;
      }
    }
    if (properties.contains(PathfinderProperties.ENDING_VERTEX_ID)) {
      if (!(properties.get(PathfinderProperties.ENDING_VERTEX_ID) instanceof String)) {
        return true;
      }
    }
    if (properties.contains(PathfinderProperties.DETECT_CYCLES)) {
      if (!(properties.get(PathfinderProperties.DETECT_CYCLES) instanceof Boolean)) {
        return true;
      }
    }
    if (properties.contains(PathfinderProperties.STARTING_VERTICES)) {
      if ((properties.get(PathfinderProperties.STARTING_VERTICES)) instanceof List<?> list) {
        if (list.isEmpty() || !(list.getFirst() instanceof Vertex<?, ?>)) {
          return true;
        }
      } else {
        return true;
      }
    }
    if (properties.contains(PathfinderProperties.HEURISTIC)) {
      if (!(properties.get(PathfinderProperties.HEURISTIC) instanceof Heuristic<?, ?>)) {
        return true;
      }
    }
    if (properties.contains(PathfinderProperties.SUM_PATH)) {
      return !(properties.get(PathfinderProperties.SUM_PATH) instanceof Boolean);
    }

    return false;
  }

  /**
   * Returns the starting Vertex to build paths for.
   *
   * @param graph      The Graph that contains all the vertices.
   * @param properties The properties to use for this invocation.
   * @param <T>        The type of the value of a Vertex.
   * @param <E>        The type of the weight of an Edge.
   * @return The List of starting Vertices.
   */
  @NotNull
  public static <T extends Comparable<T>, E extends Comparable<E>> Vertex<T, E> getStartingVertex(
      @NotNull Graph<T, E> graph,
      @NotNull Properties properties
  ) {
    if (properties.get(STARTING_VERTEX_ID) instanceof String vertex) {
      return graph.get(vertex);
    }

    throw new IllegalStateException("No starting Vertex ID found in the properties!");
  }

  /**
   * Returns the ending Vertex to build paths to.
   *
   * @param vertexMap  The Map that contains all the vertices in the Graph.
   * @param properties The properties to use for this invocation.
   * @param <T>        The type of the value of a Vertex.
   * @param <E>        The type of the weight of an Edge.
   * @return The List of starting Vertices.
   */
  @NotNull
  public static <T extends Comparable<T>, E extends Comparable<E>> Vertex<T, E> getEndingVertex(
      @NotNull Graph<T, E> vertexMap,
      @NotNull Properties properties
  ) {
    if (properties.get(ENDING_VERTEX_ID) instanceof String vertex) {
      return vertexMap.get(vertex);
    }

    throw new IllegalStateException("No ending Vertex ID found in the properties!");
  }

  /**
   * Returns the ending Vertex to build paths to.
   *
   * @param properties The properties to use for this invocation.
   * @param <T>        The type of the value of a Vertex.
   * @param <E>        The type of the weight of an Edge.
   * @return The List of starting Vertices.
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public static <T extends Comparable<T>, E extends Comparable<E>> Heuristic<T, E> getHeuristic(
      @NotNull Properties properties
  ) {
    if (properties.get(HEURISTIC) instanceof Heuristic<?, ?> heuristic) {
      return (Heuristic<T, E>) heuristic;
    }

    throw new IllegalStateException("No ending Vertex ID found in the properties!");
  }

  /**
   * Returns the List of Vertices to build paths for. This default implementation simply returns
   * a List of all the Vertices in the list stored in the properties.
   *
   * <p><br>
   * Implementations should override this method if they support having
   * the caller specify the Vertices to build paths for via a Properties instance.
   *
   * @param graph      The Map that contains all the vertices in the Graph.
   * @param <T>        The type of the value of a Vertex.
   * @param <E>        The type of the weight of an Edge.
   * @param properties The properties to use for this invocation.
   * @return The List of starting Vertices.
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public static <T extends Comparable<T>, E extends Comparable<E>> List<Vertex<T, E>> startVertices(
      @NotNull Graph<T, E> graph,
      @NotNull Properties properties
  ) {
    if (properties.get(STARTING_VERTICES) instanceof List<?> list) {
      List<Vertex<T, E>> results = new ArrayList<>(list.size());

      for (var vertex : list) {
        results.add((Vertex<T, E>) vertex);
      }
      return results;
    }
    return Collections.emptyList();
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
  public static boolean isSumRequired(Properties properties) {
    if (properties.get(SUM_PATH) instanceof Boolean sumPath) {
      return sumPath;
    }
    return false;
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
  public static boolean isCycleRequired(@NotNull Properties properties) {
    if (properties.get(DETECT_CYCLES) instanceof Boolean cycle) {
      return cycle;
    }
    return false;
  }

  /**
   * Returns the MAX_COST for the specified properties. If the property isn't set, 0 is returned;
   * otherwise the set value is returned, which may be negative. Values less than or equal to
   * 0 disable this feature in PathFinders that use it.
   *
   * @param properties The properties to use for this invocation.
   * @return The MAX_COST for the specified properties.
   */
  public static double getMaxCost(@NotNull Properties properties) {
    if (properties.get(MAX_COST) instanceof Number maxCost) {
      return maxCost.doubleValue();
    } else {
      return 0;
    }
  }
}
