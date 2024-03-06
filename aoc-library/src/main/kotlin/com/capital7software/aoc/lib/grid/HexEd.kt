package com.capital7software.aoc.lib.grid

import kotlin.math.abs

/**
 * Crossing the bridge, you've barely reached the other side of the stream when a
 * program comes up to you, clearly in distress. "It's my child process," she says,
 * "he's gotten lost in an infinite grid!"
 *
 * Fortunately for her, you have plenty of experience with infinite grids.
 *
 * Unfortunately for you, it's a hex grid.
 *
 * The hexagons ("hexes") in this grid are aligned such that adjacent hexes can be
 * found to the north, northeast, southeast, south, southwest, and northwest:
 *
 * ```
 *   \ n  /
 * nw +--+ ne
 *   /    \
 * -+      +-
 *   \    /
 * sw +--+ se
 *   / s  \
 * ```
 *
 * You have the path the child process took. Starting where he started, you need to
 * determine the fewest number of steps required to reach him. (A "step" means to
 * move from the hex you are in to any adjacent hex.)
 *
 * For example:
 *
 * - ne,ne,ne is 3 steps away.
 * - ne,ne,sw,sw is 0 steps away (back where you started).
 * - ne,ne,s,s is 2 steps away (se,se).
 * - se,sw,se,sw,sw is 3 steps away (s,s,sw).
 *
 * @param input The comma separated list of steps the child process took.
 */
class HexEd(input: String) {
  private val path: List<String> = input.split(",")

  /**
   * The first element of the [Pair] is the fewest number of steps to reach the child and the
   * second element is the maximum distance from the start along the path.
   */
  val stepsAndDistance: Pair<Int, Int> by lazy { fewestSteps() }

  /**
   * Uses the Cube Coordinates as described at
   * [Red Blob Games](https://www.redblobgames.com/grids/hexagons/) to calculate distances.
   */
  private fun fewestSteps(): Pair<Int, Int> {
    val distances = mutableListOf<Int>()

    var q = 0
    var r = 0
    var s = 0

    for (step in path) {
      when (step) {
        "n" -> {
          s++
          r--
        }

        "s" -> {
          s--
          r++
        }

        "ne" -> {
          q++
          r--
        }

        "sw" -> {
          q--
          r++
        }

        "nw" -> {
          q--
          s++
        }

        "se" -> {
          q++
          s--
        }
      }
      distances.add((abs(q) + abs(s) + abs(r)) / 2)
    }

    return Pair((abs(q) + abs(s) + abs(r)) / 2, distances.max())
  }
}
