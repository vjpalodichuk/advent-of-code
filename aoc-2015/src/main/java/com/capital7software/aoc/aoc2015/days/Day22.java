package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.graph.path.AStarAndWizards;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * --- Day 22: Wizard Simulator 20XX ---<br>
 * Little Henry Case decides that defeating bosses with swords and stuff is boring.
 * Now he's playing the game with a wizard. Of course, he gets stuck on another boss and needs your help again.
 * <p><br>
 * In this version, combat still proceeds with the mage and the boss taking alternating turns.
 * The mage still goes first. Now, however, you don't get any equipment; instead,
 * you must choose one of your spells to cast. The first character at or below 0 hit points loses.
 * <p><br>
 * Since you're a wizard, you don't get to wear armor, and you can't attack normally. However,
 * since you do magic damage, your opponent's armor is ignored, and so the boss effectively
 * has zero armor as well. As before, if armor (from a spell, in this case) would reduce damage below 1,
 * it becomes 1 instead - that is, the boss' attacks always deal at least 1 damage.
 * <p><br>
 * On each of your turns, you must select one of your spells to cast. If you cannot afford to
 * cast any spell, you lose. Spells cost mana; you start with 500 mana, but have no maximum limit.
 * You must have enough mana to cast a spell, and its cost is immediately deducted when you cast it.<br>
 * <p><br>
 * Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.
 * <p><br>
 * Magic Missile costs 53 mana. It instantly does 4 damage.<br>
 * Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.<br><br>
 * Shield costs 113 mana. It starts an effect that lasts for 6 turns.
 * While it is active, your armor is increased by 7.<br><br>
 * Poison costs 173 mana. It starts an effect that lasts for 6 turns.
 * At the start of each turn while it is active, it deals the boss 3 damage.<br><br>
 * Recharge costs 229 mana. It starts an effect that lasts for 5 turns.
 * At the start of each turn while it is active, it gives you 101 new mana.<br><br>
 * <p><br>
 * Effects all work the same way. Effects apply at the start of both the mage's turns and the boss' turns.
 * Effects are created with a timer (the number of turns they last); at the start of each turn,
 * after they apply any effect they have, their timer is decreased by one.
 * If this decreases the timer to zero, the effect ends. You cannot cast a spell that would start an
 * effect which is already active. However, effects can be started on the same turn they end.<br><br>
 * <p><br>
 * For capital7software, suppose the mage has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 10 hit points, 0 armor, 250 mana<br>
 * - Boss has 13 hit points<br>
 * Mage casts Poison.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 10 hit points, 0 armor, 77 mana<br>
 * - Boss has 13 hit points<br>
 * Poison deals 3 damage; its timer is now 5.<br>
 * Boss attacks for 8 damage.<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 2 hit points, 0 armor, 77 mana<br>
 * - Boss has 10 hit points<br>
 * Poison deals 3 damage; its timer is now 4.<br>
 * Mage casts Magic Missile, dealing 4 damage.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 2 hit points, 0 armor, 24 mana<br>
 * - Boss has 3 hit points<br>
 * Poison deals 3 damage. This kills the boss, and the mage wins.<br>
 * <p><br>
 * Now, suppose the same initial conditions, except that the boss has 14 hit points instead:
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 10 hit points, 0 armor, 250 mana<br>
 * - Boss has 14 hit points<br>
 * Mage casts Recharge.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 10 hit points, 0 armor, 21 mana<br>
 * - Boss has 14 hit points<br>
 * Recharge provides 101 mana; its timer is now 4.<br>
 * Boss attacks for 8 damage!<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 2 hit points, 0 armor, 122 mana<br>
 * - Boss has 14 hit points<br>
 * Recharge provides 101 mana; its timer is now 3.<br>
 * Mage casts Shield, increasing armor by 7.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 2 hit points, 7 armor, 110 mana<br>
 * - Boss has 14 hit points<br>
 * Shield's timer is now 5.<br>
 * Recharge provides 101 mana; its timer is now 2.<br>
 * Boss attacks for 8 - 7 = 1 damage!<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 1 hit point, 7 armor, 211 mana<br>
 * - Boss has 14 hit points<br>
 * Shield's timer is now 4.<br>
 * Recharge provides 101 mana; its timer is now 1.<br>
 * Mage casts Drain, dealing 2 damage, and healing 2 hit points.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 3 hit points, 7 armor, 239 mana<br>
 * - Boss has 12 hit points<br>
 * Shield's timer is now 3.<br>
 * Recharge provides 101 mana; its timer is now 0.<br>
 * Recharge wears off.<br>
 * Boss attacks for 8 - 7 = 1 damage!<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 2 hit points, 7 armor, 340 mana<br>
 * - Boss has 12 hit points<br>
 * Shield's timer is now 2.<br>
 * Mage casts Poison.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 2 hit points, 7 armor, 167 mana<br>
 * - Boss has 12 hit points<br>
 * Shield's timer is now 1.<br>
 * Poison deals 3 damage; its timer is now 5.<br>
 * Boss attacks for 8 - 7 = 1 damage!<br>
 * <p><br>
 * -- Mage turn --<br>
 * - Mage has 1 hit point, 7 armor, 167 mana<br>
 * - Boss has 9 hit points<br>
 * Shield's timer is now 0.<br>
 * Shield wears off, decreasing armor by 7.<br>
 * Poison deals 3 damage; its timer is now 4.<br>
 * Mage casts Magic Missile, dealing 4 damage.<br>
 * <p><br>
 * -- Boss turn --<br>
 * - Mage has 1 hit point, 0 armor, 114 mana<br>
 * - Boss has 2 hit points<br>
 * Poison deals 3 damage. This kills the boss, and the mage wins.<br>
 * <p><br>
 * You start with 50 hit points and 500 mana points. The boss's actual stats are in your puzzle input.
 * What is the least amount of mana you can spend and still win the fight?
 * (Do not include mana recharge effects as "spending" negative mana.)
 * <p>
 * Your puzzle answer was 953.
 * <p>
 * --- Part Two ---<br>
 * On the next run through the game, you increase the difficulty to hard.
 * <p><br>
 * At the start of each player turn (before any other effects apply),
 * you lose 1 hit point. If this brings you to or below 0 hit points, you lose.
 * <p><br>
 * With the same starting stats for you and the boss,
 * what is the least amount of mana you can spend and still win the fight?
 * <p>
 * Your puzzle answer was 1289.
 */
public class Day22 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day22.class.getName());

    /**
     * Instantiates the solution instance.
     */
    public Day22() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_22-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var lowest = leastAmountOfManaAndStillWin(input, false);
        var end = Instant.now();
        LOGGER.info(String.format("The least amount of mana spent and still win is: %d%n", lowest));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var lowest = leastAmountOfManaAndStillWin(input, true);
        var end = Instant.now();
        LOGGER.info(String.format("The least amount of mana spent and still win on hard mode is: %d%n", lowest));
        logTimings(LOGGER, start, end);
    }

    /**
     * Returns the least amount of Mana the Mage can spend and still win the game.
     *
     * @param input The available Spells and the Mage and Boos stats.
     * @param hard  If true, the Mage loses one hit-point at the start of their turns.
     * @return The least amount of Mana the Mage can spend and still win the game.
     */
    public int leastAmountOfManaAndStillWin(List<String> input, boolean hard) {
        var game = AStarAndWizards.buildSimulator(input);

        var min = game.calculateLeastAmountOfManaAndStillWin(hard);

        return min.first();
    }
}
