package com.capital7software.aoc.lib.graph.cycle;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Uses Path Compression and Union Rank algorithms to quickly determine if a cycle would be formed
 * between two Nodes.
 * <p>
 * When this detector is created a map between the supplied nodeIds and their associated DisjointSet instance is
 * created. Each instance of DisjointSet is initialized with the parent set to the corresponding nodeId. When
 * detect is called, the parents of the two nodes are found. If they have the same parent then true is returned as
 * a cycle would be formed. If they have two different parents, then the two are merged in to the same set and if
 * they had the same rank, the rank of the new set is increased.
 * <p>
 *
 * Inspired by the Path Compression and Union Rank algorithms used in Kruskal's MST algorithm found at:
 * <a href="https://github.com/eugenp/tutorials/tree/master/algorithms-modules/algorithms-miscellaneous-6">...</a>
 */
public class CycleDetectorDisjointSet implements CycleDetector {
    private final Map<String, DisjointSet> nodeIds;

    /**
     *  Instantiates a new CycleDetectorDisjointSet for the specified Collection of nodeIds.
     *
     * @param nodeIds The Collection of nodeIds that are in the Graph we are doing cycle detection for.
     */
    public CycleDetectorDisjointSet(@NotNull Collection<String> nodeIds) {
        this.nodeIds = init(Objects.requireNonNull(nodeIds));
    }

    /**
     * Creates a Map between the nodeIds and their DisjointSet instance. The DisjointSet for each nodeId is
     * initialized with the parent set to the nodeId that instance is associated with and a default rank of 1.
     *
     * @param nodeIds THe Collection of nodeIds that are in the Graph we are doing cycle detection for.
     * @return A Map between the nodeIds and their DisjointSet instance.
     */
    @NotNull
    private Map<String, DisjointSet> init(@NotNull Collection<String> nodeIds) {
        return nodeIds.stream()
                .map(DisjointSet::new)
                .collect(Collectors.toMap(DisjointSet::getParent, Function.identity()));
    }

    /**
     * Uses Path Compression for finding and updating the parents for the Nodes when searching for them. Uses
     * Union Rank when determining which set to merge into the other.
     * <p>
     * When detect is called, the parents of both node IDs are updated while being retrieved. Path Compression
     * is used during the find operation. This is done  If they already have the same parent,
     * then a cycle would be formed if an edge would be added between them. If they have different parents, the
     * two sets are merged using a Union Rank algorithm. The root parent with the higher rank will be the parent
     * of the merged set. The merged set's rank is only increased if both nodes were at the same rank prior to
     * the merge operation.
     * <p>
     * The running time using both Path Compression and Union Rank is
     * O(α(V)), where α(V) is the inverse Ackermann function of the total number of nodes.
     *
     * @param source The ID of the source Vertex.
     * @param target The ID of the target Vertex.
     * @return True if a cycle would be formed by adding an Edge from the source Vertex to the target Vertex.
     */
    @Override
    public boolean detect(@NotNull String source, @NotNull String target) {
        var sourceRoot = findWithCompression(source);
        var targetRoot = findWithCompression(target);

        if (sourceRoot.equals(targetRoot)) {
            return true; // A cycle would be formed.
        }

        // Merge the two sets!
        unionRank(sourceRoot, targetRoot);

        return false; // No cycle!
    }

    /**
     * If the parent is equal to the nodeId, we immediately return it. If not, then we search
     * for the parent root node and once found, update teh DisjointSet's parent with the new
     * parent root node.
     *
     * @param nodeId The nodeId of the Vertex we are searching for the root parent of.
     * @return The root parent node for the specified nodeId.
     */
    @NotNull
    private String findWithCompression(@NotNull String nodeId) {
        var disjointSet = nodeIds.get(nodeId);
        var parentId = disjointSet.getParent();
        if (parentId.equals(nodeId)) {
            return nodeId;
        } else {
            var parentId2 = find(parentId);
            disjointSet.setParent(parentId2); // Compress the path for future searches!
            return parentId2;
        }
    }

    /**
     *
     * @param nodeId The nodeId we are searching to find the root parent of.
     *
     * @return The nodeId of the root parent node.
     */
    @NotNull
    private String find(@NotNull String nodeId) {
        var parentId = nodeIds.get(nodeId).getParent();

        if (parentId.equals(nodeId)) {
            return nodeId; // The root parent node is the one where the nodeId equals the parentId.
        } else {
            return find(parentId);
        }
    }

    /**
     * Sets the parent of the source nodeId to be the targetId's parent.
     *
     * @param source THe nodeId of the source Vertex.
     * @param target The nodeId of the target Vertex.
     */
    @Deprecated
    @SuppressWarnings("unused")
    private void union(@NotNull String source, @NotNull String target) {
        var disjointSet = nodeIds.get(source);
        disjointSet.setParent(target);
    }

    /**
     * Compares the ranks of the two nodeIds and mergers the lower ranked set into the
     * higher ranked set. It does this by setting the parent of the lower ranked set to
     * be the nodeId of the higher ranked set.
     * <p>
     * After merging if the ranks of the two sets prior to the merge were equal,
     * then the rank of the merged set is increased by 1.
     *
     * @param source The nodeId of the source Vertex.
     * @param target The nodeId of the target Vertex.
     */
    private void unionRank(@NotNull String source, @NotNull String target) {
        var sourceSet = nodeIds.get(source);
        var targetSet = nodeIds.get(target);

        var sourceRank = sourceSet.getRank();
        var targetRank = targetSet.getRank();

        if (sourceRank < targetRank) {
            // Merge the lower ranked set into the higher ranked set.
            sourceSet.setParent(target);
        } else {
            // Merge the lower ranked set into the higher ranked set.
            targetSet.setParent(source);
            if (sourceRank == targetRank) {
                // Increase the rank of the new set
                sourceSet.setRank(sourceRank + 1);
            }
        }
    }
}
