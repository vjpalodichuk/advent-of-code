package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 5: Doesn't He Have Intern-Elves For This? ---
 * Santa needs help figuring out which strings in his text file are naughty or nice.
 * <p>
 * A nice string is one with all of the following properties:
 * <p>
 * It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
 * It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
 * It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
 * For example:
 * <p>
 * ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double letter (...dd...),
 * and none of the disallowed substrings.
 * aaa is nice because it has at least three vowels and a double letter, even though the letters used
 * by different rules overlap.
 * jchzalrnumimnmhp is naughty because it has no double letter.
 * haegwjzuvuyypxyu is naughty because it contains the string xy.
 * dvszwmarrgswjxmb is naughty because it contains only one vowel.
 * How many strings are nice?
 * <p>
 *     Your puzzle answer was 255.
 * --- Part Two ---
 * Realizing the error of his ways, Santa has switched to a better model of determining whether a string
 * is naughty or nice. None of the old rules apply, as they are all clearly ridiculous.
 * <p>
 * Now, a nice string is one with all of the following properties:
 * <p>
 * It contains a pair of any two letters that appears at least twice in the string without overlapping,
 * like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
 * It contains at least one letter which repeats with exactly one letter between them, like xyx,
 * abcdefeghi (efe), or even aaa.
 * For example:
 * <p>
 * qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter that repeats
 * with exactly one letter between them (zxz).
 * xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between,
 * even though the letters used by each rule overlap.
 * uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
 * ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo),
 * but no pair that appears twice.
 * How many strings are nice under these new rules?
 */
public class Day05 implements AdventOfCodeSolution {
    public record NaughtyOrNice(String letters) {
        public boolean isNice() {
            var vowelCount = 0;
            var doubleLetterCount = 0;
            var restrictedCount = 0;
            var lastChar = letters.charAt(0);

            if (isVowel(lastChar)) {
                vowelCount++;
            }

            for (int i = 1; i < letters.length(); i++) {
                var thisChar = letters.charAt(i);

                if (isVowel(thisChar)) {
                    vowelCount++;
                }

                if (isDoubleLetter(lastChar, thisChar)) {
                    doubleLetterCount++;
                }

                if (isRestricted(lastChar, thisChar)) {
                    restrictedCount++;
                }

                if (restrictedCount != 0) {
                    break; // Can't be nice if it contains a restricted character sequence!!
                }
                lastChar = thisChar;
            }

            return restrictedCount == 0 && vowelCount >= 3 && doubleLetterCount >= 1;
        }

        public boolean isNewNice() {
            var twoLetterCount = 0;
            var repeatLetterCount = 0;
            var lastChar = letters.charAt(0);

            for (int i = 1; i < letters.length() - 1; i++) {
                var thisChar = letters.charAt(i);
                var thisString = letters.substring(i - 1, i + 1);

                if (twoLetterCount == 0) {
                    var twoLetterIndex = letters.lastIndexOf(thisString);

                    if (twoLetterIndex > i) {
                        twoLetterCount++;
                    }
                }

                if (repeatLetterCount == 0) {
                    var nextChar = letters.charAt(i + 1);

                    if (lastChar == nextChar) {
                        repeatLetterCount++;
                    }
                }

                if (repeatLetterCount > 0 && twoLetterCount > 0) {
                    break;
                }

                lastChar = thisChar;
            }

            return repeatLetterCount > 0 && twoLetterCount > 0;
        }

        public boolean isRestricted(char c, char o) {
            return (c == 'a' && o == 'b') || (c == 'c' && o == 'd') || (c == 'p' && o == 'q') || (c == 'x' && o == 'y');
        }

        public boolean isDoubleLetter(char c, char o) {
            return c == o;
        }

        public boolean isVowel(char c) {
            return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
        }
    }

    private static String defaultFilename = "inputs/input_day_05-01.txt";

    public static void setDefaultFilename(String defaultFilename) {
        Day05.defaultFilename = defaultFilename;
    }

    @Override
    public String getDefaultInputFilename() {
        return defaultFilename;
    }

    @Override
    public void runPart1(List<String> input) {

        var start = Instant.now();
        var total = 0;
        for (var line : input) {
            if (new NaughtyOrNice(line).isNice()) {
                total++;
            }
        }
        var end = Instant.now();
        System.out.printf(
                "There are %d nice strings!%n", total);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {

        var start = Instant.now();
        var total = 0;
        for (var line : input) {
            if (new NaughtyOrNice(line).isNewNice()) {
                total++;
            }
        }
        var end = Instant.now();
        System.out.printf(
                "There are %d new nice strings!%n", total);
        printTiming(start, end);
    }

    public boolean isNice(String line) {
        return new NaughtyOrNice(line).isNice();
    }

    public boolean isNewNice(String line) {
        return new NaughtyOrNice(line).isNewNice();
    }

}
