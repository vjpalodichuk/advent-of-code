package com.capital7software.aoc.lib.computer

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * You finally reach the top floor of this building: a garden with a slanted glass ceiling.
 * Looks like there are no more stars to be had.
 *
 * While sitting on a nearby bench amidst some tiger lilies, you manage to decrypt some of the
 * files you extracted from the servers downstairs.
 *
 * According to these documents, Easter Bunny HQ isn't just this building - it's a
 * collection of buildings in the nearby area. They're all connected by a local monorail,
 * and there's another building not far from here! Unfortunately, being night, the
 * monorail is currently not operating.
 *
 * You remotely connect to the monorail control systems and discover that the boot
 * sequence expects a password. The password-checking logic (your puzzle input) is
 * easy to extract, but the code it uses is strange: it's assembunny code designed
 * for the new computer you just assembled. You'll have to execute the code and get
 * the password.
 *
 * The assembunny code you've extracted operates on four registers (a, b, c, and d)
 * that start at 0 and can hold any integer. However, it seems to make use of only
 * a few instructions:
 *
 * - cpy x y **copies** x (either an integer or the value of a register) into register y.
 * - inc x **increases** the value of register x by one.
 * - dec x **decreases** the value of register x by one.
 * - jnz x y **jumps** to an instruction y away (positive means forward; negative means
 * backward), but only if x is not zero.
 * - The jnz instruction moves relative to itself: an offset of -1 would continue at
 * the previous instruction, while an offset of 2 would **skip over** the next instruction.
 *
 * Node: Please see [Instruction] for a complete list of all instructions supported by
 * this computer.
 *
 * For example:
 * ```
 * cpy 41 a
 * inc a
 * inc a
 * dec a
 * jnz a 2
 * dec a
 * ```
 * The above code would set register a to 41, increase its value by 2, decrease its
 * value by 1, and then skip the last dec a (because a is not zero, so the jnz a 2
 * skips it), leaving register a at 42. When you move past the last instruction,
 * the program halts.
 *
 * @param outputHandler This handler is called everytime an [Instruction] with output is executed.
 * @param soundHandler This handler is called everytime a [PlaySound] [Instruction] is called.
 * @param debugger If present, will be called just prior to the current instruction being
 * executed.
 */
open class SmallComputer protected constructor(
    private val outputHandler: (output: Long) -> Unit,
    private val soundHandler: (output: Long) -> Unit,
    private val debugger: Debugger? = null,
) {
  protected val instructions = arrayListOf<Instruction>()
  protected val registers = mutableMapOf<String, Register>()

  @Suppress("comments:UndocumentedPublicClass")
  companion object {
    /**
     * Parses the specified [List] of raw [String] instructions into a [List] of
     * [Instruction] and loads them into a [SmallComputer]. Also initializes all registers to 0.
     *
     * @param input The [List] of raw [String] instructions to parse into [Instruction].
     * @param outputHandler The handler is called everytime an [Out] is executed.
     * @param soundHandler The handler is called everytime an [RecoverFrequency]
     * is executed.
     * @param useSound If true, then sound [instructions] will be used instead of
     * communication instructions.
     * @param debugger The debugger to use for the new instance.
     * @return A [SmallComputer] loaded with the specified instructions.
     */
    @SuppressFBWarnings
    fun buildSmallComputer(
        input: List<String>,
        outputHandler: (output: Long) -> Unit = {},
        soundHandler: (output: Long) -> Unit = {},
        useSound: Boolean = false,
        debugger: Debugger? = null
    ): SmallComputer {
      val computer = SmallComputer(outputHandler, soundHandler, debugger)
      val instructions = Optimizer(input.map { line ->
        val instruction = InstructionFactory.parse(line, useSound)
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

    private const val DEFAULT_MAX_OUTPUT = 20
  }

  /**
   * Resets all registers to zero.
   */
  fun reset() {
    registers.values.forEach { it.value = 0 }
  }

  /**
   * Returns the value of the specified register or -1 if there is no such register.
   *
   * @param register The register to retrieve the value of.
   * @return The value of the specified register or -1 if there is no such register.
   */
  operator fun get(register: String): Long {
    return registers[register]?.value ?: -1
  }

  /**
   * Sets the value of the specified register to the specified value. If the register
   * doesn't already exist, it is created.
   *
   * @param register The register to set the value of.
   * @param value The value to set the specified register to.
   */
  operator fun set(register: String, value: Long) {
    if (!registers.containsKey(register)) {
      registers[register] = Register(register)
    }

    registers[register]?.value = value
  }

  /**
   * Executes the loaded program. If the program contains any [Send] or [Receive] [Instruction]s
   * it will be run in linked mode with two [ProgramContext] contexts. Debugging is **not
   * supported** in linked mode.
   *
   * @return A [Pair] where the first element is the primary context and the second element
   * is the linked context which may be null if the execution didn't require a linked context.
   */
  @SuppressFBWarnings
  fun run(): Pair<ProgramContext, ProgramContext?> {
    val temp = arrayListOf<Instruction>().apply { addAll(instructions) }
    val context = DefaultProgramContext(outputHandler, soundHandler, instructions, registers)
    if (instructions.filterIsInstance<Receive>().isNotEmpty()) {
      return runLinked(temp, context)
    } else {
      var i = 0
      while (i >= 0 && i < temp.size) {
        if (debugger != null) {
          when (debugger.inspect(context)) {
            DebuggerResult.CONTINUE -> i = context.invoke()
            DebuggerResult.SKIP -> i++
            DebuggerResult.STOP -> i = temp.size + 1
          }
        } else {
          i = context.invoke()
        }
      }
      registers.putAll(context.getRegisters())
      return Pair<ProgramContext, ProgramContext?>(context, null)
    }
  }

  private fun runLinked(
      temp: ArrayList<Instruction>, context: DefaultProgramContext
  ): Pair<ProgramContext, ProgramContext> {
    val context2 = setLinkedContext(context)
    var i = 0
    var j = 0
    while ((i >= 0 && i < temp.size) || (j >= 0 && j < temp.size)) {
      i = context.invoke()
      j = context2.invoke()

      if (isDeadLocked(temp, context, context2, i, j)) {
        break
      }
    }
    registers.putAll(context.getRegisters())

    return Pair(context, context2)
  }

  /**
   * Executes the loaded program that either is or possibly is an infinite program.
   * If the program contains any [Send] or [Receive] [Instruction]s it will be run in
   * linked mode with two [ProgramContext] contexts. Debugging is **not supported**
   * in linked mode.
   *
   * @param max The maximum number of times to execute an [Out] or
   * [RecoverFrequency] instruction before ending the program. Also works with [Send] if
   * the executing program requires a linked context. Please note that
   * the program may end prior to max being reached if it isn't an infinite program.
   * @return A [Pair] where the first element is the primary context and the second element
   * is the linked context which may be null if the execution didn't require a linked context.
   */
  @SuppressFBWarnings
  fun runInfinite(max: Int = DEFAULT_MAX_OUTPUT): Pair<ProgramContext, ProgramContext?> {
    val temp = arrayListOf<Instruction>().apply { addAll(instructions) }
    val context = DefaultProgramContext(outputHandler, soundHandler, instructions, registers)
    if (instructions.filterIsInstance<Receive>().isNotEmpty()) {
      return runInfiniteLinked(max, temp, context)
    } else {
      var i = 0
      var count = 0
      while (i >= 0 && i < temp.size && count < max) {
        val instruction = temp[i]
        if (instruction is Out || instruction is RecoverFrequency) {
          count++
        }

        if (debugger != null) {
          when (debugger.inspect(context)) {
            DebuggerResult.CONTINUE -> i = context.invoke()
            DebuggerResult.SKIP -> i++
            DebuggerResult.STOP -> i = temp.size + 1
          }
        } else {
          i = context.invoke()
        }
      }
      registers.putAll(context.getRegisters())
      return Pair<ProgramContext, ProgramContext?>(context, null)
    }
  }

  private fun runInfiniteLinked(
      max: Int, temp: ArrayList<Instruction>, context: DefaultProgramContext
  ): Pair<ProgramContext, ProgramContext> {
    val context2 = setLinkedContext(context)
    var i = 0
    var countI = 0
    var j = 0
    var countJ = 0
    while ((i in 0 ..< temp.size && countI < max) || (j in 0 ..< temp.size && countJ < max)) {
      if (i in 0 ..< temp.size && (temp[i] is Out || temp[i] is Send)) {
        countI++
      }
      if (j in 0 ..< temp.size && (temp[j] is Out || temp[j] is Send)) {
        countJ++
      }

      i = context.invoke()
      j = context2.invoke()

      if (isDeadLocked(temp, context, context2, i, j)) {
        break
      }
    }
    registers.putAll(context.getRegisters())
    return Pair(context, context2)
  }

  private fun setLinkedContext(context: DefaultProgramContext): DefaultProgramContext {
    val context2 = DefaultProgramContext(outputHandler, soundHandler, instructions, registers)
    context.linkedContext = context2
    context2.linkedContext = context
    context["p"] = 0
    context2["p"] = 1
    return context2
  }

  private fun isDeadLocked(
      temp: ArrayList<Instruction>,
      context: DefaultProgramContext,
      context2: DefaultProgramContext,
      i: Int,
      j: Int
  ): Boolean {
    return if (context.blocked && context2.blocked) {
      val block1 = !(i in 0 ..< temp.size && temp[i] !is Receive)

      val block2 = !(j in 0 ..< temp.size && temp[j] !is Receive)

      block1 && block2
    } else {
      false
    }
  }
}
