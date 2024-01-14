package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.geometry.Point2D;

import java.util.List;
import java.util.Objects;

/**
 * A complete lighting instruction to apply to a grid. The two points are opposite corners of a rectangle that this
 * instruction applies to.
 *
 * @param type The type of instruction (ON, OFF, TOGGLE).
 * @param point1 The first point in the rectangle this instruction applies to.
 * @param point2 The second inclusive point in the rectangle (opposite corner) this instruction applies to.
 */
public record LightInstruction(LightInstructionType type, Point2D<Integer> point1, Point2D<Integer> point2) {
    /**
     * Parses the raw instructions and returns a list of LightInstruction.
     * <p>
     * For example:
     * <p>
     * turn on 0,0 through 999,999
     * <p>
     * turn off 0,0 through 999,999
     * <p>
     * toggle 0,0 through 999,999
     * <p>
     * @param input The raw instructions.
     * @return The list of parsed LightInstructions.
     */
    public static List<LightInstruction> parse(List<String> input) {
        return input
                .stream()
                .map(LightInstruction::parseLine)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Parses the raw instruction and returns a LightInstruction.
     * <p>
     * For example:
     * <p>
     * turn on 0,0 through 999,999
     * <p>
     * turn off 0,0 through 999,999
     * <p>
     * toggle 0,0 through 999,999
     * <p>
     * @param line The raw instructions.
     * @return The parsed LightInstruction.
     */
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
