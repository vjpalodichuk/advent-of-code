package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.string.RunLengthEncoder;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 10: Elves Look, Elves Say ---
 * Today, the Elves are playing a game called look-and-say. They take turns making sequences
 * by reading aloud the previous sequence and using that reading as the next sequence.
 * For example, 211 is read as "one two, two ones", which becomes 1221 (1 2, 2 1s).
 * <p>
 * Look-and-say sequences are generated iteratively, using the previous value as input for
 * the next step. For each step, take the previous value, and replace each run of digits
 * (like 111) with the number of digits (3) followed by the digit itself (1).
 * <p>
 * For example:
 * <p>
 * 1 becomes 11 (1 copy of digit 1).
 * 11 becomes 21 (2 copies of digit 1).
 * 21 becomes 1211 (one 2 followed by one 1).
 * 1211 becomes 111221 (one 1, one 2, and two 1s).
 * 111221 becomes 312211 (three 1s, two 2s, and one 1).
 * Starting with the digits in your puzzle input, apply this process 40 times. What is the length of the result?
 * <p>
 * Your puzzle answer was 360154.
 * <p>
 * --- Part Two ---
 * Neat, right? You might also enjoy hearing John Conway talking about this sequence (that's Conway of Conway's Game of Life fame).
 * <p>
 * Now, starting again with the digits in your puzzle input, apply this process 50 times. What is the length of the new result?
 * <p>
 * Your puzzle answer was 5103798.
 * <p>
 */
public class Day10 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_10-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        for (var line : input) {
            var start = Instant.now();
            var result = line;

            for (int i = 0; i < 40; i++) {
                result = getRunLengthEncodedStringString(result);
            }

            var length = result.length();
            var end = Instant.now();
            System.out.printf("The length of the result is: %d%n", length);
            printTiming(start, end);
        }
    }

    @Override
    public void runPart2(List<String> input) {
        for (var line : input) {
            var start = Instant.now();
            var result = line;

            for (int i = 0; i < 50; i++) {
                result = getRunLengthEncodedStringString(result);
            }

            var length = result.length();
            var end = Instant.now();
            System.out.printf("The length of the result is: %d%n", length);
            printTiming(start, end);
        }
    }

    @NotNull
    public String getRunLengthEncodedStringString(@NotNull String line) {
        return RunLengthEncoder.encode(line);
    }
}
