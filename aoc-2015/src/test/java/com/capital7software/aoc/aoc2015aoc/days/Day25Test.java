package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import com.capital7software.aoc.lib.grid.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day25Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day25Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day25();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void getNextCode() {
    var instance = new Day25();
    var generator = CodeGenerator.buildGenerator(lines.getFirst());
    int expected = 9132360;
    var actual = instance.getNextCode(generator);
    assertEquals(expected, actual, "The next code "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}