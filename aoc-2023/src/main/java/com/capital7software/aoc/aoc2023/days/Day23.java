package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import com.capital7software.aoc.lib.grid.Grid2D;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Day23 {
    private static final Logger LOGGER = Logger.getLogger(Day23.class.getName());

    /**
     * Instantiates this Solution instance.
     */
    public Day23() {

    }


    public enum TrailType {
        PATH('.', true, false) {
            @Override
            public Set<Direction> walkableDirections(boolean ignoreSlop) {
                // A path may go in any direction
                return ALL;
            }

            @Override
            public boolean canWalkInDirection(Direction direction, boolean ignoreSlopes) {
                return true;
            }
        },
        FOREST('#', false, false),
        SLOPE_NORTH('^', true, true) {
            @Override
            public Set<Direction> walkableDirections(boolean ignoreSlop) {
                if (ignoreSlop) {
                    return ALL;
                }

                return NORTH;
            }

            @Override
            public boolean canWalkInDirection(Direction direction, boolean ignoreSlopes) {
                return direction == Direction.NORTH || ignoreSlopes;
            }
        },
        SLOPE_EAST('>', true, true) {
            @Override
            public Set<Direction> walkableDirections(boolean ignoreSlop) {
                if (ignoreSlop) {
                    return ALL;
                }

                return EAST;
            }

            @Override
            public boolean canWalkInDirection(Direction direction, boolean ignoreSlopes) {
                return direction == Direction.EAST || ignoreSlopes;
            }
        },
        SLOPE_SOUTH('v', true, true) {
            @Override
            public Set<Direction> walkableDirections(boolean ignoreSlop) {
                if (ignoreSlop) {
                    return ALL;
                }

                return SOUTH;
            }

            @Override
            public boolean canWalkInDirection(Direction direction, boolean ignoreSlopes) {
                return direction == Direction.SOUTH || ignoreSlopes;
            }
        },
        SLOPE_WEST('<', true, true) {
            @Override
            public Set<Direction> walkableDirections(boolean ignoreSlop) {
                if (ignoreSlop) {
                    return ALL;
                }

                return WEST;
            }

            @Override
            public boolean canWalkInDirection(Direction direction, boolean ignoreSlopes) {
                return direction == Direction.WEST || ignoreSlopes;
            }
        };

        private static final Set<Direction> ALL = Set.of(Direction.values());
        private static final Set<Direction> NORTH = Set.of(Direction.NORTH);
        private static final Set<Direction> SOUTH = Set.of(Direction.SOUTH);
        private static final Set<Direction> EAST = Set.of(Direction.EAST);
        private static final Set<Direction> WEST = Set.of(Direction.WEST);
        private static final Set<Direction> NONE = Collections.emptySet();

        private final char label;
        private final boolean walkable;
        private final boolean slope;

        TrailType(char label, boolean walkable, boolean slope) {
            this.label = label;
            this.walkable = walkable;
            this.slope = slope;
        }

        public char getLabel() {
            return label;
        }

        public boolean isWalkable() {
            return walkable;
        }

        public boolean isSlope() {
            return slope;
        }

        public Set<Direction> walkableDirections(boolean ignoreSlopes) {
            return NONE;
        }

        public boolean canWalkInDirection(Direction direction, boolean ignoreSlopes) {
            return false;
        }

        public static TrailType from(char label) {
            for (var value : values()) {
                if (value.label == label) {
                    return value;
                }
            }

            return null;
        }
    }

    private record TrailTile(TrailType trailType, Point2D<Integer> point) {
        public boolean isWalkable() {
            return trailType().isWalkable();
        }

        public boolean isSlope() {
            return trailType().isSlope();
        }

        public Set<Direction> walkableDirections(boolean ignoreSlopes) {
            return trailType().walkableDirections(ignoreSlopes);
        }

        public boolean canWalkInDirection(Direction direction, boolean ignoreSlopes) {
            return trailType().canWalkInDirection(direction, ignoreSlopes);
        }

        public Point2D<Integer> pointInDirection(Direction direction) {
            return Grid2D.pointInDirection(point, direction);
        }
    }

    public record TrailEdge(TrailTile tile, int weight, boolean oneway) {
    }

    public record TrailNode(TrailTile tile, Map<Direction, TrailEdge> edges) {
        public boolean add(Direction direction, TrailEdge tile) {
            if (edges.containsKey(direction)) {
                return false;
            }

            return edges.put(direction, tile) == null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TrailNode trailNode)) {
                return false;
            }
            return tile.equals(trailNode.tile);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tile);
        }
    }

    public static class HikingSegment {
        private final long id;
        private TrailNode first;
        private TrailNode second;
        private final List<TrailEdge> edges;
        private final Set<TrailNode> nodes;
        private long length;
        private boolean deadEnd;
        private boolean cycle;
        private boolean finish;
        private boolean oneway;

        public HikingSegment(long id, TrailNode first) {
            this.id = id;
            this.first = first;
            this.second = null;
            this.edges = new ArrayList<>();
            this.nodes = new HashSet<>();
            add(first);
            this.length = -1;
            this.deadEnd = false;
            this.cycle = false;
            this.finish = false;
            this.oneway = first.tile().isSlope();
        }

        public boolean add(TrailEdge edge) {
            return edges.add(edge);
        }

        public boolean add(TrailNode node) {
            return nodes.add(node);
        }

        public TrailNode getFirst() {
            return first;
        }

        public void setFirst(TrailNode first) {
            this.first = first;
        }

        public TrailNode getSecond() {
            return second;
        }

        public void setSecond(TrailNode second) {
            this.second = second;
        }

        public long getLength() {
            return length;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public boolean isDeadEnd() {
            return deadEnd;
        }

        public void setDeadEnd(boolean deadEnd) {
            this.deadEnd = deadEnd;
        }

        public boolean isCycle() {
            return cycle;
        }

        public void setCycle(boolean cycle) {
            this.cycle = cycle;
        }

        public boolean isFinish() {
            return finish;
        }

        public void setFinish(boolean finish) {
            this.finish = finish;
        }

        public boolean isOneway() {
            return oneway;
        }

        public void setOneway(boolean oneway) {
            this.oneway = oneway;
        }

        public boolean contains(TrailNode trailNode) {
            return nodes.contains(trailNode);
        }

        public boolean canWalkThroughSegment(HikingSegment other, boolean ignoreSlopes) {
            if (!other.contains(getFirst()) && !other.contains(getSecond())) {
                // Disjointed so no we cannot walk through the other segment
                return false;
            }
            if (ignoreSlopes) {
                // We don't care about one-ways!
                return true;
            }
            // We care about one-ways. We need to make sure that we can appropriately walk through the other
            // segment. If from is a oneway, that means it's second must match the first of the other segment
            if (isOneway()) {
                if (!other.isOneway()) {
                    return true; // One-way on to a two-way is always allowed
                }
                return getSecond().equals(other.getFirst());
            }
            return true;
        }

        @Override
        public String toString() {
            return "HikingSegment{" +
                    "id=" + id +
                    ", length=" + length +
                    ", deadEnd=" + deadEnd +
                    ", cycle=" + cycle +
                    ", finish=" + finish +
                    ", oneway=" + oneway +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof HikingSegment that)) {
                return false;
            }
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    public static class HikingTrail {
        private final int id;
        private boolean complete;
        private boolean leadsToExit;
        private long length;
        private TrailNode head;
        private TrailNode tail;
        private final Set<HikingSegment> pathSet;
        private final List<HikingSegment> pathSegments;
        private final Map<TrailNode, Integer> headsAndTails;

        public HikingTrail(int id) {
            this(id, null);
        }

        public HikingTrail(int id, HikingSegment initial) {
            this.id = id;
            this.head = initial == null ? null : initial.getFirst();
            this.complete = initial != null && initial.isFinish();
            this.leadsToExit = initial != null && initial.isFinish();
            this.length = initial == null ? 0 : initial.getLength();
            this.tail = initial == null ? null : initial.getSecond();
            this.pathSegments = new LinkedList<>();
            this.pathSet = new HashSet<>();
            this.headsAndTails = new HashMap<>();

            if (initial != null) {
                pathSet.add(initial);
                pathSegments.add(initial);
                headsAndTails.put(initial.getFirst(), 1);
                headsAndTails.put(initial.getSecond(), 1);
            }
        }

        public int getId() {
            return id;
        }

        public boolean isLeadsToExit() {
            return leadsToExit;
        }

        public long getLength() {
            return length;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public boolean contains(HikingSegment segment) {
            return pathSet.contains(segment);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof HikingTrail that)) {
                return false;
            }
            return pathSet.equals(that.pathSet);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pathSet);
        }

        @Override
        public String toString() {
            return "HikingTrail{" +
                    "id=" + id +
                    ", complete=" + complete +
                    ", leadsToExit=" + leadsToExit +
                    ", length=" + length +
                    '}';
        }

        public HikingTrail copy(int newId) {
            var newTrail = new HikingTrail(newId);

            newTrail.pathSet.addAll(pathSet);
            newTrail.pathSegments.addAll(pathSegments);
            newTrail.headsAndTails.putAll(headsAndTails);
            newTrail.head = head;
            newTrail.tail = tail;
            newTrail.length = length;
            newTrail.complete = complete;
            newTrail.leadsToExit = leadsToExit;

            return newTrail;
        }

        public boolean add(HikingSegment segment) {
            int headCount = headsAndTails.computeIfAbsent(segment.getFirst(), it -> 0);
            int tailCount = headsAndTails.computeIfAbsent(segment.getSecond(), it -> 0);

            if (headCount > 1 || tailCount > 1) {
                return false;
            }

            pathSegments.add(segment);
            pathSet.add(segment);
            headsAndTails.put(segment.getFirst(), headCount + 1);
            headsAndTails.put(segment.getSecond(), tailCount + 1);

            length += segment.getLength();

            if (head == null) {
                head = segment.getFirst();
            }

            tail = segment.getSecond();

            if (segment.isFinish()) {
                leadsToExit = true;
                complete = true;
            }

            return true;
        }

        public void remove(HikingSegment toRemove) {
            if (pathSet.remove(toRemove)) {
                pathSegments.remove(toRemove);
                length -= toRemove.getLength();
                var headCount = headsAndTails.get(toRemove.getFirst()) - 1;
                var tailCount = headsAndTails.get(toRemove.getSecond()) - 1;
                headsAndTails.put(toRemove.getFirst(), headCount);
                headsAndTails.put(toRemove.getSecond(), tailCount);

                if (pathSegments.isEmpty()) {
                    head = null;
                    tail = null;
                } else {
                    tail = pathSegments.getLast().getSecond();

                    if (toRemove.isFinish()) {
                        leadsToExit = false;
                        complete = false;
                    }
                }
            }
        }
    }

    private record RowResults(List<TrailTile> tiles, Point2D<Integer> initialPosition) {
    }

    private record HikingTrails(
            Grid2D<TrailTile> grid,
            Map<TrailTile, TrailNode> nodeMap,
            List<TrailNode> trailNodes,
            Map<TrailNode, Set<HikingSegment>> segmentMap,
            List<HikingSegment> trailSegments,
            Set<HikingSegment> deadEnds,
            List<HikingTrail> trails,
            TrailTile start,
            TrailTile finish,
            boolean ignoreSlopes,
            boolean virtualGrid
    ) {
        private void buildTrailGraph() {
            nodeMap.clear();
            trailNodes.clear();

            var graph = new ArrayList<TrailNode>(grid.size());

            var queue = new LinkedList<TrailTile>();

            queue.add(start);

            var visited = new HashSet<TrailTile>();

            while (!queue.isEmpty()) {
                var tile = queue.poll();
                var node = nodeMap.computeIfAbsent(tile, it -> new TrailNode(it, new HashMap<>()));

                if (visited.contains(tile)) {
                    continue; // We have already explored this tile!
                }

                visited.add(tile);
                graph.add(node);

                var directions = tile.walkableDirections(ignoreSlopes);

                for (var direction : directions) {
                    var point = pointInDirection(tile, direction);
                    if (isOnGrid(point)) {
                        var nodeEdge = getEdgeFromHere(node, direction);
                        if (nodeEdge != null) {
                            node.add(direction, nodeEdge);
                            if (!visited.contains(nodeEdge.tile)) {
                                queue.offer(nodeEdge.tile);
                            }
                        }
                    }
                }
            }

            trailNodes.addAll(graph);

        }

        private TrailEdge getEdgeFromHere(TrailNode node, Direction direction) {
            var point = pointInDirection(node.tile(), direction);
            var tile = get(point);

            if (tile == null || !tile.isWalkable()) {
                return null;
            }

            var queue = new LinkedList<TrailTile>();

            queue.offer(node.tile());

            var count = 0;
            TrailTile lastTile = null;
            boolean oneway = false;

            while (!queue.isEmpty()) {
                lastTile = queue.poll();
                count++;

                point = pointInDirection(lastTile, direction);

                if (isOnGrid(point)) {
                    var newTile = get(point);
                    if (newTile != null && newTile.isWalkable()) {
                        lastTile = newTile;
                        if (!ignoreSlopes && lastTile.trailType.isSlope()) {
                            oneway = true;
                        }
                        if (isIntersection(newTile)) {
                            break; // We are done as we have hit an intersection!
                        }
                        queue.offer(newTile);
                    }
                }
            }

            if (count == 0) {
                return null;
            }


            return new TrailEdge(lastTile, count, oneway);
        }

        /**
         * <p>
         * The idea is that we begin at start and build unique trail segments. The segments are built
         * by exploring a path until a 3-way or 4-way intersection is encountered or there are no additional
         * neighbors to explore. We can easily detect this by checking how many edges a node has.
         * </p>
         * <p>
         *
         *     <ul>
         *         If we exhaust a path:
         *         <li>
         *              If another 3-way or 4-way intersection is the last node and it has not already
         *              been encountered on this segment add it to this segment and queue it up to be explored.
         *         </li>
         *         <li>
         *             If another 3-way or 4-way intersection is the last node and it has already
         *             been encountered on this segment, do not add it again and
         *             mark this segment as a dead-end and cycle.
         *         </li>
         *         <li>
         *             If the segment contains the finish, add it to this segment and indicate that
         *             it leads to the finish.
         *         </li>
         *         <li>
         *             If the last node encountered is not the finish and it has not already been encountered
         *             on this segment and it is not a 3-way or 4-way intersection, then add it to this
         *             segment and mark the segment as a dead-end .
         *          </li>
         *          <li>
         *              If the last node encountered is not the finish and it is not a 3-way or 4-way intersection,
         *              but has been encountered on this segment, then do not add it again and mark this
         *              segment as a dead-end and mark it as a cycle.
         *          </li>
         *     </ul>
         * </p>
         */
        private void buildTrailSegments() {
            segmentMap.clear();
            trailSegments.clear();
            Map<TrailTile, Set<Direction>> ignoreDirections = new HashMap<>();

            var queue = new LinkedList<TrailNode>();

            queue.push(nodeMap.get(start));

            var explored = new HashSet<TrailNode>();
            var segmentCount = 0;

            while (queue.peek() != null) {
                var node = queue.poll();

                if (explored.contains(node)) {
                    continue;
                }

                explored.add(node);

                for (var direction : node.edges().keySet()) {
                    if (ignoreDirections.computeIfAbsent(node.tile(), it -> new HashSet<>()).contains(direction)) {
                        continue; // Already processed this direction!
                    }
                    // Guard against adding the same segment but in reverse!
                    var edge = node.edges().get(direction);

                    if (edge != null) {
                        var ignored = ignoreDirections.computeIfAbsent(edge.tile(), it -> new HashSet<>());
                        if (ignored.contains(direction)) {
                            continue;
                        }
                        if (edge.oneway() && getWalkableNeighbor(edge.tile(), direction) == null) {
                            continue;
                        }
                    }

                    var segment = new HikingSegment(segmentCount, node);
                    buildTrailSegment(direction, node, segment, ignoreDirections);
                    if (segment.isDeadEnd()) {
                        deadEnds.add(segment);
                    }
                    segmentMap.computeIfAbsent(node, it -> new HashSet<>()).add(segment);
                    segmentMap.computeIfAbsent(segment.getSecond(), it -> new HashSet<>()).add(segment);
                    trailSegments.add(segment);
                    segmentCount++;
                    queue.offer(segment.getSecond());
                }
            }

            List.copyOf(trailSegments);
        }

        private void buildTrailSegment(
                Direction direction,
                TrailNode head,
                HikingSegment segment,
                Map<TrailTile, Set<Direction>> ignoreDirections
        ) {
            // Build out the specified segment starting from the head node in the indicated direction
            var edge = head.edges().get(direction);
            var node = nodeMap.get(edge.tile);
            Direction currentDirection = direction;
            var ignore = currentDirection.opposite();
            int length = edge.weight();

            segment.add(edge);
            segment.add(node);
            segment.setLength(length);
            segment.setSecond(node);
            if (edge.oneway() && !ignoreSlopes) {
                segment.setOneway(true);
            }

            if (node.edges.size() > 2 || (!ignoreSlopes && node.tile().isSlope())) {
                return;
            }

            Map.Entry<Direction, TrailEdge> edgeEntry;

            boolean done = false;

            while (!done) {
                // We iteratively process the edges
                edgeEntry = getNextEdgeEntry(node, currentDirection, ignore);

                if (edgeEntry == null) {
                    break;
                }
                edge = edgeEntry.getValue();
                node = nodeMap.get(edge.tile);
                currentDirection = edgeEntry.getKey();
                ignore = currentDirection.opposite();
                length += edge.weight();
                if (edge.oneway() && !ignoreSlopes) {
                    segment.setOneway(true);
                }

                // Check stopping conditions
                if (start.equals(node.tile())) {
                    // This is the start, so we are done!
                    // This should never happen as it means we went the wrong way!!
                    segment.setDeadEnd(true);
                    done = true;
                } else if (finish.equals(node.tile())) {
                    // This is the finish, so we are done!
                    segment.setFinish(true);
                    done = true;
                } else if (node.edges.size() == 1) {
                    if (!node.tile().trailType.canWalkInDirection(currentDirection, ignoreSlopes)) {
                        // This is a dead-end, so we are done!
                        segment.setDeadEnd(true);
                    }
                    if (segment.contains(node)) {
                        // This is also a cycle!
                        segment.setCycle(true);
                    }
                    done = true;
                } else if (node.edges.size() > 2) {
                    // This is a 3-way or 4-way intersection, so we are done!
                    if (segment.contains(node)) {
                        // This is also a cycle!
                        segment.setCycle(true);
                    }
                    done = true;
                } else if (segment.contains(node)) {
                    // This is a dead-end cycle, so we are done!
                    segment.setCycle(true);
                    segment.setDeadEnd(true);
                    done = true;
                }
                if (!segment.isCycle()) {
                    if (done) {
                        ignoreDirections.computeIfAbsent(node.tile(), it -> new HashSet<>()).add(ignore);
                    }
                    segment.add(edge);
                    segment.add(node);
                    segment.setLength(length);
                    segment.setSecond(node);
                }
            }
        }

        private Map.Entry<Direction, TrailEdge> getNextEdgeEntry(TrailNode node, Direction direction, Direction ignore) {
            var edge = node.edges().get(direction);

            if (edge == null) {
                // No edge in desired direction, so find a different edge
                for (var entry : node.edges.entrySet()) {
                    if (entry.getKey() == ignore) {
                        continue;
                    }
                    return entry;
                }
            } else {
                return new AbstractMap.SimpleEntry<>(direction, edge);
            }

            return null;
        }

        /**
         * Requires that the Trail Segments have already been built.
         * <p>
         * Finds and builds all unique trails that lead from start to finish.
         * That means that trails that cause cycles or that do not lead to the
         * finish are not included in the list that is built.
         * </p>
         * <p>
         * The amount of time it takes this method to calculate the unique trails
         * is directly proportional to the size and layout of the hiking trails!
         * Meaning that the more Trail Segments the longer this will take.
         * </p>
         * <p>
         * As stated above, this method will detect cycles and paths that do not
         * to finish and remove them from further consideration when building
         * the unique trails.
         * </p>
         * <p>
         * Each path is unique in that the same tile cannot be included twice.
         * </p>
         * <p>
         * A straight dead-end is any trail section that starts from a three-way or four-way
         * intersection, has no other paths leading off from it, and results in not
         * reaching the finish.
         * </p>
         * <p>
         * Once detected, the start of the straight dead-end, which is the intersection that
         * the dead-end came from, along with the direction is recorded so that future
         * searches will avoid it.
         * </p>
         * <p>
         * A cycle dead-end is any trail section that is not a straight dead-end and leads
         * directly back to a section of trail that has already been included.
         * </p>
         * <p>
         * Like a straight dead end, a cycle dead-end has its start and direction recorded.
         * Additionally, the two path parts involved in the cycle are recorded along with
         * other key segments that make up the cycle.
         * </p>
         */
        private void buildTrails() {
            if (trailNodes.isEmpty() || nodeMap.isEmpty() || trailSegments.isEmpty() || segmentMap.isEmpty()) {
                return;
            }

            trails.clear();
            var pathId = new AtomicInteger(0);
            var explored = new HashSet<HikingSegment>();

            // Start at the beginning!
            segmentMap.get(nodeMap.get(start))
                    .forEach(segment ->
                            trails.addAll(buildAllTrails(segment, pathId, null, explored)
                                    .stream()
                                    .distinct()
                                    .toList())
                    );
        }

        private Collection<? extends HikingTrail> buildAllTrails(
                HikingSegment segment,
                AtomicInteger pathId,
                HikingTrail existingTrail,
                HashSet<HikingSegment> explored
        ) {
            var newTrail = existingTrail == null ?
                    new HikingTrail(pathId.getAndIncrement(), segment) :
                    existingTrail.copy(existingTrail.getId());
            var newTrails = new LinkedList<HikingTrail>();

            if (newTrail.isLeadsToExit()) {
                if (!newTrail.contains(segment)) {
                    newTrail.add(segment);
                }

                return List.of(newTrail);
            }

            explored.add(segment); // Don't re-explore segments for this trail!

            // Ok, we are not at the finish yet!
            List<HikingSegment> nodeSegments = new LinkedList<>();
            nodeSegments.addAll(segmentMap.get(nodeMap.get(segment.getFirst().tile())));
            nodeSegments.addAll(segmentMap.get(nodeMap.get(segment.getSecond().tile())));
            nodeSegments = nodeSegments.stream().filter(it -> !explored.contains(it)).toList();

            for (var newSegment : nodeSegments) {
                if (explored.contains(newSegment) || newTrail.contains(newSegment)) {
                    continue; // already explored this segment on this trail!
                }
                if (!segment.canWalkThroughSegment(newSegment, ignoreSlopes)) {
                    continue; // Possible cycle or walking back over a segment or just disjointed.
                }
                if (!newSegment.isOneway() || segment.getSecond().equals(newSegment.getFirst())) {
                    // Explore from here!
                    if (newTrail.add(newSegment)) {
                        var newExplored = new HashSet<>(explored);
                        var trailParts = buildAllTrails(newSegment, pathId, newTrail.copy(pathId.getAndIncrement()), newExplored);
                        trailParts.forEach(part -> {
                            if (part.isLeadsToExit()) {
                                newTrails.add(part);
                            }
                        });
                        newTrail.remove(newSegment);
                    }
                }
            }

            if (!newTrail.isLeadsToExit() && nodeSegments.isEmpty()) {
                newTrail.remove(segment);
            }

            return newTrails;
        }

        public Point2D<Integer> pointInDirection(TrailTile tile, Direction direction) {
            return virtualGrid ? grid.virtualToReal(tile.pointInDirection(direction)) :
                    tile.pointInDirection(direction);
        }

        public TrailTile get(Point2D<Integer> point) {
            return grid.get(point);
        }

        public boolean canWalkInDirection(TrailTile tile, Direction direction) {
            return tile.canWalkInDirection(direction, ignoreSlopes);
        }

        /**
         * Returns true if this tile is an intersection tile. An Intersection tile is defined as a tile that is the
         * start or finish tile or a slope tile if slopes are not being ignored, or a tile that contains at least
         * two walkable tiles in two directions that are perpendicular to each other. In short, if the walkable
         * tiles form a right-angle then it is an intersection. Please note that all tiles must be walkable!
         *
         * @param tile A walkable tile to determine if it is an intersection or not.
         * @return True is returned if the specified tile is an intersection tile.
         */
        public boolean isIntersection(TrailTile tile) {
            if (tile == null || !tile.isWalkable()) {
                return false;
            }

            if (tile.equals(start) || tile.equals(finish)) {
                return true;
            }

            var directions = tile.walkableDirections(ignoreSlopes);

            if (!ignoreSlopes && tile.trailType().isSlope()) {
                return true;
            }

            if (directions.size() <= 1) {
                return false;
            }

            var neighbors = getWalkableNeighbors(tile, directions);

            if (neighbors.size() == 1) {
                // This is a dead-end!
                return true;
            }

            for (var direction : neighbors.keySet()) {
                for (var perpendicular : direction.getPerpendicular()) {
                    if (neighbors.containsKey(perpendicular)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private Map<Direction, TrailTile> getWalkableNeighbors(TrailTile tile, Set<Direction> directions) {
            var neighbors = new HashMap<Direction, TrailTile>(directions.size() + 2);

            for (var direction : directions) {
                var neighbor = getWalkableNeighbor(tile, direction);

                if (neighbor != null && neighbor.isWalkable()) {
                    neighbors.put(direction, neighbor);
                }
            }

            return neighbors;
        }

        public TrailTile getWalkableNeighbor(TrailTile tile, Direction direction) {
            if (!canWalkInDirection(tile, direction)) {
                return null;
            }

            var point = pointInDirection(tile, direction);

            if (!isOnGrid(point)) {
                return null;
            }

            return get(point);
        }

        public boolean isOnGrid(Point2D<Integer> point) {
            return grid.isOnGrid(point);
        }

        public static HikingTrails build(Stream<String> stream, boolean ignoreSlopes, boolean virtualGrid) {
            var columns = new AtomicInteger(0);
            var rows = new AtomicInteger(0);
            var first = new AtomicBoolean(true);
            var startTile = new AtomicReference<TrailTile>();
            var finishTile = new AtomicReference<TrailTile>();
            var rowResults = new LinkedList<RowResults>();

            stream.forEach(line -> {
                if (first.get()) {
                    columns.set(line.length());
                    first.set(false);
                }

                rowResults.add(parseLine(line, rows.getAndIncrement()));
            });

            var tiles = new ArrayList<TrailTile>(rows.get() * columns.get() + columns.get());
            rowResults.stream().map(RowResults::tiles).forEach(tiles::addAll);

            for (int i = 0; i < columns.get() * rows.get(); i++) {
                if (tiles.get(i).trailType() == TrailType.PATH) {
                    startTile.set(tiles.get(i));
                    break;
                }
            }

            for (int i = columns.get() * rows.get() - 1; i >= 0; i--) {
                if (tiles.get(i).trailType() == TrailType.PATH) {
                    finishTile.set(tiles.get(i));
                    break;
                }
            }

            var hikingTrails = new HikingTrails(
                    new Grid2D<>(rows.get(), columns.get(), tiles.toArray(new TrailTile[rows.get() * columns.get()])),
                    new HashMap<>(),
                    new ArrayList<>(),
                    new HashMap<>(),
                    new ArrayList<>(),
                    new HashSet<>(),
                    new ArrayList<>(),
                    startTile.get(),
                    finishTile.get(),
                    ignoreSlopes,
                    virtualGrid
            );

            hikingTrails.buildTrailGraph();
            hikingTrails.buildTrailSegments();
            hikingTrails.buildTrails();

            return hikingTrails;
        }

        private static RowResults parseLine(String line, int row) {
            if (line == null || line.isBlank()) {
                return null;
            }

            var tiles = new LinkedList<TrailTile>();
            var columns = line.length();

            for (int i = 0; i < columns; i++) {
                char element = line.charAt(i);
                TrailTile tile;
                var point = new Point2D<Integer>(i, row);
                tile = new TrailTile(TrailType.from(element), point);
                tiles.add(tile);
            }

            return new RowResults(tiles, null);
        }

        public List<HikingTrail> getTrails() {
            return trails;
        }
    }

    private static final String inputFilename = "inputs/input_day_23-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        part1(path);
        part2(path);
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            LOGGER.info("Part 1: Start!");
            var start = Instant.now();
            var hikingTrails = HikingTrails.build(stream, false, true);
            var longestPath = hikingTrails.getTrails().stream().max(Comparator.comparing(it -> it.length)).orElse(null);

            var end = Instant.now();
            LOGGER.info(String.format("The longest path from start to finish is: %d in %d ns",
                    (longestPath != null ? longestPath.getLength() : -1), Duration.between(start, end).toNanos()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            LOGGER.info("Part 2: Start!");
            var start = Instant.now();
            var hikingTrails = HikingTrails.build(stream, true, true);
            var longestPath = hikingTrails.getTrails().stream().max(Comparator.comparing(it -> it.length)).orElse(null);
            var end = Instant.now();
            LOGGER.info(String.format("The longest path from start to finish (ignoring slopes) is: %d in %d ns",
                    (longestPath != null ? longestPath.getLength() : -1), Duration.between(start, end).toNanos()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
