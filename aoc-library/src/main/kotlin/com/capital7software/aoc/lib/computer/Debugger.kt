package com.capital7software.aoc.lib.computer

import kotlin.reflect.KClass

/**
 * The result of calling an operation on the [Debugger].
 */
enum class DebuggerResult {
  /**
   * The caller **should** continue to execute the [ProgramContext] as planned.
   */
  CONTINUE,

  /**
   * The caller **should not** execute the current [Instruction] and instead should skip to the next
   * instruction. If skipping to the next instruction leads to the instruction pointer pointing
   * outside the list of instructions in the [ProgramContext], the program will exit.
   */
  SKIP,

  /**
   * The caller **should not** execute the current [Instruction] and instead should immediately
   * stop executing the [ProgramContext] and exit the program.
   */
  STOP
}

/**
 * A [Debugger] is used for debugging a [ProgramContext].
 */
interface Debugger {
  /**
   * Called before the current instruction in the specified context is executed.
   *
   * Please note that a debugger is able to modify the [ProgramContext] and its contents.
   *
   * - If [DebuggerResult.STOP] is returned, the caller should immediately stop executing the
   * specified [ProgramContext] and instead should exit.
   *
   * - If [DebuggerResult.SKIP] is returned, the caller should skip executing the current
   * instruction and, instead it should increment the instruction pointer and then continue
   * as it normally would. That may mean the program will exit.
   *
   * - If [DebuggerResult.CONTINUE] is returned, the caller should continue as normal.
   *
   * Ultimately, it is up to the **caller** to decide how to proceed.
   *
   * Callers may call multiple different instances of different debuggers on the same
   * [ProgramContext] instance in succession but **must not** call them
   * concurrently or in parallel unless care is taken to ensure that no more than a single
   * debugger writes to the context at a time.
   *
   * @param programContext The [ProgramContext] this debugger can operate on.
   * @return Returns a [DebuggerResult] that indicates if how the caller should proceed.
   */
  fun inspect(programContext: ProgramContext): DebuggerResult
}

/**
 * This [Debugger] keeps track of the number of times each [Instruction] type is about to be
 * executed. Users of this debugger can call
 */
class InstructionCounterDebugger: Debugger {
  private val counts: MutableMap<KClass<out Instruction>, Long> = mutableMapOf()
  override fun inspect(programContext: ProgramContext): DebuggerResult {
    if (programContext.index in 0 ..< programContext.instructionCount) {
      val instruction = programContext[programContext.index]
      val newCount = counts.computeIfAbsent(instruction::class) { 0L } + 1L
      counts[instruction::class] = newCount
    }
    return DebuggerResult.CONTINUE
  }

  /**
   * Returns the number of times this debugger has inspected an [Instruction] instance from the
   * specified [KClass].
   *
   * @param kClass The class to get a count of, such as [Multiply] or any number of subclasses
   * of [Instruction].
   * @return The number of times this debugger has seen an instance of the specified class.
   */
  operator fun get(kClass: KClass<out Instruction>): Long {
    return counts[kClass] ?: 0L
  }

  /**
   * Callers can call this to reset the counts of the instructions seen by this instance.
   */
  fun reset() {
    counts.clear()
  }
}
