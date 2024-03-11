package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.string.clean
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.math.sqrt

/**
 * You find a program trying to generate some art. It uses a strange process that
 * involves repeatedly enhancing the detail of an image through a set of rules.
 *
 * The image consists of a two-dimensional square grid of pixels that are either
 * on (#) or off (.). The program always begins with this pattern:
 *
 * ```
 * .#.
 * ..#
 * ###
 * ```
 *
 * Because the pattern is both 3 pixels wide and 3 pixels tall, it is said to have a **size** of 3.
 *
 * Then, the program repeats the following process:
 *
 * - If the size is evenly divisible by 2, break the pixels up into 2x2 squares, and
 * convert each 2x2 square into a 3x3 square by following the corresponding **enhancement rule**.
 * - Otherwise, the size is evenly divisible by 3; break the pixels up into 3x3 squares, and
 * convert each 3x3 square into a 4x4 square by following the corresponding **enhancement rule**.
 *
 * Because each square of pixels is replaced by a larger one, the image gains pixels
 * and so its **size** increases.
 *
 * The artist's book of enhancement rules is nearby (your puzzle input); however,
 * it seems to be missing rules. The artist explains that sometimes, one must **rotate**
 * or **flip** the input pattern to find a match. (Never rotate or flip the output
 * pattern, though.) Each pattern is written concisely: rows are listed as single
 * units, ordered top-down, and separated by slashes. For example, the following
 * rules correspond to the adjacent patterns:
 *
 * ```
 * ../.#  =  ..
 *           .#
 *
 *                 .#.
 * .#./..#/###  =  ..#
 *                 ###
 *
 *                         #..#
 * #..#/..../#..#/.##.  =  ....
 *                         #..#
 *                         .##.
 * ```
 *
 * When searching for a rule to use, rotate and flip the pattern as necessary.
 * For example, all the following patterns match the same rule:
 *
 * ```
 * .#.   .#.   #..   ###
 * ..#   #..   #.#   ..#
 * ###   ###   ##.   .#.
 * ```
 *
 * Suppose the book contained the following two rules:
 *
 * ```
 * ../.# => ##./#../...
 * .#./..#/### => #..#/..../..../#..#
 * ```
 *
 * As before, the program begins with this pattern:

 * ```
 * .#.
 * ..#
 * ###
 * ```
 *
 * The size of the grid (3) is not divisible by 2, but it is divisible by 3.
 * It divides evenly into a single square; the square matches the second rule, which produces:
 *
 * ```
 * #..#
 * ....
 * ....
 * #..#
 * ```
 *
 * The size of this enhanced grid (4) is evenly divisible by 2, so that rule
 * is used. It divides evenly into four squares:
 *
 * ```
 * #.|.#
 * ..|..
 * --+--
 * ..|..
 * #.|.#
 * ```
 *
 * Each of these squares matches the same rule (../.# => ##./#../...), three of
 * which require some flipping and rotation to line up with the rule. The output
 * for the rule is the same in all four cases:
 *
 * ```
 * ##.|##.
 * #..|#..
 * ...|...
 * ---+---
 * ##.|##.
 * #..|#..
 * ...|...
 * ```
 *
 * Finally, the squares are joined into a new grid:
 *
 * ```
 * ##.##.
 * #..#..
 * ......
 * ##.##.
 * #..#..
 * ......
 * ```
 *
 * Thus, after 2 iterations, the grid contains 12 pixels that are **on**.
 *
 */
class FractalArt private constructor() {
  private companion object {
    private const val START_ROWS: Int = 3
    private const val START_COLUMNS: Int = 3
    private const val START_SIZE: Int = START_COLUMNS * START_ROWS
    private val START_GRID: Grid2D<Char> = Grid2D(
        START_COLUMNS,
        START_ROWS,
        Array(START_SIZE) { '#' }
    ).also {
      it[0, 0] = '.'
      it[2, 0] = '.'
      it[0, 1] = '.'
      it[1, 1] = '.'
    }
  }

  private class EnhancementRule(rawRule: String) {
    private val split: List<String> = rawRule.split(" => ")
    private val ruleText: String = split.first().clean()
    private val rule: Grid2D<Char> = rawToGrid(ruleText)
    private val resultText: String = split[1].clean()
    private val originalResult = rawToGrid(resultText)
    val result: Grid2D<Char>
      get() = originalResult.copy()

    private fun rawToGrid(raw: String): Grid2D<Char> {
      val parts = raw.split('/')

      val size = parts.size

      val answer = Grid2D<Char>(
          size,
          size,
          Array(size * size) { '#' }
      ).also {
        parts.withIndex().forEach { (index, value) ->
          it.setRow(index, value.toCharArray().toList())
        }
      }
      return answer
    }

    /**
     * Returns true if the specified pattern matches this rule. If there is
     * no match, then false is returned.
     *
     * @param pattern The pattern to try amd match against this rule.
     * @return True if the pattern matches this rule.
     */
    fun matches(pattern: Grid2D<Char>): Boolean {
      return rule == pattern
    }

    /**
     * Returns true if the specified pattern text matches this rule.
     * If there is no match, then false is returned.
     * <br><br>
     * Pattern text should be in this format: <pre><code>.#./..#/###</code></pre>
     *
     * @param patternText The pattern text to try amd match against this rule.
     * @return True if the patternText matches this rule.
     */
    fun matches(patternText: String): Boolean {
      return ruleText == patternText
    }
  }

  private val rules = mutableListOf<EnhancementRule>()
  private var grid = START_GRID
  private val ruleCache = mutableMapOf<Grid2D<Char>, Grid2D<Char>>()

  /**
   * Instantiates a new [FractalArt] instance loaded with the specified fractal rules.
   *
   * @param input The fractal rules to load and use in the new [FractalArt].
   */
  @SuppressFBWarnings
  constructor(input: List<String>) : this() {
    rules.addAll(input.map { EnhancementRule(it) })
  }


  /**
   * The number of grid cells that are on (#).
   */
  val onCount: Int
    get() = grid.count { it == '#' }

  /**
   * The number of grid cells that are off (.).
   */
  val offCount: Int
    get() = grid.count { it == '.' }

  /**
   * Resets this [FractalArt] to its starting state.
   */
  fun reset() {
    grid = START_GRID
  }

  /**
   * Uses the current fractal [Grid2D] as the starting point. Performs the requested number of
   * iterations by applying the matched rule each iteration.
   *
   * After all iterations are performed, this [FractalArt]'s grid is set to be the result of the
   * last iteration.
   *
   * During each iteration, the following is performed:
   *
   * - If the grid size is evenly divisible by 2, break the pixels up into 2x2 squares, and
   * convert each 2x2 square into a 3x3 square by following the corresponding **enhancement rule**.
   * - Otherwise, the grid size is evenly divisible by 3; break the pixels up into 3x3 squares, and
   * convert each 3x3 square into a 4x4 square by following the corresponding **enhancement rule**.
   *
   * Since 2x2 and 3x3 grids can have only so many patterns, rule matches are cached so that future
   * iterations perform the rule match process faster.
   *
   * @param iterations The number of iterations to perform.
   * @throws IllegalStateException Thrown when no rule match can be made to a fractal pattern. The
   * pattern without a match is in the exception message.
   */
  fun generate(iterations: Int = 1) {
    var temp = grid

    for (i in 0..<iterations) {
      temp = if (temp.columns % 2 == 0) {
        processGrid(temp, 2)
      } else {
        processGrid(temp, 3)
      }
    }
    grid = temp
  }

  /**
   * Prints this [FractalArt] as a formatted [String].
   *
   * @return This [FractalArt] as a formatted [String].
   */
  fun print(): String {
    return grid.print()
  }

  /**
   * Breaks the specified grid up into multiple XxX grids and then for each XxX grid, finds a rule
   * match that turns the XxX grid into a YxY grid.
   *
   * After all XxX grids have been converted to YxY grids, the YxY grids are combined into one
   * grid and returned.
   *
   * @param target The grid, whose size must be evenly divisible by divisor,
   * to morph into a new grid.
   * @param divisor The divisor to use to break the grid up into. The columns and rows
   * of the grid must be evenly divisible by the divisor.
   * @return A larger grid based on the rule matches of the XxX grids.
   */
  private fun processGrid(target: Grid2D<Char>, divisor: Int): Grid2D<Char> {
    var y = 0
    val larger = mutableListOf<Grid2D<Char>>()

    while (y < target.rows) {
      var x = 0
      while (x < target.columns) {
        val items = target[x, y, divisor, divisor]
        val newGrid = Grid2D(divisor, divisor, items.toTypedArray())
        larger.add(matchRule(newGrid))
        x += divisor
      }
      y += divisor
    }

    // All decomposing and rule matching is complete. Now just stitch together a new grid!
    val first = larger.first()

    // Since the grid is always square, the size is simply the square root of the number of
    // items from the larger list.
    val size = first.columns * sqrt(larger.size.toDouble()).toInt()
    val items = Array(size * size) { '.' }
    val newGrid = Grid2D<Char>(size, size, items)

    y = 0
    var i = 0

    while (y < newGrid.rows) {
      var x = 0
      while (i < larger.size && x < newGrid.columns) {
        newGrid[x, y, first.columns, first.rows] = larger[i].items
        x += first.columns
        i++
      }
      y += first.rows
    }

    return newGrid
  }

  private fun matchRule(target: Grid2D<Char>): Grid2D<Char> {
    val match: Grid2D<Char>? = ruleCache[target]

    return if (match != null) {
      match
    } else {
      val tries = mutableSetOf<Grid2D<Char>>()
      var ruleMatch = rules.firstOrNull { it.matches(target) }

      if (ruleMatch != null) {
        ruleCache[target] = ruleMatch.result
      } else {
        tries.add(target)

        // We need to flip horizontally, then vertically, then rotate right until we find a match.
        var done = false
        var current = target
        var result: Grid2D<Char>? = null

        while (!done) {
          val tryCount = tries.size

          var next = current.flipHorizontal()
          ruleMatch = rules.firstOrNull { it.matches(next) }
          tries.add(next)

          if (ruleMatch != null) {
            result = ruleMatch.result
            done = true
          } else {
            next = current.flipVertical()
            ruleMatch = rules.firstOrNull { it.matches(next) }
            tries.add(next)
            if (ruleMatch != null) {
              result = ruleMatch.result
              done = true
            } else {
              next = current.rotateRight()
              ruleMatch = rules.firstOrNull { it.matches(next) }
              tries.add(next)
              if (ruleMatch != null) {
                result = ruleMatch.result
                done = true
              } else {
                current = next
              }
            }
          }

          if (tryCount == tries.size) {
            done = true
          }
        }

        if (result != null) {
          tries.forEach { ruleCache[it] = result }
        }
      }

      ruleCache[target] ?: error("Unable to find a rule match for ${target.toPatternText()}")
    }
  }
}

/**
 * Converts this grid into a pattern text in this format: <pre><code>.#./..#/###</code></pre>
 *
 * @return This grid in pattern text format.
 */
fun Grid2D<Char>.toPatternText(): String {
  val answer = StringBuilder()

  for (i in 0..<rows) {
    getRow(i).forEach { answer.append(it) }
    if (i + 1 < rows) {
      answer.append("/")
    }
  }

  return answer.toString()
}
