package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day24Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day24Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day24();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void testHailStones() {
        var instance = new Day24();
        var expected = 2;
        var actual = instance.testHailStones(lines, 7, 27);
        assertEquals(expected, actual, "The number of hail stones that intersect in the test area " +
                "is not what was expected: " + expected);
    }

    @Test
    void testFindRock() {
        var instance = new Day24();
        var expected = 47;
        var actual = instance.testFindRock(lines);
        assertEquals(expected, actual, "The sum of the parts of the rock " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}