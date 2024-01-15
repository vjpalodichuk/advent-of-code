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

class Day02Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day02();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            var url = classloader.getResource(instance.getDefaultInputFilename());
            assert url != null;
            path = Paths.get(url.toURI());
            lines = Files.readAllLines(path);
        } catch (URISyntaxException | IOException e) {
            System.out.printf("Unable to load input data from: %s%n", path);
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTotalWrappingPaperNeeded() {
        var instance = new Day02();
        var actual = instance.howMuchTotalWrappingPaper(instance.loadPresents(lines));

        assertEquals(101, actual);
    }

    @Test
    void getTotalRibbonNeeded() {
        var instance = new Day02();
        var actual = instance.howMuchTotalRibbon(instance.loadPresents(lines));

        assertEquals(48, actual);
    }
}