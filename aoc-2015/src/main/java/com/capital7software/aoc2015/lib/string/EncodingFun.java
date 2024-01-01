package com.capital7software.aoc2015.lib.string;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * It is important to realize the difference between the number of characters in the code
 * representation of the string literal and the number of characters in the in-memory string itself.
 * <p>
 * For example:
 * <p>
 * "" is 2 characters of code (the two double quotes), but the string contains zero characters.
 * "abc" is 5 characters of code, but 3 characters in the string data.
 * "aaa\"aaa" is 10 characters of code, but the string itself contains six "a" characters and a single,
 * escaped quote character, for a total of 7 characters in the string data.
 * "\x27" is 6 characters of code, but the string itself contains just one -
 * an apostrophe ('), escaped using hexadecimal notation.
 * <p>
 * Santa's list is a file that contains many double-quoted string literals, one on each line.
 * The only escape sequences used are \\ (which represents a single backslash),
 * \" (which represents a lone double-quote character), and \x plus two hexadecimal characters
 * (which represents a single character with that ASCII code).
 */
public record EncodingFun() {
    /**
     * The only escape sequences used are \\ (which represents a single backslash), \"
     * (which represents a lone double-quote character), and \x plus two hexadecimal
     * characters (which represents a single character with that ASCII code).
     * <p>
     * For example, given the four strings above, the total number of characters of
     * string code (2 + 5 + 10 + 6 = 23) minus the total number of characters in memory for
     * string values (0 + 3 + 7 + 1 = 11) is 23 - 11 = 12.
     *
     * @param sequence The sequence of characters to count the number of code characters of.
     * @return The number of code characters.
     */
    public static int countCode(@NotNull String sequence) {
        // In Java the number of code characters is just the length of the string!
        return sequence.length();
    }

    /**
     * The only escape sequences used are \\ (which represents a single backslash), \"
     * (which represents a lone double-quote character), and \x plus two hexadecimal
     * characters (which represents a single character with that ASCII code).
     * <p>
     * For example, given the four strings above, the total number of characters of
     * string code (2 + 5 + 10 + 6 = 23) minus the total number of characters in memory for
     * string values (0 + 3 + 7 + 1 = 11) is 23 - 11 = 12.
     *
     * @param sequence The sequence of characters to count the in-memory characters of.
     * @return The number of in-memory characters.
     */
    public static int countMemory(@NotNull String sequence) {
        if (sequence.isEmpty()) {
            return 0;
        }

        char[] chars = sequence.toCharArray();
        int length = chars.length;
        int count = 0;
        var pattern = getHexCodePattern();

        for (int i = 0; i < length; i++) {
            if (isQuote(chars[i])) {
                // Quotes are not in-memory unless part of an escape sequence
                continue;
            } else if (isBackslashChar(chars[i])) {
                // Could be the start of an escape sequence.
                if (i + 1 < length) {
                    char aChar = chars[i + 1];
                    if (isQuote(aChar) || isBackslashChar(aChar)) {
                        // embedded quote or backslash
                        i++; // Don't count it again!
                    } else if (aChar == 'x' && i + 3 < length) {
                        // Possible hex char
                        var hexString = "" + chars[i + 2] + "" + chars[i + 3];

                        if (pattern.matcher(hexString).matches()) {
                            // it is a hex string!
                            i += 3; // Don't count it again!
                        }
                    }
                }
            }
            count++;
        }

        return count;
    }

    /**
     * Encodes the sequence as a new string, including the surrounding double quotes.
     * <p>
     * For example:
     * <p>
     * "" encodes to "\"\"", an increase from 2 characters to 6.
     * "abc" encodes to "\"abc\"", an increase from 5 characters to 9.
     * "aaa\"aaa" encodes to "\"aaa\\\"aaa\"", an increase from 10 characters to 16.
     * "\x27" encodes to "\"\\x27\"", an increase from 6 characters to 11.
     *
     * @param sequence The sequence of characters to encode.
     * @return The encoded sequence of characters.
     */
    public static String encodeString(@NotNull String sequence) {
        if (sequence.isEmpty()) {
            return "";
        }

        var results = new StringBuilder();

        char[] chars = sequence.toCharArray();
        int length = chars.length;
        var pattern = getHexCodePattern();

        for (int i = 0; i < length; i++) {
            results.append(chars[i]);
            if (isQuote(chars[i])) {
                results.append("\\\"");
            } else if (isBackslashChar(chars[i])) {
                // Could be the start of an escape sequence.
                results.append("\\");
                if (i + 1 < length) {
                    char aChar = chars[i + 1];
                    if (isQuote(aChar)) {
                        // embedded quote
                        results.append("\\\"");
                        i++; // Don't count it again!
                    } else if (isBackslashChar(aChar)) {
                        // embedded backslash
                        results.append("\\\\");
                        i++; // Don't count it again!
                    } else if (aChar == 'x' && i + 3 < length) {
                        // Possible hex char
                        var hexString = "" + chars[i + 2] + "" + chars[i + 3];

                        if (pattern.matcher(hexString).matches()) {
                            // it is a hex string!
                            results.append(chars[i + 1]).append(chars[i + 2]).append(chars[i + 3]);
                            i += 3; // Don't count it again!
                        }
                    }
                }
            }
        }

        return results.toString();
    }

    private static Pattern getHexCodePattern() {
        return Pattern.compile("^[a-fA-F0-9]{2}$");
    }

    private static boolean isBackslashChar(char c) {
        return c == '\\';
    }

    private static boolean isQuote(char c) {
        return c == '\"';
    }
}
