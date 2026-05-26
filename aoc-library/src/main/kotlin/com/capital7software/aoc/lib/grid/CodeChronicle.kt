package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Point2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings


/**
 * Analyzes lock and key schematics for Advent of Code 2024 Day 25.
 *
 * Schematics are rectangular grids separated by blank lines. Locks have a filled top row and an
 * empty bottom row. Keys have an empty top row and a filled bottom row.
 *
 * A lock and key fit if, for every column, their heights do not overlap:
 *
 * `lockHeight[column] + keyHeight[column] <= availableSpace`
 *
 * where `availableSpace` is the number of interior rows between the top and bottom boundary rows.
 */
class CodeChronicle private constructor(
    private val locks: List<Schematic>,
    private val keys: List<Schematic>,
    private val availableSpace: Int,
) {
  /**
   * Companion object for parsing a code chronicle.
   */
  @SuppressFBWarnings
  companion object {
    private const val FILLED = '#'
    private const val EMPTY = '.'
    private const val COMPLETE_MESSAGE = "Chronicle delivered"

    /**
     * Parses the puzzle input into a [CodeChronicle].
     *
     * @param input The schematic input.
     * @return A parsed [CodeChronicle].
     */
    fun parse(input: List<String>): CodeChronicle {
      val blocks = mutableListOf<List<String>>()
      val current = mutableListOf<String>()

      input.forEach { line ->
        if (line.isBlank()) {
          if (current.isNotEmpty()) {
            blocks.add(current.toList())
            current.clear()
          }
        } else {
          current.add(line)
        }
      }

      if (current.isNotEmpty()) {
        blocks.add(current.toList())
      }

      require(blocks.isNotEmpty()) { "At least one schematic is required!" }

      val schematics = blocks.map { Schematic.parse(it) }
      val locks = schematics.filter { it.type == SchematicType.LOCK }
      val keys = schematics.filter { it.type == SchematicType.KEY }
      val availableSpace = schematics.first().availableSpace

      require(schematics.all { it.availableSpace == availableSpace }) {
        "All schematics must have the same available space!"
      }

      return CodeChronicle(locks, keys, availableSpace)
    }
  }

  /**
   * Counts the unique lock/key pairs that fit without overlapping in any column.
   *
   * @return The number of fitting lock/key pairs.
   */
  fun fittingPairCount(): Long {
    var answer = 0L

    for (lock in locks) {
      for (key in keys) {
        if (lock.fits(key, availableSpace)) {
          answer++
        }
      }
    }

    return answer
  }

  /**
   * Day 25 has no additional computational Part 2. Solving Part 1 completes the chronicle.
   *
   * @return A stable completion message for Part 2.
   */
  fun completeChronicle(): String = COMPLETE_MESSAGE

  private enum class SchematicType {
    LOCK,
    KEY,
  }

  private data class Schematic(
      val type: SchematicType,
      val heights: List<Int>,
      val availableSpace: Int,
  ) {
    companion object {
      @SuppressFBWarnings
      fun parse(lines: List<String>): Schematic {
        require(lines.isNotEmpty()) { "A schematic cannot be empty!" }

        val width = lines.first().length
        val height = lines.size

        require(lines.all { it.length == width }) { "All schematic rows must be the same width!" }
        require(height >= 2) { "A schematic must include boundary rows!" }

        val grid = Grid2D(
            width,
            height,
            lines.flatMap { line -> line.toList() }.toTypedArray()
        )
        val top = grid.getRow(0)
        val bottom = grid.getRow(height - 1)

        val type = when {
          top.all { it == FILLED } && bottom.all { it == EMPTY } -> SchematicType.LOCK
          top.all { it == EMPTY } && bottom.all { it == FILLED } -> SchematicType.KEY
          else -> error("Unable to classify schematic as lock or key!")
        }

        val heights = (0 until width).map { x ->
          (1 until height - 1).count { y ->
            grid[Point2D(x, y)] == FILLED
          }
        }

        return Schematic(
            type = type,
            heights = heights,
            availableSpace = height - 2,
        )
      }
    }

    fun fits(other: Schematic, availableSpace: Int): Boolean {
      require(heights.size == other.heights.size) {
        "Schematics must have the same number of columns!"
      }

      return heights.indices.all { index ->
        heights[index] + other.heights[index] <= availableSpace
      }
    }
  }
}
