package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day11Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day11();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void sumOfAllPossibleSpringConditionArrangements() {
        var instance = new Day11();
        int expected = 21;
        var actual = instance.sumOfAllPairsShortestPath(lines, 1, false);
        assertEquals(expected, actual, "The sum of the shortest paths " +
                "is not what was expected: " + expected);
    }

    @Test
    void sumOfAllPairsShortestPathSmallFactorOptimized() {
        var instance = new Day11();
        int expected = 374;
        var actual = instance.sumOfAllPairsShortestPath(lines, 1, true);
        assertEquals(expected, actual, "The sum of the shortest paths " +
                "is not what was expected: " + expected);
    }

    @Test
    void sumOfAllPairsShortestPathMediumFactorNormal() {
        var instance = new Day11();
        int expected = 1030;
        var actual = instance.sumOfAllPairsShortestPath(lines, 9, false);
        assertEquals(expected, actual, "The sum of the shortest paths " +
                "is not what was expected: " + expected);
    }

    @Test
    void sumOfAllPairsShortestPathMediumFactorOptimized() {
        var instance = new Day11();
        int expected = 1030;
        var actual = instance.sumOfAllPairsShortestPath(lines, 9, true);
        assertEquals(expected, actual, "The sum of the shortest paths " +
                "is not what was expected: " + expected);
    }

    @Test
    void sumOfAllPairsShortestPathLargeFactorNormal() {
        var instance = new Day11();
        int expected = 8410;
        var actual = instance.sumOfAllPairsShortestPath(lines, 99, false);
        assertEquals(expected, actual, "The sum of the shortest paths " +
                "is not what was expected: " + expected);
    }

    @Test
    void sumOfAllPairsShortestPathLargeFactorOptimized() {
        var instance = new Day11();
        int expected = 8410;
        var actual = instance.sumOfAllPairsShortestPath(lines, 99, true);
        assertEquals(expected, actual, "The sum of the shortest paths " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}