package com.capital7software.aoc.lib.graph.cycle;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * The DisjointSet is used by Graph algorithms for quickly determining which edges and
 * nodes to include.
 *
 * <p><br>
 * One example of its use is in detecting cycles of a Graph as in Kruskal's algorithm for building
 * a Minimum Spanning Tree of a Graph.
 *
 * <p><br>
 * The parent is the ID of the Vertex that is the parent of this DisjointSet. Typically, this is
 * initially the ID of the Vertex itself.
 *
 * <p><br>
 * The rank is used to speed up the process of merging two DisjointSets as it represents the depth
 * of the tree and membership in a set.
 *
 * <p><br>
 * Each Vertex in the Graph should have an associated instance of DisjointSet! Typically, a Map
 * is used to associate a Vertex with its DisjointSet instance.
 *
 * <p><br>
 * Inspired by the DisjointSetInfo class used in Kruskal's MST algorithm found at:
 * <a href="https://github.com/eugenp/tutorials/tree/master/algorithms-modules/algorithms-miscellaneous-6">...</a>
 */
@Setter
public class DisjointSet {
  /**
   * -- SETTER --
   * Sets the parent of this instance to the specified value.
   */
  private String parent;
  /**
   * -- SETTER --
   * Sets the Rank of this DisjointSet to the specified Rank.
   * -- GETTER --
   * Returns the rank of this DisjointSet.
   */
  @Getter
  private int rank;

  /**
   * Instantiates a new DisjointSet with the specified parent and a rank of 1.
   *
   * @param parent The parent node of this DisjointSet.
   */
  public DisjointSet(@NotNull String parent) {
    setParent(parent);
    setRank(1);
  }

  /**
   * Returns the parent of this DisjointSet.
   *
   * @return The parent of this DisjointSet.
   */
  @NotNull
  public String getParent() {
    return parent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DisjointSet that)) {
      return false;
    }
    return getRank() == that.getRank() && getParent().equals(that.getParent());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getParent(), getRank());
  }

  @Override
  public String toString() {
    return "DisjointSet{" + "parent=" + parent + ", rank=" + rank + '}';
  }
}
