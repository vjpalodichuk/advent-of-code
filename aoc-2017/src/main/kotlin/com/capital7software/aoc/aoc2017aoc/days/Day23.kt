package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.computer.CoProcessor
import com.capital7software.aoc.lib.computer.Instruction
import com.capital7software.aoc.lib.computer.InstructionCounterDebugger
import com.capital7software.aoc.lib.computer.Multiply
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import kotlin.reflect.KClass
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 23: Coprocessor Conflagration ---**
 *
 * You decide to head directly to the CPU and fix the printer from there.
 * As you get close, you find an **experimental coprocessor** doing so much work that the
 * local programs are afraid it will halt and catch fire. This would cause serious issues
 * for the rest of the computer, so you head in and see what you can do.
 *
 * The code it's running seems to be a variant of the kind you saw recently on that tablet.
 * The general functionality seems **very similar**, but some of the instructions are different:
 *
 * - set X Y **sets** register X to the value of Y.
 * - sub X Y **decreases** register X by the value of Y.
 * - mul X Y sets register X to the result of **multiplying** the value contained in
 * register X by the value of Y.
 * - jnz X Y **jumps** with an offset of the value of Y, but only if the value of X
 * is **not zero**. (An offset of 2 skips the next instruction, an offset of -1 jumps
 * to the previous instruction, and so on.)
 *
 * Only the instructions listed above are used. The eight registers here,
 * named a through h, all start at 0.
 *
 * The coprocessor is currently set to some kind of **debug mode**, which allows for testing,
 * but prevents it from doing any meaningful work.
 *
 * If you run the program (your puzzle input), **how many times is the mul instruction invoked?**
 *
 * Your puzzle answer was **5929**.
 *
 * **--- Part Two ---**
 *
 * Now, it's time to fix the problem.
 *
 * The **debug mode switch** is wired directly to register a. You flip the switch, which
 * makes **register a now start at 1** when the program is executed.
 *
 * Immediately, the coprocessor begins to overheat. Whoever wrote this program obviously
 * didn't choose a very efficient implementation. You'll need to **optimize the program** if
 * it has any hope of completing before Santa needs that printer working.
 *
 * The coprocessor's ultimate goal is to determine the final value left in register h
 * once the program completes. Technically, if it had that... it wouldn't even need
 * to run the program.
 *
 * After setting register a to 1, if the program were to run to completion, **what value
 * would be left in register h?**
 *
 * Your puzzle answer was **907**.
 */
class Day23 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day23::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_23-01.txt"

  override fun runPart1(input: MutableList<String>) {
    val kClass = Multiply::class
    val start = Instant.now()
    val answer = instructionCount(input, kClass)
    val end = Instant.now()

    log.info("$answer is the number of times the $kClass has been executed!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: MutableList<String>) {
    val register = "h"
    val start = Instant.now()
    val answer = registerValue(input, register, 1)
    val end = Instant.now()

    log.info("$answer is the number currently in register $register!")
    logTimings(log, start, end)
  }

  /**
   * Returns the number of times the specified [KClass] has been executed.
   *
   * @param input The raw program instructions.
   * @param kClass The [KClass] of an [Instruction] subclass to get the execution count of.
   * @return The number of times the specified [KClass] has been executed.
   */
  @SuppressFBWarnings
  fun instructionCount(input: List<String>, kClass: KClass<out Instruction>): Long {
    val debugger = InstructionCounterDebugger()
    val instance = CoProcessor.buildCoProcessor(input, debugger)
    debugger.reset()
    instance.reset()
    instance.run()
    val answer = debugger[kClass]
    return answer
  }

  /**
   * Returns the number of times the specified [KClass] has been executed.
   *
   * @param input The raw program instructions.
   * @param register The register to retrieve the value of after program execution.
   * @param a The value to place in register a prior to executing the program.
   * @return The number of times the specified [KClass] has been executed.
   */
  fun registerValue(input: List<String>, register: String, a: Long = 0): Long {
    val instance = CoProcessor.buildCoProcessor(input)
    instance.reset()
    instance["a"] = a
    instance.runOptimized()
    val answer = instance[register]
    return answer
  }
}
