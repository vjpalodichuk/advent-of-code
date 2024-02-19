package com.capital7software.aoc.lib.graph;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * An Edge in a Graph connects the source Vertex to the target Vertex. The target Vertex may be
 * the same as the source Vertex if a self-edge is being created.
 *
 * <p><br>
 * Edge's are considered to be directed in that they go from source to target. In an undirected
 * graph, another Edge should be added going from target to source to make it undirected.
 *
 * <p><br>
 * The label of an Edge can be used simply as a label. If a label is not provided then a default
 * one of the form source.id-target.id-randomNumber is used.
 *
 * <p><br>
 * The weight of an Edge is optional and may be null.
 *
 * <p><br>
 * Two Edges are considered equal if they have the same source and target Vertices.
 *
 * <p><br>
 * If the Edge contains a weight value then Comparable.compareTo can be used.
 *
 * @param <E> The type of the weight for this Edge.
 */
public class Edge<E extends Comparable<E>> implements Comparable<Edge<E>> {
  private static final Random LABEL_GENERATOR = new Random(System.nanoTime());
  private final String source;
  private final String target;
  private final String label;
  /**
   * -- SETTER --
   * Sets the weight of this Edge to the specified weight.
   */
  @Setter
  private E weight;

  /**
   * Instantiates a new Edge with the specified source, target, label, and weight.
   *
   * @param source The source vertex that this Edge is coming from.
   * @param target The target vertex that this Edge is going to.
   * @param label  The label of this Edge; it is not optional and should be non-null.
   * @param weight The weight of this Edge, which may be null.
   */
  public Edge(@NotNull String source, @NotNull String target, @NotNull String label, E weight) {
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
  public Edge(@NotNull String source, @NotNull String target) {
    this(source, target, source + "-" + target + "-"
        + LABEL_GENERATOR.nextInt(100_001), null);
  }

  /**
   * Returns the source Vertex ID of this Edge.
   *
   * @return The source Vertex ID of this Edge.
   */
  @NotNull
  public String getSource() {
    return source;
  }

  /**
   * Returns the target Vertex ID of this Edge.
   *
   * @return The target Vertex ID of this Edge.
   */
  @NotNull
  public String getTarget() {
    return target;
  }

  /**
   * Returns the label of this Edge.
   *
   * @return The label of this Edge.
   */
  @NotNull
  public String getLabel() {
    return label;
  }

  /**
   * Returns an Optional of the weight of this Edge.
   *
   * @return An Optional of the weight of this Edge.
   */
  @NotNull
  public Optional<E> getWeight() {
    return Optional.ofNullable(get());
  }

  /**
   * Returns the weight of this Edge.
   *
   * @return The weight of this Edge.
   */
  public E get() {
    return weight;
  }

  /**
   * Returns the weight of this Edge as a Double. If this Edge has no weight, 1.0 is returned.
   *
   * @return The weight of this Edge as a Double. If this Edge has no weight, 1.0 is returned.
   */
  public Double asDouble() {
    if (weight instanceof Double w) {
      return w;
    } else if (weight instanceof Number n) {
      return n.doubleValue();
    } else {
      return 1.0;
    }
  }

  /**
   * Returns true if the specified target Vertex is the target of this Edge.
   *
   * @param target The Vertex to test against.
   * @return True if the specified target Vertex is the target of this Edge.
   */
  public boolean hasTarget(@NotNull String target) {
    return this.target.equals(target);
  }

  /**
   * Returns true if the specified source Vertex is the source of this Edge.
   *
   * @param source The Vertex to test against.
   * @return True if the specified source Vertex is the source of this Edge.
   */
  public boolean hasSource(@NotNull String source) {
    return this.source.equals(source);
  }

  /**
   * Returns true if the specified label is the label of this Edge.
   *
   * @param label The label to test against.
   * @return True if the specified label is the label of this Edge.
   */
  public boolean hasLabel(@NotNull String label) {
    return this.label.equals(label);
  }

  /**
   * Returns a new Edge instance that is an independent copy of this Edge.
   *
   * @return A new Edge instance that is an independent copy of this Edge.
   */
  public Edge<E> copy() {
    return new Edge<>(source, target, label, weight);
  }

  /**
   * Two Edges are equal if they have the same source and target Vertices.
   *
   * @param o The other Edge to compare against.
   * @return True if the two Edges are considered to be equal.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Edge<?> edge)) {
      return false;
    }
    return getSource().equals(edge.getSource())
        && getTarget().equals(edge.getTarget());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSource(), getTarget());
  }

  @Override
  public String toString() {
    return "Edge{" + "source=" + source + ", target=" + target + ", label=" + label
        + ", weight=" + weight + '}';
  }

  @Override
  public int compareTo(@NotNull Edge<E> o) {
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
