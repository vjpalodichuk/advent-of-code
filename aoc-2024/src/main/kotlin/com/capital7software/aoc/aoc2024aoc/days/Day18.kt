package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.grid.RamRun
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.system.measureNanoTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * ***--- Day 18: RAM Run ---***
 *
 * You and The Historians look a lot more pixelated than you remember. You're
 * [inside a computer](https://adventofcode.com/2017/day/2) at the North Pole!
 *
 * Just as you're about to check out your surroundings, a program runs up to you. "This region of
 * memory isn't safe! The User misunderstood what a
 * [pushdown automaton](https://en.wikipedia.org/wiki/Pushdown_automaton) is and their algorithm
 * is pushing whole **bytes** down on top of us! Run!"
 *
 * The algorithm is fast - it's going to cause a byte to fall into your memory space once every
 * [nanosecond](https://www.youtube.com/watch?v=9eyFDBPk4Yw)! Fortunately, you're **faster**, and
 * by quickly scanning the algorithm, you create a **list of which bytes will fall** (your puzzle
 * input) in the order they'll land in your memory space.
 *
 * Your memory space is a two-dimensional grid with coordinates that range from `0` to `70` both
 * horizontally and vertically. However, for the sake of example, suppose you're on a smaller
 * grid with coordinates that range from `0` to `6` and the following list of incoming byte positions:
 *
 * ```
 * 5,4
 * 4,2
 * 4,5
 * 3,0
 * 2,1
 * 6,3
 * 2,4
 * 1,5
 * 0,6
 * 3,3
 * 2,6
 * 5,1
 * 1,2
 * 5,5
 * 2,5
 * 6,5
 * 1,4
 * 0,4
 * 6,4
 * 1,1
 * 6,1
 * 1,0
 * 0,5
 * 1,6
 * 2,0
 * ```
 *
 * Each byte position is given as an `X,Y` coordinate, where `X` is the distance from the left edge
 * of your memory space and `Y` is the distance from the top edge of your memory space.
 *
 * You and The Historians are currently in the top left corner of the memory space (at `0,0`) and
 * need to reach the exit in the bottom right corner (at `70,70` in your memory space, but at `6,6`
 * in this example). You'll need to simulate the falling bytes to plan out where it will be safe to
 * run; for now, simulate just the first few bytes falling into your memory space.
 *
 * As bytes fall into your memory space, they make that coordinate **corrupted**. Corrupted memory
 * coordinates cannot be entered by you or The Historians, so you'll need to plan your route
 * carefully. You also cannot leave the boundaries of the memory space; your only hope is to reach
 * the exit.
 *
 * In the above example, if you were to draw the memory space after the first `12` bytes have
 * fallen (using `.` for safe and `#` for corrupted), it would look like this:
 *
 * ```
 * ...#...
 * ..#..#.
 * ....#..
 * ...#..#
 * ..#..#.
 * .#..#..
 * #.#....
 * ```
 *
 * You can take steps up, down, left, or right. After just 12 bytes have corrupted locations in
 * your memory space, the shortest path from the top left corner to the exit would take **`22`**
 * steps. Here (marked with `O`) is one such path:
 *
 * ```
 * OO.#OOO
 * .O#OO#O
 * .OOO#OO
 * ...#OO#
 * ..#OO#.
 * .#.O#..
 * #.#OOOO
 * ```
 *
 * Simulate the first kilobyte (`1024` bytes) falling onto your memory space. Afterward,
 * **what is the minimum number of steps needed to reach the exit?**
 *
 * Your puzzle answer was **`272`**.
 *
 * **--- Part Two ---**
 *
 * The Historians aren't as used to moving around in this pixelated universe as you are. You're
 * afraid they're not going to be fast enough to make it to the exit before the path is
 * completely blocked.
 *
 * To determine how fast everyone needs to go, you need to determine the **first byte that will cut
 * off the path to the exit**.
 *
 * In the above example, after the byte at `1,1` falls, there is still a path to the exit:
 *
 * ```
 * O..#OOO
 * O##OO#O
 * O#OO#OO
 * OOO#OO#
 * ###OO##
 * .##O###
 * #.#OOOO
 * ```
 * 
 * However, after adding the very next byte (at `6,1`), there is no longer a path to the exit:
 *
 * ```
 * ...#...
 * .##..##
 * .#..#..
 * ...#..#
 * ###..##
 * .##.###
 * #.#....
 * ```
 * 
 * So, in this example, the coordinates of the first byte that prevents the exit from being 
 * reachable are **`6,1`**.
 *
 * Simulate more of the bytes that are about to corrupt your memory space. **What are the 
 * coordinates of the first byte that will prevent the exit from being reachable from your starting
 * position?** (Provide the answer as two integers separated by a comma with no other characters.)
 *
 * Your puzzle answer was **`16,44`**.
 */
class Day18 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day18::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_18-01.txt"

  override fun runPart1(input: List<String>) {
    var answer: Long
    val elapsed = measureNanoTime {
      answer = minimumStepsToExit(input)
    }
    log.info("$answer is the minimum number of steps needed to reach the exit!")
    logTimings(log, elapsed)
  }

  @SuppressFBWarnings
  override fun runPart2(input: List<String>) {
    lateinit var answer: String
    val elapsed = measureNanoTime {
      answer = firstBlockingByte(input)
    }
    log.info("$answer is the first byte that prevents reaching the exit!")
    logTimings(log, elapsed)
  }

  /**
   * Simulates the configured number of falling bytes and returns the minimum number of steps needed
   * to reach the exit.
   *
   * For the real puzzle input, the memory space is `71 x 71`, and the first `1024` bytes are
   * simulated.
   *
   * @param input The falling byte coordinates in `x,y` form.
   * @param width The width of the memory space.
   * @param height The height of the memory space.
   * @param bytesToSimulate The number of bytes to simulate before pathfinding.
   * @return The minimum number of steps needed to reach the exit, or `-1` if unreachable.
   */
  fun minimumStepsToExit(
      input: List<String>,
      width: Int = 71,
      height: Int = 71,
      bytesToSimulate: Int = 1024
  ): Long {
    return RamRun.load(
        input = input,
        width = width,
        height = height,
        bytesToSimulate = bytesToSimulate
    ).minimumStepsToExit().toLong()
  }

  /**
   * Returns the coordinate of the first falling byte that prevents the exit from being reachable.
   *
   * @param input The falling byte coordinates in `x,y` form.
   * @param width The width of the memory space.
   * @param height The height of the memory space.
   * @return The first blocking byte coordinate as `x,y`.
   */
  fun firstBlockingByte(
      input: List<String>,
      width: Int = 71,
      height: Int = 71
  ): String {
    return RamRun.load(
        input = input,
        width = width,
        height = height
    ).firstBlockingByteCoordinateOrEmpty()
  }
}
