package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.analysis.PermutationPromenade
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 16: Permutation Promenade ---**
 *
 * You come upon a very unusual sight; a group of programs here appear to be dancing.
 *
 * There are sixteen programs in total, named a through p. They start by standing in
 * a line: a stands in position 0, b stands in position 1, and so on until p, which
 * stands in position 15.
 *
 * The programs' **dance** consists of a sequence of **dance moves**:
 *
 * - **Spin**, written sX, makes X programs move from the end to the front, but maintain
 * their order otherwise. (For example, s3 on abcde produces cdeab).
 * - **Exchange**, written xA/B, makes the programs at positions A and B swap places.
 * - **Partner**, written pA/B, makes the programs named A and B swap places.
 *
 * For example, with only five programs standing in a line (abcde), they could
 * do the following dance:
 *
 * - s1, a spin of size 1: eabcd.
 * - x3/4, swapping the last two programs: eabdc.
 * - pe/b, swapping programs e and b: baedc.
 *
 * After finishing their dance, the programs end up in order baedc.
 *
 * You watch the dance for a while and record their dance moves (your puzzle input).
 * **In what order are the programs standing** after their dance?
 *
 * Your puzzle answer was **bkgcdefiholnpmja**.
 *
 * **--- Part Two ---**
 *
 * Now that you're starting to get a feel for the dance moves, you turn your attention to
 * **the dance as a whole**.
 *
 * Keeping the positions they ended up in from their previous dance, the programs perform
 * it again and again: including the first dance, a total of **one billion** (1000000000) times.
 *
 * In the example above, their second dance would **begin** with the order baedc, and use
 * the same dance moves:
 *
 * - s1, a spin of size 1: cbaed.
 * - x3/4, swapping the last two programs: cbade.
 * - pe/b, swapping programs e and b: ceadb.
 *
 * **In what order are the programs standing** after their billion dances?
 *
 * Your puzzle answer was **knmdfoijcbpghlea**.
 */
class Day16 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day16::class.java)
    private const val DEFAULT_PROGRAM_COUNT: Int = 16
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_16-01.txt"

  override fun runPart1(input: MutableList<String>) {
    val start = Instant.now()
    val answer = justDance(input.first())
    val end = Instant.now()

    log.info("$answer is the programs after performing the dance once!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: MutableList<String>) {
    val times = 1_000_000_000
    val start = Instant.now()
    val answer = justDance(input.first(), times = times)
    val end = Instant.now()

    log.info("$answer is the programs after performing the dance $times times!")
    logTimings(log, start, end)
  }

  /**
   * Returns the position of the programs after they perform their dance.
   *
   * @param input The [String] encoded dance to perform.
   * @param programCount The number of programs that are dancing.
   * @param times The number of times to perform the dance.
   * @return The position of the programs after they perform their dance.
   */
  fun justDance(input: String, programCount: Int = DEFAULT_PROGRAM_COUNT, times: Int = 1): String {
    val instance = PermutationPromenade(input, programCount)
    return instance.performDance(times)
  }
}
