package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day12Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day12();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void sumOfAllPossibleSpringArrangements() {
        var instance = new Day12();
        int expected = 21;
        var actual = instance.sumOfAllPossibleSpringArrangements(lines, false);
        assertEquals(expected, actual, "The sum of all possible spring arrangements " +
                "is not what was expected: " + expected);
    }

    @Test
    void sumOfAllPossibleUnfoldedSpringArrangements() {
        var instance = new Day12();
        int expected = 525152;
        var actual = instance.sumOfAllPossibleSpringArrangements(lines, true);
        assertEquals(expected, actual, "he sum of all possible unfolded spring arrangements " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}