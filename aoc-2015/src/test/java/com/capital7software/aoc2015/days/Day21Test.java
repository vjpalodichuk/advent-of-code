package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day21;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day21Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day21();
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
    void leastAmountOfGoldAndStillWin() {
        var instance = new Day21();
        int expected = 65;
        var actual = instance.leastAmountOfGoldAndStillWin(lines);
        assertEquals(expected, actual, "The least amount of gold " +
                "is not what was expected: " + expected);
    }

    @Test
    void mostAmountOfGoldAndStillLose() {
        var instance = new Day21();
        int expected = 188;
        var actual = instance.mostAmountOfGoldAndStillLose(lines);
        assertEquals(expected, actual, "The most amount of gold " +
                "is not what was expected: " + expected);
    }
}