package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day06Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day06();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void productOfTheNumberOfWinningHoldTimesPerRace() {
        var instance = new Day06();
        int expected = 288;
        var actual = instance.productOfTheNumberOfWinningHoldTimesPerRace(lines);
        assertEquals(expected, actual, "The product of the number of winning hold times per race " +
                "is not what was expected: " + expected);
    }

    @Test
    void numberOfWinningHoldTimesQuadratic() {
        var instance = new Day06();
        int expected = 71503;
        var actual = instance.numberOfWinningHoldTimesQuadratic(lines);
        assertEquals(expected, actual, "The number of winning hold times quadratically " +
                "is not what was expected: " + expected);
    }

    @Test
    void numberOfWinningHoldTimesIterative() {
        var instance = new Day06();
        int expected = 71503;
        var actual = instance.numberOfWinningHoldTimesIterative(lines);
        assertEquals(expected, actual, "The number of winning hold times iteratively " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}