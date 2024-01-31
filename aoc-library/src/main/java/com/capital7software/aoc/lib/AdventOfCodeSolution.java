package com.capital7software.aoc.lib;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;

/**
 * Interface that the AdventOfCodeRunner expects the Day classes
 * to implement.
 */
public interface AdventOfCodeSolution {
  /**
   * The default input filename for the Day.
   *
   * @return The input filename for the Day.
   */
  String getDefaultInputFilename();

  /**
   * Runs the solution for Part 1 of the problem.
   *
   * @param input The contents of the input file.
   */
  void runPart1(List<String> input);

  /**
   * Runs the solution for Part 2 of the problem.
   *
   * @param input The contents of the input file.
   */
  default void runPart2(List<String> input) {
  }

  /**
   * A helper method for printing out the timing of the solutions.
   *
   * @param logger The Logger instance to use to log the messages.
   * @param start  The Instant the solution was started.
   * @param end    The Instant the solution finished.
   */
  default void logTimings(Logger logger, Instant start, Instant end) {
    logger.info("Calculated in {} ns", Duration.between(start, end).toNanos());
  }
}
