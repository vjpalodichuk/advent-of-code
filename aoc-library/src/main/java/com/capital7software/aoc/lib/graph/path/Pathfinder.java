package com.capital7software.aoc.lib.graph.path;

import com.capital7software.aoc.lib.graph.Graph;
import java.util.Properties;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * A functional interface for finding the shortest path in a Graph.
 *
 * <p><br>
 * The valid function is called when a valid path is found so that the
 * result can be processed. Depending on the implementation, it may be called
 * at most once for each path found or only once when the find operation completes.
 * To stop the algorithm from continuing to run the find operation, FINISHED may be returned.
 *
 * <p><br>
 * The invalid function is optional and is called when a path is exhausted and there is no
 * valid path. Depending on the implementation, it may be called at most once for each path
 * attempt that fails or only once when the find operation completes. To stop the algorithm from
 * continuing to run the find operation, FINISHED may be returned.
 *
 * <p><br>
 * NEXT_START may be returned from either the valid or invalid handler to move to the next
 * starting vertex if there is one.
 *
 * @param <P> The type of the path result.
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public interface Pathfinder<P, T extends Comparable<T>, E extends Comparable<E>> {
  /**
   * Searches the specified Graph for the desired paths. The valid consumer may be called
   * each time a valid path is found or at the completion of the find operation. The invalid
   * consumer may be called each time an attempt at finding a path fails or at the completion
   * of the find operation.
   *
   * <p><br>
   * The type of the value passed to each consumer along with the exact behavior of this method,
   * such as when the consumers are called, is implementation dependent.
   *
   * <p><br>
   * The Properties control the operation of the Pathfinder. The Properties are Pathfinder
   * implementation specific. Not all Pathfinder algorithms require a Properties instance.
   * Please consult the documentation for the Pathfinder you wish to use on how to properly pass
   * the properties it needs to perform its function.
   *
   * <p><br>
   * NEXT_START may be returned from either the valid or invalid handler to move to the next
   * starting vertex if there is one. FINISHED should be returned if no more paths are needed;
   * otherwise CONTINUE should be returned.
   *
   * @param graph      The Graph to be searched
   * @param properties Implementation dependent properties to be passed as input.
   * @param valid      The function to be called for valid paths.
   * @param invalid    The function to be called for invalid paths.
   */
  void find(
      @NotNull Graph<T, E> graph,
      @NotNull Properties properties,
      @NotNull Function<P, PathfinderStatus> valid,
      Function<P, PathfinderStatus> invalid
  );
}
