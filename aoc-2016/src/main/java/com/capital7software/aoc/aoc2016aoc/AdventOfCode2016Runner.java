package com.capital7software.aoc.aoc2016aoc;

import com.capital7software.aoc.lib.AdventOfCodeRunner;

/**
 * The Advent of Code 2016 Runner. Pass the name of the Solution class
 * to run.
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * Day01
 *
 * <p><br>
 * To run the Day01 solution.
 */
public class AdventOfCode2016Runner extends AdventOfCodeRunner {
  /**
   * Instantiates a new and empty instance.
   */
  public AdventOfCode2016Runner() {
    super();
  }

  /**
   * The main entry point to run the various DayXX classes for this year's AOC. The first
   * argument is required and must be a Day class such as Day01. The second argument is optional
   * and is the full path to a file to load as the default input.
   *
   * @param args The first argument must be the name of a Day class such as Day01.
   */
  public static void main(String[] args) {
    var packageName = "com.capital7software.aoc.aoc2016aoc.days.";

    runSolution(packageName, args);
  }
}
