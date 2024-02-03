package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.string.NaughtyOrNice;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 5: Doesn't He Have Intern-Elves For This? ---<br><br>
 * Santa needs help figuring out which strings in his text file are naughty or nice.
 *
 * <p><br>
 * A nice string is one with all the following properties:
 * <ul>
 *     <li>
 *         It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
 *     </li>
 *     <li>
 *         It contains at least one letter that appears twice in a row, like xx, abcdde (dd),
 *         or aabbccdd (aa, bb, cc, or dd).
 *     </li>
 *     <li>
 *         It does not contain the strings ab, cd, pq, or xy, even if they are part of one of
 *         the other requirements.
 *     </li>
 * </ul>
 * For example:
 * <ul>
 *     <li>
 *         ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double
 *         letter (...dd...), and none of the disallowed substrings.
 *     </li>
 *     <li>
 *         aaa is nice because it has at least three vowels and a double letter, even though the
 *         letters used by different rules overlap.
 *     </li>
 *     <li>
 *         jchzalrnumimnmhp is naughty because it has no double letter.
 *     </li>
 *     <li>
 *         haegwjzuvuyypxyu is naughty because it contains the string xy.
 *     </li>
 *     <li>
 *         dvszwmarrgswjxmb is naughty because it contains only one vowel.
 *     </li>
 * </ul>
 * How many strings are nice?
 *
 * <p><br>
 * Your puzzle answer was 255.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Realizing the error of his ways, Santa has switched to a better model of determining whether
 * a string is naughty or nice. None of the old rules apply, as they are all clearly ridiculous.
 *
 * <p><br>
 * Now, a nice string is one with all the following properties:
 * <ul>
 *     <li>
 *         It contains a pair of any two letters that appears at least twice in the string without
 *         overlapping, like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
 *     </li>
 *     <li>
 *         It contains at least one letter which repeats with exactly one letter between them,
 *         like xyx, abcdefeghi (efe), or even aaa.
 *     </li>
 * </ul>
 * For example:
 * <ul>
 *     <li>
 *         qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter
 *         that repeats with exactly one letter between them (zxz).
 *     </li>
 *     <li>
 *         xxyxx is nice because it has a pair that appears twice and a letter that repeats with
 *         one between, even though the letters used by each rule overlap.
 *     </li>
 *     <li>
 *         uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single
 *         letter between them.
 *     </li>
 *     <li>
 *         ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo),
 *         but no pair that appears twice.
 *     </li>
 * </ul>
 * How many strings are nice under these new rules?
 *
 * <p><br>
 * Your puzzle answer was 55.
 */
public class Day05 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day05.class);

  /**
   * Instantiates the solution instance.
   */
  public Day05() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_05-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {

    var start = Instant.now();
    var total = 0;
    for (var line : input) {
      if (isNice(line)) {
        total++;
      }
    }
    var end = Instant.now();
    LOGGER.info("There are {} nice strings!", total);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {

    var start = Instant.now();
    var total = 0;
    for (var line : input) {
      if (isNewNice(line)) {
        total++;
      }
    }
    var end = Instant.now();
    LOGGER.info("There are {} new nice strings!", total);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns true if the specified String is a Nice String.
   *
   * @param line The String to determine if it is Nice or not.
   * @return True if the specified String is a Nice String.
   */
  public boolean isNice(String line) {
    return NaughtyOrNice.isNice(line);
  }

  /**
   * Returns true if the specified String is a Nice String using the new rules.
   *
   * @param line The String to determine if it is Nice or not.
   * @return True if the specified String is a Nice String using the new rules.
   */
  public boolean isNewNice(String line) {
    return NaughtyOrNice.isNewNice(line);
  }

}
