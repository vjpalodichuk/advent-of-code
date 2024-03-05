package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.computer.LimitedComputer
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 8: I Heard You Like Registers ---**
 *
 * You receive a signal directly from the CPU. Because of your recent assistance
 * with jump instructions, it would like you to compute the result of a series of
 * unusual register instructions.
 *
 * Each instruction consists of several parts: the register to modify, whether
 * to increase or decrease that register's value, the amount by which to increase
 * or decrease it, and a condition. If the condition fails, skip the instruction
 * without modifying the register. The registers all start at 0. The instructions
 * look like this:
 *
 * ```
 * b inc 5 if a > 1
 * a inc 1 if b < 5
 * c dec -10 if a >= 1
 * c inc -20 if c == 10
 * ```
 *
 * These instructions would be processed as follows:
 *
 * - Because a starts at 0, it is not greater than 1, and so b is not modified.
 * - a is increased by 1 (to 1) because b is less than 5 (it is 0).
 * - c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1).
 * - c is increased by -20 (to -10) because c is equal to 10.
 *
 * After this process, the largest value in any register is 1.
 *
 * You might also encounter <= (less than or equal to) or != (not equal to). However,
 * the CPU doesn't have the bandwidth to tell you what all the registers are named, and
 * leaves that to you to determine.
 *
 * **What is the largest value in any register** after completing the instructions in your
 * puzzle input?
 *
 * Your puzzle answer was **6343**.
 *
 * **--- Part Two ---**
 *
 * To be safe, the CPU also needs to know the **highest value held in any register
 * during this process** so that it can decide how much memory to allocate to these
 * operations. For example, in the above instructions, the highest value ever held
 * was 10 (in register c after the third instruction was evaluated).
 *
 * Your puzzle answer was **7184**.
 */
class Day08 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day08::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_08-01.txt"

  override fun runPart1(input: MutableList<String>) {
    val start = Instant.now()
    val answer = largestValueInAnyRegister(input)
    val end = Instant.now()

    log.info("$answer is the largest value held by any register!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: MutableList<String>) {
    val start = Instant.now()
    val answer = largestValueInAnyRegisterDuringExecution(input)
    val end = Instant.now()

    log.info("$answer is the largest value held by any register during execution!")
    logTimings(log, start, end)
  }

  /**
   * Returns the maximum value held by any register after running the
   * specified program.
   *
   * @param input The [List] of [String] to parse into instructions to execute.
   * @return The maximum value held by any register after running the
   * specified program.
   */
  fun largestValueInAnyRegister(input: List<String>): Int {
    val instance = LimitedComputer(input)
    instance.run()
    return instance.max()?.second ?: -1
  }

  /**
   * Returns the maximum value held by any register after running the
   * specified program.
   *
   * @param input The [List] of [String] to parse into instructions to execute.
   * @return The maximum value held by any register after running the
   * specified program.
   */
  fun largestValueInAnyRegisterDuringExecution(input: List<String>): Int {
    val instance = LimitedComputer(input)
    instance.run()
    return instance.maxDuringExecution()?.second ?: -1
  }
}
