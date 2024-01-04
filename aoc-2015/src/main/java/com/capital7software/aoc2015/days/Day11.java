package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.string.PasswordPolicy;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 11: Corporate Policy ---
 * Santa's previous password expired, and he needs help choosing a new one.
 * <p>
 * To help him remember his new password after the old one expires, Santa has devised
 * a method of coming up with a password based on the previous one. Corporate policy
 * dictates that passwords must be exactly eight lowercase letters (for security reasons),
 * so he finds his new password by incrementing his old password string repeatedly until it is valid.
 * <p>
 * Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on.
 * Increase the rightmost letter one step; if it was z, it wraps around to a, and
 * repeat with the next letter to the left until one doesn't wrap around.
 * <p>
 * Unfortunately for Santa, a new Security-Elf recently started, and he
 * has imposed some additional password requirements:
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
 * <p>
 * For example:
 * <p>
 * hijklmmn meets the first requirement (because it contains the straight hij)
 * but fails the second requirement (because it contains i and l).
 * abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
 * abbcegjk fails the third requirement, because it only has one double letter (bb).
 * The next password after abcdefgh is abcdffaa.
 * The next password after ghijklmn is ghjaabcc, because you eventually skip
 * all the passwords that start with ghi..., since i is not allowed.
 * <p>
 * Given Santa's current password (your puzzle input), what should his next password be?
 * <p>
 * Your puzzle answer was cqjxxyzz.
 * <p>
 * --- Part Two ---
 * Santa's password expired again. What's the next one?
 * <p>
 * Your puzzle answer was cqkaabcc.
 * <p>
 */
public class Day11 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_11-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        for (var line : input) {
            var start = Instant.now();
            var next = suggestNextPassword(line);
            var end = Instant.now();
            System.out.printf("The next valid password is: %s%n", next);
            printTiming(start, end);
        }
    }

    @Override
    public void runPart2(List<String> input) {
        for (var line : input) {
            var start = Instant.now();
            var next = suggestNextPassword(suggestNextPassword(line));
            var end = Instant.now();
            System.out.printf("The next valid password is: %s%n", next);
            printTiming(start, end);
        }
    }

    public boolean isValidPassword(String input) {
        return PasswordPolicy.isValidPassword(input);
    }

    @NotNull
    public String suggestNextPassword(@NotNull String input) {
        return PasswordPolicy.suggestNextPassword(input);
    }
}
