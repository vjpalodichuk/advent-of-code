package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.crypt.Md5Fun
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * --- Day 5: How About a Nice Game of Chess? ---
 * You are faced with a security door designed by Easter Bunny engineers that
 * seem to have acquired most of their security knowledge by watching hacking movies.
 *
 * The **eight-character password** for the door is generated one character at a time by
 * finding the MD5 hash of some Door ID (your puzzle input) and an increasing
 * integer index (starting with 0).
 *
 * A hash indicates the **next character** in the password if its hexadecimal representation
 * starts with **five zeroes**. If it does, the sixth character in the hash is the next
 * character of the password.
 *
 * For example, if the Door ID is `abc`:
 *
 * - The first index which produces a hash that starts with five zeroes is 3231929,
 * which we find by hashing abc3231929; the sixth character of the hash, and thus
 * the first character of the password, is 1.
 * - 5017308 produces the next interesting hash, which starts with 000008f82..., so
 * the second character of the password is 8.
 * - The third time a hash starts with five zeroes is for abc5278568, discovering
 * the character f.
 *
 * In this example, after continuing this search a total of eight times, the password is `18f47a30`.
 *
 * Given the actual Door ID, what is the password?
 *
 * Your puzzle answer was **801b56a7**.
 *
 * --- Part Two ---
 *
 * As the door slides open, you are presented with a second door that uses a slightly more
 * inspired security mechanism. Clearly unimpressed by the last version (in what movie is
 * the password decrypted **in order**?!), the Easter Bunny engineers have worked out
 * a better solution.
 *
 * Instead of simply filling in the password from left to right, the hash now also
 * indicates the **position** within the password to fill. You still look for hashes that
 * begin with five zeroes; however, now, the **sixth** character represents the **position** (0-7),
 * and the **seventh** character is the character to put in that position.
 *
 * A hash result of 000001f means that f is the **second** character in the password.
 * Use only the **first result** for each position, and ignore invalid positions.
 *
 * For example, if the Door ID is `abc`:
 *
 * - The first interesting hash is from abc3231929, which produces 0000015...; so,
 * 5 goes in position 1: _5______.
 *
 * - In the previous method, 5017308 produced an interesting hash; however, it is ignored,
 * because it specifies an invalid position (8).
 *
 * - The second interesting hash is at index 5357525, which produces 000004e...; so, e
 * goes in position 4: _5__e___.
 *
 * You almost choke on your popcorn as the final character falls into place, producing
 * the password `05ace8e3`.
 *
 * Given the actual Door ID and this new method, what is the password? Be extra proud
 * of your solution if it uses a cinematic "decrypting" animation.
 *
 * Your puzzle answer was 424a0197.
 */
class Day05 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day05::class.java)
  }

  override fun getDefaultInputFilename(): String {
    return "inputs/input_day_05-01.txt"
  }

  override fun runPart1(input: List<String>) {
    val start = Instant.now()
    val answer = generatePassword(input.first())
    val end = Instant.now()

    log.info("{} is the calculated password!", answer)
    logTimings(log, start, end)
  }

  override fun runPart2(input: List<String>) {
    val start = Instant.now()
    val answer = generateAdvancedPassword(input.first())
    val end = Instant.now()

    log.info("{} is the calculated advanced password!", answer)
    logTimings(log, start, end)
  }

  /**
   * Generates and returns a password for the specified string.
   *
   * @param input The secret [String] to generate a password for.
   * @param leadingZeros THe number of leading zeroes in the hash for it to be used.
   * @param length THe length of the password.
   * @return A password for the specified string.
   */
  fun generatePassword(input: String, leadingZeros: Int = 5, length: Int = 8): String {
    return Md5Fun.generatePassword(input, leadingZeros, 0, length)
  }

  /**
   * Generates and returns a password for the specified string using a more advanced algorithm.
   *
   * @param input The secret [String] to generate a password for.
   * @param leadingZeros THe number of leading zeroes in the hash for it to be used.
   * @param length THe length of the password which can be up to 16 characters in length.
   * @return A password for the specified string.
   */
  fun generateAdvancedPassword(input: String, leadingZeros: Int = 5, length: Int = 8): String {
    return Md5Fun.generateAdvancedPassword(input, leadingZeros, 0, length)
  }
}
