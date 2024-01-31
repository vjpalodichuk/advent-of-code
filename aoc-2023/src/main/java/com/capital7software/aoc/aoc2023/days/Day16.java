package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.grid.LightBeam;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 16: The Floor Will Be Lava ---<br><br>
 * With the beam of light completely focused somewhere, the reindeer leads you deeper still into
 * the Lava Production Facility. At some point, you realize that the steel facility walls have been
 * replaced with cave, and the doorways are just cave, and the floor is cave, and you're pretty
 * sure this is actually just a giant cave.
 *
 * <p><br>
 * Finally, as you approach what must be the heart of the mountain, you see a bright light in a
 * cavern up ahead. There, you discover that the beam of light you so carefully focused is
 * emerging from the cavern wall closest to the facility and pouring all of its energy into a
 * contraption on the opposite side.
 *
 * <p><br>
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
 * Your puzzle answer was 7632.
 *
 * <p><br>
 * --- Part Two ---<br><br>
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
 *
 * <p><br>
 * Your puzzle answer was 8023.
 */
public class Day16 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day16.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day16() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_16-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = numberOfEnergizedTilesTopLeftHeadingRight(input, false);
    var end = Instant.now();

    LOGGER.info("The number of energized tiles starting from the top-left is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var answer = maximumNumberOfEnergizedTiles(input);
    var end = Instant.now();

    LOGGER.info("The maximum number of energized tiles is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the number of energized tiles starting from the top-left.
   *
   * @param input     The List of Strings to parse.
   * @param recursive If true, uses recursion to count the energized tiles.
   * @return The number of energized tiles starting from the top-left.
   */
  public long numberOfEnergizedTilesTopLeftHeadingRight(List<String> input, boolean recursive) {
    var beam = LightBeam.buildLightBeam(input);
    return beam.energize(recursive);
  }

  /**
   * Returns the maximum number of tiles that can be energized by the light beam.
   *
   * @param input The List of Strings to parse.
   * @return The maximum number of tiles that can be energized by the light beam.
   */
  public long maximumNumberOfEnergizedTiles(List<String> input) {
    var beam = LightBeam.buildLightBeam(input);
    return beam.maxEnergized();
  }
}
