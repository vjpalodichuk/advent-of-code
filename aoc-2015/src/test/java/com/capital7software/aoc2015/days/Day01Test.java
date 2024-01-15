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

class Day01Test {

    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day01();
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
    void getFloor() {
        var instance = new Day01();
        var actual = instance.getFloor(lines);

        assertEquals(-3, actual);
    }

    @Test
    void getFirstBasementFloorPosition() {
        var instance = new Day01();
        var actual = instance.getFirstBasementFloorPosition(lines);

        assertEquals(1, actual);
    }
}