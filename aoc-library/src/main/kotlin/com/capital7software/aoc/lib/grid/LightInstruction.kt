package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Point2D
import kotlin.math.max

/**
 * A complete lighting instruction to apply to a grid.
 */
interface LightInstruction {
  /**
   * The [LightInstructionType] of this [LightInstruction]
   *
   */
  val type: LightInstructionType

  /**
   * Returns true if this [LightInstruction] has an alternate apply method.
   *
   */
  val hasAlternate: Boolean

  /**
   * Applies this [LightInstruction] to the specified [Grid2d].
   *
   * @param grid The [Grid2d] to apply this instruction to.
   */
  fun apply(grid: Grid2d<Int>)
}

/**
 * A [LightInstruction] that contains an alternate apply method.
 *
 */
interface LightInstructionWithAlternate : LightInstruction {
  /**
   * Applies the alternate form of this instruction.
   *
   * @param grid The [Grid2d] to apply this instruction to.
   */
  fun applyAlternate(grid: Grid2d<Int>)
}

/**
 * A useful [LightInstruction] base class to use for implementing different lighting instructions.
 *
 * @param type   The type of instruction (ON, OFF, TOGGLE).
 */
abstract class BaseLightInstruction(
    override val type: LightInstructionType
) : LightInstruction {
  override val hasAlternate: Boolean
    get() = false
}

/**
 * A useful [LightInstructionWithAlternate] base class to use for implementing different
 * lighting instructions that have alternate apply methods.
 *
 * @param type   The type of instruction (ON, OFF, TOGGLE).
 */
abstract class RangeLightInstruction(
    override val type: LightInstructionType
) : LightInstructionWithAlternate {
  override val hasAlternate: Boolean
    get() = true
}

/**
 * This [LightInstruction] will turn on a range of lights.
 *
 * This class also implements [LightInstructionWithAlternate] and offers an
 * alternate apply method.
 *
 * @param point1 The first point in the rectangle this instruction applies to.
 * @param point2 The second inclusive point in the rectangle (opposite corner) this instruction
 * applies to.
 */
class TurnOnRangeLightInstruction(
    val point1: Point2D<Int>,
    val point2: Point2D<Int>,
) : RangeLightInstruction(LightInstructionType.TURN_ON) {
  override fun apply(grid: Grid2d<Int>) {
    grid.set(point1, point2, 1)
  }

  override fun applyAlternate(grid: Grid2d<Int>) {
    grid.adjustBy(point1, point2) { current: Int -> current + 1 }
  }
}

/**
 * This [LightInstruction] will turn off a range of lights.
 *
 * This class also implements [LightInstructionWithAlternate] and offers an
 * alternate apply method.
 *
 * @param point1 The first point in the rectangle this instruction applies to.
 * @param point2 The second inclusive point in the rectangle (opposite corner) this instruction
 * applies to.
 */
class TurnOffRangeLightInstruction(
    val point1: Point2D<Int>,
    val point2: Point2D<Int>,
) : RangeLightInstruction(LightInstructionType.TURN_OFF) {
  override fun apply(grid: Grid2d<Int>) {
    grid.set(point1, point2, 0)
  }

  override fun applyAlternate(grid: Grid2d<Int>) {
    grid.adjustBy(point1, point2) { current: Int -> max(0, (current - 1)) }
  }
}

/**
 * This [LightInstruction] will toggle a range of lights.
 *
 * This class also implements [LightInstructionWithAlternate] and offers an
 * alternate apply method.
 *
 * @param point1 The first point in the rectangle this instruction applies to.
 * @param point2 The second inclusive point in the rectangle (opposite corner) this instruction
 * applies to.
 */
class ToggleRangeLightInstruction(
    val point1: Point2D<Int>,
    val point2: Point2D<Int>,
) : RangeLightInstruction(LightInstructionType.TOGGLE) {
  override fun apply(grid: Grid2d<Int>) {
    grid.toggle(point1, point2, 1, 0)
  }

  override fun applyAlternate(grid: Grid2d<Int>) {
    grid.adjustBy(point1, point2) { current: Int -> current + 2 }
  }
}

/**
 * Creates a columns x rows size rectangle in the upper left corner by turning
 * those lights on.
 *
 * @param columns The number of columns in the rectangle.
 * @param rows The number of rows in the rectangle.
 */
class RectangleLightInstruction(
    val columns: Int,
    val rows: Int,
) : BaseLightInstruction(LightInstructionType.RECT) {
  override fun apply(grid: Grid2d<Int>) {
    grid.set(0, 0, columns - 1, rows -1, 1)
  }
}

/**
 * Rotates the specified row of existing lights by the specified amount. The row will wrap
 * around if need be.
 *
 * @param rowIndex The row to rotate.
 * @param by The amount to rotate the specified row by.
 */
class RotateRowInstruction(
    val rowIndex: Int,
    val by: Int,
) : BaseLightInstruction(LightInstructionType.ROTATE_ROW) {
  override fun apply(grid: Grid2d<Int>) {
    val current = grid.getRow(rowIndex)
    val row = ArrayList(current)
    current.withIndex().forEach {
      row[(it.index + by) % current.size] = it.value
    }
    grid.setRow(rowIndex, row)
  }
}

/**
 * Rotates the specified column of existing lights by the specified amount. The column will wrap
 * around if need be.
 *
 * @param columnIndex The column to rotate.
 * @param by The amount to rotate the specified column by.
 */
class RotateColumnInstruction(
    val columnIndex: Int,
    val by: Int,
) : BaseLightInstruction(LightInstructionType.ROTATE_COLUMN) {
  override fun apply(grid: Grid2d<Int>) {
    val current = grid.getColumn(columnIndex)
    val column = ArrayList(current)
    current.withIndex().forEach {
      column[(it.index + by) % current.size] = it.value
    }
    grid.setColumn(columnIndex, column)
  }
}
