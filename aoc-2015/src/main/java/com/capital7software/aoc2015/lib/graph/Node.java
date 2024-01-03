package com.capital7software.aoc2015.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A Node in a Graph has an ID, name, value, and a set of Edges that connect it to other Nodes in the Graph.
 * <p>
 * A Graph stores a Node in a Map using its ID as the key and so it is recommended to use as few characters
 * as possible to uniquely identify Nodes.
 * <p>
 * Two Nodes are considered to be equal if they have the same ID.
 *
 * @param <T> The type of the value held by this Node.
 * @param <E> The type of the weight for Edges connected to and from this Node.
 */
public class Node<T extends Comparable<T>, E extends Comparable<E>> {
    private final String id;
    private String name;
    private T value;
    private final Map<String, Edge<T, E>> edges;

    /**
     * Instantiates a new Node with the specified ID, name, value, and set of Edges.
     *
     * @param id    The identifier of this Node.
     * @param name  The name of this Node.
     * @param value The value contained by this Node.
     * @param edges The Map to store the set of edges coming from this Node.
     */
    public Node(@NotNull String id, @NotNull String name, T value, @NotNull Map<String, Edge<T, E>> edges) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.value = value;
        this.edges = new HashMap<>(Objects.requireNonNull(edges));
    }

    /**
     * Instantiates a new Node with the specified ID. The name of the node will be the same as the ID, and it will
     * have no value and contain no edges.
     *
     * @param id The identifier of this Node.
     */
    public Node(@NotNull String id) {
        this(id, id, null, Collections.emptyMap());
    }

    /**
     * Instantiates a new Node with the specified ID. The name of the node will be the same as the ID, and it will
     * have the specified value and contain no edges.
     *
     * @param id    The identifier of this Node.
     * @param value The non-null value contained by this Node.
     */
    public Node(@NotNull String id, @NotNull T value) {
        this(id, id, value, Collections.emptyMap());
    }

    /**
     * @return The ID of this Node.
     */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * @return The name of this Node.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this Node to the specified value. Must be non-null!
     *
     * @param name The new name for this Node.
     */
    public void setName(@NotNull String name) {
        this.name = Objects.requireNonNull(name);
    }

    /**
     * @return An Optional of the value held by this Node.
     */
    @NotNull
    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    /**
     * @param value Sets the value of this Node to the specified value, which may be null.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @return An unmodifiable copy of the Edges in this Node.
     */
    @NotNull
    public Map<String, Edge<T, E>> getEdges() {
        return Collections.unmodifiableMap(edges);
    }

    /**
     * @return A copy of the set of all Edges in this Node.
     */
    @NotNull
    public Set<Edge<T, E>> getEdgeSet() {
        return new HashSet<>(edges.values());
    }

    /**
     * Adds a new Edge from this Node to target Node with the specified label and weight, which may be null.
     * <p>
     * A Node can only have one edge to a target. If an Edge already exists for the specified target, the add
     * is aborted and false is returned.
     *
     * @param target The non-null target Node for the new Edge.
     * @param label  The required label of the new Edge.
     * @param weight The optional weight of the new Edge.
     * @return True if a new Edge was added.
     */
    public boolean add(@NotNull Node<T, E> target, @NotNull String label, E weight) {
        return edges.putIfAbsent(target.getId(), new Edge<>(
                this,
                Objects.requireNonNull(target),
                Objects.requireNonNull(label),
                weight)
        ) == null;
    }

    /**
     * Adds a new Edge from this Node to target Node with the specified label and no weight.
     * <p>
     * A Node can only have one edge to a target. If an Edge already exists for the specified target, the add
     * is aborted and false is returned.
     *
     * @param target The non-null target Node for the new Edge.
     * @param label  The required label of the new Edge.
     * @return True if a new Edge was added.
     */
    public boolean add(@NotNull Node<T, E> target, @NotNull String label) {
        return edges.putIfAbsent(target.getId(), new Edge<>(
                this, Objects.requireNonNull(target), Objects.requireNonNull(label), null)
        ) == null;
    }

    /**
     * Adds a new Edge from this Node to target Node with a label in the form of source.id-target.id-randomNumber.
     * <p>
     * A Node can only have one edge to a target. If an Edge already exists for the specified target, the add
     * is aborted and false is returned.
     *
     * @param target The non-null target Node for the new Edge.
     * @return True if a new Edge was added.
     */
    public boolean add(@NotNull Node<T, E> target) {
        return edges.putIfAbsent(target.getId(), new Edge<>(
                this, Objects.requireNonNull(target))
        ) == null;
    }

    /**
     * @return The number of Edges coming from this Node by target.
     */
    public int size() {
        return edges.values().size();
    }

    /**
     * Removes all the Edges coming from this Node.
     */
    public void clear() {
        edges.clear();
    }

    /**
     * Removes the Edge from this Node to the target Node if any exist.
     * The removed Edge is returned.
     *
     * @param target The target Node of the Edge that this Node connects to.
     * @return The removed Edge.
     */
    @NotNull
    public Optional<Edge<T, E>> remove(@NotNull Node<T, E> target) {
        return Optional.ofNullable(edges.remove(Objects.requireNonNull(target).getId()));
    }

    /**
     * Two Nodes are considered equal if they have the same ID.
     *
     * @param o The other Node to check for equality against.
     * @return True if the two Nodes have the same ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node<?, ?> node)) return false;
        return getId().equals(node.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", edges=" + edges.size() +
                '}';
    }
}
