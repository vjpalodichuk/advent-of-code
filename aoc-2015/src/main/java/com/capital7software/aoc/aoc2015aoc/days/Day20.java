package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.math.InfiniteHouses;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 20: Infinite Elves and Infinite Houses ---<br><br>
 * To keep the Elves busy, Santa has them deliver some presents by hand, door-to-door.<br>
 * He sends them down a street with infinite houses numbered sequentially: 1, 2, 3, 4, 5,
 * and so on.
 *
 * <p><br>
 * Each Elf is assigned a number, too, and delivers presents to houses based on that number:
 * <ul>
 *     <li>
 *         The first Elf (number 1) delivers presents to every house: 1, 2, 3, 4, 5, ....
 *     </li>
 *     <li>
 *         The second Elf (number 2) delivers presents to every second house: 2, 4, 6, 8, 10, ....
 *     </li>
 *     <li>
 *         Elf number 3 delivers presents to every third house: 3, 6, 9, 12, 15, ....
 *     </li>
 * </ul>
 * There are infinitely many Elves, numbered starting with 1.<br>
 * Each Elf delivers presents equal to ten times his or her number at each house.
 *
 * <p><br>
 * So, the first nine houses on the street end up like this:<br>
 *
 * <p><br>
 * <code>
 * House 1 got 10 presents.<br>
 * House 2 got 30 presents.<br>
 * House 3 got 40 presents.<br>
 * House 4 got 70 presents.<br>
 * House 5 got 60 presents.<br>
 * House 6 got 120 presents.<br>
 * House 7 got 80 presents.<br>
 * House 8 got 150 presents.<br>
 * House 9 got 130 presents.<br>
 * </code>
 * <ul>
 *     <li>
 *         The first house gets 10 presents: it is visited only by Elf 1, which delivers
 *         1 * 10 = 10 presents.
 *     </li>
 *     <li>
 *         The fourth house gets 70 presents, because it is visited by Elves 1, 2, and 4,
 *         for a total of 10 + 20 + 40 = 70 presents.
 *     </li>
 * </ul>
 * What is the lowest house number of the house to get at least as many presents as the
 * number in your puzzle input?
 *
 * <p><br>
 * Your puzzle answer was 776160.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * The Elves decide they don't want to visit an infinite number of houses. Instead,
 * each Elf will stop after delivering presents to 50 houses. To make up for it,
 * they decide to deliver presents equal to eleven times their number at each house.
 *
 * <p><br>
 * With these changes, what is the new lowest house number of the house to get
 * at least as many presents as the number in your puzzle input?
 *
 * <p><br>
 * Your puzzle answer was 786240.
 */
public class Day20 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day20.class);

  /**
   * Instantiates the solution instance.
   */
  public Day20() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_20-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    int deliveredPresents = Integer.parseInt(input.getFirst());
    var lowest = lowestHouseNumber(deliveredPresents);
    var end = Instant.now();
    LOGGER.info("The lowest house number to be delivered {} presents is: {}",
                deliveredPresents, lowest);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    int deliveredPresents = Integer.parseInt(input.getFirst());
    var lowest = lowestHouseNumberNewRules(deliveredPresents);
    var end = Instant.now();
    LOGGER.info("Using the new rules, the lowest house number to be delivered {} presents is: {}",
                deliveredPresents, lowest);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the lowest house number based on the number of delivered presents.
   *
   * @param deliveredPresents The number of delivered presents.
   * @return The lowest house number based on the number of delivered presents.
   */
  public int lowestHouseNumber(int deliveredPresents) {
    var infiniteHouses = new InfiniteHouses(deliveredPresents);

    return infiniteHouses.lowestHouseNumber();
  }

  /**
   * Returns the lowest house number based on the number of delivered presents
   * using the new rules.
   *
   * @param deliveredPresents The number of delivered presents.
   * @return The lowest house number based on the number of delivered presents
   *     using the new rules.
   */
  public int lowestHouseNumberNewRules(int deliveredPresents) {
    var infiniteHouses = new InfiniteHouses(deliveredPresents);

    return infiniteHouses.lowestHouseNumberNewRules();
  }
}
