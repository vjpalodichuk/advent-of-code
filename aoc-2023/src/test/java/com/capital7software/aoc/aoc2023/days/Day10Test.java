package com.capital7software.aoc.aoc2023.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day10Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day10Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day10();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void calculateMaximumDistanceDfsSimple() {
    var instance = new Day10();
    int expected = 4;
    var actual = instance.calculateMaximumDistance(lines, false);
    assertEquals(expected, actual, "The maximum distance via DFS "
        + "is not what was expected: " + expected);
  }

  @Test
  void calculateMaximumDistanceBfsSimple() {
    var instance = new Day10();
    int expected = 4;
    var actual = instance.calculateMaximumDistance(lines, true);
    assertEquals(expected, actual, "The maximum distance via BFS "
        + "is not what was expected: " + expected);
  }

  @Test
  void calculateMaximumDistanceDfsComplex() {
    var instance = new Day10();
    setupFromFile("inputs/input_day_10-02.txt");
    int expected = 8;
    var actual = instance.calculateMaximumDistance(lines, false);
    assertEquals(expected, actual, "The maximum distance via BFS "
        + "is not what was expected: " + expected);
  }

  @Test
  void calculateMaximumDistanceBfsComplex() {
    var instance = new Day10();
    setupFromFile("inputs/input_day_10-02.txt");
    int expected = 8;
    var actual = instance.calculateMaximumDistance(lines, true);
    assertEquals(expected, actual, "The maximum distance via BFS "
        + "is not what was expected: " + expected);
  }

  @Test
  void countTilesEnclosedInMainLoopDfsSimple() {
    var instance = new Day10();
    setupFromFile("inputs/input_day_10-03.txt");
    int expected = 4;
    var actual = instance.countTilesEnclosedInMainLoop(lines, false);
    assertEquals(
        expected,
        actual,
        "The total number of tiles enclosed in the main loop via DFS "
            + "is not what was expected: " + expected
    );
  }

  @Test
  void countTilesEnclosedInMainLoopBfsSimple() {
    var instance = new Day10();
    setupFromFile("inputs/input_day_10-03.txt");
    int expected = 4;
    var actual = instance.countTilesEnclosedInMainLoop(lines, true);
    assertEquals(
        expected,
        actual,
        "The total number of tiles enclosed in the main loop via BFS "
            + "is not what was expected: " + expected
    );
  }

  @Test
  void countTilesEnclosedInMainLoopDfsComplex() {
    var instance = new Day10();
    setupFromFile("inputs/input_day_10-04.txt");
    int expected = 8;
    var actual = instance.countTilesEnclosedInMainLoop(lines, false);
    assertEquals(
        expected,
        actual,
        "The total number of tiles enclosed in the main loop via DFS "
            + "is not what was expected: " + expected
    );
  }

  @Test
  void countTilesEnclosedInMainLoopBfsComplex() {
    var instance = new Day10();
    setupFromFile("inputs/input_day_10-04.txt");
    int expected = 8;
    var actual = instance.countTilesEnclosedInMainLoop(lines, true);
    assertEquals(
        expected,
        actual,
        "The total number of tiles enclosed in the main loop via BFS "
            + "is not what was expected: " + expected
    );
  }

  @Test
  void countTilesEnclosedInMainLoopDfsMostComplex() {
    var instance = new Day10();
    setupFromFile("inputs/input_day_10-05.txt");
    int expected = 10;
    var actual = instance.countTilesEnclosedInMainLoop(lines, false);
    assertEquals(
        expected,
        actual,
        "The total number of tiles enclosed in the main loop via DFS "
            + "is not what was expected: " + expected
    );
  }

  @Test
  void countTilesEnclosedInMainLoopBfsMostComplex() {
    var instance = new Day10();
    setupFromFile("inputs/input_day_10-05.txt");
    int expected = 10;
    var actual = instance.countTilesEnclosedInMainLoop(lines, true);
    assertEquals(
        expected,
        actual,
        "The total number of tiles enclosed in the main loop via BFS "
            + "is not what was expected: " + expected
    );
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}