package com.capital7software.aoc.aoc2023.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day11Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day11Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day11();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void sumOfAllPossibleSpringConditionArrangements() {
    var instance = new Day11();
    int expected = 374;
    var actual = instance.sumOfAllPairsShortestPath(lines, 1, false);
    assertEquals(expected, actual, "The sum of the shortest paths "
        + "is not what was expected: " + expected);
  }

  @Test
  void sumOfAllPairsShortestPathSmallFactorOptimized() {
    var instance = new Day11();
    int expected = 374;
    var actual = instance.sumOfAllPairsShortestPath(lines, 1, true);
    assertEquals(expected, actual, "The sum of the shortest paths "
        + "is not what was expected: " + expected);
  }

  @Test
  void sumOfAllPairsShortestPathMediumFactorNormal() {
    var instance = new Day11();
    int expected = 1030;
    var actual = instance.sumOfAllPairsShortestPath(lines, 9, false);
    assertEquals(expected, actual, "The sum of the shortest paths "
        + "is not what was expected: " + expected);
  }

  @Test
  void sumOfAllPairsShortestPathMediumFactorOptimized() {
    var instance = new Day11();
    int expected = 1030;
    var actual = instance.sumOfAllPairsShortestPath(lines, 9, true);
    assertEquals(expected, actual, "The sum of the shortest paths "
        + "is not what was expected: " + expected);
  }

  @Test
  void sumOfAllPairsShortestPathLargeFactorNormal() {
    var instance = new Day11();
    int expected = 8410;
    var actual = instance.sumOfAllPairsShortestPath(lines, 99, false);
    assertEquals(expected, actual, "The sum of the shortest paths "
        + "is not what was expected: " + expected);
  }

  @Test
  void sumOfAllPairsShortestPathLargeFactorOptimized() {
    var instance = new Day11();
    int expected = 8410;
    var actual = instance.sumOfAllPairsShortestPath(lines, 99, true);
    assertEquals(expected, actual, "The sum of the shortest paths "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}