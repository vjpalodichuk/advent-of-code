package com.capital7software.aoc.lib.string;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Simple Run Length Encoder (RLE) for numeric strings.
 * <p>
 * This class only works on string where all characters are the digits 0 - 9. Any other character in
 * the input string will cause the call to encode to fail.
 *
 */
public class RunLengthEncoder {
    public static final Pattern DIGITS_ONLY = Pattern.compile("^\\d+$");

    /**
     * Run Length Encodes (RLE) the numeric input string. If the string is blank or
     * contains any characters other than the digits 0 - 9, the empty string is returned.
     * <p>
     * For example:
     * <ul>
     *     <li>
     *         1 becomes 11 (1 copy of digit 1).
     *     </li>
     *     <li>
     *         11 becomes 21 (2 copies of digit 1).
     *     </li>
     *     <li>
     *         21 becomes 1211 (one 2 followed by one 1).
     *     </li>
     *     <li>
     *         1211 becomes 111221 (one 1, one 2, and two 1s).
     *     </li>
     *     <li>
     *         111221 becomes 312211 (three 1s, two 2s, and one 1).
     *     </li>
     * </ul>
     *
     * @param input The numeric string to encode.
     * @return The encoded string or an empty string if the input is not all digits.
     */
    @NotNull
    public static String encode(@NotNull String input) {
        if (input.isBlank()) {
            return "";
        }

        if (!DIGITS_ONLY.matcher(input).matches()) {
            return "";
        }

        char previous = 0;

        var result = new StringBuilder(input.length() * 2);
        var count = 0;

        for (var c : input.toCharArray()) {
            count++;
            if (previous == 0) {
                previous = c;
                count = 0;
                continue;
            }
            if (previous != c) {
                result.append(count);
                result.append(previous);
                previous = c;
                count = 0;
            }
        }

        // Add the last one!
        result.append(count + 1);
        result.append(previous);
        return result.toString();
    }
}
