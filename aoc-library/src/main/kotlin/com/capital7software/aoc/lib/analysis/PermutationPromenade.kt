package com.capital7software.aoc.lib.analysis

import com.capital7software.aoc.lib.string.RotateRight
import com.capital7software.aoc.lib.string.StringOperation
import com.capital7software.aoc.lib.string.SwapLetters
import com.capital7software.aoc.lib.string.SwapPositions
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

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
 * @param dance: The comma (,) separated dance instructions.
 * @param numPrograms: The number of programs participating in the dance. Must be between
 * 5 and 26; defaults to 16.
 */
class PermutationPromenade(dance: String, numPrograms: Int = DEFAULT_PROGRAM_COUNT) {
  private val programs: String = generatePrograms(numPrograms)
  private val moves: List<StringOperation> = parseMoves(dance)

  private companion object {
    private const val DEFAULT_PROGRAM_COUNT: Int = 16
    private const val MIN_PROGRAM_COUNT: Int = 5
    private const val MAX_PROGRAM_COUNT: Int = 26
    private val SPIN = """s(\d+)""".toRegex()
    private val EXCHANGE = """x(\d+)/(\d+)""".toRegex()
    private val PARTNER = """p([a-zA-Z]+)/([a-zA-Z]+)""".toRegex()
  }

  private fun generatePrograms(count: Int): String {
    check(count in MIN_PROGRAM_COUNT..MAX_PROGRAM_COUNT) {
      "$count is outside the range of $MIN_PROGRAM_COUNT to $MAX_PROGRAM_COUNT inclusive"
    }

    val answer = StringBuilder()

    for (i in 'a'..'z') {
      answer.append(i)

      if (answer.length >= count) {
        break
      }
    }

    return answer.toString()
  }

  @SuppressFBWarnings
  private fun parseMoves(dance: String): List<StringOperation> =
      dance.split(",").map { parseMove(it) }

  private fun parseMove(move: String): StringOperation {
    return when (move.first()) {
      'p' -> {
        val match = PARTNER.find(move)!!
        SwapLetters(match.groups[1]!!.value[0], match.groups[2]!!.value[0])
      }

      's' -> {
        val match = SPIN.find(move)!!
        RotateRight(match.groups[1]!!.value.toInt())
      }

      'x' -> {
        val match = EXCHANGE.find(move)!!
        SwapPositions(match.groups[1]!!.value.toInt(), match.groups[2]!!.value.toInt())
      }

      else -> error("Unknown move: $move")
    }
  }

  /**
   * Performs (or calculates) the dance the specified number of [times] and returns the state the
   * programs are in after.
   *
   * @param times The requested number of times to perform the dance. If a cycle is detected, then
   * the ending position is calculated from the previous result.
   */
  fun performDance(times: Int = 1): String {
    val cache = mutableListOf<String>()
    val seen = mutableSetOf<String>()
    var chars = programs.toCharArray().copyInto(CharArray(programs.length))
    var detectedCycleIndex = 0
    var answer = String(chars)
    cache.add(answer)
    seen.add(answer)

    var cycleDetected = false
    var count = 0

    while (!cycleDetected && count < times) {
      moves.forEach {
        it.execute(chars)
      }

      chars = chars.copyInto(CharArray(chars.size))
      answer = String(chars)
      count++

      val index = if (answer in seen) {
        cache.indexOf(answer)
      } else {
        -1
      }

      if (index != -1) {
        detectedCycleIndex = index
        cycleDetected = true
      } else {
        cache.add(answer)
        seen.add(answer)
      }
    }
    var calculatedIndex = times

    if (cycleDetected && calculatedIndex >= detectedCycleIndex) {
      // The end state has already been cached so calculate the index to it.
      // The index can be found using this formula:
      // index = indexOfFirstInstanceOfDuplicate +
      // ((dancesToPerform - indexOfFirstInstanceOfDuplicate) %
      // (actualDancesPerformed - indexOfFirstInstanceOfDuplicate))
      calculatedIndex = (detectedCycleIndex
          + ((times - detectedCycleIndex) % (count - detectedCycleIndex)))
      answer = cache[calculatedIndex]
    }
    return answer
  }
}
