package com.capital7software.aoc.lib.grid;


import com.capital7software.aoc.lib.collection.PriorityQueue;
import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Graph;
import com.capital7software.aoc.lib.graph.Vertex;
import com.capital7software.aoc.lib.graph.path.GenericPathFinder;
import com.capital7software.aoc.lib.graph.path.PathFinderResult;
import com.capital7software.aoc.lib.graph.path.PathFinderStatus;
import com.capital7software.aoc.lib.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * There's a map of nearby hiking trails (your puzzle input) that indicates paths (.),
 * forest (#), and steep slopes (^, &#62;, v, and &#60;).
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * <code>
 * #.#####################<br>
 * #.......#########...###<br>
 * #######.#########.#.###<br>
 * ###.....#.&#62;.&#62;.###.#.###<br>
 * ###v#####.#v#.###.#.###<br>
 * ###.&#62;...#.#.#.....#...#<br>
 * ###v###.#.#.#########.#<br>
 * ###...#.#.#.......#...#<br>
 * #####.#.#.#######.#.###<br>
 * #.....#.#.#.......#...#<br>
 * #.#####.#.#.#########v#<br>
 * #.#...#...#...###...&#62;.#<br>
 * #.#.#v#######v###.###v#<br>
 * #...#.&#62;.#...&#62;.&#62;.#.###.#<br>
 * #####v#.#.###v#.#.###.#<br>
 * #.....#...#...#.#.#...#<br>
 * #.#########.###.#.#.###<br>
 * #...###...#...#...#.###<br>
 * ###.###.#.###v#####v###<br>
 * #...#...#.#.&#62;.&#62;.#.&#62;.###<br>
 * #.###.###.#.###.#.#v###<br>
 * #.....###...###...#...#<br>
 * #####################.#<br>
 * </code>
 *
 * <p><br>
 * You're currently on the single path tile in the top row; your goal is to reach the single path
 * tile in the bottom row. Because of all the mist from the waterfall, the slopes are probably
 * quite icy; if you step onto a slope tile, your next step must be downhill (in the direction
 * the arrow is pointing). To make sure you have the most scenic hike possible, never step onto
 * the same tile twice. What is the longest hike you can take?
 *
 * <p><br>
 * In the example above, the longest hike you can take is marked with O, and your starting
 * position is marked S:
 *
 * <p><br>
 * <code>
 * #S#####################<br>
 * #OOOOOOO#########...###<br>
 * #######O#########.#.###<br>
 * ###OOOOO#OOO&#62;.###.#.###<br>
 * ###O#####O#O#.###.#.###<br>
 * ###OOOOO#O#O#.....#...#<br>
 * ###v###O#O#O#########.#<br>
 * ###...#O#O#OOOOOOO#...#<br>
 * #####.#O#O#######O#.###<br>
 * #.....#O#O#OOOOOOO#...#<br>
 * #.#####O#O#O#########v#<br>
 * #.#...#OOO#OOO###OOOOO#<br>
 * #.#.#v#######O###O###O#<br>
 * #...#.&#62;.#...&#62;OOO#O###O#<br>
 * #####v#.#.###v#O#O###O#<br>
 * #.....#...#...#O#O#OOO#<br>
 * #.#########.###O#O#O###<br>
 * #...###...#...#OOO#O###<br>
 * ###.###.#.###v#####O###<br>
 * #...#...#.#.&#62;.&#62;.#.&#62;O###<br>
 * #.###.###.#.###.#.#O###<br>
 * #.....###...###...#OOO#<br>
 * #####################O#<br>
 * </code>
 *
 * <p><br>
 * This hike contains 94 steps. (The other possible hikes you could have taken were 90, 86, 82, 82,
 * and 74 steps long.)
 *
 * <p><br>
 * Find the longest hike you can take through the hiking trails listed on your map. How many steps
 * long is the longest hike?
 *
 * <p><br>
 * As you reach the trailhead, you realize that the ground isn't as slippery as you expected; you'll
 * have no problem climbing up the steep slopes.
 *
 * <p><br>
 * Now, treat all slopes as if they were normal paths (.). You still want to make sure you have the
 * most scenic hike possible, so continue to ensure that you never step onto the same tile twice.
 * What is the longest hike you can take?
 *
 * <p><br>
 * In the example above, this increases the longest hike to 154 steps:
 *
 * <p><br>
 * <code>
 * #S#####################<br>
 * #OOOOOOO#########OOO###<br>
 * #######O#########O#O###<br>
 * ###OOOOO#.&#62;OOO###O#O###<br>
 * ###O#####.#O#O###O#O###<br>
 * ###O&#62;...#.#O#OOOOO#OOO#<br>
 * ###O###.#.#O#########O#<br>
 * ###OOO#.#.#OOOOOOO#OOO#<br>
 * #####O#.#.#######O#O###<br>
 * #OOOOO#.#.#OOOOOOO#OOO#<br>
 * #O#####.#.#O#########O#<br>
 * #O#OOO#...#OOO###...&#62;O#<br>
 * #O#O#O#######O###.###O#<br>
 * #OOO#O&#62;.#...&#62;O&#62;.#.###O#<br>
 * #####O#.#.###O#.#.###O#<br>
 * #OOOOO#...#OOO#.#.#OOO#<br>
 * #O#########O###.#.#O###<br>
 * #OOO###OOO#OOO#...#O###<br>
 * ###O###O#O###O#####O###<br>
 * #OOO#OOO#O#OOO&#62;.#.&#62;O###<br>
 * #O###O###O#O###.#.#O###<br>
 * #OOOOO###OOO###...#OOO#<br>
 * #####################O#<br>
 * </code>
 *
 * <p><br>
 * Find the longest hike you can take through the surprisingly dry hiking trails listed on your map.
 * How many steps long is the longest hike?
 */
public class HikingTrails {
  private enum TrailType {
    PATH('.', true, false) {
      @Override
      public Set<Direction> walkableDirections(boolean ignoreSlopes) {
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
      public Set<Direction> walkableDirections(boolean ignoreSlopes) {
        if (ignoreSlopes) {
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
      public Set<Direction> walkableDirections(boolean ignoreSlopes) {
        if (ignoreSlopes) {
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
      public Set<Direction> walkableDirections(boolean ignoreSlopes) {
        if (ignoreSlopes) {
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
      public Set<Direction> walkableDirections(boolean ignoreSlopes) {
        if (ignoreSlopes) {
          return ALL;
        }

        return WEST;
      }

      @Override
      public boolean canWalkInDirection(Direction direction, boolean ignoreSlopes) {
        return direction == Direction.WEST || ignoreSlopes;
      }
    };

    private static final Set<Direction> ALL = new HashSet<>(Direction.CARDINAL_DIRECTIONS);
    private static final Set<Direction> NORTH = Set.of(Direction.NORTH);
    private static final Set<Direction> SOUTH = Set.of(Direction.SOUTH);
    private static final Set<Direction> EAST = Set.of(Direction.EAST);
    private static final Set<Direction> WEST = Set.of(Direction.WEST);
    private static final Set<Direction> NONE = Collections.emptySet();

    private final char label;
    @Getter
    private final boolean walkable;
    @Getter
    private final boolean slope;

    TrailType(char label, boolean walkable, boolean slope) {
      this.label = label;
      this.walkable = walkable;
      this.slope = slope;
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

  private record TrailTile(TrailType trailType, Point2D<Integer> point)
      implements Comparable<TrailTile> {
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

    @Override
    public int compareTo(@NotNull HikingTrails.TrailTile o) {
      return point.compareTo(o.point);
    }

    public String toId() {
      return point.x() + "-" + point.y();
    }
  }

  private static class HikingSegment {
    private final long id;
    @Getter
    private final Vertex<TrailTile, Integer> first;
    @Setter
    @Getter
    private Vertex<TrailTile, Integer> second;
    private final Set<Vertex<TrailTile, Integer>> nodes;
    @Setter
    @Getter
    private long length;
    @Setter
    @Getter
    private boolean deadEnd;
    @Setter
    @Getter
    private boolean cycle;
    @Setter
    @Getter
    private boolean finish;
    @Setter
    @Getter
    private boolean oneway;

    public HikingSegment(long id, Vertex<TrailTile, Integer> first) {
      this.id = id;
      this.first = first;
      this.second = null;
      this.nodes = new HashSet<>();
      add(first);
      this.length = -1;
      this.deadEnd = false;
      this.cycle = false;
      this.finish = false;
      this.oneway = first.getValue().isPresent() && first.getValue().get().isSlope();
    }

    public void add(Vertex<TrailTile, Integer> node) {
      nodes.add(node);
    }

    public boolean contains(Vertex<TrailTile, Integer> trailNode) {
      return nodes.contains(trailNode);
    }

    @Override
    public String toString() {
      return "HikingSegment{"
          + "id=" + id
          + ", length=" + length
          + ", deadEnd=" + deadEnd
          + ", cycle=" + cycle
          + ", finish=" + finish
          + ", oneway=" + oneway
          + '}';
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

  private record RowResults(List<TrailTile> tiles, Point2D<Integer> initialPosition) {
  }

  private final Grid2D<TrailTile> grid;
  private final Graph<TrailTile, Integer> intersectionGraph;
  private final Graph<TrailTile, Long> segmentGraph;
  private final TrailTile start;
  private final TrailTile finish;
  private final boolean ignoreSlopes;
  private final boolean virtualGrid;

  private HikingTrails(
      Grid2D<TrailTile> grid,
      TrailTile start,
      TrailTile finish,
      boolean ignoreSlopes,
      boolean virtualGrid
  ) {
    this.grid = grid;
    this.start = start;
    this.finish = finish;
    this.ignoreSlopes = ignoreSlopes;
    this.virtualGrid = virtualGrid;
    this.intersectionGraph = buildIntersectionGraph();
    this.segmentGraph = buildSegmentGraph();
  }

  private Graph<TrailTile, Integer> buildIntersectionGraph() {
    var graph = new Graph<TrailTile, Integer>("hiking-intersections");

    var queue = new LinkedList<TrailTile>();

    queue.add(start);

    var visited = new HashSet<TrailTile>();

    while (!queue.isEmpty()) {
      var tile = queue.poll();

      if (tile == null || visited.contains(tile)) {
        continue; // We have already explored this tile!
      }

      visited.add(tile);
      var nodeId = tile.toId();

      graph.add(new Vertex<>(nodeId, tile));
      var node = graph.getVertex(nodeId).orElse(null);

      if (node == null) {
        continue;
      }

      var directions = tile.walkableDirections(ignoreSlopes);

      for (var direction : directions) {
        var point = pointInDirection(tile, direction);
        if (isOnGrid(point)) {
          var target = getTargetFromHere(tile, direction);
          if (target != null && target.first() != null && target.second() != null) {
            var targetId = target.first().toId();

            graph.add(new Vertex<>(targetId, target.first()));

            graph.add(
                nodeId,
                targetId,
                nodeId + "-" + targetId,
                target.second()
            );

            if (!visited.contains(target.first())) {
              queue.offer(target.first());
            }
          }
        }
      }
    }
    return graph;
  }

  /**
   * Returns a Pair where the first element is the intersection tile found in the specified
   * direction and the second element is the length from the specified tile to the
   * intersection tile. <b>Please note that a point in the specified direction must exist
   * or else a NullPointerException will be thrown.</b>
   *
   * @param tile      The tile to start our walk from.
   * @param direction The direction of the walk.
   * @return A Pair with the first element being the intersection tile and the second the
   *     number of steps to get from the source tile to the intersection tile.
   */
  private Pair<TrailTile, Integer> getTargetFromHere(
      @NotNull TrailTile tile,
      @NotNull Direction direction
  ) {
    var point = pointInDirection(tile, direction);

    // The above point is guaranteed to be on the grid!
    var currentTile = get(point);

    if (currentTile == null || !currentTile.isWalkable()) {
      return null;
    }

    var queue = new LinkedList<TrailTile>();

    queue.offer(tile);

    var count = 0;

    while (!queue.isEmpty()) {
      currentTile = queue.poll();
      count++;

      point = pointInDirection(currentTile, direction);

      if (isOnGrid(point)) {
        var newTile = get(point);
        if (newTile != null && newTile.isWalkable()) {
          currentTile = newTile;

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

    return new Pair<>(currentTile, count);
  }

  /**
   * The idea is that we begin at start and build unique trail segments. The segments are built
   * by exploring a path until a 3-way or 4-way intersection is encountered or there are no
   * additional neighbors to explore. We can easily detect this by checking how many edges a
   * node has.
   *
   * <p><br>
   * If we exhaust a path:
   * <ul>
   *     <li>
   *          If another 3-way or 4-way intersection is the last node and it has not already
   *          been encountered on this segment add it to this segment and queue it up to be
   *          explored.
   *     </li>
   *     <li>
   *         If another 3-way or 4-way intersection is the last node and it has already
   *         been encountered on this segment, do not add it again and
   *         mark this segment as a dead-end and cycle.
   *     </li>
   *     <li>
   *         If the segment contains the finish, add it to this segment and indicate that
   *         it leads to the finish.
   *     </li>
   *     <li>
   *         If the last node encountered is not the finish and it has not already been encountered
   *         on this segment and it is not a 3-way or 4-way intersection, then add it to this
   *         segment and mark the segment as a dead-end .
   *      </li>
   *      <li>
   *          If the last node encountered is not the finish and it is not a 3-way or 4-way
   *          intersection, but has been encountered on this segment, then do not add it again
   *          and mark this segment as a dead-end and mark it as a cycle.
   *      </li>
   * </ul>
   */
  private Graph<TrailTile, Long> buildSegmentGraph() {
    final var queue = new PriorityQueue<Vertex<TrailTile, Integer>>();
    final var graph = new Graph<TrailTile, Long>("hiking-segments");

    queue.offer(
        intersectionGraph.getVertex(start.toId()).orElse(new Vertex<>(start.toId(), start))
    );

    final var explored = new HashSet<String>();
    var segmentCount = 1;

    while (queue.peek() != null) {
      var node = queue.poll();

      if (node == null || explored.contains(node.getId())) {
        continue;
      }

      explored.add(node.getId());

      for (var edge : intersectionGraph.getEdges(node)) {
        var segment = new HikingSegment(segmentCount, node);
        buildTrailSegment(intersectionGraph, edge, node, segment);
        if (segment.getFirst().getValue().isPresent()) {
          graph.add(
              new Vertex<>(segment.getFirst().getId(), segment.getFirst().getValue().get())
          );
        } else {
          graph.add(new Vertex<>(segment.getFirst().getId()));
        }

        if (segment.getSecond().getValue().isPresent()) {
          graph.add(
              new Vertex<>(segment.getSecond().getId(), segment.getSecond().getValue().get())
          );
        } else {
          graph.add(new Vertex<>(segment.getSecond().getId()));
        }

        graph.add(
            segment.getFirst().getId(),
            segment.getSecond().getId(),
            "" + segmentCount,
            segment.getLength()
        );
        if (!segment.isOneway()) {
          graph.add(
              segment.getSecond().getId(),
              segment.getFirst().getId(),
              "" + (-segmentCount),
              segment.getLength()
          );
        }
        segmentCount++;
        queue.offer(segment.getSecond());
      }
    }

    return graph;
  }

  private void buildTrailSegment(
      @NotNull Graph<TrailTile, Integer> graph,
      Edge<Integer> edge,
      Vertex<TrailTile, Integer> head,
      HikingSegment segment
  ) {
    // Build out the specified segment starting from the head node in the indicated direction
    var node = Objects.requireNonNull(graph.getVertex(edge.getTarget()).orElse(null));

    int length = edge.getWeight().orElse(0);

    segment.add(node);
    segment.setLength(length);
    segment.setSecond(node);

    if (node.getValue().isPresent() && node.getValue().get().isSlope() && !ignoreSlopes) {
      segment.setOneway(true);
    }

    if (node.size() > 2
        || (!ignoreSlopes && node.getValue().isPresent() && node.getValue().get().isSlope())) {
      return;
    }

    var visited = new HashSet<String>();
    visited.add(head.getId());
    visited.add(node.getId());

    boolean done = false;

    while (!done) {
      // We iteratively process the edges
      edge = getNextEdge(node, graph, visited);

      if (edge == null) {
        break;
      }

      node = graph.getVertex(edge.getTarget()).orElse(null);

      if (node == null) {
        break;
      }

      var tile = node.getValue().orElse(null);

      if (tile == null) {
        break;
      }

      length += edge.getWeight().orElse(0);

      if (tile.isSlope() && !ignoreSlopes) {
        segment.setOneway(true);
      }

      // Check stopping conditions
      if (start.equals(tile)) {
        // This is the start, so we are done!
        // This should never happen as it means we went the wrong way!!
        segment.setDeadEnd(true);
        done = true;
      } else if (finish.equals(tile)) {
        // This is the finish, so we are done!
        segment.setFinish(true);
        done = true;
      } else {
        int nodeSize = node.size();
        if (nodeSize == 1) {
          var nodeEdges = node.getEdges().values();

          for (var nodeEdge : nodeEdges) {
            if (visited.contains(nodeEdge.getTarget())) {
              // This is a dead-end, so we are done!
              segment.setDeadEnd(true);
            }
          }
          if (segment.contains(node)) {
            // This is also a cycle!
            segment.setCycle(true);
          }
          done = true;
        } else if (nodeSize > 2) {
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
      }
      if (!segment.isCycle()) {
        visited.add(node.getId());
        segment.add(node);
        segment.setLength(length);
        segment.setSecond(node);
      }
    }
  }

  private Edge<Integer> getNextEdge(
      @NotNull Vertex<TrailTile, Integer> node,
      @NotNull Graph<TrailTile, Integer> graph,
      @NotNull HashSet<String> visited
  ) {
    for (var edge : graph.getEdges(node)) {
      if (visited.contains(edge.getTarget())) {
        continue;
      }
      return edge;
    }
    return null;
  }

  private Point2D<Integer> pointInDirection(TrailTile tile, Direction direction) {
    return virtualGrid ? grid.virtualToReal(tile.pointInDirection(direction)) :
        tile.pointInDirection(direction);
  }

  private TrailTile get(Point2D<Integer> point) {
    return grid.get(point);
  }

  private boolean canWalkInDirection(TrailTile tile, Direction direction) {
    return tile.canWalkInDirection(direction, ignoreSlopes);
  }

  /**
   * Returns true if this tile is an intersection tile. An Intersection tile is defined as a
   * tile that is the start or finish tile or a slope tile if slopes are not being ignored,
   * or a tile that contains at least two walkable tiles in two directions that are perpendicular
   * to each other. In short, if the walkable tiles form a right-angle then it is an intersection.
   * Please note that all tiles must be walkable!
   *
   * @param tile A walkable tile to determine if it is an intersection or not.
   * @return True is returned if the specified tile is an intersection tile.
   */
  private boolean isIntersection(TrailTile tile) {
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

  private Map<Direction, TrailTile> getWalkableNeighbors(
      TrailTile tile,
      Set<Direction> directions
  ) {
    var neighbors = new HashMap<Direction, TrailTile>(directions.size() + 2);

    for (var direction : directions) {
      var neighbor = getWalkableNeighbor(tile, direction);

      if (neighbor != null && neighbor.isWalkable()) {
        neighbors.put(direction, neighbor);
      }
    }

    return neighbors;
  }

  private TrailTile getWalkableNeighbor(TrailTile tile, Direction direction) {
    if (!canWalkInDirection(tile, direction)) {
      return null;
    }

    var point = pointInDirection(tile, direction);

    if (!isOnGrid(point)) {
      return null;
    }

    return get(point);
  }

  private boolean isOnGrid(Point2D<Integer> point) {
    return grid.isOnGrid(point);
  }

  /**
   * Builds and returns a new HikingTrails instance. The instance is populated with the
   * available trails that lead to the exit along with the distance of each trail. If
   * ignoreSlopes is true, then slopes are ignored when building the hiking trails. If
   * virtualGrid is true, then the grid is treated as if it were virtual.
   *
   * @param input        The List of Strings to parse into HikingTrails.
   * @param ignoreSlopes If true, then the slopes on the trails are ignored when building
   *                     the trails.
   * @param virtualGrid  If true, the grid is treated as being virtual.
   * @return A new HikingTrails instance populated with the available trails and their distances.
   */
  public static HikingTrails buildHikingTrails(
      List<String> input,
      boolean ignoreSlopes,
      boolean virtualGrid
  ) {
    var columns = new AtomicInteger(0);
    var rows = new AtomicInteger(0);
    var first = new AtomicBoolean(true);
    var startTile = new AtomicReference<TrailTile>();
    var finishTile = new AtomicReference<TrailTile>();
    var rowResults = new LinkedList<RowResults>();

    input.forEach(line -> {
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

    return new HikingTrails(
        new Grid2D<>(
            rows.get(), columns.get(), tiles.toArray(new TrailTile[rows.get() * columns.get()])
        ),
        startTile.get(),
        finishTile.get(),
        ignoreSlopes,
        virtualGrid
    );
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
      var point = new Point2D<>(i, row);
      tile = new TrailTile(TrailType.from(element), point);
      tiles.add(tile);
    }

    return new RowResults(tiles, null);
  }

  private Optional<PathFinderResult<TrailTile, Long>> findLongestTrail() {
    final var pathFinder = new GenericPathFinder<TrailTile, Long>();
    final var properties = new Properties();
    properties.put(GenericPathFinder.Props.SUM_PATH, Boolean.TRUE);
    properties.put(GenericPathFinder.Props.STARTING_VERTEX_ID, start.toId());
    properties.put(GenericPathFinder.Props.ENDING_VERTEX_ID, finish.toId());
    var longestPathResult = new AtomicReference<PathFinderResult<TrailTile, Long>>(null);

    pathFinder.find(
        segmentGraph,
        properties,
        it -> {
          if (longestPathResult.get() == null) {
            longestPathResult.set(it);
          } else {
            if (longestPathResult.get().cost() < it.cost()) {
              longestPathResult.set(it);
            }
          }
          return PathFinderStatus.CONTINUE;
        },
        null
    );

    return Optional.ofNullable(longestPathResult.get());
  }

  /**
   * Returns the number of steps on the longest HikingTrail.
   *
   * @return The number of steps on the longest HikingTrail.
   */
  public long stepsOfLongestTrail() {
    var longestTrail = findLongestTrail();

    return longestTrail.map(PathFinderResult::cost).orElse(-1L);
  }
}
