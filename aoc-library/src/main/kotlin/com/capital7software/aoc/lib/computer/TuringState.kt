package com.capital7software.aoc.lib.computer

/**
 * Determines what to do now and next.
 *
 * Based on whether the cursor is pointing at a 0 or a 1, the current state says
 * **what value to write** at the current position of the cursor, whether to **move the cursor**
 * left or right one slot, and **which state to use next**.
 *
 * @param id The ID of this state.
 * @param writeIfZero What value should be written if the current value is zero (0).
 * @param moveLeftIfZero If true and the current value is zero (0), then move left, else move right.
 * @param continueWithIZero The next state if the current value is zero (0).
 * @param writeIfOne What value should be written if the current value is one (1).
 * @param moveLeftIfOne If true and the current value is one (1), then move left, else move right.
 * @param continueWithIfOne The next state if the current value is one (1).
 */
class TuringState(
    val id: String,
    private val writeIfZero: Int,
    private val moveLeftIfZero: Boolean,
    private val continueWithIZero: String,
    private val writeIfOne: Int,
    private val moveLeftIfOne: Boolean,
    private val continueWithIfOne: String,
) {
  /**
   * Returns the value to write at the current position based on
   * the value of the current position.
   *
   * @param currentValue The value of the entry under the cursor.
   * @return The value to write to the entry under the cursor.
   */
  fun shouldWrite(currentValue: Int): Int {
    return if (currentValue == 0) {
      writeIfZero
    } else {
      writeIfOne
    }
  }

  /**
   * Returns true if the cursor should be moved to the left;
   * otherwise it should be moved to the right.
   *
   * @param currentValue The value of the entry under the cursor.
   * @return True if the cursor should be moved to the left;
   * otherwise it should be moved to the right.
   */
  fun shouldMoveLeft(currentValue: Int): Boolean {
    return if (currentValue == 0) {
      moveLeftIfZero
    } else {
      moveLeftIfOne
    }
  }

  /**
   * Returns the ID of the next state to use.
   *
   * @param currentValue The value of the entry under the cursor.
   * @return The ID of the state to use next.
   */
  fun shouldContinueWith(currentValue: Int): String {
    return if (currentValue == 0) {
      continueWithIZero
    } else {
      continueWithIfOne
    }
  }
}
