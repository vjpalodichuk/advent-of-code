package com.capital7software.aoc.aoc2023.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day21Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day21Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day21();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void numberOfPlotsThatCanBeReachedNormal() {
    var instance = new Day21();
    var expected = 16;
    var actual = instance.walk(lines, 6, false);
    assertEquals(expected, actual, "The number of garden plots "
        + "is not what was expected: " + expected);
  }

  @Test
  void numberOfPlotsThatCanBeReachedVirtual() {
    var instance = new Day21();
    var expected = 2_665;
    var actual = instance.walk(lines, 64, true);
    assertEquals(expected, actual, "The number of garden plots "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}