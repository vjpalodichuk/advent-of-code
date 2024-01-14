package com.capital7software.aoc.lib.graph.cycle;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The DisjointSet is used by Graph algorithms for quickly determining which edges and nodes to include.
 * <p>
 * One capital7software of its use is in detecting cycles of a Graph as in Kruskal's algorithm for building
 * a Minimum Spanning Tree of a Graph.
 * <p>
 * The parent is the ID of the Vertex that is the parent of this DisjointSet. Typically, this is initially
 * the ID of the Vertex itself.
 * <p>
 * The rank is used to speed up the process of merging two DisjointSets as it represents the depth of the tree
 * and membership in a set.
 * <p>
 * Each Vertex in the Graph should have an associated instance of DisjointSet! Typically, a Map is used to
 * associate a Vertex with its DisjointSet instance.
 * <p>
 * Inspired by the DisjointSetInfo class used in Kruskal's MST algorithm found at:
 * <a href="https://github.com/eugenp/tutorials/tree/master/algorithms-modules/algorithms-miscellaneous-6">...</a>
 *
 */
public class DisjointSet {
    private String parent;
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
     *
     * @return The parent of this DisjointSet.
     */
    @NotNull
    public String getParent() {
        return parent;
    }

    /**
     *
     * @param parent The new value to set as the parent of this DisjointSet.
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     *
     * @return The rank of this DisjointSet.
     */
    public int getRank() {
        return rank;
    }

    /**
     *
     * @param rank The new rank of this DisjointSet.
     */
    public void setRank(int rank) {
        this.rank = rank;
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
        return "DisjointSet{" +
                "parent=" + parent +
                ", rank=" + rank +
                '}';
    }
}
