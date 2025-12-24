package com.capital7software.aoc.lib.analysis

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * The ancient civilization on [Pluto](https://adventofcode.com/2019/day/20) was known for
 * its ability to manipulate spacetime, and while The Historians explore their infinite
 * corridors, you've noticed a strange set of physics-defying stones.
 *
 * At first glance, they seem like normal stones: they're arranged in a perfectly **straight
 * line**, and each stone has a **number** engraved on it.
 *
 * The strange part is that every time you blink, the stones **change**.
 *
 * Sometimes, the number engraved on a stone changes. Other times, a stone might **split in
 * two, causing all the other stones to shift over a bit to make room in their perfectly
 * straight line.
 *
 * As you observe them for a while, you find that the stones have a consistent behavior.
 * Every time you blink, the stones each **simultaneously** change according to the
 * **first applicable rule** in this list:
 *
 * - If the stone is engraved with the number ```0```, it is replaced by a stone engraved
 * with the number ```1```.
 * - If the stone is engraved with a number that has an **even** number of digits, it is
 * replaced by **two stones**. The left half of the digits are engraved on the new left
 * stone, and the right half of the digits are engraved on the new right stone. (The
 * new numbers don't keep extra leading zeroes: ```1000``` would become stones ```10```
 * and ```0```.)
 * - If none of the other rules apply, the stone is replaced by a new stone; the old
 * stone's number ***multiplied by 2024** is engraved on the new stone.
 *
 * No matter how the stones change, their ```order is preserved```, and they stay on
 * their perfectly straight line.
 *
 * @param input The [String] that contains the initial arrangement of stones.
 */
@SuppressFBWarnings
class PlutonianPebbles(input: String) {
  private companion object {
    private const val MAX_SLOW_BLINKS = 30
  }

  private val initialStones: List<Stone> by lazy {
    input
        .split(" ")
        .map { Stone(it, it.toLong()) }
  }

  /**
   * A stone engraved with a [label] that represents a numeric [value].
   *
   * @property label The [String] representation of the numeric [value].
   * @property value The numeric value of this stone.
   */
  data class Stone(val label: String, val value: Long)

  /**
   * Sometimes, the number engraved on a stone changes. Other times, a stone might **split in
   * two, causing all the other stones to shift over a bit to make room in their perfectly
   * straight line.
   *
   * As you observe them for a while, you find that the stones have a consistent behavior.
   * Every time you blink, the stones each **simultaneously** change according to the
   * **first applicable rule** in this list:
   *
   * - If the stone is engraved with the number ```0```, it is replaced by a stone engraved
   * with the number ```1```.
   * - If the stone is engraved with a number that has an **even** number of digits, it is
   * replaced by **two stones**. The left half of the digits are engraved on the new left
   * stone, and the right half of the digits are engraved on the new right stone. (The
   * new numbers don't keep extra leading zeroes: ```1000``` would become stones ```10```
   * and ```0```.)
   * - If none of the other rules apply, the stone is replaced by a new stone; the old
   * stone's number ***multiplied by 2024** is engraved on the new stone.
   *
   * No matter how the stones change, their ```order is preserved```, and they stay on
   * their perfectly straight line.
   *
   * How will the stones evolve if you keep blinking at them? You take a note of the number
   * engraved on each stone in the line (your puzzle input).
   *
   * If you have an arrangement of five stones engraved with the numbers
   * ```0 1 10 99 999``` and you blink once, the stones transform as follows:
   *
   * - The first stone, ```0```, becomes a stone marked ```1```.
   * - The second stone, ```1```, is multiplied by 2024 to become ```2024```.
   * - The third stone, ```10```, is split into a stone marked ```1``` followed by a stone
   * marked ```0```.
   * - The fourth stone, ```99```, is split into two stones marked ```9```.
   * - The fifth stone, ```999```, is replaced by a stone marked ```2021976```.
   *
   * So, after blinking once, your five stones would become an arrangement of seven stones
   * engraved with the numbers ```1 2024 1 0 9 9 2021976```.
   *
   * Here is a longer example:
   *
   * ```
   * Initial arrangement:
   * 125 17
   *
   * After 1 blink:
   * 253000 1 7
   *
   * After 2 blinks:
   * 253 0 2024 14168
   *
   * After 3 blinks:
   * 512072 1 20 24 28676032
   *
   * After 4 blinks:
   * 512 72 2024 2 0 2 4 2867 6032
   *
   * After 5 blinks:
   * 1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32
   *
   * After 6 blinks:
   * 2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2
   * ```
   *
   * In this example, after blinking six times, you would have ```22``` stones.
   * After blinking ```25``` times, you would have **```55312```** stones!
   *
   * @param times The number of times to blink.
   * @return The [List] of [Stone]s after the specified number of blinks.
   */
  fun blink(times: Int): List<Stone> {
    check(times in 0..MAX_SLOW_BLINKS) { "Use fastBlink if $times is greater than 30."}

    var result = initialStones.toList()

    IntRange(1, times).forEach { _ ->
      result = evolve(result)
    }

    return result
  }

  /**
   * Calculates and returns a Map that represents the unique stone engravings (the keys) and the
   * number of occurrences of each engraved stone (the values) versus [blink], which returns an
   * ordered list of every [Stone].
   *
   * @param times The number of times to blink.
   * @return The [Map] of [Stone]s to occurrences after the specified number of blinks.
   */
  fun fastBlink(times: Int): Map<Stone, Long> {
    var result = hashMapOf<Stone, Long>()

    initialStones.forEach { stone ->
      result[stone] = result.getOrDefault(stone, 0L) + 1L
    }

    IntRange(1, times).forEach { _ ->
      result = fastEvolve(result)
    }

    return result
  }

  private fun fastEvolve(current: HashMap<Stone, Long>): HashMap<Stone, Long> {
    val result = hashMapOf<Stone, Long>()

    for ((stone, count) in current) {
      if (stone.value == 0L) {
        val s = (Stone("1", 1L))
        result[s] = result.getOrDefault(s, 0L) + count
      } else if (stone.label.length % 2 == 0) {
        var left = stone.label.substring(0, stone.label.length / 2)
        val leftLong = left.toLong()
        left = leftLong.toString()
        val leftStone = Stone(left, leftLong)
        result[leftStone] = result.getOrDefault(leftStone, 0L) + count
        var right = stone.label.substring(stone.label.length / 2)
        val rightLong = right.toLong()
        right = rightLong.toString()
        val rightStone = Stone(right, rightLong)
        result[rightStone] = result.getOrDefault(rightStone, 0L) + count
      } else {
        val newLong = stone.value * 2024L
        val s = Stone(newLong.toString(), newLong)
        result[s] = result.getOrDefault(s, 0L) + count
      }
    }
    return result
  }

  private fun evolve(current: List<Stone>): List<Stone> {
    val result = mutableListOf<Stone>()

    for (stone in current) {
      if (stone.value == 0L) {
        result.add(Stone("1", 1))
      } else if (stone.label.length % 2 == 0) {
        var left = stone.label.substring(0, stone.label.length / 2)
        val leftLong = left.toLong()
        left = leftLong.toString()
        result.add(Stone(left, leftLong))
        var right = stone.label.substring(stone.label.length / 2)
        val rightLong = right.toLong()
        right = rightLong.toString()
        result.add(Stone(right, rightLong))
      } else {
        val newLong = stone.value * 2024
        result.add(Stone(newLong.toString(), newLong))
      }
    }
    return result
  }
}
