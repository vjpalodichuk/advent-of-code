package com.capital7software.aoc.lib.graph.parser;

import com.capital7software.aoc.lib.graph.Graph;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * A functional interface to parse and load a Graph of the specified types.
 *
 * <p><br>
 * See the parser specified documentation for details on how to properly use it.
 *
 * @param <T> The type of the value of a Vertex in the Graph this parser creates.
 * @param <E> The type of the weight of the Edges in the Graph this parser creates.
 */

public interface GraphParser<T extends Comparable<T>, E extends Comparable<E>> {
  /**
   * Parses the input and returns an instance of a Graph that contains the
   * Nodes and Edges defined in the input list.
   *
   * @param input The specification to be parsed to create a new Graph.
   * @param name  The name for the new Graph.
   * @return A new Graph with the Nodes and Edges defined in the specification.
   */
  @NotNull
  Optional<Graph<T, E>> parse(@NotNull List<String> input, @NotNull String name);
}
