package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.LineSegment2D;
import com.capital7software.aoc.lib.geometry.Point2D;
import com.capital7software.aoc.lib.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

/**
 * You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near",
 * unfortunately, is as close as you can get - the instructions on the Easter Bunny
 * Recruiting Document the Elves intercepted start here, and nobody had time to work
 * them out further.
 *
 * <p><br>
 * The Document indicates that you should start at the given coordinates (where you just
 * landed) and face <b>North</b>. Then, follow the provided sequence:
 * either turn left (L) or right (R) 90 degrees, then walk forward the given number of blocks,
 * ending at a new intersection.
 *
 * <p><br>
 * There's no time to follow such ridiculous instructions on foot, though, so you take
 * a moment and work out the destination. Given that you can only walk on the street
 * grid of the city, how far is the shortest path to the destination?
 *
 * <p><br>
 * For example:
 * <ul>
 *   <li>
 *     Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
 *   </li>
 *   <li>
 *     R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
 *   </li>
 *   <li>
 *     R5, L5, R5, R3 leaves you 12 blocks away.
 *   </li>
 * </ul>
 * How many blocks away is Easter Bunny HQ?
 *
 * <p><br>
 * Then, you notice the instructions continue on the back of the Recruiting Document. Easter
 * Bunny HQ is actually at the first location you visit twice.
 *
 * <p><br>
 * For example, if your instructions are R8, R4, R4, R8, the first location you visit twice
 * is 4 blocks away, due East.
 *
 * <p><br>
 * How many blocks away is the first location you visit twice?

 *
 * @param instructions The List of instructions to follow.
 */
public record TaxiCab(List<Pair<Character, Long>> instructions) {

  /**
   * Instantiates a new TaxiCab with the specified List of instructions.
   *
   * @param instructions The List of instructions to follow.
   */
  public TaxiCab(List<Pair<Character, Long>> instructions) {
    this.instructions = new ArrayList<>(instructions);
  }

  /**
   * Builds and returns a new TaxiCab instance loaded with the instructions parsed from
   * the specified input String.
   *
   * @param input The instructions to parse.
   * @return A new TaxiCab instance loaded with the instructions parsed from
   *     the specified input String.
   */
  public static TaxiCab buildTaxiCab(@NotNull String input) {
    return new TaxiCab(
        Arrays.stream(input.split(", "))
            .map(it -> new Pair<>(it.charAt(0), Long.parseLong(it.substring(1))))
            .toList()
    );
  }

  /**
   * Returns an unmodifiable copy of the instruction loaded in this TaxiCab.
   *
   * @return An unmodifiable copy of the instruction loaded in this TaxiCab.
   */
  public List<Pair<Character, Long>> instructions() {
    return Collections.unmodifiableList(instructions);
  }

  /**
   * Follows the instructions starting at point 0,0 and facing North. Returns a Pair where the
   * first property contains a Point2D that is the starting point and the second property
   * contains a [Point2D] that is the last point after following all the instructions.
   *
   * <p><br>
   * The InfiniteGrid contains methods like InfiniteGrid.manhattanDistance that can be used
   * to calculate the distance between the two returned points.
   *
   * @return A Pair where the first property contains a Point2D that is the starting
   *     point and the second property contains a Point2D that is the last point after
   *     following all the instructions.
   */
  public Pair<Point2D<Long>, Point2D<Long>> followInstructions() {
    var start = start();
    var directionRef = new AtomicReference<>(Direction.NORTH);
    var currentRef = new AtomicReference<>(start);

    instructions.forEach(instruction -> {
      directionRef.set(instruction.first() == 'L'
                           ? directionRef.get().getLeft() : directionRef.get().getRight());
      currentRef.set(new Point2D<>(
          currentRef.get().x() + (directionRef.get().dx() * instruction.second()),
          currentRef.get().y() + (directionRef.get().dy() * instruction.second())
      ));
    });

    return new Pair<>(start, currentRef.get());
  }

  /**
   * Follows the instructions starting at point 0,0 and facing North. Returns a Pair where the
   * first property contains a Point2D that is the starting point and the second property
   * contains a [Point2D] that is the last point after following all the instructions.
   *
   * <p><br>
   * The InfiniteGrid contains methods like InfiniteGrid.manhattanDistance that can be used
   * to calculate the distance between the two returned points.
   *
   * @return A Pair where the first property contains a Point2D that is the starting
   *     point and the second property contains a Point2D that is the last point after
   *     following all the instructions.
   */
  public Pair<Point2D<Long>, Point2D<Long>> firstLocationVisitedTwice() {
    var start = start();
    var directionRef = new AtomicReference<>(Direction.NORTH);
    var currentRef = new AtomicReference<>(start);
    var visited = new LinkedHashSet<LineSegment2D<Long>>();

    for (var instruction : instructions) {
      directionRef.set(instruction.first() == 'L'
                           ? directionRef.get().getLeft() : directionRef.get().getRight());
      var newPoint = new Point2D<>(
          currentRef.get().x() + (directionRef.get().dx() * instruction.second()),
          currentRef.get().y() + (directionRef.get().dy() * instruction.second())
      );
      var newLine = new LineSegment2D<>(
          InfiniteGrid.pointInDirection(currentRef.get(), directionRef.get()),
          newPoint
      );

      // Check if this line intersects with any other line and
      // if so, find the point of intersection
      var intersectionPoint = findIntersectionPoint(newLine, visited);

      if (intersectionPoint != null) {
        currentRef.set(intersectionPoint);
        break;
      }

      visited.add(newLine);
      currentRef.set(newPoint);
    }

    return new Pair<>(start, currentRef.get());
  }

  private Point2D<Long> findIntersectionPoint(
      LineSegment2D<Long> newLine,
      Collection<LineSegment2D<Long>> existingLines
  ) {
    Point2D<Long> result = null;

    for (var line : existingLines) {
      var point = LineSegment2D.intersect(line, newLine);

      if (point != null) {
        result = point;
        break;
      }
    }

    return result;
  }

  private Point2D<Long> start() {
    return new Point2D<>(0L, 0L);
  }
}
