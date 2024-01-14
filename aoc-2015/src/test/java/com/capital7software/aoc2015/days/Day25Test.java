package com.capital7software.aoc2015.days;

import com.capital7software.aoc.lib.grid.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day25Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day25();
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
    void getNextCode() {
        var instance = new Day25();
        var generator = CodeGenerator.buildGenerator(lines.getFirst());
        int expected = 9132360;
        var actual = instance.getNextCode(generator);
        assertEquals(expected, actual, "The next code " +
                "is not what was expected: " + expected);
    }
}