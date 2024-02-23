package com.capital7software.aoc.lib.math

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * The halls open into an interior plaza containing a large kinetic sculpture. The sculpture
 * is in a sealed enclosure and seems to involve a set of identical spherical capsules that
 * are carried to the top and allowed to bounce through the maze of spinning pieces.
 *
 * Part of the sculpture is even interactive! When a button is pressed, a capsule is
 * dropped and tries to fall through slots in a set of rotating discs to finally go
 * through a little hole at the bottom and come out of the sculpture. If any of the
 * slots aren't aligned with the capsule as it passes, the capsule bounces off the
 * disc and soars away. You feel compelled to get one of those capsules.
 *
 * The discs pause their motion each second and come in different sizes; they seem
 * to each have a fixed number of positions at which they stop. You decide to call
 * the position with the slot 0, and count up for each position it reaches next.
 *
 * Furthermore, the discs are spaced out so that after you push the button, one
 * second elapses before the first disc is reached, and one second elapses as
 * the capsule passes from one disc to the one below it. So, if you push the
 * button at time=100, then the capsule reaches the top disc at time=101, the
 * second disc at time=102, the third disc at time=103, and so on.
 *
 * The button will only drop a capsule at an integer time - no fractional
 * seconds allowed.
 *
 * For example, at time=0, suppose you see the following arrangement:
 *
 * ```
 * Disc #1 has 5 positions; at time=0, it is at position 4.
 * Disc #2 has 2 positions; at time=0, it is at position 1.
 * ```
 *
 * If you press the button exactly at time=0, the capsule would start to fall; it
 * would reach the first disc at time=1. Since the first disc was at position 4
 * at time=0, by time=1 it has ticked one position forward. As a five-position disc,
 * the next position is 0, and the capsule falls through the slot.
 *
 * Then, at time=2, the capsule reaches the second disc. The second disc has ticked
 * forward two positions at this point: it started at position 1, then continued to
 * position 0, and finally ended up at position 1 again. Because there's only a
 * slot at position 0, the capsule bounces away.
 *
 * If, however, you wait until time=5 to push the button, then when the capsule
 * reaches each disc, the first disc will have ticked forward 5+1 = 6 times
 * (to position 0), and the second disc will have ticked forward 5+2 = 7 times
 * (also to position 0). In this case, the capsule would fall through the discs
 * and come out of the machine.
 *
 * However, your situation has more than two discs; you've noted their positions
 * in your puzzle input. What is the **first time you can press the button**
 * to get a capsule?
 */
class DiscSculpture() {
  companion object {
    private val DISC_REGEX: Regex = ("Disc #(?<order>\\d+) has (?<slots>\\d+) positions; at " +
        "time=0, it is at position (?<initial>\\d+).").toRegex()
    private const val ORDER = "order"
    private const val SLOTS = "slots"
    private const val INITIAL = "initial"
  }

  /**
   * A rotating disc in the sculpture that the capsule must pass through.
   * A capsule is only able to pass through a [Disc] if it reaches the disc
   * when its position is at slot 0 as that is the slot that contains a hole for the
   * capsule to pass through. A disc will pause its motion every whole second before moving on to
   * the next slot.
   *
   * A disc has an order, a number of slots, and an initial position that corresponds to its
   * position at time zero. All calculations to determine the disc's slot position at a time T
   * are relative to the disc's order, number of slots, and starting position.
   *
   * The order of a disc is the 1-based order that the capsule encounters it. So a disc with
   * an order of 1 is the first disc that the capsule must pass through and a disc with an
   * order of 3 is the third disc that the capsule must pass through.
   *
   * The number of slots a disc has determines how many seconds it takes for the disc to
   * complete a full rotation. That is, if a disc has five slots and its initial position
   * is slot 0, then at time T = 0 it will be at slot zero and at time T = 5 it will be at
   * slot zero,
   *
   * The initial position of the disc determines which slot it will be on at time T = 0. The
   * initial position is 0-based and so its value must be between 0 and slots - 1. To
   * determine where the disc will be at time T = t, the initial position is added to t. To
   * determine where the disc will be at time T = t in relation to its order in the sculpture,
   * both the order and initial position are added to t.
   *
   * The general formula is:
   *
   * ``(order + initial + time) modulo slots = current``
   *
   * Where time is when the button was pressed to release the capsule.
   *
   * So to have the capsule come out of the sculpture, all discs in the sculpture need to return
   * 0 when asked for its position at time t.
   *
   * @param order The 1-based position of this [Disc] in the sculpture.
   * @param slots The total number of slots in this [Disc]. Used as the moduli for solving
   * congruences with multiple discs.
   * @param initial The 0-based slot position that this disc is in at time = 0.
   */
  data class Disc(val order: Int, val slots: Int, val initial: Int) {
    /**
     * The modulus coefficient for this Disc. This is used in solving congruences to
     * find a time t that allows the capsule to pass through all discs in the sculpture.
     *
     * ``(order + initial) modulo slots = a```
     *
     * The time t + a modulo slots must equal zero.
     *
     */
    val a: Int by lazy { MathOperations.mod(order + initial, slots) }

    /**
     * A [Congruence] that can be used for finding a time t that causes this disc to be
     * at slot 0 when the capsule reaches it.
     *
     */
    val congruence: Congruence<Int> by lazy { Congruence(a, slots) }

    /**
     * Returns this [Disc] 0-based slot position at the specified time that the
     * button was pressed to release the capsule.
     *
     * @param time The at which the button was pressed to release the capsule.
     * @return The 0-based slot that will be in position at the specified time the
     * button was pressed.
     */
    fun position(time: Int): Int = MathOperations.mod(order + initial + time, slots)

  }

  private val discs: MutableList<Disc> = mutableListOf()

  /**
   * The number of [Disc] in this [DiscSculpture].
   */
  val size: Int
    get() = discs.size

  /**
   * Instantiates a new [DiscSculpture] by parsing the specified [List] of [String] into
   * [Disc].
   *
   * @param input A [List] of [String] in this format:
   * ```
   * Disc #1 has 5 positions; at time=0, it is at position 4.
   * Disc #2 has 2 positions; at time=0, it is at position 1.
   * ```
   */
  @SuppressFBWarnings
  constructor(input: List<String>) : this() {
    input.mapNotNull { DISC_REGEX.find(it) }
        .map {
          Disc(
              it.groups[ORDER]!!.value.toInt(),
              it.groups[SLOTS]!!.value.toInt(),
              it.groups[INITIAL]!!.value.toInt()
          )
        }
        .forEach { add(it) }
  }

  /**
   * Adds a new [Disc] to this sculpture.
   */
  fun add(disc: Disc) {
    discs.add(disc)
  }

  /**
   * Calculates and returns the time that the button should be pressed to get the capsule.
   *
   * If this sculpture is empty, then -1 is returned.
   */
  @SuppressFBWarnings
  fun solve(): Int {
    return if (discs.isEmpty()) {
      return -1
    } else {
      val congruences = discs.map { it.congruence }
      var current: Congruence<Int>? = congruences.first()

      for (i in 1 until congruences.size) {
        current = current?.solve(congruences[i])
      }

      // modulus - remainder = time to push the button!
      (current?.modulus?.minus(current.remainder)) ?: -1
    }
  }
}
