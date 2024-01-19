package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.grid.ChristmasLights;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * --- Day 6: Probably a Fire Hazard ---<br>
 * Because your neighbors keep defeating you in the holiday house decorating contest year after year,
 * you've decided to deploy one million lights in a 1000x1000 grid.
 * <p><br>
 * Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to
 * display the ideal lighting configuration.
 * <p><br>
 * Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are
 * at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or
 * toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents
 * opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore
 * refers to 9 lights in a 3x3 square. The lights all start turned off.
 * <p><br>
 * To defeat your neighbors this year, all you have to do is set up your lights by doing the
 * instructions Santa sent you in order.
 * <p><br>
 * For example:
 * <p><br>
 * turn on 0,0 through 999,999 would turn on (or leave on) every light.<br>
 * toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones
 * that were on, and turning on the ones that were off.<br>
 * turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.<br>
 * <p><br>
 * After following the instructions, how many lights are lit?
 * <p>
 * Your puzzle answer was 377891.
 * <p>
 * --- Part Two ---<br>
 * You just finish implementing your winning light pattern when you realize you mistranslated Santa's
 * message from Ancient Nordic Elvish.
 * <p>
 * The light grid you bought actually has individual brightness controls; each light can have a
 * brightness of zero or more. The lights all start at zero.
 * <p>
 * The phrase turn on actually means that you should increase the brightness of those lights by 1.
 * <p>
 * The phrase turn off actually means that you should decrease the brightness of those lights by 1,
 * to a minimum of zero.
 * <p>
 * The phrase toggle actually means that you should increase the brightness of those lights by 2.
 * <p>
 * What is the total brightness of all lights combined after following Santa's instructions?
 * <p>
 * For example:
 * <p>
 * turn on 0,0 through 0,0 would increase the total brightness by 1.<br>
 * toggle 0,0 through 999,999 would increase the total brightness by 2000000.<br>
 * <p>
 * Your puzzle answer was 14110788.
 */
public class Day06 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day06.class.getName());

    /**
     * Instantiates the solution instance.
     */
    public Day06() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_06-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {

        var start = Instant.now();
        var lights = loadLights(input, 1_000, 1_000);
        lights.applyInstructions();

        var total = getOnLightCount(lights);

        var end = Instant.now();
        LOGGER.info(String.format(
                "There are %d lights Lit!%n", total));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {

        var start = Instant.now();
        var lights = loadLights(input, 1_000, 1_000);
        lights.applyNewInterpretationOfInstructions();

        var total = getTotalBrightness(lights);

        var end = Instant.now();
        LOGGER.info(String.format(
                "Total brightness of the lights is: %d%n", total));
        logTimings(LOGGER, start, end);
    }

    /**
     * Loads the lighting instructions and returns a ChristLights instance setup with those
     * instructions on a grid with the specified dimensions.
     *
     * @param rawInstructions The lighting instructions to parse.
     * @param columns         The number of columns in the lighting grid.
     * @param rows            The number of rows in the lighting grid.
     * @return A ChristLights instance setup with the specified instructions on a grid
     * with the specified dimensions.
     */
    public ChristmasLights loadLights(List<String> rawInstructions, int columns, int rows) {
        return ChristmasLights.buildFromLightingInstructions(columns, rows, rawInstructions);
    }

    /**
     * Returns the number of lights that are on.
     *
     * @param lights The ChristmasLights.
     * @return The number of lights that are on.
     */
    public long getOnLightCount(ChristmasLights lights) {
        return lights.getOnLightCount();
    }

    /**
     * Returns the total brightness of the ChristmasLights setup.
     *
     * @param lights The ChristmasLights.
     * @return The total brightness of the ChristmasLights setup.
     */
    public long getTotalBrightness(ChristmasLights lights) {
        return lights.getTotalBrightness();
    }
}
