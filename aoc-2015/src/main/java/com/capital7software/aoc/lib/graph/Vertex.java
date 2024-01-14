package com.capital7software.aoc.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A Vertex in a Graph has an ID, name, value, and a set of Edges that connect it to other Nodes in the Graph.
 * <p>
 * A Graph stores a Vertex in a Map using its ID as the key and so it is recommended to use as few characters
 * as possible to uniquely identify Nodes.
 * <p>
 * Two Nodes are considered to be equal if they have the same ID.
 * <p>
 * Two Nodes are Comparable if they have a non-null value.
 *
 * @param <T> The type of the value held by this Vertex.
 * @param <E> The type of the weight for Edges connected to and from this Vertex.
 */
public class Vertex<T extends Comparable<T>, E extends Comparable<E>> implements Comparable<Vertex<T, E>> {
    private final String id;
    private String name;
    private T value;
    private final Map<String, Edge<T, E>> edges;

    /**
     * Instantiates a new Vertex with the specified ID, name, value, and set of Edges.
     *
     * @param id    The identifier of this Vertex.
     * @param name  The name of this Vertex.
     * @param value The value contained by this Vertex.
     * @param edges The Map to store the set of edges coming from this Vertex.
     */
    public Vertex(@NotNull String id, @NotNull String name, T value, @NotNull Map<String, Edge<T, E>> edges) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.value = value;
        this.edges = new HashMap<>(Objects.requireNonNull(edges));
    }

    /**
     * Instantiates a new Vertex with the specified ID. The name of the vertex will be the same as the ID, and it will
     * have no value and contain no edges.
     *
     * @param id The identifier of this Vertex.
     */
    public Vertex(@NotNull String id) {
        this(id, id, null, Collections.emptyMap());
    }

    /**
     * Instantiates a new Vertex with the specified ID. The name of the vertex will be the same as the ID, and it will
     * have the specified value and contain no edges.
     *
     * @param id    The identifier of this Vertex.
     * @param value The non-null value contained by this Vertex.
     */
    public Vertex(@NotNull String id, @NotNull T value) {
        this(id, id, value, Collections.emptyMap());
    }

    /**
     * @return The ID of this Vertex.
     */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * @return The name of this Vertex.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this Vertex to the specified value. Must be non-null!
     *
     * @param name The new name for this Vertex.
     */
    public void setName(@NotNull String name) {
        this.name = Objects.requireNonNull(name);
    }

    /**
     * @return An Optional of the value held by this Vertex.
     */
    @NotNull
    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    /**
     * @param value Sets the value of this Vertex to the specified value, which may be null.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @return An unmodifiable copy of the Edges in this Vertex.
     */
    @NotNull
    public Map<String, Edge<T, E>> getEdges() {
        return Collections.unmodifiableMap(edges);
    }

    /**
     * @return A copy of the set of all Edges in this Vertex.
     */
    @NotNull
    public Set<Edge<T, E>> getEdgeSet() {
        return new HashSet<>(edges.values());
    }

    /**
     * Adds a new Edge from this Vertex to target Vertex with the specified label and weight, which may be null.
     * <p>
     * A Vertex can only have one edge to a target. If an Edge already exists for the specified target, the add
     * is aborted and false is returned.
     *
     * @param target The non-null target Vertex for the new Edge.
     * @param label  The required label of the new Edge.
     * @param weight The optional weight of the new Edge.
     * @return True if a new Edge was added.
     */
    public boolean add(@NotNull Vertex<T, E> target, @NotNull String label, E weight) {
        return edges.putIfAbsent(target.getId(), new Edge<>(
                this,
                Objects.requireNonNull(target),
                Objects.requireNonNull(label),
                weight)
        ) == null;
    }

    /**
     * Adds a new Edge from this Vertex to target Vertex with the specified label and no weight.
     * <p>
     * A Vertex can only have one edge to a target. If an Edge already exists for the specified target, the add
     * is aborted and false is returned.
     *
     * @param target The non-null target Vertex for the new Edge.
     * @param label  The required label of the new Edge.
     * @return True if a new Edge was added.
     */
    public boolean add(@NotNull Vertex<T, E> target, @NotNull String label) {
        return edges.putIfAbsent(target.getId(), new Edge<>(
                this, Objects.requireNonNull(target), Objects.requireNonNull(label), null)
        ) == null;
    }

    /**
     * Adds a new Edge from this Vertex to target Vertex with a label in the form of source.id-target.id-randomNumber.
     * <p>
     * A Vertex can only have one edge to a target. If an Edge already exists for the specified target, the add
     * is aborted and false is returned.
     *
     * @param target The non-null target Vertex for the new Edge.
     * @return True if a new Edge was added.
     */
    public boolean add(@NotNull Vertex<T, E> target) {
        return edges.putIfAbsent(target.getId(), new Edge<>(
                this, Objects.requireNonNull(target))
        ) == null;
    }

    /**
     * @return The number of Edges coming from this Vertex by target.
     */
    public int size() {
        return edges.values().size();
    }

    /**
     * Removes all the Edges coming from this Vertex.
     */
    public void clear() {
        edges.clear();
    }

    /**
     * Removes the Edge from this Vertex to the target Vertex if any exist.
     * The removed Edge is returned.
     *
     * @param target The target Vertex of the Edge that this Vertex connects to.
     * @return The removed Edge.
     */
    @NotNull
    public Optional<Edge<T, E>> remove(@NotNull Vertex<T, E> target) {
        return Optional.ofNullable(edges.remove(Objects.requireNonNull(target).getId()));
    }

    /**
     * Two Nodes are considered equal if they have the same ID.
     *
     * @param o The other Vertex to check for equality against.
     * @return True if the two Nodes have the same ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex<?, ?> vertex)) return false;
        return getId().equals(vertex.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", edges=" + edges.size() +
                '}';
    }

    /**
     * The value of the Edge that has the least weight or an empty Optional if there is no such Edge.
     *
     * @return The value of the Edge that has the least weight or an empty Optional if there is no such Edge.
     */
    public Optional<E> getLeastWeightedEdgeValue() {
        return edges
                .values()
                .stream()
                .map(Edge::getWeight)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(Comparator.naturalOrder());
    }

    /**
     * Returns the Edge for the specified Vertex targetId or an empty Optional if there is no such Edge.
     *
     * @param targetId The Vertex to get the Edge to.
     * @return The Edge for the specified Vertex targetId.
     */
    public Optional<Edge<T, E>> getEdge(@NotNull String targetId) {
        return Optional.ofNullable(edges.get(targetId));
    }

    @Override
    public int compareTo(@NotNull Vertex<T, E> o) {
        if (value == null && o.value == null) {
            return 0;
        } else if (value != null && o.value != null) {
            return value.compareTo(o.value);
        } else if (value != null) {
            return -1;
        } else {
            return 1;
        }
    }
}
