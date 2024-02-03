package com.capital7software.aoc.aoc2016aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

@Slf4j
class Day01Test extends AdventOfCodeTestBase {
  @Override
  public Logger getLogger() {
    return log;
  }

  @BeforeEach
  void setUp() {
    var instance = new Day01();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void testDistanceFromStart() {
    var instance = new Day01();
    var expected = 5;
    var actual = instance.distanceFromStart(lines.getFirst());
    assertEquals(expected, actual, "The distance is not what was expected: ${expected}");

    setupFromFile("inputs/input_day_01-02.txt");
    expected = 2;
    actual = instance.distanceFromStart(lines.getFirst());
    assertEquals(expected, actual, "The distance is not what was expected: ${expected}");

    setupFromFile("inputs/input_day_01-03.txt");
    expected = 12;
    actual = instance.distanceFromStart(lines.getFirst());
    assertEquals(expected, actual, "The distance is not what was expected: ${expected}");
  }

  @Test
  void testDistanceFirstLocationVisitedTwiceFromStart() {
    var instance = new Day01();
    setupFromFile("inputs/input_day_01-04.txt");
    var expected = 4;
    var actual = instance.distanceFirstLocationVisitedTwice(lines.getFirst());
    assertEquals(expected, actual, "The distance is not what was expected: ${expected}");
  }
}
