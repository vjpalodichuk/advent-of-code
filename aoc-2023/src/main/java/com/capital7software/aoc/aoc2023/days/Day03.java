package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.GearRatios;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * --- Day 3: Gear Ratios ---<br><br>
 * You and the Elf eventually reach a gondola lift station; he says the gondola lift will
 * take you up to the water source, but this is as far as he can bring you. You go inside.
 * <p><br>
 * It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.
 * <p><br>
 * <b>"Aaah!"</b>
 * <p><br>
 * You turn around to see a slightly-greasy Elf with a wrench and a look of surprise.
 * "Sorry, I wasn't expecting anyone! The gondola lift isn't working right now;
 * it'll still be a while before I can fix it." You offer to help.
 * <p><br>
 * The engineer explains that an engine part seems to be missing from the engine,
 * but nobody can figure out which one. If you can add up all the part numbers in
 * the engine schematic, it should be easy to work out which part is missing.
 * <p><br>
 * The engine schematic (your puzzle input) consists of a visual representation of the engine.
 * There are lots of numbers and symbols you don't really understand, but apparently any number
 * adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum.
 * (Periods (.) do not count as a symbol.)
 * <p><br>
 * Here is an example engine schematic:
 * <p><br>
 * <code>
 * 467..114..<br>
 * ...*......<br>
 * ..35..633.<br>
 * ......#...<br>
 * 617*......<br>
 * .....+.58.<br>
 * ..592.....<br>
 * ......755.<br>
 * ...$.*....<br>
 * .664.598..<br>
 * </code>
 * <p><br>
 * In this schematic, two numbers are not part numbers because they are not adjacent to
 * a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a
 * symbol and so is a part number; their sum is 4361.
 * <p><br>
 * Of course, the actual engine schematic is much larger. What is the sum of all the
 * part numbers in the engine schematic?
 * <p><br>
 * Your puzzle answer was 529618.
 * <p><br>
 * --- Part Two ---<br><br>
 * The engineer finds the missing part and installs it in the engine! As the engine springs
 * to life, you jump in the closest gondola, finally ready to ascend to the water source.
 * <p><br>
 * You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately,
 * the gondola has a phone labeled "help", so you pick it up and the engineer answers.
 * <p><br>
 * Before you can explain the situation, she suggests that you look out the window.
 * There stands the engineer, holding a phone in one hand and waving with the other.
 * You're going so slowly that you haven't even left the station. You exit the gondola.
 * <p><br>
 * The missing part wasn't the only issue - one of the gears in the engine is wrong.
 * A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is
 * the result of multiplying those two numbers together.
 * <p><br>
 * This time, you need to find the gear ratio of every gear and add them all up so that
 * the engineer can figure out which gear needs to be replaced.
 * <p><br>
 * Consider the same engine schematic again:
 * <p><br>
 * <code>
 * 467..114..<br>
 * ...*......<br>
 * ..35..633.<br>
 * ......#...<br>
 * 617*......<br>
 * .....+.58.<br>
 * ..592.....<br>
 * ......755.<br>
 * ...$.*....<br>
 * .664.598..<br>
 * </code>
 * <p><br>
 * In this schematic, there are two gears. The first is in the top left; it has part
 * numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right;
 * its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent
 * to one part number.) Adding up all the gear ratios produces 467835.
 * <p><br>
 * What is the sum of all the gear ratios in your engine schematic?
 * <p><br>
 * Your puzzle answer was 77509019.
 *
 */
public class Day03 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day03.class.getName());

    /**
     * Instantiates the solution instance.
     */
    public Day03() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_03-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var sum = getSumOfAllPartNumbersInSchematic(input);
        var end = Instant.now();

        LOGGER.info(String.format("The sum of all part numbers in the schematic is: %d%n", sum));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var sum = getSumOfAllGearRatiosInSchematic(input);
        var end = Instant.now();

        LOGGER.info(String.format("The sum of all gear ratios in the schematic is: %d%n", sum));
        logTimings(LOGGER, start, end);
    }

    /**
     * Returns the sum of all PartNumbers found in the specified schematic.
     *
     * @param schematic The schematic to find the PartNumbers in.
     * @return The sum of all PartNumbers found in the specified schematic.
     */
    public long getSumOfAllPartNumbersInSchematic(List<String> schematic) {
        var instance = new GearRatios();

        return instance.findAllPartNumbers(schematic)
                .stream()
                .mapToLong(GearRatios.PartNumber::id)
                .sum();
    }

    /**
     * Returns the sum of all gear ratios from the gears found in the specified schematic.
     *
     * @param schematic The schematic to find the PartNumbers in.
     * @return The sum of all gear ratios from the gears found in the specified schematic.
     */
    public long getSumOfAllGearRatiosInSchematic(List<String> schematic) {
        var instance = new GearRatios();

        return instance.findAllGearRatios(instance.findAllPartNumbers(schematic))
                .stream()
                .mapToLong(it -> it)
                .sum();
    }
}
