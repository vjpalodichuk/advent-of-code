package com.capital7software.aoc.aoc2023aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day01Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day01Test.class);

  @Test
  void getSumOfCalibrationValues() {
    var instance = new Day01();
    setupFromFile(instance.getDefaultInputFilename());
    int expected = 142;
    var actual = instance.getSumOfCalibrationValues(lines, false);
    assertEquals(expected, actual, "The sum of calibration values "
        + "is not what was expected: " + expected);
  }

  @Test
  void getSumOfRealCalibrationValues() {
    var instance = new Day01();
    setupFromFile("inputs/input_day_01-02.txt");
    int expected = 281;
    var actual = instance.getSumOfCalibrationValues(lines, true);
    assertEquals(expected, actual, "The sum of real calibration values "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}