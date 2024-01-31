package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.CosmicExpansion;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 11: Cosmic Expansion ---<br><br>
 * You continue following signs for "Hot Springs" and eventually come across an observatory.
 * The Elf within turns out to be a researcher studying cosmic expansion using the giant
 * telescope here.
 *
 * <p><br>
 * He doesn't know anything about the missing machine parts; he's only visiting for this research
 * project. However, he confirms that the hot springs are the next-closest area likely to have
 * people; he'll even take you straight there once he's done with today's observation analysis.
 *
 * <p><br>
 * Maybe you can help him with the analysis to speed things up?
 *
 * <p><br>
 * The researcher has collected a bunch of data and compiled the data into a single giant image
 * (your puzzle input). The image includes empty space (.) and galaxies (#). For example:
 *
 * <p><br>
 * <code>
 * ...#......<br>
 * .......#..<br>
 * #.........<br>
 * ..........<br>
 * ......#...<br>
 * .#........<br>
 * .........#<br>
 * ..........<br>
 * .......#..<br>
 * #...#.....<br>
 * </code>
 *
 * <p><br>
 * The researcher is trying to figure out the sum of the lengths of the shortest
 * path between every pair of galaxies. However, there's a catch: the universe
 * expanded in the time it took the light from those galaxies to reach the observatory.
 *
 * <p><br>
 * Due to something involving gravitational effects, only some space expands.
 * In fact, the result is that any rows or columns that contain no galaxies should all
 * actually be twice as big.
 *
 * <p><br>
 * In the above example, three columns and two rows contain no galaxies:
 *
 * <p><br>
 * <code>
 * v  v  v<br>
 * ...#......<br>
 * .......#..<br>
 * #.........<br>
 * &#62;..........&#60;<br>
 * ......#...<br>
 * .#........<br>
 * .........#<br>
 * &#62;..........&#60;<br>
 * .......#..<br>
 * #...#.....<br>
 * ^  ^  ^<br>
 * </code>
 *
 * <p><br>
 * These rows and columns need to be twice as big; the result of cosmic expansion therefore
 * looks like this:
 *
 * <p><br>
 * <code>
 * ....#........<br>
 * .........#...<br>
 * #............<br>
 * .............<br>
 * .............<br>
 * ........#....<br>
 * .#...........<br>
 * ............#<br>
 * .............<br>
 * .............<br>
 * .........#...<br>
 * #....#.......<br>
 * </code>
 *
 * <p><br>
 * Equipped with this expanded universe, the shortest path between every pair of galaxies
 * can be found. It can help to assign every galaxy a unique number:
 *
 * <p><br>
 * <code>
 * ....1........<br>
 * .........2...<br>
 * 3............<br>
 * .............<br>
 * .............<br>
 * ........4....<br>
 * .5...........<br>
 * ............6<br>
 * .............<br>
 * .............<br>
 * .........7...<br>
 * 8....9.......<br>
 * </code>
 *
 * <p><br>
 * In these 9 galaxies, there are 36 pairs. Only count each pair once; order within the pair
 * doesn't matter. For each pair, find any shortest path between the two galaxies using only
 * steps that move up, down, left, or right exactly one . or # at a time. (The shortest path
 * between two galaxies is allowed to pass through another galaxy.)
 *
 * <p><br>
 * For example, here is one of the shortest paths between galaxies 5 and 9:
 *
 * <p><br>
 * <code>
 * ....1........<br>
 * .........2...<br>
 * 3............<br>
 * .............<br>
 * .............<br>
 * ........4....<br>
 * .5...........<br>
 * .##.........6<br>
 * ..##.........<br>
 * ...##........<br>
 * ....##...7...<br>
 * 8....9.......<br>
 * </code>
 *
 * <p><br>
 * This path has length 9 because it takes a minimum of nine steps to get from galaxy 5 to galaxy
 * 9 (the eight locations marked # plus the step onto galaxy 9 itself). Here are some other
 * examples of shortest path lengths:
 *
 * <p><br>
 * <code>
 * Between galaxy 1 and galaxy 7: 15<br>
 * Between galaxy 3 and galaxy 6: 17<br>
 * Between galaxy 8 and galaxy 9: 5<br>
 * </code>
 *
 * <p><br>
 * In this example, after expanding the universe, the sum of the shortest path between all
 * 36 pairs of galaxies is 374.
 *
 * <p><br>
 * Expand the universe, then find the length of the shortest path between every pair of galaxies.
 * What is the sum of these lengths?
 *
 * <p><br>
 * Your puzzle answer was 9686930.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * The galaxies are much older (and thus much farther apart) than the researcher initially
 * estimated.
 *
 * <p><br>
 * Now, instead of the expansion you did before, make each empty row or column one million
 * times larger. That is, each empty row should be replaced with 1000000 empty rows, and each
 * empty column should be replaced with 1000000 empty columns.
 *
 * <p><br>
 * (In the example above, if each empty row or column were merely 10 times larger, the sum
 * of the shortest paths between every pair of galaxies would be 1030. If each empty row or
 * column were merely 100 times larger, the sum of the shortest paths between every pair of
 * galaxies would be 8410. However, your universe will need to expand far beyond these values.)
 *
 * <p><br>
 * Starting with the same initial image, expand the universe according to these new rules,
 * then find the length of the shortest path between every pair of galaxies. What is the sum
 * of these lengths?
 *
 * <p><br>
 * Your puzzle answer was 630728425490.
 */
public class Day11 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day11.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day11() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_11-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    int gapFactor = 1;
    runParts(input, start, gapFactor);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    int gapFactor = 999_999;
    runParts(input, start, gapFactor);
  }

  private void runParts(List<String> input, Instant start, int gapFactor) {
    var answer = sumOfAllPairsShortestPath(input, gapFactor, false);
    var end = Instant.now();

    LOGGER.info("With a Gap Factor of {} the sum of all Galaxy pairs shortest path is: {}",
                gapFactor, answer);
    logTimings(LOGGER, start, end);

    start = Instant.now();
    answer = sumOfAllPairsShortestPath(input, gapFactor, true);
    end = Instant.now();

    LOGGER.info(
        "With a Gap Factor of {} the sum of optimized all Galaxy pairs shortest path is: {}",
        gapFactor, answer
    );
    logTimings(LOGGER, start, end);
  }

  /**
   * Calculates and returns the sum of the shortest paths between all pairs of Galaxies.
   *
   * @param input     The List of Strings that contain the Galaxies to parse.
   * @param gapFactor The number of additional spaces to count for each gap encountered.
   * @param optimized If true, uses a time and space efficient algorithm to calculate the
   *                  distances.
   * @return The sum of the shortest paths between all pairs of Galaxies!
   */
  public long sumOfAllPairsShortestPath(List<String> input, int gapFactor, boolean optimized) {
    var cosmos = CosmicExpansion.buildUniverse(input);

    return cosmos.sumOfAllPairsShortestPath(gapFactor, optimized);
  }
}
