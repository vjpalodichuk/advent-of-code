package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day04Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day04();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getSumOfAllCardPoints() {
        var instance = new Day04();
        int expected = 13;
        var actual = instance.getSumOfAllCardPoints(lines);
        assertEquals(expected, actual, "The sum of all ScratchCard points " +
                "is not what was expected: " + expected);
    }

    @Test
    void getTotalNumberOfCards() {
        var instance = new Day04();
        int expected = 30;
        var actual = instance.getTotalNumberOfCards(lines);
        assertEquals(expected, actual, "The total number of ScratchCards " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}