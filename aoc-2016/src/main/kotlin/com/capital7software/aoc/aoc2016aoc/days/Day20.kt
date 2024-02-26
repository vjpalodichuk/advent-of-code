package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.analysis.FirewallRules
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * --- Day 20: Firewall Rules ---
 *
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
 * Given the list of blocked IPs you retrieved from the firewall (your puzzle input),
 * **what is the lowest-valued IP** that is not blocked?
 *
 * Your puzzle answer was 31053880.
 *
 * --- Part Two ---
 *
 * **How many IPs** are allowed by the blacklist?
 *
 * Your puzzle answer was 117.
 */
class Day20 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day20::class.java)
  }

  override fun getDefaultInputFilename(): String {
    return "inputs/input_day_20-01.txt"
  }

  override fun runPart1(input: List<String>) {
    val start = Instant.now()
    val answer = findLowestIpAvailable(input)
    val end = Instant.now()

    log.info("$answer is the lowest available IP Address!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: List<String>) {
    val start = Instant.now()
    val answer = availableIps(input, UInt.MAX_VALUE.toLong())
    val end = Instant.now()

    log.info("$answer is the number of available IP Addresses!")
    logTimings(log, start, end)
  }

  /**
   * Calculates and returns the lowest available IP Address.
   *
   * @param input The list of Blocked IP Address Ranges to parse.
   * @return The lowest available IP Address.
   */
  @SuppressFBWarnings
  fun findLowestIpAvailable(input: List<String>): Long {
    val instance = FirewallRules(input)
    return instance.lowestIpAvailable()
  }

  /**
   * Calculates and returns the count of available IP Addresses.
   *
   * @param input The list of Blocked IP Address Ranges to parse.
   * @param maxIp The maximum IP Address to consider.
   * @return The total number of available IP Addresses.
   */
  @SuppressFBWarnings
  fun availableIps(input: List<String>, maxIp: Long): Long {
    val instance = FirewallRules(input)
    return instance.availableIps(maxIp)
  }

}
