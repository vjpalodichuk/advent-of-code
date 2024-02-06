package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;


/**
 * Upon closer inspection, the contraption appears to be a flat, two-dimensional square grid
 * containing empty space (.), mirrors (/ and \), and splitters (| and -).
 *
 * <p><br>
 * The contraption is aligned so that most of the beam bounces around the grid, but each tile
 * on the grid converts some of the beam's light into heat to melt the rock in the cavern.
 *
 * <p><br>
 * You note the layout of the contraption (your puzzle input). For example:
 *
 * <p><br>
 * <code>
 * .|...\....<br>
 * |.-.\.....<br>
 * .....|-...<br>
 * ........|.<br>
 * ..........<br>
 * .........\<br>
 * ..../.\\..<br>
 * .-.-/..|..<br>
 * .|....-|.\<br>
 * ..//.|....<br>
 * </code>
 *
 * <p><br>
 * The beam enters the top-left corner from the left and heading to the right. Then,
 * its behavior depends on what it encounters as it moves:
 * <ul>
 *     <li>
 *         If the beam encounters empty space (.), it continues in the same direction.
 *     </li>
 *     <li>
 *         If the beam encounters a mirror (/ or \), the beam is reflected 90 degrees
 *         depending on the angle of the mirror. For instance, a rightward-moving beam
 *         that encounters a / mirror would continue upward in the mirror's column,
 *         while a rightward-moving beam that encounters a \ mirror would continue
 *         downward from the mirror's column.
 *     </li>
 *     <li>
 *         If the beam encounters the pointy end of a splitter (| or -), the beam passes
 *         through the splitter as if the splitter were empty space. For instance, a
 *         rightward-moving beam that encounters a - splitter would continue in the same direction.
 *     </li>
 *     <li>
 *         If the beam encounters the flat side of a splitter (| or -), the beam is split
 *         into two beams going in each of the two directions the splitter's pointy ends are
 *         pointing. For instance, a rightward-moving beam that encounters a | splitter would
 *         split into two beams: one that continues upward from the splitter's column and one
 *         that continues downward from the splitter's column.
 *     </li>
 *     <li>
 *         Beams do not interact with other beams; a tile can have many beams passing through
 *         it at the same time. A tile is energized if that tile has at least one beam pass
 *         through it, reflect in it, or split in it.
 *     </li>
 * </ul>
 * In the above example, here is how the beam of light bounces around the contraption:
 *
 * <p><br>
 * <code>
 * &#62;|&#60;&#60;&#60;\....<br>
 * |v-.\^....<br>
 * .v...|-&#62;&#62;&#62;<br>
 * .v...v^.|.<br>
 * .v...v^...<br>
 * .v...v^..\<br>
 * .v../2\\..<br>
 * &#60;-&#62;-/vv|..<br>
 * .|&#60;&#60;&#60;2-|.\<br>
 * .v//.|.v..<br>
 * </code>
 *
 * <p><br>
 * Beams are only shown on empty tiles; arrows indicate the direction of the beams.
 * If a tile contains beams moving in multiple directions, the number of distinct
 * directions is shown instead. Here is the same diagram but instead only showing
 * whether a tile is energized (#) or not (.):
 *
 * <p><br>
 * <code>
 * ######....<br>
 * .#...#....<br>
 * .#...#####<br>
 * .#...##...<br>
 * .#...##...<br>
 * .#...##...<br>
 * .#..####..<br>
 * ########..<br>
 * .#######..<br>
 * .#...#.#..<br>
 * </code>
 *
 * <p><br>
 * Ultimately, in this example, 46 tiles become energized.
 *
 * <p><br>
 * The light isn't energizing enough tiles to produce lava; to debug the contraption,
 * you need to start by analyzing the current situation. With the beam starting in the
 * top-left heading right, how many tiles end up being energized?
 *
 * <p><br>
 * As you try to work out what might be wrong, the reindeer tugs on your shirt and leads
 * you to a nearby control panel. There, a collection of buttons lets you align the
 * contraption so that the beam enters from any edge tile and heading away from that edge.
 * (You can choose either of two directions for the beam if it starts on a corner; for
 * instance, if the beam starts in the bottom-right corner, it can start heading either
 * left or upward.)
 *
 * <p><br>
 * So, the beam could start on any tile in the top row (heading downward), any tile in
 * the bottom row (heading upward), any tile in the leftmost column (heading right), or
 * any tile in the rightmost column (heading left). To produce lava, you need to find the
 * configuration that energizes as many tiles as possible.
 *
 * <p><br>
 * In the above example, this can be achieved by starting the beam in the fourth tile from
 * the left in the top row:
 *
 * <p><br>
 * <code>
 * .|&#60;2&#60;\....<br>
 * |v-v\^....<br>
 * .v.v.|-&#62;&#62;&#62;<br>
 * .v.v.v^.|.<br>
 * .v.v.v^...<br>
 * .v.v.v^..\<br>
 * .v.v/2\\..<br>
 * &#60;-2-/vv|..<br>
 * .|&#60;&#60;&#60;2-|.\<br>
 * .v//.|.v..<br>
 * </code>
 *
 * <p><br>
 * Using this configuration, 51 tiles are energized:
 *
 * <p><br>
 * <code>
 * .#####....<br>
 * .#.#.#....<br>
 * .#.#.#####<br>
 * .#.#.##...<br>
 * .#.#.##...<br>
 * .#.#.##...<br>
 * .#.#####..<br>
 * ########..<br>
 * .#######..<br>
 * .#...#.#..<br>
 * </code>
 *
 * <p><br>
 * Find the initial beam configuration that energizes the largest number of tiles; how many
 * tiles are energized in that configuration?
 */
public class LightBeam {
  private enum Entity {
    EMPTY_SPACE('.') {
      public List<Direction> nextHeading(Direction heading) {
        switch (heading) {
          case NORTH -> {
            return NORTH;
          }
          case EAST -> {
            return EAST;
          }
          case SOUTH -> {
            return SOUTH;
          }
          case WEST -> {
            return WEST;
          }
          default -> throw new RuntimeException("Unknown heading: " + heading);
        }
      }
    },
    UPWARD_MIRROR('/') {
      public List<Direction> nextHeading(Direction heading) {
        switch (heading) {
          case NORTH -> {
            return EAST;
          }
          case EAST -> {
            return NORTH;
          }
          case SOUTH -> {
            return WEST;
          }
          case WEST -> {
            return SOUTH;
          }
          default -> throw new RuntimeException("Unknown heading: " + heading);
        }
      }
    },
    DOWNWARD_MIRROR('\\') {
      public List<Direction> nextHeading(Direction heading) {
        switch (heading) {
          case NORTH -> {
            return WEST;
          }
          case EAST -> {
            return SOUTH;
          }
          case SOUTH -> {
            return EAST;
          }
          case WEST -> {
            return NORTH;
          }
          default -> throw new RuntimeException("Unknown heading: " + heading);
        }
      }
    },
    VERTICAL_SPLITTER('|') {
      public List<Direction> nextHeading(Direction heading) {
        switch (heading) {
          case NORTH -> {
            return NORTH;
          }
          case EAST, WEST -> {
            return SPLIT_VERTICAL;
          }
          case SOUTH -> {
            return SOUTH;
          }
          default -> throw new RuntimeException("Unknown heading: " + heading);
        }
      }
    },
    HORIZONTAL_SPLITTER('-') {
      public List<Direction> nextHeading(Direction heading) {
        switch (heading) {
          case NORTH, SOUTH -> {
            return SPLIT_HORIZONTAL;
          }
          case EAST -> {
            return EAST;
          }
          case WEST -> {
            return WEST;
          }
          default -> throw new RuntimeException("Unknown heading: " + heading);
        }
      }
    };

    private static final List<Direction> SPLIT_VERTICAL = List.of(Direction.NORTH, Direction.SOUTH);
    private static final List<Direction> SPLIT_HORIZONTAL = List.of(Direction.WEST, Direction.EAST);
    private static final List<Direction> NORTH = List.of(Direction.NORTH);
    private static final List<Direction> EAST = List.of(Direction.EAST);
    private static final List<Direction> SOUTH = List.of(Direction.SOUTH);
    private static final List<Direction> WEST = List.of(Direction.WEST);
    private static final List<Direction> EMPTY = new ArrayList<>();

    private final char label;

    Entity(char label) {
      this.label = label;
    }

    public static Entity from(char label) {
      for (var value : values()) {
        if (value.label == label) {
          return value;
        }
      }

      throw new RuntimeException("Unknown label:" + label);
    }

    public List<Direction> nextHeading(Direction heading) {
      return EMPTY;
    }

    @Override
    public String toString() {
      return String.valueOf(label);
    }
  }

  private record Tile(Entity entity, Point2D<Integer> point) {
    public static Tile from(char label, int column, int row) {
      return new Tile(Entity.from(label), new Point2D<>(column, row));
    }

    public List<Direction> nextHeading(Direction direction) {
      return entity.nextHeading(direction);
    }
  }

  private record Beam(Grid2d<Tile> grid, Point2D<Integer> startPoint, Direction startHeading) {

    private static Beam create(Grid2d<Tile> grid) {
      return new Beam(grid, new Point2D<>(0, 0), Direction.EAST);
    }

    public static Beam buildBeam(List<String> input) {
      var tiles = new ArrayList<Tile>();
      var columns = new AtomicInteger(0);
      var rows = new AtomicInteger(0);
      var first = new AtomicBoolean(true);

      input.forEach(line -> {
        if (first.get()) {
          columns.set(line.length());
          first.set(false);
        }
        var rowTiles = new ArrayList<Tile>(line.length());

        if (parseRow(line, rowTiles, rows.get())) {
          rows.incrementAndGet();
          tiles.addAll(rowTiles);
          rowTiles.clear();
        }
      });

      return Beam.create(
          new Grid2d<>(
              columns.get(), rows.get(), tiles.toArray(new Tile[columns.get() * rows.get()]))
      );
    }

    private static boolean parseRow(String line, ArrayList<Tile> rowTiles, int row) {
      if (line == null || line.isBlank()) {
        return false;
      }

      var chars = line.toCharArray();

      for (var column = 0; column < chars.length; column++) {
        rowTiles.add(Tile.from(chars[column], column, row));
      }

      return !rowTiles.isEmpty();
    }

    private List<? extends Map.Entry<Tile, Direction>> getNext(Tile tile, Direction heading) {
      var directions = tile.nextHeading(heading);
      var result = new ArrayList<Map.Entry<Tile, Direction>>(directions.size());

      directions.forEach(direction -> {
        var nextPoint = Grid2d.pointInDirection(tile.point, direction);
        if (grid.isOnGrid(nextPoint)) {
          result.add(new AbstractMap.SimpleEntry<>(grid.get(nextPoint), direction));
        }
      });

      return result;
    }

    public int maxEnergized() {
      final Set<Integer> max = ConcurrentHashMap.newKeySet();
      try (var pool = new ForkJoinPool(32)) {
        var tasks = new ArrayList<ForkJoinTask<Integer>>(4 * (grid().size()));

        for (int i = 0; i < grid.columns(); i++) {
          int finalI = i;
          tasks.add(pool.submit(() -> {
            max.add(energize(finalI, 0, Direction.SOUTH, new HashMap<>()));
            max.add(energize(finalI, grid().rows() - 1, Direction.NORTH, new HashMap<>()));
            return 0;
          }));
        }

        for (int i = 0; i < grid.rows(); i++) {
          int finalI = i;
          tasks.add(pool.submit(() -> {
            max.add(energize(0, finalI, Direction.EAST, new HashMap<>()));
            max.add(energize(grid().columns() - 1, finalI, Direction.WEST, new HashMap<>()));
            return 0;
          }));
        }
        tasks.forEach(it -> {
          try {
            it.get();
          } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
          }
        });
        pool.shutdown();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return max.stream().max(Integer::compareTo).orElse(0);
    }

    public int energize() {
      return energize(startPoint.x(), startPoint.y(), startHeading, new HashMap<>());
    }

    private int energize(
        int column,
        int row,
        Direction heading,
        Map<Tile, Set<Direction>> visited
    ) {
      // get the starting tile!
      var tile = grid.get(column, row);

      // We will perform the traversal iteratively since we run out of stack space
      // performing it recursively
      var queue = new LinkedList<Map.Entry<Tile, Direction>>();
      queue.offer(new AbstractMap.SimpleEntry<>(tile, heading));

      while (queue.peek() != null) {
        var entry = queue.poll();
        var currentTile = entry.getKey();
        var currentHeading = entry.getValue();
        var visits = visited.computeIfAbsent(currentTile, it -> new HashSet<>());

        if (visits.contains(currentHeading)) {
          continue; // We already visited this tile from this direction!
        }

        // We visit this tile and then get the next heading(s) and continue
        visits.add(currentHeading);

        getNext(currentTile, currentHeading).forEach(queue::offer);
      }

      return visited.size();
    }

    public int energizeRecursive() {
      var tile = grid.get(startPoint);
      var visited = new HashMap<Tile, Set<Direction>>();
      traverseRecursive(tile, startHeading, visited);

      return visited.size();
    }

    private void traverseRecursive(
        Tile tile,
        Direction heading,
        Map<Tile, Set<Direction>> visited
    ) {
      // Really cannot use on large grids unless the stack space is increased
      // Stopping conditions first
      var visits = visited.computeIfAbsent(tile, it -> new HashSet<>(Direction.values().length));

      if (visits.contains(heading)) {
        return; // We already visited this tile from this direction!
      }

      // We visit this tile and then get the next heading(s) and continue
      visits.add(heading);

      getNext(tile, heading).forEach(it -> traverseRecursive(it.getKey(), it.getValue(), visited));
    }
  }

  private final Beam beam;

  private LightBeam(@NotNull Beam beam) {
    this.beam = beam;
  }

  /**
   * Builds and returns a new LightBeam loaded with the layout from the specified List of
   * Strings to parse.
   *
   * @param input The List of Strings to parse.
   * @return A new LightBeam loaded with the layout from the specified List of Strings to parse.
   */
  public static LightBeam buildLightBeam(List<String> input) {
    return new LightBeam(Beam.buildBeam(input));
  }

  /**
   * Energizes the tiles using the light beam starting at the top-left and continuing East.
   * Returns the number of tiles energized from that starting position.
   *
   * @param recursive If true, the calculation is done recursively instead of iteratively.
   * @return The number of tiles energized from that starting position.
   */
  public int energize(boolean recursive) {
    if (recursive) {
      return beam.energizeRecursive();
    } else {
      return beam.energize();
    }
  }

  /**
   * Analyses the entire layout and returns the maximum number of tiles that can be energized.
   *
   * @return The maximum number of tiles that can be energized.
   */
  public int maxEnergized() {
    return beam.maxEnergized();
  }
}
