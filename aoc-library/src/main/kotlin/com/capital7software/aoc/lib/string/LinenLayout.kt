package com.capital7software.aoc.lib.string

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * Solves the Linen Layout towel-design problem.
 *
 * The available towel patterns can be used any number of times. A design is possible when it can be
 * segmented exactly into one or more towel patterns.
 *
 * This is equivalent to a memoized word-break parser:
 *
 * - [possibleDesignCount] counts designs with at least one valid segmentation.
 * - [totalArrangementCount] counts every valid segmentation for every design.
 */
class LinenLayout private constructor(
    private val towelsByFirstStripe: Map<Char, List<String>>,
    private val designs: List<String>
) {
  /**
   * Counts how many desired designs can be made from the available towel patterns.
   *
   * @return The number of possible designs.
   */
  fun possibleDesignCount(): Long {
    return designs.count { arrangementCount(it) > 0 }.toLong()
  }

  /**
   * Counts all possible towel arrangements for all desired designs.
   *
   * @return The total number of arrangements across all designs.
   */
  fun totalArrangementCount(): Long {
    return designs.sumOf(::arrangementCount)
  }

  /**
   * Counts the number of ways the specified [design] can be segmented into available towel patterns.
   *
   * @param design The target design.
   * @return The number of valid arrangements.
   */
  fun arrangementCount(design: String): Long {
    val memo = LongArray(design.length + 1) { UNKNOWN }

    fun countFrom(index: Int): Long {
      if (index == design.length) {
        return 1L
      }

      if (memo[index] != UNKNOWN) {
        return memo[index]
      }

      val candidates = towelsByFirstStripe[design[index]].orEmpty()
      val count = candidates
          .asSequence()
          .filter { towel -> design.startsWith(towel, index) }
          .sumOf { towel -> countFrom(index + towel.length) }

      memo[index] = count
      return count
    }

    return countFrom(0)
  }

  /**
   * Companion object for [LinenLayout].
   */
  companion object {
    private const val UNKNOWN = -1L

    /**
     * Parses the puzzle input and returns a [LinenLayout].
     *
     * Expected input format:
     *
     * ```text
     * r, wr, b, g, bwu, rb, gb, br
     *
     * brwrr
     * bggr
     * ```
     *
     * @param input The puzzle input.
     * @return A parsed [LinenLayout].
     */
    @SuppressFBWarnings
    fun parse(input: List<String>): LinenLayout {
      require(input.isNotEmpty()) { "Input cannot be empty!" }

      val towels = input.first()
          .split(",")
          .map(String::trim)
          .filter(String::isNotEmpty)

      require(towels.isNotEmpty()) { "At least one towel pattern is required!" }

      val designs = input
          .drop(1)
          .map(String::trim)
          .filter(String::isNotEmpty)

      val towelsByFirstStripe = towels
          .distinct()
          .sortedByDescending(String::length)
          .groupBy(String::first)

      return LinenLayout(towelsByFirstStripe, designs)
    }
  }
}
