package com.capital7software.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.aoc2015aoc.days.Day24;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day24Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day24Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day24();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void getLowestQeScoreWith3Partitions() {
    var instance = new Day24();
    int expected = 99;
    var actual = instance.getLowestQeScore(lines, 3);
    assertEquals(expected, actual, "The lowest QE Score with 3 partitions "
        + "is not what was expected: " + expected);
  }

  @Test
  void getLowestQeScoreWith4Partitions() {
    var instance = new Day24();
    int expected = 44;
    var actual = instance.getLowestQeScore(lines, 4);
    assertEquals(expected, actual, "The lowest QE Score with 4 partitions "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}