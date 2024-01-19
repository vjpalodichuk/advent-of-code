package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day08Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day08();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void calculateStepsToTraverseWastelandSingleNodeLow() {
        var instance = new Day08();
        int expected = 2;
        var actual = instance.calculateStepsToTraverseWastelandSingleNode(lines);
        assertEquals(expected, actual, "The total steps taken " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateStepsToTraverseWastelandSingleNodeHigh() {
        var instance = new Day08();
        setupFromFile("inputs/input_day_08-02.txt");
        int expected = 6;
        var actual = instance.calculateStepsToTraverseWastelandSingleNode(lines);
        assertEquals(expected, actual, "The total steps taken " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateStepsToTraverseWastelandMultipleNodes() {
        var instance = new Day08();
        setupFromFile("inputs/input_day_08-03.txt");
        int expected = 6;
        var actual = instance.calculateStepsToTraverseWastelandMultipleNodes(lines);
        assertEquals(expected, actual, "The total winnings using Jokers " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}