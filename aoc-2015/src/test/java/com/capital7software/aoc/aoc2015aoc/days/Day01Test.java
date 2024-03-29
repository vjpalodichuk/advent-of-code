package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day01Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day01Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day01();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void getFloor() {
    var instance = new Day01();
    var actual = instance.getFloor(lines);

    assertEquals(-3, actual);
  }

  @Test
  void getFirstBasementFloorPosition() {
    var instance = new Day01();
    var actual = instance.getFirstBasementFloorPosition(lines);

    assertEquals(1, actual);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}