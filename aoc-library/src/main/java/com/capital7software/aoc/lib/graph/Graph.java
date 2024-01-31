package com.capital7software.aoc.lib.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * A Graph that uses adjacency lists to represent the Vertices and Edges in this Graph.
 *
 * <p><br>
 * By convention, this Graph is considered to be directional. To make it non-directional, for every
 * Edge that is added, add another edge by reversing the source and target Vertices. This will
 * ensure that for every Edge from Vertex A to Vertex B there is also an Edge that goes from
 * Vertex B to Vertex A.
 *
 * <p><br>
 * When building a Graph, all the Vertices should be added to this Graph and then all the Edges
 * should be added to connect the Vertices.
 *
 * <p><br>
 * Attempting to add an Edge to a Vertex that doesn't yet exist in the Graph will cause the add
 * to fail.
 *
 * <p><br>
 * Vertices can easily be added with just their ID and nothing else.
 *
 * <p><br>
 * Operations for searching the Graph are handled by other Algorithm classes.
 *
 * <p><br>
 * This Graph simply contains methods that allow other Graph Algorithms to operate on this Graph.
 *
 * @param <T> The type of the value held by Vertices in this Graph.
 * @param <E> The type of the weight for Edges connected to and from Vertices in this Graph.
 */
public class Graph<T extends Comparable<T>, E extends Comparable<E>> {
  private static final String DEFAULT_NAME = "graph";

  private final String name;
  private final Map<String, Vertex<T, E>> vertices;

  /**
   * Instantiates a new Graph with the specified name and map of Vertices.
   *
   * @param name     The required name of this Graph.
   * @param vertices The required map to hold the Vertices of this Graph.
   */
  public Graph(@NotNull String name, @NotNull Map<String, Vertex<T, E>> vertices) {
    this.name = Objects.requireNonNull(name);
    this.vertices = new HashMap<>(Objects.requireNonNull(vertices));
  }

  /**
   * Instantiates a new Graph with a default name of graph and no Vertices.
   */
  public Graph() {
    this(DEFAULT_NAME, new HashMap<>());
  }

  /**
   * Instantiates a new Graph with the specified name and an empty map of Vertices.
   *
   * @param name The name to use for this Graph.
   */
  public Graph(@NotNull String name) {
    this(name, new HashMap<>());
  }

  /**
   * Returns the name of this Graph.
   *
   * @return The name of this Graph.
   */
  @NotNull
  public String getName() {
    return name;
  }

  /**
   * Returns a modifiable list of Vertices in this Graph.
   *
   * @return A modifiable list of Vertices in this Graph.
   */
  @NotNull
  public List<Vertex<T, E>> getVertices() {
    return new ArrayList<>(vertices.values());
  }

  /**
   * Returns an unmodifiable map of Vertices in this Graph.
   *
   * @return An unmodifiable map of Vertices in this Graph.
   */
  @NotNull
  public Map<String, Vertex<T, E>> getVertexMap() {
    return Collections.unmodifiableMap(vertices);
  }

  /**
   * Two Graphs are considered equal if they have the same name.
   *
   * @param o The other Graph to test for equality against.
   * @return True if the other Graph has the same name as this Graph.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Graph<?, ?> graph)) {
      return false;
    }
    return getName().equals(graph.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "Graph{" + "name='" + name + '\'' + ", vertexs=" + vertices + '}';
  }

  /**
   * Adds a new Vertex to this Graph with the specified vertexId.
   *
   * @param vertexId The vertexId of the new Vertex that is being added.
   * @return True if the Vertex was successfully added; false if a
   *     Vertex with that ID already exists in this Graph.
   */
  public boolean add(@NotNull String vertexId) {
    if (vertices.containsKey(Objects.requireNonNull(vertexId))) {
      return false;
    }

    return add(new Vertex<>(vertexId));
  }

  /**
   * Adds an existing Vertex to this Graph.
   *
   * @param vertex The existing Vertex to add to this Graph.
   * @return True if the Vertex was successfully added; false if a
   *     Vertex with the same ID already exists in this Graph.
   */
  public boolean add(@NotNull Vertex<T, E> vertex) {
    Objects.requireNonNull(vertex);

    return vertices.putIfAbsent(vertex.getId(), vertex) == null;
  }

  /**
   * Adds a new Edge to this Graph from source to target with the specified label and weight.
   * Both source and target must already exist as Vertices in this Graph or else the add will fail.
   *
   * <p><br>
   * Also note that if you want to make this Edge non-directed then please be sure to add an
   * Edge going from target to source.
   *
   * @param source The source Vertex for the Edge.
   * @param target The target Vertex for the Edge.
   * @param label  The label to use for the new Edge.
   * @param weight The weight to use for the new Edge.
   * @return True if the Edge was created and added to this Graph; otherwise false.
   */
  public boolean add(
      @NotNull Vertex<T, E> source,
      @NotNull Vertex<T, E> target,
      @NotNull String label,
      @NotNull E weight
  ) {
    if (!vertices.containsKey(Objects.requireNonNull(source).getId())
        || !vertices.containsKey(Objects.requireNonNull(target).getId())) {
      return false;
    }

    return source.add(target, Objects.requireNonNull(label), Objects.requireNonNull(weight));
  }

  /**
   * Adds a new Edge to this Graph from source to target with the specified label and no weight.
   * Both source and target must already exist as Vertices in this Graph or else the add will fail.
   *
   * <p><br>
   * Also note that if you want to make this Edge non-directed then please be sure to add an
   * Edge going from target to source.
   *
   * @param source The source Vertex for the Edge.
   * @param target The target Vertex for the Edge.
   * @param label  The label to use for the new Edge.
   * @return True if the Edge was created and added to this Graph; otherwise false.
   */
  public boolean add(
      @NotNull Vertex<T, E> source,
      @NotNull Vertex<T, E> target,
      @NotNull String label
  ) {
    if (!vertices.containsKey(Objects.requireNonNull(source).getId())
        || !vertices.containsKey(Objects.requireNonNull(target).getId())) {
      return false;
    }

    return source.add(target, Objects.requireNonNull(label));
  }

  /**
   * Adds a new Edge to this Graph from source to target with a default label and no weight.
   * Both source and target must already exist as Vertices in this Graph or else the add will fail.
   *
   * <p><br>
   * Also note that if you want to make this Edge non-directed then please be sure to add an
   * Edge going from target to source.
   *
   * @param source The source Vertex for the Edge.
   * @param target The target Vertex for the Edge.
   * @return True if the Edge was created and added to this Graph; otherwise false.
   */
  public boolean add(@NotNull Vertex<T, E> source, @NotNull Vertex<T, E> target) {
    if (!vertices.containsKey(Objects.requireNonNull(source).getId())
        || !vertices.containsKey(Objects.requireNonNull(target).getId())) {
      return false;
    }

    return source.add(target);
  }

  /**
   * Adds a new Edge to this Graph from source to target with the specified label and weight.
   * Both source and target must already exist as Vertices in this Graph or else the add will fail.
   *
   * <p><br>
   * Also note that if you want to make this Edge non-directed then please be sure to add an
   * Edge going from target to source.
   *
   * @param sourceId The ID of the source Vertex for the Edge.
   * @param targetId The ID of the target Vertex for the Edge.
   * @param label    The label to use for the new Edge.
   * @param weight   The weight to use for the new Edge.
   * @return True if the Edge was created and added to this Graph; otherwise false.
   */
  public boolean add(
      @NotNull String sourceId,
      @NotNull String targetId,
      @NotNull String label,
      @NotNull E weight
  ) {
    if (!vertices.containsKey(Objects.requireNonNull(sourceId))
        || !vertices.containsKey(Objects.requireNonNull(targetId))) {
      return false;
    }

    return vertices
        .get(sourceId)
        .add(vertices.get(targetId), Objects.requireNonNull(label), Objects.requireNonNull(weight));
  }

  /**
   * Adds a new Edge to this Graph from source to target with the specified label and no weight.
   * Both source and target must already exist as Vertices in this Graph or else the add will fail.
   *
   * <p><br>
   * Also note that if you want to make this Edge non-directed then please be sure to add an
   * Edge going from target to source.
   *
   * @param sourceId The ID of the source Vertex for the Edge.
   * @param targetId The ID of the target Vertex for the Edge.
   * @param label    The label to use for the new Edge.
   * @return True if the Edge was created and added to this Graph; otherwise false.
   */
  public boolean add(@NotNull String sourceId, @NotNull String targetId, @NotNull String label) {
    if (!vertices.containsKey(Objects.requireNonNull(sourceId))
        || !vertices.containsKey(Objects.requireNonNull(targetId))) {
      return false;
    }

    return vertices.get(sourceId).add(vertices.get(targetId), Objects.requireNonNull(label));
  }

  /**
   * Adds a new Edge to this Graph from source to target with a default label and no weight.
   * Both source and target must already exist as Vertices in this Graph or else the add will fail.
   *
   * <p><br>
   * Also note that if you want to make this Edge non-directed then please be sure to add an
   * Edge going from target to source.
   *
   * @param sourceId The ID of the source Vertex for the Edge.
   * @param targetId The ID of the target Vertex for the Edge.
   * @return True if the Edge was created and added to this Graph; otherwise false.
   */
  public boolean add(@NotNull String sourceId, @NotNull String targetId) {
    if (!vertices.containsKey(Objects.requireNonNull(sourceId))
        || !vertices.containsKey(Objects.requireNonNull(targetId))) {
      return false;
    }

    return vertices.get(sourceId).add(vertices.get(targetId));
  }

  /**
   * Removes all edges from and to the specified vertex and removes it from this graph.
   * Returns a reference to the vertex that was removed. If the specified vertex is not
   * in this graph, an empty Optional is returned.
   *
   * @param vertexId The identifier for the vertex to remove.
   * @return Returns an Optional with a reference to the vertex that was removed with no edges.
   *     If the specified vertex is not in this graph, the Optional will be empty.
   */
  @NotNull
  public Optional<Vertex<T, E>> remove(@NotNull String vertexId) {
    if (!vertices.containsKey(Objects.requireNonNull(vertexId))) {
      return Optional.empty();
    }

    Vertex<T, E> vertex = vertices.get(vertexId);
    getEdgesTo(vertex).forEach(this::remove);
    vertex.clear();
    return Optional.ofNullable(vertices.remove(vertexId));
  }

  /**
   * Removes the specified edge from the source Vertex contained within the Edge.
   * If the Edge was found and successfully removed, the reference to it will
   * be in the returned Optional.
   *
   * @param edge The instance of the Edge to remove.
   * @return An Optional that contains a reference to the removed Edge if it existed.
   */
  @NotNull
  public Optional<Edge<E>> remove(@NotNull Edge<E> edge) {
    Objects.requireNonNull(edge);

    if (!vertices.containsKey(Objects.requireNonNull(edge.getSource()))
        && !vertices.containsKey(Objects.requireNonNull(edge.getTarget()))) {
      return Optional.empty();
    }

    return vertices.get(edge.getSource()).remove(edge.getTarget());
  }

  /**
   * Removes all edges from and to the specified vertex and removes it from this graph.
   * Returns a reference to the vertex that was removed. If the specified vertex is not
   * in this graph, an empty Optional is returned.
   *
   * @param vertex The instance of the Vertex to remove from this Graph.
   * @return Returns an Optional with a reference to the vertex that was removed with no edges.
   *     If the specified vertex is not in this graph, the Optional will be empty.
   */
  @NotNull
  public Optional<Vertex<T, E>> remove(@NotNull Vertex<T, E> vertex) {
    return remove(Objects.requireNonNull(vertex).getId());
  }

  /**
   * Removes the Edge going from source to target Vertex.
   *
   * @param source The source Vertex that contains the Edge.
   * @param target The target Vertex of the Edges we are removing.
   * @return The removed Edge.
   */
  public @NotNull Optional<Edge<E>> remove(
      @NotNull Vertex<T, E> source,
      @NotNull Vertex<T, E> target
  ) {
    return remove(Objects.requireNonNull(source).getId(), Objects.requireNonNull(target).getId());
  }

  /**
   * Removes the Edge going from source to target Vertex.
   *
   * @param source The source Vertex ID that contains the Edge.
   * @param target The target Vertex ID of the Edges we are removing.
   * @return The removed Edge.
   */
  public @NotNull Optional<Edge<E>> remove(@NotNull String source, @NotNull String target) {
    if (!vertices.containsKey(Objects.requireNonNull(source))
        && !vertices.containsKey(Objects.requireNonNull(target))) {
      return Optional.empty();
    }

    return vertices.get(source).remove(vertices.get(target).getId());
  }

  /**
   * Returns the number of Vertices in this Graph.
   *
   * @return The number of Vertices in this Graph.
   */
  public int size() {
    return vertices.size();
  }

  /**
   * Returns an Optional with a reference to the specified Vertex ID.
   *
   * @param vertexId The ID of the Vertex to retrieve from this Graph.
   * @return An Optional with a reference to the specified Vertex ID.
   */
  @NotNull
  public Optional<Vertex<T, E>> getVertex(@NotNull String vertexId) {
    return Optional.ofNullable(vertices.get(Objects.requireNonNull(vertexId)));
  }

  /**
   * Returns the Set of IDs for the Vertices in this Graph.
   *
   * @return The Set of IDs for the Vertices in this Graph.
   */
  @NotNull
  public Collection<String> getVertexIds() {
    return Set.copyOf(vertices.keySet());
  }

  /**
   * Removes all Edges from all Vertices and all Vertices from this Graph.
   */
  public void clear() {
    removeEdges();
    vertices.clear();
  }

  /**
   * Adds the Vertices of the specified Edge to this Graph and then adds
   * the Edge to this Graph.
   *
   * @param edge The Edge and Vertices to add to this Graph.
   */
  public void addAsNew(@NotNull Edge<E> edge) {
    add(edge.getSource());
    add(edge.getTarget());
    edge.getWeight().ifPresentOrElse(
        weight -> add(edge.getSource(), edge.getTarget(), edge.getLabel(), weight),
        () -> add(edge.getSource(), edge.getTarget(), edge.getLabel())
    );
  }

  /**
   * Returns the number of Edges in all Vertices in this Graph.
   *
   * @return The number of Edges in all Vertices in this Graph.
   */
  public int edgeCount() {
    return vertices.values().stream().mapToInt(Vertex::size).sum();
  }

  /**
   * Removes all Edges from all Vertices in this Graph.
   */
  public void removeEdges() {
    vertices.values().forEach(Vertex::clear);
  }

  /**
   * Returns an unmodifiable Set of all Edges in this Graph.
   *
   * @return An unmodifiable Set of all Edges in this Graph.
   */
  @NotNull
  public Set<Edge<E>> getEdges() {
    return vertices
        .values()
        .stream()
        .flatMap(it -> it.getEdgeSet().stream())
        .collect(Collectors.toSet());
  }

  /**
   * Returns a Set of all Edges coming from the specified Vertex.
   *
   * @param vertex The Vertex to retrieve the Edges from.
   * @return A Set of all Edges coming from the specified Vertex.
   */
  @NotNull
  public Set<Edge<E>> getEdges(@NotNull Vertex<T, E> vertex) {
    return Objects.requireNonNull(vertex).getEdgeSet();
  }

  /**
   * Returns a Set of all Edges coming from the specified Vertex ID.
   *
   * @param vertexId The Vertex ID to retrieve the Edges from.
   * @return A Set of all Edges coming from the specified Vertex ID.
   */
  @NotNull
  public Set<Edge<E>> getEdges(@NotNull String vertexId) {
    Objects.requireNonNull(vertexId);

    if (!vertices.containsKey(Objects.requireNonNull(vertexId))) {
      return Collections.emptySet();
    }

    return getEdges(vertices.get(vertexId));
  }

  /**
   * Returns a List of Edges that connect to the specified Vertex; which includes
   * any self edges.
   *
   * @param target The Vertex that the edges need to connect to.
   * @return The Set of Edges containing the requested target.
   */
  @NotNull
  public Set<Edge<E>> getEdgesTo(@NotNull Vertex<T, E> target) {
    Objects.requireNonNull(target);
    var answer = new HashSet<Edge<E>>();

    vertices.values()
        .forEach((vertex) -> {
          if (vertex.getEdges().containsKey(target.getId())) {
            answer.add(vertex.getEdges().get(target.getId()));
          }
        });

    return answer;
  }

  /**
   * Returns a List of Edges that connect to the specified Vertex; which includes
   * any self edges.
   *
   * @param targetId The Vertex ID that the edges need to connect to.
   * @return The Set of Edges containing the requested target.
   */
  @NotNull
  public Set<Edge<E>> getEdgesTo(@NotNull String targetId) {
    Objects.requireNonNull(targetId);

    if (!vertices.containsKey(Objects.requireNonNull(targetId))) {
      return Collections.emptySet();
    }

    return getEdgesTo(vertices.get(targetId));
  }

  /**
   * Returns true if a Vertex with the specified vertexId is in this Graph.
   *
   * @param vertexId The vertexId to check for existence.
   * @return True if a Vertex with the specified vertexId is in this Graph.
   */
  public boolean contains(@NotNull String vertexId) {
    return vertices.containsKey(vertexId);
  }
}
