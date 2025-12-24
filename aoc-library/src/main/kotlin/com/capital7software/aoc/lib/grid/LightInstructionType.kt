package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.util.Pair

/**
 * The type of lighting instruction for laying out the ChristmasLights.
 */
enum class LightInstructionType(
    /**
     * The prefix of the instruction that uniquely identifies the type.
     *
     */
    val prefix: String,
) {
  /**
   * Instruction to turn on a range of lights.
   *
   * For example:
   *
   *     turn on 0,0 through 999,999
   *
   * Would turn on (or leave on) every light.
   *
   * **ALTERNATE RULE**
   *
   * The phrase turn on means to increase the brightness of those lights by 1.
   */
  TURN_ON("turn on") {
    override fun realParse(instruction: String): LightInstruction {
      val range = parseRange(instruction)
      return TurnOnRangeLightInstruction(range.first(), range.second())
    }
  },

  /**
   * Instruction to turn off a range of lights.
   *
   * For example:
   *
   *     turn off 0,0 through 999,999
   *
   * Would turn off (or leave off) every light.
   *
   * **ALTERNATE RULE**
   *
   * The phrase turn off means to decrease the brightness of those lights by 1,
   * to a minimum of zero.
   *
   */
  TURN_OFF("turn off") {
    override fun realParse(instruction: String): LightInstruction {
      val range = parseRange(instruction)
      return TurnOffRangeLightInstruction(range.first(), range.second())
    }
  },

  /**
   * Instruction to toggle a range of lights.
   *
   * For example:
   *
   *     toggle 0,0 through 999,999
   *
   * Would toggle the first line of 1000 lights, turning off the ones that were on,
   * and turning on the ones that were off.
   *
   * **ALTERNATE RULE**
   *
   * The phrase toggle means to increase the brightness of those lights by 2.
   *
   */
  TOGGLE("toggle") {
    override fun realParse(instruction: String): LightInstruction {
      val range = parseRange(instruction)
      return ToggleRangeLightInstruction(range.first(), range.second())
    }
  },

  /**
   * Instruction to create a small rectangle in the top-left corner,
   *
   *     rect 3x2
   *
   *     ###....
   *     ###....
   *     .......
   *
   */
  RECT("rect") {
    override fun realParse(instruction: String): LightInstruction {
      val dimensions = instruction
              .substring(prefix.length + 1)
              .split("x".toRegex())
      val columns = dimensions[0].trim().toInt()
      val rows = dimensions[1].trim().toInt()

      return RectangleLightInstruction(columns, rows)
    }
  },

  /**
   * Instruction to rotate the specified column by the specified number of pixels.
   *
   *     rotate column x=1 by 1
   *
   *     #.#....
   *     ###....
   *     .#.....
   *
   */
  ROTATE_COLUMN("rotate column x=") {
    override fun realParse(instruction: String): LightInstruction {
      val elements = parseRotate(instruction)

      return RotateColumnInstruction(elements.first(), elements.second())
    }
  },

  /**
   * Instruction to rotate the specified row by the specified number of pixels.
   *
   *     rotate row y=0 by 4
   *
   *     ....#.#
   *     ###....
   *     .#.....
   *
   */
  ROTATE_ROW("rotate row y=") {
    override fun realParse(instruction: String): LightInstruction {
      val elements = parseRotate(instruction)

      return RotateRowInstruction(elements.first(), elements.second())
    }
  };

  @Suppress("comments:UndocumentedPublicClass")
  companion object {
    /**
     * If the specified instruction represents a known type, the instruction is parsed
     * and a new [LightInstruction] is returned; otherwise null is returned.
     *
     * @param instruction The instruction [String] to parse.
     * @return A new [LightInstruction] if the specified instruction is of a known type;
     * otherwise null is returned.
     */
    @JvmStatic
    fun parseInstruction(instruction: String): LightInstruction? {
      return entries.firstOrNull { it.canParse(instruction) }?.parse(instruction)
    }
  }

  /**
   * Returns true if this type can parse the specified instruction into a new
   * [LightInstruction] instance; otherwise null is returned.
   *
   * @return True if this type can parse the specified instruction into a new
   * [LightInstruction] instance; otherwise null is returned.
   */
  fun canParse(instruction: String): Boolean {
    return instruction.startsWith(prefix)
  }

  /**
   * If the specified instruction represents an instruction for this type then the instruction
   * is parsed and a new [LightInstruction] is returned; otherwise null is returned.
   *
   * @param instruction The instruction [String] to parse.
   * @return A new [LightInstruction] if the specified instruction is of a known type;
   * otherwise null is returned.
   */
  fun parse(instruction: String): LightInstruction? {
    if (!canParse(instruction)) {
      return null
    }

    return realParse(instruction)
  }

  protected abstract fun realParse(instruction: String): LightInstruction

  /**
   * Parses the specified range based instruction into a [Pair] of [Point2D] points that
   * represent the range of the instruction.
   *
   * @return A [Pair] of [Point2D] points that represent the range of the instruction.
   */
  protected fun parseRange(instruction: String): Pair<Point2D<Int>, Point2D<Int>> {
    val index = prefix.length + 1
    val coords = instruction
            .substring(index)
            .split(" through ".toRegex())
    val first = coords[0].split(",")
    val second = coords[1].split(",")

    val x1 = first[0].trim().toInt()
    val y1 = first[1].trim().toInt()
    val x2 = second[0].trim().toInt()
    val y2 = second[1].trim().toInt()

    return Pair(Point2D(x1, y1), Point2D(x2, y2))
  }

  protected fun parseRotate(instruction: String): Pair<Int, Int> {
    val elements = instruction
        .substring(prefix.length)
        .split("by".toRegex())
    val axis = elements[0].trim().toInt()
    val by = elements[1].trim().toInt()

    return Pair(axis, by)
  }
}
