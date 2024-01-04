package com.capital7software.aoc2015.lib.graph.cycle;

import org.jetbrains.annotations.NotNull;

/**
 * Functional interface for detecting cycles for a collection of Nodes based on their IDs.
 */
public interface CycleDetector {
    /**
     * Returns true if a cycle would be formed by adding an Edge from the source Vertex to the target Vertex.
     * How this is done is implementation specific.
     *
     * @param source The ID of the source Vertex.
     * @param target The ID of the target Vertex.
     * @return True if a cycle would be formed by adding an Edge from the source Vertex to the target Vertex.
     */
    boolean detect(@NotNull String source, @NotNull String target);
}
