package com.capital7software.aoc.lib.string

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings


/**
 * As you walk through the door, a glowing humanoid shape yells in your direction.
 * "You there! Your state appears to be idle. Come help us repair the corruption in
 * this spreadsheet - if we take another millisecond, we'll have to display an hourglass cursor!"
 *
 * The spreadsheet consists of rows of apparently-random numbers. To make sure the
 * recovery process is on the right track, they need you to calculate the spreadsheet's
 * **checksum**. For each row, determine the difference between the largest value and the
 * smallest value; the checksum is the sum of all of these differences.
 *
 * For example, given the following spreadsheet:
 *
 * ```
 * 5 1 9 5
 * 7 5 3
 * 2 4 6 8
 * ```
 *
 * - The first row's largest and smallest values are 9 and 1, and their difference is 8.
 * - The second row's largest and smallest values are 7 and 3, and their difference is 4.
 * - The third row's difference is 6.
 *
 * In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.
 *
 * "Based on what we're seeing, it looks like all the User wanted is some information about
 * the **evenly divisible values** in the spreadsheet. Unfortunately, none of us are
 * equipped for that kind of calculation - most of us specialize in bitwise operations."
 *
 * It sounds like the goal is to find the only two numbers in each row where one evenly
 * divides the other - that is, where the result of the division operation is a whole
 * number. They would like you to find those numbers on each line, divide them,
 * and add up each line's result.
 *
 * For example, given the following spreadsheet:
 *
 * ```
 * 5 9 2 8
 * 9 4 7 3
 * 3 8 6 5
 * ```
 *
 * - In the first row, the only two numbers that evenly divide are 8 and 2; the result of this division is 4.
 * - In the second row, the two numbers are 9 and 3; the result is 3.
 * - In the third row, the result is 2.
 *
 * In this example, the sum of the results would be 4 + 3 + 2 = 9.
 *
 * @param input The [Collection] that contains the rows from the spreadsheet.
 */
@SuppressFBWarnings
class CorruptionChecksum(input: Collection<String>) {
  private val rows: List<List<Int>> = input.map {
    row -> row.split("\\s+".toRegex()).map { it.toInt() }
  }

  /**
   * Calculates and returns the checksum for the loaded spreadsheet rows.
   *
   * @param evenlyDivisible If true, then only the two numbers in each row that evenly divide one
   * another are used in the checksum calculation.
   * @return The checksum for the loaded spreadsheet rows.
   */
  fun checksum(evenlyDivisible: Boolean = false): Int {
    return if (evenlyDivisible) {
      evenChecksum()
    } else {
      standardChecksum()
    }
  }

  private fun evenChecksum(): Int {
    var sum = 0

    rows.forEach { row ->
      sum += evenChecksumRow(row.sorted())
    }

    return sum
  }

  private fun evenChecksumRow(row: List<Int>): Int {
    for (i in row.indices) {
      for (j in row.size - 1 downTo i) {
        if (i == j || row[i] == 0 || row[j] < row[i]) {
          continue
        }

        if (row[j] % row[i] == 0) {
          return row[j] / row[i]
        }
      }
    }
    return 0
  }

  private fun standardChecksum(): Int {
    var sum = 0

    rows.forEach { row ->
      var min = Int.MAX_VALUE
      var max = Int.MIN_VALUE

      row.forEach { item ->
        if (item < min) {
          min = item
        }
        if (item > max) {
          max = item
        }
      }

      sum += (max - min)
    }

    return sum
  }
}
