package com.capital7software.aoc.lib.computer

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
 * The above instructions after cpy b c can be replaced with a single [Multiply]
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
 * The above instructions after copy c d can be replaced with a single [Add]
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
        is JumpNotZero -> {
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
   * @param instruction The [JumpNotZero] instruction that is the last instruction
   * in the block and has not been added to the specified list.
   * @param instructions The [List] of [Instruction] upto, but not including, this instruction.
   */
  private fun convertToMultiply(
      instruction: JumpNotZero, instructions: MutableList<Instruction>
  ) {
    val source = instruction.source
    val offset = instruction.offset.toInt()
    val backIndex = instructions.size + offset
    val cpy = instructions[backIndex]
    val inc = instructions[backIndex + 1]
    val dec1 = instructions[backIndex + 2]
    val dec2 = instructions[backIndex + 4]
    val mul = Multiply(dec2.args().first(), cpy.args().first())
    val newCpy = Copy(dec2.args().first(), inc.args().first())
    val zero1 = Copy("0", dec1.args().first())
    val zero2 = Copy("0", dec2.args().first())
    instructions[backIndex] = mul
    instructions[backIndex + 1] = newCpy
    instructions[backIndex + 2] = zero1
    instructions[backIndex + 3] = zero2
    instructions[backIndex + 4] = NoOpSingleArg(inc.args().first())
    instructions.add(NoOpSingleArg(source))
  }

  /**
   * Converts an addition block using inc, dec, and jnz to an add instruction. Replaces
   * the existing instructions in the specified list with new instructions.
   *
   * isAdd must return true for the specified instruction before calling this method.
   *
   * @param instruction The [JumpNotZero] instruction that is the last instruction
   * in the block and has not been added to the specified list.
   * @param instructions The [List] of [Instruction] upto, but not including, this instruction.
   */
  private fun convertToAdd(
      instruction: JumpNotZero, instructions: MutableList<Instruction>
  ) {
    val source = instruction.source
    val offset = instruction.offset.toInt()
    val backIndex = instructions.size + offset
    val dec = instructions[backIndex]
    val inc = instructions[backIndex + 1]
    val add = Add(inc.args().first(), dec.args().first())
    val zero = Copy("0", source)
    instructions[backIndex] = add
    instructions[backIndex + 1] = zero
    instructions.add(NoOpSingleArg(source))
  }

  /**
   * Returns true if the specified [JumpNotZero] instruction is the last
   * instruction in a multiply operation.
   *
   * The following must be met:
   *
   * - The source parameter of the jump instruction must be a register.
   * - The offset parameter of the jump instruction must be an integer literal.
   * - The offset parameter must jump back to an [Copy] instruction and
   * the source value of the copy instruction must be a register. The source value will be
   * used as the source operand in the [Multiply]. The destination value of the
   * copy instruction must also be a register and will be zeroed out after
   * the multiply instruction.
   * - The instruction that immediately precedes the specified instruction must be an
   * [Decrement] and the register parameter must be equal to the source parameter
   * of the specified jump instruction.
   * - The instruction immediately following the copy instruction must be an
   * [Increment] and the register will have the result of the multiply instruction
   * copied into it.
   * - The instruction following the increment instruction must be an [Decrement]
   * and the register must be equal to the destination register of the copy instruction.
   * - Finally, after the decrement instruction must be an [JumpNotZero] that
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
      instruction: JumpNotZero, instructions: List<Instruction>
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
   * Returns true if the specified [JumpNotZero] instruction is the last
   * instruction in an addition operation.
   *
   * The following must be met:
   *
   * - The source parameter of the jump instruction must be a register.
   * - The offset parameter of the jump instruction must be an integer literal.
   * - The offset parameter must jump back to an [Decrement] instruction and
   * the register of the decrement instruction must match the source register of the
   * jump instruction. This register will be added to the increment register and then
   * zeroed out.
   * - The instruction that immediately precedes the specified instruction must be an
   * [Increment] and the instruction that immediately follows the decrement
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
      instruction: JumpNotZero, instructions: List<Instruction>
  ): Boolean {
    val source = instruction.source
    val offset = instruction.offset.toIntOrNull()

    return !(offset == null || !isAlpha(source) || offset >= 0
        || !jumpsBackToDecrement(offset, source, instructions)
        || !isLastIncrement(instructions)
        || !incrementFollowsDecrement(offset, instructions))
  }

  private fun isAlpha(value: String): Boolean = value.toLongOrNull() == null
  private fun isNumeric(value: String): Boolean = value.toLongOrNull() != null
  private fun isLastDecrement(value: String, instructions: List<Instruction>): Boolean =
      instructions.last() is Decrement && instructions.last().args().first() == value

  private fun isLastIncrement(instructions: List<Instruction>): Boolean =
      instructions.last() is Increment

  private fun jumpsBackToDecrement(
      offset: Int, source: String, instructions: List<Instruction>
  ): Boolean =
      instructions.size + offset >= 0 && instructions.size + offset < instructions.size
          && instructions[instructions.size + offset] is Decrement
          && isAlpha(instructions[instructions.size + offset].args().first())
          && instructions[instructions.size + offset].args().first() == source

  private fun jumpsBackToCopy(offset: Int, instructions: List<Instruction>): Boolean =
      instructions.size + offset >= 0 && instructions.size + offset < instructions.size
          && instructions[instructions.size + offset] is Copy
          && isAlpha(instructions[instructions.size + offset].args().first())
          && isAlpha(instructions[instructions.size + offset].args()[1])

  private fun incrementFollowsDecrement(offset: Int, instructions: List<Instruction>): Boolean =
      instructions.size + offset + 1 >= 0 && instructions.size + offset + 1 < instructions.size
          && instructions[instructions.size + offset + 1] is Increment
          && instructions[instructions.size + offset] is Decrement
          && instructions.last() === instructions[instructions.size + offset + 1]

  private fun incrementFollowsCopy(offset: Int, instructions: List<Instruction>): Boolean =
      instructions.size + offset + 1 >= 0 && instructions.size + offset + 1 < instructions.size
          && instructions[instructions.size + offset + 1] is Increment

  private fun decrementFollowsCopyIncrement(
      offset: Int, instructions: List<Instruction>
  ): Boolean =
      instructions.size + offset + 2 >= 0 && instructions.size + offset + 2 < instructions.size
          && instructions[instructions.size + offset + 2] is Decrement
          && instructions[instructions.size + offset + 2].args().first() ==
          instructions[instructions.size + offset].args()[1]

  private fun jumpFollowsCopyIncrementDecrement(
      offset: Int, instructions: List<Instruction>
  ): Boolean =
      instructions.size + offset + 3 >= 0 && instructions.size + offset + 3 < instructions.size
          && instructions[instructions.size + offset + 3] is JumpNotZero
          && isNumeric(instructions[instructions.size + offset + 3].args()[1])
          && offset + 3 == instructions[instructions.size + offset + 3].args()[1].toInt()
          && instructions[instructions.size + offset + 3].args().first() ==
          instructions[instructions.size + offset + 2].args().first()
}
