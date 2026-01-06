package com.capital7software.aoc.lib.math

import com.capital7software.aoc.lib.geometry.Point2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * A button in a [ClawMachine]. When the button is pressed, it advances in both the `X`
 * and `Y` axis by the amounts specified in the [offsets] property. The number of tokens needed
 * to press the button is specified in the [cost] property.
 *
 * @param label The [String] identifier for this button
 * @param offsets The [Point2D] that specifies how far along the `X` and `Y` axis to advance
 * when this button is pressed
 * @param cost The number of tokens required to press this button
 */
data class ClawButton(val label: String, val offsets: Point2D<Long>, val cost: Int)

/**
 * A Claw style game machine.
 *
 * The claw machines here are a little unusual. Instead of a joystick or directional buttons
 * to control the claw, these machines have two [ClawButton]s labeled A and B. Worse, you can't
 * just put in a token and play; it costs **3 tokens** to push the A button and **1 token**
 * to push the B button.
 *
 * Not every machine can actually win the designated [prize]. If a machine cannot be won its
 * [cost] will be `0`.
 *
 * @param buttonA The A [ClawButton]
 * @param buttonB The B [ClawButton]
 * @param prize The [Point2D] location of the prize
 */
data class ClawMachine(val buttonA: ClawButton, val buttonB: ClawButton, val prize: Point2D<Long>) {
  /**
   * The total number of tokens needed to win the prize. If the prize for this machine can be won,
   * the cost will be greater than `0`
   */
  val cost: Long by lazy {
    val offsetsA = buttonA.offsets
    val offsetsB = buttonB.offsets

    val determinant = offsetsA.x() * offsetsB.y() - offsetsA.y() * offsetsB.x()
    val pressesA = (prize.x() * offsetsB.y() - prize.y() * offsetsB.x()) / determinant
    val pressesB = (prize.y() * offsetsA.x() - prize.x() * offsetsA.y()) / determinant
    val newX = offsetsA.x() * pressesA + offsetsB.x() * pressesB
    val newY = offsetsA.y() * pressesA + offsetsB.y() * pressesB
    val testPoint = Point2D(newX, newY)

    when (testPoint == prize) {
      true -> pressesA * buttonA.cost + pressesB * buttonB.cost
      false -> 0
    }
  }
}

/**
 * Contains the [ClawMachine]s that will be played.
 *
 * @param machines The [List] of [ClawMachine]s this contraption manages
 */
@SuppressFBWarnings
class ClawContraption(val machines: List<ClawMachine>) {
  /**
   * Contains Class level properties and functions
   */
  companion object {
    private val BUTTON_REGEX: Regex = """Button\s+(?<label>\w):\s+X\+(?<x>\d+),\s+Y\+(?<y>\d+)""".toRegex()
    private val PRIZE_REGEX: Regex = """Prize:\s+X=(?<x>\d+),\s+Y=(?<y>\d+)""".toRegex()
    private const val COST_A: Int = 3
    private const val COST_B: Int = 1

    /**
     * Loads each [ClawMachine] specified in [inputs] and returns a new [ClawContraption] that
     * manages them.
     *
     * The format of the machines is a [List] of [String] like in the following example:
     *
     * ```
     * Button A: X+94, Y+34
     * Button B: X+22, Y+67
     * Prize: X=8400, Y=5400
     *
     * Button A: X+26, Y+66
     * Button B: X+67, Y+21
     * Prize: X=12748, Y=12176
     *
     * Button A: X+17, Y+86
     * Button B: X+84, Y+37
     * Prize: X=7870, Y=6450
     *
     * Button A: X+69, Y+23
     * Button B: X+27, Y+71
     * Prize: X=18641, Y=10279
     * ```
     *
     * @param inputs The list of [ClawMachine]s to load into the [ClawContraption]
     * @param prizeOffset An optional offset that can be added to the axis of each prize
     * location.
     */
    fun load(inputs: List<String>, prizeOffset: Long = 0L) : ClawContraption {
      var buttonA: ClawButton? = null
      var buttonB: ClawButton? = null
      var prize: Point2D<Long>? = null
      val machines = mutableListOf<ClawMachine>()

      inputs.forEach { line ->
        var matchResult = BUTTON_REGEX.matchEntire(line)

        if (matchResult != null) {
          val label = matchResult.groups["label"]?.value ?: ""
          val x = matchResult.groups["x"]?.value?.toLong() ?: 0L
          val y = matchResult.groups["y"]?.value?.toLong() ?: 0L
          val point = Point2D(x, y)

          when (label == "A") {
            true -> buttonA = ClawButton(label, point, COST_A)
            else -> buttonB = ClawButton(label, point, COST_B)
          }
        } else {
          matchResult = PRIZE_REGEX.matchEntire(line)

          if (matchResult != null) {
            val x = (matchResult.groups["x"]?.value?.toLong() ?: 0L) + prizeOffset
            val y = (matchResult.groups["y"]?.value?.toLong() ?: 0L) + prizeOffset
            prize = Point2D(x, y)
          }
        }

        if (buttonA != null && buttonB != null && prize != null) {
          machines.add(ClawMachine(buttonA, buttonB, prize))
          buttonA = null
          buttonB = null
          prize = null
        }
      }

      return ClawContraption(machines)
    }
  }

  /**
   * The total cost in tokens to win all possible prizes from all [ClawMachine] this
   * contraction manages.
   */
  val cost: Long by lazy { machines.sumOf { it.cost } }
}
