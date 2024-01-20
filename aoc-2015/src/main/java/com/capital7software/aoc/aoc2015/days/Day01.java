package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.string.ApartmentBuilding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 1: Not Quite Lisp ---<br><br>
 * Santa was hoping for a white Christmas, but his weather machine's "snow" function is powered by stars,
 * and he's fresh out! To save Christmas, he needs you to collect fifty stars by December 25th.
 * <p><br>
 * Collect stars by helping Santa solve puzzles. Two puzzles will be made available on each day in the
 * Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 * <p><br>
 * Here's an easy puzzle to warm you up.
 * <p><br>
 * Santa is trying to deliver presents in a large apartment building, but he can't find the right
 * floor - the directions he got are a little confusing. He starts on the ground floor (floor 0)
 * and then follows the instructions one character at a time.
 * <p><br>
 * An opening parenthesis, (, means he should go up one floor, and a closing parenthesis, ),
 * means he should go down one floor.
 * <p><br>
 * The apartment building is very tall, and the basement is very deep; he will never find the top or bottom floors.
 * <p><br>
 * For example:
 * <p><br>
 * <code>
 * (()) and ()() both result in floor 0.<br>
 * ((( and (()(()( both result in floor 3.<br>
 * ))((((( also results in floor 3.<br>
 * ()) and ))( both result in floor -1 (the first basement level).<br>
 * ))) and )())()) both result in floor -3.<br>
 * </code>
 * <p><br>
 * Part 1: To what floor do the instructions take Santa?
 * <p><br>
 * Your puzzle answer was 280.
 * <p><br>
 * --- Part Two ---<br><br>
 * Now, given the same instructions, find the position of the first character that causes him to enter
 * the basement (floor -1). The first character in the instructions has position 1,
 * the second character has position 2, and so on.
 * <p><br>
 * For example:
 * <p><br>
 * <code>
 * ) causes him to enter the basement at character position 1.<br>
 * ()()) causes him to enter the basement at character position 5.<br>
 * </code>
 * <p><br>
 * What is the position of the character that causes Santa to first enter the basement?
 * <p><br>
 * Your puzzle answer was 1797.
 */
public class Day01 implements AdventOfCodeSolution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day01.class);

    /**
     * Instantiates the solution instance.
     */
    public Day01() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_01-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        long floor = getFloor(input);
        var end = Instant.now();

        LOGGER.info("Santa is on the {} floor!", floor);
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        long floor = getFirstBasementFloorPosition(input);
        var end = Instant.now();

        LOGGER.info("Position {} causes Santa to enter the basement for the first time!", floor);
        logTimings(LOGGER, start, end);
    }

    /**
     * Returns the floor Santa is on after following the instructions.
     *
     * @param input The instructions to follow.
     * @return The floor Santa is on after following the instructions.
     */
    public long getFloor(List<String> input) {
        var building = new ApartmentBuilding(input);
        return building.finalFloor();
    }

    /**
     * Returns the position of the item that sent Santa to the basement
     * for the first time.
     *
     * @param input The instructions to follow.
     * @return The index of the instruction that sent Santa to the basement.
     */
    public long getFirstBasementFloorPosition(List<String> input) {
        var building = new ApartmentBuilding(input);
        return building.firstBasementFloorPosition();
    }
}
