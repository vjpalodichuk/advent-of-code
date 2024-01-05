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

class Day15Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day15();
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
    void whatIsTheTotalScoreOfTheHighestScoringCookie() {
        var instance = new Day15();
        long [] expected = {0, 0, 0, 0};

        var actual = instance.whatIsTheTotalScoreOfTheHighestScoringCookie(lines);
        assertEquals(expected[0], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);

        actual = instance.whatIsTheTotalScoreOfTheHighestScoringCookie(lines);
        assertEquals(expected[1], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);

        actual = instance.whatIsTheTotalScoreOfTheHighestScoringCookie(lines);
        assertEquals(expected[2], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);

        actual = instance.whatIsTheTotalScoreOfTheHighestScoringCookie(lines);
        assertEquals(expected[3], actual, "The distance traveled of the winning reindeer is " +
                "not what was expected: " + expected[0]);
    }
}