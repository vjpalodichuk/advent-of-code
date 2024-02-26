package com.capital7software.aoc.lib.analysis

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * You'd like to set up a small hidden computer here, so you can use it to get back into
 * the network later. However, the corporate firewall only allows communication with
 * certain external IP addresses.
 *
 * You've retrieved the list of blocked IPs from the firewall, but the list seems to be
 * messy and poorly maintained, and it's not clear which IPs are allowed. Also, rather
 * than being written in dot-decimal notation, they are written as plain 32-bit integers,
 * which can have any value from 0 through 4294967295, inclusive.
 *
 * For example, suppose only the values 0 through 9 were valid, and that you retrieved
 * the following blacklist:
 *
 * ```
 * 5-8
 * 0-2
 * 4-7
 * ```
 * The blacklist specifies ranges of IPs (inclusive of both the start and end value)
 * that are **not** allowed. Then, the only IPs that this firewall allows are 3 and 9, since
 * those are the only numbers not in any range.
 *
 */
class FirewallRules() {
  private val ranges: MutableList<LongRange> = mutableListOf()

  /**
   * Instantiates a new [FirewallRules] instance with the blocked ranges of IPs specified in
   * the [List] of [String].
   */
  @SuppressFBWarnings
  constructor(input: List<String>) : this() {
    ranges.addAll(
        input.map {
          val split = it.split("-")
          LongRange(split[0].toLong(), split[1].toLong())
        }.sortedBy { it.first }
    )
  }

  /**
   * Returns the lowest IP Address that isn't blocked by the firewall rules.
   *
   * @return The lowest IP Address that isn't blocked by the firewall rules.
   */
  fun lowestIpAvailable(): Long {
    var lowest = Long.MAX_VALUE
    var blockRange = LongRange(ranges[0].first, ranges[0].last)

    if (blockRange.first > 0) {
      lowest = blockRange.first
    } else {
      for (i in 1 ..< ranges.size) {
        val range = ranges[i]
        if (!blockRange.contains(range.first) && blockRange.last + 1 < range.first) {
          lowest = blockRange.last + 1
          break
        } else if (!blockRange.contains(range.last)) {
          blockRange = LongRange(blockRange.first, range.last)
        }
      }
      if (lowest == Long.MAX_VALUE && !ranges.last().contains(UInt.MAX_VALUE.toLong())) {
        lowest = ranges.last().last + 1
      }
    }
    return lowest
  }

  /**
   * Returns the lowest IP Address that isn't blocked by the firewall rules.
   *
   * @return The lowest IP Address that isn't blocked by the firewall rules.
   */
  fun availableIps(maxIp: Long): Long {
    val allowed = mutableListOf<Long>()
    var blockRange = LongRange(ranges[0].first, ranges[0].last)

    if (blockRange.first > 0) {
      for (i in 0 ..< blockRange.first) {
        allowed.add(i)
      }
    }

    for (i in 1 ..< ranges.size) {
      val range = ranges[i]

      if (!blockRange.contains(range.first) && blockRange.last + 1 < range.first) {
        // Add the allowed IPs
        for (j in (blockRange.last + 1) ..< range.first) {
          allowed.add(j)
        }
        // Start a new block range
        blockRange = range
      } else if (!blockRange.contains(range.last)) {
        // Extend the current block range
        blockRange = LongRange(blockRange.first, range.last)
      }
    }

    val maxBlocked = ranges.maxBy { it.last }

    // Catch any allowed IPs after the last block range.
    for (k in (maxBlocked.last + 1) .. maxIp) {
      allowed.add(k)
    }

    return allowed.size.toLong()
  }
}
