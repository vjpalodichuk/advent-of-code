package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day07Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day07();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void calculateTotalWinningsUsingJacks() {
        var instance = new Day07();
        int expected = 6440;
        var actual = instance.calculateTotalWinnings(lines, false);
        assertEquals(expected, actual, "The total winnings using Jacks " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateTotalWinningsUsingJokers() {
        var instance = new Day07();
        int expected = 5905;
        var actual = instance.calculateTotalWinnings(lines, true);
        assertEquals(expected, actual, "The total winnings using Jokers " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}