package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day15;
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
        long expected = 62_842_880;

        var actual = instance.whatIsTheTotalScoreOfTheHighestScoringCookie(lines, 100_000);
        assertEquals(expected, actual, "The total score of the highest-scoring " +
                "cookie is not what was expected: " + expected);
    }

    @Test
    void whatIsTheTotalScoreOfTheHighestScoringCalorieRestrictedCookie() {
        var instance = new Day15();
        long expected = 57_600_000;

        var actual = instance.whatIsTheTotalScoreOfTheHighestScoringCaloriesRestrictedCookie(lines, 250_000);
        assertEquals(expected, actual, "The total score of the highest-scoring " +
                "cookie is not what was expected: " + expected);
    }
}