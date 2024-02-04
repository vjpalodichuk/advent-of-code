package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day07Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day07Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day07();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void whatSignalsAreOnTheWires() {
    var instance = new Day07();
    final var board = instance.loadCircuitBoard("0", lines);
    var expected = new HashMap<String, Integer>();
    expected.put("d", 72);
    expected.put("e", 507);
    expected.put("f", 492);
    expected.put("g", 114);
    expected.put("h", 65412);
    expected.put("i", 65079);
    expected.put("j", 114);
    expected.put("x", 123);
    expected.put("y", 456);

    instance
        .getWireValues(board)
        .forEach((key, value) ->
                     assertEquals(expected.get(key), value, "Mismatch for key: " + key));
  }

  @Test
  void whatSignalsAreOnTheWiresAfterOverride() {
    var instance = new Day07();
    var board = instance.loadCircuitBoard("0", lines);
    instance.getWireValues(board); // Cause the signals to fire
    instance.override(board, "h", "x");
    instance.resetWires(board);
    var expected = new HashMap<String, Integer>();
    expected.put("d", 384);
    expected.put("e", 65484);
    expected.put("f", 65040);
    expected.put("g", 114);
    expected.put("h", 123);
    expected.put("i", 65079);
    expected.put("j", 114);
    expected.put("x", 65412);
    expected.put("y", 456);
    var wires = instance.getWireValues(board);
    wires
        .forEach((key, value) ->
                     assertEquals(expected.get(key), value, "Mismatch for key: " + key));
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}