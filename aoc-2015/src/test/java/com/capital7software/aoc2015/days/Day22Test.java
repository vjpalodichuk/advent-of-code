package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day22;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day22();
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
    void leastAmountOfManaAndStillWin() {
        var instance = new Day22();
        int expected = 953;
        var actual = instance.leastAmountOfManaAndStillWin(lines, false);
        assertEquals(expected, actual, "The least amount of mana " +
                "is not what was expected: " + expected);
    }

    @Test
    void leastAmountOfManaOnHardAndStillWin() {
        var instance = new Day22();
        int expected = 1289;
        var actual = instance.leastAmountOfManaAndStillWin(lines, true);
        assertEquals(expected, actual, "The least amount of mana on hard " +
                "is not what was expected: " + expected);
    }
}