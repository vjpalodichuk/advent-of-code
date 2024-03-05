package com.capital7software.aoc.lib.computer

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
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
 * To be safe, the CPU also needs to know the **highest value held in any register
 * during this process** so that it can decide how much memory to allocate to these
 * operations. For example, in the above instructions, the highest value ever held
 * was 10 (in register c after the third instruction was evaluated).
 */
@SuppressFBWarnings
class LimitedComputer(input: List<String>) {
  private val instructions: List<Instruction> by lazy { input.map { Instruction.parse(it) } }
  private val registers: MutableMap<String, Int> = mutableMapOf()
  private var executionMax: Pair<String, Int>? = null

  private sealed class Instruction(
      val target: String,
      val amount: Int,
      val testRegister: String,
      val test: String,
      val testValue: Int
  ) {
    companion object {
      /**
       * Parses the raw instruction and returns an [Instruction].
       *
       * @param instruction The raw instruction to parse.
       * @return An [Instruction] that represents the specified instruction.
       */
      fun parse(instruction: String): Instruction {
        val split = instruction.split("\\s+".toRegex())

        return when (split[1]) {
          "inc" -> Increment(split[0], split[2].toInt(), split[4], split[5], split[6].toInt())
          "dec" -> Decrement(split[0], split[2].toInt(), split[4], split[5], split[6].toInt())
          else -> error("Unknown instruction: ${split[1]}")
        }
      }
    }

    /**
     * Executes this instruction and returns the offset relative to this [Instruction]
     * for the next [Instruction].
     *
     * Each instruction explains what parameters to pass and the order to pass them.
     *
     * @param registers The [Map] of registers for this [Instruction] to use.
     * @param instructions The [List] of [Instruction] to execute.
     * @param index The index to [Instruction] being executed.
     * @return The offset for next [Instruction].
     */
    abstract fun execute(
        registers: MutableMap<String, Int>,
        index: Int,
        instructions: MutableList<Instruction>
    ): Int

    protected fun test(lhs: Int, test: String, rhs: Int): Boolean {
      return when (test) {
        "<" -> lhs < rhs
        ">" -> lhs > rhs
        ">=" -> lhs >= rhs
        "<=" -> lhs <= rhs
        "==" -> lhs == rhs
        "!=" -> lhs != rhs
        else -> error("Unknown test condition: $test")
      }
    }

    private class Increment(
        target: String,
        amount: Int,
        testRegister: String,
        test: String,
        testValue: Int
    ) : Instruction(target, amount, testRegister, test, testValue) {
      override fun execute(
          registers: MutableMap<String, Int>, index: Int, instructions: MutableList<Instruction>
      ): Int {
        val testReg = registers.computeIfAbsent(testRegister) { 0 }
        val destReg = registers.computeIfAbsent(target) { 0 }

        if (test(testReg, test, testValue)) {
          registers[target] = destReg + amount
        }
        return 1
      }
    }

    private class Decrement(
        target: String,
        amount: Int,
        testRegister: String,
        test: String,
        testValue: Int
    ) : Instruction(target, amount, testRegister, test, testValue) {
      override fun execute(
          registers: MutableMap<String, Int>, index: Int, instructions: MutableList<Instruction>
      ): Int {
        val testReg = registers.computeIfAbsent(testRegister) { 0 }
        val destReg = registers.computeIfAbsent(target) { 0 }

        if (test(testReg, test, testValue)) {
          registers[target] = destReg - amount
        }
        return 1
      }
    }
  }

  /**
   * Executes the loaded program.
   */
  fun run() {
    var i = 0

    val temp = arrayListOf<Instruction>().apply { addAll(instructions) }
    registers.clear()
    executionMax = null

    while (i < temp.size) {
      val instruction = temp[i]

      i += instruction.execute(registers, i, temp)
      val current = registers.computeIfAbsent(instruction.target) { 0 }
      val currentMax = executionMax
      if (currentMax == null || currentMax.second < current) {
        executionMax = Pair(instruction.target, current)
      }
    }
  }

  /**
   * Returns a [Pair] where the first element is the register with the
   * maximum value and the second is the maximum value.
   */
  fun max(): Pair<String, Int>? {
    return registers.maxByOrNull { it.value }?.toPair()
  }

  /**
   * Returns the maximum value held by any register during the execution of the program.
   *
   * @return The maximum value held by any register during the execution of the program.
   */
  fun maxDuringExecution(): Pair<String, Int>? {
    return executionMax
  }
}
