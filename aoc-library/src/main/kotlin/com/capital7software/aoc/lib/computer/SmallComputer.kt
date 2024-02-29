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
 */
class SmallComputer {
  private val instructions = arrayListOf<Instruction>()
  private val registers = mutableMapOf<String, Register>().apply {
    put("a", Register("a"))
    put("b", Register("b"))
    put("c", Register("c"))
    put("d", Register("d"))
  }

  companion object {
    /**
     * Parses the specified [List] of raw [String] instructions into a [List] of
     * [Instruction] and loads them into a [SmallComputer].
     *
     * @param input The [List] of raw [String] instructions to parse into [Instruction].
     * @return A [SmallComputer] loaded with the specified instructions.
     */
    @SuppressFBWarnings
    fun buildSmallComputer(input: List<String>): SmallComputer {
      val computer = SmallComputer()
      val instructions = Optimizer(input.map { Instruction.parse(it) }).optimize()
      computer.instructions.addAll(instructions)

      return computer
    }
  }

  /**
   * Represents a register in a computer that can hold a single [Int] value.
   *
   * @param id The identifier of this register.
   */
  data class Register(val id: String) {
    /**
     * The current value of this [Register]
     */
    var value: Int = 0

    override fun toString(): String {
      return "Register(id='$id', value=$value)"
    }
  }

  /**
   * Represents an instruction that can be executed on a [SmallComputer].
   *
   * @param label The instruction label.
   * @param paramCount The number of parameters this instruction takes.
   */
  sealed class Instruction(val label: String, val paramCount: Int) {
    companion object {
      /**
       * Parses the specified [String] into an [Instruction] that can be executed by the
       * [SmallComputer].
       *
       * @param instruction The raw [String] instruction to parse.
       * @return An [Instruction] parsed from the specified raw [String] instruction.
       */
      fun parse(instruction: String): Instruction {
        val split = instruction.split("\\s+".toRegex())

        return when (split.first()) {
          "cpy" -> Copy(split[1].clean(), split[2].clean())
          "inc" -> Increment(split[1].clean())
          "dec" -> Decrement(split[1].clean())
          "hlf" -> Half(split[1].clean())
          "tpl" -> Triple(split[1].clean())
          "jmp" -> Jump(split[1].clean())
          "jnz" -> JumpNotZero(split[1].clean(), split[2].clean())
          "jie" -> JumpIfEven(split[1].clean(), split[2].clean())
          "jio" -> JumpIfOne(split[1].clean(), split[2].clean())
          "tgl" -> Toggle(split[1].clean())
          "add" -> Add(split[1].clean(), split[2].clean())
          "mul" -> Multiply(split[1].clean(), split[2].clean())
          "noo" -> NoOpSingleArg(split[1].clean())
          "nop" -> NoOpTwoArg(split[1].clean(), split[2].clean())
          else -> error("Unknown instruction: ${split.first()}")
        }
      }
    }

    /**
     * cpy x y copies x (either an integer or the value of a register) into register y.
     *
     * @param source The source register or an integer value.
     * @param destination The register to copy the source value to.
     */
    class Copy(
        private val source: String,
        private val destination: String
    ) : Instruction("cpy", 2) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val dest: Register = registers[destination]
            ?: error("Missing destination register!")
        dest.value = source.toIntOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))
        return 1
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(source, destination, "")
      }

      override fun toString(): String {
        return "Copy(source='$source', destination='$destination')"
      }
    }

    /**
     * inc x increases the value of register x by one.
     *
     * @param register The register to increment.
     */
    class Increment(private val register: String) : Instruction("inc", 1) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val reg: Register = registers[register]
            ?: error("Missing register!")
        reg.value += 1
        return 1
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(register, "", "")
      }

      override fun toString(): String {
        return "Increment(register='$register')"
      }
    }

    /**
     * dec x decreases the value of register x by one.
     *
     * @param register The register to decrement.
     */
    class Decrement(private val register: String) : Instruction("dec", 1) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val reg: Register = registers[register]
            ?: error("Missing register!")
        reg.value -= 1
        return 1
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(register, "", "")
      }

      override fun toString(): String {
        return "Decrement(register='$register')"
      }
    }

    /**
     * hlf r sets register r to half its current value.
     *
     * @param register The register to halve the value of.
     */
    class Half(private val register: String) : Instruction("hlf", 1) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val reg: Register = registers[register]
            ?: error("Missing register!")
        reg.value /= 2
        return 1
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(register, "", "")
      }

      override fun toString(): String {
        return "Half(register='$register')"
      }
    }

    /**
     * tpl r sets register r to triple its current value.
     *
     * @param register The register to halve the value of.
     */
    class Triple(private val register: String) : Instruction("tpl", 1) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val reg: Register = registers[register]
            ?: error("Missing register!")
        reg.value *= 3
        return 1
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(register, "", "")
      }

      override fun toString(): String {
        return "Triple(register='$register')"
      }
    }

    /**
     * jmp offset is a jump; it continues with the instruction offset away relative to itself.
     *
     * @param offset The offset relative to this instruction.
     */
    class Jump(private val offset: String) : Instruction("jmp", 1) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val numOffset = offset.toIntOrNull()
            ?: (registers[offset]?.value ?: error("Missing offset register!"))

        return numOffset
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(offset, "", "")
      }

      override fun toString(): String {
        return "Jump(offset=$offset)"
      }
    }

    /**
     * jnz x y jumps to an instruction y away (positive means forward; negative means backward),
     * but only if x is not zero; x may be a register or value.
     *
     * @param source The source register or an integer value.
     * @param offset The offset relative to this instruction.
     */
    class JumpNotZero(
        val source: String,
        val offset: String
    ) : Instruction("jnz", 2) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val value = source.toIntOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))

        val numOffset = offset.toIntOrNull()
            ?: (registers[offset]?.value ?: error("Missing offset register!"))

        return if (value != 0) {
          numOffset
        } else {
          1
        }
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(source, offset, "")
      }

      override fun toString(): String {
        return "JumpNotZero(source='$source', offset=$offset)"
      }
    }

    /**
     * jie r offset is like jmp, but only jumps if register r is even ("jump if even");
     * r may be a register or value.
     *
     * @param source The source register or an integer value.
     * @param offset The offset relative to this instruction.
     */
    class JumpIfEven(
        private val source: String,
        private val offset: String
    ) : Instruction("jie", 2) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val value = source.toIntOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))

        val numOffset = offset.toIntOrNull()
            ?: (registers[offset]?.value ?: error("Missing offset register!"))

        return if (value % 2 == 0) {
          numOffset
        } else {
          1
        }
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(source, offset, "")
      }

      override fun toString(): String {
        return "JumpIfEven(source='$source', offset=$offset)"
      }
    }

    /**
     * jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
     *
     * @param source The source register or an integer value.
     * @param offset The offset relative to this instruction.
     */
    class JumpIfOne(
        private val source: String,
        private val offset: String
    ) : Instruction("jio", 2) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val value = source.toIntOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))

        val numOffset = offset.toIntOrNull()
            ?: (registers[offset]?.value ?: error("Missing offset register!"))

        return if (value == 1) {
          numOffset
        } else {
          1
        }
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(source, offset, "")
      }

      override fun toString(): String {
        return "JumpIfOdd(source='$source', offset=$offset)"
      }
    }

    /**
     * tgl x **toggles** the instruction x away (pointing at instructions
     * like jnz does: positive means forward; negative means backward):
     *
     * - For **one-argument** instructions, inc becomes dec, and all other one-argument
     * instructions become inc.
     * - For **two-argument** instructions, jnz becomes cpy, and all other two-instructions
     * become jnz.
     * - The arguments of a toggled instruction are **not affected.**
     * - If an attempt is made to toggle an instruction outside the program, **nothing happens.**
     * - If toggling produces an **invalid instruction** (like cpy 1 2) and an attempt is
     * later made to execute that instruction, **skip it instead.**
     * - If tgl toggles **itself** (for example, if a is 0, tgl a would target itself and
     * become inc a), the resulting instruction is not executed until the next time it is reached.
     *
     * For example, given this program:
     *
     * ```
     * cpy 2 a
     * tgl a
     * tgl a
     * tgl a
     * cpy 1 a
     * dec a
     * dec a
     * ```
     *
     * - cpy 2 a initializes register a to 2.
     * - The first tgl a toggles an instruction a (2) away from it, which changes the
     * third tgl a into inc a.
     * - The second tgl a also modifies an instruction 2 away from it, which changes
     * the cpy 1 a into jnz 1 a.
     * - The fourth line, which is now inc a, increments a to 3.
     * - Finally, the fifth line, which is now jnz 1 a, jumps a (3) instructions ahead,
     * skipping the dec a instructions.
     *
     * In this example, the final value in register a is 3.
     *
     * @param source The register that contains the offset for this instruction to toggle.
     */
    class Toggle(val source: String) : Instruction("tgl", 1) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val value = source.toIntOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))

        val target = index + value

        return when (target < 0 || target >= instructions.size) {
          true -> 1
          else -> {
            val instruction = instructions[target]

            val params = instruction.args()
            val paramCount = instruction.paramCount

            val newInstruction = if (paramCount == 1) {
              when (instruction) {
                is Increment -> {
                  if (params.first.toIntOrNull() != null) {
                    NoOpSingleArg(params.first)
                  } else {
                    Decrement(params.first)
                  }
                }

                else -> {
                  if (params.first.toIntOrNull() != null) {
                    NoOpSingleArg(params.first)
                  } else {
                    Increment(params.first)
                  }
                }
              }
            } else {
              when (instruction) {
                is JumpNotZero -> {
                  if (params.second.toIntOrNull() != null) {
                    NoOpTwoArg(params.first, params.second)
                  } else {
                    Copy(params.first, params.second)
                  }
                }

                else -> {
                  JumpNotZero(params.first, params.second)
                }
              }
            }
            instructions[target] = newInstruction
            1
          }
        }
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(source, "", "")
      }

      override fun toString(): String {
        return "Toggle(target='$source')"
      }
    }

    /**
     * add x y adds x (either an integer or the value of a register) to the current value of
     * register y and stores the result of the addition in register y.
     *
     * @param source The source register or an integer value.
     * @param destination The register to add source by and store the result in.
     */
    class Add(
        private val source: String,
        private val destination: String
    ) : Instruction("add", 2) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val dest: Register = registers[destination]
            ?: error("Missing destination register!")
        val src = source.toIntOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))
        dest.value += src
        return 1
      }

      override fun args(): kotlin.Triple<String, String, String> =
          Triple(source, destination, "")

      override fun toString(): String {
        return "Add(source='$source', destination='$destination')"
      }
    }

    /**
     * mul x y multiplies x (either an integer or the value of a register) by the current value of
     * register y and stores the result of the multiplication in register y.
     *
     * @param source The source register or an integer value.
     * @param destination The register to multiply source by and store the result in.
     */
    class Multiply(
        private val source: String,
        private val destination: String
    ) : Instruction("mul", 2) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        val dest: Register = registers[destination]
            ?: error("Missing destination register!")
        val src = source.toIntOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))
        dest.value *= src
        return 1
      }

      override fun args(): kotlin.Triple<String, String, String> =
          Triple(source, destination, "")

      override fun toString(): String {
        return "Multiply(source='$source', destination='$destination')"
      }
    }

    /**
     * No-op 1 arg instruction.
     *
     * @param arg1 The one argument
     */
    class NoOpSingleArg(private val arg1: String) : Instruction("noo", 1) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        // Do nothing
        return 1
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(arg1, "", "")
      }

      override fun toString(): String {
        return "NoOpSingleArg(arg1='$arg1')"
      }
    }

    /**
     * No-op 2 arg instruction.
     *
     * @param arg1 The first arguments
     * @param arg2 The second argument.
     */
    class NoOpTwoArg(
        private val arg1: String, private val arg2: String
    ) : Instruction("nop", 2) {
      override fun execute(
          registers: Map<String, Register>, instructions: MutableList<Instruction>, index: Int
      ): Int {
        // Do nothing
        return 1
      }

      override fun args(): kotlin.Triple<String, String, String> {
        return Triple(arg1, arg2, "")
      }

      override fun toString(): String {
        return "NoOpTwoArg(arg1='$arg1', arg2='$arg2')"
      }
    }

    /**
     * Executes this instruction and returns the offset relative to this [Instruction]
     * for the next [Instruction].
     *
     * Each instruction explains what parameters to pass and the order to pass them.
     *
     * @param registers The [Map] of [Register] for this [Instruction] to use.
     * @param instructions The [List] of [Instruction] to execute.
     * @param index The index to [Instruction] being executed.
     * @return The offset for next [Instruction].
     */
    abstract fun execute(
        registers: Map<String, Register>,
        instructions: MutableList<Instruction>,
        index: Int
    ): Int

    /**
     * Returns the arguments of this [Instruction] in a [Triple].
     *
     * @return The arguments of this [Instruction] in a [Triple].
     */
    abstract fun args(): kotlin.Triple<String, String, String>
  }

  /**
   * Optimizes a list of instructions by replacing slow segments with faster instructions.
   *
   * For example:
   *
   * ```
   * cpy a d
   * cpy 0 a
   * cpy b c
   * inc a
   * dec c
   * jnz c -2
   * dec d
   * jnz d -5
   * ```
   *
   * The above instructions after cpy b c can be replaced with a single [Instruction.Multiply]
   * instruction. So, after [Register] b is copied over to register c, register a is
   * incremented by the value stored in register c. Then at jnz d -5 the program jumps back to
   * copying register b to register c and register a is again incremented by the value stored in
   * register c. So, register a is incremented c * d times as increment a c times is repeated
   * d times.
   *
   * In the above example, the code after cpy 0 a would be replaced with the following:
   *
   * ```
   * mul b d
   * cpy d a
   * cpy 0 c
   * cpy 0 d
   * noo 0
   * noo 0
   * ```
   *
   * Another example:
   *
   * ```
   * cpy c d
   * dec d
   * inc c
   * jnz d -2
   * ```
   *
   * The above instructions after copy c d can be replaced with a single [Instruction.Add]
   * instruction. So, after [Register] c is copied over to register d, register c is
   * incremented by the value stored in register d.
   *
   * In the add example, the code after cpy c d would be replaced with the following:
   *
   * ```
   * add d c
   * cpy 0 d
   * noo 0 0
   * ```
   *
   * The optimized code includes instructions to zero out the source registers (the ones that
   * are decremented) and no-op instructions so that jump instructions are still able to
   * jump to the correct place.
   *
   * @param instructions The list of [Instruction] to optimize.
   */
  class Optimizer(instructions: List<Instruction>) {
    private val instructions: List<Instruction> = instructions.toList()

    /**
     * Runs the optimizer and returns a new list of optimized instructions.
     *
     * @return A new list of optimized instructions.
     */
    fun optimize(): List<Instruction> {
      val answer = mutableListOf<Instruction>()

      for (i in instructions.indices) {
        when (val instruction = instructions[i]) {
          is Instruction.JumpNotZero -> {
            if (isMultiply(instruction, answer)) {
              convertToMultiply(instruction, answer)
            } else if (isAdd(instruction, answer)) {
              convertToAdd(instruction, answer)
            } else {
              answer.add(instruction)
            }
          }

          else -> answer.add(instruction)
        }
      }

      return answer
    }


    /**
     * Converts a multiply block using inc, dec, cpy, and jnz to a mul instruction. Replaces
     * the existing instructions in the specified list with new instructions.
     *
     * isMultiply must return true for the specified instruction before calling this method.
     *
     * @param instruction The [Instruction.JumpNotZero] instruction that is the last instruction
     * in the block and has not been added to the specified list.
     * @param instructions The [List] of [Instruction] upto, but not including, this instruction.
     */
    private fun convertToMultiply(
        instruction: Instruction.JumpNotZero, instructions: MutableList<Instruction>
    ) {
      val source = instruction.source
      val offset = instruction.offset.toInt()
      val backIndex = instructions.size + offset
      val cpy = instructions[backIndex]
      val inc = instructions[backIndex + 1]
      val dec1 = instructions[backIndex + 2]
      val dec2 = instructions[backIndex + 4]
      val mul = Instruction.Multiply(cpy.args().first, dec2.args().first)
      val newCpy = Instruction.Copy(dec2.args().first, inc.args().first)
      val zero1 = Instruction.Copy("0", dec1.args().first)
      val zero2 = Instruction.Copy("0", dec2.args().first)
      instructions[backIndex] = mul
      instructions[backIndex + 1] = newCpy
      instructions[backIndex + 2] = zero1
      instructions[backIndex + 3] = zero2
      instructions[backIndex + 4] = Instruction.NoOpSingleArg(inc.args().first)
      instructions.add(Instruction.NoOpSingleArg(source))
    }

    /**
     * Converts an addition block using inc, dec, and jnz to an add instruction. Replaces
     * the existing instructions in the specified list with new instructions.
     *
     * isAdd must return true for the specified instruction before calling this method.
     *
     * @param instruction The [Instruction.JumpNotZero] instruction that is the last instruction
     * in the block and has not been added to the specified list.
     * @param instructions The [List] of [Instruction] upto, but not including, this instruction.
     */
    private fun convertToAdd(
        instruction: Instruction.JumpNotZero, instructions: MutableList<Instruction>
    ) {
      val source = instruction.source
      val offset = instruction.offset.toInt()
      val backIndex = instructions.size + offset
      val dec = instructions[backIndex]
      val inc = instructions[backIndex + 1]
      val add = Instruction.Add(dec.args().first, inc.args().first)
      val zero = Instruction.Copy("0", source)
      instructions[backIndex] = add
      instructions[backIndex + 1] = zero
      instructions.add(Instruction.NoOpSingleArg(source))
    }

    /**
     * Returns true if the specified [Instruction.JumpNotZero] instruction is the last
     * instruction in a multiply operation.
     *
     * The following must be met:
     *
     * - The source parameter of the jump instruction must be a register.
     * - The offset parameter of the jump instruction must be an integer literal.
     * - The offset parameter must jump back to an [Instruction.Copy] instruction and
     * the source value of the copy instruction must be a register. The source value will be
     * used as the source operand in the [Instruction.Multiply]. The destination value of the
     * copy instruction must also be a register and will be zeroed out after
     * the multiply instruction.
     * - The instruction that immediately precedes the specified instruction must be an
     * [Instruction.Decrement] and the register parameter must be equal to the source parameter
     * of the specified jump instruction.
     * - The instruction immediately following the copy instruction must be an
     * [Instruction.Increment] and the register will have the result of the multiply instruction
     * copied into it.
     * - The instruction following the increment instruction must be an [Instruction.Decrement]
     * and the register must be equal to the destination register of the copy instruction.
     * - Finally, after the decrement instruction must be an [Instruction.JumpNotZero] that
     * jumps back to the increment instruction that follows the copy instruction and the source
     * must be a register that matches the register in the decrement instruction.
     *
     * If all of the above conditions are met, then true is returned.
     *
     * This is a fail-fast operation that will immediately return false as soon as one of
     * the above conditions isn't met.
     *
     * @param instruction The current instruction being examined.
     * @param instructions The [List] of [Instruction] that contains all the instructions
     * examined so far minus the specified instruction.
     */
    private fun isMultiply(
        instruction: Instruction.JumpNotZero, instructions: List<Instruction>
    ): Boolean {
      val source = instruction.source
      val offset = instruction.offset.toIntOrNull()

      return !(offset == null || !isAlpha(source) || offset >= 0
          || !jumpsBackToCopy(offset, instructions)
          || !isLastDecrement(source, instructions) || !incrementFollowsCopy(offset, instructions)
          || !decrementFollowsCopyIncrement(offset, instructions)
          || !jumpFollowsCopyIncrementDecrement(offset, instructions))
    }

    /**
     * Returns true if the specified [Instruction.JumpNotZero] instruction is the last
     * instruction in an addition operation.
     *
     * The following must be met:
     *
     * - The source parameter of the jump instruction must be a register.
     * - The offset parameter of the jump instruction must be an integer literal.
     * - The offset parameter must jump back to an [Instruction.Decrement] instruction and
     * the register of the decrement instruction must match the source register of the
     * jump instruction. This register will be added to the increment register and then
     * zeroed out.
     * - The instruction that immediately precedes the specified instruction must be an
     * [Instruction.Increment] and the instruction that immediately follows the decrement
     * instruction must be the same increment instruction. In short, the offset value of the
     * jump instruction must be equal to -2. The register in the increment instruction will be
     * used as an operand and destination for the add instruction.
     *
     * If all of the above conditions are met, then true is returned.
     *
     * This is a fail-fast operation that will immediately return false as soon as one of
     * the above conditions isn't met.
     *
     * @param instruction The current instruction being examined.
     * @param instructions The [List] of [Instruction] that contains all the instructions
     * examined so far minus the specified instruction.
     */
    private fun isAdd(
        instruction: Instruction.JumpNotZero, instructions: List<Instruction>
    ): Boolean {
      val source = instruction.source
      val offset = instruction.offset.toIntOrNull()

      return !(offset == null || !isAlpha(source) || offset >= 0
          || !jumpsBackToDecrement(offset, source, instructions)
          || !isLastIncrement(instructions)
          || !incrementFollowsDecrement(offset, instructions))
    }

    private fun isAlpha(value: String): Boolean = value.toIntOrNull() == null
    private fun isNumeric(value: String): Boolean = value.toIntOrNull() != null
    private fun isLastDecrement(value: String, instructions: List<Instruction>): Boolean =
        instructions.last() is Instruction.Decrement && instructions.last().args().first == value

    private fun isLastIncrement(instructions: List<Instruction>): Boolean =
        instructions.last() is Instruction.Increment

    private fun jumpsBackToDecrement(
        offset: Int, source: String, instructions: List<Instruction>
    ): Boolean =
        instructions.size + offset >= 0 && instructions.size + offset < instructions.size
            && instructions[instructions.size + offset] is Instruction.Decrement
            && isAlpha(instructions[instructions.size + offset].args().first)
            && instructions[instructions.size + offset].args().first == source

    private fun jumpsBackToCopy(offset: Int, instructions: List<Instruction>): Boolean =
        instructions.size + offset >= 0 && instructions.size + offset < instructions.size
            && instructions[instructions.size + offset] is Instruction.Copy
            && isAlpha(instructions[instructions.size + offset].args().first)
            && isAlpha(instructions[instructions.size + offset].args().second)

    private fun incrementFollowsDecrement(offset: Int, instructions: List<Instruction>): Boolean =
        instructions.size + offset + 1 >= 0 && instructions.size + offset + 1 < instructions.size
            && instructions[instructions.size + offset + 1] is Instruction.Increment
            && instructions[instructions.size + offset] is Instruction.Decrement
            && instructions.last() === instructions[instructions.size + offset + 1]

    private fun incrementFollowsCopy(offset: Int, instructions: List<Instruction>): Boolean =
        instructions.size + offset + 1 >= 0 && instructions.size + offset + 1 < instructions.size
            && instructions[instructions.size + offset + 1] is Instruction.Increment

    private fun decrementFollowsCopyIncrement(
        offset: Int, instructions: List<Instruction>
    ): Boolean =
        instructions.size + offset + 2 >= 0 && instructions.size + offset + 2 < instructions.size
            && instructions[instructions.size + offset + 2] is Instruction.Decrement
            && instructions[instructions.size + offset + 2].args().first ==
            instructions[instructions.size + offset].args().second

    private fun jumpFollowsCopyIncrementDecrement(
        offset: Int, instructions: List<Instruction>
    ): Boolean =
        instructions.size + offset + 3 >= 0 && instructions.size + offset + 3 < instructions.size
            && instructions[instructions.size + offset + 3] is Instruction.JumpNotZero
            && isNumeric(instructions[instructions.size + offset + 3].args().second)
            && offset + 3 == instructions[instructions.size + offset + 3].args().second.toInt()
            && instructions[instructions.size + offset + 3].args().first ==
            instructions[instructions.size + offset + 2].args().first
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
  operator fun get(register: String): Int {
    return registers[register]?.value ?: -1
  }

  /**
   * Sets the value of the specified register to the specified value. If the register
   * doesn't already exist, it is created.
   *
   * @param register The register to set the value of.
   * @param value The value to set the specified register to.
   */
  operator fun set(register: String, value: Int) {
    if (!registers.containsKey(register)) {
      registers[register] = Register(register)
    }

    registers[register]?.value = value
  }

  /**
   * Executes the loaded program.
   */
  fun run() {
    var i = 0
    val temp = arrayListOf<Instruction>().apply { addAll(instructions) }
    while (i < temp.size) {
      val instruction = temp[i]

      i += instruction.execute(registers, temp, i)
    }
  }
}

/**
 * A [String] extension function to remove commas and whitespace from this [String].
 */
fun String.clean(): String {
  return replace(",", "").trim()
}
