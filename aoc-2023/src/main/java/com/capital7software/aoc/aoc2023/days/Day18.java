package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.geometry.Lagoon;
import com.capital7software.aoc.lib.geometry.Point2D;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 18: Lavaduct Lagoon ---<br><br>
 * Thanks to your efforts, the machine parts factory is one of the first factories up and running
 * since the lavafall came back. However, to catch up with the large backlog of parts requests,
 * the factory will also need a large supply of lava for a while; the Elves have already started
 * creating a large lagoon nearby for this purpose.
 *
 * <p><br>
 * However, they aren't sure the lagoon will be big enough; they've asked you to take a look at
 * the dig plan (your puzzle input). For example:
 *
 * <p><br>
 * <code>
 * R 6 (#70c710)<br>
 * D 5 (#0dc571)<br>
 * L 2 (#5713f0)<br>
 * D 2 (#d2c081)<br>
 * R 2 (#59c680)<br>
 * D 2 (#411b91)<br>
 * L 5 (#8ceee2)<br>
 * U 2 (#caa173)<br>
 * L 1 (#1b58a2)<br>
 * U 2 (#caa171)<br>
 * R 2 (#7807d2)<br>
 * U 3 (#a77fa3)<br>
 * L 2 (#015232)<br>
 * U 2 (#7a21e3)<br>
 * </code>
 *
 * <p><br>
 * The digger starts in a 1-meter cube hole in the ground. They then dig the specified
 * number of meters up (U), down (D), left (L), or right (R), clearing full 1-meter
 * cubes as they go. The directions are given as seen from above, so if "up" were north,
 * then "right" would be east, and so on. Each trench is also listed with the color that
 * the edge of the trench should be painted as an RGB hexadecimal color code.
 *
 * <p><br>
 * When viewed from above, the above example dig plan would result in the following loop
 * of trench (#) having been dug out from otherwise ground-level terrain (.):
 *
 * <p><br>
 * <code>
 * #######<br>
 * #.....#<br>
 * ###...#<br>
 * ..#...#<br>
 * ..#...#<br>
 * ###.###<br>
 * #...#..<br>
 * ##..###<br>
 * .#....#<br>
 * .######<br>
 * </code>
 *
 * <p><br>
 * At this point, the trench could contain 38 cubic meters of lava. However, this is just
 * the edge of the lagoon; the next step is to dig out the interior so that it is one meter
 * deep as well:
 *
 * <p><br>
 * <code>
 * #######<br>
 * #######<br>
 * #######<br>
 * ..#####<br>
 * ..#####<br>
 * #######<br>
 * #####..<br>
 * #######<br>
 * .######<br>
 * .######<br>
 * </code>
 *
 * <p><br>
 * Now, the lagoon can contain a much more respectable 62 cubic meters of lava. While the interior
 * is dug out, the edges are also painted according to the color codes in the dig plan.
 *
 * <p><br>
 * The Elves are concerned the lagoon won't be large enough; if they follow their dig plan,
 * how many cubic meters of lava could it hold?
 *
 * <p><br>
 * Your puzzle answer was 34329.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * The Elves were right to be concerned; the planned lagoon would be much too small.
 *
 * <p><br>
 * After a few minutes, someone realizes what happened; someone swapped the color and instruction
 * parameters when producing the dig plan. They don't have time to fix the bug; one of them asks
 * if you can extract the correct instructions from the hexadecimal codes.
 *
 * <p><br>
 * Each hexadecimal code is six hexadecimal digits long. The first five hexadecimal digits encode
 * the distance in meters as a five-digit hexadecimal number. The last hexadecimal digit encodes
 * the direction to dig: 0 means R, 1 means D, 2 means L, and 3 means U.
 *
 * <p><br>
 * So, in the above example, the hexadecimal codes can be converted into the true instructions:
 *
 * <p><br>
 * <code>
 * #70c710 = R 461937<br>
 * #0dc571 = D 56407<br>
 * #5713f0 = R 356671<br>
 * #d2c081 = D 863240<br>
 * #59c680 = R 367720<br>
 * #411b91 = D 266681<br>
 * #8ceee2 = L 577262<br>
 * #caa173 = U 829975<br>
 * #1b58a2 = L 112010<br>
 * #caa171 = D 829975<br>
 * #7807d2 = L 491645<br>
 * #a77fa3 = U 686074<br>
 * #015232 = L 5411<br>
 * #7a21e3 = U 500254<br>
 * </code>
 *
 * <p><br>
 * Digging out this loop and its interior produces a lagoon that can hold an impressive
 * 952408144115 cubic meters of lava.
 *
 * <p><br>
 * Convert the hexadecimal color codes into the correct instructions; if the Elves follow this new
 * dig plan, how many cubic meters of lava could the lagoon hold?
 *
 * <p><br>
 * Your puzzle answer was 42617947302920.
 */
public class Day18 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day18.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day18() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_18-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = calculateArea(input, false);
    var end = Instant.now();

    LOGGER.info("The area of the dug out Lagoon is: {}",
                answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var answer = calculateArea(input, true);
    var end = Instant.now();

    LOGGER.info("The area of the dug out Lagoon is: {}",
                answer);
    logTimings(LOGGER, start, end);
  }

  /**
   * Calculates and returns the area of the Lagoon.
   *
   * @param input                The List of Strings that represent the instructions for
   *                             digging out the Lagoon.
   * @param colorsAsInstructions If true, the colors will be decoded as digging instructions.
   * @return The area of the Lagoon.
   */
  public double calculateArea(List<String> input, boolean colorsAsInstructions) {
    var lagoon = Lagoon.buildLagoon(new Point2D<>(0.0, 0.0), input, colorsAsInstructions);

    return lagoon.calculateArea();
  }
}
