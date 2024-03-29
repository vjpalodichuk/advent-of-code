package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.geometry.Present;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 2: I Was Told There Would Be No Math ---<br><br>
 * The elves are running low on wrapping paper, and so they need to submit an order for more.
 * They have a list of the dimensions (length l, width w, and height h) of each present, and
 * only want to order exactly as much as they need.
 *
 * <p><br>
 * Fortunately, every present is a box (a perfect right rectangular prism), which makes
 * calculating the required wrapping paper for each gift a little easier: find the surface area
 * of the box, which is 2*l*w + 2*w*h + 2*h*l. The elves also need a little extra paper for each
 * present: the area of the smallest side.
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         A present with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square feet of wrapping
 *         paper plus 6 square feet of slack, for a total of 58 square feet.
 *     </li>
 *     <li>
 *         A present with dimensions 1x1x10 requires 2*1 + 2*10 + 2*10 = 42 square feet of
 *         wrapping paper plus 1 square foot of slack, for a total of 43 square feet.
 *     </li>
 * </ul>
 * Part 1: All numbers in the elves' list are in feet. How many total square feet of wrapping
 * paper should they order?
 *
 * <p><br>
 * Your puzzle answer was 1606483.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * The elves are also running low on ribbon. Ribbon is all the same width, so they only have to
 * worry about the length they need to order, which they would again like to be exact.
 *
 * <p><br>
 * The ribbon required to wrap a present is the shortest distance around its sides, or the
 * smallest perimeter of any one face. Each present also requires a bow made out of ribbon as well;
 * the feet of ribbon required for the perfect bow is equal to the cubic feet of volume of the
 * present. Don't ask how they tie the bow, though; they'll never tell.
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the present
 *         plus 2*3*4 = 24 feet of ribbon for the bow, for a total of 34 feet.
 *     </li>
 *     <li>
 *         A present with dimensions 1x1x10 requires 1+1+1+1 = 4 feet of ribbon to wrap the present
 *         plus 1*1*10 = 10 feet of ribbon for the bow, for a total of 14 feet.
 *     </li>
 * </ul>
 * How many total feet of ribbon should they order?
 *
 * <p><br>
 * Your puzzle answer was 3842356.
 */
public class Day02 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day02.class);

  /**
   * Instantiates the solution instance.
   */
  public Day02() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_02-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var presents = loadPresents(input);
    var total = howMuchTotalWrappingPaper(presents);
    var end = Instant.now();

    LOGGER.info("The elves' need to order {} square feet of wrapping paper!", total);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var presents = loadPresents(input);
    var total = howMuchTotalRibbon(presents);
    var end = Instant.now();

    LOGGER.info("The elves' need to order {} square feet of ribbon!", total);
    logTimings(LOGGER, start, end);
  }

  /**
   * Parses the specified input and returns it as a List of Presents.
   *
   * @param input The input to parse.
   * @return A List of Presents.
   */
  public List<Present> loadPresents(List<String> input) {
    return Present.parse(input);
  }

  /**
   * Returns the total amount of wrapping paper the Elves need.
   *
   * @param presents The number of presents to wrap.
   * @return The total amount of wrapping paper the Elves need.
   */
  public long howMuchTotalWrappingPaper(List<Present> presents) {
    return presents
      .stream()
      .mapToLong(Present::calculateNeededPaper)
      .sum();
  }

  /**
   * Returns the total amount of ribbon the Elves need.
   *
   * @param presents The number of presents to put a bow on.
   * @return The total amount of ribbon the Elves need.
   */
  public long howMuchTotalRibbon(List<Present> presents) {
    return presents
      .stream()
      .mapToLong(Present::calculateNeededRibbon)
      .sum();
  }
}
