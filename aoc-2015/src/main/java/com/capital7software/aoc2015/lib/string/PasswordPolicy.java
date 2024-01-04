package com.capital7software.aoc2015.lib.string;

import org.jetbrains.annotations.NotNull;

public class PasswordPolicy {
    public static boolean isValidPassword(@NotNull String input) {
        if (input.isBlank()) {
            return false;
        }
        var chars = input.toCharArray();

        return isValidPassword(chars);
    }

    public static boolean isValidPassword(char @NotNull [] chars) {
        var badChars = 0;
        var straightCount = 0;
        var pairCount = 0;

        char pairOneChar = 0;
        char pairTwoChar = 0;

        for (int i = 0; i < chars.length; i++) {
            var c = chars[i];
            if (c == 'i' || c == 'l' || c == 'o') {
                badChars++;
            }

            if (straightCount == 0 && i + 2 < chars.length) {
                if (c + 2 == chars[i + 1] + 1 && chars[i + 1] + 1 == chars[i + 2]) {
                    straightCount++;
                }
            }

            if (pairCount < 2 && i + 1 < chars.length) {
                if (c == chars[i + 1]) {
                    if (pairOneChar == 0) {
                        pairOneChar = c;
                        pairCount++;
                    } else if (pairTwoChar == 0 && c != pairOneChar) {
                        pairTwoChar = c;
                        pairCount++;
                    }
                }
            }

        }

        return badChars == 0 && straightCount > 0 && pairCount > 1;

    }

    @NotNull
    public static String suggestNextPassword(@NotNull String input) {
        if (input.isBlank()) {
            return "";
        }
        var chars = input.toCharArray();

        var done = false;

        while(!done) {
            done = isValidPassword(increment(chars, chars.length, 1));
        }

        return String.valueOf(chars);
    }

    private static char @NotNull [] increment(char @NotNull [] input, int length, int pos) {
        if (pos < 1 || pos > length) {
            throw new IndexOutOfBoundsException("pos is outside the bounds of input: " + pos);
        }
        var index = length - pos;
        var c = input[index];

        // Are we wrapping around?
        if (c == 'z') {
            input[index] = 'a';
            // Then we also need to increment the character to the left
            increment(input, length, pos + 1);
        } else {
            // Increment
            c++;
            // Skip bad chars.
            if (c == 'i' || c == 'l' || c == 'o') {
                c++;
            }
            input[index] = c;
        }
        return input;
    }
}
