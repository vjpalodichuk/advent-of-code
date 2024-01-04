package com.capital7software.aoc2015.lib.graph.path;

import com.capital7software.aoc2015.lib.graph.Edge;
import com.capital7software.aoc2015.lib.graph.Graph;
import com.capital7software.aoc2015.lib.graph.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * A Hamiltonian Path is a path in a Graph where every Vertex is visited exactly once!
 * A Hamiltonian Cycle is a Hamiltonian Path where the last Vertex in the path has an Edge that
 * connects back to the first Vertex in the path.
 * <p>
 * This implementation is capable of finding both Hamiltonian Paths and Hamiltonian Cycles.
 * <p>
 * This class has two Properties that can be set: Props.DETECT_CYCLES and Props.STARTING_VERTICES.
 * <p>
 * DETECT_CYCLES: Set to Boolean.TRUE if Hamiltonian Cycles instead of Paths should be built.
 * <p>
 * STARTING_VERTICES: Accepts a List of Vertices to build paths for. Please note that the
 * specified vertices must exist in the Graph specified in the find call.
 *
 * @param <T> The type of the value held by Nodes in the graph.
 * @param <E> The type of the weight held by Edges in the graph.
 */
public class HamiltonianPathFinder<T extends Comparable<T>, E extends Comparable<E>>
        implements PathFinder<PathFinderResult<T, E>, T, E> {
    public enum Props {
        DETECT_CYCLES,
        STARTING_VERTICES
    }

    @Override
    public void find(
            @NotNull Graph<T, E> graph,
            @NotNull Properties properties,
            @NotNull Function<PathFinderResult<T, E>, PathFinderStatus> valid,
            Function<PathFinderResult<T, E>, PathFinderStatus> invalid
    ) {
        if (!validateProperties(properties)) {
            throw new IllegalArgumentException("The provided Properties are invalid: " + properties);
        }

        var vertexMap = graph.getVertexMap();

        // Paths will be investigated for these nodes.
        var startingVertices = getStartingVertices(vertexMap, properties);

        // Does a cycle need to be created?
        var cycleRequired = isCycleRequired(properties);
        var pathId = new AtomicInteger(0);
        var size = vertexMap.values().size();

        for(var vertex : startingVertices) {
            var vertices = new ArrayList<>(vertexMap.values().stream().filter(it -> !it.getId().equals(vertex.getId())).toList());
            var pathSoFar = new ArrayList<Vertex<T, E>>(size);
            var edgesSoFar = new ArrayList<Edge<T, E>>(size - 1);
            var idsSoFar = new HashSet<String>(size);

            pathSoFar.add(vertex); // Add the starting Vertex to the path.
            idsSoFar.add(vertex.getId());

            if (findPath(
                    vertices,
                    size,
                    pathId,
                    cycleRequired,
                    pathSoFar,
                    edgesSoFar,
                    idsSoFar,
                    valid,
                    invalid
            ) == PathFinderStatus.FINISHED) {
                break;
            }
        }
    }

    /**
     * If the properties are invalid false should be returned to cause an IllegalArgumentException to be thrown.
     * <p>
     * Implementations that use Properties should override this method to ensure the Properties are
     * properly validated.
     *
     * @param properties The properties to validate.
     * @return True if the properties are valid, false if there is something wrong with them.
     */
    protected boolean validateProperties(@NotNull Properties properties) {
        if (properties.contains(Props.DETECT_CYCLES)) {
            if (!(properties.get(Props.DETECT_CYCLES) instanceof Boolean)) {
                return false;
            }
        }
        if (properties.contains(Props.STARTING_VERTICES)) {
            if ((properties.get(Props.STARTING_VERTICES)) instanceof List<?> list) {
                return !list.isEmpty() && list.get(0) instanceof Vertex<?, ?>;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * If this method returns True then a path is only valid if there is an Edge that leads from the end Vertex
     * in the path back to the start Vertex.
     * <p>
     * Implementations that require a cycle for a path to be valid should override this method and have it
     * return true.
     * <p>
     * Properties are provided should the implementation elect to allow the caller to specify if a cycle should
     * be required.
     * <p>
     * This implementation will check for the Props.DETECT_CYCLES property and use it if present;
     * otherwise this method returns false.
     *
     * @param properties The properties to validate.
     * @return True if cycles should be detected; otherwise false.
     */
    protected boolean isCycleRequired(Properties properties) {
        if (properties.get(Props.DETECT_CYCLES) instanceof Boolean cycle) {
            return cycle;
        }
        return false;
    }

    /**
     * Returns the List of Vertices to build paths for. This default implementation simply returns a List
     * of all the Vertices in the Graph.
     * <p>
     * Implementations should override this method if they support having
     * the caller specify the Vertices to build paths for via a Properties instance.
     *
     * @param vertexMap  The Map that contains all the vertices in the Graph.
     * @param properties The Properties for this invocation.
     * @return The List of starting Vertices.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    protected List<Vertex<T, E>> getStartingVertices(
            @NotNull Map<String, Vertex<T, E>> vertexMap,
            @NotNull Properties properties
    ) {
        if (properties.get(Props.STARTING_VERTICES) instanceof List<?> list) {
            List<Vertex<T, E>> results = new ArrayList<>(list.size());

            for (var vertex : list) {
                results.add((Vertex<T, E>) vertex);
            }
            return results;
        }
        return new ArrayList<>(vertexMap.values());
    }

    @NotNull
    private PathFinderStatus findPath(
            List<Vertex<T, E>> vertices,
            int requiredCount,
            AtomicInteger pathId,
            boolean cycleRequired,
            List<Vertex<T, E>> path,
            List<Edge<T, E>> edges,
            Set<String> visitedIds,
            @NotNull Function<PathFinderResult<T, E>, PathFinderStatus> validCallback,
            Function<PathFinderResult<T, E>, PathFinderStatus> invalidCallback) {
        // Check the stopping conditions.
        if (path.size() == requiredCount) {
            if (cycleRequired) {
                // Validate we can get from the last vertex back to the starting vertex!
                if (path.get(0).getEdge(path.get(path.size() - 1).getId()).isPresent()) {
                    return validCallback.apply(buildPathResult(path, edges, pathId.getAndIncrement()));
                } else if (invalidCallback != null) {
                    return invalidCallback.apply(buildPathResult(path, edges, pathId.getAndIncrement()));
                } else {
                    return PathFinderStatus.CONTINUE;
                }
            } else {
                return validCallback.apply(buildPathResult(path, edges, pathId.getAndIncrement()));
            }
        }

        PathFinderStatus lastStatus = null;

        // Go through all the vertices
        for (var vertex : vertices) {
             if (!visitedIds.contains(vertex.getId()) &&
                     path.get(path.size() - 1).getEdge(vertex.getId()).isPresent()) {
                 // Add the vertex and edge to the path and mark the vertex as visited.
                 edges.add(path.get(path.size() - 1).getEdge(vertex.getId()).get());
                 visitedIds.add(vertex.getId());
                 path.add(vertex);

                 var status = findPath(
                         vertices,
                         requiredCount,
                         pathId,
                         cycleRequired,
                         path,
                         edges,
                         visitedIds,
                         validCallback,
                         invalidCallback
                 );
                 lastStatus = status;

                 // Clean-up
                 path.remove(path.size() - 1);
                 visitedIds.remove(vertex.getId());
                 edges.remove(edges.size() - 1);

                 if (status != PathFinderStatus.CONTINUE) {
                     return status;
                 }
             }
        }

        if (lastStatus == null) {
            // Failed to find any vertices to add to the path!
            if (invalidCallback != null) {
                invalidCallback.apply(buildPathResult(path, edges, pathId.get()));
            }
            return PathFinderStatus.CONTINUE;
        }
        return lastStatus;
    }

    @NotNull
    private PathFinderResult<T, E> buildPathResult(
            @NotNull List<Vertex<T, E>> path,
            @NotNull List<Edge<T, E>> edges,
            int pathId
    ) {
        var start = path.get(0);
        var end = path.size() > 1 ? path.get(path.size() - 1) : path.get(0);

        return new PathFinderResult<>(pathId, start, end, new ArrayList<>(edges));
    }
}
