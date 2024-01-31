package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.grid.GardenSteps;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 21: Step Counter ---<br><br>
 * You manage to catch the airship right as it's dropping someone else off on their
 * all-expenses-paid trip to Desert Island! It even helpfully drops you off near the gardener
 * and his massive farm.
 *
 * <p><br>
 * "You got the sand flowing again! Great work! Now we just need to wait until we have enough
 * sand to filter the water for Snow Island, and we'll have snow again in no time."
 *
 * <p><br>
 * While you wait, one of the Elves that works with the gardener heard how good you are at solving
 * problems and would like your help. He needs to get his steps in for the day, and so he'd like to
 * know which garden plots he can reach with exactly his remaining 64 steps.
 *
 * <p><br>
 * He gives you an up-to-date map (your puzzle input) of his starting position (S), garden plots
 * (.), and rocks (#). For example:
 *
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##..#.<br>
 * ..#.#...#..<br>
 * ....#.#....<br>
 * .##..S####.<br>
 * .##..#...#.<br>
 * .......##..<br>
 * .##.#.####.<br>
 * .##..##.##.<br>
 * ...........<br>
 * </code>
 *
 * <p><br>
 * The Elf starts at the starting position (S) which also counts as a garden plot. Then, he can
 * take one step north, south, east, or west, but only onto tiles that are garden plots. This
 * would allow him to reach any of the tiles marked O:
 *
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##..#.<br>
 * ..#.#...#..<br>
 * ....#O#....<br>
 * .##.OS####.<br>
 * .##..#...#.<br>
 * .......##..<br>
 * .##.#.####.<br>
 * .##..##.##.<br>
 * ...........<br>
 * </code>
 *
 * <p><br>
 * Then, he takes a second step. Since at this point he could be at either tile marked O, his
 * second step would allow him to reach any garden plot that is one step north, south, east,
 * or west of any tile that he could have reached after the first step:
 *
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##..#.<br>
 * ..#.#O..#..<br>
 * ....#.#....<br>
 * .##O.O####.<br>
 * .##.O#...#.<br>
 * .......##..<br>
 * .##.#.####.<br>
 * .##..##.##.<br>
 * ...........<br>
 * </code>
 *
 * <p><br>
 * After two steps, he could be at any of the tiles marked O above, including the starting
 * position (either by going north-then-south or by going west-then-east).
 *
 * <p><br>
 * A single third step leads to even more possibilities:
 *
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##..#.<br>
 * ..#.#.O.#..<br>
 * ...O#O#....<br>
 * .##.OS####.<br>
 * .##O.#...#.<br>
 * ....O..##..<br>
 * .##.#.####.<br>
 * .##..##.##.<br>
 * ...........<br>
 * </code>
 *
 * <p><br>
 * He will continue like this until his steps for the day have been exhausted. After a total
 * of 6 steps, he could reach any of the garden plots marked O:
 *
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##.O#.<br>
 * .O#O#O.O#..<br>
 * O.O.#.#.O..<br>
 * .##O.O####.<br>
 * .##.O#O..#.<br>
 * .O.O.O.##..<br>
 * .##.#.####.<br>
 * .##O.##.##.<br>
 * ...........<br>
 * </code>
 *
 * <p><br>
 * In this example, if the Elf's goal was to get exactly 6 more steps today, he could use them
 * to reach any of 16 garden plots.
 *
 * <p><br>
 * However, the Elf actually needs to get 64 steps today, and the map he's handed you is much
 * larger than the example map.
 *
 * <p><br>
 * Starting from the garden plot marked S on your map, how many garden plots could the Elf
 * reach in exactly 64 steps?
 *
 * <p><br>
 * Your puzzle answer was 3740.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * The Elf seems confused by your answer until he realizes his mistake: he was reading from
 * a list of his favorite numbers that are both perfect squares and perfect cubes, not his
 * step counter.
 *
 * <p><br>
 * The actual number of steps he needs to get today is exactly 26501365.
 *
 * <p><br>
 * He also points out that the garden plots and rocks are set up so that the map repeats
 * infinitely in every direction.
 *
 * <p><br>
 * So, if you were to look one additional map-width or map-height out from the edge of the
 * example map above, you would find that it keeps repeating:
 *
 * <p><br>
 * <code>
 * .................................<br>
 * .....###.#......###.#......###.#.<br>
 * .###.##..#..###.##..#..###.##..#.<br>
 * ..#.#...#....#.#...#....#.#...#..<br>
 * ....#.#........#.#........#.#....<br>
 * .##...####..##...####..##...####.<br>
 * .##..#...#..##..#...#..##..#...#.<br>
 * .......##.........##.........##..<br>
 * .##.#.####..##.#.####..##.#.####.<br>
 * .##..##.##..##..##.##..##..##.##.<br>
 * .................................<br>
 * .................................<br>
 * .....###.#......###.#......###.#.<br>
 * .###.##..#..###.##..#..###.##..#.<br>
 * ..#.#...#....#.#...#....#.#...#..<br>
 * ....#.#........#.#........#.#....<br>
 * .##...####..##..S####..##...####.<br>
 * .##..#...#..##..#...#..##..#...#.<br>
 * .......##.........##.........##..<br>
 * .##.#.####..##.#.####..##.#.####.<br>
 * .##..##.##..##..##.##..##..##.##.<br>
 * .................................<br>
 * .................................<br>
 * .....###.#......###.#......###.#.<br>
 * .###.##..#..###.##..#..###.##..#.<br>
 * ..#.#...#....#.#...#....#.#...#..<br>
 * ....#.#........#.#........#.#....<br>
 * .##...####..##...####..##...####.<br>
 * .##..#...#..##..#...#..##..#...#.<br>
 * .......##.........##.........##..<br>
 * .##.#.####..##.#.####..##.#.####.<br>
 * .##..##.##..##..##.##..##..##.##.<br>
 * .................................<br>
 * </code>
 *
 * <p><br>
 * This is just a tiny three-map-by-three-map slice of the inexplicably-infinite farm layout;
 * garden plots and rocks repeat as far as you can see. The Elf still starts on the one middle
 * tile marked S, though - every other repeated S is replaced with a normal garden plot (.).
 *
 * <p><br>
 * Here are the number of reachable garden plots in this new infinite version of the example
 * map for different numbers of steps:
 *
 * <p><br>
 * <code>
 * In exactly 6 steps, he can still reach 16 garden plots.<br>
 * In exactly 10 steps, he can reach any of 50 garden plots.<br>
 * In exactly 50 steps, he can reach 1594 garden plots.<br>
 * In exactly 100 steps, he can reach 6536 garden plots.<br>
 * In exactly 500 steps, he can reach 167004 garden plots.<br>
 * In exactly 1000 steps, he can reach 668697 garden plots.<br>
 * In exactly 5000 steps, he can reach 16733044 garden plots.<br>
 * </code>
 *
 * <p><br>
 * However, the step count the Elf needs is much larger! Starting from the garden plot marked
 * S on your infinite map, how many garden plots could the Elf reach in exactly 26501365 steps?
 *
 * <p><br>
 * Your puzzle answer was 620962518745459.
 */
public class Day21 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day21.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day21() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_21-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = walk(input, 64, false);
    var end = Instant.now();

    LOGGER.info("The number of garden plots reached in 64 steps is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var answer = walk(input, 26_501_365, true);
    var end = Instant.now();

    LOGGER.info("The number of garden plots reached in 26,501,365 steps is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  /**
   * Take a walk through the garden. Walks the specified number of steps. If virtual is true,
   * the garden is expanded to be an infinite garden. Returns the number of garden plots that
   * can be reached by walking the specified number of steps.
   *
   * @param input   The List of Strings to parse into a garden.
   * @param steps   The number of steps to take.
   * @param virtual If true, the garden is expanded to be an infinite garden.
   * @return The number of garden plots that can be reached by walking the specified number
   *     of steps.
   */
  public long walk(List<String> input, long steps, boolean virtual) {
    var garden = GardenSteps.buildGardenSteps(input);
    return garden.walk(steps, virtual);
  }
}
