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

class Day23Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day23();
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
    void runProgramAndGetValueInRegister() {
        var instance = new Day23();
        int expected = 2;
        var actual = instance.runProgramAndGetValueInRegister(lines, "a");
        assertEquals(expected, actual, "The value in register a " +
                "is not what was expected: " + expected);
    }

    @Test
    void runProgramWithStartingRegisterAndValueAndGetValueInRegister() {
        var instance = new Day23();
        int expected = 7;
        var actual = instance.runProgramWithStartingRegisterAndValueAndGetValueInRegister(lines, "a", 1L, "a");
        assertEquals(expected, actual, "The value in register a " +
                "is not what was expected: " + expected);
    }
}