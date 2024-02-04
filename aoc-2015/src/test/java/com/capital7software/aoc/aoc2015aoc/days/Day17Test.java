package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day17Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day17Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day17();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void combinationsOfContainersToHoldEggNog() {
    var instance = new Day17();
    int expected = 4;

    var actual = instance.combinationsOfContainersToHoldEggNog(25, lines);
    assertEquals(expected, actual.first(), "The number of Egg Nog container combinations "
        + "is not what was expected: " + expected);
  }

  @Test
  void minimumNumberOfContainersToHoldEggNog() {
    var instance = new Day17();
    int expected = 3;
    int minExpected = 2;

    var actual = instance.combinationsOfContainersToHoldEggNog(25, lines);

    var min = actual
        .second()
        .stream()
        .map(List::size)
        .min(Comparator.naturalOrder())
        .orElse(-1);

    assertEquals(
        minExpected,
        min,
        "The minimum number of containers needed did not meet expectations: " + minExpected
    );

    var minCount = actual.second().stream().filter(it -> it.size() == min).count();
    assertEquals(expected, minCount, "The number of Egg Nog container combinations "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}