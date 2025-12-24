package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.crypt.PassphraseValidator
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 4: High-Entropy Passphrases ---**
 *
 * A new system policy has been put in place that requires all accounts to use
 * a **passphrase** instead of simply a pass**word**. A passphrase consists of a series
 * of words (lowercase letters) separated by spaces.
 *
 * To ensure security, a valid passphrase must contain no duplicate words.
 *
 * For example:
 *
 * - aa bb cc dd ee is valid.
 * - aa bb cc dd aa is not valid - the word aa appears more than once.
 * - aa bb cc dd aaa is valid - aa and aaa count as different words.
 *
 * The system's full passphrase list is available as your puzzle input. **How many
 * passphrases are valid?**
 *
 * Your puzzle answer was **477**.
 *
 * **--- Part Two ---**
 *
 * For added security, yet another system policy has been put in place. Now, a valid passphrase
 * must contain no two words that are anagrams of each other - that is, a passphrase is
 * invalid if any word's letters can be rearranged to form any other word in the passphrase.
 *
 * For example:
 *
 * - abcde fghij is a valid passphrase.
 * - abcde xyz ecdab is not valid - the letters from the third word can be rearranged to
 * form the first word.
 * - a ab abc abd abf abj is a valid passphrase, because all letters need to be used when
 * forming another word.
 * - iiii oiii ooii oooi oooo is valid.
 * - oiii ioii iioi iiio is not valid - any of these words can be rearranged to form any
 * other word.
 *
 * Under this new system policy, **how many passphrases are valid?**
 *
 * Your puzzle answer was **167**.
 */
class Day04 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day04::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_04-01.txt"

  override fun runPart1(input: MutableList<String>) {
    val start = Instant.now()
    val answer = validPassphraseCount(input)
    val end = Instant.now()

    log.info("$answer is the number of valid passphrases!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: MutableList<String>) {
    val start = Instant.now()
    val answer = validNewRulesPassphraseCount(input)
    val end = Instant.now()

    log.info("$answer is the number of valid passphrases!")
    logTimings(log, start, end)
  }

  /**
   * Returns the count of valid passphrases.
   *
   * @param input The [List] of [String] passphrases to validate and count.
   * @return The number of valid passphrases.
   */
  fun validPassphraseCount(input: List<String>): Int {
    val instance = PassphraseValidator(input)

    return instance.validPhrases().size
  }

  /**
   * Returns the count of valid passphrases.
   *
   * @param input The [List] of [String] passphrases to validate and count.
   * @return The number of valid passphrases.
   */
  fun validNewRulesPassphraseCount(input: List<String>): Int {
    val instance = PassphraseValidator(input)

    return instance.validNewPhraseRules().size
  }
}
