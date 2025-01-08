package com.capital7software.aoc.lib.string

/**
 * "Our computers are having issues, so I have no idea if we have any Chief Historians in stock!
 * You're welcome to check the warehouse, though," says the mildly flustered shopkeeper at the
 * [North Pole Toboggan Rental Shop](https://adventofcode.com/2020/day/2).
 * The Historians head out to take a look.
 *
 * The shopkeeper turns to you. "Any chance you can see why our computers are having issues again?"
 *
 * The computer appears to be trying to run a program, but its memory (your puzzle input) is
 * **corrupted**. All the instructions have been jumbled up!
 *
 * @param input The corrupted memory to process. All strings are joined into a single string
 * before being processed.
 */
class MullItOver(input: List<String>) {
  companion object {
    private val MUL_REGEX = "(?<instruction>mul)\\((?<operand1>\\d+),(?<operand2>\\d+)\\)".toRegex()
    private val DO_REGEX = "(?<instruction>do)\\(\\)".toRegex()
    private val DONT_REGEX = "(?<instruction>don't)\\(\\)".toRegex()
    private const val INSTRUCTION_GROUP = "instruction"
    private const val OPERAND_1_GROUP = "operand1"
    private const val OPERAND_2_GROUP = "operand2"
    private const val DO_LENGTH = "do()".length
  }

  private val input = input.joinToString(separator = System.lineSeparator())

  /**
   * It seems like the goal of the program is just to **multiply some numbers**. It does that
   * with instructions like ```mul(X,Y)```, where ```X``` and ```Y``` are each 1-3 digit numbers.
   * For instance, ```mul(44,46)``` multiplies ```44``` by ```46``` to get a result of ```2024```.
   * Similarly, ```mul(123,4)``` would multiply ```123``` by ```4```.
   *
   * However, because the program's memory has been corrupted, there are also many invalid
   * characters that should be ***ignored***, even if they look like part of a ```mul```
   * instruction. Sequences like ```mul(4*```, ```mul(6,9!```, ```?(12,34)```, or ```mul ( 2 , 4 )```
   * do **nothing**.
   *
   * For example, consider the following section of corrupted memory:
   *
   * ```xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))```
   *
   * Only four sections are real ```mul``` instructions. Adding up the result
   * of each instruction produces ```161``` ```(2*4 + 5*5 + 11*8 + 8*5)```.
   *
   * As you scan through the corrupted memory, you notice that some of the conditional statements
   * are also still intact. If you handle some of the uncorrupted conditional statements in the
   * program, you might be able to get an even more accurate result.
   *
   * There are two new instructions you'll need to handle:
   *
   * - The ```do()``` instruction **enables** future ```mul``` instructions.
   * - The ```don't()``` instruction **disables** future ```mul``` instructions.
   * Only the **most recent** ```do()``` or ```don't()``` instruction applies. At the beginning of
   * the program, ```mul``` instructions are **enabled**.
   *
   * For example:
   *
   * ```xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))```
   *
   * This corrupted memory is similar to the example from before, but this time the ```mul(5,5)```
   * and ```mul(11,8)``` instructions are **disabled** because there is a ```don't()``` instruction
   * before them. The other ```mul``` instructions function normally, including the one at the end
   * that gets re-**enabled** by a ```do()``` instruction.
   *
   * This time, the sum of the results is ```48``` ```(2*4 + 8*5)```.
   *
   * By default, valid mul instructions are processed and their products summed. If
   * enableDosAndDonts is true, then any valid mul instructions encountered between a
   * don't() and a do() are not processed.
   *
   * @param enableDosAndDonts If true, then any do() and don't() instructions will toggle the
   * enabled / disabled state when calculating the sum.
   */
  fun sumOfProducts(enableDosAndDonts: Boolean = true) : Long {
    var sum = 0L
    var enabled = true
    val matches = MUL_REGEX.findAll(input).toMutableList()
    val dos = DO_REGEX.findAll(input).toMutableList()
    val donts = DONT_REGEX.findAll(input).toMutableList()
    val lastRange = IntRange(input.length, input.length + 1)
    var currentDo = dos.removeFirstOrNull()?.range ?: IntRange(0, DO_LENGTH)
    var currentDont = donts.removeFirstOrNull()?.range ?: lastRange

    matches.forEach { match ->
      if (enableDosAndDonts) {
        if (enabled) {
          if (match.range.first > currentDont.last) {
            enabled = false
            currentDo = nextRange(currentDo, match.range, dos, lastRange)

          }
        } else {
          if (match.range.first > currentDo.last) {
            enabled = true
            currentDont = nextRange(currentDont, match.range, donts, lastRange)
          }
        }
      }

      if (enabled) {
        sum += processMatch(match)
      }
    }

    return sum
  }

  private fun nextRange(
      itemRange: IntRange,
      matchRange: IntRange,
      items: MutableList<MatchResult>,
      lastRange: IntRange
  ): IntRange {
    var result = itemRange

    while (result.last < matchRange.first) {
      result = items.removeFirstOrNull()?.range ?: lastRange
    }

    return result
  }

  private fun processMatch(match: MatchResult): Long {
    return when (match.groups.isNotEmpty()) {
      false -> 0L
      true -> {
        val instruction = match.groups[INSTRUCTION_GROUP]!!.value
        val operand1 = match.groups[OPERAND_1_GROUP]!!.value.toInt()
        val operand2 = match.groups[OPERAND_2_GROUP]!!.value.toInt()

        if (instruction == "mul") {
          (operand1 * operand2.toLong())
        } else {
          0L
        }
      }
    }
  }
}
