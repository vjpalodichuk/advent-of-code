package com.capital7software.aoc.lib.computer

import com.capital7software.aoc.lib.string.clean

/**
 * Following the twisty passageways deeper and deeper into the CPU, you finally reach the
 * core of the computer. Here, in the expansive central chamber, you find a grand apparatus
 * that fills the entire room, suspended nanometers above your head.
 *
 * You had always imagined CPUs to be noisy, chaotic places, bustling with activity.
 * Instead, the room is quiet, motionless, and dark.
 *
 * Suddenly, you and the CPU's **garbage collector** startle each other. "It's not often we
 * get many visitors here!", he says. You inquire about the stopped machinery.
 *
 * "It stopped milliseconds ago; not sure why. I'm a garbage collector, not a doctor."
 * You ask what the machine is for.
 *
 * "Programs these days, don't know their origins. That's the **Turing machine**! It's
 * what makes the whole computer work." You try to explain that Turing machines are
 * merely models of computation, but he cuts you off. "No, see, that's just what they
 * **want** you to think. Ultimately, inside every CPU, there's a Turing machine driving
 * the whole thing! Too bad this one's broken. We're doomed!"
 *
 * You ask how you can help. "Well, unfortunately, the only way to get the computer running
 * again would be to create a whole new Turing machine from scratch, but there's no **way**
 * you can-" He notices the look on your face, gives you a curious glance, shrugs, and
 * goes back to sweeping the floor.
 *
 * You find the **Turing machine blueprints** (your puzzle input) on a tablet in a nearby
 * pile of debris. Looking back up at the broken Turing machine above, you can
 * start to identify its parts:
 *
 * - A **tape** which contains 0 repeated infinitely to the left and right.
 * - A **cursor**, which can move left or right along the tape and read or write
 * values at its current position.
 * - A set of **states**, each containing rules about what to do based on the
 * current value under the cursor.
 *
 * Each slot on the tape has two possible values: 0 (the starting value for all slots) and 1.
 * Based on whether the cursor is pointing at a 0 or a 1, the current state says
 * **what value to write** at the current position of the cursor, whether to **move the cursor**
 * left or right one slot, and **which state to use next**.
 *
 * For example, suppose you found the following blueprint:
 *
 * ```
 * Begin in state A.
 * Perform a diagnostic checksum after 6 steps.
 *
 * In state A:
 *   If the current value is 0:
 *     - Write the value 1.
 *     - Move one slot to the right.
 *     - Continue with state B.
 *   If the current value is 1:
 *     - Write the value 0.
 *     - Move one slot to the left.
 *     - Continue with state B.
 *
 * In state B:
 *   If the current value is 0:
 *     - Write the value 1.
 *     - Move one slot to the left.
 *     - Continue with state A.
 *   If the current value is 1:
 *     - Write the value 1.
 *     - Move one slot to the right.
 *     - Continue with state A.
 * ```
 *
 * Running it until the number of steps required to take the listed **diagnostic checksum**
 * would result in the following tape configurations (with the **cursor** marked
 * in square brackets):
 *
 * ```
 * ... 0  0  0 [0] 0  0 ... (before any steps; about to run state A)
 * ... 0  0  0  1 [0] 0 ... (after 1 step;     about to run state B)
 * ... 0  0  0 [1] 1  0 ... (after 2 steps;    about to run state A)
 * ... 0  0 [0] 0  1  0 ... (after 3 steps;    about to run state B)
 * ... 0 [0] 1  0  1  0 ... (after 4 steps;    about to run state A)
 * ... 0  1 [1] 0  1  0 ... (after 5 steps;    about to run state B)
 * ... 0  1  1 [0] 1  0 ... (after 6 steps;    about to run state A)
 * ```
 *
 * The CPU can confirm that the Turing machine is working by taking a **diagnostic checksum**
 * after a specific number of steps (given in the blueprint). Once the specified number of
 * steps have been executed, the Turing machine should pause; once it does, count the number of
 * times 1 appears on the tape. In the above example, the **diagnostic checksum** is **3**.
 */
class TuringMachine private constructor() {
  private companion object {
    private val BEGIN_REGEX: Regex = "Begin in state (?<start>\\w+)".toRegex()
    private val CHECKSUM_REGEX: Regex = "checksum after (?<value>\\d+) steps".toRegex()
    private val IN_STATE_REGEX: Regex = "In state (?<state>\\w+):".toRegex()

    //private val CURRENT_VALUE_REGEX: Regex = "current value is (?<value>\\d+):".toRegex()
    private val WRITE_VALUE_REGEX: Regex = "Write the value (?<value>\\d+)".toRegex()
    private val MOVE_SLOT_REGEX: Regex = "slot to the (?<direction>\\w+)".toRegex()
    private val NEXT_STATE_REGEX: Regex = "Continue with state (?<state>\\w+)".toRegex()
  }

  private val states: MutableMap<String, TuringState> = mutableMapOf()
  private var checkAt: Int = 0
  private val tape: TuringTape = TuringTape()
  private var current: TuringState? = null
  private var start: TuringState? = null

  constructor(input: List<String>) : this() {
    var index = 0

    val startState: String = BEGIN_REGEX.find(input[index++])?.groups?.get("start")?.value
        ?: error("Malformed Turing Machine Blueprint!")
    checkAt = CHECKSUM_REGEX.find(input[index++])?.groups?.get("value")?.value?.toInt()
        ?: error("Malformed Turing Machine Blueprint!")
    index++ // Skip the blank line

    while (index < input.size) {
      val stateName = IN_STATE_REGEX
          .find(input[index++])?.groups?.get("state")?.value?.clean()
          ?: error("Malformed Turing Machine Blueprint!")
      index++ // Assume if zero first.
      val writeIfZero = WRITE_VALUE_REGEX
          .find(input[index++])?.groups?.get("value")?.value?.clean()?.toInt()
          ?: error("Malformed Turing Machine Blueprint!")
      val moveIfZero = MOVE_SLOT_REGEX
          .find(input[index++])?.groups?.get("direction")?.value?.clean()
          ?: error("Malformed Turing Machine Blueprint!")
      val nextIfZero = NEXT_STATE_REGEX
          .find(input[index++])?.groups?.get("state")?.value?.clean()
          ?: error("Malformed Turing Machine Blueprint!")
      index++ // Assume if one next.
      val writeIfOne = WRITE_VALUE_REGEX
          .find(input[index++])?.groups?.get("value")?.value?.clean()?.toInt()
          ?: error("Malformed Turing Machine Blueprint!")
      val moveIfOne = MOVE_SLOT_REGEX
          .find(input[index++])?.groups?.get("direction")?.value?.clean()
          ?: error("Malformed Turing Machine Blueprint!")
      val nextIfOne = NEXT_STATE_REGEX
          .find(input[index++])?.groups?.get("state")?.value?.clean()
          ?: error("Malformed Turing Machine Blueprint!")
      index++ // Assume blank line.

      val newState = TuringState(
          stateName,
          writeIfZero,
          moveIfZero == "left",
          nextIfZero,
          writeIfOne,
          moveIfOne == "left",
          nextIfOne
      )
      states[stateName] = newState
    }
    start = states[startState] ?: error("Missing start state!")
    current = start
  }

  /**
   * Resets this [TuringMachine] to its starting state.
   */
  fun reset() {
    current = start
    tape.reset()
  }


  /**
   * Executes this [TuringMachine] until the diagnostic checksum value is computed.
   */
  fun execute(): Int {
    var i = 0

    while (i < checkAt) {
      current = processState(current!!)
      i++
    }
    val answer = tape.count { it == 1 }
    reset()
    return answer
  }

  private fun processState(state: TuringState): TuringState {
    val currentValue = tape.read()

    val toWrite = state.shouldWrite(currentValue)
    val moveLeft = state.shouldMoveLeft(currentValue)
    val nexState = state.shouldContinueWith(currentValue)

    tape.write(toWrite)

    if (moveLeft) {
      tape.moveLeft()
    } else {
      tape.moveRight()
    }

    return states[nexState]!!
  }
}
