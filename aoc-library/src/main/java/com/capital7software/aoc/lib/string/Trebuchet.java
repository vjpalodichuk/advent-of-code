package com.capital7software.aoc.lib.string;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * The calibration document consists of lines of text; each line originally
 * contained a specific calibration value that the Elves now need to recover. On each line,
 * the calibration value can be found by combining the first digit and the last digit (in that order)
 * to form a single two-digit number.
 * <p><br>
 * For example:<br>
 * <ul>
 *     <li>
 *         1abc2
 *     </li>
 *     <li>
 *         pqr3stu8vwx
 *     </li>
 *     <li>
 *         a1b2c3d4e5f
 *     </li>
 *     <li>
 *         treb7uchet
 *     </li>
 * </ul>
 * In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.
 * <p><br>
 * Your puzzle answer was 54338.
 * <p><br>
 * Your calculation isn't quite right. It looks like some of the digits are actually spelled out w
 * ith letters: one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".
 * <p><br>
 * Equipped with this new information, you now need to find the real first and last digit on each line.
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         two1nine
 *     </li>
 *     <li>
 *         eightwothree
 *     </li>
 *     <li>
 *         abcone2threexyz
 *     </li>
 *     <li>
 *         xtwone3four
 *     </li>
 *     <li>
 *         4nineeightseven2
 *     </li>
 *     <li>
 *         zoneight234
 *     </li>
 *     <li>
 *         7pqrstsixteen
 *     </li>
 * </ul>
 * In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.
 *
 * @param input The calibration text to process.
 */
public record Trebuchet(@NotNull List<String> input) {
    private static final int ZERO = 0;
    private static final HashMap<String, Character> DIGITS_MAP = new HashMap<>() {{
        put("one", '1');
        put("two", '2');
        put("three", '3');
        put("four", '4');
        put("five", '5');
        put("six", '6');
        put("seven", '7');
        put("eight", '8');
        put("nine", '9');
    }};

    /**
     * Instantiates a new instance with the specified List of calibration input values.
     *
     * @param input The List of calibration values to parse.
     */
    public Trebuchet(@NotNull List<String> input) {
        this.input = new ArrayList<>(input);
    }

    /**
     * Returns an unmodifiable List of the input for this instance.
     * @return An unmodifiable List of the input for this instance.
     */
    public @NotNull List<String> input() {
        return Collections.unmodifiableList(input);
    }

    /**
     * Returns a List of calibration values parsed from the input.
     *
     * @param real If true, then words that are digits will count as a digit.
     * @return A List of calibration values parsed from the input.
     */
    public @NotNull List<Integer> getCalibrationValues(boolean real) {
        return input.stream().map(it -> extractTwoDigitNumber(it, real)).toList();
    }

    private static @NotNull Integer extractTwoDigitNumber(String input, boolean real) {
        if (input == null || input.isEmpty()) {
            return ZERO;
        }

        var results = extractDigits(input, real);
        char first = results.getFirst(), last = results.getLast();

        return Integer.parseInt("" + first + last);
    }

    private static @NotNull List<Character> extractDigits(@NotNull String input, boolean real) {
        var results = new ArrayList<Character>();
        var chars = input.toCharArray();
        var keys = DIGITS_MAP.keySet().stream().toList();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                results.add(chars[i]);
            } else if (real) {
                var sub = input.substring(i);
                for (String key : keys) {
                    if (sub.startsWith(key)) {
                        results.add(DIGITS_MAP.get(key));
                        break;
                    }
                }
            }
        }
        return results;
    }
}
