package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.ArrayDeque

/**
 * Models the race track and calculates how many legal cheats save at least a requested
 * number of picoseconds.
 *
 * Track tiles are '.', 'S', and 'E'. Wall tiles are '#'.
 *
 * A cheat is uniquely identified by the normal track tile where collision is disabled
 * and the normal track tile where collision is re-enabled. During the cheat, walls may
 * be crossed for at most maxCheatDuration moves.
 */
class RaceCondition private constructor(
    private val grid: Grid2D<Char>,
    start: Point2D<Int>,
) {
  private data class CheatState(
      val point: Point2D<Int>,
      val cost: Int
  )

  private data class Cheat(
      val start: Point2D<Int>,
      val end: Point2D<Int>
  )

  private val distancesFromStart: Map<Point2D<Int>, Int> = calculateDistancesFrom(start)

  /**
   * Counts all unique cheats that save at least [minimumSavings] picoseconds.
   *
   * @param minimumSavings The minimum number of picoseconds a cheat must save.
   * @param maxCheatDuration The maximum number of picoseconds collision can be disabled.
   * @return The number of cheats that save at least [minimumSavings] picoseconds.
   */
  fun countCheatsThatSaveAtLeast(
      minimumSavings: Int = 100,
      maxCheatDuration: Int = 2
  ): Long {
    val cheats = mutableSetOf<Cheat>()

    for ((cheatStart, startDistance) in distancesFromStart) {
      val reachableCheatEnds = findReachableCheatEnds(cheatStart, maxCheatDuration)

      for ((cheatEnd, cheatDistance) in reachableCheatEnds) {
        val endDistance = distancesFromStart[cheatEnd] ?: continue

        if (endDistance <= startDistance) {
          continue
        }

        val savings = endDistance - startDistance - cheatDistance

        if (savings >= minimumSavings) {
          cheats.add(Cheat(cheatStart, cheatEnd))
        }
      }
    }

    return cheats.size.toLong()
  }

  /**
   * Returns a map of savings amount to the number of cheats that save exactly that amount.
   *
   * This is useful for validating against the example table from the problem statement.
   *
   * @param maxCheatDuration The maximum number of picoseconds collision can be disabled.
   * @return A map of exact savings amount to cheat count.
   */
  fun cheatSavingsDistribution(maxCheatDuration: Int = 2): Map<Int, Long> {
    val savingsByCheat = mutableMapOf<Cheat, Int>()

    for ((cheatStart, startDistance) in distancesFromStart) {
      val reachableCheatEnds = findReachableCheatEnds(cheatStart, maxCheatDuration)

      for ((cheatEnd, cheatDistance) in reachableCheatEnds) {
        val endDistance = distancesFromStart[cheatEnd] ?: continue

        if (endDistance <= startDistance) {
          continue
        }

        val savings = endDistance - startDistance - cheatDistance

        if (savings > 0) {
          savingsByCheat[Cheat(cheatStart, cheatEnd)] = savings
        }
      }
    }

    return savingsByCheat.values
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
        .toSortedMap()
  }

  private fun calculateDistancesFrom(source: Point2D<Int>): Map<Point2D<Int>, Int> {
    val distances = mutableMapOf<Point2D<Int>, Int>()
    val queue = ArrayDeque<Point2D<Int>>()

    distances[source] = 0
    queue.add(source)

    while (queue.isNotEmpty()) {
      val current = queue.removeFirst()
      val currentDistance = distances.getValue(current)

      for (neighbor in trackNeighbors(current)) {
        if (neighbor in distances) {
          continue
        }

        distances[neighbor] = currentDistance + 1
        queue.add(neighbor)
      }
    }

    return distances
  }

  @SuppressFBWarnings
  private fun trackNeighbors(point: Point2D<Int>): List<Point2D<Int>> {
    return grid.getNeighbors(point, Direction.CARDINALS) { value, _ -> isTrack(value) }
        .map { Grid2D.pointInDirection(point, it.first()) }
  }

  private fun findReachableCheatEnds(
      cheatStart: Point2D<Int>,
      maxCheatDuration: Int
  ): Map<Point2D<Int>, Int> {
    val distances = mutableMapOf<Point2D<Int>, Int>()
    val queue = ArrayDeque<CheatState>()
    val reachableTrackEnds = mutableMapOf<Point2D<Int>, Int>()

    distances[cheatStart] = 0
    queue.add(CheatState(cheatStart, 0))

    while (queue.isNotEmpty()) {
      val current = queue.removeFirst()

      if (current.cost == maxCheatDuration) {
        continue
      }

      for (direction in Direction.CARDINALS) {
        val next = Grid2D.pointInDirection(current.point, direction)

        if (!grid.isOnGrid(next)) {
          continue
        }

        val nextCost = current.cost + 1
        val previousCost = distances[next]

        if (previousCost != null && previousCost <= nextCost) {
          continue
        }

        distances[next] = nextCost
        queue.add(CheatState(next, nextCost))

        if (next != cheatStart && isTrack(next)) {
          reachableTrackEnds.merge(next, nextCost, ::minOf)
        }
      }
    }

    return reachableTrackEnds
  }

  private fun isTrack(point: Point2D<Int>): Boolean {
    return grid.isOnGrid(point) && isTrack(grid.get(point))
  }

  private fun isTrack(value: Char): Boolean {
    return value == '.' || value == 'S' || value == 'E'
  }

  /**
   * Companion object for [RaceCondition].
   */
  companion object {
    /**
     * Parses the specified puzzle input into a [RaceCondition] instance.
     *
     * @param input The puzzle input.
     * @return A new [RaceCondition] instance.
     */
    fun parse(input: List<String>): RaceCondition {
      val grid = Grid2D.buildCharacterGrid(input)
      val start = grid.findFirst('S')
          .orElseThrow { IllegalArgumentException("Race track is missing start tile S!") }
      grid.findFirst('E')
          .orElseThrow { IllegalArgumentException("Race track is missing end tile E!") }

      return RaceCondition(grid, start)
    }
  }
}
