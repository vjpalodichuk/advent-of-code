package com.capital7software.aoc.aoc2018aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.string.clean
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.system.measureNanoTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 1: Chronal Calibration ---**
 *
 * After feeling like you've been falling for a few minutes, you look at the device's
 * tiny screen. "Error: Device must be calibrated before first use. Frequency drift detected.
 * Cannot maintain destination lock." Below the message, the device shows a sequence of
 * changes in frequency (your puzzle input). A value like +6 means the current frequency
 * increases by 6; a value like -3 means the current frequency decreases by 3.
 *
 * For example, if the device displays frequency changes of +1, -2, +3, +1, then starting
 * from a frequency of zero, the following changes would occur:
 *
 * - Current frequency  0, change of +1; resulting frequency  1.
 * - Current frequency  1, change of -2; resulting frequency -1.
 * - Current frequency -1, change of +3; resulting frequency  2.
 * - Current frequency  2, change of +1; resulting frequency  3.
 *
 * In this example, the resulting frequency is 3.
 *
 * Here are other example situations:
 *
 * - +1, +1, +1 results in  3
 * - +1, +1, -2 results in  0
 * - -1, -2, -3 results in -6
 *
 * Starting with a frequency of zero, **what is the resulting frequency** after all the
 * changes in frequency have been applied?
 *
 * Your puzzle answer was **569**.
 *
 * **--- Part Two ---**
 *
 * You notice that the device repeats the same frequency change list over and over.
 * To calibrate the device, you need to find the first frequency it reaches **twice**.
 *
 * For example, using the same list of changes above, the device would loop as follows:
 *
 * - Current frequency  0, change of +1; resulting frequency  1.
 * - Current frequency  1, change of -2; resulting frequency -1.
 * - Current frequency -1, change of +3; resulting frequency  2.
 * - Current frequency  2, change of +1; resulting frequency  3.
 * - (At this point, the device continues from the start of the list.)
 * - Current frequency  3, change of +1; resulting frequency  4.
 * - Current frequency  4, change of -2; resulting frequency  2, which has already been seen.
 *
 * In this example, the first frequency reached twice is 2. Note that your device might
 * need to repeat its list of frequency changes many times before a duplicate frequency
 * is found, and that duplicates might be found while in the middle of processing the list.
 *
 * Here are other examples:
 *
 * - +1, -1 first reaches 0 twice.
 * - +3, +3, +4, -2, -4 first reaches 10 twice.
 * - -6, +3, +8, +5, -6 first reaches 5 twice.
 * - +7, +7, -2, -7, -4 first reaches 14 twice.
 *
 * **What is the first frequency your device reaches twice**?
 *
 * Your puzzle answer was **77666**.
 */
class Day01 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day01::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_01-01.txt"

  override fun runPart1(input: List<String>) {
    var answer: Int
    val elapsed = measureNanoTime {
      answer = getFrequency(input)
    }
    log.info("$answer is the resulting frequency!")
    logTimings(log, elapsed)
  }

  override fun runPart2(input: List<String>) {
    var answer: Int
    val elapsed = measureNanoTime {
      answer = getFirstRepeatedFrequency(input)
    }
    log.info("$answer is the first repeated frequency!")
    logTimings(log, elapsed)
  }

  /**
   * Returns the result of applying the frequency changes in the specified list.
   *
   * @param input The list of frequency changes.
   * @return The result of applying all the frequency changes.
   */
  @SuppressFBWarnings
  fun getFrequency(input: List<String>): Int {
    return input.sumOf { it.clean().toInt() }
  }

  /**
   * Returns the result of repeatedly applying the frequency changes in the specified list
   * until a repeated frequency is found.
   *
   * @param input The list of frequency changes.
   * @return The result of repeatedly applying all the frequency changes to find the first
   * repeated number.
   */
  @SuppressFBWarnings
  fun getFirstRepeatedFrequency(input: List<String>): Int {
    val frequencies = input.map { it.clean().toInt() }
    var done = false
    val seen = mutableSetOf<Int>()

    var current = 0
    seen.add(current)
    while (!done) {
      var i = 0

      while (i < frequencies.size) {
        current += frequencies[i]
        if (!seen.add(current)) {
          done = true
          break // out of the inner loop.
        }
        i++
      }
    }
    return current
  }
}
