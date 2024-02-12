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

      computer.instructions.addAll(input.map { Instruction.parse(it) })

      return computer
    }
  }

  /**
   * Represents a register in a computer that can hold a single [Long] value.
   *
   * @param id The identifier of this register.
   */
  data class Register(val id: String) {
    /**
     * The current value of this [Register]
     */
    var value: Long = 0
  }

  /**
   * Represents an instruction that can be executed on a [SmallComputer].
   *
   * @param label The instruction label.
   */
  sealed class Instruction(val label: String) {
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
          "jmp" -> Jump(split[1].clean().toLong())
          "jnz" -> JumpNotZero(split[1].clean(), split[2].clean().toLong())
          "jie" -> JumpIfEven(split[1].clean(), split[2].clean().toLong())
          "jio" -> JumpIfOne(split[1].clean(), split[2].clean().toLong())
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
    ) : Instruction("cpy") {
      override fun execute(registers: Map<String, Register>): Long {
        val dest: Register = registers[destination]
            ?: error("Missing destination register!")
        dest.value = source.toLongOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))
        return 1
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
    class Increment(private val register: String) : Instruction("inc") {
      override fun execute(registers: Map<String, Register>): Long {
        val reg: Register = registers[register]
            ?: error("Missing register!")
        reg.value += 1
        return 1
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
    class Decrement(private val register: String) : Instruction("dec") {
      override fun execute(registers: Map<String, Register>): Long {
        val reg: Register = registers[register]
            ?: error("Missing register!")
        reg.value -= 1
        return 1
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
    class Half(private val register: String) : Instruction("hlf") {
      override fun execute(registers: Map<String, Register>): Long {
        val reg: Register = registers[register]
            ?: error("Missing register!")
        reg.value /= 2
        return 1
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
    class Triple(private val register: String) : Instruction("tpl") {
      override fun execute(registers: Map<String, Register>): Long {
        val reg: Register = registers[register]
            ?: error("Missing register!")
        reg.value *= 3
        return 1
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
    class Jump(private val offset: Long) : Instruction("jmp") {
      override fun execute(registers: Map<String, Register>): Long {
        return offset
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
        private val source: String,
        private val offset: Long
    ) : Instruction("jnz") {
      override fun execute(registers: Map<String, Register>): Long {
        val value = source.toLongOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))

        return if (value != 0L) {
          offset
        } else {
          1
        }
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
        private val offset: Long
    ) : Instruction("jie") {
      override fun execute(registers: Map<String, Register>): Long {
        val value = source.toLongOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))

        return if (value % 2 == 0L) {
          offset
        } else {
          1
        }
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
        private val offset: Long
    ) : Instruction("jio") {
      override fun execute(registers: Map<String, Register>): Long {
        val value = source.toLongOrNull()
            ?: (registers[source]?.value ?: error("Missing source register!"))

        return if (value == 1L) {
          offset
        } else {
          1
        }
      }

      override fun toString(): String {
        return "JumpIfOdd(source='$source', offset=$offset)"
      }
    }

    /**
     * Executes this instruction and returns the offset relative to this [Instruction]
     * for the next [Instruction].
     *
     * Each instruction explains what parameters to pass and the order to pass them.
     *
     * @param registers The [Map] of [Register] for this [Instruction] to use.
     * @return The offset for next [Instruction].
     */
    abstract fun execute(registers: Map<String, Register>): Long
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
    return registers[register]?.value ?: -1L
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
   * Executes the loaded program.
   */
  fun run() {
    var i = 0

    while (i < instructions.size) {
      val instruction = instructions[i]

      i += instruction.execute(registers).toInt()
    }
  }
}

/**
 * A [String] extension function to remove commas and whitespace from this [String].
 */
fun String.clean(): String {
  return replace(",", "").trim()
}
