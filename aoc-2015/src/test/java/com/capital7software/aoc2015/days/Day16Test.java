package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day16;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day16Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day16();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void identifyAuntSue() {
        var instance = new Day16();
        long expected = 40;

        var actual = instance.identifyAuntSue(lines);
        assertEquals(expected, actual, "The ID of Aunt Sue is not what was expected: " + expected);
    }

    @Test
    void whatIsTheTotalScoreOfTheHighestScoringCalorieRestrictedCookie() {
        var instance = new Day16();
        long expected = 241;

        var actual = instance.identifyRealAuntSue(lines);
        assertEquals(expected, actual, "The ID of Aunt Sue is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}