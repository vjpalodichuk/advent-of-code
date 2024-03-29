package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day18Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day18Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day18();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void animateLights() {
    var instance = new Day18();
    int expected = 4;

    var actual = instance.animateLights(4, lines, false);
    assertEquals(expected, actual.first(), "The number of lights on "
        + "is not what was expected: " + expected);
  }

  @Test
  void animateLightsCornersAlwaysOn() {
    var instance = new Day18();
    int expected = 17;

    var actual = instance.animateLights(5, lines, true);
    assertEquals(expected, actual.first(), "The number of lights on "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}