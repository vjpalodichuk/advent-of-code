package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day22Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day22();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void totalNumberOfBricksThatCanBeSafelyDisintegrated() {
        var instance = new Day22();
        var expected = 5;
        var actual = instance.totalNumberOfBricksThatCanBeSafelyDisintegrated(lines);
        assertEquals(expected, actual, "The number of garden plots " +
                "is not what was expected: " + expected);
    }

    @Test
    void sumOfFallingBricksInAChainReaction() {
        var instance = new Day22();
        var expected = 7;
        var actual = instance.sumOfFallingBricksInAChainReaction(lines);
        assertEquals(expected, actual, "The number of falling bricks " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}