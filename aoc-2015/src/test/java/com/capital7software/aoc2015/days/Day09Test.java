package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day09;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day09Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day09();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void distanceOfShortestRouteVisitingEachNodeOnceTest() {
        var instance = new Day09();
        var expected = 605;
        var actual = instance.distanceOfShortestRouteVisitingEachNodeOnce(lines);
        assertEquals(expected, actual, "Shortest distance is not what was expected: " + expected);
    }

    @Test
    void distanceOfShortestCycleVisitingEachNodeOnceTest() {
        var instance = new Day09();
        var expected = 605;
        var actual = instance.distanceOfShortestCycleVisitingEachNodeOnce(lines);
        assertEquals(expected, actual, "Shortest cycle distance is not what was expected: " + expected);
    }

    @Test
    void distanceOfLongestRouteVisitingEachNodeOnceTest() {
        var instance = new Day09();
        var expected = 982;
        var actual = instance.distanceOfLongestRouteVisitingEachNodeOnce(lines);
        assertEquals(expected, actual, "Longest distance is not what was expected: " + expected);
    }

    @Test
    void mstKruskal() {
        var instance = new Day09();
        var expected = 605;
        var actual = instance.mstKruskal(lines);
        assertEquals(expected, actual, "Kruskal MST is not what was expected: " + expected);
    }

    @Test
    void mstPrim() {
        var instance = new Day09();
        var expected = 605;
        var actual = instance.mstPrim(lines);
        assertEquals(expected, actual, "Prim MST is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}