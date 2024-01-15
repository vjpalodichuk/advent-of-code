package com.capital7software.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.string.JSONFun;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 12: JSAbacusFramework.io ---
 * Santa's Accounting-Elves need help balancing the books after a recent order. Unfortunately,
 * their accounting software uses a peculiar storage format. That's where you come in.
 * <p>
 * They have a JSON document which contains a variety of things: arrays ([1,2,3]),
 * objects ({"a":1, "b":2}), numbers, and strings. Your first job is to simply
 * find all the numbers throughout the document and add them together.
 * <p>
 * For example:
 * <p>
 * [1,2,3] and {"a":2,"b":4} both have a sum of 6.
 * [[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
 * {"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
 * [] and {} both have a sum of 0.
 * You will not encounter any strings containing numbers.
 * <p>
 * What is the sum of all numbers in the document?
 * <p>
 * Your puzzle answer was 156366.
 * <p>
 * --- Part Two ---
 * Uh oh - the Accounting-Elves have realized that they double-counted everything red.
 * <p>
 * Ignore any object (and all of its children) which has any property with the
 * value "red". Do this only for objects ({...}), not arrays ([...]).
 * <p>
 * [1,2,3] still has a sum of 6.
 * [1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
 * {"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
 * [1,"red",5] has a sum of 6, because "red" in an array has no effect.
 * <p>
 * Your puzzle answer was 96852.
 *
 */
public class Day12 implements AdventOfCodeSolution {
    /**
     * Instantiates the solution instance.
     */
    public Day12() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_12-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        for (var line : input) {
            var start = Instant.now();
            var sum = sumNumbersInString(line);
            var end = Instant.now();
            System.out.printf("The sum of all numbers in the JSON string is: %d%n", sum);
            printTiming(start, end);
        }
    }

    @Override
    public void runPart2(List<String> input) {
        for (var line : input) {
            var start = Instant.now();
            var sum = sumNumbersInStringSkippingRedObjects(line);
            var end = Instant.now();
            System.out.printf("The sum of all numbers in the JSON string skipping red objects is: %d%n", sum);
            printTiming(start, end);
        }
    }

    /**
     * Returns the sum of all the numbers encountered in the specified JSON String.
     *
     * @param line The JSON String to sum the numbers from.
     * @return The sum of all the numbers encountered in the specified JSON String.
     */
    public long sumNumbersInString(String line) {
        return JSONFun.sumOfAllNumbers(line);
    }


    /**
     * Returns the sum of all the numbers encountered in the specified JSON String
     * that are not in a Red object or sub-object.
     *
     * @param line The JSON String to sum the numbers from.
     * @return The sum of all the numbers encountered in the specified JSON String
     * that are not in a Red object or sub-object.
     */
    public long sumNumbersInStringSkippingRedObjects(String line) {
        return JSONFun.sumOfAllNumbersSkippingObjectsWithRedPropertyValues(line);
    }
}
