package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.collection.PriorityQueue;
import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import com.capital7software.aoc.lib.util.Pair;
import com.capital7software.aoc.lib.util.Range;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Scanning the area, you discover that the entire field you're standing on is densely packed
 * with pipes; it was hard to tell at first because they're the same metallic silver color as
 * the "ground". You make a quick sketch of all the surface pipes you can see (your puzzle input).
 *
 * <p><br>
 * The pipes are arranged in a two-dimensional grid of tiles:
 *
 * <p><br>
 * <code>
 * | is a vertical pipe connecting north and south.<br>
 * - is a horizontal pipe connecting east and west.<br>
 * L is a 90-degree bend connecting north and east.<br>
 * J is a 90-degree bend connecting north and west.<br>
 * 7 is a 90-degree bend connecting south and west.<br>
 * F is a 90-degree bend connecting south and east.<br>
 * . is ground; there is no pipe in this tile.<br>
 * S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't
 * show what shape the pipe has.<br><br>
 * </code>
 * Based on the acoustics of the animal's scurrying, you're confident the pipe that contains
 * the animal is one large, continuous loop.
 *
 * <p><br>
 * For example, here is a square loop of pipe:
 *
 * <p><br>
 * <code>
 * .....<br>
 * .F-7.<br>
 * .|.|.<br>
 * .L-J.<br>
 * .....<br>
 * </code>
 *
 * <p><br>
 * If the animal had entered this loop in the northwest corner, the sketch would instead look
 * like this:
 *
 * <p><br>
 * <code>
 * .....<br>
 * .S-7.<br>
 * .|.|.<br>
 * .L-J.<br>
 * .....<br>
 * </code>
 *
 * <p><br>
 * In the above diagram, the S tile is still a 90-degree F bend: you can tell because of how
 * the adjacent pipes connect to it.
 *
 * <p><br>
 * Unfortunately, there are also many pipes that aren't connected to the loop! This sketch
 * shows the same loop as above:
 *
 * <p><br>
 * <code>
 * -L|F7<br>
 * 7S-7|<br>
 * L|7||<br>
 * -L-J|<br>
 * L|-JF<br>
 * </code>
 *
 * <p><br>
 * In the above diagram, you can still figure out which pipes form the main loop: they're the
 * ones connected to S, pipes those pipes connect to, pipes those pipes connect to, and so on.
 * Every pipe in the main loop connects to its two neighbors (including S, which will have
 * exactly two pipes connecting to it, and which is assumed to connect back to those two pipes).
 *
 * <p><br>
 * Here is a sketch that contains a slightly more complex main loop:
 *
 * <p><br>
 * <code>
 * ..F7.<br>
 * .FJ|.<br>
 * SJ.L7<br>
 * |F--J<br>
 * LJ...<br>
 * </code>
 *
 * <p><br>
 * Here's the same example sketch with the extra, non-main-loop pipe tiles also shown:
 *
 * <p><br>
 * <code>
 * 7-F7-<br>
 * .FJ|7<br>
 * SJLL7<br>
 * |F--J<br>
 * LJ.LJ<br>
 * </code>
 *
 * <p><br>
 * If you want to get out ahead of the animal, you should find the tile in the loop that is
 * farthest from the starting position. Because the animal is in the pipe, it doesn't make
 * sense to measure this by direct distance. Instead, you need to find the tile that would take
 * the longest number of steps along the loop to reach from the starting point - regardless of
 * which way around the loop the animal went.
 *
 * <p><br>
 * In the first example with the square loop:
 *
 * <p><br>
 * <code>
 * .....<br>
 * .S-7.<br>
 * .|.|.<br>
 * .L-J.<br>
 * .....<br>
 * </code>
 *
 * <p><br>
 * You can count the distance each tile in the loop is from the starting point like this:
 *
 * <p><br>
 * <code>
 * .....<br>
 * .012.<br>
 * .1.3.<br>
 * .234.<br>
 * .....<br>
 * </code>
 *
 * <p><br>
 * In this example, the farthest point from the start is 4 steps away.
 *
 * <p><br>
 * Here's the more complex loop again:
 *
 * <p><br>
 * <code>
 * ..F7.<br>
 * .FJ|.<br>
 * SJ.L7<br>
 * |F--J<br>
 * LJ...<br>
 * </code>
 *
 * <p><br>
 * Here are the distances for each tile on that loop:
 *
 * <p><br>
 * <code>
 * ..45.<br>
 * .236.<br>
 * 01.78<br>
 * 14567<br>
 * 23...<br>
 * </code>
 *
 * <p><br>
 * Find the single giant loop starting at S. How many steps along the loop does it take to get
 * from the starting position to the point farthest from the starting position?
 *
 * <p><br>
 * You quickly reach the farthest point of the loop, but the animal never emerges. Maybe its
 * nest is within the area enclosed by the loop?
 *
 * <p><br>
 * To determine whether it's even worth taking the time to search for such a nest, you should
 * calculate how many tiles are contained within the loop. For example:
 *
 * <p><br>
 * <code>
 * ...........<br>
 * .S-------7.<br>
 * .|F-----7|.<br>
 * .||.....||.<br>
 * .||.....||.<br>
 * .|L-7.F-J|.<br>
 * .|..|.|..|.<br>
 * .L--J.L--J.<br>
 * ...........<br>
 * </code>
 *
 * <p><br>
 * The above loop encloses merely four tiles - the two pairs of . in the southwest and southeast
 * (marked I below). The middle . tiles (marked O below) are not in the loop. Here is the same loop
 * again with those regions marked:
 *
 * <p><br>
 * <code>
 * ...........<br>
 * .S-------7.<br>
 * .|F-----7|.<br>
 * .||OOOOO||.<br>
 * .||OOOOO||.<br>
 * .|L-7OF-J|.<br>
 * .|II|O|II|.<br>
 * .L--JOL--J.<br>
 * .....O.....<br>
 * </code>
 *
 * <p><br>
 * In fact, there doesn't even need to be a full tile path to the outside for tiles to count
 * as outside the loop - squeezing between pipes is also allowed! Here, I is still within the
 * loop and O is still outside the loop:
 *
 * <p><br>
 * <code>
 * ..........<br>
 * .S------7.<br>
 * .|F----7|.<br>
 * .||OOOO||.<br>
 * .||OOOO||.<br>
 * .|L-7F-J|.<br>
 * .|II||II|.<br>
 * .L--JL--J.<br>
 * ..........<br>
 * </code>
 *
 * <p><br>
 * In both of the above examples, 4 tiles are enclosed by the loop.
 *
 * <p><br>
 * Here's a larger example:
 *
 * <p><br>
 * <code>
 * .F----7F7F7F7F-7....<br>
 * .|F--7||||||||FJ....<br>
 * .||.FJ||||||||L7....<br>
 * FJL7L7LJLJ||LJ.L-7..<br>
 * L--J.L7...LJS7F-7L7.<br>
 * ....F-J..F7FJ|L7L7L7<br>
 * ....L7.F7||L7|.L7L7|<br>
 * .....|FJLJ|FJ|F7|.LJ<br>
 * ....FJL-7.||.||||...<br>
 * ....L---J.LJ.LJLJ...<br>
 * </code>
 *
 * <p><br>
 * The above sketch has many random bits of ground, some of which are in the loop (I) and some of
 * which are outside it (O):
 *
 * <p><br>
 * <code>
 * OF----7F7F7F7F-7OOOO<br>
 * O|F--7||||||||FJOOOO<br>
 * O||OFJ||||||||L7OOOO<br>
 * FJL7L7LJLJ||LJIL-7OO<br>
 * L--JOL7IIILJS7F-7L7O<br>
 * OOOOF-JIIF7FJ|L7L7L7<br>
 * OOOOL7IF7||L7|IL7L7|<br>
 * OOOOO|FJLJ|FJ|F7|OLJ<br>
 * OOOOFJL-7O||O||||OOO<br>
 * OOOOL---JOLJOLJLJOOO<br>
 * </code>
 *
 * <p><br>
 * In this larger example, 8 tiles are enclosed by the loop.
 *
 * <p><br>
 * Any tile that isn't part of the main loop can count as being enclosed by the loop.
 * Here's another example with many bits of junk pipe lying around that aren't connected to
 * the main loop at all:
 *
 * <p><br>
 * <code>
 * FF7FSF7F7F7F7F7F---7<br>
 * L|LJ||||||||||||F--J<br>
 * FL-7LJLJ||||||LJL-77<br>
 * F--JF--7||LJLJ7F7FJ-<br>
 * L---JF-JLJ.||-FJLJJ7<br>
 * |F|F-JF---7F7-L7L|7|<br>
 * |FFJF7L7F-JF7|JL---7<br>
 * 7-L-JL7||F7|L7F-7F7|<br>
 * L.L7LFJ|||||FJL7||LJ<br>
 * L7JLJL-JLJLJL--JLJ.L<br>
 * </code>
 *
 * <p><br>
 * Here are just the tiles that are enclosed by the loop marked with I:
 *
 * <p><br>
 * <code>
 * FF7FSF7F7F7F7F7F---7<br>
 * L|LJ||||||||||||F--J<br>
 * FL-7LJLJ||||||LJL-77<br>
 * F--JF--7||LJLJIF7FJ-<br>
 * L---JF-JLJIIIIFJLJJ7<br>
 * |F|F-JF---7IIIL7L|7|<br>
 * |FFJF7L7F-JF7IIL---7<br>
 * 7-L-JL7||F7|L7F-7F7|<br>
 * L.L7LFJ|||||FJL7||LJ<br>
 * L7JLJL-JLJLJL--JLJ.L<br>
 * </code>
 *
 * <p><br>
 * In this last example, 10 tiles are enclosed by the loop.
 *
 * <p><br>
 * Figure out whether you have time to search for the nest by calculating the area within the loop.
 * How many tiles are enclosed by the loop?
 */
public class PipeMaze {
  @Getter
  private enum Tile {
    VERTICAL('|', new Direction[]{Direction.NORTH, Direction.SOUTH}) {
      @Override
      public boolean connectsNorth() {
        return true;
      }

      @Override
      public boolean connectsSouth() {
        return true;
      }
    },
    HORIZONTAL('-', new Direction[]{Direction.EAST, Direction.WEST}) {
      @Override
      public boolean connectsEast() {
        return true;
      }

      @Override
      public boolean connectsWest() {
        return true;
      }
    },
    NORTH_EAST('L', new Direction[]{Direction.NORTH, Direction.EAST}) {
      @Override
      public boolean connectsNorth() {
        return true;
      }

      @Override
      public boolean connectsEast() {
        return true;
      }
    },
    NORTH_WEST('J', new Direction[]{Direction.NORTH, Direction.WEST}) {
      @Override
      public boolean connectsNorth() {
        return true;
      }

      @Override
      public boolean connectsWest() {
        return true;
      }
    },
    SOUTH_WEST('7', new Direction[]{Direction.SOUTH, Direction.WEST}) {
      @Override
      public boolean connectsSouth() {
        return true;
      }

      @Override
      public boolean connectsWest() {
        return true;
      }
    },
    SOUTH_EAST('F', new Direction[]{Direction.SOUTH, Direction.EAST}) {
      @Override
      public boolean connectsEast() {
        return true;
      }

      @Override
      public boolean connectsSouth() {
        return true;
      }
    },
    GROUND('.', null),
    START('S', null);

    private final char label;
    private final Set<Direction> directions;

    Tile(char label, Direction[] directions) {
      this.label = label;
      if (directions != null) {
        this.directions = new HashSet<>(directions.length);
        this.directions.addAll(Arrays.asList(directions));
      } else {
        this.directions = new HashSet<>();
      }
    }

    boolean isStart() {
      return this == Tile.START;
    }

    @Override
    public String toString() {
      return String.valueOf(label);
    }

    public static Tile fromLabel(char label) {
      for (var value : values()) {
        if (label == value.getLabel()) {
          return value;
        }
      }

      return null;
    }

    public static Tile fromDirections(HashSet<Direction> directions) {
      for (var value : values()) {
        if (directions.containsAll(value.getDirections())) {
          return value;
        }
      }

      return null;
    }

    public boolean connectsNorth() {
      return false;
    }

    public boolean connectsEast() {
      return false;
    }

    public boolean connectsSouth() {
      return false;
    }

    public boolean connectsWest() {
      return false;
    }
  }

  @Getter
  private static class MazeTile implements Comparable<MazeTile> {
    private final Tile tile;
    private final Point2D<Integer> point;

    public MazeTile(Tile tile, Point2D<Integer> point) {
      this.tile = tile;
      this.point = point;
    }

    public int getX() {
      return point.x();
    }

    public int getY() {
      return point.y();
    }

    public boolean isStartTile() {
      return tile.isStart();
    }

    public boolean connectsNorth() {
      return tile.connectsNorth();
    }

    public boolean connectsEast() {
      return tile.connectsEast();
    }

    public boolean connectsSouth() {
      return tile.connectsSouth();
    }

    public boolean connectsWest() {
      return tile.connectsWest();
    }

    public boolean connectsNorthTo(MazeTile other) {
      return connectsNorth() && other.connectsSouth() && getY() == other.getY() + 1
          && getX() == other.getX();
    }

    public boolean connectsEastTo(MazeTile other) {
      return connectsEast() && other.connectsWest() && getY() == other.getY()
          && getX() == other.getX() - 1;
    }

    public boolean connectsSouthTo(MazeTile other) {
      return connectsSouth() && other.connectsNorth() && getY() == other.getY() - 1
          && getX() == other.getX();
    }

    public boolean connectsWestTo(MazeTile other) {
      return connectsWest() && other.connectsEast() && getY() == other.getY()
          && getX() == other.getX() + 1;
    }

    public boolean connectsTo(MazeTile other) {
      return connectsNorthTo(other)
          || connectsSouthTo(other)
          || connectsWestTo(other)
          || connectsEastTo(other);
    }

    public List<MazeTile> getNeighbors(Grid2D<MazeTile> grid) {
      return grid.getAllNeighbors(getPoint())
          .stream()
          .map(Pair::second)
          .filter(this::connectsTo)
          .toList();
    }

    @Override
    public String toString() {
      return "MazeTile{"
          + "tile=" + tile
          + ", x=" + getX()
          + ", y=" + getY()
          + '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof MazeTile mazeTile)) {
        return false;
      }
      return getX() == mazeTile.getX() && getY() == mazeTile.getY();
    }

    @Override
    public int hashCode() {
      return Objects.hash(getX(), getY());
    }

    @Override
    public int compareTo(MazeTile o) {
      if (getY() != o.getY()) {
        return Integer.compare(getY(), o.getY());
      } else {
        return Integer.compare(getX(), o.getX());
      }
    }

  }

  private static class StartMazeTile extends MazeTile {
    public StartMazeTile(Tile tile, Point2D<Integer> point) {
      super(tile, point);
    }

    @Override
    public boolean isStartTile() {
      return true;
    }

    public static StartMazeTile fromDirections(
        HashSet<Direction> directions,
        Point2D<Integer> point
    ) {
      return new StartMazeTile(Tile.fromDirections(directions), point);
    }

  }

  private final Grid2D<MazeTile> grid;
  private final MazeTile startTile;

  private PipeMaze(@NotNull Grid2D<MazeTile> grid, MazeTile startTile) {
    this.grid = grid.copy();
    this.startTile = startTile;
  }

  /**
   * Builds and returns a new PipeMaze loaded from the specified List of Strings.
   *
   * @param input The List of Strings to parse in to a PipeMaze.
   * @return A new PipeMaze loaded from the specified List of Strings.
   */
  public static PipeMaze buildPipeMaze(List<String> input) {
    final var maze = new ArrayList<MazeTile>();
    final var mazeSize = loadMaze(input, maze);
    final var mazeTotalSize = mazeSize.first() * mazeSize.second();
    var grid = new Grid2D<>(
        mazeSize.first(), mazeSize.second(), maze.toArray(new MazeTile[mazeTotalSize]));

    final var startTile = findStartingTile(maze);
    final var startPipeTile = determineStartingTileType(startTile, grid);
    grid.set(startPipeTile.getPoint(), startPipeTile);

    return new PipeMaze(grid, startPipeTile);
  }

  private static Pair<Integer, Integer> loadMaze(List<String> input, List<MazeTile> maze) {
    var rows = new AtomicInteger(0);
    var columns = new AtomicInteger(0);
    var first = new AtomicBoolean(true);

    input.forEach(line -> {
      maze.addAll(loadMazeRow(line, rows.getAndIncrement()));
      if (first.get()) {
        columns.set(maze.size());
        first.set(false);
      }
    });

    return new Pair<>(columns.get(), rows.get());
  }

  private static List<MazeTile> loadMazeRow(String line, int row) {
    var column = new AtomicInteger(0);
    var list = new ArrayList<MazeTile>();
    line.chars()
        .mapToObj(it -> (char) it)
        .map(Tile::fromLabel)
        .forEach(it -> list.add(new MazeTile(it, new Point2D<>(column.getAndIncrement(), row))));
    return list;
  }

  private static MazeTile findStartingTile(List<MazeTile> maze) {
    for (var tile : maze) {
      if (tile.isStartTile()) {
        return tile;
      }
    }

    throw new RuntimeException("Unable to find the starting tile!");
  }

  private static MazeTile determineStartingTileType(MazeTile startTile, Grid2D<MazeTile> grid) {
    var directions = new HashSet<Direction>();
    var startPoint = startTile.getPoint();

    // Check north, west, east, and south of the start tile to find two tiles that connect to
    // the starting tile
    if (startTile.getY() > 0 && grid.get(Grid2D.pointInDirection(startPoint, Direction.NORTH))
        .connectsSouth()) {
      directions.add(Direction.NORTH);
    }
    if (startTile.getX() > 0 && grid.get(Grid2D.pointInDirection(startPoint, Direction.WEST))
        .connectsEast()) {
      directions.add(Direction.WEST);
    }
    if (startTile.getY() < grid.rows() - 1
        && grid.get(Grid2D.pointInDirection(startPoint, Direction.SOUTH)).connectsNorth()) {
      directions.add(Direction.SOUTH);
    }
    if (startTile.getX() < grid.columns() - 1
        && grid.get(Grid2D.pointInDirection(startPoint, Direction.EAST)).connectsWest()) {
      directions.add(Direction.EAST);
    }

    return StartMazeTile.fromDirections(directions, startPoint);
  }

  /**
   * Calculates the distance from the starting tile to every other tile connected to it in
   * the same loop! Returns a Map of the points in the loop and their distances from the
   * starting tile. Please note that the point for the starting tile has a distance
   * of zero (0).
   *
   * @param bfs If true, a BFS Search is performed; otherwise a recursive DFS search is used.
   * @return A Map of the points in the loop and their distances from the starting tile.
   */
  public Map<Point2D<Integer>, Integer> calculateDistances(boolean bfs) {
    if (bfs) {
      return calculateDistancesBfs(startTile, grid).entrySet()
          .stream()
          .collect(Collectors.toMap(it -> it.getKey().point, Map.Entry::getValue));
    } else {
      final var distances = new HashMap<MazeTile, Integer>();
      calculateDistancesDfs(startTile, 0, distances, grid);
      return distances.entrySet()
          .stream()
          .collect(Collectors.toMap(it -> it.getKey().point, Map.Entry::getValue));
    }
  }

  /**
   * Calculates and returns the number of tiles enclosed by the specified loop. The loop to pass
   * to this method can be obtained by calling calculateDistances.
   *
   * @param distances The Map of Point2Ds and their distances from the start tile.
   * @return The number of tiles enclosed by the specified loop.
   */
  public long calculateTilesEnclosedInLoop(@NotNull Map<Point2D<Integer>, Integer> distances) {
    return calculateTilesEnclosedInLoop(
        distances.entrySet()
            .stream()
            .collect(Collectors.toMap(it -> grid.get(it.getKey()), Map.Entry::getValue)),
        grid
    );
  }

  private static long calculateTilesEnclosedInLoop(
      @NotNull Map<MazeTile, Integer> mainLoop,
      @NotNull Grid2D<MazeTile> grid
  ) {
    final var directions = Set.of(Tile.VERTICAL, Tile.NORTH_EAST, Tile.NORTH_WEST);

    return Stream.of(grid.items())
        .filter(it -> !mainLoop.containsKey(it))
        .filter(tile -> {
          // We use ray casting to determine if a point is inside or outside the polygon formed by
          // the main loop. We start at the top and cast a ray in the WEST direction and then count
          // how many points on the polygon it hits.
          // Since we are casting from the top we only have to check VERTICAL, NORTH_EAST, and
          // NORTH_WEST directions.
          // Using the Jordan Curve Theorem, an even number of crossings means the point is outside
          // the polygon!
          var ray = new Range<>(0, tile.getX());
          return (mainLoop.keySet() // Check the tile against the tiles in the main loop!
              .stream()
              // The tile we are checking and the loop tile must be on the same row
              // and the loop tile's column must be in range of the ray we are casting!
              .filter(loopTile -> tile.getY() == loopTile.getY() && ray.contains(loopTile.getX()))
              // The loop tile must be in the proper direction!
              .filter(loopTile -> directions.contains(grid.get(loopTile.getPoint()).getTile()))
              .count() % 2) == 1;
        })
        .count();
  }

  /**
   * We will use Dijkstra's shortest path algorithm to calculate the distances from the specified
   * starting tile.
   *
   * @return A List of the distances for the tiles in the path from the starting tile.
   */
  private static Map<MazeTile, Integer> calculateDistancesBfs(
      @NotNull final MazeTile startTile,
      @NotNull final Grid2D<MazeTile> grid
  ) {
    final var queue = new PriorityQueue<MazeTile>();
    final var distances = new HashMap<MazeTile, Integer>();

    // Initialize our queue and distances
    queue.offer(startTile);
    distances.put(startTile, 0);

    while (!queue.isEmpty()) {
      final var current = queue.poll();
      if (current == null) {
        continue;
      }

      final var distance = distances.get(current);
      final var neighbors = current.getNeighbors(grid);

      for (final var neighbor : neighbors) {
        var distanceToNeighbor = distance;
        distanceToNeighbor++;
        if (distanceToNeighbor < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
          distances.put(neighbor, distanceToNeighbor);
          queue.add(neighbor);
        }
      }
    }

    return distances;
  }

  private void calculateDistancesDfs(
      @NotNull final MazeTile tile,
      final int level,
      @NotNull final Map<MazeTile, Integer> distances,
      @NotNull final Grid2D<MazeTile> grid
  ) {
    distances.put(tile, level);

    final var neighbors = tile.getNeighbors(grid);
    final var distance = distances.get(tile);

    for (var neighbor : neighbors) {
      var distanceToNeighbor = distance;
      distanceToNeighbor++;
      if (distanceToNeighbor < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
        calculateDistancesDfs(neighbor, distanceToNeighbor, distances, grid);
      }
    }
  }
}
