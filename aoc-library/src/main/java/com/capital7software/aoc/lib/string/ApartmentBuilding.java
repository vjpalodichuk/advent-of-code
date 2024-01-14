package com.capital7software.aoc.lib.string;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Santa follows the provided instructions to deliver presents at this apartment building.
 * <p>
 * An opening parenthesis, (, means he should go up one floor, and a closing parenthesis, ),
 * means he should go down one floor.
 * <p>
 * For example:
 * <p>
 * (()) and ()() both result in floor 0.
 * ((( and (()(()( both result in floor 3.
 * ))((((( also results in floor 3.
 * ()) and ))( both result in floor -1 (the first basement level).
 * ))) and )())()) both result in floor -3.
 *
 * @param instructions The list of instructions to follow.
 */
public record ApartmentBuilding(@NotNull List<String> instructions) {
    public ApartmentBuilding(@NotNull List<String> instructions) {
        this.instructions = new ArrayList<>(instructions);
    }

    @Override
    public @NotNull List<String> instructions() {
        return Collections.unmodifiableList(instructions);
    }

    /**
     * The final floor Santa is on after following the instructions.
     *
     * @return The final floor Santa is on.
     */
    public long finalFloor() {
        var openCount = 0L;
        var closedCount = 0L;

        for (var line : instructions) {
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

    /**
     * Finds the position of the first character that causes Santa to enter
     * the basement (floor -1). The first character in the instructions has position 1,
     * the second character has position 2, and so on.
     *
     * @return the position of the first character that causes Santa to enter the basement
     * or -1 if he never enters the basement.
     */
    public long firstBasementFloorPosition() {
        var openCount = 0L;
        var closedCount = 0L;

        for (var line : instructions) {
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
