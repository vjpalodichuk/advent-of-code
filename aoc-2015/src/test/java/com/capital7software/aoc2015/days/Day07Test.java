package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day07;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day07();
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
    void whatSignalsAreOnTheWires() {
        var instance = new Day07();
        var board = instance.loadCircuitBoard("0", lines);
        var expected = new HashMap<String, Integer>();
        expected.put("d", 72);
        expected.put("e", 507);
        expected.put("f", 492);
        expected.put("g", 114);
        expected.put("h", 65412);
        expected.put("i", 65079);
        expected.put("j", 114);
        expected.put("x", 123);
        expected.put("y", 456);

        instance.getWireValues(board).forEach((key, value) ->
                assertEquals(expected.get(key), value, "Mismatch for key: " + key));
    }

    @Test
    void whatSignalsAreOnTheWiresAfterOverride() {
        var instance = new Day07();
        var board = instance.loadCircuitBoard("0", lines);
        instance.getWireValues(board); // Cause the signals to fire
        instance.override(board, "h", "x");
        instance.resetWires(board);
        var expected = new HashMap<String, Integer>();
        expected.put("d", 384);
        expected.put("e", 65484);
        expected.put("f", 65040);
        expected.put("g", 114);
        expected.put("h", 123);
        expected.put("i", 65079);
        expected.put("j", 114);
        expected.put("x", 65412);
        expected.put("y", 456);
        var wires = instance.getWireValues(board);
        wires.forEach((key, value) -> assertEquals(expected.get(key), value, "Mismatch for key: " + key));
    }
}