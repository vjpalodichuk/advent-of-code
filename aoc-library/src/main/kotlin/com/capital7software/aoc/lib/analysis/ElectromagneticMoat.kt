package com.capital7software.aoc.lib.analysis

import com.capital7software.aoc.lib.string.clean
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * **--- Day 24: Electromagnetic Moat ---**
 *
 * The CPU itself is a large, black building surrounded by a bottomless pit. Enormous metal
 * tubes extend outward from the side of the building at regular intervals and descend down
 * into the void. There's no way to cross, but you need to get inside.
 *
 * No way, of course, other than building a **bridge** out of the magnetic components
 * strewn about nearby.
 *
 * Each component has two **ports**, one on each end. The ports come in all different types,
 * and only matching types can be connected. You take an inventory of the components by their
 * port types (your puzzle input). Each port is identified by the number of **pins** it uses;
 * more pins mean a stronger connection for your bridge. A 3/7 component, for example, has a
 * type-3 port on one side, and a type-7 port on the other.
 *
 * Your side of the pit is metallic; a perfect surface to connect a magnetic, **zero-pin port**.
 * Because of this, the first port you use must be of type 0. It doesn't matter what type of
 * port you end with; your goal is just to make the bridge as strong as possible.
 *
 * The **strength** of a bridge is the sum of the port types in each component.
 * For example, if your bridge is made of components 0/3, 3/7, and 7/4, your bridge
 * has a strength of 0+3 + 3+7 + 7+4 = 24.
 *
 * For example, suppose you had the following components:
 *
 * ```
 * 0/2
 * 2/2
 * 2/3
 * 3/4
 * 3/5
 * 0/1
 * 10/1
 * 9/10
 * ```
 *
 * With them, you could make the following valid bridges:
 *
 * - 0/1
 * - 0/1--10/1
 * - 0/1--10/1--9/10
 * - 0/2
 * - 0/2--2/3
 * - 0/2--2/3--3/4
 * - 0/2--2/3--3/5
 * - 0/2--2/2
 * - 0/2--2/2--2/3
 * - 0/2--2/2--2/3--3/4
 * - 0/2--2/2--2/3--3/5
 *
 * (Note how, as shown by 10/1, order of ports within a component doesn't matter.
 * However, you may only use each port on a component once.)
 *
 * Of these bridges, the **strongest** one is 0/1--10/1--9/10; it has a strength
 * of 0+1 + 1+10 + 10+9 = **31**.
 *
 * @param input The component list to parse and load.
 */
class ElectromagneticMoat @SuppressFBWarnings constructor(
    input: List<String>
) {
  private val components: List<Component> = input.map { parseComponent(it) }
  private val componentMap: Map<Int, Set<Component>> =
      mutableMapOf<Int, Set<Component>>().apply {
        val temp = mutableMapOf<Int, MutableSet<Component>>()
        components.forEach { component ->
          temp.computeIfAbsent(component.port1) { mutableSetOf() }.add(component)
          temp.computeIfAbsent(component.port2) { mutableSetOf() }.add(component)
        }
        putAll(temp)
      }

  private fun parseComponent(line: String): Component {
    val split = line.split("/")
    val port1 = split[0].clean().toInt()
    val port2 = split[1].clean().toInt()

    return Component(port1, port2)
  }

  /**
   * A component in a bridge with two ports.
   *
   * @param port1 The first port.
   * @param port2 The second port.
   */
  data class Component(val port1: Int, val port2: Int) {
    /**
     * The strength of a component is the sum or its ports.
     */
    val strength: Int = port1 + port2

    /**
     * Returns true if the specified port has a matching connector in this component.
     *
     * @return True if the specified port has a matching connector in this component.
     */
    fun contains(port: Int): Boolean = isPort1(port) || isPort2(port)

    /**
     * Returns true if the specified port connects to this component via port1.
     *
     * @return True if the specified port connects to this component via port1.
     */
    fun isPort1(port: Int): Boolean = port == port1

    /**
     * Returns true if the specified port connects to this component via port2.
     *
     * @return True if the specified port connects to this component via port2.
     */
    fun isPort2(port: Int): Boolean = port == port2
  }

  /**
   * Builds the strongest possible bridge with the loaded components.
   */
  fun buildStrongestBridge(): List<Component> {
    // Always start with a component that has a port type of 0.
    val starts = componentMap[0] ?: error("Missing components with port type 0")
    val visited = mutableSetOf<Component>()
    var maxStrength = Int.MIN_VALUE
    var maxBridge = listOf<Component>()
    val pathSoFar = mutableListOf<Component>()

    starts.forEach { start ->
      val targetPort: Int = if (start.isPort1(0)) {
        start.port2
      } else {
        start.port1
      }
      pathSoFar.add(start)
      visited.add(start)

      val strongestFromHere =
          buildStrongestBridge(start, targetPort, start.strength, pathSoFar, visited)
      val strengthFromHere = strongestFromHere.sumOf { it.strength }

      if (strengthFromHere > maxStrength) {
        maxBridge = strongestFromHere.toList()
        maxStrength = strengthFromHere
      }
      pathSoFar.removeLast()
      visited.remove(start)
    }

    return maxBridge.toList()
  }

  @SuppressFBWarnings
  private fun buildStrongestBridge(
      component: Component,
      port: Int,
      strength: Int,
      path: MutableList<Component>,
      visited: MutableSet<Component>,
      currentMaxStrength: Int = Int.MIN_VALUE,
  ): List<Component> {
    val candidates = (componentMap[port] ?: emptyList()).filter { it != component }

    if (candidates.isEmpty()) {
      return if (strength > currentMaxStrength) {
        path.toList()
      } else {
        emptyList()
      }
    }
    var localMax = currentMaxStrength
    var localMaxPath: List<Component> = path

    for (candidate in candidates) {
      if (candidate !in visited) {
        visited.add(candidate)
        path.add((candidate))
        val newPort = if (candidate.isPort1(port)) {
          candidate.port2
        } else {
          candidate.port1
        }
        val newStrength = strength + candidate.strength
        val newPath = buildStrongestBridge(candidate, newPort, newStrength, path, visited, localMax)

        if (newPath.isNotEmpty()) {
          val tempStrength = newPath.sumOf { it.strength }
          if (tempStrength > localMax) {
            localMaxPath = newPath
            localMax = tempStrength
          }
        }

        path.removeLast()
        visited.remove(candidate)
      }
    }

    return localMaxPath
  }

  /**
   * Builds the strongest possible bridge with the loaded components.
   */
  fun buildLongestThenStrongestBridge(): List<Component> {
    // Always start with a component that has a port type of 0.
    val starts = componentMap[0] ?: error("Missing components with port type 0")
    val visited = mutableSetOf<Component>()
    var maxStrength = Int.MIN_VALUE
    var maxLength = Int.MIN_VALUE
    var maxBridge = listOf<Component>()
    val pathSoFar = mutableListOf<Component>()

    starts.forEach { start ->
      val targetPort: Int = if (start.isPort1(0)) {
        start.port2
      } else {
        start.port1
      }
      pathSoFar.add(start)
      visited.add(start)

      val longestThenStrongestFromHere =
          buildLongestThenStrongestBridge(
              start, targetPort, start.strength, 1, pathSoFar, visited
          )
      val strengthFromHere = longestThenStrongestFromHere.sumOf { it.strength }
      val lengthFromHere = longestThenStrongestFromHere.size

      if (lengthFromHere > maxLength
          || (lengthFromHere == maxLength && strengthFromHere > maxStrength)) {
        maxBridge = longestThenStrongestFromHere.toList()
        maxStrength = strengthFromHere
        maxLength = lengthFromHere
      }
      pathSoFar.removeLast()
      visited.remove(start)
    }

    return maxBridge.toList()
  }

  @SuppressFBWarnings
  private fun buildLongestThenStrongestBridge(
      component: Component,
      port: Int,
      strength: Int,
      length: Int,
      path: MutableList<Component>,
      visited: MutableSet<Component>,
      currentMaxStrength: Int = Int.MIN_VALUE,
      currentMaxLength: Int = Int.MIN_VALUE,
  ): List<Component> {
    val candidates = (componentMap[port] ?: emptyList()).filter { it != component }

    if (candidates.isEmpty()) {
      return if (length > currentMaxLength
          || (length == currentMaxLength && strength > currentMaxStrength)) {
        path.toList()
      } else {
        emptyList()
      }
    }
    var localMaxStrength = currentMaxStrength
    var localMaxLength = currentMaxLength
    var localMaxPath: List<Component> = path

    for (candidate in candidates) {
      if (candidate !in visited) {
        visited.add(candidate)
        path.add((candidate))
        val newPort = if (candidate.isPort1(port)) {
          candidate.port2
        } else {
          candidate.port1
        }
        val newStrength = strength + candidate.strength
        val newLength = length + 1
        val newPath = buildLongestThenStrongestBridge(
            candidate,
            newPort,
            newStrength,
            newLength,
            path,
            visited,
            localMaxStrength,
            localMaxLength
        )

        if (newPath.isNotEmpty()) {
          val tempStrength = newPath.sumOf { it.strength }
          val tempLength = newPath.size

          if (tempLength > localMaxLength
              || (tempLength == localMaxLength && tempStrength > localMaxStrength)) {
            localMaxPath = newPath
            localMaxStrength = tempStrength
            localMaxLength = tempLength
          }
        }

        path.removeLast()
        visited.remove(candidate)
      }
    }

    return localMaxPath
  }
}
