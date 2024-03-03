package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.grid.LightInstructionFactory.parse
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

private const val TURN_ON_LIMIT = 3L
private const val TURN_OFF_LOWER_LIMIT = 2L
private const val TURN_OFF_UPPER_LIMIT = 3L

/**
 * Because your neighbors keep defeating you in the holiday house decorating contest year after
 * year, you've decided to deploy one million lights in a 1000x1000 grid.
 *
 * Furthermore, because you've been especially nice this year, Santa has mailed you instructions
 * on how to display the ideal lighting configuration.
 *
 * Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are
 * at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or
 * toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents
 * opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore
 * refers to 9 lights in a 3x3 square. The lights all start turned off.
 *
 * To defeat your neighbors this year, all you have to do is set up your lights by doing the
 * instructions Santa sent you in order.
 *
 * For example:
 *
 *      turn on 0,0 through 999,999
 * Would turn on (or leave on) every light.
 *
 *      toggle 0,0 through 999,0
 * Would toggle the first line of 1000 lights, turning off the ones that were on, and turning
 * on the ones that were off.
 *
 *      turn off 499,499 through 500,500
 * Would turn off (or leave off) the middle four lights.
 *
 * After following the instructions, how many lights are lit?
 *
 * You just finish implementing your winning light pattern when you realize you mistranslated
 * Santa's message from Ancient Nordic Elvish.
 *
 * The light grid you bought actually has individual brightness controls; each light can have a
 * brightness of zero or more. The lights all start at zero.
 *
 * - The phrase turn on actually means that you should increase the brightness of those
 * lights by 1.
 * - The phrase turn off actually means that you should decrease the brightness of those lights
 * by 1, to a minimum of zero.
 * - The phrase toggle actually means that you should increase the brightness of those lights by 2.
 *
 * What is the total brightness of all lights combined after following Santa's instructions?
 *
 * For example:
 *
 *     turn on 0,0 through 0,0
 * Would increase the total brightness by 1.
 *
 *     toggle 0,0 through 999,999
 * Would increase the total brightness by 2000000.
 *
 * @param grid         The grid that holds all the lights.
 * @param instructions The list of instructions to apply to the light grid.
 */
@SuppressFBWarnings
class LightGrid(
    grid: Grid2d<Int>,
    instructions: List<LightInstruction>
) {

  private val grid: Grid2d<Int> = grid.copy()
  private val instructions: List<LightInstruction> = ArrayList(instructions)

  companion object {
    /**
     * Builds the LightGrid in a grid of the specified size and parses and loads the
     * specified raw lighting instructions.
     *
     * Format of the instructions are:
     *
     * -     turn on 887,9 through 959,629
     * -     turn off 539,243 through 559,965
     * -     toggle 235,450 through 650,725
     * -     rect 3x2
     * -     rotate column x=1 by 1
     * -     rotate row y=0 by 4
     *
     * @param columns         The number of columns in the grid.
     * @param rows            The number of rows in the grid.
     * @param rawInstructions The raw lighting instructions to parse and load.
     * @return A new LightGrid instance in the specified size and loaded with the parsed
     * instructions.
     */
    @JvmStatic
    fun buildFromLightingInstructions(
        columns: Int,
        rows: Int,
        rawInstructions: List<String?>
    ): LightGrid {
      val items = Array(columns * rows) { 0 }

      val grid = Grid2d(columns, rows, items)
      val lights = LightGrid(grid, parse(rawInstructions))
      lights.reset()

      return lights
    }

    /**
     * Builds the LightGrid in a grid based on the specified layout.
     *
     * Format of the is:
     *
     * - All rows must be of the same length.
     * - A **.** means to turn the light off.
     * - A **#** means to turn the light on.
     *
     * The grid size is determined by the number of rows and columns in the layout file.
     * The LightGrid returned by this instance have no Instructions as they are meant to
     * be animated using the new animate method.
     *
     * @param layout    The layout file to parse.
     * @param cornersOn If set to true, the four corner lights are turned on even if the layout
     * doesn't specify them to be.
     * @return A new LightGrid instance in the specified size and loaded with the parsed
     * instructions.
     */
    @JvmStatic
    fun buildFromLightingLayout(
        layout: List<String?>,
        cornersOn: Boolean
    ): LightGrid {
      val columns = AtomicInteger(0)
      val rows = AtomicInteger(0)
      val first = AtomicBoolean(true)
      val lights: MutableList<Int> = ArrayList()

      for (line in layout) {
        if (line.isNullOrBlank()) {
          continue
        }

        if (first.get()) {
          columns.set(line.length)
          first.set(false)
        }

        parseLayoutLine(line, lights)
        rows.getAndIncrement()
      }
      val items = Array(columns.get() * rows.get()) { 0 }

      assert(items.size == lights.size)
      for (i in lights.indices) {
        items[i] = lights[i]
      }

      if (cornersOn) {
        items[0] = 1
        items[items.size - 1] = 1
        items[columns.get() - 1] = 1
        items[columns.get() * rows.get() - columns.get()] = 1
      }

      val grid = Grid2d(columns.get(), rows.get(), items)

      return LightGrid(grid, ArrayList())
    }

    private fun parseLayoutLine(line: String, lights: MutableList<Int>) {
      for (c in line.toCharArray()) {
        if (c == '.') {
          lights.add(0)
        } else {
          lights.add(1)
        }
      }
    }
  }

  /**
   * Returns a copy of the Grid2D used by this instance.
   *
   * @return A copy of the Grid2D used by this instance.
   */
  fun grid(): Grid2d<Int> {
    return grid.copy()
  }

  /**
   * Returns an unmodifiable List of LightInstructions loaded into this instance.
   *
   * @return An unmodifiable List of LightInstructions loaded into this instance.
   */
  fun instructions(): List<LightInstruction> {
    return Collections.unmodifiableList(instructions)
  }

  /**
   * Turns all the lights in the grid off.
   */
  fun reset() {
    grid.fill(0)
  }

  /**
   * Applies all the loaded LightInstruction instructions in this instance.
   *
   * For example:
   *
   *     turn on 0,0 through 999,999
   * Would turn on (or leave on) every light.
   *
   *     toggle 0,0 through 999,0
   * Would toggle the first line of 1000 lights, turning off the ones that were on,
   * and turning on the ones that were off.
   *
   *     turn off 499,499 through 500,500
   * Would turn off (or leave off) the middle four lights.
   *
   * After following the instructions, how many lights are lit?
   */
  fun applyInstructions() {
    instructions.forEach(Consumer { it.apply(grid) })
  }

  /**
   * Applies all the loaded LightInstruction instructions in this instance using the new rules.
   *
   * The light grid you bought actually has individual brightness controls; each light can have a
   * brightness of zero or more. The lights all start at zero.
   *
   * - The phrase turn on actually means that you should increase the brightness of those
   * lights by 1.
   * - The phrase turn off actually means that you should decrease the brightness of those
   * lights by 1, to a minimum of zero.
   * - The phrase toggle actually means that you should increase the brightness of those lights by 2.
   *
   * For example:
   *
   *     turn on 0,0 through 0,0
   * Would increase the total brightness by 1.
   *
   *     toggle 0,0 through 999,999
   * Would increase the total brightness by 2000000.
   */
  fun applyAlternateInstructions() {
    instructions
        .stream()
        .filter(LightInstruction::hasAlternate)
        .map { it as LightInstructionWithAlternate }
        .forEach { it.applyAlternate(grid) }
  }

  /**
   * Returns the count of lights that are currently on.
   */
  val onLightCount: Long
    get() = grid.stream().filter { it > 0 }.count()

  /**
   * Returns the total brightness of all the lights that are on.
   */
  val totalBrightness: Long
    get() = grid.stream().mapToLong { it.toLong() }
        .sum()

  /**
   * Animates the lights using the following rules.
   *
   * - A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
   * - A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
   *
   * If cornersOn is set to true then the four corner lights will never turn off!
   *
   * @param steps     The number of animation steps to perform.
   * @param cornersOn If true, the four corner lights will never turn off!
   */
  fun animateLights(steps: Int, cornersOn: Boolean) {
    if (steps <= 0) {
      return
    }

    repeat(steps) {
      animateLights(cornersOn)
    }
  }

  /**
   * Animates the lights using the following rules.
   *
   * - A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
   * - A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
   *
   * If cornersOn is set to true then the four corner lights will never turn off!
   *
   * @param cornersOn If true, the four corner lights will never turn off!
   */
  fun animateLights(cornersOn: Boolean) {
    val currentState = grid.copy()

    for (i in 0 until grid.columns()) {
      for (j in 0 until grid.rows()) {
        if (cornersOn && grid.isCorner(i, j)) {
          grid[i, j] = 1
          continue
        }

        val neighbors = currentState.getNeighbors(i, j)
        val onCount = neighbors.stream()
            .map { it.second() }
            .filter { it != null && it > 0 }
            .count()

        val currentValue = currentState[i, j]!!

        if (currentValue > 0 && shouldTurnOffLight(onCount)) {
          grid[i, j] = 0
        } else if (currentValue == 0 && shouldTurnOnLight(onCount)) {
          grid[i, j] = 1
        }
      }
    }
  }

  private fun shouldTurnOffLight(onCount: Long): Boolean {
    return onCount < TURN_OFF_LOWER_LIMIT || onCount > TURN_OFF_UPPER_LIMIT
  }

  private fun shouldTurnOnLight(onCount: Long): Boolean {
    return onCount == TURN_ON_LIMIT
  }

  override fun toString(): String {
    val builder = StringBuilder(grid.size())
    builder.append("LightGrid - Columns: ")
        .append(grid.columns)
        .append(", Rows: ")
        .append(grid.rows)
        .append(", Instructions: ")
        .append(instructions.size)
        .append(getDisplayAsMessage())

    return builder.toString()
  }

  private fun getDisplayAsMessage(): String {
    val builder = StringBuilder(grid.size())

    grid.withIndex().forEach {
      if (it.index % grid.columns == 0) {
        builder.append("\n")
      }
      if (it.value > 0) {
        builder.append(it.value)
      } else {
        builder.append(" ")
      }
    }
    builder.append("\n")

    return builder.toString()
  }
}
