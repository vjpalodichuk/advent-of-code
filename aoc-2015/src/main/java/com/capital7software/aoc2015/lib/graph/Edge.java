package com.capital7software.aoc2015.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * An Edge in a Graph connects the source Vertex to the target Vertex. The target Vertex may be the same as the
 * source Vertex if a self-edge is being created.
 * <p>
 * Edge's are considered to be directed in that they go from source to target. In an undirected graph, another
 * Edge should be added going from target to source to make it undirected.
 * <p>
 * The label of an Edge can be used simply as a label. If a label is not provided then a default one of the form
 * source.id-target.id-randomNumber is used.
 * <p>
 * The weight of an Edge is optional and may be null.
 * <p>
 * Two Edges are considered equal if they have the same source and target Vertices.
 * <p>
 * If the Edge contains a weight value then Comparable.compareTo can be used.
 *
 * @param <T> The type of the value that the Vertices contain.
 * @param <E> The type of the weight for this Edge.
 */
public class Edge<T extends Comparable<T>, E extends Comparable<E>> implements Comparable<Edge<T, E>> {
    private final Vertex<T, E> source;
    private final Vertex<T, E> target;
    private final String label;
    private E weight;

    /**
     * Instantiates a new Edge with the specified source, target, label, and weight.
     *
     * @param source The source vertex that this Edge is coming from.
     * @param target The target vertex that this Edge is going to.
     * @param label  The label of this Edge; it is not optional and should be non-null.
     * @param weight The weight of this Edge, which may be null.
     */
    public Edge(@NotNull Vertex<T, E> source, @NotNull Vertex<T, E> target, @NotNull String label, E weight) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.label = Objects.requireNonNull(label);
        this.weight = weight;
    }

    /**
     * Instantiates a new Edge with the specified source, target, a label in the form of
     * source.id-target.id-randomNumber and no weight.
     *
     * @param source The source vertex that this Edge is coming from.
     * @param target The target vertex that this Edge is going to.
     */
    public Edge(@NotNull Vertex<T, E> source, @NotNull Vertex<T, E> target) {
        this(source, target, source.getId() + "-" + target.getId() + "-" +
                (int) (Math.random() * 100_000) + 1, null);
    }

    /**
     * @return The source Vertex of this Edge.
     */
    @NotNull
    public Vertex<T, E> getSource() {
        return source;
    }

    /**
     * @return The target Vertex of this Edge.
     */
    @NotNull
    public Vertex<T, E> getTarget() {
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
     * Returns true if the specified target Vertex is the target of this Edge.
     *
     * @param target The Vertex to test against.
     * @return True if the specified target Vertex is the target of this Edge.
     */
    public boolean hasTarget(@NotNull Vertex<T, E> target) {
        return this.target.equals(target);
    }

    /**
     * Returns true if the specified source Vertex is the source of this Edge.
     *
     * @param source The Vertex to test against.
     * @return True if the specified source Vertex is the source of this Edge.
     */
    public boolean hasSource(@NotNull Vertex<T, E> source) {
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
     * Two Edges are equal if they have the same source and target Vertices.
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

    @Override
    public int compareTo(@NotNull Edge<T, E> o) {
        if (weight == null && o.weight == null) {
            return 0;
        } else if (weight != null && o.weight != null) {
            return weight.compareTo(o.weight);
        } else if (weight != null) {
            return -1;
        } else {
            return 1;
        }
    }
}
