package com.capital7software.aoc2015.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * An Edge in a Graph connects the source Node to the target Node. The target Node may be the same as the
 * source Node if a self-edge is being created.
 * <p>
 * Edge's are considered to be directed in that they go from source to target. In an undirected graph, another
 * Edge should be added going from target to source to make it undirected.
 * <p>
 * The label of an Edge can be used simply as a label. If a label is not provided then a default one of the form
 * source.id-target.id-randomNumber is used.
 * <p>
 * The weight of an Edge is optional and may be null.
 * <p>
 * Two Edges are considered equal if they have the same source and target Nodes.
 *
 * @param <T> The type of the value that the Nodes contain.
 * @param <E> The type of the weight for this Edge.
 */
public class Edge<T extends Comparable<T>, E extends Comparable<E>> {
    private final Node<T, E> source;
    private final Node<T, E> target;
    private final String label;
    private E weight;

    /**
     * Instantiates a new Edge with the specified source, target, label, and weight.
     *
     * @param source The source node that this Edge is coming from.
     * @param target The target node that this Edge is going to.
     * @param label  The label of this Edge; it is not optional and should be non-null.
     * @param weight The weight of this Edge, which may be null.
     */
    public Edge(@NotNull Node<T, E> source, @NotNull Node<T, E> target, @NotNull String label, E weight) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.label = Objects.requireNonNull(label);
        this.weight = weight;
    }

    /**
     * Instantiates a new Edge with the specified source, target, a label in the form of
     * source.id-target.id-randomNumber and no weight.
     *
     * @param source The source node that this Edge is coming from.
     * @param target The target node that this Edge is going to.
     */
    public Edge(@NotNull Node<T, E> source, @NotNull Node<T, E> target) {
        this(source, target, source.getId() + "-" + target.getId() + "-" +
                (int) (Math.random() * 100_000) + 1, null);
    }

    /**
     * @return The source Node of this Edge.
     */
    @NotNull
    public Node<T, E> getSource() {
        return source;
    }

    /**
     * @return The target Node of this Edge.
     */
    @NotNull
    public Node<T, E> getTarget() {
        return target;
    }

    /**
     * @return The label of this Edge.
     */
    @NotNull
    public String getLabel() {
        return label;
    }

    /**
     *
     * @return An Optional of the weight of this Edge.
     */
    @NotNull
    public Optional<E> getWeight() {
        return Optional.ofNullable(weight);
    }

    /**
     * Sets the weight of this Edge to the specified weight.
     *
     * @param weight The new weight of this Edge.
     */
    public void setWeight(E weight) {
        this.weight = weight;
    }

    /**
     * Returns true if the specified target Node is the target of this Edge.
     *
     * @param target The Node to test against.
     * @return True if the specified target Node is the target of this Edge.
     */
    public boolean hasTarget(@NotNull Node<T, E> target) {
        return this.target.equals(target);
    }

    /**
     * Returns true if the specified source Node is the source of this Edge.
     *
     * @param source The Node to test against.
     * @return True if the specified source Node is the source of this Edge.
     */
    public boolean hasSource(@NotNull Node<T, E> source) {
        return this.source.equals(source);
    }

    /**
     * Returns true if the specified label is the label of this Edge.
     * @param label The label to test against.
     * @return True if the specified label is the label of this Edge.
     */
    public boolean hasLabel(@NotNull String label) {
        return this.label.equals(label);
    }


    /**
     * Two Edges are equal if they have the same source and target Nodes.
     *
     * @param o The other Edge to compare against.
     * @return True if the two Edges are considered to be equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge<?, ?> edge)) return false;
        return getSource().equals(edge.getSource()) &&
                getTarget().equals(edge.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTarget());
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", target=" + target +
                ", label=" + label +
                ", weight=" + weight +
                '}';
    }
}
