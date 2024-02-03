package com.capital7software.aoc.aoc2023aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day08Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day08Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day08();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void calculateStepsToTraverseWastelandSingleNodeLow() {
    var instance = new Day08();
    int expected = 2;
    var actual = instance.calculateStepsToTraverseWastelandSingleNode(lines);
    assertEquals(expected, actual, "The total steps taken "
        + "is not what was expected: " + expected);
  }

  @Test
  void calculateStepsToTraverseWastelandSingleNodeHigh() {
    var instance = new Day08();
    setupFromFile("inputs/input_day_08-02.txt");
    int expected = 6;
    var actual = instance.calculateStepsToTraverseWastelandSingleNode(lines);
    assertEquals(expected, actual, "The total steps taken "
        + "is not what was expected: " + expected);
  }

  @Test
  void calculateStepsToTraverseWastelandMultipleNodes() {
    var instance = new Day08();
    setupFromFile("inputs/input_day_08-03.txt");
    int expected = 6;
    var actual = instance.calculateStepsToTraverseWastelandMultipleNodes(lines);
    assertEquals(expected, actual, "The total winnings using Jokers "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}