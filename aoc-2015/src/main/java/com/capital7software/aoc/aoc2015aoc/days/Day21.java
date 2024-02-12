package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.game.SwordsAndStuff;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 21: RPG Simulator 20XX ---<br><br>
 * Little Henry Case got a new video game for Christmas. It's an RPG, and he's stuck on a boss.
 * He needs to know what equipment to buy at the shop. He hands you the controller.
 *
 * <p><br>
 * In this game, the mage (you) and the enemy (the boss) take turns attacking.
 * The mage always goes first. Each attack reduces the opponent's hit points by at least 1.
 * The first character at or below 0 hit points loses.
 *
 * <p><br>
 * Damage dealt by an attacker each turn is equal to the attacker's damage score minus the
 * defender's armor score. An attacker always does at least 1 damage. So, if the attacker
 * has a damage score of 8, and the defender has an armor score of 3, the defender loses
 * 5 hit points. If the defender had an armor score of 300, the defender would still lose
 * 1 hit point.
 *
 * <p><br>
 * Your damage score and armor score both start at zero. They can be increased by buying
 * items in exchange for gold. You start with no items and have as much gold as you need.
 * Your total damage or armor is equal to the sum of those stats from all of your items.
 * You have 100 hit points.
 *
 * <p><br>
 * Here is what the item shop is selling:
 *
 * <p><br>
 * <code>
 * Weapons: &nbsp;&nbsp;    Cost  Damage  Armor<br>
 * Dagger &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 8&nbsp;&nbsp;&nbsp; 4 &nbsp;&nbsp;&nbsp; 0<br>
 * Shortsword &nbsp;&nbsp;10 &nbsp;&nbsp;&nbsp;5 &nbsp;&nbsp;&nbsp; 0<br>
 * Warhammer &nbsp;&nbsp; 25 &nbsp;&nbsp;&nbsp;6 &nbsp;&nbsp;&nbsp; 0<br>
 * Longsword &nbsp;&nbsp; 40 &nbsp;&nbsp;&nbsp;7 &nbsp;&nbsp;&nbsp; 0<br>
 * Greataxe &nbsp;&nbsp;&nbsp; 74 &nbsp;&nbsp; 8 &nbsp;&nbsp;&nbsp; 0<br>
 * <br><br>
 * Armor: &nbsp;&nbsp;   Cost  Damage  Armor<br>
 * Leather &nbsp;&nbsp;&nbsp; 13 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 1<br>
 * Chainmail &nbsp;&nbsp;31 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 2<br>
 * Splintmail &nbsp;53 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 3<br>
 * Bandedmail &nbsp;75 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 4<br>
 * Platemail &nbsp;102 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 5<br>
 * <br><br>
 * Rings: &nbsp;&nbsp;&nbsp;&nbsp; Cost  Damage  Armor<br>
 * Damage +1 &nbsp;&nbsp; 25 &nbsp;&nbsp; 1 &nbsp;&nbsp;&nbsp; 0<br>
 * Damage +2 &nbsp;&nbsp; 50 &nbsp;&nbsp; 2 &nbsp;&nbsp;&nbsp; 0<br>
 * Damage +3 &nbsp;&nbsp;100 &nbsp;&nbsp; 3 &nbsp;&nbsp;&nbsp; 0<br>
 * Defense +1 &nbsp;&nbsp;20 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 1<br>
 * Defense +2 &nbsp;&nbsp;40 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 2<br>
 * Defense +3 &nbsp;&nbsp;80 &nbsp;&nbsp; 0 &nbsp;&nbsp;&nbsp; 3<br>
 * </code>
 *
 * <p><br>
 * You must buy exactly one weapon; no dual-wielding. Armor is optional, but you can't
 * use more than one. You can buy 0-2 rings (at most one for each hand). You must use
 * any items you buy. The shop only has one of each item, so you can't buy, for example,
 * two rings of Damage +3.
 *
 * <p><br>
 * For example, suppose you have 8 hit points, 5 damage, and 5 armor, and that the boss
 * has 12 hit points, 7 damage, and 2 armor:
 *
 * <p><br>
 * <ul>
 *     <li>
 *         The mage deals 5-2 = 3 damage; the boss goes down to 9 hit points.
 *     </li>
 *     <li>
 *         The boss deals 7-5 = 2 damage; the mage goes down to 6 hit points.
 *     </li>
 *     <li>
 *         The mage deals 5-2 = 3 damage; the boss goes down to 6 hit points.
 *     </li>
 *     <li>
 *         The boss deals 7-5 = 2 damage; the mage goes down to 4 hit points.
 *     </li>
 *     <li>
 *         The mage deals 5-2 = 3 damage; the boss goes down to 3 hit points.
 *     </li>
 *     <li>
 *         The boss deals 7-5 = 2 damage; the mage goes down to 2 hit points.
 *     </li>
 *     <li>
 *         The mage deals 5-2 = 3 damage; the boss goes down to 0 hit points.
 *     </li>
 * </ul>
 * In this scenario, the mage wins! (Barely.)
 *
 * <p><br>
 * You have 100 hit points. The boss's actual stats are in your puzzle input. What is the
 * least amount of gold you can spend and still win the fight?
 *
 * <p><br>
 * Your puzzle answer was 91.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Turns out the shopkeeper is working with the boss, and can persuade you to buy whatever
 * items he wants. The other rules still apply, and he still only has one of each item.
 *
 * <p><br>
 * What is the most amount of gold you can spend and still lose the fight?
 *
 * <p><br>
 * Your puzzle answer was 158.
 */
public class Day21 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day21.class);

  /**
   * Instantiates the solution instance.
   */
  public Day21() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_21-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var lowest = leastAmountOfGoldAndStillWin(input);
    var end = Instant.now();
    LOGGER.info("The least amount of gold spent and still win is: {}", lowest);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var lowest = mostAmountOfGoldAndStillLose(input);
    var end = Instant.now();
    LOGGER.info("The most amount of gold spent and still lose is: {}", lowest);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the least amount of Gold the Player can spend and still win the game.
   *
   * @param lines The available items and the player and boss stats.
   * @return The least amount of Gold the Player can spend and still win the game.
   */
  public int leastAmountOfGoldAndStillWin(List<String> lines) {
    var simulator = SwordsAndStuff.buildSimulator(lines);
    var least = simulator.calculateLeastGoldSpentToWinTheGame();
    return least.first();
  }

  /**
   * Returns the most amount of Gold the Player can spend and still lose the game.
   *
   * @param lines The available items and the player and boss stats.
   * @return The most amount of Gold the Player can spend and still lose the game.
   */
  public int mostAmountOfGoldAndStillLose(List<String> lines) {
    var simulator = SwordsAndStuff.buildSimulator(lines);
    var most = simulator.calculateMostAmountOfGoldAndStillLose();
    return most.first();
  }
}
