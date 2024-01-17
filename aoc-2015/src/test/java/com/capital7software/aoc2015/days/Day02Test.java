package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day02;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day02Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day02Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day02();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getTotalWrappingPaperNeeded() {
        var instance = new Day02();
        var actual = instance.howMuchTotalWrappingPaper(instance.loadPresents(lines));

        assertEquals(101, actual);
    }

    @Test
    void getTotalRibbonNeeded() {
        var instance = new Day02();
        var actual = instance.howMuchTotalRibbon(instance.loadPresents(lines));

        assertEquals(48, actual);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}