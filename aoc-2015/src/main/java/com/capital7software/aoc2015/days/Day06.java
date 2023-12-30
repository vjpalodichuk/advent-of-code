package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.Grid2D;
import com.capital7software.aoc2015.lib.Point2D;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * --- Day 6: Probably a Fire Hazard ---
 * Because your neighbors keep defeating you in the holiday house decorating contest year after year,
 * you've decided to deploy one million lights in a 1000x1000 grid.
 * <p>
 * Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to
 * display the ideal lighting configuration.
 * <p>
 * Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are
 * at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or
 * toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents
 * opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore
 * refers to 9 lights in a 3x3 square. The lights all start turned off.
 * <p>
 * To defeat your neighbors this year, all you have to do is set up your lights by doing the
 * instructions Santa sent you in order.
 * <p>
 * For example:
 * <p>
 * turn on 0,0 through 999,999 would turn on (or leave on) every light.
 * toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones
 * that were on, and turning on the ones that were off.
 * turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
 * After following the instructions, how many lights are lit?
 * <p>
 *     Your puzzle answer was 377891.
 * <p>
 * --- Part Two ---
 * You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from Ancient Nordic Elvish.
 * <p>
 * The light grid you bought actually has individual brightness controls; each light can have a brightness of zero or more. The lights all start at zero.
 * <p>
 * The phrase turn on actually means that you should increase the brightness of those lights by 1.
 * <p>
 * The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.
 * <p>
 * The phrase toggle actually means that you should increase the brightness of those lights by 2.
 * <p>
 * What is the total brightness of all lights combined after following Santa's instructions?
 * <p>
 * For example:
 * <p>
 * turn on 0,0 through 0,0 would increase the total brightness by 1.
 * toggle 0,0 through 999,999 would increase the total brightness by 2000000.
 */
public class Day06 implements AdventOfCodeSolution {
    public enum LightInstructionType {
        TURN_ON("turn on"),
        TURN_OFF("turn off"),
        TOGGLE("toggle");

        private final String label;

        LightInstructionType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public record LightInstruction(LightInstructionType type, Point2D<Integer> point1, Point2D<Integer> point2) {
        public static List<LightInstruction> parse(List<String> input) {
            return input
                    .stream()
                    .map(LightInstruction::parseLine)
                    .filter(Objects::nonNull)
                    .toList();
        }

        public static LightInstruction parseLine(String line) {
            if (line == null || line.isBlank()) {
                return null;
            }

            var instructionTypes = LightInstructionType.values();

            for (var type : instructionTypes) {
                int index;

                if (line.startsWith(type.getLabel())) {
                    index = type.getLabel().length() + 1;
                } else {
                    continue;
                }

                var coords = line.substring(index).split(" through ");
                var first = coords[0].split(",");
                var second = coords[1].split(",");
                var x1 = Integer.parseInt(first[0].trim());
                var y1 = Integer.parseInt(first[1].trim());
                var x2 = Integer.parseInt(second[0].trim());
                var y2 = Integer.parseInt(second[1].trim());

                return new LightInstruction(type, new Point2D<>(x1, y1), new Point2D<>(x2, y2));
            }
            return null;
        }
    }

    public record ChristmasLights(Grid2D<Integer> grid, List<LightInstruction> instructions) {
        public void reset() {
            grid.fill(0);
        }

        public void applyInstructions() {
            for (var instruction : instructions) {
                if (instruction.type == LightInstructionType.TURN_ON) {
                    grid.set(instruction.point1(), instruction.point2(), 1);
                } else if (instruction.type == LightInstructionType.TURN_OFF) {
                    grid.set(instruction.point1(), instruction.point2(), 0);
                } else {
                    grid.toggle(instruction.point1(), instruction.point2(), 1, 0);
                }
            }
        }

        public void applyNewInterpretationOfInstructions() {
            for (var instruction : instructions) {
                if (instruction.type == LightInstructionType.TURN_ON) {
                    grid.adjustBy(instruction.point1(), instruction.point2(), current -> current + 1);
                } else if (instruction.type == LightInstructionType.TURN_OFF) {
                    grid.adjustBy(instruction.point1(), instruction.point2(), current -> Math.max(0, current - 1));
                } else {
                    grid.adjustBy(instruction.point1(), instruction.point2(), current -> current + 2);
                }
            }
        }

        public long getOnLightCount() {
            return Stream.of(grid().items()).filter(it -> it != 0).count();
        }

        public long getTotalBrightness() {
            return Stream.of(grid().items()).mapToLong(it -> (long) it).sum();
        }
    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_06-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {

        var start = Instant.now();
        var instructions = loadInstructions(input);
        var lights = loadLights(instructions, 1_000, 1_000);
        lights.applyInstructions();

        var total = getOnLightCount(lights);

        var end = Instant.now();
        System.out.printf(
                "There are %d lights Lit!%n", total);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {

        var start = Instant.now();
        var instructions = loadInstructions(input);
        var lights = loadLights(instructions, 1_000, 1_000);
        lights.applyNewInterpretationOfInstructions();

        var total = getTotalBrightness(lights);

        var end = Instant.now();
        System.out.printf(
                "Total brightness of the lights is: %d%n", total);
        printTiming(start, end);
    }

    public List<LightInstruction> loadInstructions(List<String> input) {
        return LightInstruction.parse(input);
    }

    public ChristmasLights loadLights(List<LightInstruction> instructions, int columns, int rows) {
        var items = new Integer[columns * rows];

        var grid = new Grid2D<>(columns, rows, items, 0);
        var lights = new ChristmasLights(grid, instructions);
        lights.reset();
        return lights;
    }

    public long getOnLightCount(ChristmasLights lights) {
        return lights.getOnLightCount();
    }

    public long getTotalBrightness(ChristmasLights lights) {
        return lights.getTotalBrightness();
    }
}
