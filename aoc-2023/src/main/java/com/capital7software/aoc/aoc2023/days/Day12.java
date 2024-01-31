package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.HotSprings;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 12: Hot Springs ---<br><br>
 * You finally reach the hot springs! You can see steam rising from secluded areas attached
 * to the primary, ornate building.
 *
 * <p><br>
 * As you turn to enter, the researcher stops you. "Wait - I thought you were looking for
 * the hot springs, weren't you?" You indicate that this definitely looks like hot springs to you.
 *
 * <p><br>
 * "Oh, sorry, common mistake! This is actually the onsen! The hot springs are next door."
 *
 * <p><br>
 * You look in the direction the researcher is pointing and suddenly notice the massive
 * metal helixes towering overhead. "This way!"
 *
 * <p><br>
 * It only takes you a few more steps to reach the main gate of the massive fenced-off
 * area containing the springs. You go through the gate and into a small administrative building.
 *
 * <p><br>
 * "Hello! What brings you to the hot springs today? Sorry they're not very hot right now;
 * we're having a lava shortage at the moment." You ask about the missing machine parts for
 * Desert Island.
 *
 * <p><br>
 * "Oh, all of Gear Island is currently offline! Nothing is being manufactured at the moment,
 * not until we get more lava to heat our forges. And our springs. The springs aren't very
 * springy unless they're hot!"
 *
 * <p><br>
 * "Say, could you go up and see why the lava stopped flowing? The springs are too cold
 * for normal operation, but we should be able to find one springy enough to launch you up there!"
 *
 * <p><br>
 * There's just one problem - many of the springs have fallen into disrepair, so they're
 * not actually sure which springs would even be safe to use! Worse yet, their condition
 * records of which springs are damaged (your puzzle input) are also damaged! You'll need
 * to help them repair the damaged records.
 *
 * <p><br>
 * In the giant field just outside, the springs are arranged into rows. For each row,
 * the condition records show every spring and whether it is operational (.) or damaged (#).
 * This is the part of the condition records that is itself damaged; for some springs,
 * it is simply unknown (?) whether the spring is operational or damaged.
 *
 * <p><br>
 * However, the engineer that produced the condition records also duplicated some of this
 * information in a different format! After the list of springs for a given row, the size
 * of each contiguous group of damaged springs is listed in the order those groups appear
 * in the row. This list always accounts for every damaged spring, and each number is the
 * entire size of its contiguous group (that is, groups are always separated by at least
 * one operational spring: #### would always be 4, never 2,2).
 *
 * <p><br>
 * So, condition records with no unknown spring conditions might look like this:
 *
 * <p><br>
 * <code>
 * #.#.### 1,1,3<br>
 * .#...#....###. 1,1,3<br>
 * .#.###.#.###### 1,3,1,6<br>
 * ####.#...#... 4,1,1<br>
 * #....######..#####. 1,6,5<br>
 * .###.##....# 3,2,1<br>
 * </code>
 *
 * <p><br>
 * However, the condition records are partially damaged; some of the springs' conditions are
 * actually unknown (?). For example:
 *
 * <p><br>
 * <code>
 * ???.### 1,1,3<br>
 * .??..??...?##. 1,1,3<br>
 * ?#?#?#?#?#?#?#? 1,3,1,6<br>
 * ????.#...#... 4,1,1<br>
 * ????.######..#####. 1,6,5<br>
 * ?###???????? 3,2,1<br>
 * </code>
 *
 * <p><br>
 * Equipped with this information, it is your job to figure out how many different arrangements
 * of operational and broken springs fit the given criteria in each row.
 *
 * <p><br>
 * In the first line (???.### 1,1,3), there is exactly one way separate groups of one, one,
 * and three broken springs (in that order) can appear in that row: the first three unknown
 * springs must be broken, then operational, then broken (#.#), making the whole row #.#.###.
 *
 * <p><br>
 * The second line is more interesting: .??..??...?##. 1,1,3 could be a total of four different
 * arrangements. The last ? must always be broken (to satisfy the final contiguous group of
 * three broken springs), and each ?? must hide exactly one of the two broken springs.
 * (Neither ?? could be both broken springs or they would form a single contiguous group of two;
 * if that were true, the numbers afterward would have been 2,3 instead.) Since each ?? can
 * either be #. or .#, there are four possible arrangements of springs.
 *
 * <p><br>
 * The last line is actually consistent with ten different arrangements! Because the first
 * number is 3, the first and second ? must both be . (if either were #, the first number
 * would have to be 4 or higher). However, the remaining run of unknown spring conditions
 * have many different ways they could hold groups of two and one broken springs:
 *
 * <p><br>
 * <code>
 * ?###???????? 3,2,1<br>
 * .###.##.#...<br>
 * .###.##..#..<br>
 * .###.##...#.<br>
 * .###.##....#<br>
 * .###..##.#..<br>
 * .###..##..#.<br>
 * .###..##...#<br>
 * .###...##.#.<br>
 * .###...##..#<br>
 * .###....##.#<br>
 * </code>
 *
 * <p><br>
 * In this example, the number of possible arrangements for each row is:
 *
 * <p><br>
 * <code>
 * ???.### 1,1,3 - 1 arrangement<br>
 * .??..??...?##. 1,1,3 - 4 arrangements<br>
 * ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement<br>
 * ????.#...#... 4,1,1 - 1 arrangement<br>
 * ????.######..#####. 1,6,5 - 4 arrangements<br>
 * ?###???????? 3,2,1 - 10 arrangements<br>
 * </code>
 *
 * <p><br>
 * Adding all the possible arrangement counts together produces a total of 21 arrangements.
 *
 * <p><br>
 * For each row, count all the different arrangements of operational and broken springs
 * that meet the given criteria. What is the sum of those counts?
 *
 * <p><br>
 * Your puzzle answer was 7110.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * As you look out at the field of springs, you feel like there are way more springs than
 * the condition records list. When you examine the records, you discover that they were
 * actually folded up this whole time!
 *
 * <p><br>
 * To unfold the records, on each row, replace the list of spring conditions with five
 * copies of itself (separated by ?) and replace the list of contiguous groups of damaged
 * springs with five copies of itself (separated by ,).
 *
 * <p><br>
 * So, this row:
 *
 * <p><br>
 * <code>
 * .# 1<br>
 * </code>
 *
 * <p><br>
 * Would become:
 *
 * <p><br>
 * <code>
 * .#?.#?.#?.#?.# 1,1,1,1,1<br>
 * </code>
 *
 * <p><br>
 * The first line of the above example would become:
 *
 * <p><br>
 * <code>
 * ???.###????.###????.###????.###????.### 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3<br>
 * </code>
 *
 * <p><br>
 * In the above example, after unfolding, the number of possible arrangements for some rows
 * is now much larger:
 *
 * <p><br>
 * <code>
 * ???.### 1,1,3 - 1 arrangement<br>
 * .??..??...?##. 1,1,3 - 16384 arrangements<br>
 * ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement<br>
 * ????.#...#... 4,1,1 - 16 arrangements<br>
 * ????.######..#####. 1,6,5 - 2500 arrangements<br>
 * ?###???????? 3,2,1 - 506250 arrangements<br>
 * </code>
 *
 * <p><br>
 * After unfolding, adding all the possible arrangement counts together produces 525152.
 *
 * <p><br>
 * Unfold your condition records; what is the new sum of possible arrangement counts?
 *
 * <p><br>
 * Your puzzle answer was 1566786613613.
 */
public class Day12 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day12.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day12() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_12-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = sumOfAllPossibleSpringArrangements(input, false);
    var end = Instant.now();

    LOGGER.info("Sum of all possible spring arrangements is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var answer = sumOfAllPossibleSpringArrangements(input, true);
    var end = Instant.now();

    LOGGER.info("Sum of all possible unfolded spring arrangements is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  /**
   * Calculates and returns the sum of all possible spring arrangements. If unfold is true,
   * then the spring record is replaced with five copies of itself.
   *
   * @param input  The List of Strings to parse the spring records from.
   * @param unfold If true, then each spring record is replaced with five copies of itself.
   * @return The sum of all possible spring arrangements.
   */
  public long sumOfAllPossibleSpringArrangements(List<String> input, boolean unfold) {
    var hotSprings = HotSprings.buildHotSprings(input, unfold);
    return hotSprings.sumOfAllPossibleArrangements();
  }
}
