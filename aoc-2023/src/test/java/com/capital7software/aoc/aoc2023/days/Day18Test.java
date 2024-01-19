package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import com.capital7software.aoc.lib.geometry.LineSegment2D;
import com.capital7software.aoc.lib.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class Day18Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day18Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day18();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void calculateAreaWithoutColors() {
        var instance = new Day18();
        var expected = 62;
        var actual = instance.calculateArea(lines, false);
        assertEquals(expected, actual, "The minimum heat loss " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateAreaWithColors() {
        var instance = new Day18();
        var expected = 952_408_144_115L;
        var actual = instance.calculateArea(lines, true);
        assertEquals(expected, actual, "The minimum heat loss " +
                "is not what was expected: " + expected);
    }

    @Test
    void linesIntersectSimple() {
        var line1 = new LineSegment2D<>(new Point2D<>(1.0, 1.0), new Point2D<>(1.0, 10.0));
        var line2 = new LineSegment2D<>(new Point2D<>(1.0, -2.0), new Point2D<>(1.0, 12.0));
        var expected = new Point2D<>(1.0, 1.0);
        var actual = line1.intersect(line2);

        assertEquals(expected, actual, "The intersection point " +
                "is not what was expected: " + expected);
    }

    @Test
    void linesIntersectMedium() {
        var line1 = new LineSegment2D<>(new Point2D<>(1.0, 1.0), new Point2D<>(1.0, 10.0));
        var line2 = new LineSegment2D<>(new Point2D<>(1.0, 2.0), new Point2D<>(1.0, 12.0));
        var expected = new Point2D<>(1.0, 2.0);
        var actual = line1.intersect(line2);

        assertEquals(expected, actual, "The intersection point " +
                "is not what was expected: " + expected);
    }

    @Test
    void linesIntersectHard() {
        var line1 = new LineSegment2D<>(new Point2D<>(1.0, 1.0), new Point2D<>(1.0, 10.0));
        var line2 = new LineSegment2D<>(new Point2D<>(1.0, -2.0), new Point2D<>(1.0, 8.0));
        var expected = new Point2D<>(1.0, 8.0);
        var actual = line1.intersect(line2);

        assertEquals(expected, actual, "The intersection point " +
                "is not what was expected: " + expected);
    }

    @Test
    void linesDontIntersectSimple() {
        var line1 = new LineSegment2D<>(new Point2D<>(1.0, 1.0), new Point2D<>(1.0, 10.0));
        var line2 = new LineSegment2D<>(new Point2D<>(1.1, 0.5), new Point2D<>(1.1, 10.1));
        var actual = line1.intersect(line2);

        assertNull(actual, "The intersection point " +
                "is not what was expected: ");
    }

    @Test
    void linesDontIntersectHard() {
        var line1 = new LineSegment2D<>(new Point2D<>(1.0, 1.0), new Point2D<>(1.0, 10.0));
        var line2 = new LineSegment2D<>(new Point2D<>(0.9, 0.5), new Point2D<>(0.9, 10.1));
        var actual = line1.intersect(line2);

        assertNull(actual, "The intersection point " +
                "is not what was expected: ");
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}