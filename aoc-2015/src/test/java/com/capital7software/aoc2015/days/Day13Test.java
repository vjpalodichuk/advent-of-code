package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day13;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day13();
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
    void greatestChangeInHappiness() {
        var instance = new Day13();
        var expected = 330;
        var actual = instance.greatestChangeInHappiness(lines);
        assertEquals(expected, actual, "Greatest change in happiness is not what was expected: " + expected);
    }

    @Test
    void greatestChangeInHappinessWithMeAtTheParty() {
        var instance = new Day13();
        var expected = 286;
        setupFromFile("inputs/input_day_13-02.txt");
        var actual = instance.greatestChangeInHappiness(lines);
        assertEquals(expected, actual, "Greatest change in happiness is not what was expected: " + expected);
    }
}