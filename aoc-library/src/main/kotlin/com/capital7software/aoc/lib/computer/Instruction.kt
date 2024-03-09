package com.capital7software.aoc.lib.computer

import com.capital7software.aoc.lib.string.clean

/**
 * Represents an instruction that can be executed on a [ProgramContext].
 */
interface Instruction {
  /**
   * Executes this instruction and returns the offset relative to this [Instruction]
   * for the next [Instruction].
   *
   * Each instruction explains what parameters to pass and the order to pass them.
   *
   * @param context The [ProgramContext] this instruction is executing in.
   * @return The offset for the next [Instruction] to execute.
   */
  fun invoke(context: ProgramContext): Int

  /**
   * Returns the arguments of this [Instruction] in an ordered [List].
   *
   * @return The arguments of this [Instruction] in a [List].
   */
  fun args(): List<String>
}

/**
 * Creates [Instruction].
 */
object InstructionFactory {
  /**
   * Parses the specified [String] into an [Instruction] that can be executed by the
   * [ProgramContext].
   *
   * @param instruction The raw [String] instruction to parse.
   * @return An [Instruction] parsed from the specified raw [String] instruction.
   */
  fun parse(instruction: String, useSound: Boolean = false): Instruction {
    val split = instruction.split("\\s+".toRegex())

    return when (split.first()) {
      "cpy" -> Copy(split[1].clean(), split[2].clean())
      "inc" -> Increment(split[1].clean())
      "dec" -> Decrement(split[1].clean())
      "hlf" -> HalveValue(split[1].clean())
      "tpl" -> TripleValue(split[1].clean())
      "jmp" -> Jump(split[1].clean())
      "jnz" -> JumpNotZero(split[1].clean(), split[2].clean())
      "jie" -> JumpIfEven(split[1].clean(), split[2].clean())
      "jio" -> JumpIfOne(split[1].clean(), split[2].clean())
      "tgl" -> Toggle(split[1].clean())
      "add" -> Add(split[1].clean(), split[2].clean())
      "mul" -> Multiply(split[1].clean(), split[2].clean())
      "noo" -> NoOpSingleArg(split[1].clean())
      "nop" -> NoOpTwoArg(split[1].clean(), split[2].clean())
      "out" -> Out(split[1].clean())
      "snd" -> if (useSound) PlaySound(split[1].clean()) else Send(split[1].clean())
      "set" -> SetValue(split[1].clean(), split[2].clean())
      "mod" -> Modulo(split[1].clean(), split[2].clean())
      "rcv" -> if (useSound) RecoverFrequency(split[1].clean()) else (Receive(split[1].clean()))
      "jgz" -> JumpGreaterThanZero(split[1].clean(), split[2].clean())
      else -> error("Unknown instruction: ${split.first()}")
    }
  }
}

/**
 * - cpy x y copies x (either an integer or the value of a register) into register y.
 *
 * @param source The source register or an integer value.
 * @param destination The register to copy the source value to.
 */
class Copy(
    private val source: String,
    private val destination: String
) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    context[destination] = source.toLongOrNull() ?: context[source]
    return 1
  }

  override fun args(): List<String> {
    return listOf(source, destination)
  }

  override fun toString(): String {
    return "Copy(source='$source', destination='$destination')"
  }
}

/**
 * - inc x increases the value of register x by one.
 *
 * @param register The register to increment.
 */
class Increment(private val register: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    context[register]++
    return 1
  }

  override fun args(): List<String> {
    return listOf(register)
  }

  override fun toString(): String {
    return "Increment(register='$register')"
  }
}

/**
 * - dec x decreases the value of register x by one.
 *
 * @param register The register to decrement.
 */
class Decrement(private val register: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    context[register]--
    return 1
  }

  override fun args(): List<String> {
    return listOf(register)
  }

  override fun toString(): String {
    return "Decrement(register='$register')"
  }
}

/**
 * - hlf r sets register r to half its current value.
 *
 * @param register The register to halve the value of.
 */
class HalveValue(private val register: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    context[register] /= 2L
    return 1
  }

  override fun args(): List<String> {
    return listOf(register)
  }

  override fun toString(): String {
    return "HalfValue(register='$register')"
  }
}

/**
 * - tpl r sets register r to triple its current value.
 *
 * @param register The register to halve the value of.
 */
class TripleValue(private val register: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    context[register] *= 3L
    return 1
  }

  override fun args(): List<String> {
    return listOf(register)
  }

  override fun toString(): String {
    return "TripleValue(register='$register')"
  }
}

/**
 * - jmp offset is a jump; it continues with the instruction offset away relative to itself.
 *
 * @param offset The offset relative to this instruction.
 */
class Jump(private val offset: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val numOffset = offset.toLongOrNull() ?: context[offset]

    return numOffset.toInt()
  }

  override fun args(): List<String> {
    return listOf(offset)
  }

  override fun toString(): String {
    return "Jump(offset=$offset)"
  }
}

/**
 * - jnz x y jumps to an instruction y away (positive means forward; negative means backward),
 * but only if x is not zero; x may be a register or value.
 *
 * @param source The source register or an integer value.
 * @param offset The offset relative to this instruction.
 */
class JumpNotZero(val source: String, val offset: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val value = source.toLongOrNull() ?: context[source]

    val numOffset = offset.toLongOrNull() ?: context[offset]

    return if (value != 0L) {
      numOffset.toInt()
    } else {
      1
    }
  }

  override fun args(): List<String> {
    return listOf(source, offset)
  }

  override fun toString(): String {
    return "JumpNotZero(source='$source', offset=$offset)"
  }
}

/**
 * - jie r offset is like jmp, but only jumps if register r is even ("jump if even");
 * r may be a register or value.
 *
 * @param source The source register or an integer value.
 * @param offset The offset relative to this instruction.
 */
class JumpIfEven(private val source: String, private val offset: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val value = source.toLongOrNull() ?: context[source]

    val numOffset = offset.toLongOrNull() ?: context[offset]

    return if (value % 2 == 0L) {
      numOffset.toInt()
    } else {
      1
    }
  }

  override fun args(): List<String> {
    return listOf(source, offset)
  }

  override fun toString(): String {
    return "JumpIfEven(source='$source', offset=$offset)"
  }
}

/**
 * - jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
 *
 * @param source The source register or an integer value.
 * @param offset The offset relative to this instruction.
 */
class JumpIfOne(private val source: String, private val offset: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val value = source.toLongOrNull() ?: context[source]

    val numOffset = offset.toLongOrNull() ?: context[offset]

    return if (value == 1L) {
      numOffset.toInt()
    } else {
      1
    }
  }

  override fun args(): List<String> {
    return listOf(source, offset)
  }

  override fun toString(): String {
    return "JumpIfOdd(source='$source', offset=$offset)"
  }
}

/**
 * - tgl x **toggles** the instruction x away (pointing at instructions
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
class Toggle(val source: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val value = source.toLongOrNull() ?: context[source]

    val target = context.index + value

    return when (target < 0 || target >= context.instructionCount) {
      true -> 1
      else -> {
        val instruction = context[target.toInt()]

        val params = instruction.args()
        val paramCount = instruction.args().size

        val newInstruction = if (paramCount == 1) {
          when (instruction) {
            is Increment -> {
              if (params.first().toLongOrNull() != null) {
                NoOpSingleArg(params.first())
              } else {
                Decrement(params.first())
              }
            }

            else -> {
              if (params.first().toLongOrNull() != null) {
                NoOpSingleArg(params.first())
              } else {
                Increment(params.first())
              }
            }
          }
        } else {
          when (instruction) {
            is JumpNotZero -> {
              if (params[1].toLongOrNull() != null) {
                NoOpTwoArg(params.first(), params[1])
              } else {
                Copy(params.first(), params[1])
              }
            }

            else -> {
              JumpNotZero(params.first(), params[1])
            }
          }
        }
        context[target.toInt()] = newInstruction
        1
      }
    }
  }

  override fun args(): List<String> {
    return listOf(source)
  }

  override fun toString(): String {
    return "Toggle(source='$source')"
  }
}

/**
 * - add x y sets register x to the result of adding the current value of
 * register x to the value of y (either an integer or the value of a register).
 *
 * @param register The register to add source by and store the result in.
 * @param increment The source register or an integer value.
 */
class Add(private val register: String, private val increment: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val src = increment.toLongOrNull() ?: context[increment]
    context[register] += src
    return 1
  }

  override fun args(): List<String> =
      listOf(increment, register)

  override fun toString(): String {
    return "Add(register='$register', increment='$increment')"
  }
}

/**
 * - mul x y sets register x to the result of multiplying the current value of
 * register x by the value of y (either an integer or the value of a register).
 *
 * @param register The register to multiply by and store the result in.
 * @param multiplier The source register or an integer value.
 */
class Multiply(private val register: String, private val multiplier: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val src = multiplier.toLongOrNull() ?: context[multiplier]
    context[register] *= src
    return 1
  }

  override fun args(): List<String> =
      listOf(multiplier, register)

  override fun toString(): String {
    return "Multiply(register='$register', multiplier='$multiplier')"
  }
}

/**
 * No-op 1 arg instruction.
 *
 * @param arg1 The one argument
 */
class NoOpSingleArg(private val arg1: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    // Do nothing
    return 1
  }

  override fun args(): List<String> {
    return listOf(arg1)
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
class NoOpTwoArg(private val arg1: String, private val arg2: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    // Do nothing
    return 1
  }

  override fun args(): List<String> {
    return listOf(arg1, arg2)
  }

  override fun toString(): String {
    return "NoOpTwoArg(arg1='$arg1', arg2='$arg2')"
  }
}

/**
 * - out x stores the value of the specified source [Register] in the [ProgramContext.output].
 *
 * After the value is stored the [ProgramContext.outputHandler] is then called with the
 * value of the output.
 *
 * @param source The literal or register to send to the [ProgramContext.output].
 * The [ProgramContext.output] is overwritten everytime this instruction is executed.
 */
class Out(private val source: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val src = source.toLongOrNull() ?: context[source]
    context.output = src
    context.outputHandler(src)
    return 1
  }

  override fun args(): List<String> {
    return listOf(source)
  }

  override fun toString(): String {
    return "Out(source='$source')"
  }
}

/**
 * - snd X plays a sound with a frequency equal to the value of X.
 *
 * Stores the value of the specified source [Register] in the [ProgramContext.soundOutput].
 *
 * @param source The literal or register to send to the [ProgramContext.soundOutput].
 * The [ProgramContext.soundOutput] is overwritten everytime this instruction is executed.
 */
class PlaySound(private val source: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val src = source.toLongOrNull() ?: context[source]
    context.soundOutput = src
    return 1
  }

  override fun args(): List<String> {
    return listOf(source)
  }

  override fun toString(): String {
    return "Sound(source='$source')"
  }
}

/**
 * - set X Y sets register X to the value of Y.
 *
 * This is the opposite of [Copy] as that copies x to y and stores the value in y.
 *
 * @param register The register to copy the source value to.
 * @param source The source register or an integer value.
 */
class SetValue(private val register: String, private val source: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    context[register] = source.toLongOrNull() ?: context[source]
    return 1
  }

  override fun args(): List<String> {
    return listOf(source, register)
  }

  override fun toString(): String {
    return "Set(source='$source', destination='$register')"
  }
}

/**
 * - mod X Y sets [Register] X to the remainder of dividing the value contained in
 * register X by the value of Y (that is, it sets X to the result of X modulo Y).
 *
 * @param register The register is the dividend and the result of the modulo operation is
 * then stored in this register.
 * @param divisor The source register or an integer value to divide the register by.
 */
class Modulo(private val register: String, private val divisor: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val src = divisor.toLongOrNull() ?: context[divisor]
    context[register] %= src
    return 1
  }

  override fun args(): List<String> =
      listOf(divisor, register)

  override fun toString(): String {
    return "Modulo(register='$register', divisor='$divisor')"
  }
}

/**
 * - rcv X recovers the frequency of the last sound played, but only when the
 * value of X is not zero. (If it is zero, the command does nothing.)
 *
 * After the value is recovered, the [ProgramContext.soundHandler] is called with the
 * recovered value.
 *
 * @param source The literal or register that contains the value to test.
 */
class RecoverFrequency(private val source: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val src = source.toLongOrNull() ?: context[source]
    if (src != 0L) {
      context.soundHandler(context.soundOutput)
    }
    return 1
  }

  override fun args(): List<String> {
    return listOf(source)
  }

  override fun toString(): String {
    return "RecoverFrequency(source='$source')"
  }
}

/**
 * - jgz X Y jumps with an offset of the value of Y, but only if the value of X is greater
 * than zero. (An offset of 2 skips the next instruction, an offset of -1 jumps to
 * the previous instruction, and so on.)
 *
 * @param source The register is the dividend and the result of the modulo operation is
 * then stored in this register.
 * @param offset The source register or an integer value to divide the register by.
 */
class JumpGreaterThanZero(private val source: String, private val offset: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val src = source.toLongOrNull() ?: context[source]
    val offset = offset.toLongOrNull() ?: context[offset]

    return if (src > 0) {
      offset.toInt()
    } else {
      1
    }
  }

  override fun args(): List<String> =
      listOf(offset, source)

  override fun toString(): String {
    return "Modulo(register='$source', divisor='$offset')"
  }
}

/**
 * - snd X **sends** the value of X to the other program. These values wait in a queue until
 * that program is ready to receive them. Each program has its own message queue, so a
 * program can never receive a message it sent.
 *
 * @param source The literal or register to send to the other program.
 */
class Send(private val source: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val value = source.toLongOrNull() ?: context[source]
    context.send(value)
    return 1
  }

  override fun args(): List<String> {
    return listOf(source)
  }

  override fun toString(): String {
    return "Send(source='$source')"
  }
}

/**
 * - rcv X **receives** the next value and stores it in register X. If no values are in the
 * queue, the program waits for a value to be sent to it. Programs do not continue to
 * the next instruction until they have received a value. Values are received in the
 * order they are sent.
 *
 * @param register The literal or register to store the received value in.
 */
class Receive(private val register: String) : Instruction {
  override fun invoke(
      context: ProgramContext
  ): Int {
    val value = context.receive()
    return if (value != null) {
      context[register] = value
      1
    } else {
      0
    }
  }

  override fun args(): List<String> {
    return listOf(register)
  }

  override fun toString(): String {
    return "Receive(source='$register')"
  }
}
