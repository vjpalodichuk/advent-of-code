package com.capital7software.aoc.lib.analysis

/**
 * Suddenly, whirling in the distance, you notice what looks like a massive, pixelated
 * hurricane: a deadly spinlock. This spinlock isn't just consuming computing power,
 * but memory, too; vast, digital mountains are being ripped from the ground and
 * consumed by the vortex.
 *
 * If you don't move quickly, fixing that printer will be the least of your problems.
 *
 * This spinlock's algorithm is simple but efficient, quickly consuming everything in
 * its path. It starts with a circular buffer containing only the value 0, which it
 * marks as the **current position**. It then steps forward through the circular buffer
 * some number of steps (your puzzle input) before inserting the first new value, 1,
 * after the value it stopped on. The inserted value becomes the **current position**.
 * Then, it steps forward from there the same number of steps, and wherever it stops,
 * inserts after it the second new value, 2, and uses that as the new **current position**
 * again.
 *
 * It repeats this process of **stepping forward, inserting a new value**, and **using the
 * location of the inserted value as the new current position** a total of 2017 times,
 * inserting **2017** as its final operation, and ending with a total of 2018 values
 * (including 0) in the circular buffer.
 *
 * For example, if the spinlock were to step 3 times per insert, the circular buffer
 * would begin to evolve like this (using parentheses to mark the current position
 * after each iteration of the algorithm):
 *
 * - (0), the initial state before any insertions.
 * - 0 (1): the spinlock steps forward three times (0, 0, 0), and then inserts the
 * - first value, 1, after it. 1 becomes the current position.
 * - 0 (2) 1: the spinlock steps forward three times (0, 1, 0), and then inserts the
 * - second value, 2, after it. 2 becomes the current position.
 * - 0  2 (3) 1: the spinlock steps forward three times (1, 0, 2), and then inserts
 * the third value, 3, after it. 3 becomes the current position.
 *
 * And so on:
 *
 * ```
 * 0  2 (4) 3  1
 * 0 (5) 2  4  3  1
 * 0  5  2  4  3 (6) 1
 * 0  5 (7) 2  4  3  6  1
 * 0  5  7  2  4  3 (8) 6  1
 * 0 (9) 5  7  2  4  3  8  6  1
 * ```
 *
 * Eventually, after 2017 insertions, the section of the circular buffer near the
 * last insertion looks like this:
 *
 * ```
 * 1512  1134  151 (2017) 638  1513  851
 * ```
 *
 * Perhaps, if you can identify the value that will ultimately be **after** the last
 * value written (2017), you can short-circuit the spinlock. In this example,
 * that would be 638.
 *
 * @param steps The number of steps forward after each insert.
 * @param bufferSize The number of values to insert.
 */
class SpinLock(private val steps: Int, private val bufferSize: Int = DEFAULT_BUFFER_SIZE) {
  private companion object {
    private const val DEFAULT_BUFFER_SIZE: Int = 2018
  }

  private val buffer: Pair<List<Int>, Int> by lazy { buildBuffer() }

  /**
   * The number after the last one written.
   */
  val numberAfterLastWritten: Int by lazy { buffer.second }

  private fun buildBuffer(): Pair<List<Int>, Int> {
    val answer = mutableListOf<Int>()
    answer.add(0)
    var position = 0

    while (answer.size < bufferSize) {
      position = (position + steps + 1) % answer.size
      answer.add(position, answer.size)

    }

    return Pair(answer, answer[(position + 1) % answer.size])
  }

  /**
   * Returns the value after the [target] value in the buffer.
   *
   * @param target The target value to find the next value after.
   * @return The value after the [target] value in the buffer.
   */
  fun valueAfter(target: Int): Int {
    check(target in 0..<bufferSize) { "$target is not in buffer!" }

    var last: Int = -1
    var position = 0

    for (i in 1 .. bufferSize) {
      position = (position + steps) % i
      if (position++ == 0) {
        last = i
      }
    }

    return last
  }
}
