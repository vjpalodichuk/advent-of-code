package com.capital7software.aoc2015.lib.graph.path;

import com.capital7software.aoc2015.lib.graph.Graph;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.function.Function;

abstract public class AbstractHamiltonianPathFinder<T extends Comparable<T>, E extends Comparable<E>> implements PathFinder<PathFinderResult<T, E>, T, E> {
    @Override
    public void find(
            @NotNull Graph<T, E> graph,
            @NotNull Properties properties,
            @NotNull Function<PathFinderResult<T, E>, Boolean> valid,
            Function<PathFinderResult<T, E>, Boolean> invalid
    ) {
        if (!validateProperties(properties)) {
            throw new IllegalArgumentException("The provided Properties are invalid: " + properties);
        }

        var vertexMap = graph.getVertexMap();
    }

    /**
     * If the properties are invalid false should be returned to cause an IllegalArgumentException to be thrown.
     *
     * @param properties The properties to validate.
     * @return True if the properties are valid, false if there is something wrong with them.
     */
    protected boolean validateProperties(@NotNull Properties properties) {
        return true;
    }
}
