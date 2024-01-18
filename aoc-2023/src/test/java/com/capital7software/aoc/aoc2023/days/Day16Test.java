package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day16Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day16();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void numberOfEnergizedTilesTopLeftHeadingRight() {
        var instance = new Day16();
        var expected = 46;
        var actual = instance.numberOfEnergizedTilesTopLeftHeadingRight(lines, false);
        assertEquals(expected, actual, "The number of energized tiles " +
                "is not what was expected: " + expected);
    }

    @Test
    void numberOfEnergizedTilesTopLeftHeadingRightRecursive() {
        var instance = new Day16();
        var expected = 46;
        var actual = instance.numberOfEnergizedTilesTopLeftHeadingRight(lines, true);
        assertEquals(expected, actual, "The number of energized tiles " +
                "is not what was expected: " + expected);
    }

    @Test
    void maximumNumberOfEnergizedTiles() {
        var instance = new Day16();
        var expected = 51;
        var actual = instance.maximumNumberOfEnergizedTiles(lines);
        assertEquals(expected, actual, "The maximum number of energized tiles " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}