package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.util.Pair
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in
 * such a rush that you forgot to use the bathroom! Fancy office buildings like this one
 * usually have keypad locks on their bathrooms, so you search the front desk for the code.
 *
 * "In order to improve security," the document you find says, "bathroom codes will no
 * longer be written down. Instead, please memorize and follow the procedure below to
 * access the bathrooms."
 *
 * The document goes on to explain that each button to be pressed can be found by starting
 * on the previous button and moving to adjacent buttons on the keypad: U moves up,
 * D moves down, L moves left, and R moves right. Each line of instructions corresponds
 * to one button, starting at the previous button (or, for the first line, **the "5" button**);
 * press whatever button you're on at the end of each line. If a move doesn't lead
 * to a button, ignore it.
 *
 * You can't hold it much longer, so you decide to figure out the code as you walk
 * to the bathroom. You picture a keypad like this:
 *
 *    1 2 3
 *    4 5 6
 *    7 8 9
 *
 * Suppose your instructions are:
 *
 *    ULL
 *    RRDDD
 *    LURDL
 *    UUUUD
 * You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"),
 * so the first button is 1.
 * Starting from the previous button ("1"), you move right twice (to "3") and then down three
 * times (stopping at "9" after two moves and ignoring the third), ending up with 9.
 * Continuing from "9", you move left, up, right, down, and left, ending with 8.
 * Finally, you move up four times (stopping at "2"), then down once, ending with 5.
 * So, in this example, the bathroom code is 1985.
 *
 * Your puzzle input is the instructions from the document you found at the front desk.
 * What is the bathroom code?
 *
 * @param instructions The list of instructions for finding the codes.
 */
@SuppressFBWarnings
class BathroomCode(
    val instructions: List<Pair<Int, String>>
) {
  companion object {
    private val startSimple = Point2D<Int>(1, 1)
    private val startComplex = Point2D<Int>(0, 2)

    private val simpleKeypad = Grid2D<Char>(3, 3, Array<Char>(3 * 3) { '0' })
        .apply {
      set(0, 0, '1')
      set(1, 0, '2')
      set(2, 0, '3')
      set(0, 1, '4')
      set(1, 1, '5')
      set(2, 1, '6')
      set(0, 2, '7')
      set(1, 2, '8')
      set(2, 2, '9')
    }
    private val complexKeypad = Grid2D<Char>(5, 5, Array<Char>(5 * 5) { '0' })
        .apply {
      set(2, 0, '1')
      set(1, 1, '2')
      set(2, 1, '3')
      set(3, 1, '4')
      set(0, 2, '5')
      set(1, 2, '6')
      set(2, 2, '7')
      set(3, 2, '8')
      set(4, 2, '9')
      set(1, 3, 'A')
      set(2, 3, 'B')
      set(3, 3, 'C')
      set(2, 4, 'D')
    }

    /**
     * Builds and returns a new BathroomCode instance loaded with the specified instructions.
     *
     * @param input The [List] of [String] which represent the instructions for finding each
     * number in the code.
     * @return A new BathroomCode instance loaded with the specified instructions.
     */
    fun buildBathroomCode(input: List<String>): BathroomCode {
      return BathroomCode(
          input.withIndex().map { Pair(it.index, it.value) }
      )
    }
  }

  /**
   * Calculates and returns the bathroom code to use.
   *
   * @return The bathroom code to use.
   */
  fun findSimpleBathroomCode(): String {
    val builder = StringBuilder()
    val keypad: Grid2D<Char> = simpleKeypad
    var lastPoint = startSimple

    instructions.forEach { instruction ->
      instruction.second().toCharArray()
          .forEach { directionLabel ->
            val direction = Direction.fromLabel(directionLabel.toString())
            val newPoint = Grid2D.pointInDirection(lastPoint, direction)

            if (keypad.isOnGrid(newPoint)) {
              lastPoint = newPoint
            }
          }
      builder.append(keypad.get(lastPoint))
    }

    return builder.toString()
  }

  /**
   * Calculates and returns the bathroom code to use.
   *
   * @return The bathroom code to use.
   */
  fun findComplexBathroomCode(): String {
    val builder = StringBuilder()
    val keypad: Grid2D<Char> = complexKeypad
    var lastPoint = startComplex

    instructions.forEach { instruction ->
      instruction.second().toCharArray()
          .forEach { directionLabel ->
            val direction = Direction.fromLabel(directionLabel.toString())
            val newPoint = Grid2D.pointInDirection(lastPoint, direction)

            if (keypad.isOnGrid(newPoint) && keypad.get(newPoint) != '0') {
              lastPoint = newPoint
            }
          }
      builder.append(keypad.get(lastPoint))
    }

    return builder.toString()
  }
}
