package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.string.InverseCaptcha
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 1: Inverse Captcha ---**
 *
 * You're standing in a room with "digitization quarantine" written in LEDs along one wall.
 * The only door is locked, but it includes a small interface.
 * "Restricted Area - Strictly No Digitized Users Allowed."
 *
 * It goes on to explain that you may only leave by solving a captcha to prove
 * you're **not** a human. Apparently, you only get one millisecond to solve the
 * captcha: too fast for a normal human, but it feels like hours to you.
 *
 * The captcha requires you to review a sequence of digits (your puzzle input) and
 * find the **sum** of all digits that match the **next** digit in the list. The list is
 * circular, so the digit after the last digit is the **first** digit in the list.
 *
 * For example:
 *
 * - 1122 produces a sum of 3 (1 + 2) because the first digit (1) matches the second
 * digit and the third digit (2) matches the fourth digit.
 * - 1111 produces 4 because each digit (all 1) matches the next.
 * - 1234 produces 0 because no digit matches the next.
 * - 91212129 produces 9 because the only digit that matches the next one is the last digit, 9.
 *
 * **What is the solution** to your captcha?
 *
 * Your puzzle answer was **1102**.
 *
 * **--- Part Two ---**
 *
 * You notice a progress bar that jumps to 50% completion. Apparently, the door isn't
 * yet satisfied, but it did emit a star as encouragement. The instructions change:
 *
 * Now, instead of considering the **next** digit, it wants you to consider the digit
 * **halfway around** the circular list. That is, if your list contains 10 items, only
 * include a digit in your sum if the digit 10/2 = 5 steps forward matches it.
 * Fortunately, your list has an even number of elements.
 *
 * For example:
 *
 * - 1212 produces 6: the list contains 4 items, and all four digits match the digit 2 items ahead.
 * - 1221 produces 0, because every comparison is between a 1 and a 2.
 * - 123425 produces 4, because both 2s match each other, but no other digit has a match.
 * - 123123 produces 12.
 * - 12131415 produces 4.
 *
 * **What is the solution** to your new captcha?
 */
class Day01: AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day01::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_01-01.txt"

  override fun runPart1(input: MutableList<String>) {
    val start = Instant.now()
    val answer = getCaptchaSolution(input.first())
    val end = Instant.now()

    log.info("$answer is the solution to the captcha problem!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: MutableList<String>) {
    val start = Instant.now()
    val answer = getCaptchaSolution(input.first(), true)
    val end = Instant.now()

    log.info("$answer is the solution to the captcha problem when using halfway!")
    logTimings(log, start, end)
  }

  /**
   * Solves the specified captcha and returns an integer that is the solution.
   *
   * @param input The numerical string captcha.
   * @param halfway If true, then digits are compared halfway around the circular list.
   * @return An integer that is the solution.
   */
  fun getCaptchaSolution(input: String, halfway: Boolean = false): Int {
    val instance = InverseCaptcha(input)
    return instance.solve(halfway)
  }
}
