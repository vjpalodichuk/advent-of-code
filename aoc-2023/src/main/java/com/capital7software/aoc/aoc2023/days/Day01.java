package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.string.Trebuchet;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 1: Trebuchet?! ---<br><br>
 * Something is wrong with global snow production, and you've been selected to take a look.
 * The Elves have even given you a map; on it, they've used stars to mark the top fifty locations
 * that are likely to be having problems.
 *
 * <p><br>
 * You've been doing this long enough to know that to restore snow operations,
 * you need to check all fifty stars by December 25th.
 *
 * <p><br>
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent
 * calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one
 * star. Good luck!
 *
 * <p><br>
 * You try to ask why they can't just use a weather machine ("not powerful enough") and where
 * they're even sending you ("the sky") and why your map looks mostly blank ("you sure ask a lot
 * of questions") and hang on did you just say the sky ("of course, where do you think snow comes
 * from") when you realize that the Elves are already loading you into a trebuchet ("please hold
 * still, we need to strap you in").
 *
 * <p><br>
 * As they're making the final adjustments, they discover that their calibration document
 * (your puzzle input) has been amended by a very young Elf who was apparently just excited
 * to show off her art skills. Consequently, the Elves are having trouble reading the values
 * on the document.
 *
 * <p><br>
 * The newly-improved calibration document consists of lines of text; each line originally
 * contained a specific calibration value that the Elves now need to recover. On each line,
 * the calibration value can be found by combining the first digit and the last digit
 * (in that order) to form a single two-digit number.
 *
 * <p><br>
 * For example:<br>
 * <ul>
 *     <li>
 *         1abc2
 *     </li>
 *     <li>
 *         pqr3stu8vwx
 *     </li>
 *     <li>
 *         a1b2c3d4e5f
 *     </li>
 *     <li>
 *         treb7uchet
 *     </li>
 * </ul>
 * In this example, the calibration values of these four lines are 12, 38, 15, and 77.
 * Adding these together produces 142.
 *
 * <p><br>
 * Consider your entire calibration document. What is the sum of all the calibration values?
 *
 * <p><br>
 * Your puzzle answer was 54338.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Your calculation isn't quite right. It looks like some of the digits are actually spelled out w
 * ith letters: one, two, three, four, five, six, seven, eight, and nine also count
 * as valid "digits".
 *
 * <p><br>
 * Equipped with this new information, you now need to find the real first and last
 * digit on each line.
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         two1nine
 *     </li>
 *     <li>
 *         eightwothree
 *     </li>
 *     <li>
 *         abcone2threexyz
 *     </li>
 *     <li>
 *         xtwone3four
 *     </li>
 *     <li>
 *         4nineeightseven2
 *     </li>
 *     <li>
 *         zoneight234
 *     </li>
 *     <li>
 *         7pqrstsixteen
 *     </li>
 * </ul>
 * In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding
 * these together produces 281.
 *
 * <p><br>
 * What is the sum of all the calibration values?
 *
 * <p><br>
 * Your puzzle answer was 53389.
 */
public class Day01 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day01.class);

  /**
   * Instantiates the solution instance.
   */
  public Day01() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_01-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var sum = getSumOfCalibrationValues(input, false);
    var end = Instant.now();

    LOGGER.info("The sum of the calibration values is: {}", sum);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var sum = getSumOfCalibrationValues(input, true);
    var end = Instant.now();

    LOGGER.info("The sum of the real calibration values is: {}", sum);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the sum of the calibration values specified in the List of input Strings.
   *
   * @param input The List of String calibration values.
   * @param real  If true, then words that spell digits will be interpreted as digits.
   * @return The sum of the calibration values specified in the List of input Strings.
   */
  public int getSumOfCalibrationValues(List<String> input, boolean real) {
    var instance = new Trebuchet(input);

    var values = instance.getCalibrationValues(real);

    return values.stream().reduce(0, Integer::sum);
  }
}
