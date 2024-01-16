package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day09;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day09();
        setupFromFile(instance.getDefaultInputFilename());
    }

    void setupFromFile(String filename) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            var url = classloader.getResource(filename);
            assert url != null;
            path = Paths.get(url.toURI());
            lines = Files.readAllLines(path);
        } catch (URISyntaxException | IOException e) {
            System.out.printf("Unable to load input data from: %s%n", path);
            throw new RuntimeException(e);
        }
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
}