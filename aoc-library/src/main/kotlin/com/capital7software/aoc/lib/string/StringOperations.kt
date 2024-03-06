package com.capital7software.aoc.lib.string

import kotlin.math.max
import kotlin.math.min

/**
 * An [StringOperation] for manipulating the contents of a [CharArray].
 *
 * Various operations implement this interface. The details on what
 * a particular operation does can be found in the documentation for
 * that class.
 */
sealed interface StringOperation {
  /**
   * Executes the operation. Operations mutate the specified [CharArray].
   *
   * @param letters The [CharArray] to operate on.
   */
  fun execute(letters: CharArray)

  /**
   * Executes the inverse of the operation. Operations mutate the specified [CharArray].
   * Not all subclasses implement this optional method. If a class doesn't
   * override this method execute will automatically be called.
   *
   * @param letters The [CharArray] to operate on.
   */
  fun inverse(letters: CharArray) = execute(letters)
}

/**
 * Swaps the characters at the specified indexes.
 *
 * For example, suppose you start with abcde.
 *
 * - swap position 4 with position 0 swaps the first and last letters, producing ebcda.
 *
 * The inverse of this operation is simply this operation.
 *
 * @param first The first index in the swap operation.
 * @param second The second index in the swap operation.
 */
data class SwapPositions(val first: Int, val second: Int) : StringOperation {
  override fun execute(letters: CharArray) {
    val t = letters[first]
    letters[first] = letters[second]
    letters[second] = t
  }
}

/**
 * Swaps the characters by finding their positions in the array.
 * If the characters appear more than once, only the first occurrence of
 * each character will be swapped.
 *
 * For example, suppose you start with abcde.
 *
 * - swap letter d with letter b swaps the positions of d and b: adcbe.
 *
 * The inverse of this operation is simply this operation.
 *
 * @param first The first character in the swap operation.
 * @param second The second character in the swap operation.
 * @throws IllegalStateException If the letters cannot be found to swap them.
 */
data class SwapLetters(val first: Char, val second: Char) : StringOperation {
  override fun execute(letters: CharArray) {
    var i = -1
    var j = -1

    for (k in letters.indices) {
      if (letters[k] == first) {
        i = k
      }
      if (letters[k] == second) {
        j = k
      }
      if (i >= 0 && j >= 0) {
        break
      }
    }
    check(i >= 0 && j >= 0) { "Unable to swap letters" }

    val t = letters[i]
    letters[i] = letters[j]
    letters[j] = t
  }
}

/**
 * Rotates the character in the array to the left the specified number of [steps].
 *
 * For example, suppose you start with abcde.
 *
 * - rotate left 1-step shifts all letters left one position, causing the first letter
 * to wrap to the end of the string: bcdea.
 *
 * The inverse of this operation is [RotateRight].
 *
 * @param steps The number of steps to rotate.
 */
data class RotateLeft(val steps: Int) : StringOperation {
  override fun execute(letters: CharArray) {
    val left = steps % letters.size

    if (left == 0 || left == letters.size) {
      return
    }

    val t = CharArray(left)
    letters.copyInto(t, 0, 0, left)

    for (i in left..<letters.size) {
      letters[i - left] = letters[i]
    }

    t.copyInto(letters, letters.size - left)
  }

  override fun inverse(letters: CharArray) {
    RotateRight(steps).execute(letters)
  }
}

/**
 * Rotates the character in the array to the right the specified number of [steps].
 *
 * For example, suppose you start with abcde.
 *
 * - rotate right 1-step shifts all letters right one position, causing the last letter
 * to wrap to the start of the string: eabcd.
 *
 * The inverse of this operation is [RotateLeft].
 *
 * @param steps The number of steps to rotate.
 */
class RotateRight(val steps: Int) : StringOperation {
  override fun execute(letters: CharArray) {
    val right = steps % letters.size

    if (right == 0 || right == letters.size) {
      return
    }

    val t = CharArray(right)
    letters.copyInto(t, 0, letters.size - right)

    for (i in letters.size - 1 - right downTo 0) {
      letters[i + right] = letters[i]
    }

    t.copyInto(letters)
  }

  override fun inverse(letters: CharArray) {
    RotateLeft(steps).execute(letters)
  }
}

/**
 * Rotate based on position of the specified [letter] means that the whole string should
 * be **rotated to the right** based on the **index** of the specified letter (counting from 0)
 * as determined **before** this instruction does any rotations. Once the index is
 * determined, rotate the string to the right one time, plus a number of times equal
 * to that index, plus one additional time if the index was at least 4.
 *
 * For example, suppose you start with abdec:
 *
 * - Rotate based on position of letter b finds the index of letter b (1), then rotates
 * the string right once plus a number of times equal to that index (2): ecabd.
 * - Then rotate based on position of letter d finds the index of letter d (4), then rotates
 * the string right once, plus a number of times equal to that index, plus an additional
 * time because the index was at least 4, for a total of 6 right rotations: decab.
 *
 * The inverse of this operation only works on character arrays of length equal to **eight (8)**.
 * The inverse rotates the characters back into their positions.
 *
 * @param letter The letter to base the rotation on.
 * @throws IllegalStateException If the letter cannot be found or if inverse is called and the
 * character array is **not exactly eight (8)** characters long.
 */
data class RotateBasedOnPosition(val letter: Char) : StringOperation {
  private companion object {
    private val INV_MAP = mutableMapOf<Int, Int>().apply {
      put(0, 7)
      put(1, 7)
      put(2, 2)
      put(3, 6)
      put(4, 1)
      put(5, 5)
      put(6, 0)
      put(7, 4)
    }
  }

  override fun execute(letters: CharArray) {
    val index = executeInternal(letters)
    var steps = 1 + index
    if (index >= 4) {
      steps++
    }
    RotateRight(steps).execute(letters)
  }

  override fun inverse(letters: CharArray) {
    val index = executeInternal(letters)
    val steps = INV_MAP[index] ?: error("Unexpected password length!")
    RotateRight(steps).execute(letters)
  }

  private fun executeInternal(letters: CharArray): Int {
    var index = -1

    for (i in letters.indices) {
      if (letters[i] == letter) {
        index = i
        break
      }
    }
    check(index >= 0) { "Unable to find index of $letter" }
    return index
  }
}

/**
 * The span of letters at indexes [start] through [end] (including the letters at [start] and
 * [end]) should be **reversed in order.**
 *
 * For example, suppose you start with edcba:
 *
 * - Reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
 *
 * The inverse of this operation is simply this operation.
 *
 * @param start The starting index.
 * @param end The ending index.
 */
data class ReversePositions(val start: Int, val end: Int) : StringOperation {
  override fun execute(letters: CharArray) {
    var min = min(start, end)
    var max = max(start, end)

    while (min < max) {
      val t = letters[max]
      letters[max] = letters[min]
      letters[min] = t
      min++
      max--
    }
  }
}

/**
 * The letter which is at index [source] should be **removed** from the string,
 * then **inserted** such that it ends up at index [destination].
 *
 * For example, suppose you start with bcdea:
 *
 * - Move source 1 to destination 4 removes the letter at position 1 (c), then inserts
 * it at position 4 (the end of the string): bdeac.
 *
 * The inverse of this operation performs a move but with the source and destination
 * swapped.
 *
 * @param source The index of the character to move.
 * @param destination The index to move the character at [source] to.
 */
data class MovePositions(val source: Int, val destination: Int) : StringOperation {
  override fun execute(letters: CharArray) {
    val t = letters[source]

    if (destination > source) {
      // move everything from source + 1 through destination to the left
      for (i in source + 1..destination) {
        letters[i - 1] = letters[i]
      }
    } else {
      // move everything from destination through source - 1 to the right
      for (i in source - 1 downTo destination) {
        letters[i + 1] = letters[i]
      }
    }

    letters[destination] = t
  }

  override fun inverse(letters: CharArray) {
    MovePositions(destination, source).execute(letters)
  }
}
