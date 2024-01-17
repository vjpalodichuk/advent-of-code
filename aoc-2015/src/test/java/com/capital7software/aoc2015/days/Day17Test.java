package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day17;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day17Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day17();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void combinationsOfContainersToHoldEggNog() {
        var instance = new Day17();
        int expected = 4;

        var actual = instance.combinationsOfContainersToHoldEggNog(25, lines);
        assertEquals(expected, actual.first(), "The number of Egg Nog container combinations " +
                "is not what was expected: " + expected);
    }

    @Test
    void minimumNumberOfContainersToHoldEggNog() {
        var instance = new Day17();
        int expected = 3;
        int minExpected = 2;

        var actual = instance.combinationsOfContainersToHoldEggNog(25, lines);

        var min = actual.second().stream().map(List::size).min(Comparator.naturalOrder()).orElse(-1);
        assertEquals(minExpected, min, "The minimum number of containers needed did not meet expectations: " + minExpected);
        var minCount = actual.second().stream().filter(it -> it.size() == min).count();
        assertEquals(expected, minCount, "The number of Egg Nog container combinations " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}