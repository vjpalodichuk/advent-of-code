package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.analysis.ElfParty
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * --- Day 19: An Elephant Named Joseph ---
 *
 * The Elves contact you over a highly secure emergency channel. Back at the North Pole,
 * the Elves are busy misunderstanding White Elephant parties.
 *
 * Each Elf brings a present. They all sit in a circle, numbered starting with position 1.
 * Then, starting with the first Elf, they take turns stealing all the presents from the
 * Elf to their left. An Elf with no presents is removed from the circle and does not take turns.
 *
 * For example, with five Elves (numbered 1 to 5):
 * ```
 *   1
 * 5   2
 *  4 3
 * ```
 *
 * - Elf 1 takes Elf 2's present.
 * - Elf 2 has no presents and is skipped.
 * - Elf 3 takes Elf 4's present.
 * - Elf 4 has no presents and is also skipped.
 * - Elf 5 takes Elf 1's two presents.
 * - Neither Elf 1 nor Elf 2 have any presents, so both are skipped.
 * - Elf 3 takes Elf 5's three presents.
 *
 * So, with **five** Elves, the Elf that sits starting in position 3 gets all the presents.
 *
 * With the number of Elves given in your puzzle input, **which Elf gets all the presents?**
 *
 * Your puzzle input is 3001330.
 *
 * Your puzzle answer was 1808357.
 *
 * --- Part Two ---
 *
 * Realizing the folly of their present-exchange rules, the Elves agree to instead steal
 * presents from the Elf **directly across the circle**. If two Elves are across the circle,
 * the one on the left (from the perspective of the stealer) is stolen from. The other rules
 * remain unchanged: Elves with no presents are removed from the circle entirely, and the
 * other elves move in slightly to keep the circle evenly spaced.
 *
 * For example, with five Elves (again numbered 1 to 5):
 *
 * - The Elves sit in a circle; Elf 1 goes first:
 * ```
 *   1
 * 5   2
 *  4 3
 * ```
 * - Elves 3 and 4 are across the circle; Elf 3's present is stolen, being the one to the left.
 * Elf 3 leaves the circle, and the rest of the Elves move in:
 * ```
 *   1           1
 * 5   2  -->  5   2
 *  4 -          4
 * ```
 * - Elf 2 steals from the Elf directly across the circle, Elf 5:
 * ```
 *   1         1
 * -   2  -->     2
 *   4         4
 * ```
 * - Next is Elf 4 who, choosing between Elves 1 and 2, steals from Elf 1:
 * ```
 *  -          2
 *     2  -->
 *  4          4
 * ```
 * - Finally, Elf 2 steals from Elf 4:
 * ```
 *  2
 *     -->  2
 *  -
 * ```
 * So, with **five** Elves, the Elf that sits starting in position 2 gets all the presents.
 *
 * With the number of Elves given in your puzzle input, **which Elf now gets all the presents?**
 *
 * Your puzzle answer was 1407007.
 */
class Day19 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day19::class.java)
  }

  override fun getDefaultInputFilename(): String {
    return "inputs/input_day_19-01.txt"
  }

  override fun runPart1(input: List<String>) {
    val start = Instant.now()
    val answer = elfWithAllPresentsLeftExchangeFast(input.first().toInt())
    val end = Instant.now()

    log.info("${answer.id} is the Elf with all ${answer.presents} presents!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: List<String>) {
    val start = Instant.now()
    val answer = elfWithAllPresentsAcrossExchangeFast(input.first().toInt())
    val end = Instant.now()

    log.info("${answer.id} is the Elf with all ${answer.presents} presents!")
    logTimings(log, start, end)
  }

  /**
   * Calculates and returns the Elf with all the presents after the linear exchange is done.
   *
   * @param numElves The number of Elves exchanging gifts.
   * @return The Elf that is left with all the presents.
   */
  @SuppressFBWarnings
  fun elfWithAllPresentsLeftExchange(numElves: Int): ElfParty.Elf {
    val elf = ElfParty.leftExchange(numElves)
    return elf
  }

  /**
   * Calculates and returns the Elf with all the presents after the linear exchange is done.
   *
   * @param numElves The number of Elves exchanging gifts.
   * @return The Elf that is left with all the presents.
   */
  fun elfWithAllPresentsLeftExchangeFast(numElves: Int): ElfParty.Elf {
    val elf = ElfParty.leftExchangeFast(numElves)
    return elf
  }

  /**
   * Calculates and returns the Elf with all the presents after the across exchange is done.
   *
   * @param numElves The number of Elves exchanging gifts.
   * @return The Elf that is left with all the presents.
   */
  fun elfWithAllPresentsAcrossExchange(numElves: Int): ElfParty.Elf {
    val elf = ElfParty.acrossExchange(numElves)
    return elf
  }

  /**
   * Calculates and returns the Elf with all the presents after the across exchange is done.
   *
   * @param numElves The number of Elves exchanging gifts.
   * @return The Elf that is left with all the presents.
   */
  fun elfWithAllPresentsAcrossExchangeFast(numElves: Int): ElfParty.Elf {
    val elf = ElfParty.acrossExchangeFast(numElves)
    return elf
  }
}
