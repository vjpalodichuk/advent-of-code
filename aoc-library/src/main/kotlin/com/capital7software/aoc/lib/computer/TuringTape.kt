package com.capital7software.aoc.lib.computer

/**
 * The infinite tape in a [TuringMachine]. It supports writing only zeros (0) and ones (1).
 *
 * The tape has the following features:
 *
 * - It is infinite.
 * - The cursor can be moved either left or right, one position at a time.
 * - If the cursor moves off either end of the tape, it automatically adds a new entry.
 * - The cursor always points to the current entry.
 * - The current entry value can be read.
 * - The current entry can be written to.
 * - The tape can be iterated over like any other collection. The order of the entries
 * is not defined.
 *
 */
class TuringTape: Iterable<Int> {
  private class TapeEntry(
      var value: Int = 0,
      var left: TapeEntry? = null,
      var right: TapeEntry? = null
  )

  private val root: TapeEntry = TapeEntry()
  private var cursor: TapeEntry = root

  /**
   * The current size of this [TuringTape], which is the number of entries.
   *
   * Any time a new entry is added to this tape, the size will increase.
   */
  var size: Int = 1
    private set

  /**
   * Returns the value for the entry pointed to by the cursor.
   *
   * @return The value for the entry pointed to by the cursor.
   */
  fun read(): Int {
    return cursor.value
  }

  /**
   * Writes one (1) if the specified value is non-zero; otherwise writes
   * zero (0) to the entry pointed to by the cursor.
   *
   * @param value THe value to write.
   */
  fun write(value: Int) {
    cursor.value = if (value != 0) 1 else 0
  }

  /**
   * Moves the cursor to the left one entry.
   *
   * If the cursor is at the end of the tape going left,
   * it will add a new entry to the left and then move left.
   */
  fun moveLeft() {
    if (cursor.left == null) {
      val t = TapeEntry(right = cursor)
      cursor.left = t
      size++
    }
    cursor = cursor.left!!
  }

  /**
   * Moves the cursor to the right one entry.
   *
   * If the cursor is at the end of the tape going right,
   * it will add a new entry to the right and then move right.
   */
  fun moveRight() {
    if (cursor.right == null) {
      val t = TapeEntry(left = cursor)
      cursor.right = t
      size++
    }
    cursor = cursor.right!!
  }

  private inner class TapeIterator: Iterator<Int> {
    var goLeft = true
    var goRight = false
    var current: TapeEntry? = root

    override fun hasNext(): Boolean {
      return current != null
    }

    override fun next(): Int {
      if (current == null) {
        throw NoSuchElementException()
      }

      val temp = current!!
      val value = temp.value

      current = if (goLeft && temp.left == null) {
        goLeft = false
        goRight = true
        // The first element returned is the root, and so we have to go to the right of it.
        root.right
      } else if (goLeft) {
        temp.left
      } else if (goRight) {
        temp.right
      } else {
        goLeft = false
        goRight = false
        null
      }

      return value
    }

  }
  override fun iterator(): Iterator<Int> = TapeIterator()

  /**
   * Resets this tape to the starting state.
   */
  fun reset() {
    if (root.left != null) {
      root.left!!.right = null
    }
    root.left = null
    if (root.right != null) {
      root.right!!.left = null
    }
    root.right = null
    root.value = 0
    cursor = root
    size = 1
  }
}
