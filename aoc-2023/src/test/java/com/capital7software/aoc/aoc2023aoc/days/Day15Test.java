package com.capital7software.aoc.aoc2023aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day15Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day15Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day15();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void sumOfLensHashes() {
    var instance = new Day15();
    var expected = 1320;
    var actual = instance.sumOfLensHashes(lines);
    assertEquals(expected, actual, "The sum of all the lens hashes "
        + "is not what was expected: " + expected);
  }

  @Test
  void focusingPowerOfLensConfiguration() {
    var instance = new Day15();
    var expected = 145;
    var actual = instance.focusingPowerOfLensConfiguration(lines);
    assertEquals(expected, actual, "The focusing power of the lens configuration "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}