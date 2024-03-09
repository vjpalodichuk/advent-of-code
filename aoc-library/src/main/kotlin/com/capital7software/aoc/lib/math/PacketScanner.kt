package com.capital7software.aoc.lib.math

import com.capital7software.aoc.lib.string.clean
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * **--- Day 13: Packet Scanners ---**
 *
 * You need to cross a vast **firewall**. The firewall consists of several layers, each with
 * a **security scanner** that moves back and forth across the layer. To succeed, you must
 * not be detected by a scanner.
 *
 * By studying the firewall briefly, you are able to record (in your puzzle input) the **depth**
 * of each layer and the **range** of the scanning area for the scanner within it, written as
 * depth: range. Each layer has a thickness of exactly 1. A layer at depth 0 begins immediately
 * inside the firewall; a layer at depth 1 would start immediately after that.
 *
 * For example, suppose you've recorded the following:
 *
 * ```
 * 0: 3
 * 1: 2
 * 4: 4
 * 6: 4
 * ```
 *
 * This means that there is a layer immediately inside the firewall (with range 3), a second
 * layer immediately after that (with range 2), a third layer which begins at depth 4
 * (with range 4), and a fourth layer which begins at depth 6 (also with range 4).
 * Visually, it might look like this:
 *
 * ```
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... [ ] ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 * ```
 * Within each layer, a security scanner moves back and forth within its range. Each security
 * scanner starts at the top and moves down until it reaches the bottom, then moves up until
 * it reaches the top, and repeats. A security scanner takes **one picosecond** to move one step.
 * Drawing scanners as S, the first few picoseconds look like this:
 *
 * ```
 * Picosecond 0:
 *  0   1   2   3   4   5   6
 * [S] [S] ... ... [S] ... [S]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 * Picosecond 1:
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... [ ] ... [ ]
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 * Picosecond 2:
 *  0   1   2   3   4   5   6
 * [ ] [S] ... ... [ ] ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [S]             [S]     [S]
 *                 [ ]     [ ]
 *
 * Picosecond 3:
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... [ ] ... [ ]
 * [S] [S]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [S]     [S]
 * ```
 *
 * Your plan is to hitch a ride on a packet about to move through the firewall. The packet will
 * travel along the top of each layer, and it moves at **one layer per picosecond**. Each
 * picosecond, the packet moves one layer forward (its first move takes it into layer 0),
 * and then the scanners move one step. If there is a scanner at the top of the layer
 * **as your packet enters it**, you are **caught**. (If a scanner moves into the top of its layer
 * while you are there, you are **not** caught: it doesn't have time to notice you before
 * you leave.) If you were to do this in the configuration above, marking your current position
 * with parentheses, your passage through the firewall would look like this:
 *
 * ```
 * Initial state:
 *  0   1   2   3   4   5   6
 * [S] [S] ... ... [S] ... [S]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 * Picosecond 0:
 *  0   1   2   3   4   5   6
 * (S) [S] ... ... [S] ... [S]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * ( ) [ ] ... ... [ ] ... [ ]
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 1:
 *  0   1   2   3   4   5   6
 * [ ] ( ) ... ... [ ] ... [ ]
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [ ] (S) ... ... [ ] ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [S]             [S]     [S]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 2:
 *  0   1   2   3   4   5   6
 * [ ] [S] (.) ... [ ] ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [S]             [S]     [S]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [ ] [ ] (.) ... [ ] ... [ ]
 * [S] [S]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [S]     [S]
 *
 *
 * Picosecond 3:
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... (.) [ ] ... [ ]
 * [S] [S]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [S]     [S]
 *
 *  0   1   2   3   4   5   6
 * [S] [S] ... (.) [ ] ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [S]     [S]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 4:
 *  0   1   2   3   4   5   6
 * [S] [S] ... ... ( ) ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [S]     [S]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... ( ) ... [ ]
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 5:
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... [ ] (.) [ ]
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [ ] [S] ... ... [S] (.) [S]
 * [ ] [ ]         [ ]     [ ]
 * [S]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 6:
 *  0   1   2   3   4   5   6
 * [ ] [S] ... ... [S] ... (S)
 * [ ] [ ]         [ ]     [ ]
 * [S]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... [ ] ... ( )
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 * ```
 *
 * In this situation, you are **caught** in layers 0 and 6, because your packet entered the
 * layer when its scanner was at the top when you entered it. You are **not** caught in layer 1,
 * since the scanner moved into the top of the layer once you were already there.
 *
 * The **severity** of getting caught on a layer is equal to its **depth** multiplied by its
 * **range**. (Ignore layers in which you do not get caught.) The severity of the whole trip
 * is the sum of these values. In the example above, the trip severity is 0 * 3 + 6 * 4 = **24**.
 *
 * Now, you need to pass through the firewall without being caught - easier said than done.
 *
 * You can't control the speed of the packet, but you can delay it any number of picoseconds.
 * For each picosecond you delay the packet before beginning your trip, all security scanners
 * move one step. You're not in the firewall during this time; you don't enter layer 0 until
 * you stop delaying the packet.
 *
 * In the example above, if you delay 10 picoseconds (picoseconds 0 - 9), you won't get caught:
 *
 * ```
 * State after delaying:
 *  0   1   2   3   4   5   6
 * [ ] [S] ... ... [ ] ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [S]             [S]     [S]
 *                 [ ]     [ ]
 *
 * Picosecond 10:
 *  0   1   2   3   4   5   6
 * ( ) [S] ... ... [ ] ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [S]             [S]     [S]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * ( ) [ ] ... ... [ ] ... [ ]
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 11:
 *  0   1   2   3   4   5   6
 * [ ] ( ) ... ... [ ] ... [ ]
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [S] (S) ... ... [S] ... [S]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 12:
 *  0   1   2   3   4   5   6
 * [S] [S] (.) ... [S] ... [S]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [ ] [ ] (.) ... [ ] ... [ ]
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 13:
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... (.) [ ] ... [ ]
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [ ] [S] ... (.) [ ] ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [S]             [S]     [S]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 14:
 *  0   1   2   3   4   5   6
 * [ ] [S] ... ... ( ) ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [S]             [S]     [S]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... ( ) ... [ ]
 * [S] [S]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [S]     [S]
 *
 *
 * Picosecond 15:
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... [ ] (.) [ ]
 * [S] [S]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [S]     [S]
 *
 *  0   1   2   3   4   5   6
 * [S] [S] ... ... [ ] (.) [ ]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [S]     [S]
 *                 [ ]     [ ]
 *
 *
 * Picosecond 16:
 *  0   1   2   3   4   5   6
 * [S] [S] ... ... [ ] ... ( )
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [S]     [S]
 *                 [ ]     [ ]
 *
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... [ ] ... ( )
 * [S] [S]         [S]     [S]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 * ```
 *
 * Because all smaller delays would get you caught, the fewest number of picoseconds you
 * would need to delay to get through safely is 10.
 *
 * @param input The [List] of [String] that describes the depth and range of each layer.
 * @param catchFree If true, then a trip through the firewall will be delayed to ensure not
 * getting caught by a scanner.
 */
class PacketScanner(input: List<String>, private val catchFree: Boolean = false) {
  private companion object {
    private const val MAX_ITERATIONS: Int = Int.MAX_VALUE
  }

  /**
   * A layer in the firewall. It has a [depth] which determines its position within the firewall.
   * Depths start at 0. It also has a [range] that determines how far a scanner can go forward
   * before it starts scanning backwards. Scanners are always moving either forwards or backwards
   * and take one step every pico-second.
   *
   * All scanners are in position 0 at time 0.
   *
   * @param depth The depth of this layer within the firewall.
   * @param range The range of the security scanner. The position of a scanner is 0-based and so
   * the indexes for positions would be 0 through range - 1 or 0 <= i < range.
   */
  data class Layer(val depth: Int, val range: Int) {
    /**
     * To support the scanner's back-and-forth movement, we minus 1 from the range
     * when calculating the scanner's position at a particular point in time.
     */
    private val cycle: Int = checkCycle(range)

    /**
     * The severity of getting caught by a scanner in this layer!
     */
    val severity: Int = depth * range

    /**
     * This is used to find a time t that allows packet to travel through the firewall without
     * getting caught by a scanner.
     *
     * To ensure the packet is not caught, then find a t for all layers such that:
     *
     * (time t + depth) modulo divisor not equal zero.
     *
     */
    val divisor: Int by lazy { 2 * cycle }

    private fun checkCycle(length: Int): Int {
      check(length > 1) { "The cycle will not work with $length" }
      return length - 1
    }

    /**
     * Returns the 0-based position of the scanner for this layer at the specified time.
     *
     * The cycle value is range - 1. The quotient of time / cycle determines if the scanner
     * is moving forwards or backwards. If the quotient is odd, then it is moving backwards;
     * otherwise it is moving forwards.
     *
     * If it is moving forwards, then the calculation is simply time % cycle; otherwise
     * it is cycle - (time % cycle) to get the scanner's position moving backwards.
     *
     * @param time The amount of time that has transpired.
     * @return The 0-based position of the scanner for this layer at the specified time.
     */
    fun scannerAt(time: Int): Int = if (((time / cycle) and 1) == 1) {
      cycle - (time % cycle)
    } else {
      time % cycle
    }
  }

  private val firewall: MutableMap<Int, Layer> by lazy { inputToFirewall(input) }

  @SuppressFBWarnings
  private fun inputToFirewall(input: List<String>): MutableMap<Int, Layer> {
    return input
        .map { line ->
          val split = line.split(": ")
          Layer(split[0].clean().toInt(), split[1].clean().toInt())
        }
        .associateBy { it.depth }
        .toMutableMap()
  }

  /**
   * The total severity of taking a trip through the firewall and the delay used for the trip.
   *
   * The first element of the [Pair] is the delay used when traveling through the firewall. The
   * second element is the severity of the trip. The best trip has a severity of 0.
   */
  val severity: Pair<Int, Int> by lazy { takeTrip() }

  private fun takeTrip(): Pair<Int, Int> {
    var sum = 0
    val delay = if (catchFree) {
      calculateDelay()
    } else {
      0
    }

    for (i in 0..firewall.keys.max()) {
      if (firewall.containsKey(i)) {
        val layer = firewall[i] ?: error("Missing firewall layer for ID $i")

        if (layer.scannerAt(delay + i) == 0) {
          // We are caught!
          sum += layer.severity
        }
      }
    }

    return Pair(delay, sum)
  }

  private fun calculateDelay(): Int {
    val layers = firewall.values

    for (i in 0..MAX_ITERATIONS) {
      if (layers.all { (it.depth + i) % it.divisor != 0 }) {
        return i
      }
    }
    return -1
  }
}
