package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.collection.PriorityQueue;
import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

/**
 * The crucibles are top-heavy and pushed by hand. Unfortunately, the crucibles become very
 * difficult to steer at high speeds, and so it can be hard to go in a straight line for very long.
 *
 * <p><br>
 * To get Desert Island the machine parts it needs as soon as possible, you'll need to find the
 * best way to get the crucible from the lava pool to the machine parts factory. To do this, you
 * need to minimize heat loss while choosing a route that doesn't require the crucible to go in a
 * straight line for too long.
 *
 * <p><br>
 * Fortunately, the Elves here have a map (your puzzle input) that uses traffic patterns, ambient
 * temperature, and hundreds of other parameters to calculate exactly how much heat loss can be
 * expected for a crucible entering any particular city block.
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * <code>
 * 2413432311323<br>
 * 3215453535623<br>
 * 3255245654254<br>
 * 3446585845452<br>
 * 4546657867536<br>
 * 1438598798454<br>
 * 4457876987766<br>
 * 3637877979653<br>
 * 4654967986887<br>
 * 4564679986453<br>
 * 1224686865563<br>
 * 2546548887735<br>
 * 4322674655533<br>
 * </code>
 *
 * <p><br>
 * Each city block is marked by a single digit that represents the amount of heat loss if the
 * crucible enters that block. The starting point, the lava pool, is the top-left city block;
 * the destination, the machine parts factory, is the bottom-right city block. (Because you already
 * start in the top-left block, you don't incur that block's heat loss unless you leave that block
 * and then return to it.)
 *
 * <p><br>
 * Because it is difficult to keep the top-heavy crucible going in a straight line for very long,
 * it can move at most three blocks in a single direction before it must turn 90 degrees left or
 * right. The crucible also can't reverse direction; after entering each city block, it may only
 * turn left, continue straight, or turn right.
 *
 * <p><br>
 * One way to minimize heat loss is this path:
 *
 * <p><br>
 * <code>
 * 2&#62;&#62;34^&#62;&#62;&#62;1323<br>
 * 32v&#62;&#62;&#62;35v5623<br>
 * 32552456v&#62;&#62;54<br>
 * 3446585845v52<br>
 * 4546657867v&#62;6<br>
 * 14385987984v4<br>
 * 44578769877v6<br>
 * 36378779796v><br>
 * 465496798688v<br>
 * 456467998645v<br>
 * 12246868655&#60;v<br>
 * 25465488877v5<br>
 * 43226746555v&#62;<br>
 * </code>
 *
 * <p><br>
 * This path never moves more than three consecutive blocks in the same direction and incurs a
 * heat loss of only 102.
 *
 * <p><br>
 * Directing the crucible from the lava pool to the machine parts factory, but not moving more
 * than three consecutive blocks in the same direction, what is the least heat loss it can incur?
 *
 * <p><br>
 * The crucibles of lava simply aren't large enough to provide an adequate supply of lava to the
 * machine parts factory. Instead, the Elves are going to upgrade to ultra crucibles.
 *
 * <p><br>
 * Ultra crucibles are even more difficult to steer than normal crucibles. Not only do they have
 * trouble going in a straight line, but they also have trouble turning!
 *
 * <p><br>
 * Once an ultra crucible starts moving in a direction, it needs to move a minimum of four blocks
 * in that direction before it can turn (or even before it can stop at the end). However, it will
 * eventually start to get wobbly: an ultra crucible can move a maximum of ten consecutive blocks
 * without turning.
 *
 * <p><br>
 * In the above example, an ultra crucible could follow this path to minimize heat loss:
 *
 * <p><br>
 * <code>
 * 2&#62;&#62;&#62;&#62;&#62;&#62;&#62;&#62;1323<br>
 * 32154535v5623<br>
 * 32552456v4254<br>
 * 34465858v5452<br>
 * 45466578v&#62;&#62;&#62;&#62;<br>
 * 143859879845v<br>
 * 445787698776v<br>
 * 363787797965v<br>
 * 465496798688v<br>
 * 456467998645v<br>
 * 122468686556v<br>
 * 254654888773v<br>
 * 432267465553v<br>
 * </code>
 *
 * <p><br>
 * In the above example, an ultra crucible would incur the minimum possible heat loss of 94.
 *
 * <p><br>
 * Here's another example:
 *
 * <p><br>
 * <code>
 * 111111111111<br>
 * 999999999991<br>
 * 999999999991<br>
 * 999999999991<br>
 * 999999999991<br>
 * </code>
 *
 * <p><br>
 * Sadly, an ultra crucible would need to take an unfortunate path like this one:
 *
 * <p><br>
 * <code>
 * 1&#62;&#62;&#62;&#62;&#62;&#62;&#62;1111<br>
 * 9999999v9991<br>
 * 9999999v9991<br>
 * 9999999v9991<br>
 * 9999999v&#62;&#62;&#62;&#62;<br>
 * </code>
 *
 * <p><br>
 * This route causes the ultra crucible to incur the minimum possible heat loss of 71.
 *
 * <p><br>
 * Directing the ultra crucible from the lava pool to the machine parts factory, what is the
 * least heat loss it can incur?
 */
public class ClumsyCrucible {
  private record Tile(int heat, Point2D<Integer> point) {
  }

  private static class Crucible {
    private record Path(@NotNull Tile tile, @NotNull Direction direction, int steps) {
      @Override
      public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (!(o instanceof Path path)) {
          return false;
        }
        return steps == path.steps && tile.equals(path.tile) && direction == path.direction;
      }

      @Override
      public int hashCode() {
        return Objects.hash(tile, steps, direction);
      }
    }

    private record PathCost(@NotNull Path path, long cost,
                            long distanceToGoal) implements Comparable<PathCost> {
      public long pathCost() {
        return cost;
      }

      public long stepsInDirection() {
        return path.steps();
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (!(o instanceof PathCost pathCost)) {
          return false;
        }
        return cost == pathCost.cost && distanceToGoal == pathCost.distanceToGoal
            && path.equals(pathCost.path);
      }

      @Override
      public int hashCode() {
        return Objects.hash(path, cost, distanceToGoal);
      }

      @Override
      public int compareTo(PathCost o) {
        return Long.compare(pathCost(), o.pathCost());
      }
    }

    private final Grid2d<Tile> grid;
    private final Tile start;
    private final Tile end;

    private final int minSteps;
    private final int maxSteps;

    private Crucible(
        @NotNull Grid2d<Tile> grid,
        @NotNull Tile start,
        @NotNull Tile end,
        int minSteps,
        int maxSteps
    ) {
      this.grid = grid;
      this.start = start;
      this.end = end;
      this.minSteps = minSteps;
      this.maxSteps = maxSteps;
    }

    public static Grid2d<Tile> parseGrid(List<String> input) {
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
      return new Grid2d<>(
          columns.get(),
          rows.get(),
          tiles.toArray(new Tile[columns.get() * rows.get()])
      );
    }

    private static boolean parseRow(String line, ArrayList<Tile> rowTiles, int row) {
      if (line == null || line.isBlank()) {
        return false;
      }

      var chars = line.toCharArray();

      for (int column = 0; column < chars.length; column++) {
        rowTiles.add(
            new Tile(Integer.parseInt(String.valueOf(chars[column])), new Point2D<>(column, row))
        );
      }

      return !rowTiles.isEmpty();
    }

    public long calculateMinimumHeatLoss() {
      var visited = new HashSet<Path>();

      var costs = new HashMap<Path, Long>();

      var queue = new PriorityQueue<PathCost>();

      queue.offer(
          new PathCost(
              new Path(
                  start,
                  Direction.EAST,
                  0
              ),
              0,
              grid.rows() + grid.columns()
          )
      );
      queue.offer(
          new PathCost(
              new Path(
                  start,
                  Direction.SOUTH,
                  0
              ),
              0,
              grid.rows() + grid.columns()
          )
      );

      while (!queue.isEmpty()) {
        var pathCost = queue.poll();

        if (pathCost == null) {
          continue;
        }

        // Stopping conditions
        if (pathCost.path().tile().equals(end) && pathCost.stepsInDirection() >= minSteps) {
          // We are at the end!
          return pathCost.cost();
        }

        if (visited.contains(pathCost.path())) {
          // We already have been here!
          continue;
        }

        visited.add(pathCost.path());

        // Get our neighbors!
        for (var neighbor : getNeighbors(pathCost)) {
          // Track how much of a heat loss the path will increase by
          var newCost = pathCost.cost() + neighbor.tile().heat();

          if (costs.computeIfAbsent(neighbor, it -> Long.MAX_VALUE) <= newCost) {
            continue; // New heat is greater than the existing heat for this path!!
          }

          costs.put(neighbor, newCost);

          var distanceToEnd = (end.point().x() - neighbor.tile().point().x())
              + (end.point().y() - neighbor.tile().point().y());

          // Add this path back to the queue!
          queue.offer(new PathCost(neighbor, newCost, distanceToEnd));
        }
      }

      return costs.keySet().stream().filter(it -> it.tile().equals(end))
          .mapToLong(costs::get)
          .min()
          .orElse(Long.MAX_VALUE);
    }

    private @NotNull List<Path> getNeighbors(@NotNull PathCost pathCost) {
      var neighbors = new ArrayList<Path>(Direction.values().length);

      if (pathCost.stepsInDirection() < minSteps) {
        var neighbor = takeStepForward(pathCost.path());
        if (neighbor != null) {
          neighbors.add(neighbor);
        }
      } else if (pathCost.stepsInDirection() < maxSteps) {
        var neighbor = takeStepForward(pathCost.path());
        if (neighbor != null) {
          neighbors.add(neighbor);
        }
        neighbor = takeStepRight(pathCost.path());
        if (neighbor != null) {
          neighbors.add(neighbor);
        }
        neighbor = takeStepLeft(pathCost.path());
        if (neighbor != null) {
          neighbors.add(neighbor);
        }
      } else {
        var neighbor = takeStepRight(pathCost.path());
        if (neighbor != null) {
          neighbors.add(neighbor);
        }
        neighbor = takeStepLeft(pathCost.path());
        if (neighbor != null) {
          neighbors.add(neighbor);
        }
      }

      return neighbors;
    }

    private Path takeStepForward(@NotNull Path path) {
      var point = path.tile().point();
      var direction = path.direction();
      var newPoint = Grid2d.pointInDirection(point, direction);
      if (grid.isOnGrid(newPoint)) {
        var newTile = grid.get(newPoint);
        return new Path(newTile, direction, path.steps() + 1);
      }

      return null;
    }

    private Path takeStepLeft(@NotNull Path path) {
      var point = path.tile().point();
      var direction = path.direction().getLeft();
      var newPoint = Grid2d.pointInDirection(point, direction);
      if (grid.isOnGrid(newPoint)) {
        var newTile = grid.get(newPoint);
        return new Path(newTile, direction, 1);
      }

      return null;
    }

    private Path takeStepRight(@NotNull Path path) {
      var point = path.tile().point();
      var direction = path.direction().getRight();
      var newPoint = Grid2d.pointInDirection(point, direction);
      if (grid.isOnGrid(newPoint)) {
        var newTile = grid.get(newPoint);
        return new Path(newTile, direction, 1);
      }

      return null;
    }
  }

  private final Crucible crucible;

  private ClumsyCrucible(Crucible crucible) {
    this.crucible = crucible;
  }

  /**
   * Builds and returns a new ClumsyCrucible where the crucible starts in the upper-left corner
   * and moves to the lower=right corner of the grid. The minimumSteps parameter controls the
   * minimum number of steps in the same direction that the crucible must travel before it can
   * turn left or right. The maximumSteps parameter controls the maximum number of steps in the
   * same direction that the crucible can travel before it must turn left or right.
   *
   * @param input    The List of Strings to parse that contains the map of the grid.
   * @param minSteps The number of steps in the same direction the crucible must travel
   *                 before it can turn.
   * @param maxSteps The number of steps in the same direction the crucible can travel
   *                 before it must turn.
   * @return A new ClumsyCrucible loaded with the specified grid and step settings.
   */
  public static @NotNull ClumsyCrucible buildClumsyCrucible(
      @NotNull List<String> input,
      int minSteps,
      int maxSteps
  ) {
    var grid = Crucible.parseGrid(input);
    var crucible = new Crucible(grid, grid.getFirst(), grid.getLast(), minSteps, maxSteps);

    return new ClumsyCrucible(crucible);
  }

  /**
   * Calculates the path for this crucible to take that results in the minimum loss of heat
   * and returns that value.
   *
   * @return The heat loss of the path with the minimum heat loss.
   */
  public long calculateMinimumHeatLoss() {
    return crucible.calculateMinimumHeatLoss();
  }
}
