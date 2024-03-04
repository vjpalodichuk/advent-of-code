package com.capital7software.aoc.lib.crypt

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * Validates password phrases.
 *
 * @param phrases The [List] of passphrases to validate.
 */
class PassphraseValidator(phrases: List<String>) {
  private val passPhrases: List<String> = phrases.toList()

  /**
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
   * @return The [List] of valid phrases.
   */
  fun validPhrases(): List<String> {
    val answer = mutableListOf<String>()
    passPhrases.forEach { phrase ->
      val words = phrase.split("\\s+".toRegex())

      val possiblyValid = mutableSetOf<String>()

      words.forEach { possiblyValid.add(it) }

      if (possiblyValid.size == words.size) {
        answer.add(phrase)
      }
    }

    return answer
  }

  /**
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
   * @return The [List] of valid phrases.
   */
  @SuppressFBWarnings
  fun validNewPhraseRules(): List<String> {
    val answer = mutableListOf<String>()
    passPhrases.forEach { phrase ->
      val words = phrase.split("\\s+".toRegex()).map { String(it.toCharArray().apply { sort() }) }

      val wordSet = mutableSetOf<String>()

      words.forEach { wordSet.add(it) }

      if (words.size == wordSet.size) {
        answer.add(phrase)
      }
    }

    return answer
  }
}
