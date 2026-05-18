package com.capital7software.aoc.lib.util

/**
 * The epsilon value used for floating point comparisons.
 */
const val EPSILON = 0.00000001

/**
 * Determines if two floating point values are nearly equal.
 */
infix fun Double.isNearlyEqual(other: Double): Boolean {
  return kotlin.math.abs(this - other) < EPSILON
}
