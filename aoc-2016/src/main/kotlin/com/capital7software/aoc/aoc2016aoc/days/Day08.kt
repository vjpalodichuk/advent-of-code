package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.grid.LightGrid
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * --- Day 8: Two-Factor Authentication ---
 *
 * You come across a door implementing what you can only assume is an implementation of
 * two-factor authentication after a long game of requirements telephone.
 *
 * To get past the door, you first swipe a keycard (no problem; there was one on a
 * nearby desk). Then, it displays a code on a little screen, and you type that code
 * on a keypad. Then, presumably, the door unlocks.
 *
 * Unfortunately, the screen has been smashed. After a few minutes, you've taken
 * everything apart and figured out how it works. Now you just have to work out what
 * the screen **would** have displayed.
 *
 * The magnetic strip on the card you swiped encodes a series of instructions for the
 * screen; these instructions are your puzzle input. The screen is **50 pixels wide and
 * 6 pixels tall**, all of which start off, and is capable of three somewhat peculiar
 * operations:
 *
 * - rect AxB turns **on** all the pixels in a rectangle at the top-left of the screen
 * which is A wide and B tall.
 * - rotate row y=A by B shifts all the pixels in row A (0 is the top row) **right** by
 * B pixels. Pixels that would fall off the right end appear at the left end of the row.
 * - rotate column x=A by B shifts all the pixels in column A (0 is the left column)
 * **down** by B pixels. Pixels that would fall off the bottom appear at the top of the column.
 *
 * For example, here is a simple sequence on a smaller screen:
 *
 * Creates a small rectangle in the top-left corner:
 *     rect 3x2
 *
 *     ###....
 *     ###....
 *     .......
 *
 * Rotates the second column down by one pixel:
 *     rotate column x=1 by 1
 *
 *     #.#....
 *     ###....
 *     .#.....
 *
 * Rotates the top row right by four pixels:
 *     rotate row y=0 by 4
 *
 *     ....#.#
 *     ###....
 *     .#.....
 *
 * Again rotates the second column down by one pixel,
 * causing the bottom pixel to wrap back to the top:
 *     rotate column x=1 by 1
 *
 *     .#..#.#
 *     #.#....
 *     .#.....
 *
 * As you can see, this display technology is extremely powerful, and will soon
 * dominate the tiny-code-displaying-screen market. That's what the advertisement
 * on the back of the display tries to convince you, anyway.
 *
 * There seems to be an intermediate check of the voltage used by the display: after you
 * swipe your card, if the screen did work, **how many pixels should be lit?**
 *
 * To begin, get your puzzle input.
 *
 * Your puzzle answer was 116.
 *
 * --- Part Two ---
 *
 * You notice that the screen is only capable of displaying capital letters; in the font
 * it uses, each letter is 5 pixels wide and 6 tall.
 *
 * After you swipe your card, **what code is the screen trying to display?**
 *
 * Your puzzle answer was UPOJFLBCEZ.
 */
class Day08 : AdventOfCodeSolution {
  private companion object {
    private val log: Logger = LoggerFactory.getLogger(Day08::class.java)

    private const val DISPLAY_COLUMNS = 50
    private const val DISPLAY_ROWS = 6
  }

  override fun getDefaultInputFilename(): String {
    return "inputs/input_day_08-01.txt"
  }

  override fun runPart1(input: List<String>) {
    val start = Instant.now()
    val answer = numberOfPixelsThatShouldBeLit(input)
    val end = Instant.now()

    log.info("{} is the number of pixels that should be lit!", answer)
    logTimings(log, start, end)
  }

  override fun runPart2(input: List<String>) {
    val start = Instant.now()
    val answer = getDisplayMessage(input)
    val end = Instant.now()

    log.info("{} is the display message after applying the instructions!", answer)
    logTimings(log, start, end)
  }

  /**
   * Calculates and returns the number of pixels that should be lit based on the
   * specified lighting instructions.
   *
   * @param input The [List] of [String] that are parsed into lighting instructions.
   * @return The number of pixels that should be lit based on the specified [List] of [String]
   * that is parsed into lighting instructions.
   */
  fun numberOfPixelsThatShouldBeLit(input: List<String>): Long {
    val instance = LightGrid.buildFromLightingInstructions(DISPLAY_COLUMNS, DISPLAY_ROWS, input)
    instance.applyInstructions()
    return instance.onLightCount
  }

  /**
   * Calculates and returns the message on the display after applying the specified [List] of
   * [String] instructions to the [LightGrid]
   *
   * @param input The [List] of [String] that make up the instructions to parse.
   * @return The message on the display after applying the specified [List] of [String]
   * instructions to the [LightGrid]
   */
  fun getDisplayMessage(input: List<String>): String {
    val instance = LightGrid.buildFromLightingInstructions(DISPLAY_COLUMNS, DISPLAY_ROWS, input)
    instance.applyInstructions()
    return instance.toString()
  }
}
