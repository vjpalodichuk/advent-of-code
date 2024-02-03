package com.capital7software.aoc.aoc2023aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.grid.HikingTrails;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 23: A Long Walk ---<br><br>
 * The Elves resume water filtering operations! Clean water starts flowing over the edge of
 * Island Island.
 *
 * <p><br>
 * They offer to help you go over the edge of Island Island, too! Just hold on tight to one end
 * of this impossibly long rope and they'll lower you down a safe distance from the massive
 * waterfall you just created.
 *
 * <p><br>
 * As you finally reach Snow Island, you see that the water isn't really reaching the ground:
 * it's being absorbed by the air itself. It looks like you'll finally have a little downtime
 * while the moisture builds up to snow-producing levels. Snow Island is pretty scenic, even
 * without any snow; why not take a walk?
 *
 * <p><br>
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
 * In the example above, the longest hike you can take is marked with O, and your starting position
 * is marked S:
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
 * Your puzzle answer was 2278.
 *
 * <p><br>
 * --- Part Two ---<br><br>
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
 *
 * <p><br>
 * Your puzzle answer was 6734.
 */
public class Day23 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day23.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day23() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_23-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = stepsOfLongestTrail(input, false, true);
    var end = Instant.now();

    LOGGER.info("Total number of steps of the longest trail is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var answer = stepsOfLongestTrail(input, true, true);
    var end = Instant.now();

    LOGGER.info("Total number of steps of the longest trail ignoring slopes is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the number of steps of the longest hiking trail.
   *
   * @param input        The List of Strings to parse into the map of hiking paths.
   * @param ignoreSlopes If true, then slopes that restrict the walking direction are ignored.
   * @param virtualGrid  If true, then the grid is treated as being virtual.
   * @return The number of steps of the longest hiking trail.
   */
  public long stepsOfLongestTrail(List<String> input, boolean ignoreSlopes, boolean virtualGrid) {
    var hikingTrails = HikingTrails.buildHikingTrails(input, ignoreSlopes, virtualGrid);
    return hikingTrails.stepsOfLongestTrail();
  }
}
