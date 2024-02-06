package com.capital7software.aoc.lib.grid

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.*

/**
 * Parses [String] based instructions into [LightInstruction] based instructions.
 */
@SuppressFBWarnings
object LightInstructionFactory {
  /**
   * Parses the raw instructions and returns a [List] of [LightInstruction].
   *
   * For example:
   *
   *     turn on 0,0 through 999,999
   *     turn off 0,0 through 999,999
   *     toggle 0,0 through 999,999
   *
   * @param input The raw instructions.
   * @return The [List] of parsed [LightInstruction].
   */
  @JvmStatic
  fun parse(input: List<String?>): List<LightInstruction> {
    return input
        .stream()
        .map { line: String? -> parseLine(line) }
        .filter { obj: LightInstruction? -> Objects.nonNull(obj) }
        .map { it as LightInstruction }
        .toList()
  }

  /**
   * Parses the raw instruction and returns a [LightInstruction].
   *
   * For example:
   *
   *     turn on 0,0 through 999,999
   *     turn off 0,0 through 999,999
   *     toggle 0,0 through 999,999
   *
   * @param line The raw instructions.
   * @return The parsed [LightInstruction].
   */
  @JvmStatic
  fun parseLine(line: String?): LightInstruction? {
    if (line.isNullOrBlank()) {
      return null
    }

    return LightInstructionType.parseInstruction(line)
  }
}
