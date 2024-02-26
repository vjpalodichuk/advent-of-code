package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.crypt.Md5Fun
import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.graph.Graph
import com.capital7software.aoc.lib.graph.Vertex
import com.capital7software.aoc.lib.grid.InfiniteGrid
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.security.MessageDigest
import java.util.Properties
import java.util.function.BiFunction

/**
 * You're trying to access a secure vault protected by a 4x4 grid of small rooms
 * connected by doors. You start in the top-left room (marked S), and you can
 * access the vault (marked V) once you reach the bottom-right room:
 *
 * ```
 * #########
 * #S| | | #
 * #-#-#-#-#
 * # | | | #
 * #-#-#-#-#
 * # | | | #
 * #-#-#-#-#
 * # | | |
 * ####### V
 * ```
 *
 * Fixed walls are marked with #, and doors are marked with - or |.
 *
 * The doors in your **current room** are either open or closed (and locked) based on
 * the hexadecimal MD5 hash of a passcode (your puzzle input) followed by a sequence
 * of uppercase characters representing the **path you have taken so far** (U for up,
 * D for down, L for left, and R for right).
 *
 * Only the first four characters of the hash are used; they represent, respectively,
 * the doors **up, down, left, and right** from your current position. Any b, c, d, e,
 * or f means that the corresponding door is **open**; any other character (any number
 * or a) means that the corresponding door is **closed and locked**.
 *
 * To access the vault, all you need to do is reach the bottom-right room; reaching
 * this room opens the vault and all doors in the maze.
 *
 * For example, suppose the passcode is hijkl. Initially, you have taken no steps,
 * and so your path is empty: you simply find the MD5 hash of hijkl alone. The
 * first four characters of this hash are ced9, which indicate that up is open (c),
 * down is open (e), left is open (d), and right is closed and locked (9). Because
 * you start in the top-left corner, there are no "up" or "left" doors to be open,
 * so your only choice is **down**.
 *
 * Next, having gone only one step (down, or D), you find the hash of hijklD. This
 * produces f2bc, which indicates that you can go back up, left (but that's a wall),
 * or right. Going right means hashing hijkl**DR** to get 5745 - all doors closed and
 * locked. However, going **up** instead is worthwhile: even though it returns you to
 * the room you started in, your path would then be DU, opening a **different set of
 * doors.**
 *
 * After going DU (and then hashing hijkl**DU** to get 528e), only the right door is
 * open; after going DUR, all doors lock. (Fortunately, your actual passcode is
 * not hijkl).
 */
class SecureVault(
    private val passcode: String,
    private val columns: Int,
    private val rows: Int,
    name: String = "vault-$passcode",
) : Heuristic<Point2D<Long>, Int> {
  companion object {
    private val OPEN = setOf('b', 'c', 'd', 'e', 'f')
    private val PATH_REGEX = """\d+,\d+:(?<path>\w+)""".toRegex()
  }

  private val start = Point2D<Long>(0L, 0L)
  private val finish = Point2D<Long>((columns - 1).toLong(), (rows - 1).toLong())
  private val md = MessageDigest.getInstance("MD5")
  private val graph = Graph<Point2D<Long>, Int>(name).apply {
    // Add the start and finish tiles to this graph.
    add(Vertex("${start.id()}:", "start", start, mutableMapOf()))
    add(Vertex("${finish.id()}:", "finish", finish, mutableMapOf()))
  }
  private val expanded: MutableSet<String> = mutableSetOf()
  private val expander:
      BiFunction<
          Graph<Point2D<Long>, Int>, Vertex<Point2D<Long>, Int>, Boolean> =
      BiFunction<
          Graph<Point2D<Long>, Int>, Vertex<Point2D<Long>, Int>, Boolean>
      { g: Graph<Point2D<Long>, Int>, v: Vertex<Point2D<Long>, Int> ->
        val point: Point2D<Long> = v.get()
        val path: String = if (v.id.length == point.id().length + 1) {
          ""
        } else {
          v.id.substring(point.id().length + 1)
        }

        return@BiFunction if (expanded.contains(v.id)) {
          false
        } else {
          val weight = 1
          neighbors(point, path)
              .forEach {
                val targetId = it.id
                g.add(it)
                g.add(v.id, targetId, "${v.id}-$targetId", weight)
              }
          expanded.add(v.id)
          true
        }
      }

  private fun clear() {
    graph.clear()
    graph.add(Vertex("${start.id()}:", "start", start, mutableMapOf()))
    graph.add(Vertex("${finish.id()}:", "finish", finish, mutableMapOf()))
  }

  /**
   * The doors in your **current room** are either open or closed (and locked) based on
   * the hexadecimal MD5 hash of a passcode (your puzzle input) followed by a sequence
   * of uppercase characters representing the **path you have taken so far** (U for up,
   * D for down, L for left, and R for right).
   *
   * Only the first four characters of the hash are used; they represent, respectively,
   * the doors **up, down, left, and right** from your current position. Any b, c, d, e,
   * or f means that the corresponding door is **open**; any other character (any number
   * or a) means that the corresponding door is **closed and locked**.
   *
   * @param point The [Point2D] of the room to get the neighbors of.
   * @param path The path so far.
   */
  @SuppressFBWarnings
  private fun neighbors(point: Point2D<Long>, path: String): List<Vertex<Point2D<Long>, Int>> {
    val answer = mutableListOf<Vertex<Point2D<Long>, Int>>()
    val hash = Md5Fun.generateHash(md, passcode + path)

    addIfUnlocked(hash[0], point, path, Direction.NORTH, "U", answer)
    addIfUnlocked(hash[1], point, path, Direction.SOUTH, "D", answer)
    addIfUnlocked(hash[2], point, path, Direction.WEST, "L", answer)
    addIfUnlocked(hash[3], point, path, Direction.EAST, "R", answer)

    return answer
  }

  private fun addIfUnlocked(
      code: Char,
      point: Point2D<Long>,
      path: String,
      direction: Direction,
      step: String,
      neighbors: MutableList<Vertex<Point2D<Long>, Int>>
  ) {
    if (OPEN.contains(code)) {
      val newPoint = InfiniteGrid.pointInDirection(point, direction)

      if (pointIntVault(newPoint)) {
        if (newPoint == finish) {
          neighbors.add(Vertex(
              "${newPoint.id()}:", path + step, newPoint, mutableMapOf())
          )
        } else {
          neighbors.add(Vertex(
              "${newPoint.id()}:$path$step", path + step, newPoint, mutableMapOf())
          )
        }
      }
    }
  }

  private fun pointIntVault(point: Point2D<Long>): Boolean {
    return point.x in 0..<columns && point.y in 0..<rows
  }

  /**
   * Calculates and returns the shortest path from the start location to the vault.
   * The first element of the returned [Pair] is the length of the path, the second is the
   * actual path through the vault. If no path exists, the length will be -1.
   *
   * Passcodes actually used by Easter Bunny Vault Security do allow access to the
   * vault if you know the right path. For example:
   *
   * - If your passcode were ihgpwlah, the shortest path would be DDRRRD.
   * - With kglvqrro, the shortest path would be DDUDRLRRUDRD.
   * - With ulqzkmiv, the shortest would be DRURDRUDDLLDLUURRDULRLDUUDDDRR.
   *
   * @return A [Pair] that contains the cost and path for the shortest path.
   */
  fun findShortestPath(): Pair<Int, String> {
    clear()
    val (pathfinder, properties) = createPathfinder()
    var shortestPath: PathfinderResult<Point2D<Long>, Int>? = null

    pathfinder.find(
        graph,
        properties,
        expander,
        {
          val current = shortestPath

          if (current == null) {
            shortestPath = it
          } else if (current.cost > it.cost) {
            shortestPath = it
          }
          // AlphaStar is greedy and only calls the valid handler a single time!
          PathfinderStatus.FINISHED
        },
        null
    )

    return pathToResult(shortestPath)
  }

  /**
   * Calculates and returns the longest path from the start location to the vault.
   * The first element of the returned [Pair] is the length of the path, the second is the
   * actual path through the vault. If no path exists, the length will be -1.
   *
   * For example:
   *
   * If your passcode were ihgpwlah, the longest path would take 370 steps.
   * With kglvqrro, the longest path would be 492 steps long.
   * With ulqzkmiv, the longest path would be 830 steps long.
   *
   * @return A [Pair] that contains the cost and path for the shortest path.
   */
  fun findLongestPath(): Pair<Int, String> {
    var longestPath: PathfinderResult<Point2D<Long>, Int>? = null
    val (pathfinder, properties) = createPathfinder(true)

    pathfinder.find(
        graph,
        properties,
        expander,
        {
          val current = longestPath

          if (current == null) {
            longestPath = it
          } else if (current.cost < it.cost) {
            longestPath = it
          }
          // The Depth-first Pathfinder may need to find many paths before it hits the longest.
          PathfinderStatus.CONTINUE
        },
        null
    )

    return pathToResult(longestPath)
  }

  private fun pathToResult(path: PathfinderResult<Point2D<Long>, Int>?): Pair<Int, String> {
    return when (path) {
      null -> Pair(-1, "")
      else -> {
        val edge = path.edges.last()
        val add = if (edge.source.startsWith(finish.x.toString())) {
          "D"
        } else {
          "R"
        }
        val match = PATH_REGEX.find(edge.source)

        val finalPath = if (match != null && match.groups["path"] != null) {
          match.groups["path"]!!.value + add
        } else {
          add
        }
        Pair(path.cost, finalPath)
      }
    }
  }

  private fun createPathfinder(
      depthFirst: Boolean = false
  ): Pair<DynamicPathfinder<PathfinderResult<Point2D<Long>, Int>, Point2D<Long>, Int>, Properties> {
    clear()
    val pathFinder: DynamicPathfinder<PathfinderResult<Point2D<Long>, Int>, Point2D<Long>, Int> =
        if (depthFirst) {
          DepthFirstPathfinder()
        } else {
          AlphaStarPathfinder()
        }
    val properties = Properties()
    properties[PathfinderProperties.SUM_PATH] = true
    properties[PathfinderProperties.STARTING_VERTEX_ID] = "${start.id()}:"
    properties[PathfinderProperties.ENDING_VERTEX_ID] = "${finish.id()}:"
    properties[PathfinderProperties.HEURISTIC] = this
    return Pair(pathFinder, properties)
  }
}
