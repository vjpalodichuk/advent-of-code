package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.crypt.PasswordScrambler
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * --- Day 21: Scrambled Letters and Hash ---
 *
 * The computer system you're breaking into uses a weird scrambling function to store its
 * passwords. It shouldn't be much trouble to create your own scrambled password, so you can
 * add it to the system; you just have to implement the scrambler.
 *
 * The scrambling function is a series of operations (the exact list is provided in your
 * puzzle input). Starting with the password to be scrambled, apply each operation in
 * succession to the string. The individual operations behave as follows:
 *
 * - swap position X with position Y means that the letters at indexes X and Y
 * (counting from 0) should be **swapped.**
 * - swap letter X with letter Y means that the letters X and Y should be **swapped**
 * (regardless of where they appear in the string).
 * - rotate left/right X steps means that the whole string should be **rotated**;
 * for example, one right rotation would turn abcd into dabc.
 * - rotate based on position of letter X means that the whole string should
 * be **rotated to the right** based on the **index** of letter X (counting from 0)
 * as determined **before** this instruction does any rotations. Once the index is
 * determined, rotate the string to the right one time, plus a number of times equal
 * to that index, plus one additional time if the index was at least 4.
 * - reverse positions X through Y means that the span of letters at indexes X through
 * Y (including the letters at X and Y) should be **reversed in order.**
 * - move position X to position Y means that the letter which is at index X should
 * be **removed** from the string, then **inserted** such that it ends up at index Y.
 *
 * For example, suppose you start with abcde and perform the following operations:
 *
 * - swap position 4 with position 0 swaps the first and last letters, producing the
 * input for the next step, ebcda.
 * - swap letter d with letter b swaps the positions of d and b: edcba.
 * - reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
 * - rotate left 1-step shifts all letters left one position, causing the first letter
 * to wrap to the end of the string: bcdea.
 * - move position 1 to position 4 removes the letter at position 1 (c), then inserts
 * it at position 4 (the end of the string): bdeac.
 * - move position 3 to position 0 removes the letter at position 3 (a), then inserts
 * it at position 0 (the front of the string): abdec.
 * - rotate based on position of letter b finds the index of letter b (1), then rotates
 * the string right once plus a number of times equal to that index (2): ecabd.
 * - rotate based on position of letter d finds the index of letter d (4), then rotates
 * the string right once, plus a number of times equal to that index, plus an additional
 * time because the index was at least 4, for a total of 6 right rotations: decab.
 *
 * After these steps, the resulting scrambled password is decab.
 *
 * Now, you just need to generate a new scrambled password, and you can access the system.
 * Given the list of scrambling operations in your puzzle input,
 * **what is the result of scrambling abcdefgh?**
 *
 * Your puzzle answer was bfheacgd.
 *
 * --- Part Two ---
 *
 * You scrambled the password correctly, but you discover that you can't actually modify
 * the password file on the system. You'll need to un-scramble one of the existing passwords
 * by reversing the scrambling process.
 *
 * What is the un-scrambled version of the scrambled password fbgdceah?
 *
 * Your puzzle answer was gcehdbfa.
 */
class Day21 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day21::class.java)
  }

  override fun getDefaultInputFilename(): String {
    return "inputs/input_day_21-01.txt"
  }

  override fun runPart1(input: List<String>) {
    val salt = "abcdefgh"
    val start = Instant.now()
    val answer = scramble(salt, input)
    val end = Instant.now()

    log.info("$answer is the scrambled password!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: List<String>) {
    val salt = "fbgdceah"
    val start = Instant.now()
    val answer = unscramble(salt, input)
    val end = Instant.now()

    log.info("$answer is the unscrambled password!")
    logTimings(log, start, end)
  }

  /**
   * Scrambles and returns a scrambled password based on the specified password and list of
   * instructions.
   *
   * @param password The starting string of the password to scramble.
   * @param input The list of instructions on how to scramble a password.
   * @return The scrambled password.
   */
  @SuppressFBWarnings
  fun scramble(password: String, input: List<String>): String {
    val instance = PasswordScrambler(input)
    return instance.scramble(password)
  }

  /**
   * Unscrambles the specified scrambled password based on the inverse of the specified
   * instructions.
   *
   * @param password The scrambled password to unscramble.
   * @param input The list of instructions on how to scramble a password.
   * @return The unscrambled password.
   */
  @SuppressFBWarnings
  fun unscramble(password: String, input: List<String>): String {
    val instance = PasswordScrambler(input)
    return instance.unscramble(password)
  }
}
