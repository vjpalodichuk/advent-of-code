package com.capital7software.aoc.lib.analysis

import com.capital7software.aoc.lib.string.clean

/**
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
 * @param polymer The polymer string; which can be very long.
 * @param scrub If set to true, then the polymer will be scrubbed to find the until to
 * remove to produce the largest number of reactions.
 */
data class AlchemicalReduction(
    private val polymer: String,
    private val scrub: Boolean = false
) {
  init {
    check(polymer.isNotBlank() && polymer.length > 1) { "The polymer cannot be blank!" }
  }

  /**
   * The remaining units after the polymer reaction(s).
   */
  val remainingUnits: String by lazy { react() }

  private companion object {
    // The value added to the less than of two characters
    // If they are then equal, they react!
    private const val CHARACTER_DIFFERENCE = 'a' - 'A'
  }

  /**
   * Reactions are immediate. Anytime a reaction takes place
   * any newly adjacent units that react will do so before any
   * other units further down the polymer.
   *
   */
  private fun react(): String {
    return if (scrub) {
      preact(polymer)
    } else {
      react(polymer)
    }
  }

  private fun react(source: String): String {
    val builder = StringBuilder(source)
    var i = 0

    do {
      // Check if the current char and the next char react.
      if (doReact(builder[i], builder[i + 1])) {
        // Delete uses an exclusive end index.
        builder.delete(i, i + 2)
        // Go back one character to check for new reactions.
        i = maxOf(0, i - 1)
      } else {
        i++
      }
    } while (i < builder.length - 1)

    return builder.toString().clean()
  }

  private fun preact(source: String): String {
    var i = 'A'
    var shortestPolymer = source

    while (i <= 'Z') {
      val filtered = polymer.filter { it != i && it != i + CHARACTER_DIFFERENCE }
      val result = react(filtered)
      if (result.length < shortestPolymer.length) {
        shortestPolymer = result
      }
      i++
    }

    return shortestPolymer
  }

  private fun doReact(a: Char, b: Char): Boolean {
    val c = if (a <= b) {
      a
    } else {
      b
    }
    val d = if (a <= b) {
      b
    } else {
      a
    }

    return (c + CHARACTER_DIFFERENCE) == d
  }
}
