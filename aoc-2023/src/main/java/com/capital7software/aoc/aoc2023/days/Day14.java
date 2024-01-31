package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.ParabolicReflectorDish;
import com.capital7software.aoc.lib.geometry.Direction;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 14: Parabolic Reflector Dish ---<br><br>
 * You reach the place where all the mirrors were pointing: a massive parabolic reflector dish
 * attached to the side of another large mountain.
 *
 * <p><br>
 * The dish is made up of many small mirrors, but while the mirrors themselves are roughly in
 * the shape of a parabolic reflector dish, each individual mirror seems to be pointing in
 * slightly the wrong direction. If the dish is meant to focus light, all it's doing right
 * now is sending it in a vague direction.
 *
 * <p><br>
 * This system must be what provides the energy for the lava! If you focus the reflector
 * dish, maybe you can go where it's pointing and use the light to fix the lava production.
 *
 * <p><br>
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
 * Your puzzle answer was 106648.
 *
 * <p><br>
 * --- Part Two ---<br><br>
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
 *
 * <p><br>
 * Your puzzle answer was 87700.
 */
public class Day14 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day14.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day14() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_14-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = tiltAndCalculateLoadOnNorthSupports(input);
    var end = Instant.now();

    LOGGER.info("After tilting the platform North, the load on the North Supports is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var spinCount = 1_000_000_000;
    var start = Instant.now();
    var answer = spinAndCalculateLoadOnNorthSupports(input, spinCount);
    var end = Instant.now();

    LOGGER.info("After {} spin cycles the load on the North Supports is: {}",
                spinCount, answer);
    logTimings(LOGGER, start, end);
  }

  /**
   * Creates a new ParabolicReflectorDish from the specified List of Strings and then Calculates
   * and returns the load on the North support beams.
   *
   * @param input The List of Strings to parse.
   * @return The load on the North support beams.
   */
  public long tiltAndCalculateLoadOnNorthSupports(List<String> input) {
    var parabolicReflectorDish = ParabolicReflectorDish.loadPlatform(input);
    parabolicReflectorDish.tilt(Direction.NORTH);

    return parabolicReflectorDish.calculateLoad(Direction.NORTH);
  }

  /**
   * Creates a new ParabolicReflectorDish from the specified List of Strings and then Calculates
   * and returns the load on the North support beams after performing the specified number of
   * spin cycles.
   *
   * @param input     The List of Strings to parse.
   * @param spinCount The number of spin cycles to perform.
   * @return The load on the North support beams after performing the specified number of
   *     spin cycles.
   */
  public long spinAndCalculateLoadOnNorthSupports(List<String> input, long spinCount) {
    var parabolicReflectorDish = ParabolicReflectorDish.loadPlatform(input);
    parabolicReflectorDish.spinCycle(spinCount);

    return parabolicReflectorDish.calculateLoad(Direction.NORTH);
  }
}
