package com.capital7software.aoc.lib.analysis;

import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import com.capital7software.aoc.lib.grid.Grid2d;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;


/**
 * Upon closer inspection, the individual mirrors each appear to be connected via an elaborate
 * system of ropes and pulleys to a large metal platform below the dish. The platform is covered
 * in large rocks of various shapes. Depending on their position, the weight of the rocks deforms
 * the platform, and the shape of the platform controls which ropes move and ultimately the
 * focus of the dish.
 *
 * <p><br>
 * In short: if you move the rocks, you can focus the dish. The platform even has a control
 * panel on the side that lets you tilt it in one of four directions! The rounded rocks (O)
 * will roll when the platform is tilted, while the cube-shaped rocks (#) will stay in place.
 * You note the positions of all the empty spaces (.) and rocks (your puzzle input). For example:
 *
 * <p><br>
 * <code>
 * O....#....<br>
 * O.OO#....#<br>
 * .....##...<br>
 * OO.#O....O<br>
 * .O.....O#.<br>
 * O.#..O.#.#<br>
 * ..O..#O..O<br>
 * .......O..<br>
 * #....###..<br>
 * #OO..#....<br>
 * </code>
 *
 * <p><br>
 * Start by tilting the lever so all of the rocks will slide north as far as they will go:
 *
 * <p><br>
 * <code>
 * OOOO.#.O..<br>
 * OO..#....#<br>
 * OO..O##..O<br>
 * O..#.OO...<br>
 * ........#.<br>
 * ..#....#.#<br>
 * ..O..#.O.O<br>
 * ..O.......<br>
 * #....###..<br>
 * #....#....<br>
 * </code>
 *
 * <p><br>
 * You notice that the support beams along the north side of the platform are damaged; to ensure
 * the platform doesn't collapse, you should calculate the total load on the north support beams.
 *
 * <p><br>
 * The amount of load caused by a single rounded rock (O) is equal to the number of rows from
 * the rock to the south edge of the platform, including the row the rock is on. (Cube-shaped
 * rocks (#) don't contribute to load.) So, the amount of load caused by each rock in each row
 * is as follows:
 *
 * <p><br>
 * <code>
 * OOOO.#.O.. 10<br>
 * OO..#....#  9<br>
 * OO..O##..O  8<br>
 * O..#.OO...  7<br>
 * ........#.  6<br>
 * ..#....#.#  5<br>
 * ..O..#.O.O  4<br>
 * ..O.......  3<br>
 * #....###..  2<br>
 * #....#....  1<br>
 * </code>
 *
 * <p><br>
 * The total load is the sum of the load caused by all the rounded rocks. In this example,
 * the total load is 136.
 *
 * <p><br>
 * Tilt the platform so that the rounded rocks all roll north. Afterward, what is the total load on
 * the north support beams?
 *
 * <p><br>
 * The parabolic reflector dish deforms, but not in a way that focuses the beam. To do that, you'll
 * need to move the rocks to the edges of the platform. Fortunately, a button on the side of the
 * control panel labeled "spin cycle" attempts to do just that!
 *
 * <p><br>
 * Each cycle tilts the platform four times so that the rounded rocks roll north, then west,
 * then south, then east. After each tilt, the rounded rocks roll as far as they can before
 * the platform tilts in the next direction. After one cycle, the platform will have finished
 * rolling the rounded rocks in those four directions in that order.
 *
 * <p><br>
 * Here's what happens in the example above after each of the first few cycles:
 *
 * <p><br>
 * After 1 cycle:
 *
 * <p><br>
 * <code>
 * .....#....<br>
 * ....#...O#<br>
 * ...OO##...<br>
 * .OO#......<br>
 * .....OOO#.<br>
 * .O#...O#.#<br>
 * ....O#....<br>
 * ......OOOO<br>
 * #...O###..<br>
 * #..OO#....<br>
 * </code>
 *
 * <p><br>
 * After 2 cycles:
 *
 * <p><br>
 * <code>
 * .....#....<br>
 * ....#...O#<br>
 * .....##...<br>
 * ..O#......<br>
 * .....OOO#.<br>
 * .O#...O#.#<br>
 * ....O#...O<br>
 * .......OOO<br>
 * #..OO###..<br>
 * #.OOO#...O<br>
 * </code>
 *
 * <p><br>
 * After 3 cycles:
 *
 * <p><br>
 * <code>
 * .....#....<br>
 * ....#...O#<br>
 * .....##...<br>
 * ..O#......<br>
 * .....OOO#.<br>
 * .O#...O#.#<br>
 * ....O#...O<br>
 * .......OOO<br>
 * #...O###.O<br>
 * #.OOO#...O<br>
 * </code>
 *
 * <p><br>
 * This process should work if you leave it running long enough, but you're still worried about the
 * north support beams. To make sure they'll survive for a while, you need to calculate the total
 * load on the north support beams after 1000000000 cycles.
 *
 * <p><br>
 * In the above example, after 1000000000 cycles, the total load on the north support beams is 64.
 *
 * <p><br>
 * Run the spin cycle for 1000000000 cycles. Afterward, what is the total load on the
 * north support beams?
 */
public class ParabolicReflectorDish {
  @Getter
  private enum Rock {
    ROUNDED_ROCK('O'),
    CUBE_ROCK('#'),
    EMPTY('.');

    private final char label;

    Rock(char label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return String.valueOf(label);
    }

    public static @NotNull Rock from(char label) {
      for (var value : values()) {
        if (value.getLabel() == label) {
          return value;
        }
      }

      throw new IllegalArgumentException("Unknown Rock character: " + label);
    }
  }

  private static class Tile {
    @Getter
    private final Rock rock;
    private Point2D<Integer> point;

    public Tile(@NotNull Rock rock, @NotNull Point2D<Integer> point) {
      this.rock = rock;
      this.point = point;
    }

    public @NotNull Point2D<Integer> getPoint() {
      return point;
    }

    public void setPoint(@NotNull Point2D<Integer> point) {
      this.point = point;
    }

    @Override
    public String toString() {
      return rock.toString();
    }

    public static Tile from(char label) {
      return from(label, -1, -1);
    }

    public static Tile from(char label, int column, int row) {
      return new Tile(Rock.from(label), new Point2D<>(column, row));
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Tile tile)) {
        return false;
      }
      return Objects.equals(point, tile.point) && getRock() == tile.getRock();
    }

    @Override
    public int hashCode() {
      return Objects.hash(getRock(), point);
    }
  }

  private record Platform(@NotNull Grid2d<Tile> grid) {

    public static @NotNull Platform parse(@NotNull List<String> input) {
      var tiles = new ArrayList<Tile>();

      var columns = new AtomicInteger(0);
      var rows = new AtomicInteger(0);

      var first = new AtomicBoolean(true);

      input.forEach(line -> {
        if (first.get()) {
          columns.set(line.length());
          first.set(false);
        }

        if (parseLine(line, tiles, rows.get())) {
          rows.incrementAndGet();
        }
      });

      return new Platform(
          new Grid2d<>(
              columns.get(),
              rows.get(),
              tiles.toArray(new Tile[columns.get() * rows.get()])
          )
      );
    }

    private static boolean parseLine(String line, @NotNull ArrayList<Tile> tiles, int row) {
      if (line == null || line.isBlank()) {
        return false;
      }

      var chars = line.toCharArray();
      for (int column = 0; column < chars.length; column++) {
        tiles.add(Tile.from(chars[column], column, row));
      }
      return true;
    }

    @Override
    public @NotNull String toString() {
      var builder = new StringBuilder();
      builder.append("Platform {");
      builder.append("\n\tColumns: ");
      builder.append(grid.columns());
      builder.append("\n\tRows: ");
      builder.append(grid.rows());
      builder.append("\n\tCurrent tile positions:");

      for (int i = 0; i < grid.rows(); i++) {
        builder.append("\n\t\t");
        for (int j = 0; j < grid.columns(); j++) {
          builder.append(grid.get(new Point2D<>(j, i)));
        }
        if (i + 1 < grid.rows()) {
          builder.append("\n");
        }
      }
      builder.append("\n}");

      return builder.toString();
    }

    public void tilt(@NotNull Direction direction) {
      var offset = direction.dx() + direction.dy();
      int loopOffset = getLoopOffset(direction);
      var extractor = getExtractor(direction);
      var setter = getSetter(direction);
      var predicate = getLoopPredicate(direction);
      var loopStart = getLoopStart(direction);

      // Go through each row / column depending on the direction
      for (var i = 0; i < grid.getRowOrColumnLength(direction); i++) {
        var items = grid.getRowOrColumn(direction, i);
        // Go through each item in the row / column and attempt to move them!
        moveRockTiles(loopStart, predicate, offset, loopOffset, extractor, setter, items);
      }
    }

    private void moveRockTiles(
        @NotNull Integer loopStart,
        @NotNull Predicate<Integer> predicate,
        int offset,
        int loopOffset,
        @NotNull Function<Tile, Integer> extractor,
        @NotNull BiConsumer<Tile, Integer> setter,
        @NotNull List<Tile> items
    ) {
      // To do this efficiently in a single pass per row / column, we have to keep
      // track of the previous space count.
      // For example, if the row looked like this:
      // . . O . . . O O . O . . . . O . #
      // The first rounded rock would have a value of 2, the next would have a value of
      // 5, then 5, then 6, then 10; the last rock is a Cube and cannot be moved.
      // This allows us to make a single pass through the list and update our grid accordingly!
      var currentSpaceCount = 0;
      var previousSpaceCount = 0;

      // If the currentRock is a Space just increment space count and move to the next rock!
      // If it is a rounded rock, we will add the currentSpaceCount to the previousSpaceCount
      // and then add the rounded rock to the map and reset the current space count.
      // If it is a cube rock, we simply reset the space counts
      for (var i = loopStart; predicate.test(i); i += loopOffset) {
        var tile = items.get(i);
        if (tile.getRock() == Rock.EMPTY) {
          currentSpaceCount++;
        } else if (tile.getRock() == Rock.ROUNDED_ROCK) {
          previousSpaceCount += currentSpaceCount;
          if (previousSpaceCount > 0) {
            // Only move it if it actually moved!!
            moveRockTile(tile, extractor, setter, offset, previousSpaceCount);
          }
          currentSpaceCount = 0;
        } else {
          currentSpaceCount = 0;
          previousSpaceCount = 0;
        }
      }

    }

    private void moveRockTile(
        @NotNull Tile tile,
        @NotNull Function<Tile, Integer> extractor,
        @NotNull BiConsumer<Tile, Integer> setter,
        @NotNull Integer offset,
        @NotNull Integer movedBy
    ) {
      // We first move the rock to its new spot then we put an empty rock in its old spot.
      var oldPoint = tile.getPoint();
      var newPos = extractor.apply(tile) + (offset * movedBy);
      // Update it's values
      setter.accept(tile, newPos);

      grid.set(tile.getPoint(), tile);
      grid.set(oldPoint, new Tile(Rock.EMPTY, oldPoint));
    }

    public @NotNull BiConsumer<Tile, Integer> getSetter(@NotNull Direction direction) {
      return switch (direction) {
        case NORTH, SOUTH ->
            (tile, newRow) -> tile.setPoint(new Point2D<>(tile.getPoint().x(), newRow));
        case EAST, WEST ->
            (tile, newColumn) -> tile.setPoint(new Point2D<>(newColumn, tile.getPoint().y()));
        default -> throw new RuntimeException("Unknown direction: " + direction);
      };
    }

    public @NotNull Function<Tile, Integer> getExtractor(@NotNull Direction direction) {
      return switch (direction) {
        case NORTH, SOUTH -> (it) -> it.getPoint().y();
        case EAST, WEST -> (it) -> it.getPoint().x();
        default -> throw new RuntimeException("Unknown direction: " + direction);
      };
    }

    public @NotNull Predicate<Integer> getLoopPredicate(@NotNull Direction direction) {
      return switch (direction) {
        case NORTH, WEST -> current -> current < grid.getRowOrColumnLength(direction);
        case EAST, SOUTH -> current -> current >= 0;
        default -> throw new RuntimeException("Unknown direction: " + direction);
      };
    }

    public @NotNull Integer getLoopStart(@NotNull Direction direction) {
      return switch (direction) {
        case NORTH, WEST -> 0;
        case EAST, SOUTH -> grid.getRowOrColumnLength(direction) - 1;
        default -> throw new RuntimeException("Unknown direction: " + direction);
      };
    }

    public @NotNull Integer getLoopOffset(@NotNull Direction direction) {
      return switch (direction) {
        case NORTH, WEST -> 1;
        case EAST, SOUTH -> -1;
        default -> throw new RuntimeException("Unknown direction: " + direction);
      };
    }

    public @NotNull Function<Tile, Integer> getLoadCalculator(@NotNull Direction direction) {
      switch (direction) {
        case NORTH -> {
          return (Tile tile) -> grid.getRowOrColumnLength(direction) - tile.getPoint().y();
        }
        case SOUTH -> {
          return (Tile tile) -> 1 + tile.getPoint().y();
        }
        case EAST -> {
          return (Tile tile) -> 1 + tile.getPoint().x();
        }
        case WEST -> {
          return (Tile tile) -> grid.getRowOrColumnLength(direction) - tile.getPoint().x();
        }
        default -> throw new RuntimeException("Unknown direction: " + direction);
      }
    }

    public long calculateLoad(@NotNull Direction direction) {
      long supportLoad;
      var loadCalculator = getLoadCalculator(direction);

      supportLoad = grid.stream()
          .filter(it -> it.getRock() == Rock.ROUNDED_ROCK)
          .mapToLong(it -> (long) loadCalculator.apply(it))
          .sum();

      return supportLoad;
    }

    /**
     * Spins the platform by tilting it NORTH, WEST, SOUTH, EAST up to the requested number
     * of times. After this method returns, the platform will be in a state as if it was spun
     * the requested number of times.
     *
     * @param cyclesToPerform The requested number of times to cycle the platform.
     */
    public void spinCycle(long cyclesToPerform) {
      var gridCache = new ArrayList<List<Tile>>();
      var detectedCycleIndex = 0L;
      var currentTiles = copyTiles();
      // Add the current state of the platform
      gridCache.add(currentTiles);

      var cycleDetected = false;
      var cycleCount = 0;
      while (!cycleDetected && cycleCount < cyclesToPerform) {
        currentTiles = cycle();
        cycleCount++;
        var index = gridCache.indexOf(currentTiles);

        if (index != -1) {
          detectedCycleIndex = index;
          cycleDetected = true;
        } else {
          gridCache.add(currentTiles);
        }
      }
      long calculatedIndex = cyclesToPerform;

      if (cycleDetected && calculatedIndex >= detectedCycleIndex) {
        // The end state has already been cached so calculate the index to it.
        // The index can be found using this formula:
        // index = indexOfFirstInstanceOfDuplicate +
        // ((cyclesToPerform - indexOfFirstInstanceOfDuplicate) %
        // (actualCyclesPerformed - indexOfFirstInstanceOfDuplicate))
        calculatedIndex = detectedCycleIndex
            + ((cyclesToPerform - detectedCycleIndex) % (cycleCount - detectedCycleIndex));
        gridCache.get((int) calculatedIndex)
            .forEach(it -> {

            });

        gridCache.get((int) calculatedIndex)
            .forEach(tile -> grid.set(tile.getPoint(), tile));
      }
    }

    private @NotNull List<Tile> cycle() {
      tilt(Direction.NORTH);
      tilt(Direction.WEST);
      tilt(Direction.SOUTH);
      tilt(Direction.EAST);

      return copyTiles();
    }

    private @NotNull List<Tile> copyTiles() {
      return grid.stream().map(it -> new Tile(it.getRock(), it.getPoint())).toList();
    }
  }

  private final Platform platform;

  private ParabolicReflectorDish(@NotNull Platform platform) {
    this.platform = platform;
  }

  /**
   * Builds and returns a new ParabolicReflectorDish loaded with the layout from the specified
   * List of Strings.
   *
   * @param input The layout of the platform and rocks to parse.
   * @return A new ParabolicReflectorDish loaded with the layout from the specified
   *     List of Strings.
   */
  public static @NotNull ParabolicReflectorDish loadPlatform(@NotNull List<String> input) {
    var platform = Platform.parse(input);

    return new ParabolicReflectorDish(platform);
  }

  /**
   * Tilts the platform in the specified Direction. All movable rocks are moved as far as
   * possible in the direction of the tilt.
   *
   * @param direction The Cardinal Direction to tilt the platform of this
   *                  ParabolicReflectorDish in.
   */
  public void tilt(@NotNull Direction direction) {
    platform.tilt(direction);
  }

  /**
   * The number of cycles to perform. When this method returns, this ParabolicReflectorDish
   * will be in a state after the last spin cycle. If a repeating cycle is detected, then the
   * spin cycle will be short-circuited but, the state of this ParabolicReflectorDish will be
   * in a state that is equivalent to having performed the requested number of spins.
   *
   * @param cyclesToPerform The number of spin cycles to perform.
   */
  public void spinCycle(long cyclesToPerform) {
    platform.spinCycle(cyclesToPerform);
  }

  /**
   * Calculates and returns the load on the specified support beams.
   *
   * @param direction The cardinal Direction to calculate the load on the beams of.
   * @return The load on the specified support beams.
   */
  public long calculateLoad(@NotNull Direction direction) {
    return platform.calculateLoad(direction);
  }
}
