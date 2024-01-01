package com.capital7software.aoc2015.lib.grid;

import java.util.List;
import java.util.stream.Stream;

/**
 * Because your neighbors keep defeating you in the holiday house decorating contest year after year,
 * you've decided to deploy one million lights in a 1000x1000 grid.
 * <p>
 * The Christmas lights are arranged using a Grid and controlled with LightInstructions.
 * <p>
 * Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are
 * at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or
 * toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents
 * opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore
 * refers to 9 lights in a 3x3 square. The lights all start turned off.
 * <p>
 * @param grid The grid that holds all the lights.
 * @param instructions The list of instructions to apply to the light grid.
 */
public record ChristmasLights(Grid2D<Integer> grid, List<LightInstruction> instructions) {
    /**
     * Builds the ChristmasLights in a grid of the specified size and parses and loads the
     * specified raw lighting instructions.
     *
     * @param columns The number of columns in the grid.
     * @param rows The number of rows in the grid.
     * @param rawInstructions The raw lighting instructions to parse and load.
     * @return A new ChristmasLights instance in the specified size and loaded with the parsed
     *         instructions.
     */
    public static ChristmasLights build(int columns, int rows, List<String> rawInstructions) {
        var items = new Integer[columns * rows];

        var grid = new Grid2D<>(columns, rows, items, 0);
        var lights = new ChristmasLights(grid, LightInstruction.parse(rawInstructions));
        lights.reset();

        return lights;
    }

    /**
     * Turns all the lights in the grid off.
     */
    public void reset() {
        grid.fill(0);
    }

    /**
     * Applies all the loaded LightInstruction instructions in this instance.
     * <p>
     * For example:
     * <p>
     * turn on 0,0 through 999,999 would turn on (or leave on) every light.
     * toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones
     * that were on, and turning on the ones that were off.
     * turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
     * After following the instructions, how many lights are lit?
     * <p>
     */
    public void applyInstructions() {
        for (var instruction : instructions) {
            if (instruction.type() == LightInstructionType.TURN_ON) {
                grid.set(instruction.point1(), instruction.point2(), 1);
            } else if (instruction.type() == LightInstructionType.TURN_OFF) {
                grid.set(instruction.point1(), instruction.point2(), 0);
            } else {
                grid.toggle(instruction.point1(), instruction.point2(), 1, 0);
            }
        }
    }

    /**
     * Applies all the loaded LightInstruction instructions in this instance using the new rules.
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
     * For example:
     * <p>
     * turn on 0,0 through 0,0 would increase the total brightness by 1.
     * toggle 0,0 through 999,999 would increase the total brightness by 2000000.
     * <p>
     */
    public void applyNewInterpretationOfInstructions() {
        for (var instruction : instructions) {
            if (instruction.type() == LightInstructionType.TURN_ON) {
                grid.adjustBy(instruction.point1(), instruction.point2(), current -> current + 1);
            } else if (instruction.type() == LightInstructionType.TURN_OFF) {
                grid.adjustBy(instruction.point1(), instruction.point2(), current -> Math.max(0, current - 1));
            } else {
                grid.adjustBy(instruction.point1(), instruction.point2(), current -> current + 2);
            }
        }
    }

    /**
     *
     * @return The count of lights that are currently on.
     */
    public long getOnLightCount() {
        return Stream.of(grid().items()).filter(it -> it != 0).count();
    }

    /**
     *
     * @return The total brightness of all the lights that are on.
     */
    public long getTotalBrightness() {
        return Stream.of(grid().items()).mapToLong(it -> (long) it).sum();
    }
}
