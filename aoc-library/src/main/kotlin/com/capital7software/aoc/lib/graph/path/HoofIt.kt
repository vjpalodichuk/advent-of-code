package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.grid.Grid2D
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * You all arrive at a [Lava Production Facility](https://adventofcode.com/2023/day/15) on a
 * floating island in the sky. As the others begin to search the massive industrial complex, you
 * feel a small nose boop your leg and look down to discover a reindeer wearing a hard hat.
 *
 * The reindeer is holding a book titled "Lava Island Hiking Guide". However, when you open
 * the book, you discover that most of it seems to have been scorched by lava! As you're
 * about to ask how you can help, the reindeer brings you a blank
 * [topographic map](https://en.wikipedia.org/wiki/Topographic_map) of the surrounding area
 * (your puzzle input) and looks up at you excitedly.
 *
 * Perhaps you can help fill in the missing hiking trails?
 *
 * The topographic map indicates the **height** at each position using a scale from ```0```
 * (lowest) to ```9``` (highest). For example:
 *
 * ```
 * 0123
 * 1234
 * 8765
 * 9876
 * ```
 *
 * Based on un-scorched scraps of the book, you determine that a good hiking trail is
 * **as long as possible** and has an **even, gradual, uphill slope**. For all practical
 * purposes, this means that a hiking trail is any path that starts at height ```0```,
 * ends at height ```9```, and always increases by a height of exactly 1 at each step.
 * Hiking trails never include diagonal steps - only up, down, left, or right
 * (from the perspective of the map).
 *
 * You look up from the map and notice that the reindeer has helpfully begun to construct
 * a small pile of pencils, markers, rulers, compasses, stickers, and other equipment you
 * might need to update the map with hiking trails.
 *
 * A **trailhead** is any position that starts one or more hiking trails - here, these
 * positions will always have height ```0```. Assembling more fragments of pages, you
 * establish that a trailhead's ```score``` is the number of ```9```-height positions
 * reachable from that trailhead via a hiking trail. In the above example, the single
 * trailhead in the top left corner has a score of ```1``` because it can reach a
 * single ```9``` (the one in the bottom left).
 *
 * This trailhead has a score of ```2```:
 *
 * ```
 * ...0...
 * ...1...
 * ...2...
 * 6543456
 * 7.....7
 * 8.....8
 * 9.....9
 * ```
 *
 * (The positions marked ```.``` are impassable tiles to simplify these examples; they do
 * not appear on your actual topographic map.)
 *
 * This trailhead has a score of ```4``` because every ```9``` is reachable via a hiking
 * trail except the one immediately to the left of the trailhead:
 *
 * ```
 * ..90..9
 * ...1.98
 * ...2..7
 * 6543456
 * 765.987
 * 876....
 * 987....
 * ```
 *
 * This topographic map contains **two** trailheads; the trailhead at the top has a score
 * of ```1```, while the trailhead at the bottom has a score of ```2```:
 *
 * ```
 * 10..9..
 * 2...8..
 * 3...7..
 * 4567654
 * ...8..3
 * ...9..2
 * .....01
 * ```
 *
 * Here's a larger example:
 *
 * ```
 * 89010123
 * 78121874
 * 87430965
 * 96549874
 * 45678903
 * 32019012
 * 01329801
 * 10456732
 * ```
 *
 * This larger example has 9 trailheads. Considering the trailheads in reading order,
 * they have scores of ```5```, ```6```, ```5```, ```3```, ```1```, ```3```, ```5```,
 * ```3```, and ```5```. Adding these scores together, the sum of the scores of all
 * trailheads is **```36```**.
 *
 * The reindeer spends a few minutes reviewing your hiking trail map before realizing
 * something, disappearing for a few minutes, and finally returning with yet another
 * slightly-charred piece of paper.
 *
 * The paper describes a second way to measure a trailhead called its **rating**. A trailhead's
 * rating is the **number of distinct hiking trails** which begin at that trailhead. For example:
 *
 * ```
 * .....0.
 * ..4321.
 * ..5..2.
 * ..6543.
 * ..7..4.
 * ..8765.
 * ..9....
 * ```
 *
 * The above map has a single trailhead; its rating is ```3``` because there are exactly
 * three distinct hiking trails which begin at that position:
 *
 * ```
 * .....0.   .....0.   .....0.
 * ..4321.   .....1.   .....1.
 * ..5....   .....2.   .....2.
 * ..6....   ..6543.   .....3.
 * ..7....   ..7....   .....4.
 * ..8....   ..8....   ..8765.
 * ..9....   ..9....   ..9....
 * ```
 *
 * Here is a map containing a single trailhead with rating ```13```:
 *
 * ```
 * ..90..9
 * ...1.98
 * ...2..7
 * 6543456
 * 765.987
 * 876....
 * 987....
 * ```
 *
 * This map contains a single trailhead with rating ```227``` (because there are ```121```
 * distinct hiking trails that lead to the ```9``` on the right edge and ```106``` that
 * lead to the ```9``` on the bottom edge):
 *
 * ```
 * 012345
 * 123456
 * 234567
 * 345678
 * 4.6789
 * 56789.
 * ```
 *
 * Here's the larger example from before:
 *
 * ```
 * 89010123
 * 78121874
 * 87430965
 * 96549874
 * 45678903
 * 32019012
 * 01329801
 * 10456732
 * ```
 *
 * Considering its trailheads in reading order, they have ratings of ```20```, ```24```,
 * ```10```, ```4```, ```1```, ```4```, ```5```, ```8```, and ```5```. The sum of all
 * trailhead ratings in this larger example topographic map is **```81```**.
 *
 * @param input [List] of [String]s that represents the height of each plat in all trails.
 */
class HoofIt(input: List<String>) {
  private companion object {
    private const val OFFSET = 0x30 // ASCII 0
    private const val TRAIL_HEAD_HEIGHT = 0 // All trails start at a height of zero (0).
    private const val TARGET_SUM = 45 // The sum of 0 through 9

    @SuppressFBWarnings
    private fun buildGrid(input: List<String>): Grid2D<Int> {
      val rows = input.size
      val columns = input[0].length
      val items = input.map { line -> line.map { it.code - OFFSET } }.flatten().toTypedArray()
      return Grid2D(columns, rows, items)
    }

    @SuppressFBWarnings
    private fun findAllStartTiles(grid: Grid2D<Int>): Set<TrailTile> {
      return grid.findAll(TRAIL_HEAD_HEIGHT).map { TrailTile(it) }.toSet()
    }
  }

  private val grid: Grid2D<Int> by lazy { buildGrid(input) }
  private val startingTiles by lazy { findAllStartTiles(grid) }

  /**
   * Represents a [point] in space and the [height] of that space.
   *
   * @property point The [Point2D] where this piece of the trail is located.
   * @property height The elevation of this piece of the trail.
   */
  data class TrailTile(val point: Point2D<Int>, val height: Int = 0)

  /**
   * Represents a full valid hiking trail. A valid trail consists of 10 [tiles] where the first
   * step has a height of 0 and the height increases by 1 for each subsequent step ending with the
   * last step having a height of 9.
   *
   * @property tiles The [List] of [TrailTile] that make up this trail.
   */
  @SuppressFBWarnings
  data class HikingTrail(val tiles: List<TrailTile>)

  /**
   * A trailhead is any position that starts one or more hiking trails - here, these positions
   * will always have height 0.
   *
   * @property point The location of this trailhead.
   * @property trails The valid [HikingTrail]s that this trailhead starts.
   */
  @SuppressFBWarnings
  data class Trailhead(val point: Point2D<Int>, val trails: List<HikingTrail>) {
    /**
     * The number of distinct 9 [TrailTile.height] [TrailTile]s this head leads to.
     */
    val score: Int by lazy { trails.map { it.tiles.last() }.distinct().size }

    /**
     * The distinct number of trails that can be traversed from this head.
     */
    val rating: Int by lazy { trails.size }
  }

  /**
   * Finds and returns a [List] of all [Trailhead]s and their [HikingTrail]s.
   * @return A [List] of all [Trailhead]s and their [HikingTrail]s.
   */
  fun findAll(): List<Trailhead> {
    val result = mutableListOf<Trailhead>()
    for (startTile in startingTiles) {
      val trails = findAllTrails(startTile)

      if (trails.isNotEmpty()) {
        result.add(Trailhead(startTile.point, trails))
      }
    }
    return result
  }

  private fun findAllTrails(
      tile: TrailTile,
  ): List<HikingTrail> {
    /**
     * Pretty simple really.
     *
     * For the specified startTile, find all valid [HikingTrail]s. We do this by:
     *
     * - Starting at the startTile, add itself to the current path and its height to the current
     * sum of heights.
     * - Next, find all neighbors in the four cardinal directions whose
     * height is 1 + the current sum of heights.
     * - If there are no neighbors to explore and the current sum of heights is equal to our target
     * sum of heights, then add a valid [HikingTrail] to the results to return.
     * - If there are no neighbors to explore and the current sum of heights is not equal to our
     * target sum of heights, then just continue.
     * - Otherwise, For each neighbor, repeat the above.
     * - Remove the tile from the current path before returning.
     */
    val results = mutableListOf<HikingTrail>()
    val pathSoFar = mutableListOf<TrailTile>()

    findAllTrails(tile, 0, pathSoFar, results)

    return results
  }

  private fun findAllTrails(
      tile: TrailTile,
      sumOfHeights: Int,
      pathSoFar: MutableList<TrailTile>,
      foundSoFar: MutableList<HikingTrail>
  ) {
    val newSum = sumOfHeights + tile.height

    pathSoFar.add(tile)

    val neighbors = findNeighbors(tile)

    if (neighbors.isEmpty()) {
      if (TARGET_SUM == newSum) {
        foundSoFar.add(HikingTrail(pathSoFar.toList()))
      }
    } else {
      for (neighbor in neighbors) {
        findAllTrails(neighbor, newSum, pathSoFar, foundSoFar)
      }
    }

    pathSoFar.removeLast()
  }

  private fun findNeighbors(tile: TrailTile): List<TrailTile> {
    val results = mutableListOf<TrailTile>()

    for (direction in Direction.CARDINAL_DIRECTIONS) {
      val point = tile.point.pointInDirection(direction)

      if (grid.isOnGrid(point)) {
        val height = grid[point]

        if (tile.height + 1 == height) {
          results.add(TrailTile(point, height))
        }
      }
    }
    return results
  }
}
