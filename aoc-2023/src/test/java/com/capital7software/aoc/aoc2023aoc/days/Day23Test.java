package com.capital7software.aoc.aoc2023aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day23Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day23Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day23();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void stepsOfLongestTrailWithSlopes() {
    var instance = new Day23();
    var expected = 94;
    var actual = instance.stepsOfLongestTrail(lines, false, true);
    assertEquals(expected, actual, "The number of steps "
        + "is not what was expected: " + expected);
  }

  @Test
  void stepsOfLongestTrailWithoutSlopes() {
    var instance = new Day23();
    var expected = 154;
    var actual = instance.stepsOfLongestTrail(lines, true, true);
    assertEquals(expected, actual, "The number of steps "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}