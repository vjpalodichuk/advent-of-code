package com.capital7software.aoc.lib.crypt

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * An object that contains function for working with signal processing.
 */
@SuppressFBWarnings
object SignalNoise {
  /**
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
   */
  fun errorCorrectSignal(input: List<String>, leastCommon: Boolean = false): String {
    val columnMap: MutableMap<Int, MutableMap<Char, Int>> = mutableMapOf()

    input.forEach { line ->
      line.toCharArray().withIndex().forEach {
        val map = columnMap.computeIfAbsent(it.index) { mutableMapOf() }
        map[it.value] = map.getOrDefault(it.value, 0) + 1
      }
    }

    val message = CharArray(columnMap.size)

    columnMap.forEach { (column, map) ->
      val target = if (leastCommon) {
        map.minByOrNull { it.value }
      } else {
        map.maxByOrNull { it.value }
      }
      if (target != null) {
        message[column] = target.key
      }
    }

    return String(message)
  }
}
