package com.capital7software.aoc.lib.string;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Stores a copy of the specified input String that can then be hashed at will.
 *
 * @param string The String to save to operate on.
 */
public record HashString(String string) {

    /**
     * The HASH algorithm is a way to turn any string of characters into a single number in the range 0 to 255.
     * To run the HASH algorithm on a string, start with a current value of 0. Then, for each character in
     * the string starting from the beginning:
     * <p><br>
     * <ul>
     *     <li>
     *         Determine the ASCII code for the current character of the string.
     *     </li>
     *     <li>
     *         Increase the current value by the ASCII code you just determined.
     *     </li>
     *     <li>
     *         Set the current value to itself multiplied by 17.
     *     </li>
     *     <li>
     *         Set the current value to the remainder of dividing itself by 256.
     *     </li>
     * </ul>
     * <p><br>
     * After following these steps for each character in the string in order, the current value is
     * the output of the HASH algorithm.
     *
     * @return The hash of this HashString.
     */
    public int hashAscii() {
        var characters = string.getBytes(StandardCharsets.US_ASCII);

        var hash = 0L;

        for (var character : characters) {
            hash += character;
            hash *= 17;
            hash %= 256;
        }

        return (int) hash;
    }

    /**
     * Builds and returns a List of HashString parsed from the specified List of input Strings.
     *
     * @param input The List of String to parse and hash.
     * @return A List of HashString parsed from the specified List of input Strings.
     */
    public static List<HashString> parse(List<String> input) {
        var builder = new StringBuilder();

        input.forEach(builder::append);

        var temp = builder.toString().replace("\n", "");
        return Arrays.stream(temp.split(",")).map(HashString::new).toList();
    }
}
