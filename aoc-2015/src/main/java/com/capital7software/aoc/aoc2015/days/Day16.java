package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.AuntSueIdentifier;
import com.capital7software.aoc.lib.analysis.AuntSueIdentifier.AuntSue;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 16: Aunt Sue ---<br><br>
 * Your Aunt Sue has given you a wonderful gift, and you'd like to send her a thank-you card.
 * However, there's a small problem: she signed it "From, Aunt Sue".
 *
 * <p><br>
 * You have 500 Aunts named "Sue".
 *
 * <p><br>
 * So, to avoid sending the card to the wrong person, you need to figure out which Aunt Sue
 * (which you conveniently number 1 to 500, for sanity) gave you the gift. You open the present
 * and, as luck would have it, good ol' Aunt Sue got you a My First Crime Scene Analysis Machine!
 * Just what you wanted. Or needed, as the case may be.
 *
 * <p><br>
 * The My First Crime Scene Analysis Machine (MFCSAM for short) can detect a few specific
 * compounds in a given sample, as well as how many distinct kinds of those compounds there are.
 * According to the instructions, these are what the MFCSAM can detect:
 * <ul>
 *     <li>
 *         children, by human DNA age analysis.
 *     </li>
 *     <li>
 *         cats. It doesn't differentiate individual breeds.
 *     </li>
 *     <li>
 *         Several seemingly random breeds of dog: samoyeds, pomeranians, akitas, and vizslas.
 *     </li>
 *     <li>
 *         goldfish. No other kinds of fish.
 *     </li>
 *     <li>
 *         trees, all in one group.
 *     </li>
 *     <li>
 *         cars, presumably by exhaust or gasoline or something.
 *     </li>
 *     <li>
 *         perfumes, which is handy, since many of your Aunts Sue wear a few kinds.
 *     </li>
 * </ul>
 * In fact, many of your Aunts Sue have many of these. You put the wrapping from the gift into
 * the MFCSAM. It beeps inquisitively at you a few times and then prints out a message on
 * ticker tape:
 *
 * <p><br>
 * <code>
 * children: 3<br>
 * cats: 7<br>
 * samoyeds: 2<br>
 * pomeranians: 3<br>
 * akitas: 0<br>
 * vizslas: 0<br>
 * goldfish: 5<br>
 * trees: 3<br>
 * cars: 2<br>
 * perfumes: 1<br>
 * </code>
 *
 * <p><br>
 * You make a list of the things you can remember about each Aunt Sue.
 * Things missing from your list aren't zero - you simply don't remember the value.
 *
 * <p><br>
 * What is the number of the Sue that got you the gift?
 *
 * <p><br>
 * Your puzzle answer was 40.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * As you're about to send the thank you note, something in the MFCSAM's instructions catches
 * your eye. Apparently, it has an outdated retroencabulator, and so the output from the machine
 * isn't exact values - some of them indicate ranges.
 *
 * <p><br>
 * In particular, the cats and trees readings indicates that there are greater than that many
 * (due to the unpredictable nuclear decay of cat dander and tree pollen), while the pomeranians
 * and goldfish readings indicate that there are fewer than that many (due to the modial
 * interaction of magnetoreluctance).
 *
 * <p><br>
 * What is the number of the real Aunt Sue?
 *
 * <p><br>
 * Your puzzle answer was 241.
 */
public class Day16 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day16.class);

  /**
   * Instantiates the solution instance.
   */
  public Day16() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_16-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var max = identifyAuntSue(input);
    var end = Instant.now();
    LOGGER.info("The number of the Sue that got me the gift is: {}", max);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var max = identifyRealAuntSue(input);
    var end = Instant.now();
    LOGGER.info("The number of the real Sue that got me the gift is: {}", max);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the ID of the AuntSue based on the all the available suspects.
   *
   * @param input The suspect AuntSues.
   * @return The ID of the AuntSue based on the all the available suspects.
   */
  public long identifyAuntSue(List<String> input) {
    var identifier = AuntSueIdentifier.parse(input);
    var target = new AuntSue(
        0,
        3,
        7,
        2,
        3,
        0,
        0,
        5,
        3,
        2,
        1
    );

    var suspects = identifier.identifySoftSuspects(target);
    AtomicInteger answer = new AtomicInteger(0);

    suspects.stream().findFirst().ifPresent(auntSue -> answer.set(auntSue.id()));

    return answer.get();
  }

  /**
   * Returns the ID of the AuntSue based on the all the available suspects.
   *
   * @param input The suspect AuntSues.
   * @return The ID of the AuntSue based on the all the available suspects.
   */
  public long identifyRealAuntSue(List<String> input) {
    var identifier = AuntSueIdentifier.parse(input);
    var target = new AuntSue(
        0,
        3,
        7,
        2,
        3,
        0,
        0,
        5,
        3,
        2,
        1
    );

    var suspects = identifier.identifyHardSuspects(target);
    AtomicInteger answer = new AtomicInteger(0);

    suspects.stream().findFirst().ifPresent(auntSue -> answer.set(auntSue.id()));

    return answer.get();
  }
}
