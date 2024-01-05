package com.capital7software.aoc2015.days;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day14();
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
    void whatDistanceHasTheWinnerTraveled() {
        var instance = new Day14();
        double [] expected = {16.0, 160.0, 176.0, 1120.0};

        var actual = instance.whatDistanceHasTheWinnerTraveled(lines, 1);
        assertEquals(expected[0], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);

        actual = instance.whatDistanceHasTheWinnerTraveled(lines, 10);
        assertEquals(expected[1], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);

        actual = instance.whatDistanceHasTheWinnerTraveled(lines, 11);
        assertEquals(expected[2], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);

        actual = instance.whatDistanceHasTheWinnerTraveled(lines, 1000);
        assertEquals(expected[3], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);
    }

    @Test
    void howManyPointsDoesTheWinnerHave() {
        var instance = new Day14();
        double [] expected = {1.0, 10.0, 11.0, 689.0};

        var actual = instance.howManyPointsDoesTheWinnerHave(lines, 1);
        assertEquals(expected[0], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);

        actual = instance.howManyPointsDoesTheWinnerHave(lines, 10);
        assertEquals(expected[1], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);

        actual = instance.howManyPointsDoesTheWinnerHave(lines, 11);
        assertEquals(expected[2], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);

        actual = instance.howManyPointsDoesTheWinnerHave(lines, 1000);
        assertEquals(expected[3], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);
    }
}