package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.math.QuantumEntanglement;

import java.time.Instant;
import java.util.List;

/**
 * <p>
 * Your puzzle answer was .
 * <p>
 * <p>
 * Your puzzle answer was .
 * <p>
 */
public class Day25 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_25-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var lowest = getLowestQEScore(input, 3);
        var end = Instant.now();
        System.out.printf("The lowest QE Score with 3 partitions is: %d%n", lowest);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var lowest = getLowestQEScore(input, 4);
        var end = Instant.now();
        System.out.printf("The lowest QE Score with 4 partitions is: %d%n", lowest);
        printTiming(start, end);
    }

    public long getLowestQEScore(List<String> input, int partitions) {
        var sleigh = new QuantumEntanglement(input);
        return sleigh.getLowestQEScore(partitions);
    }
}
