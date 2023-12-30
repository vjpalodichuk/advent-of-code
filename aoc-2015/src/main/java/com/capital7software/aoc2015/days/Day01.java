package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 1: Not Quite Lisp ---
 * Santa was hoping for a white Christmas, but his weather machine's "snow" function is powered by stars,
 * and he's fresh out! To save Christmas, he needs you to collect fifty stars by December 25th.
 * <p>
 * Collect stars by helping Santa solve puzzles. Two puzzles will be made available on each day in the
 * Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 * <p>
 * Here's an easy puzzle to warm you up.
 * <p>
 * Santa is trying to deliver presents in a large apartment building, but he can't find the right
 * floor - the directions he got are a little confusing. He starts on the ground floor (floor 0)
 * and then follows the instructions one character at a time.
 * <p>
 * An opening parenthesis, (, means he should go up one floor, and a closing parenthesis, ),
 * means he should go down one floor.
 * <p>
 * The apartment building is very tall, and the basement is very deep; he will never find the top or bottom floors.
 * <p>
 * For example:
 * <p>
 * (()) and ()() both result in floor 0.
 * ((( and (()(()( both result in floor 3.
 * ))((((( also results in floor 3.
 * ()) and ))( both result in floor -1 (the first basement level).
 * ))) and )())()) both result in floor -3.
 * Part 1: To what floor do the instructions take Santa?
 * <p>
 *     Your puzzle answer was 280.
 * <p>
 * --- Part Two ---
 * Now, given the same instructions, find the position of the first character that causes him to enter
 * the basement (floor -1). The first character in the instructions has position 1,
 * the second character has position 2, and so on.
 * <p>
 * For example:
 * <p>
 * ) causes him to enter the basement at character position 1.
 * ()()) causes him to enter the basement at character position 5.
 * What is the position of the character that causes Santa to first enter the basement?
 * <p>
 *     Your puzzle answer was 1797.
 */
public class Day01 implements AdventOfCodeSolution {

    private static final String defaultFilename = "inputs/input_day_01-01.txt";

    @Override
    public String getDefaultInputFilename() {
        return defaultFilename;
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        long floor = getFloor(input);
        var end = Instant.now();

        System.out.printf("Santa is on the %d floor!%n", floor);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        long floor = getFirstBasementFloorPosition(input);
        var end = Instant.now();

        System.out.printf("Position %d causes Santa to enter the basement for the first time!%n", floor);
        printTiming(start, end);
    }

    public long getFloor(List<String> input) {
        var openCount = 0L;
        var closedCount = 0L;

        for (var line : input) {
            if (line != null && !line.isBlank()) {
                for (var c : line.toCharArray()) {
                    if (c == '(') {
                        openCount++;
                    } else if (c == ')') {
                        closedCount++;
                    }
                }
            }
        }

        return openCount - closedCount;
    }

    public long getFirstBasementFloorPosition(List<String> input) {
        var openCount = 0L;
        var closedCount = 0L;

        for (var line : input) {
            if (line != null && !line.isBlank()) {
                var chars = line.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];

                    if (c == '(') {
                        openCount++;
                    } else if (c == ')') {
                        closedCount++;
                    }
                    if (closedCount > openCount) {
                        return i + 1;
                    }
                }
            }
        }

        return -1L;
    }
}
