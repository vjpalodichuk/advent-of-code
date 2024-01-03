package com.capital7software.aoc2015.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A Graph that uses adjacency lists to represent the Nodes and Edges in this Graph.
 * <p>
 * By convention, this Graph is considered to be directional. To make it non-directional, for every
 * Edge that is added, add another edge by reversing the source and target Nodes. This will ensure that
 * for every Edge from Node A to Node B there is also an Edge that goes from Node B to Node A.
 * <p>
 * When building a Graph, all the Nodes should be added to this Graph and then all the Edges should be
 * added to connect the Nodes.
 * <p>
 * Attempting to add an Edge to a Node that doesn't yet exist in the Graph will cause the add to fail.
 * <p>
 * Nodes can easily be added with just their ID and nothing else.
 * <p>
 * Operations for searching the Graph are handled by other Algorithm classes.
 * <p>
 * This Graph simply contains methods that allow other Graph Algorithms to operate on this Graph.
 *
 * @param <T> The type of the value held by Nodes in this Graph.
 * @param <E> The type of the weight for Edges connected to and from Nodes in this Graph.
 */
public class Graph<T extends Comparable<T>, E extends Comparable<E>> {
    private static final String DEFAULT_NAME = "graph";

    private final String name;
    private final Map<String, Node<T, E>> nodes;

    /**
     * Instantiates a new Graph with the specified name and map of Nodes.
     *
     * @param name  The required name of this Graph.
     * @param nodes The required map to hold the Nodes of this Graph.
     */
    public Graph(@NotNull String name, @NotNull Map<String, Node<T, E>> nodes) {
        this.name = Objects.requireNonNull(name);
        this.nodes = new HashMap<>(Objects.requireNonNull(nodes));
    }

    /**
     * Instantiates a new Graph with a default name of graph and no Nodes.
     */
    public Graph() {
        this(DEFAULT_NAME, new HashMap<>());
    }

    /**
     * Instantiates a new Graph with the specified name and an empty map of Nodes.
     *
     * @param name The name to use for this Graph.
     */
    public Graph(@NotNull String name) {
        this(name, new HashMap<>());
    }

    /**
     * @return The name of this Graph.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return The map of Nodes in this Graph.
     */
    @NotNull
    public Map<String, Node<T, E>> getNodes() {
        return Collections.unmodifiableMap(nodes);
    }

    /**
     * Two Graphs are considered equal if they have the same name.
     *
     * @param o The other Graph to test for equality against.
     * @return True if the other Graph has the same name as this Graph.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph<?, ?> graph)) return false;
        return getName().equals(graph.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Graph{" +
                "name='" + name + '\'' +
                ", nodes=" + nodes +
                '}';
    }

    /**
     * Adds a new Node to this Graph with the specified nodeId.
     *
     * @param nodeId The nodeId of the new Node that is being added.
     * @return True if the Node was successfully added; false if a
     * Node with that ID already exists in this Graph.
     */
    public boolean add(@NotNull String nodeId) {
        if (nodes.containsKey(Objects.requireNonNull(nodeId))) {
            return false;
        }

        return add(new Node<>(nodeId));
    }

    /**
     * Adds an existing Node to this Graph.
     *
     * @param node The existing Node to add to this Graph.
     * @return True if the Node was successfully added; false if a
     * Node with the same ID already exists in this Graph.
     */
    public boolean add(@NotNull Node<T, E> node) {
        Objects.requireNonNull(node);

        return nodes.putIfAbsent(node.getId(), node) == null;
    }


    /**
     * Removes all edges from and to the specified node and removes it from this graph.
     * Returns a reference to the node that was removed. If the specified node is not
     * in this graph, an empty Optional is returned.
     *
     * @param nodeId The identifier for the node to remove.
     * @return Returns an Optional with a reference to the node that was removed with no edges.
     * If the specified node is not in this graph, the Optional will be empty.
     */
    @NotNull
    public Optional<Node<T, E>> remove(@NotNull String nodeId) {
        if (!nodes.containsKey(Objects.requireNonNull(nodeId))) {
            return Optional.empty();
        }

        Node<T, E> node = nodes.get(nodeId);
        getEdgesTo(node).forEach(this::remove);
        node.clear();
        return Optional.ofNullable(nodes.remove(nodeId));
    }

    /**
     * Removes all edges from and to the specified node and removes it from this graph.
     * Returns a reference to the node that was removed. If the specified node is not
     * in this graph, an empty Optional is returned.
     *
     * @param node The instance of the Node to remove from this Graph.
     * @return Returns an Optional with a reference to the node that was removed with no edges.
     * If the specified node is not in this graph, the Optional will be empty.
     */
    @NotNull
    public Optional<Node<T, E>> remove(@NotNull Node<T, E> node) {
        return remove(Objects.requireNonNull(node).getId());
    }

    /**
     * @return The number of Nodes in this Graph.
     */
    public int size() {
        return nodes.size();
    }

    /**
     * @param nodeId The ID of the Node to retrieve from this Graph.
     * @return An Optional with a reference to the specified Node ID.
     */
    @NotNull
    public Optional<Node<T, E>> getNode(@NotNull String nodeId) {
        return Optional.ofNullable(nodes.get(Objects.requireNonNull(nodeId)));
    }

    /**
     * @return The Set of IDs for the Nodes in this Graph.
     */
    @NotNull
    public Collection<String> getNodeIds() {
        return Set.copyOf(nodes.keySet());
    }

    /**
     * Removes all Edges from all Nodes and all Nodes from this Graph.
     */
    public void clear() {
        removeEdges();
        nodes.clear();
    }

    /**
     * Adds a new Edge to this Graph from source to target with the specified label and weight.
     * Both source and target must already exist as Nodes in this Graph or else the add will fail.
     * <p>
     * Also note that if you want to make this Edge non-directed then please be sure to add an
     * Edge going from target to source.
     *
     * @param source The source Node for the Edge.
     * @param target The target Node for the Edge.
     * @param label  The label to use for the new Edge.
     * @param weight The weight to use for the new Edge.
     * @return True if the Edge was created and added to this Graph; otherwise false.
     */
    public boolean add(@NotNull Node<T, E> source, @NotNull Node<T, E> target, @NotNull String label, @NotNull E weight) {
        if (!nodes.containsKey(Objects.requireNonNull(source).getId()) ||
                !nodes.containsKey(Objects.requireNonNull(target).getId())) {
            return false;
        }

        return source.add(target, Objects.requireNonNull(label), Objects.requireNonNull(weight));
    }

    /**
     * Adds a new Edge to this Graph from source to target with the specified label and no weight.
     * Both source and target must already exist as Nodes in this Graph or else the add will fail.
     * <p>
     * Also note that if you want to make this Edge non-directed then please be sure to add an
     * Edge going from target to source.
     *
     * @param source The source Node for the Edge.
     * @param target The target Node for the Edge.
     * @param label  The label to use for the new Edge.
     * @return True if the Edge was created and added to this Graph; otherwise false.
     */
    public boolean add(@NotNull Node<T, E> source, @NotNull Node<T, E> target, @NotNull String label) {
        if (!nodes.containsKey(Objects.requireNonNull(source).getId()) ||
                !nodes.containsKey(Objects.requireNonNull(target).getId())) {
            return false;
        }

        return source.add(target, Objects.requireNonNull(label));
    }

    /**
     * Adds a new Edge to this Graph from source to target with a default label and no weight.
     * Both source and target must already exist as Nodes in this Graph or else the add will fail.
     * <p>
     * Also note that if you want to make this Edge non-directed then please be sure to add an
     * Edge going from target to source.
     *
     * @param source The source Node for the Edge.
     * @param target The target Node for the Edge.
     * @return True if the Edge was created and added to this Graph; otherwise false.
     */
    public boolean add(@NotNull Node<T, E> source, @NotNull Node<T, E> target) {
        if (!nodes.containsKey(Objects.requireNonNull(source).getId()) ||
                !nodes.containsKey(Objects.requireNonNull(target).getId())) {
            return false;
        }

        return source.add(target);
    }

    /**
     * Adds a new Edge to this Graph from source to target with the specified label and weight.
     * Both source and target must already exist as Nodes in this Graph or else the add will fail.
     * <p>
     * Also note that if you want to make this Edge non-directed then please be sure to add an
     * Edge going from target to source.
     *
     * @param sourceId The ID of the source Node for the Edge.
     * @param targetId The ID of the target Node for the Edge.
     * @param label    The label to use for the new Edge.
     * @param weight   The weight to use for the new Edge.
     * @return True if the Edge was created and added to this Graph; otherwise false.
     */
    public boolean add(@NotNull String sourceId, @NotNull String targetId, @NotNull String label, @NotNull E weight) {
        if (!nodes.containsKey(Objects.requireNonNull(sourceId)) ||
                !nodes.containsKey(Objects.requireNonNull(targetId))) {
            return false;
        }

        return nodes.get(sourceId).add(nodes.get(targetId), Objects.requireNonNull(label), Objects.requireNonNull(weight));
    }

    /**
     * Adds a new Edge to this Graph from source to target with the specified label and no weight.
     * Both source and target must already exist as Nodes in this Graph or else the add will fail.
     * <p>
     * Also note that if you want to make this Edge non-directed then please be sure to add an
     * Edge going from target to source.
     *
     * @param sourceId The ID of the source Node for the Edge.
     * @param targetId The ID of the target Node for the Edge.
     * @param label    The label to use for the new Edge.
     * @return True if the Edge was created and added to this Graph; otherwise false.
     */
    public boolean add(@NotNull String sourceId, @NotNull String targetId, @NotNull String label) {
        if (!nodes.containsKey(Objects.requireNonNull(sourceId)) ||
                !nodes.containsKey(Objects.requireNonNull(targetId))) {
            return false;
        }

        return nodes.get(sourceId).add(nodes.get(targetId), Objects.requireNonNull(label));
    }

    /**
     * Adds a new Edge to this Graph from source to target with a default label and no weight.
     * Both source and target must already exist as Nodes in this Graph or else the add will fail.
     * <p>
     * Also note that if you want to make this Edge non-directed then please be sure to add an
     * Edge going from target to source.
     *
     * @param sourceId The ID of the source Node for the Edge.
     * @param targetId The ID of the target Node for the Edge.
     * @return True if the Edge was created and added to this Graph; otherwise false.
     */
    public boolean add(@NotNull String sourceId, @NotNull String targetId) {
        if (!nodes.containsKey(Objects.requireNonNull(sourceId)) || !nodes.containsKey(Objects.requireNonNull(targetId))) {
            return false;
        }

        return nodes.get(sourceId).add(nodes.get(targetId));
    }

    /**
     * @return The number of Edges in all Nodes in this Graph.
     */
    public int edgeCount() {
        return nodes.values().stream().mapToInt(Node::size).sum();
    }

    /**
     * Removes all Edges from all Nodes in this Graph.
     */
    public void removeEdges() {
        nodes.values().forEach(Node::clear);
    }

    /**
     * Removes the specified edge from the source Node contained within the Edge.
     * If the Edge was found and successfully removed, the reference to it will
     * be in the returned Optional.
     *
     * @param edge The instance of the Edge to remove.
     * @return An Optional that contains a reference to the removed Edge if it existed.
     */
    @NotNull
    public Optional<Edge<T, E>> remove(@NotNull Edge<T, E> edge) {
        Objects.requireNonNull(edge);

        if (!nodes.containsKey(Objects.requireNonNull(edge.getSource()).getId()) &&
                !nodes.containsKey(Objects.requireNonNull(edge.getTarget()).getId())) {
            return Optional.empty();
        }

        return nodes.get(edge.getSource().getId()).remove(edge.getTarget());
    }

    /**
     * Removes the Edge going from source to target Node.
     *
     * @param source The source Node that contains the Edge.
     * @param target The target Node of the Edges we are removing.
     * @return The removed Edge.
     */
    public @NotNull Optional<Edge<T, E>> remove(@NotNull Node<T, E> source, @NotNull Node<T, E> target) {
        return remove(Objects.requireNonNull(source).getId(), Objects.requireNonNull(target).getId());
    }

    /**
     * Removes the Edge going from source to target Node.
     *
     * @param source The source Node ID that contains the Edge.
     * @param target The target Node ID of the Edges we are removing.
     * @return The removed Edge.
     */
    public @NotNull Optional<Edge<T, E>> remove(@NotNull String source, @NotNull String target) {
        if (!nodes.containsKey(Objects.requireNonNull(source)) && !nodes.containsKey(Objects.requireNonNull(target))) {
            return Optional.empty();
        }

        return nodes.get(source).remove(nodes.get(target));
    }

    /**
     * @return An unmodifiable Set of all Edges in this Graph.
     */
    @NotNull
    public Set<Edge<T, E>> getEdges() {
        return nodes.values().stream().flatMap(it -> it.getEdgeSet().stream()).collect(Collectors.toSet());
    }

    /**
     * Returns a Set of all Edges coming from the specified Node.
     *
     * @param node The Node to retrieve the Edges from.
     * @return A Set of all Edges coming from the specified Node.
     */
    @NotNull
    public Set<Edge<T, E>> getEdges(@NotNull Node<T, E> node) {
        return Objects.requireNonNull(node).getEdgeSet();
    }

    /**
     * Returns a Set of all Edges coming from the specified Node ID.
     *
     * @param nodeId The Node ID to retrieve the Edges from.
     * @return A Set of all Edges coming from the specified Node ID.
     */
    @NotNull
    public Set<Edge<T, E>> getEdges(@NotNull String nodeId) {
        Objects.requireNonNull(nodeId);

        if (!nodes.containsKey(Objects.requireNonNull(nodeId))) {
            return Collections.emptySet();
        }

        return getEdges(nodes.get(nodeId));
    }

    /**
     * Returns a List of Edges that connect to the specified Node; which includes
     * any self edges.
     *
     * @param target The Node that the edges need to connect to.
     * @return The Set of Edges containing the requested target.
     */
    @NotNull
    public Set<Edge<T, E>> getEdgesTo(@NotNull Node<T, E> target) {
        Objects.requireNonNull(target);
        var answer = new HashSet<Edge<T, E>>();

        nodes.values()
                .forEach((node) -> {
                    if (node.getEdges().containsKey(target.getId())) {
                        answer.add(node.getEdges().get(target.getId()));
                    }
                });

        return answer;
    }

    /**
     * Returns a List of Edges that connect to the specified Node; which includes
     * any self edges.
     *
     * @param targetId The Node ID that the edges need to connect to.
     * @return The Set of Edges containing the requested target.
     */
    @NotNull
    public Set<Edge<T, E>> getEdgesTo(@NotNull String targetId) {
        Objects.requireNonNull(targetId);

        if (!nodes.containsKey(Objects.requireNonNull(targetId))) {
            return Collections.emptySet();
        }

        return getEdgesTo(nodes.get(targetId));
    }
}
