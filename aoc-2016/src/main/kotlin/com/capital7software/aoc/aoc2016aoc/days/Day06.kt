package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.crypt.SignalNoise
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * --- Day 6: Signals and Noise ---
 *
 * Something is jamming your communications with Santa. Fortunately, your signal is
 * only partially jammed, and protocol in situations like this is to switch to a simple
 * repetition code to get the message through.
 *
 * In this model, the same message is sent repeatedly. You've recorded the repeating
 * message signal (your puzzle input), but the data seems quite corrupted - almost too
 * badly to recover. **Almost**.
 *
 * All you need to do is figure out which character is most frequent for each position.
 * For example, suppose you had recorded the following messages:
 *
 *    eedadn
 *    drvtee
 *    eandsr
 *    raavrd
 *    atevrs
 *    tsrnev
 *    sdttsa
 *    rasrtv
 *    nssdts
 *    ntnada
 *    svetve
 *    tesnvt
 *    vntsnd
 *    vrdear
 *    dvrsen
 *    enarar
 * The most common character in the first column is e; in the second, a; in the third, s,
 * and so on. Combining these characters returns the error-corrected message, `easter`.
 *
 * Given the recording in your puzzle input, **what is the error-corrected version of
 * the message being sent?**
 *
 * Your puzzle answer was tzstqsua.
 *
 * --- Part Two ---
 *
 * Of course, that **would** be the message - if you hadn't agreed to use a **modified
 * repetition code** instead.
 *
 * In this modified code, the sender instead transmits what looks like random data,
 * but for each character, the character they actually want to send is **slightly
 * less likely** than the others. Even after signal-jamming noise, you can look at
 * the letter distributions in each column and choose the **least common** letter to
 * reconstruct the original message.
 *
 * In the above example, the least common character in the first column is a;
 * in the second, d, and so on. Repeating this process for the remaining characters
 * produces the original message, advent.
 *
 * Given the recording in your puzzle input and this new decoding methodology,
 * **what is the original message** that Santa is trying to send?
 *
 * Your puzzle answer was `myregdnr`.
 *
 */
class Day06 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day06::class.java)
  }

  override fun getDefaultInputFilename(): String {
    return "inputs/input_day_06-01.txt"
  }

  override fun runPart1(input: List<String>) {
    val start = Instant.now()
    val answer = errorCorrectSignal(input)
    val end = Instant.now()

    log.info("{} is the error corrected message!", answer)
    logTimings(log, start, end)
  }

  override fun runPart2(input: List<String>) {
    val start = Instant.now()
    val answer = errorCorrectSignal(input, true)
    val end = Instant.now()

    log.info("{} is the least common error corrected message!", answer)
    logTimings(log, start, end)
  }

  /**
   * Generates and returns the error corrected message found in the specified degraded
   * signal.
   *
   * @param input The [List] of [String] that make up the signal to error correct.
   * @param leastCommon If true, then the least common letter in each column is used.
   * @return The error corrected message found in the specified degraded signal.
   */
  fun errorCorrectSignal(input: List<String>, leastCommon: Boolean = false): String {
    return SignalNoise.errorCorrectSignal(input, leastCommon)
  }
}
