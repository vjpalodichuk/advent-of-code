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

class Day06Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day06();
        setup(instance.getDefaultInputFilename());
    }

    void setup(String filename) {
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
    void getLitLights() {
        var instance = new Day06();
        var expectedCount = 998_996;
        var lights = instance.loadLights(lines, 1_000, 1_000);
        lights.applyInstructions();
        var actualCount = instance.getOnLightCount(lights);

        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getTotalBrightnessOfLights() {
        var instance = new Day06();
        var expectedCount = 1_001_996;
        var lights = instance.loadLights(lines, 1_000, 1_000);
        lights.applyNewInterpretationOfInstructions();
        var actualCount = instance.getTotalBrightness(lights);

        assertEquals(expectedCount, actualCount);
    }
}