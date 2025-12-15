package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import com.capital7software.aoc.lib.util.Pair
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.jvm.optionals.getOrNull

/**
 * Why not search for the Chief Historian near the
 * [gardener](https://adventofcode.com/2023/day/5) and his
 * [massive farm](https://adventofcode.com/2023/day/21)? There's plenty of food, so The
 * Historians grab something to eat while they search.
 *
 * You're about to settle near a complex arrangement of garden plots when some Elves ask
 * if you can lend a hand. They'd like to set up fences around each region of garden plots,
 * but they can't figure out how much fence they need to order or how much it will cost.
 * They hand you a map (your puzzle input) of the garden plots.
 *
 * Each garden plot grows only a single type of plant and is indicated by a single letter
 * on your map. When multiple garden plots are growing the same type of plant and are
 * touching (horizontally or vertically), they **form** a region. For example:
 *
 * ```
 * AAAA
 * BBCD
 * BBCC
 * EEEC
 * ```
 *
 * This 4x4 arrangement includes garden plots growing five different types of plants
 * (labeled ```A```, ```B```, ```C```, ```D```, and ```E```), each grouped into their
 * own region.
 *
 */
class GardenGroups private constructor(
    private val regions: Set<GardenRegion>
) {
  companion object {
    /**
     * Creates a new [GardenGroups] from the specified raw input that contains the garden.
     *
     * @param input The [List] of [String]s that represent a MxN Garden.
     * - This means the length of each string must be the same.
     * - Each character within the [List] of [String]s represents a **plant** in a
     * single [GardenPlot].
     * - Adjacent [GardenPlot]s that have the same **plant** are grouped together
     * in [GardenRegion]s.
     * - The [GardenRegionSide]s of a [GardenRegion] are the [GardenPlot]s that are on the
     * border.
     * @return A new [GardenGroups] instance
     */
    fun create(input: List<String>): GardenGroups {
      val regions = hashSetOf<GardenRegion>()

      val grid: Grid2D<GardenPlot> = createGrid(input)
      val visited = hashSetOf<GardenPlot>()
      val queue = ArrayDeque<GardenPlot>().apply { addAll(grid) }

      while (!queue.isEmpty()) {
        val plot = queue.removeFirstOrNull() ?: break

        if (!visited.contains(plot)) {
          val region = GardenRegion.create(plot, grid)
          regions.add(region)
          visited.addAll(region.plots)
        }
      }

      return GardenGroups(regions)
    }

    private fun createGrid(input: List<String>): Grid2D<GardenPlot> {
      val grid = Grid2D(
          input.maxBy { it.length }.length,
          input.size,
          Array(input.maxBy { it.length }.length * input.size) { GardenPlot(' ', Point2D(-1, -1)) }
      )

      input.withIndex().forEach { row ->
        row.value.toCharArray().withIndex().forEach { col ->
          val point = Point2D(col.index, row.index)
          grid[point] = GardenPlot(col.value, point)
        }
      }

      return grid
    }
  }

  /**
   * In order to accurately calculate the cost of the fence around a single region, you
   * need to know that region's **area** and **perimeter**.
   *
   * The **area** of a region is simply the number of garden plots the region contains.
   * The above map's type ```A```, ```B```, and ```C``` plants are each in a region of
   * area ```4```. The type ```E``` plants are in a region of area ```3```; the type
   * ```D``` plants are in a region of area ```1```.
   *
   * Each garden plot is a square and so has **four sides**. The **perimeter** of a
   * region is the number of sides of garden plots in the region that do not touch
   * another garden plot in the same region. The type ```A``` and ```C``` plants
   * are each in a region with perimeter ```10```. The type ```B``` and ```E```
   * plants are each in a region with perimeter ```8```. The lone ```D``` plot forms
   * its own region with perimeter ```4```.
   *
   * Visually indicating the sides of plots in each region that contribute to the
   * perimeter using ```-``` and ```|```, the above map's regions' perimeters are
   * measured as follows:
   *
   * ```
   * +-+-+-+-+
   * |A A A A|
   * +-+-+-+-+     +-+
   *               |D|
   * +-+-+   +-+   +-+
   * |B B|   |C|
   * +   +   + +-+
   * |B B|   |C C|
   * +-+-+   +-+ +
   *           |C|
   * +-+-+-+   +-+
   * |E E E|
   * +-+-+-+
   * ```
   *
   * Plants of the same type can appear in multiple separate regions, and regions can even
   * appear within other regions. For example:
   *
   * ```
   * OOOOO
   * OXOXO
   * OOOOO
   * OXOXO
   * OOOOO
   * ```
   *
   * The above map contains **five** regions, one containing all the ```O``` garden plots,
   * and the other four each containing a single ```X``` plot.
   *
   * The four ```X``` regions each have area ```1``` and perimeter ```4```. The region
   * containing ```21``` type ```O``` plants is more complicated; in addition to its outer
   * edge contributing a perimeter of ```20```, its boundary with each ```X``` region
   * contributes an additional ```4``` to its perimeter, for a total perimeter of ```36```.
   *
   * Due to "modern" business practices, the **price** of fence required for a region is
   * found by **multiplying** that region's area by its perimeter. The **total price** of
   * fencing all regions on a map is found by adding together the price of fence for every
   * region on the map.
   *
   * In the first example, region ```A``` has price ```4 * 10 = 40```, region ```B``` has
   * price ```4 * 8 = 32```, region ```C``` has price ```4 * 10 = 40```, region ```D```
   * has price ```1 * 4 = 4```, and region ```E``` has price ```3 * 8 = 24```. So, the
   * total price for the first example is ```140```.
   *
   * In the second example, the region with all the ```O``` plants has price
   * ```21 * 36 = 756```, and each of the four smaller ```X``` regions has price
   * ```1 * 4 = 4```, for a total price of **```772```** (```756 + 4 + 4 + 4 + 4```).
   *
   * Here's a larger example:
   *
   * ```
   * RRRRIICCFF
   * RRRRIICCCF
   * VVRRRCCFFF
   * VVRCCCJFFF
   * VVVVCJJCFE
   * VVIVCCJJEE
   * VVIIICJJEE
   * MIIIIIJJEE
   * MIIISIJEEE
   * MMMISSJEEE
   * ```
   *
   * It contains:
   *
   * - A region of ```R``` plants with price ```12 * 18 = 216```.
   * - A region of ```I``` plants with price ```4 * 8 = 32```.
   * - A region of ```C``` plants with price ```14 * 28 = 392```.
   * - A region of ```F``` plants with price ```10 * 18 = 180```.
   * - A region of ```V``` plants with price ```13 * 20 = 260```.
   * - A region of ```J``` plants with price ```11 * 20 = 220```.
   * - A region of ```C``` plants with price ```1 * 4 = 4```.
   * - A region of ```E``` plants with price ```13 * 18 = 234```.
   * - A region of ```I``` plants with price ```14 * 22 = 308```.
   * - A region of ```M``` plants with price ```5 * 12 = 60```.
   * - A region of ```S``` plants with price ```3 * 8 = 24```.
   *
   * So, it has a total price of **```1930```**.
   *
   * **What is the total price of fencing all regions on your map?**
   */
  val totalByPerimeter: Int by lazy { regions.sumOf { it.priceByPerimeter } }

  /**
   * Fortunately, the Elves are trying to order so much fence that they qualify
   * for a **bulk discount**!
   *
   * Under the bulk discount, instead of using the perimeter to calculate the price,
   * you need to use the **number of sides** each region has. Each straight section of
   * fence counts as a side, regardless of how long it is.
   *
   * Consider this example again:
   * ```
   * AAAA
   * BBCD
   * BBCC
   * EEEC
   * ```
   * The region containing type A plants has ```4``` sides, as does each of the regions
   * containing plants of type ```B```, ```D```, and ```E```. However, the more complex
   * region containing the plants of type ```C``` has ```8``` sides!
   *
   * Using the new method of calculating the per-region price by multiplying the region's
   * area by its number of sides, regions ```A``` through ```E``` have prices ```16```,
   * ```16```, ```32```, ```4```, and ```12```, respectively, for a total price of **80**.
   *
   * The second example above (full of type ```X``` and ```O``` plants) would have a
   * total price of **436**.
   *
   * Here's a map that includes an E-shaped region full of type ```E``` plants:
   * ```
   * EEEEE
   * EXXXX
   * EEEEE
   * EXXXX
   * EEEEE
   * ```
   * The E-shaped region has an area of ```17``` and ```12``` sides for a price of ```204```.
   * Including the two regions full of type ```X``` plants, this map has a total price of **236**.
   *
   * This map has a total price of **368**:
   * ```
   * AAAAAA
   * AAABBA
   * AAABBA
   * ABBAAA
   * ABBAAA
   * AAAAAA
   * ```
   * It includes two regions full of type ```B``` plants (each with ```4``` sides) and a
   * single region full of type ```A``` plants (with ```4``` sides on the outside and ```8```
   * more sides on the inside, a total of ```12``` sides). Be especially careful when counting
   * the fence around regions like the one full of type ```A``` plants; in particular, each
   * section of fence has an in-side and an out-side, so the fence does not connect across the
   * middle of the region (where the two ```B``` regions touch diagonally). (The Elves would
   * have used the Möbius Fencing Company instead, but their contract terms were too one-sided.)
   *
   * The larger example from before now has the following updated prices:
   *
   * - A region of ```R``` plants with price ```12 * 10 = 120```.
   * - A region of ```I``` plants with price ```4 * 4 = 16```.
   * - A region of ```C``` plants with price ```14 * 22 = 308```.
   * - A region of ```F``` plants with price ```10 * 12 = 120```.
   * - A region of ```V``` plants with price ```13 * 10 = 130```.
   * - A region of ```J``` plants with price ```11 * 12 = 132```.
   * - A region of ```C``` plants with price ```1 * 4 = 4```.
   * - A region of ```E``` plants with price ```13 * 8 = 104```.
   * - A region of ```I``` plants with price ```14 * 16 = 224```.
   * - A region of ```M``` plants with price ```5 * 6 = 30```.
   * - A region of ```S``` plants with price ```3 * 6 = 18```.
   *
   * Adding these together produces its new total price of **1206**.
   *
   * **What is the new total price of fencing all regions on your map?**
   *
   */
  val totalBySides: Int by lazy { regions.sumOf { it.priceBySides } }
}

/**
 * A single plot!
 *
 * @param plant The character that represents the plant on this plot
 * @param point The point in the garden that this plot occupies
 */
data class GardenPlot(
    val plant: Char,
    val point: Point2D<Int>
) : Comparable<GardenPlot> {

  /**
   * The ID of the plat is in the form of x,y where x and y are the respective coordinates
   * of this plot
   */
  val id: String by lazy { point.id() }

  override fun compareTo(other: GardenPlot): Int = point.compareTo(other.point)
}

/**
 * Represents a side of a [GardenRegion]
 *
 * A [GardenRegionSide] consists of one or more plots in the same region that do not touch another
 * plot within the same region in the direction the side is facing but, they do abutt one another.
 *
 * @param direction is the [Direction] that this side is facing
 * @param start is the [GardenPlot] that this side is calculated from
 * @param end is the [GardenPlot] that this side ends at
 * @param plots is the [List] of [GardenPlot]s that make of this side
 * @param length is the number of [GardenPlot]s in this side
 */
@SuppressFBWarnings
data class GardenRegionSide(
    val direction: Direction,
    val start: GardenPlot,
    val end: GardenPlot,
    val plots: List<GardenPlot>,
    val length: Int = plots.size
)

/**
 * Represents a region within a [GardenGroups]
 *
 * A [GardenRegion] consists of one or more [GardenPlot]s that have the same plant and that make
 * a connected [Graph]. This means that there exists a path from one plot in the region to every
 * other plot in the same region.
 *
 * Regions have [plots], [sides], [perimeter], and [area].
 * The [priceBySides] or [priceByPerimeter] can also be calculated for this region.
 *
 * @param graph The [Graph] of [GardenPlot]s that make up the region
 * @param plant The plant that exists in this region
 * @param start The [Point2D] of the initial [GardenPlot] that built the [Graph] for this region
 */
@SuppressFBWarnings
class GardenRegion private constructor(
    val graph: Graph<GardenPlot, Int>,
    val plant: Char,
    val start: Point2D<Int>,
) {
  companion object {
    private const val SIDES_PER_PLOT = 4

    /**
     * Creates a new [GardenRegion] starting from the [start] [GardenPlot]. The [grid] is
     * searched and any adjacent [GardenPlot]s that have the same [GardenPlot.plant] will
     * be added to the new [GardenRegion].
     *
     * @param start The [GardenPlot] that marks the beginning of the new [GardenRegion]. The
     * [GardenPlot.plant] from this [GardenPlot] will be used as the [plant] for the
     * new [GardenRegion]. The search for adjacent [GardenPlot]s will begin from [GardenPlot.point].
     * @param grid The [Grid2D] that contains all of the [GardenPlot]s that the new [GardenRegion]
     * will be created from. The [grid] will be searched for adjacent [GardenPlot]s beginning from
     * [start] and continuing in all [Direction.CARDINAL_DIRECTIONS] until either the edge of the
     * garden is reached or a [GardenPlot] with a different plant is encountered.
     * @return The newly instantiated [GardenRegion]
     */
    fun create(start: GardenPlot, grid: Grid2D<GardenPlot>): GardenRegion {
      val graph = Graph<GardenPlot, Int>(start.plant.toString())

      val queue = ArrayDeque<GardenPlot>()
      val visited = hashSetOf<GardenPlot>()

      queue.add(start)

      while (!queue.isEmpty()) {
        val plot = queue.removeFirstOrNull() ?: break

        if (visited.contains(plot)) {
          continue
        }

        visited.add(plot)

        graph.add(plot.id, plot)

        Direction.CARDINAL_DIRECTIONS
            .asSequence()
            .filter { grid.isOnGrid(plot.point.pointInDirection(it)) }
            .filter { grid[plot.point.pointInDirection(it)].plant == plot.plant }
            .forEach {
              val neighbor = grid[plot.point.pointInDirection(it)]
              if (!visited.contains(neighbor)) {
                queue.add(neighbor)
              }
              graph.add(neighbor.id, neighbor)
              graph.add(plot.id, neighbor.id, "${plot.id}-${neighbor.id}")
            }

      }
      return GardenRegion(graph, start.plant, start.point)
    }

    private fun buildSides(region: GardenRegion): Set<GardenRegionSide> {
      // Begin at the start
      val queue = ArrayDeque<Pair<Direction, Vertex<GardenPlot, Int>>>()
      val visited = hashSetOf<Pair<Direction, Vertex<GardenPlot, Int>>>()

      fillQueue(region, queue)

      val sides = hashMapOf<Pair<Direction, Vertex<GardenPlot, Int>>, GardenRegionSide>()

      while (!queue.isEmpty()) {
        val element = queue.removeFirstOrNull() ?: break

        if (visited.contains(element) || sides.containsKey(element)) {
          continue
        }

        visited.add(element)

        val side = linkedSetOf<Vertex<GardenPlot, Int>>()

        val direction = element.first()
        val vertex = element.second()
        val newStart: Vertex<GardenPlot, Int> = buildSide(direction, side, vertex, region.graph)

        if (side.isNotEmpty()) {
          sides[Pair(direction, newStart)] = GardenRegionSide(
              direction,
              side.first.get(),
              side.last.get(),
              side.map { it.get() }
          )
          visited.add(Pair(direction, region.graph[side.last.get().point.id()]))
        }

        if (newStart.id != vertex.id) {
          visited.add(Pair(direction, newStart))
        }
      }

      return sides.values.toSet()
    }

    private fun fillQueue(region: GardenRegion, queue: ArrayDeque<Pair<Direction, Vertex<GardenPlot, Int>>>) {
      val added: HashSet<Pair<Direction, Vertex<GardenPlot, Int>>> = hashSetOf()

      region.graph.vertices.forEach { vertex ->
        if (vertex.size() < SIDES_PER_PLOT) {
          Direction.CARDINAL_DIRECTIONS.forEach { direction ->
            if (getNext(region.graph, vertex, direction) == null) {
              val neighbors = direction.perpendicular.mapNotNull { perpendicular ->
                val neighbor = getNext(region.graph, vertex, perpendicular)
                if (neighbor != null) {
                  if (getNext(region.graph, neighbor, direction) == null) {
                    perpendicular
                  } else {
                    null
                  }
                } else {
                  null
                }
              }

              if (neighbors.size < direction.perpendicular.size && !added.contains(Pair(direction, vertex))) {
                queue.add(Pair(direction, vertex))
                added.add(Pair(direction, vertex))
              }
            }
          }
        }
      }
    }

    private fun buildSide(
        direction: Direction,
        side: LinkedHashSet<Vertex<GardenPlot, Int>>,
        vertex: Vertex<GardenPlot, Int>,
        graph: Graph<GardenPlot, Int>,
    ): Vertex<GardenPlot, Int> {
      val leftDirection = direction.perpendicular.first()
      val rightDirection = direction.perpendicular.last()

      val start = findStart(direction, vertex, leftDirection, graph)

      side.add(start)

      fillSide(side, start, direction, rightDirection, graph)

      return start
    }

    private fun fillSide(
        side: LinkedHashSet<Vertex<GardenPlot, Int>>,
        vertex: Vertex<GardenPlot, Int>,
        direction: Direction,
        rightDirection: Direction,
        graph: Graph<GardenPlot, Int>,
    ) {
      var last = vertex
      var next = getNext(graph, last, rightDirection)

      while (next != null) {
        val nextPlotInDirection = getNext(graph, next, direction)

        if (nextPlotInDirection == null) {
          side.add(next)
          last = next
          next = getNext(graph, last, rightDirection)
        } else {
          next = null
        }
      }
    }

    private fun findStart(
        direction: Direction,
        vertex: Vertex<GardenPlot, Int>,
        leftDirection: Direction,
        graph: Graph<GardenPlot, Int>,
    ): Vertex<GardenPlot, Int> {
      var last = vertex
      var next = getNext(graph, last, leftDirection)

      while (next != null) {
        val nextPlotInDirection = getNext(graph, next, direction)

        if (nextPlotInDirection == null) {
          last = next
          next = getNext(graph, last, leftDirection)
        } else {
          next = null
        }
      }

      return last
    }

    private fun getNext(
        graph: Graph<GardenPlot, Int>,
        vertex: Vertex<GardenPlot, Int>,
        direction: Direction
    ): Vertex<GardenPlot, Int>? = graph[vertex.get().point.pointInDirection(direction).id()]
  }

  /**
   * The [GardenPlot]s that exist within this [GardenRegion]
   */
  val plots: List<GardenPlot> by lazy {
    graph.vertices
        .mapNotNull { it.value.getOrNull() }
  }

  /**
   * The area of a [GardenRegion] is the number of [GardenPlot]s that make up this region
   */
  val area: Int by lazy { plots.size }

  /**
   * The perimeter of a [GardenRegion] is the sum of the length of each side within this region
   */
  val perimeter: Int by lazy { sides.sumOf { it.length } }

  /**
   * The total price is calculated by multiplying the [area] of this region by its [perimeter]
   */
  val priceByPerimeter: Int by lazy { area * perimeter }

  /**
   * The sides are the set of [GardenRegionSide]s found within this region.
   */
  val sides: Set<GardenRegionSide> by lazy { buildSides(this) }

  /**
   * The total price is calculated by multiplying the [area] of this region by the number of [sides]
   */
  val priceBySides: Int by lazy { area * sides.size }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GardenRegion

    if (plant != other.plant) return false
    if ((plots - other.plots).isNotEmpty() && (other.plots - plots).isNotEmpty()) return false

    return true
  }

  override fun hashCode(): Int {
    var result = plant.hashCode()
    result = 31 * result + plots.hashCode()
    return result
  }

  override fun toString(): String {
    return "GardenRegion(plant=$plant, start=$start, area=$area, graph=$graph)"
  }
}
