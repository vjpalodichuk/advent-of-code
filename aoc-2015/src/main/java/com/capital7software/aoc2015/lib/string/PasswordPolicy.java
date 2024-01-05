package com.capital7software.aoc2015.lib.string;

import org.jetbrains.annotations.NotNull;

/**
 * <ul>
 *     <li>
 *         Passwords must include one increasing straight of at least three letters,
 *         like abc, bcd, cde, and so on, up to xyz. They cannot skip letters; abd doesn't count.
 *     </li>
 *     <li>
 *         Passwords may not contain the letters i, o, or l, as these letters can
 *         be mistaken for other characters and are therefore confusing.
 *     </li>
 *     <li>
 *         Passwords must contain at least two different, non-overlapping pairs
 *         of letters, like aa, bb, or zz.
 *     </li>
 * </ul>
 * <p>
 * For example:
 * <p>
 * <ul>
 *     <li>
 *         hijklmmn meets the first requirement (because it contains the straight hij)
 *         but fails the second requirement (because it contains i and l).
 *     </li>
 *     <li>
 *         abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
 *     </li>
 *     <li>
 *         abbcegjk fails the third requirement, because it only has one double letter (bb).
 *     </li>
 *     <li>
 *         The next password after abcdefgh is abcdffaa.
 *     </li>
 *     <li>
 *         The next password after ghijklmn is ghjaabcc, because you eventually skip
 *         all the passwords that start with ghi..., since i is not allowed.
 *     </li>
 * </ul>
 */
public class PasswordPolicy {
    /**
     * Returns true if the specified input is a valid password.
     * <p>
     *     <ul>
     *         <li>
     *             Passwords must include one increasing straight of at least three letters,
     *             like abc, bcd, cde, and so on, up to xyz. They cannot skip letters; abd doesn't count.
     *         </li>
     *         <li>
     *             Passwords may not contain the letters i, o, or l, as these letters can
     *             be mistaken for other characters and are therefore confusing.
     *         </li>
     *         <li>
     *             Passwords must contain at least two different, non-overlapping pairs
     *             of letters, like aa, bb, or zz.
     *         </li>
     *     </ul>
     *
     * @param input The password to validate.
     * @return True if the specified input is a valid password.
     */
    public static boolean isValidPassword(@NotNull String input) {
        if (input.isBlank()) {
            return false;
        }
        var chars = input.toCharArray();

        return isValidPassword(chars);
    }

    /**
     * Returns true if the specified input is a valid password.
     * <p>
     *     <ul>
     *         <li>
     *             Passwords must include one increasing straight of at least three letters,
     *             like abc, bcd, cde, and so on, up to xyz. They cannot skip letters; abd doesn't count.
     *         </li>
     *         <li>
     *             Passwords may not contain the letters i, o, or l, as these letters can
     *             be mistaken for other characters and are therefore confusing.
     *         </li>
     *         <li>
     *             Passwords must contain at least two different, non-overlapping pairs
     *             of letters, like aa, bb, or zz.
     *         </li>
     *     </ul>
     *
     * @param chars The password to validate.
     * @return True if the specified input is a valid password.
     */
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

    /**
     * Returns a string that is the next valid password. If no valid password can be found
     * then an IndexOutOfBoundsException will be thrown.
     *
     * @param input The current password to suggest a new one for.
     * @throws IndexOutOfBoundsException will be thrown if the string rolls over past it's
     *         maximum value; which would be the case if all characters were the letter z.
     * @return The next valid password.
     */
    @NotNull
    public static String suggestNextPassword(@NotNull String input) throws IndexOutOfBoundsException {
        if (input.isBlank()) {
            return "";
        }
        var chars = input.toCharArray();

        var done = false;

        while(!done) {
            done = isValidPassword(increment(chars, 1));
        }

        return String.valueOf(chars);
    }

    /**
     * Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on.
     * Increase the rightmost letter one step; if it was z, it wraps around to a, and
     * repeat with the next letter to the left until one doesn't wrap around.
     * <p>
     * This is a recursive method. When making the initial call it is important to pass
     * 1 as the pos value. This is because pos if the 1-based index from the end of
     * the input array.
     * <p>
     * An IndexOutOfBoundsException will be thrown if the string rolls over past it's
     * maximum value; which would be the case if all characters were the letter z.
     *
     * @param input The string to increment.
     * @param pos The 1-based index from the end of the string. So 1 is the last character.
     * @throws IndexOutOfBoundsException will be thrown if the string rolls over past it's
     *         maximum value; which would be the case if all characters were the letter z.
     * @return The input array.
     */
    public static char @NotNull [] increment(char @NotNull [] input, int pos) throws IndexOutOfBoundsException {
        var length = input.length;
        if (pos < 1 || pos > length) {
            throw new IndexOutOfBoundsException("pos is outside the bounds of input: " + pos);
        }
        var index = length - pos;
        var c = input[index];

        // Are we wrapping around?
        if (c == 'z') {
            input[index] = 'a';
            // Then we also need to increment the character to the left
            increment(input, pos + 1);
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
