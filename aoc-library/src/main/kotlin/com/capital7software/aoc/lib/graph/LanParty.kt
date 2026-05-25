package com.capital7software.aoc.lib.graph

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.SortedSet

/**
 * Models a LAN party network as an undirected graph of computer names.
 *
 * This class provides reusable clique-oriented graph operations:
 *
 * - counting fully-connected groups of three computers, also known as triangles
 * - finding the largest fully-connected group, also known as a maximum clique
 *
 * @param adjacency The undirected adjacency map for the network.
 */
@SuppressFBWarnings
class LanParty private constructor(
    private val adjacency: Map<String, Set<String>>,
) {
  /**
   * Companion object for parsing LAN party networks from input.
   */
  companion object {
    private const val EDGE_SEPARATOR = "-"

    /**
     * Parses an undirected LAN network from lines in the form `aa-bb`.
     *
     * Blank lines are ignored.
     *
     * @param input The raw puzzle input.
     * @return A [LanParty] instance backed by an undirected adjacency map.
     */
    fun parse(input: List<String>): LanParty {
      val connections = mutableMapOf<String, MutableSet<String>>()

      input.asSequence()
          .map { it.trim() }
          .filter { it.isNotEmpty() }
          .forEach { line ->
            val parts = line.split(EDGE_SEPARATOR)

            require(parts.size == 2 && parts[0].isNotBlank() && parts[1].isNotBlank()) {
              "Invalid LAN connection line: $line"
            }

            val first = parts[0]
            val second = parts[1]

            require(first != second) {
              "Self-connections are not supported: $line"
            }

            connections.computeIfAbsent(first) { mutableSetOf() }.add(second)
            connections.computeIfAbsent(second) { mutableSetOf() }.add(first)
          }

      return LanParty(connections.mapValues { it.value.toSet() })
    }
  }

  /**
   * Counts all unique groups of three computers where each computer is connected to the other two.
   *
   * If [requiredPrefix] is non-null, only groups where at least one computer starts with that prefix
   * are counted.
   *
   * @param requiredPrefix Optional computer-name prefix required on at least one computer.
   * @return The number of matching fully-connected groups of three computers.
   */
  fun countInterconnectedTriples(requiredPrefix: String? = null): Int {
    val computers = adjacency.keys.sorted()
    var count = 0

    for (firstIndex in computers.indices) {
      val first = computers[firstIndex]

      for (secondIndex in firstIndex + 1 until computers.size) {
        val second = computers[secondIndex]

        if (!areConnected(first, second)) {
          continue
        }

        for (thirdIndex in secondIndex + 1 until computers.size) {
          val third = computers[thirdIndex]

          if (areConnected(first, third) && areConnected(second, third) &&
              containsRequiredPrefix(first, second, third, requiredPrefix)) {
            count++
          }
        }
      }
    }

    return count
  }

  /**
   * Returns the largest fully-connected group of computers.
   *
   * If multiple largest groups exist, the lexicographically smallest sorted group is returned to keep
   * the result deterministic.
   *
   * @return The maximum clique as a sorted [List] of computer names.
   */
  fun largestInterconnectedGroup(): List<String> {
    var best: SortedSet<String> = sortedSetOf()

    fun better(candidate: Set<String>): Boolean {
      if (candidate.size != best.size) {
        return candidate.size > best.size
      }

      return candidate.sorted().joinToString(",") < best.joinToString(",")
    }

    fun bronKerbosch(
        clique: Set<String>,
        candidates: Set<String>,
        excluded: Set<String>,
    ) {
      if (candidates.isEmpty() && excluded.isEmpty()) {
        if (better(clique)) {
          best = clique.toSortedSet()
        }
        return
      }

      if (clique.size + candidates.size < best.size) {
        return
      }

      val pivot = (candidates + excluded).maxByOrNull { adjacency[it]?.size ?: 0 }
      val pivotNeighbors = pivot?.let { adjacency[it].orEmpty() }.orEmpty()
      val verticesToExplore = candidates - pivotNeighbors

      var remainingCandidates = candidates
      var remainingExcluded = excluded

      for (vertex in verticesToExplore.sorted()) {
        val neighbors = adjacency[vertex].orEmpty()

        bronKerbosch(
            clique = clique + vertex,
            candidates = remainingCandidates intersect neighbors,
            excluded = remainingExcluded intersect neighbors,
        )

        remainingCandidates = remainingCandidates - vertex
        remainingExcluded = remainingExcluded + vertex

        if (clique.size + remainingCandidates.size < best.size) {
          break
        }
      }
    }

    bronKerbosch(
        clique = emptySet(),
        candidates = adjacency.keys,
        excluded = emptySet(),
    )

    return best.toList()
  }

  /**
   * Returns the LAN party password, which is the largest interconnected group's computer names
   * sorted alphabetically and joined by commas.
   *
   * @return The LAN party password.
   */
  fun password(): String = largestInterconnectedGroup().joinToString(",")

  private fun areConnected(first: String, second: String): Boolean {
    return adjacency[first]?.contains(second) == true
  }

  private fun containsRequiredPrefix(
      first: String,
      second: String,
      third: String,
      requiredPrefix: String?,
  ): Boolean {
    return requiredPrefix == null ||
        first.startsWith(requiredPrefix) ||
        second.startsWith(requiredPrefix) ||
        third.startsWith(requiredPrefix)
  }
}
