package com.capital7software.aoc.lib.math

import com.capital7software.aoc.lib.util.Triple
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.math.absoluteValue

/**
 * Fortunately, the first location The Historians want to search isn't a long walk
 * from the Chief Historian's office.
 *
 * While the [Red-Nosed Reindeer nuclear fusion/fission plant](https://adventofcode.com/2015/day/19)
 * appears to contain no sign of the Chief Historian, the engineers there run
 * up to you as soon as they see you. Apparently, they **still** talk about the
 * time Rudolph was saved through molecular synthesis from a single electron.
 *
 * They're quick to add that - since you're already here - they'd really appreciate
 * your help analyzing some unusual data from the Red-Nosed reactor. You turn to
 * check if The Historians are waiting for you, but they seem to have already divided
 * into groups that are currently searching every corner of the facility. You offer
 * to help with the unusual data.
 *
 * @param input The [String] [List] of reports.
 */
class RedNosedReports @SuppressFBWarnings constructor(input: List<String>) {
  private companion object {
    private const val MIN_DISTANCE = 1L
    private const val MAX_DISTANCE = 3L
  }

  private val reports =
      input.map { report -> report.split(' ').map { level -> level.toLong() } }

  private enum class LevelTestResult {
    SAFE,
    INCREASING_TO_DECREASING,
    EXCEED_MAX_DISTANCE,
    BELOW_MIN_DISTANCE,
    DECREASING_TO_INCREASING,
  }

  private enum class LevelDirection {
    NONE,
    INCREASING,
    DECREASING,
  }

  /**
   * The unusual data (your puzzle input) consists of many **reports**, one report
   * per line. Each report is a list of numbers called **levels** that are separated
   * by spaces. For example:
   *
   * ```
   * 7 6 4 2 1
   * 1 2 7 8 9
   * 9 7 6 2 1
   * 1 3 2 4 5
   * 8 6 4 4 1
   * 1 3 6 7 9
   * ```
   *
   * This example data contains six reports each containing five levels.
   *
   * The engineers are trying to figure out which reports are **safe**.
   * The Red-Nosed reactor safety systems can only tolerate levels that are
   * either gradually increasing or gradually decreasing. So, a report only
   * counts as safe if both of the following are true:
   *
   * - The levels are either **all increasing** or **all decreasing**.
   * - Any two adjacent levels differ by **at least one** and **at most three**.
   *
   * In the example above, the reports can be found safe or unsafe by checking those rules:
   *
   * - ```7 6 4 2 1```: **Safe** because the levels are all decreasing by 1 or 2.
   * - ```1 2 7 8 9```: **Unsafe** because 2 7 is an increase of 5.
   * - ```9 7 6 2 1```: **Unsafe** because 6 2 is a decrease of 4.
   * - ```1 3 2 4 5```: **Unsafe** because 1 3 is increasing but 3 2 is decreasing.
   * - ```8 6 4 4 1```: **Unsafe** because 4 4 is neither an increase nor a decrease.
   * - ```1 3 6 7 9```: **Safe** because the levels are all increasing by 1, 2, or 3.
   *
   * So, in this example, ```2``` reports are **safe**.
   *
   * The Problem Dampener is a reactor-mounted module that lets the reactor safety systems
   * **tolerate a single bad level** in what would otherwise be a safe report. It's like
   * the bad level never happened!
   *
   * Now, the same rules apply as before, except if removing a single level from an unsafe
   * report would make it safe, the report instead counts as safe.
   *
   * More of the above example's reports are now safe:
   *
   * - ```7 6 4 2 1```: **Safe** without removing any level.
   * - ```1 2 7 8 9```: **Unsafe** regardless of which level is removed.
   * - ```9 7 6 2 1```: **Unsafe** regardless of which level is removed.
   * - ```1 3 2 4 5```: **Safe** by removing the second level, 3.
   * - ```8 6 4 4 1```: **Safe** by removing the third level, 4.
   * - ```1 3 6 7 9```: **Safe** without removing any level.
   *
   * Thanks to the Problem Dampener, **```4```** reports are actually **safe**!
   *
   * @param useDampener If set to true, then a failing report will be considered valid if it
   * passes validation by removing upto 1 level from the report.
   */
  @SuppressFBWarnings
  fun calculateNumberOfSafeReports(useDampener: Boolean = false): Long {
    return reports.map { isReportSafe(it, useDampener) }.count { it }.toLong()
  }

  private fun isReportSafe(report: List<Long>, useDampener: Boolean): Boolean {
    // Require at least two or more items.
    if (report.size <= 1) {
      return true
    }

    val i = 0
    val j = 1
    val currentDirection = LevelDirection.NONE // Tracks increasing, decreasing, or neither
    val end = report.size - 1

    return testLevelsRecursive(
        report,
        currentDirection,
        i,
        j,
        end,
        useDampener,
        Integer.MIN_VALUE,
        0
    )
  }

  private fun testLevelsRecursive(
      report: List<Long>,
      currentDirection: LevelDirection,
      i: Int,
      j: Int,
      end: Int,
      useDampener: Boolean,
      removedIndex: Int,
      errorCount: Int,
  ): Boolean {

    if (j > end) return errorCount == 0 || (errorCount == 1 && useDampener)

    val results = testLevels(report[i], report[j], currentDirection)

    /**
     * If the level passes, return the result of testing the next levels
     *
     * Else if the level doesn't pass, and we already have removed an index, or there is no
     * dampener, return false.
     *
     * Else if we haven't removed an index yet, and we are using the dampener then return:
     *  If we are at i == 0, the result of testing indexes 0 and 2 or testing indexes 1 and 2;
     *    both with a current direction of none.
     *  If we are at i == 1, the result of testing indexes 0 and 2 or testing indexes 1 and 2;
     *    both with a current direction of none, or testing indexes 1 and 3 using the current
     *    direction.
     *  Else the result of testing indexes i - 1 and j or i and j + 1 all using the current
     *    direction.
     */
    return if (results.first() == LevelTestResult.SAFE && results.second() == LevelTestResult.SAFE) {
      var nextI = i + 1
      val nextJ = j + 1
      if (removedIndex > Integer.MIN_VALUE && nextI == removedIndex) {
        nextI++
      }
      testLevelsRecursive(report, results.third(), nextI, nextJ, end, useDampener, removedIndex, errorCount)
    } else if (!useDampener || errorCount > 0) {
      false
    } else if (i > 0) {
      val nextI = i - 1

      if (i == 1) {
        testLevelsRecursive(report, LevelDirection.NONE, i, j, end, useDampener, 0, errorCount + 1)
            || testLevelsRecursive(report, LevelDirection.NONE, nextI, j, end, useDampener, i, errorCount + 1)
            || testLevelsRecursive(report, currentDirection, i, j + 1, end, useDampener, j, errorCount + 1)
      } else {
        testLevelsRecursive(report, currentDirection, nextI, j, end, useDampener, i, errorCount + 1)
            || testLevelsRecursive(report, currentDirection, i, j + 1, end, useDampener, j, errorCount + 1)
      }
    } else {
      testLevelsRecursive(report, currentDirection, i, j + 1, end, useDampener, j, errorCount + 1)
          || testLevelsRecursive(report, currentDirection, j, j + 1, end, useDampener, i, errorCount + 1)
    }
  }

  private fun testLevels(
      left: Long,
      right: Long,
      currentDirection: LevelDirection,
  ): Triple<LevelTestResult, LevelTestResult, LevelDirection> {
    val difference = left - right
    val direction = calculateDirection(difference)
    val distance = difference.absoluteValue

    return Triple(testDirection(currentDirection, direction), testDistance(distance), direction)
  }

  private fun calculateDirection(difference: Long): LevelDirection {
    return if (difference == 0L) {
      LevelDirection.NONE
    } else if (difference < 0L) {
      LevelDirection.INCREASING
    } else {
      LevelDirection.DECREASING
    }
  }

  private fun testDirection(
      currentDirection: LevelDirection,
      newDirection: LevelDirection
  ): LevelTestResult {
    return when (currentDirection) {
      LevelDirection.NONE ->
        if (newDirection != LevelDirection.NONE) LevelTestResult.SAFE
        else LevelTestResult.INCREASING_TO_DECREASING

      LevelDirection.INCREASING ->
        if (newDirection == LevelDirection.INCREASING) LevelTestResult.SAFE
        else LevelTestResult.INCREASING_TO_DECREASING

      LevelDirection.DECREASING ->
        if (newDirection == LevelDirection.DECREASING) LevelTestResult.SAFE
        else LevelTestResult.DECREASING_TO_INCREASING
    }
  }

  private fun testDistance(distance: Long): LevelTestResult {
    return if (distance < MIN_DISTANCE) {
      LevelTestResult.BELOW_MIN_DISTANCE
    } else if (distance > MAX_DISTANCE) {
      LevelTestResult.EXCEED_MAX_DISTANCE
    } else {
      LevelTestResult.SAFE
    }
  }
}
