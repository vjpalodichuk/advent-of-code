package com.capital7software.aoc.lib.graph.path

import com.capital7software.aoc.lib.computer.clean
import com.capital7software.aoc.lib.graph.Graph
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.Properties

/**
 * Walking along the memory banks of the stream, you find a small village that is experiencing a
 * little confusion: some programs can't communicate with each other.
 *
 * Programs in this village communicate using a fixed system of **pipes**. Messages are passed
 * between programs using these pipes, but most programs aren't connected to each other
 * directly. Instead, programs pass messages between each other until the message
 * reaches the intended recipient.
 *
 * For some reason, though, some of these messages aren't ever reaching their intended recipient,
 * and the programs suspect that some pipes are missing. They would like you to investigate.
 *
 * You walk through the village and record the ID of each program and the IDs with which it can
 * communicate directly (your puzzle input). Each program has one or more programs with which
 * it can communicate, and these pipes are bidirectional; if 8 says it can communicate with 11,
 * then 11 will say it can communicate with 8.
 *
 * You need to figure out how many programs are in the group that contains program ID 0.
 *
 * For example, suppose you go door-to-door like a travelling salesman and record
 * the following list:
 *
 * ```
 * 0 <-> 2
 * 1 <-> 1
 * 2 <-> 0, 3, 4
 * 3 <-> 2, 4
 * 4 <-> 2, 3, 6
 * 5 <-> 6
 * 6 <-> 4, 5
 * ```
 *
 * In this example, the following programs are in the group that contains program ID 0:
 *
 * - Program 0 by definition.
 * - Program 2, directly connected to program 0.
 * - Program 3 via program 2.
 * - Program 4 via program 2.
 * - Program 5 via programs 6, then 4, then 2.
 * - Program 6 via programs 4, then 2.
 *
 * Therefore, a total of 6 programs are in this group; all but program 1, which has a pipe
 * that connects it to itself.
 *
 * There are more programs than just the ones in the group containing program ID 0.
 * The rest of them have no way of reaching that group, and still might have no way
 * of reaching each other.
 *
 * A **group** is a collection of programs that can all communicate via pipes either
 * directly or indirectly. The programs you identified just a moment ago are all
 * part of the same group. Now, they would like you to determine the total number
 * of groups.
 *
 * In the example above, there were 2 groups: one consisting of programs 0,2,3,4,5,6,
 * and the other consisting solely of program 1.
 *
 * @param input The [List] of [String] that describe the communications graph for the programs.
 */
class DigitalPlumber(input: List<String>) {
  private val graph: Graph<Int, Int> = processInput(input)

  @SuppressFBWarnings
  private fun processInput(input: List<String>): Graph<Int, Int> {
    val g = Graph<Int, Int>("program-communications")

    input.forEach { line ->
      val split = line.split(" <-> ".toRegex())
      val sourceId = split[0]
      val targets = split[1].split(",").map { it.clean() }

      g.add(sourceId, sourceId.toInt())

      targets.forEach { targetId ->
        g.add(targetId, targetId.toInt())
        // Bi-directional communication!
        g.add(sourceId, targetId, "$sourceId-$targetId", 1)
        g.add(targetId, sourceId, "$targetId-$sourceId", 1)
      }
    }
    return g
  }

  /**
   * Finds and returns the programs that are a part of the specified group. A program is in the
   * specified group if there exists a path to that program.
   *
   * @param id The program ID that we are interested in finding the other programs that are in
   * their group
   * @return The set of program IDs that are in the specified group.
   */
  @SuppressFBWarnings
  fun programsInGroup(id: Int): Set<Int> {
    val answer = mutableSetOf<Int>()
    answer.add(id)

    val pathFinder = ExplorerPathfinder<Int, Int>()
    val properties = Properties()
    properties[PathfinderProperties.STARTING_VERTEX_ID] = id.toString()
    properties[PathfinderProperties.MAX_COST] = graph.size()
    var distinct: PathfinderResult<Int, Int>? = null

    pathFinder.find(
        graph,
        properties,
        {
          distinct = it

          // Explorer is greedy and only calls the valid handler a single time!
          PathfinderStatus.FINISHED
        },
        null
    )
    answer.addAll(distinct?.vertices?.mapNotNull { it.get() } ?: listOf())
    return answer
  }

  /**
   * Returns the total number of distinct communications groups in the communication network.
   *
   * @return The total number of distinct communications groups in the communication network.
   */
  @SuppressFBWarnings
  fun allGroups(): List<Set<Int>> {
    val visited = mutableSetOf<Int>()
    val answer = mutableListOf<Set<Int>>()

    graph.vertices.forEach { vertex ->
      if (vertex.get() !in visited) {
        val group = programsInGroup(vertex.get())
        visited.addAll(group)
        answer.add(group)
      }
    }

    return answer
  }
}
