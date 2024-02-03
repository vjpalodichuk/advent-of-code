package com.capital7software.aoc.aoc2016aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.grid.InfiniteGrid;
import com.capital7software.aoc.lib.grid.TaxiCab;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * --- Day 1: No Time for a Taxicab ---<br><br>
 * Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's
 * oscillator is regulated by stars. Unfortunately, the stars have been stolen... by the
 * Easter Bunny. To save Christmas, Santa needs you to retrieve all fifty stars by December 25th.
 *
 * <p><br>
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the
 * Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle
 * grants one star. Good luck!
 *
 * <p><br>
 * You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near",
 * unfortunately, is as close as you can get - the instructions on the Easter Bunny
 * Recruiting Document the Elves intercepted start here, and nobody had time to work
 * them out further.
 *
 * <p><br>
 * *The Document indicates that you should start at the given coordinates (where you just
 * landed) and face* **North**. Then, follow the provided sequence: either turn left (L) or
 * right (R) 90 degrees, then walk forward the given number of blocks, ending at a new
 * intersection.
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
 * Your puzzle answer was 209.
 *
 * <p><br>
 * --- Part Two ---<br><br>
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
 * <p><br>
 * Your puzzle answer was 136.
 */
@Slf4j
public class Day01 implements AdventOfCodeSolution {
  /**
   * Instantiates a new and empty instance.
   */
  public Day01() {
    super();
  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_01-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    input.forEach(it -> {
      var start = Instant.now();
      var answer = distanceFromStart(it);
      var end = Instant.now();

      log.info("{} steps away from the origin!", answer);
      logTimings(log, start, end);
    });
  }

  @Override
  public void runPart2(List<String> input) {
    input.forEach(it -> {
      var start = Instant.now();
      var answer = distanceFirstLocationVisitedTwice(it);
      var end = Instant.now();

      log.info("First location visited twice is {} steps away from the origin!", answer);
      logTimings(log, start, end);
    });
  }

  /**
   * Returns the Manhattan Distance between the starting point (0,0) and
   * the ending point after the instructions are followed.
   *
   * @param input The String that contains the instructions to parse and follow.
   * @return The Manhattan Distance between the starting point (0,0) and the ending
   *     point after the instructions are followed.
   */
  public Long distanceFromStart(String input) {
    var taxiCab = TaxiCab.buildTaxiCab(input);
    var result = taxiCab.followInstructions();
    return InfiniteGrid.manhattanDistance(result.first(), result.second());
  }

  /**
   * Returns the distance of the location first visited twice.
   *
   * @param input The String that contains the instructions to parse and follow.
   * @return The distance of the location first visited twice.
   */
  public Long distanceFirstLocationVisitedTwice(String input) {
    var taxiCab = TaxiCab.buildTaxiCab(input);
    var result = taxiCab.firstLocationVisitedTwice();
    return InfiniteGrid.manhattanDistance(result.first(), result.second());
  }
}
