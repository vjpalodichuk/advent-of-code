package com.capital7software.aoc.lib.computer

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

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
 * @param debugger The debugger to use for this instance.
 */
class CoProcessor private constructor(
    debugger: Debugger? = null
) : SmallComputer({}, {}, debugger) {
  @Suppress("comments:UndocumentedPublicClass")
  companion object {
    /**
     * Parses the specified [List] of raw [String] instructions into a [List] of
     * [Instruction] and loads them into a [SmallComputer]. Also initializes all registers to 0.
     *
     * @param input The [List] of raw [String] instructions to parse into [Instruction].
     * @param debugger The debugger to use for the new instance.
     * @return A [CoProcessor] loaded with the specified instructions.
     */
    @SuppressFBWarnings
    fun buildCoProcessor(
        input: List<String>,
        debugger: Debugger? = null
    ): CoProcessor {
      val computer = CoProcessor(debugger)
      val instructions = Optimizer(input.map { line ->
        val instruction = InstructionFactory.parse(line)
        instruction.args().forEach { arg ->
          if (arg.isNotBlank() && arg.toLongOrNull() == null) {
            computer.registers.computeIfAbsent(arg) { Register(it) }
          }
        }
        instruction
      }).optimize()
      computer.instructions.addAll(instructions)

      return computer
    }

    private const val B_INITIAL_VALUE: Long = 79L
    private const val B_MULTIPLIER: Long = 100L
    private const val B_ADDEND: Long = 100_000L
    private const val C_ADDEND: Long = 17_000L
    private const val B_INCREMENT: Long = 17L
  }

  /**
   * Quickly executes the specialized program and returns the [ProgramContext] after it was
   * executed.
   *
   * @return The [ProgramContext] after the program was executed.
   */
  fun runOptimized(): ProgramContext {
    set("b", B_INITIAL_VALUE)
    set("c", get("b"))
    if (get("a") != 0L) {
      set("b", B_MULTIPLIER * get("b") + B_ADDEND)
      set("c", get("b") + C_ADDEND)
    }
    do {
      set("f", 1L)
      set("d", 2L)
      do {
        set("e", 2L)
        if (get("b") % get("d") == 0L && get("b") / get("d") != 1L) {
          set("f", 0L)
        }
        set("d", get("d") + 1L)
      } while (get("d") != get("b"))
      if (get("f") == 0L) {
        set("h", get("h") + 1L)
      }
      if (get("b") == get("c")) {
        break
      }
      set("b", get("b") + B_INCREMENT)
    } while (true)

    return DefaultProgramContext({ }, { }, instructions, registers)
  }
}
