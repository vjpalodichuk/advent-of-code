package com.capital7software.aoc.lib.computer

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * A small 3-bit computer used by the Historians' debugger.
 *
 * The program is encoded as a flat list of 3-bit values. Each instruction is two values:
 *
 * - opcode
 * - operand
 *
 * The instruction pointer points directly into that flat program list. Therefore, normal
 * instruction advancement is by `2`, while `jnz` assigns the pointer directly to its literal
 * operand.
 *
 * Registers `A`, `B`, and `C` are not limited to 3 bits and are represented as [Long] values.
 *
 * @param registerA The initial value of register `A`.
 * @param registerB The initial value of register `B`.
 * @param registerC The initial value of register `C`.
 * @param program The flat list of opcode / operand values.
 */
@SuppressFBWarnings
class ChronospatialComputer private constructor(
    registerA: Long,
    registerB: Long,
    registerC: Long,
    val program: List<Int>
) {
  private val initialRegisterB: Long = registerB
  private val initialRegisterC: Long = registerC

  private val registers: MutableMap<String, Register> = mutableMapOf(
      REGISTER_A to Register(REGISTER_A, registerA),
      REGISTER_B to Register(REGISTER_B, registerB),
      REGISTER_C to Register(REGISTER_C, registerC)
  )

  private var instructionPointer: Int = 0
  private val output: MutableList<Int> = mutableListOf()

  /**
   * Static factory and parsing helpers for [ChronospatialComputer].
   */
  companion object {
    private const val REGISTER_A = "A"
    private const val REGISTER_B = "B"
    private const val REGISTER_C = "C"
    private const val REGISTER_A_DIGIT_COUNT = 8

    private val RegisterRegex = """^Register (?<register>[ABC]): (?<value>-?\d+)$""".toRegex()
    private val ProgramRegex = """^Program: (?<program>[0-7](?:,[0-7])*)$""".toRegex()

    /**
     * Parses debugger output into a new [ChronospatialComputer].
     *
     * Expected input shape:
     *
     * ```
     * Register A: 729
     * Register B: 0
     * Register C: 0
     *
     * Program: 0,1,5,4,3,0
     * ```
     *
     * Blank lines are ignored.
     *
     * @param input The raw puzzle input.
     * @return A new [ChronospatialComputer] initialized from [input].
     */
    fun load(
        input: List<String>
    ): ChronospatialComputer {
      val registers = mutableMapOf(
          REGISTER_A to 0L,
          REGISTER_B to 0L,
          REGISTER_C to 0L
      )
      var program: List<Int>? = null

      input.filter { it.isNotBlank() }.forEach { line ->
        val registerMatch = RegisterRegex.matchEntire(line)
        val programMatch = ProgramRegex.matchEntire(line)

        when {
          registerMatch != null -> {
            val register = registerMatch.groups["register"]?.value
                ?: error("Unable to parse register from line: $line")
            val value = registerMatch.groups["value"]?.value?.toLong()
                ?: error("Unable to parse register value from line: $line")

            registers[register] = value
          }

          programMatch != null -> {
            program = programMatch.groups["program"]?.value
                ?.split(',')
                ?.map { it.toInt() }
                ?: error("Unable to parse program from line: $line")
          }

          else -> error("Unable to parse debugger line: $line")
        }
      }

      return ChronospatialComputer(
          registerA = registers[REGISTER_A] ?: 0L,
          registerB = registers[REGISTER_B] ?: 0L,
          registerC = registers[REGISTER_C] ?: 0L,
          program = program ?: error("No program was found in the input")
      )
    }
  }

  /**
   * Returns the current value of the specified register.
   *
   * @param register The register name: `A`, `B`, or `C`.
   * @return The current value of the specified register.
   */
  operator fun get(register: String): Long {
    return registers[register]?.value ?: error("Unknown register: $register")
  }

  /**
   * Sets the current value of the specified register.
   *
   * @param register The register name: `A`, `B`, or `C`.
   * @param value The value to store.
   */
  operator fun set(register: String, value: Long) {
    registers[register]?.value = value
  }

  /**
   * Executes the loaded program until the instruction pointer attempts to read an opcode past the
   * end of the program.
   *
   * @return The output values produced by all `out` instructions.
   */
  fun run(): List<Int> {
    while (instructionPointer in program.indices) {
      step()
    }

    return output.toList()
  }

  /**
   * Executes the loaded program and returns all output values joined by commas.
   *
   * @return The program output as a comma-separated [String].
   */
  fun runToString(): String = run().joinToString(",")

  /**
   * Returns the lowest positive initial value for register `A` that causes this computer's output
   * to exactly reproduce its own program.
   *
   * The original initial value of register `A` is intentionally ignored for this search. The initial
   * values of registers `B` and `C` are preserved.
   *
   * This method is available on all instances, but callers should normally construct the computer
   * with `ChronospatialComputer.load(input, true)` to make the Part 2 intent explicit.
   *
   * @return The lowest positive initial value for register `A`.
   */
  fun lowestPositiveInitialRegisterAForProgramCopy(): Long {
    return findLowestProgramCopyRegisterA()
  }

  private fun findLowestProgramCopyRegisterA(): Long {
    var candidates = setOf(0L)

    for (index in program.indices.reversed()) {
      val expected = program.drop(index)
      val nextCandidates = mutableSetOf<Long>()

      candidates.forEach { candidate ->
        for (digit in 0 until REGISTER_A_DIGIT_COUNT) {
          val next = candidate * REGISTER_A_DIGIT_COUNT + digit

          if (next <= 0) {
            continue
          }

          val actual = execute(
              registerA = next,
              registerB = initialRegisterB,
              registerC = initialRegisterC,
              maxOutput = expected.size
          )

          if (actual == expected) {
            nextCandidates.add(next)
          }
        }
      }

      candidates = nextCandidates

      check(candidates.isNotEmpty()) {
        "Unable to find any register A candidates that emit expected suffix: $expected"
      }
    }

    return candidates.min()
  }

  private fun execute(
      registerA: Long,
      registerB: Long,
      registerC: Long,
      maxOutput: Int = Int.MAX_VALUE
  ): List<Int> {
    val computer = ChronospatialComputer(
        registerA = registerA,
        registerB = registerB,
        registerC = registerC,
        program = program,
    )

    while (computer.instructionPointer in computer.program.indices &&
        computer.output.size < maxOutput) {
      computer.step()
    }

    return computer.output.toList()
  }

  private fun step() {
    val opcode = program[instructionPointer]
    val operand = program.getOrNull(instructionPointer + 1)
        ?: error("Missing operand for opcode $opcode at position $instructionPointer")

    when (opcode) {
      0 -> adv(operand)
      1 -> bxl(operand)
      2 -> bst(operand)
      3 -> jnz(operand)
      4 -> bxc()
      5 -> out(operand)
      6 -> bdv(operand)
      7 -> cdv(operand)
      else -> error("Invalid opcode: $opcode")
    }
  }

  private fun adv(operand: Int) {
    this[REGISTER_A] = divideRegisterAByPowerOfTwo(operand)
    instructionPointer += 2
  }

  private fun bxl(operand: Int) {
    this[REGISTER_B] = this[REGISTER_B] xor operand.toLong()
    instructionPointer += 2
  }

  private fun bst(operand: Int) {
    this[REGISTER_B] = combo(operand) modulo REGISTER_A_DIGIT_COUNT
    instructionPointer += 2
  }

  private fun jnz(operand: Int) {
    instructionPointer = if (this[REGISTER_A] == 0L) {
      instructionPointer + 2
    } else {
      operand
    }
  }

  private fun bxc() {
    this[REGISTER_B] = this[REGISTER_B] xor this[REGISTER_C]
    instructionPointer += 2
  }

  private fun out(operand: Int) {
    output.add((combo(operand) modulo REGISTER_A_DIGIT_COUNT).toInt())
    instructionPointer += 2
  }

  private fun bdv(operand: Int) {
    this[REGISTER_B] = divideRegisterAByPowerOfTwo(operand)
    instructionPointer += 2
  }

  private fun cdv(operand: Int) {
    this[REGISTER_C] = divideRegisterAByPowerOfTwo(operand)
    instructionPointer += 2
  }

  private fun divideRegisterAByPowerOfTwo(operand: Int): Long {
    val exponent = combo(operand)

    require(exponent >= 0) {
      "Combo operand resolved to a negative exponent: $exponent"
    }

    return if (exponent >= Long.SIZE_BITS - 1) {
      0L
    } else {
      this[REGISTER_A] / (1L shl exponent.toInt())
    }
  }

  private fun combo(operand: Int): Long {
    return when (operand) {
      0, 1, 2, 3 -> operand.toLong()
      4 -> this[REGISTER_A]
      5 -> this[REGISTER_B]
      6 -> this[REGISTER_C]
      7 -> error("Combo operand 7 is reserved and is not valid")
      else -> error("Invalid 3-bit operand: $operand")
    }
  }

  private infix fun Long.modulo(divisor: Int): Long {
    return ((this % divisor) + divisor) % divisor
  }
}
