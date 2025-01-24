package com.capital7software.aoc.lib.analysis

import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.grid.Grid2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * You find yourselves on the [roof](https://adventofcode.com/2016/day/25) of a top-secret
 * Easter Bunny installation.
 *
 * While The Historians do their thing, you take a look at the familiar **huge antenna**.
 * Much to your surprise, it seems to have been reconfigured to emit a signal that makes
 * people 0.1% more likely to buy Easter Bunny brand Imitation Mediocre Chocolate as a
 * Christmas gift! Unthinkable!
 *
 * Scanning across the city, you find that there are actually many such antennas. Each antenna
 * is tuned to a specific **frequency** indicated by a single lowercase letter, uppercase letter,
 * or digit. You create a map. For example:
 *
 * ```
 * ............
 * ........0...
 * .....0......
 * .......0....
 * ....0.......
 * ......A.....
 * ............
 * ............
 * ........A...
 * .........A..
 * ............
 * ............
 * ```
 *
 * @param input [List] of [String]s that represents the map of antenna frequencies to parse.
 * Each [String] must consist of an equal number of characters.
 */
class ResonantCollinearity(input: List<String>) {
  private val grid: Grid2D<Char>
  private val antennas: Map<Char, List<Point2D<Int>>>

  init {
    initializeMap(input).apply {
      antennas = first
      grid = second
    }
  }

  companion object {
    private fun initializeMap(input: List<String>): Pair<Map<Char, List<Point2D<Int>>>, Grid2D<Char>> {
      val rows = input.size
      val cols = input[0].length
      val items = Array(rows * cols) { '.' }
      val antennas = mutableMapOf<Char, MutableList<Point2D<Int>>>()

      input.forEachIndexed { i, line ->
        line.forEachIndexed { j, item ->
          if (item.isLetterOrDigit()) {
            val point2D = Point2D(j, i)

            items[i * cols + j] = item
            antennas.computeIfAbsent(item) { mutableListOf() }.add(point2D)
          }
        }
      }

      return Pair(antennas, Grid2D(rows, cols, items))
    }
  }

  /**
   * Watching over your shoulder as you work, one of The Historians asks if you took the effects of
   * resonant harmonics into your calculations.
   *
   * Whoops!
   *
   * After updating your model, it turns out that an antinode occurs at **any grid position**
   * exactly in line with at least two antennas of the same frequency, regardless of distance.
   * This means that some of the new antinodes will occur at the position of each antenna (unless
   * that antenna is the only one of its frequency).
   *
   * So, these three ```T```-frequency antennas now create many antinodes:
   *
   * ```
   * T....#....
   * ...T......
   * .T....#...
   * .........#
   * ..#.......
   * ..........
   * ...#......
   * ..........
   * ....#.....
   * ..........
   * ```
   *
   * In fact, the three ```T```-frequency antennas are all exactly in line with two antennas, so
   * they are all also antinodes! This brings the total number of antinodes in the above example
   * to **```9```**.
   *
   * The original example now has **```34```** antinodes, including the antinodes that appear on
   * every antenna:
   *
   * ```
   * ##....#....#
   * .#.#....0...
   * ..#.#0....#.
   * ..##...0....
   * ....0....#..
   * .#...#A....#
   * ...#..#.....
   * #....#.#....
   * ..#.....A...
   * ....#....A..
   * .#........#.
   * ...#......##
   * ```
   */
  fun calculateNewModelBoundedAntinodes(): List<Antinode> {
    return calculateBoundedAntinodes(true)
  }

  /**
   * Calculates and returns the antinodes that are bound to this map.
   *
   * The signal only applies its nefarious effect at specific **antinodes** based on the resonant
   * frequencies of the antennas. In particular, an antinode occurs at any point that is perfectly
   * in line with two antennas of the same frequency - but only when one of the antennas is twice
   * as far away as the other. This means that for any pair of antennas with the same frequency,
   * there are two antinodes, one on either side of them.
   *
   * So, for these two antennas with frequency ```a```, they create the two antinodes marked
   * with ```#```:
   *
   * ```
   * ..........
   * ...#......
   * ..........
   * ....a.....
   * ..........
   * .....a....
   * ..........
   * ......#...
   * ..........
   * ..........
   * ```
   *
   * Adding a third antenna with the same frequency creates several more antinodes. It would
   * ideally add four antinodes, but two are off the right side of the map, so instead
   * it adds only two:
   *
   * ```
   * ..........
   * ...#......
   * #.........
   * ....a.....
   * ........a.
   * .....a....
   * ..#.......
   * ......#...
   * ..........
   * ..........
   * ```
   *
   * Antennas with different frequencies don't create antinodes; ```A``` and ```a``` count as
   * different frequencies. However, antinodes **can** occur at locations that contain antennas.
   * In this diagram, the lone antenna with frequency capital ```A``` creates no antinodes but
   * has a lowercase-```a```-frequency antinode at its location:
   *
   * ```
   * ..........
   * ...#......
   * #.........
   * ....a.....
   * ........a.
   * .....a....
   * ..#.......
   * ......A...
   * ..........
   * ..........
   * ```
   *
   * The first example has antennas with two different frequencies, so the antinodes they create
   * look like this, plus an antinode overlapping the topmost ```A```-frequency antenna:
   *
   * ```
   * ......#....#
   * ...#....0...
   * ....#0....#.
   * ..#....0....
   * ....0....#..
   * .#....A.....
   * ...#........
   * #......#....
   * ........A...
   * .........A..
   * ..........#.
   * ..........#.
   * ```
   *
   * Because the topmost ```A```-frequency antenna overlaps with a ```0```-frequency antinode,
   * there are **```14```** total unique locations that contain an antinode within the bounds
   * of the map.
   */
  fun calculateBoundedAntinodes(): List<Antinode> {
    return calculateBoundedAntinodes(false)
  }

  private fun calculateBoundedAntinodes(newModel: Boolean): List<Antinode> {
    val antinodes = mutableListOf<Antinode>()

    antennas.forEach { (frequency, points) ->
      points.forEachIndexed { i, pointA ->
        for (pointBIndex in i + 1 until points.size) {
          val pointB = points[pointBIndex]
          antinodes.addAll(calculateBoundedAntinodes(frequency, pointA, pointB, newModel))
        }
      }
    }

    return antinodes
  }

  private fun calculateBoundedAntinodes(
      frequency: Char,
      pointA: Point2D<Int>,
      pointB: Point2D<Int>,
      newModel: Boolean
  ): List<Antinode> {
    val result = mutableListOf<Antinode>()
    val sources = setOf(pointA, pointB)
    val difference = pointB - pointA

    if (newModel) {
      result.add(Antinode(frequency, pointA, sources))

      var antinodeA = pointA - difference

      while (grid.isOnGrid(antinodeA)) {
        result.add(Antinode(frequency, antinodeA, sources))
        antinodeA -= difference
      }

      result.add(Antinode(frequency, pointB, sources))

      var antinodeB = difference + pointB

      while (grid.isOnGrid(antinodeB)) {
        result.add(Antinode(frequency, antinodeB, sources))
        antinodeB = difference + antinodeB
      }
    } else {
      val antinodeA = pointA - difference
      val antinodeB = difference + pointB

      if (grid.isOnGrid(antinodeA)) {
        result.add(Antinode(frequency, antinodeA, sources))
      }
      if (grid.isOnGrid(antinodeB)) {
        result.add(Antinode(frequency, antinodeB, sources))
      }
    }

    return result
  }
}

/**
 * The antinode is a nefarious effect that occurs at any point that is perfectly in line with two
 * antennas of the sam frequency - but only when one of the antennas is twice as far away as the
 * other. This means that for any pair of antennas with the same frequency, there are two
 * antinodes, one on either side of them.
 *
 * @property frequency The frequency that generated this antinode.
 * @property point The [Point2D] on the map this antinode is located.
 * @property sources The source antennas that generated this antinode.
 */
@SuppressFBWarnings
data class Antinode(val frequency: Char, val point: Point2D<Int>, val sources: Set<Point2D<Int>>)
