package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day25Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day25Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day25();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void findMinimumCut() {
        var instance = new Day25();
        var expected = 54;
        var actual = instance.findMinimumCut(lines, 3);
        assertEquals(expected, actual, "The product of the minimum cut " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}