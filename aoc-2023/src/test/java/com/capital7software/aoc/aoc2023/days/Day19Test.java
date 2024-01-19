package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day19Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day19Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day19();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void calculateAreaWithoutColors() {
        var instance = new Day19();
        var expected = 19_114;
        var actual = instance.sumOfRatingsOfAcceptedParts(lines);
        assertEquals(expected, actual, "The sum of the ratings of the accepted parts " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateAreaWithColors() {
        var instance = new Day19();
        var expected = 167_409_079_868_000L;
        var actual = instance.distinctCombinationsOfAcceptedRatings(lines);
        assertEquals(expected, actual, "The number of distinct combinations that will be accepted " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}