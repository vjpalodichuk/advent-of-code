package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.string.RunLengthEncoder;
import java.time.Instant;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 10: Elves Look, Elves Say ---<br><br>
 * Today, the Elves are playing a game called look-and-say. They take turns making sequences
 * by reading aloud the previous sequence and using that reading as the next sequence.
 * For example, 211 is read as "one two, two ones", which becomes 1221 (1 2, 2 1s).
 *
 * <p><br>
 * Look-and-say sequences are generated iteratively, using the previous value as input for
 * the next step. For each step, take the previous value, and replace each run of digits
 * (like 111) with the number of digits (3) followed by the digit itself (1).
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * <code>
 * 1 becomes 11 (1 copy of digit 1).<br>
 * 11 becomes 21 (2 copies of digit 1).<br>
 * 21 becomes 1211 (one 2 followed by one 1).<br>
 * 1211 becomes 111221 (one 1, one 2, and two 1s).<br>
 * 111221 becomes 312211 (three 1s, two 2s, and one 1).<br>
 * </code>
 *
 * <p><br>
 * Starting with the digits in your puzzle input, apply this process 40 times. What is the length
 * of the result?
 *
 * <p><br>
 * Your puzzle answer was 360154.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Neat, right? You might also enjoy hearing John Conway talking about this sequence
 * (that's Conway of Conway's Game of Life fame).
 *
 * <p><br>
 * Now, starting again with the digits in your puzzle input, apply this process 50 times.
 * What is the length of the new result?
 *
 * <p><br>
 * Your puzzle answer was 5103798.
 */
public class Day10 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day10.class);

  /**
   * Instantiates the solution instance.
   */
  public Day10() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_10-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    for (var line : input) {
      var start = Instant.now();
      var result = line;

      for (int i = 0; i < 40; i++) {
        result = getRunLengthEncodedStringString(result);
      }

      var length = result.length();
      var end = Instant.now();
      LOGGER.info("The length of the result is: {}", length);
      logTimings(LOGGER, start, end);
    }
  }

  @Override
  public void runPart2(List<String> input) {
    for (var line : input) {
      var start = Instant.now();
      var result = line;

      for (int i = 0; i < 50; i++) {
        result = getRunLengthEncodedStringString(result);
      }

      var length = result.length();
      var end = Instant.now();
      LOGGER.info("The length of the result is: {}", length);
      logTimings(LOGGER, start, end);
    }
  }

  /**
   * Encodes and returns the new Run Length Encoded String.
   *
   * @param line The String to encode.
   * @return The new Run Length Encoded String.
   */
  @NotNull
  public String getRunLengthEncodedStringString(@NotNull String line) {
    return RunLengthEncoder.encode(line);
  }
}
