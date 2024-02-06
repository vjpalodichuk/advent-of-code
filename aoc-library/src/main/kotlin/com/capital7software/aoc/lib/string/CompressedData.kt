package com.capital7software.aoc.lib.string

import com.capital7software.aoc.lib.util.Pair
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * Wandering around a secure area, you come across a datalink port to a new part of the network.
 * After briefly scanning it for interesting files, you find one file in particular that catches
 * your attention. It's compressed with an experimental format, but fortunately, the documentation
 * for the format is nearby.
 *
 * The format compresses a sequence of characters. Whitespace is ignored. To indicate that some
 * sequence should be repeated, a marker is added to the file, like (10x2). To decompress this
 * marker, take the subsequent 10 characters and repeat them 2 times. Then, continue reading
 * the file after the repeated data. The marker itself is not included in the
 * decompressed output.
 *
 * If parentheses or other characters appear within the data referenced by a marker,
 * that's okay - treat it like normal data, not a marker, and then resume looking for markers
 * after the decompressed section.
 *
 * For example:
 *
 * - `ADVENT` contains no markers and decompresses to itself with no changes,
 * resulting in a decompressed length of 6.
 *
 * - `A(1x5)BC` repeats only the B a total of 5 times, becoming ABBBBBC for a
 * decompressed length of 7.
 *
 * - `(3x3)XYZ` becomes XYZXYZXYZ for a decompressed length of 9.
 *
 * - `A(2x2)BCD(2x2)EFG` doubles the BC and EF, becoming ABCBCDEFEFG for a
 * decompressed length of 11.
 *
 * - `(6x1)(1x3)A` simply becomes (1x3)A - the (1x3) looks like a marker, but because it's
 * within a data section of another marker, it is not treated any differently from the A
 * that comes after it. It has a decompressed length of 6.
 *
 * - `X(8x2)(3x3)ABCY` becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18),
 * because the decompressed data from the (8x2) marker (the (3x3)ABC) is skipped and
 * not processed further.
 *
 * What is the **decompressed length** of the file (your puzzle input)? Don't count whitespace.
 *
 * Apparently, the file actually uses **version two** of the format.
 *
 * In version two, the only difference is that markers within decompressed data
 * are decompressed. This, the documentation explains, provides much more substantial
 * compression capabilities, allowing many-gigabyte files to be stored in
 * only a few kilobytes.
 *
 * For example:
 *
 * - `(3x3)XYZ` still becomes XYZXYZXYZ, as the decompressed section contains no markers.
 * - `X(8x2)(3x3)ABCY` becomes XABCABCABCABCABCABCY, because the decompressed data from
 * the (8x2) marker is then further decompressed, thus triggering the (3x3) marker
 * twice for a total of six ABC sequences.
 * - `(27x12)(20x12)(13x14)(7x10)(1x12)A` decompresses into a string of A
 * repeated 241920 times.
 * - `(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN` becomes 445
 * characters long.
 *
 * Unfortunately, the computer you brought probably doesn't have enough memory
 * to actually decompress the file; you'll have to **come up with another way** to
 * get its decompressed length.
 *
 * What is the **decompressed length** of the file using this improved format?
 */
object CompressedData {
  /**
   * The regular expression to simply match a marker.
   */
  private val MARKER_MATCH_REGEX: Regex = """\(\d+x\d+\)""".toRegex()

  /**
   * The regular expression with capturing groups to extract a marker.
   */
  private val MARKER_EXTRACT_REGEX: Regex = """\((?<length>\d+)x(?<times>\d+)\)""".toRegex()

  /**
   * The number of match groups that are required to decompress the section.
   */
  private const val REQUIRED_MATCH_GROUPS: Int = 3

  private const val GROUP_LENGTH: String = "length"
  private const val GROUP_TIMES: String = "times"

  private fun extractMatchValues(matchResult: MatchResult): Pair<Int, Int> {
    val length = matchResult.groups[GROUP_LENGTH]?.value?.toInt() ?: 0
    val times = matchResult.groups[GROUP_TIMES]?.value?.toInt() ?: 0

    return Pair(length, times)
  }

  /**
   * The format compresses a sequence of characters. Whitespace is ignored. To indicate that some
   * sequence should be repeated, a marker is added to the file, like (10x2). To decompress this
   * marker, take the subsequent 10 characters and repeat them 2 times. Then, continue reading
   * the file after the repeated data. The marker itself is not included in the
   * decompressed output.
   *
   * If parentheses or other characters appear within the data referenced by a marker,
   * that's okay - treat it like normal data, not a marker, and then resume looking for markers
   * after the decompressed section.
   *
   * For example:
   *
   * - `ADVENT` contains no markers and decompresses to itself with no changes,
   * resulting in a decompressed length of 6.
   *
   * - `A(1x5)BC` repeats only the B a total of 5 times, becoming ABBBBBC for a
   * decompressed length of 7.
   *
   * - `(3x3)XYZ` becomes XYZXYZXYZ for a decompressed length of 9.
   *
   * - `A(2x2)BCD(2x2)EFG` doubles the BC and EF, becoming ABCBCDEFEFG for a
   * decompressed length of 11.
   *
   * - `(6x1)(1x3)A` simply becomes (1x3)A - the (1x3) looks like a marker, but because it's
   * within a data section of another marker, it is not treated any differently from the A
   * that comes after it. It has a decompressed length of 6.
   *
   * - `X(8x2)(3x3)ABCY` becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18),
   * because the decompressed data from the (8x2) marker (the (3x3)ABC) is skipped and
   * not processed further.
   *
   * If [repeat] is true then the repeated data is also decompressed. Please note that for
   * really large and heavily compressed files an [OutOfMemoryError] may be thrown. If that
   * happens, simply use the length function to get the length of the decompressed file.
   *
   * For example:
   *
   * - `(3x3)XYZ` still becomes XYZXYZXYZ, as the decompressed section contains no markers.
   * - `X(8x2)(3x3)ABCY` becomes XABCABCABCABCABCABCY, because the decompressed data from
   * the (8x2) marker is then further decompressed, thus triggering the (3x3) marker
   * twice for a total of six ABC sequences.
   * - `(27x12)(20x12)(13x14)(7x10)(1x12)A` decompresses into a string of A
   * repeated 241920 times.
   * - `(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN` becomes 445
   * characters long.
   *
   * @param input The [String] to decompress.
   * @param repeat If true, the repeated data is decompressed as well.
   * @return A new [String] that has been decompressed.
   */
  fun decompress(input: String, repeat: Boolean = false): String {
    if (!MARKER_MATCH_REGEX.containsMatchIn(input)) {
      return input
    }

    var currentSub = input
    val builder = StringBuilder(input.length)

    do {
      val matchResult = MARKER_EXTRACT_REGEX.find(currentSub)

      // If no match, then simply add the remaining string to the result
      if (matchResult == null || matchResult.groups.size != REQUIRED_MATCH_GROUPS) {
        builder.append(currentSub)
        currentSub = ""
        continue
      }

      if (matchResult.range.first > 0) {
        val sub = currentSub.substring(0, matchResult.range.first)
        builder.append(sub)
      }

      val (length, times) = extractMatchValues(matchResult)

      val repeatString = currentSub.substring(
          matchResult.range.last + 1,
          matchResult.range.last + 1 + length
      )
      val repeated = repeatString.repeat(times)

      if (repeat) {
        builder.append(decompress(repeated, repeat))
      } else {
        builder.append(repeated)
      }

      currentSub = currentSub.substring(matchResult.range.last + 1 + length)
    } while (currentSub.isNotBlank())

    return builder.toString()
  }

  /**
   * The format compresses a sequence of characters. Whitespace is ignored. To indicate that some
   * sequence should be repeated, a marker is added to the file, like (10x2). To decompress this
   * marker, take the subsequent 10 characters and repeat them 2 times. Then, continue reading
   * the file after the repeated data. The marker itself is not included in the
   * decompressed output.
   *
   * If parentheses or other characters appear within the data referenced by a marker,
   * that's okay - treat it like normal data, not a marker, and then resume looking for markers
   * after the decompressed section.
   *
   * For example:
   *
   * - `ADVENT` contains no markers and decompresses to itself with no changes,
   * resulting in a decompressed length of 6.
   *
   * - `A(1x5)BC` repeats only the B a total of 5 times, becoming ABBBBBC for a
   * decompressed length of 7.
   *
   * - `(3x3)XYZ` becomes XYZXYZXYZ for a decompressed length of 9.
   *
   * - `A(2x2)BCD(2x2)EFG` doubles the BC and EF, becoming ABCBCDEFEFG for a
   * decompressed length of 11.
   *
   * - `(6x1)(1x3)A` simply becomes (1x3)A - the (1x3) looks like a marker, but because it's
   * within a data section of another marker, it is not treated any differently from the A
   * that comes after it. It has a decompressed length of 6.
   *
   * - `X(8x2)(3x3)ABCY` becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18),
   * because the decompressed data from the (8x2) marker (the (3x3)ABC) is skipped and
   * not processed further.
   *
   * If [repeat] is true then the repeated data is also decompressed. Please note that for
   * really large and heavily compressed files an [OutOfMemoryError] may be thrown. If that
   * happens, simply use the length function to get the length of the decompressed file.
   *
   * For example:
   *
   * - `(3x3)XYZ` still becomes XYZXYZXYZ, as the decompressed section contains no markers.
   * - `X(8x2)(3x3)ABCY` becomes XABCABCABCABCABCABCY, because the decompressed data from
   * the (8x2) marker is then further decompressed, thus triggering the (3x3) marker
   * twice for a total of six ABC sequences.
   * - `(27x12)(20x12)(13x14)(7x10)(1x12)A` decompresses into a string of A
   * repeated 241920 times.
   * - `(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN` becomes 445
   * characters long.
   *
   * @param input A [List] of [String] to decompress.
   * @param repeat If true, the repeated data is decompressed as well.
   * @return A new [List] of [String] that have been decompressed.
   */
  @SuppressFBWarnings
  fun decompress(input: List<String>, repeat: Boolean = false): List<String> {
    return input.map { decompress(it, repeat) }
  }

  /**
   * Returns the decompressed length of the specified compressed [String].
   *
   * @see decompress
   * @param input The [String] to get the decompressed length of.
   * @param repeat If true, the repeated data is decompressed as well.
   * @return The length of the decompressed file.
   */
  fun length(input: String, repeat: Boolean = false): Long {
    var currentSub = input
    var currentLength = 0L

    do {
      val matchResult = MARKER_EXTRACT_REGEX.find(currentSub)

      // If no match, then simply add the remaining string to the result
      if (matchResult == null || matchResult.groups.size != REQUIRED_MATCH_GROUPS) {
        currentLength += currentSub.length.toLong()

        currentSub = ""
        continue
      }

      currentLength += matchResult.range.first

      val (length, times) = extractMatchValues(matchResult)

      val startIndex = matchResult.range.last + 1

      val repeatString = currentSub.substring(startIndex, startIndex + length)

      currentLength += if (repeat) {
        times * length(repeatString, repeat)
      } else {
        times * repeatString.length.toLong()
      }

      currentSub = currentSub.substring(startIndex + length)
    } while (currentSub.isNotBlank())

    return currentLength
  }

  /**
   * Returns a [List] containing the decompressed lengths of the specified compressed
   * [List] of [String].
   *
   * @see decompress
   * @param input A [List] of [String] to get the decompressed length for.
   * @param repeat If true, the repeated data is decompressed as well.
   * @return The length of the decompressed file.
   */
  @SuppressFBWarnings
  fun length(input: List<String>, repeat: Boolean = false): List<Long> {
    return input.map { length(it, repeat) }
  }
}
