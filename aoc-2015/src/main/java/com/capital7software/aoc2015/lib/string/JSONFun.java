package com.capital7software.aoc2015.lib.string;

import org.jetbrains.annotations.NotNull;

/**
 * Various utility methods for working with JSON strings.
 */
public class JSONFun {
    /**
     * Finds and sums all the numbers in the specified JSON document. Any string values that
     * contain numbers are ignored.
     * <p>
     * For example:
     * <p>
     * <ul>
     *     <li>
     *         [1,2,3] and {"a":2,"b":4} both have a sum of 6.
     *     </li>
     *     <li>
     *         [[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
     *     </li>
     *     <li>
     *         {"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
     *     </li>
     *     <li>
     *         [] and {} both have a sum of 0.
     *     </li>
     * </ul>
     * @param input THe input JSON document. JSON validation is NOT performed.
     * @return The sum of numbers in the JSON string.
     */
    public static long SumOfAllNumbers(@NotNull String input) {
        var sum = 0L;

        if (input.isBlank()) {
            return sum;
        }

        var chars = input.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            var c = chars[i];

            if (c == '\"') {
                // Dealing with a string.
                i = skipPastQuote(input, i);
            } else if ((c >= '0' && c <= '9') ||
                    (c == '-' && i + 1 < chars.length && chars[i + 1] >= '0' && chars[i + 1] <= '9')) {
                // Dealing with a number.
                var newIndex = c == '-' ? findEndOfNumber(chars, i + 1) : findEndOfNumber(chars, i);

                if (newIndex >= i) {
                    sum += Long.parseLong(input, i, newIndex + 1, 10);
                }

                i = newIndex;
            }
        }
        return sum;
    }

    /**
     * Finds and sums all the numbers in the specified JSON document. Any string values that
     * contain numbers are ignored. Also ignores any objects and their children if they contain
     * a property with the value of red.
     * <p>
     * For example:
     * <p>
     * <ul>
     *     <li>
     *         [1,2,3] and {"a":2,"b":4} both still have a sum of 6.
     *     </li>
     *     <li>
     *         [[[3]]] and {"a":{"b":4},"c":-1} both still have a sum of 3.
     *     </li>
     *     <li>
     *         {"a":[-1,1]} and [-1,{"a":1}] both still have a sum of 0.
     *     </li>
     *     <li>
     *         [] and {} both still have a sum of 0.
     *     </li>
     *     <li>
     *         [1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
     *     </li>
     *     <li>
     *         {"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
     *     </li>
     *     <li>
     *         [1,"red",5] has a sum of 6, because "red" in an array has no effect.
     *     </li>
     * </ul>
     * @param input THe input JSON document. JSON validation is NOT performed.
     * @return The sum of numbers in the JSON string.
     */
    public static long SumOfAllNumbersSkippingObjectsWithRedPropertyValues(@NotNull String input) {
        var sum = 0L;

        if (input.isBlank()) {
            return sum;
        }

        var chars = input.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            var c = chars[i];

            if (c == '{') {
                if (i + 1 < chars.length) {
                    i = skipObjectsWithRedPropertyValues(chars, i + 1);
                }
            } else if (c == '\"') {
                // Dealing with a string.
                i = skipPastQuote(input, i);
            } else if ((c >= '0' && c <= '9') ||
                    (c == '-' && i + 1 < chars.length && chars[i + 1] >= '0' && chars[i + 1] <= '9')) {
                // Dealing with a number.
                var newIndex = c == '-' ? findEndOfNumber(chars, i + 1) : findEndOfNumber(chars, i);

                if (newIndex >= i) {
                    sum += Long.parseLong(input, i, newIndex + 1, 10);
                }

                i = newIndex;
            }
        }
        return sum;
    }

    private static int skipPastQuote(String input, int startIndex) {
        var index = startIndex;
        if (startIndex + 1 < input.length()) {
            var newIndex = input.indexOf('\"', startIndex + 1);
            if (newIndex > startIndex) {
                index = newIndex; // Skip past the closing quote!
            }
        }
        return index;
    }

    private static int skipObjectsWithRedPropertyValues(char[] chars, int startIndex) {
        var closingsNeeded = 1;

        var skip = false;

        var newIndex = startIndex;
        var inPropertyCheck = false;

        while (newIndex < chars.length && closingsNeeded > 0) {
            for (int i = startIndex; i < chars.length && closingsNeeded > 0; i++) {
                var c = chars[i];

                if (inPropertyCheck) {
                    if (c == '{') {
                        closingsNeeded++;
                    } else if (c == '}') {
                        closingsNeeded--;
                        newIndex = i;
                    } else if (c == '[' || c == ']') {
                        inPropertyCheck = false;
                    } else if (c == '\"' &&
                            closingsNeeded == 1 &&
                            i + 4 < chars.length &&
                            chars[i + 1] == 'r' &&
                            chars[i + 2] == 'e' &&
                            chars[i + 3] == 'd' &&
                            chars[i + 4] == '\"') {
                        skip = true;
                        i += 4;
                    } else if (c == ',') {
                        inPropertyCheck = false;
                    }
                } else {
                    if (c == '{') {
                        closingsNeeded++;
                    } else if (c == '}') {
                        closingsNeeded--;
                        newIndex = i;
                    } else if (c == ':' && !skip) {
                        inPropertyCheck = true;
                    }
                }
            }
        }

        if (skip) {
            return newIndex;
        } else {
            return startIndex - 1;
        }
    }

    private static int findEndOfNumber(char[] chars, int startIndex) {
        var index = startIndex;

        while (index < chars.length) {
            var c = chars[index];

            if (c >= '0' && c <= '9') {
                index++;
            } else {
                index--;
                break;
            }
        }

        return index;
    }
}
