package com.capital7software.aoc.lib.computer

/**
 * A debugger program here is having an issue: it is trying to repair a memory reallocation
 * routine, but it keeps getting stuck in an infinite loop.
 *
 * In this area, there are sixteen memory banks; each memory bank can hold
 * any number of **blocks.** The goal of the reallocation routine is to balance
 * the blocks between the memory banks.
 *
 * The reallocation routine operates in cycles. In each cycle, it finds the memory bank
 * with the most blocks (ties won by the lowest-numbered memory bank) and redistributes
 * those blocks among the banks. To do this, it removes all of the blocks from the
 * selected bank, then moves to the next (by index) memory bank and inserts one of
 * the blocks. It continues doing this until it runs out of blocks; if it reaches the
 * last memory bank, it wraps around to the first one.
 *
 * The debugger would like to know how many redistributions can be done before a
 * blocks-in-banks configuration is produced that **has been seen before.**
 *
 * For example, imagine a scenario with only four memory banks:
 *
 * - The banks start with 0, 2, 7, and 0 blocks. The third bank has the most blocks, so
 * it is chosen for redistribution.
 * - Starting with the next bank (the fourth bank) and then continuing to the first bank,
 * the second bank, and so on, the 7 blocks are spread out over the memory banks.
 * The fourth, first, and second banks get two blocks each, and the third bank gets one back.
 * The final result looks like this: 2 4 1 2.
 * - Next, the second bank is chosen because it contains the most blocks (four).
 * Because there are four memory banks, each gets one block. The result is: 3 1 2 3.
 * - Now, there is a tie between the first and fourth memory banks, both of which have
 * three blocks. The first bank wins the tie, and its three blocks are distributed evenly
 * over the other three banks, leaving it with none: 0 2 3 4.
 * - The fourth bank is chosen, and its four blocks are distributed such that each of the
 * four banks receives one: 1 3 4 1.
 * - The third bank is chosen, and the same thing happens: 2 4 1 2.
 *
 * At this point, we've reached a state we've seen before: 2 4 1 2 was already seen.
 * The infinite loop is detected after the fifth block redistribution cycle, and so the
 * answer in this example is 5.
 *
 * @param blockString The [String] in tab delimited format where each integer is the number of
 * blocks in that bank.
 */
class MemoryReallocation(blockString: String) {
  private val banks: Map<Int, Int> = blockString.split("\\s+".toRegex())
      .withIndex()
      .associate { it.index to it.value.clean().toInt() }

  /**
   * The number of redistribution cycles that need to be performed to detect an infinite loop
   * is the first element of the pair. The second element is the state that caused the detection.
   */
  val cycleCount: Pair<Int, String> by lazy { countRedistributionCycles(banks) }

  /**
   * The length of the infinite loop cycle.
   */
  val cyclesInLoop: Pair<Int, String> by lazy {
    val temp = countRedistributionCycles(stateToMap(cycleCount.second), cycleCount.second)
    Pair(temp.first - 1, temp.second)
  }

  private fun countRedistributionCycles(
      map: Map<Int, Int>,
      startState: String? = null
  ): Pair<Int, String> {
    var answer = Pair(-1, "")
    val states: MutableSet<String> = mutableSetOf<String>().apply {
      if (startState != null) {
        add(startState)
      }
    }

    val temp = mutableMapOf<Int, Int>().apply { putAll(map) }

    var done = false

    while (!done) {
      val currentEntry = getMaxEntry(temp)
      var id = currentEntry.key
      val count = currentEntry.value
      temp[id] = 0 // clear it out as we are going to redistribute it
      for (i in 0..<count) {
        id = (id + 1) % temp.size
        temp[id] = temp[id]!! + 1
      }

      val state = toState(temp)
      if (!states.add(state)) {
        answer = Pair(states.size + 1, state)
        done = true
      }
    }

    return answer
  }

  private fun getMaxEntry(map: Map<Int, Int>): Map.Entry<Int, Int> {
    var max: Map.Entry<Int, Int>? = null

    map.forEach { entry ->
      val currentMax = max
      if (currentMax == null || entry.value > currentMax.value) {
        max = entry
      } else if (entry.value == currentMax.value && entry.key < currentMax.key) {
        max = entry
      }
    }

    return max ?: error("Unable to find find a maximum entry!")
  }

  private fun toState(map: Map<Int, Int>): String {
    val builder = StringBuilder()

    for (i in 0..<map.size) {
      if (i != 0) {
        builder.append(",")
      }
      builder.append(map[i])
    }

    return builder.toString()
  }

  private fun stateToMap(state: String): Map<Int, Int> {
    return state.split(",").withIndex().associate { it.index to it.value.toInt() }
  }
}
