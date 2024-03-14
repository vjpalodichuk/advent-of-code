package com.capital7software.aoc.aoc2018aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.analysis.AlchemicalReduction
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.system.measureNanoTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 5: Alchemical Reduction ---**
 *
 * You've managed to sneak in to the prototype suit manufacturing lab. The Elves are
 * making decent progress, but are still struggling with the suit's size reduction
 * capabilities.
 *
 * While the very latest in 1518 alchemical technology might have solved their problem
 * eventually, you can do better. You scan the chemical composition of the suit's
 * material and discover that it is formed by extremely long polymers (one of which
 * is available as your puzzle input).
 *
 * The polymer is formed by smaller **units** which, when triggered, react with each other
 * such that two adjacent units of the same type and opposite polarity are destroyed.
 * Units' types are represented by letters; units' polarity is represented by
 * capitalization. For instance, r and R are units with the same type but opposite
 * polarity, whereas r and s are entirely different types and do not react.
 *
 * For example:
 *
 * - In aA, a and A react, leaving nothing behind.
 * - In abBA, bB destroys itself, leaving aA. As above, this then destroys
 * itself, leaving nothing.
 * - In abAB, no two adjacent units are of the same type, and so nothing happens.
 * - In aabAAB, even though aa and AA are of the same type, their polarities
 * match, and so nothing happens.
 *
 * Now, consider a larger example, dabAcCaCBAcCcaDA:
 *
 * ```
 * dabAcCaCBAcCcaDA  The first 'cC' is removed.
 * dabAaCBAcCcaDA    This creates 'Aa', which is removed.
 * dabCBAcCcaDA      Either 'cC' or 'Cc' are removed (the result is the same).
 * dabCBAcaDA        No further actions can be taken.
 * ```
 *
 * After all possible reactions, the resulting polymer contains **10 units**.
 *
 * **How many units remain after fully reacting the polymer you scanned?**
 *
 * Your puzzle answer was **9288**.
 *
 * **--- Part Two ---**
 *
 * Time to improve the polymer.
 *
 * One of the unit types is causing problems; it's preventing the polymer from collapsing
 * as much as it should. Your goal is to figure out which unit type is causing the most
 * problems, remove all instances of it (regardless of polarity), fully react the remaining
 * polymer, and measure its length.
 *
 * For example, again using the polymer dabAcCaCBAcCcaDA from above:
 *
 * - Removing all A/a units produces dbcCCBcCcD. Fully reacting this polymer produces dbCBcD,
 * which has length 6.
 * - Removing all B/b units produces daAcCaCAcCcaDA. Fully reacting this polymer produces
 * daCAcaDA, which has length 8.
 * - Removing all C/c units produces dabAaBAaDA. Fully reacting this polymer produces daDA,
 * which has length 4.
 * - Removing all D/d units produces abAcCaCBAcCcaA. Fully reacting this polymer produces
 * abCBAc, which has length 6.
 *
 * In this example, removing all C/c units was best, producing the answer 4.
 *
 * **What is the length of the shortest polymer you can produce** by removing all units of
 * exactly one type and fully reacting the result?
 *
 * Your puzzle answer was **5844**.
 */
class Day05 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day05::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_05-01.txt"

  override fun runPart1(input: List<String>) {
    var answer: Int
    val elapsed = measureNanoTime {
      answer = getRemainingCount(input.first())
    }
    log.info("$answer is the length of the remaining units!")
    logTimings(log, elapsed)
  }

  override fun runPart2(input: List<String>) {
    var answer: Int
    val elapsed = measureNanoTime {
      answer = getRemainingCount(input.first(), true)
    }
    log.info("$answer is the length of the remaining units using scrubbing!")
    logTimings(log, elapsed)
  }

  /**
   * Returns the number of units remaining after fully reacting the scanned polymer.
   *
   * @param input The list of overnight shift changes and falling asleep / waking up times.
   * @return The number of units remaining after fully reacting the scanned polymer.
   */
  @SuppressFBWarnings
  fun getRemainingCount(input: String, scrub: Boolean = false): Int {
    return AlchemicalReduction(input, scrub).remainingUnits.length
  }

}
