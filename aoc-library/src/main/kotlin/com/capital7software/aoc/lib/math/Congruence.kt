package com.capital7software.aoc.lib.math

/**
 * A mathematical congruence equation where x modulo modulus = remainder.
 *
 * @param remainder The remainder of the modulo operation.
 * @param modulus The modulus of the modulo operation.
 */
data class Congruence<T : Number>(val remainder: T, val modulus: T) {
  companion object {
    private const val DEFAULT_MAX_TRIES = 10_000
  }

  /**
   * Returns true if the specified value satisfies this congruence.
   *
   * @param x The value to test.
   */
  private fun satisfies(x: T) = MathOperations.mod(x, modulus) == remainder

  /**
   * Finds a value that satisfies this [Congruence] and the
   * other specified [Congruence] and returns a new [Congruence] that is
   * mathematically equivalent to this [Congruence] and the other specified
   * [Congruence]. If no solution found within [maxTries], null is returned.
   *
   * The process for solving is as follows:
   *
   * - start with the [remainder] of this [Congruence] and use it as the x value of the other
   * congruence and increase x by modulus until an x value is found that satisfies the other
   * congruence.
   * - If no x value found after [maxTries] return null.
   * - Else, once a valid x value is found, calculate the LCM of the modulus of this congruence and
   * the other congruence.
   * - Create and return a new [Congruence] with the found x value as the remainder and the
   * LCM as the modulus.
   *
   * @param other The other [Congruence] to use in finding a solution.
   * @param maxTries The maximum number of tries to perform before giving up on finding a solution.
   * @return A new [Congruence] that is equivalent to the combination of both congruences or
   * null if there is no solution after maxTries.
   */
  fun solve(other: Congruence<T>, maxTries: Int = DEFAULT_MAX_TRIES): Congruence<T>? {
    val x: T? = findX(other, maxTries)
    return when (x) {
      null -> null
      else -> {
        val lcm: T = MathOperations.lcm(modulus, other.modulus)
        val newCongruence = Congruence(x, lcm)
        newCongruence
      }
    }
  }

  private fun findX(other: Congruence<T>, maxTries: Int = DEFAULT_MAX_TRIES): T? {
    var x: T? = null
    var testX: T = remainder
    var i = 0

    while (x == null && i <= maxTries) {
      x = if (other.satisfies(testX)) testX else {
        testX = MathOperations.add(testX, modulus)
        null
      }
      i++
    }
    return x
  }
}
