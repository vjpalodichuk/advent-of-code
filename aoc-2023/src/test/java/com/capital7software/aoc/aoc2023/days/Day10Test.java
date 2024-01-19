package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day10Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day10();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void calculateMaximumDistanceDFSSimple() {
        var instance = new Day10();
        int expected = 4;
        var actual = instance.calculateMaximumDistance(lines, false);
        assertEquals(expected, actual, "The maximum distance via DFS " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateMaximumDistanceBFSSimple() {
        var instance = new Day10();
        int expected = 4;
        var actual = instance.calculateMaximumDistance(lines, true);
        assertEquals(expected, actual, "The maximum distance via BFS " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateMaximumDistanceDFSComplex() {
        var instance = new Day10();
        setupFromFile("inputs/input_day_10-02.txt");
        int expected = 8;
        var actual = instance.calculateMaximumDistance(lines, false);
        assertEquals(expected, actual, "The maximum distance via BFS " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateMaximumDistanceBFSComplex() {
        var instance = new Day10();
        setupFromFile("inputs/input_day_10-02.txt");
        int expected = 8;
        var actual = instance.calculateMaximumDistance(lines, true);
        assertEquals(expected, actual, "The maximum distance via BFS " +
                "is not what was expected: " + expected);
    }

    @Test
    void countTilesEnclosedInMainLoopDFSSimple() {
        var instance = new Day10();
        setupFromFile("inputs/input_day_10-03.txt");
        int expected = 4;
        var actual = instance.countTilesEnclosedInMainLoop(lines, false);
        assertEquals(expected, actual, "The total number of tiles enclosed in the main loop via DFS " +
                "is not what was expected: " + expected);
    }

    @Test
    void countTilesEnclosedInMainLoopBFSSimple() {
        var instance = new Day10();
        setupFromFile("inputs/input_day_10-03.txt");
        int expected = 4;
        var actual = instance.countTilesEnclosedInMainLoop(lines, true);
        assertEquals(expected, actual, "The total number of tiles enclosed in the main loop via BFS " +
                "is not what was expected: " + expected);
    }

    @Test
    void countTilesEnclosedInMainLoopDFSComplex() {
        var instance = new Day10();
        setupFromFile("inputs/input_day_10-04.txt");
        int expected = 8;
        var actual = instance.countTilesEnclosedInMainLoop(lines, false);
        assertEquals(expected, actual, "The total number of tiles enclosed in the main loop via DFS " +
                "is not what was expected: " + expected);
    }

    @Test
    void countTilesEnclosedInMainLoopBFSComplex() {
        var instance = new Day10();
        setupFromFile("inputs/input_day_10-04.txt");
        int expected = 8;
        var actual = instance.countTilesEnclosedInMainLoop(lines, true);
        assertEquals(expected, actual, "The total number of tiles enclosed in the main loop via BFS " +
                "is not what was expected: " + expected);
    }

    @Test
    void countTilesEnclosedInMainLoopDFSMostComplex() {
        var instance = new Day10();
        setupFromFile("inputs/input_day_10-05.txt");
        int expected = 10;
        var actual = instance.countTilesEnclosedInMainLoop(lines, false);
        assertEquals(expected, actual, "The total number of tiles enclosed in the main loop via DFS " +
                "is not what was expected: " + expected);
    }

    @Test
    void countTilesEnclosedInMainLoopBFSMostComplex() {
        var instance = new Day10();
        setupFromFile("inputs/input_day_10-05.txt");
        int expected = 10;
        var actual = instance.countTilesEnclosedInMainLoop(lines, true);
        assertEquals(expected, actual, "The total number of tiles enclosed in the main loop via BFS " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}