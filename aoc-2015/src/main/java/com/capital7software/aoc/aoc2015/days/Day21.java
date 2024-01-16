package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.graph.constaint.SwordsAndStuff;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 21: RPG Simulator 20XX ---<br>
 * Little Henry Case got a new video game for Christmas. It's an RPG, and he's stuck on a boss.
 * He needs to know what equipment to buy at the shop. He hands you the controller.
 * <p><br>
 * In this game, the mage (you) and the enemy (the boss) take turns attacking.
 * The mage always goes first. Each attack reduces the opponent's hit points by at least 1.
 * The first character at or below 0 hit points loses.
 * <p><br>
 * Damage dealt by an attacker each turn is equal to the attacker's damage score minus the
 * defender's armor score. An attacker always does at least 1 damage. So, if the attacker
 * has a damage score of 8, and the defender has an armor score of 3, the defender loses
 * 5 hit points. If the defender had an armor score of 300, the defender would still lose 1 hit point.
 * <p><br>
 * Your damage score and armor score both start at zero. They can be increased by buying
 * items in exchange for gold. You start with no items and have as much gold as you need.
 * Your total damage or armor is equal to the sum of those stats from all of your items.
 * You have 100 hit points.
 * <p><br>
 * Here is what the item shop is selling:
 * <p><br>
 * Weapons:    Cost  Damage  Armor<br>
 * Dagger        8     4       0<br>
 * Shortsword   10     5       0<br>
 * Warhammer    25     6       0<br>
 * Longsword    40     7       0<br>
 * Greataxe     74     8       0<br>
 * <p>
 * Armor:      Cost  Damage  Armor<br>
 * Leather      13     0       1<br>
 * Chainmail    31     0       2<br>
 * Splintmail   53     0       3<br>
 * Bandedmail   75     0       4<br>
 * Platemail   102     0       5<br>
 * <p>
 * Rings:      Cost  Damage  Armor<br>
 * Damage +1    25     1       0<br>
 * Damage +2    50     2       0<br>
 * Damage +3   100     3       0<br>
 * Defense +1   20     0       1<br>
 * Defense +2   40     0       2<br>
 * Defense +3   80     0       3<br>
 * <p><br>
 * You must buy exactly one weapon; no dual-wielding. Armor is optional, but you can't use more than one. You can buy 0-2 rings (at most one for each hand). You must use any items you buy. The shop only has one of each item, so you can't buy, for capital7software, two rings of Damage +3.
 * <p><br>
 * For capital7software, suppose you have 8 hit points, 5 damage, and 5 armor, and that the boss has 12 hit points, 7 damage, and 2 armor:
 * <p><br>
 * The mage deals 5-2 = 3 damage; the boss goes down to 9 hit points.<br>
 * The boss deals 7-5 = 2 damage; the mage goes down to 6 hit points.<br>
 * The mage deals 5-2 = 3 damage; the boss goes down to 6 hit points.<br>
 * The boss deals 7-5 = 2 damage; the mage goes down to 4 hit points.<br>
 * The mage deals 5-2 = 3 damage; the boss goes down to 3 hit points.<br>
 * The boss deals 7-5 = 2 damage; the mage goes down to 2 hit points.<br>
 * The mage deals 5-2 = 3 damage; the boss goes down to 0 hit points.<br>
 * <p><br>
 * In this scenario, the mage wins! (Barely.)
 * <p><br>
 * You have 100 hit points. The boss's actual stats are in your puzzle input. What is the least amount of gold you can spend and still win the fight?
 * <p>
 * Your puzzle answer was 91.
 * <p>
 *     --- Part Two ---<br>
 * Turns out the shopkeeper is working with the boss, and can persuade you to buy whatever items he wants.
 * The other rules still apply, and he still only has one of each item.
 * <p>
 * What is the most amount of gold you can spend and still lose the fight?
 * <p>
 * Your puzzle answer was 158.
 *
 */
public class Day21 implements AdventOfCodeSolution {
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
        System.out.printf("The least amount of gold spent and still win is: %d%n", lowest);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var lowest = mostAmountOfGoldAndStillLose(input);
        var end = Instant.now();
        System.out.printf("The most amount of gold spent and still lose is: %d%n", lowest);
        printTiming(start, end);
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
