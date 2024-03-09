package com.capital7software.aoc.lib.computer

import com.capital7software.aoc.lib.string.clean
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.math.abs

/**
 * Wandering further through the circuits of the computer, you come upon a tower of programs
 * that have gotten themselves into a bit of trouble. A recursive algorithm has gotten out of
 * hand, and now they're balanced precariously in a large tower.
 *
 * One program at the bottom supports the entire tower. It's holding a large disc, and on
 * the disc are balanced several more sub-towers. At the bottom of these sub-towers,
 * standing on the bottom disc, are other programs, each holding **their** own disc, and so on.
 * At the very tops of these sub-sub-sub-...-towers, many programs stand simply keeping
 * the disc below them balanced but with no disc of their own.
 *
 * You offer to help, but first you need to understand the structure of these towers.
 * You ask each program to yell out their **name**, their **weight**, and
 * (if they're holding a disc) the **names of the programs immediately above** them balancing
 * on that disc. You write this information down (your puzzle input). Unfortunately,
 * in their panic, they don't do this in an orderly fashion; by the time you're done,
 * you're not sure which program gave which information.
 *
 * @param input The [List] of [String] which is the list of programs and the programs they support.
 */
class RecursiveCircus(input: List<String>) {
  private data class Program(val name: String, val weight: Int, val supports: Set<String>) {
    fun towerWeight(programs: Map<String, Program>): Pair<Int, Int> {
      return if (supports.isNotEmpty()) {
        var sum = 0

        supports.forEach { name ->
          val program = programs[name] ?: error("Missing $name in programs map!")
          val temp = program.towerWeight(programs)
          sum += temp.first
          sum += temp.second
        }
        Pair(weight, sum)
      } else {
        Pair(weight, 0)
      }
    }

    fun findUnbalanced(programs: Map<String, Program>): List<Pair<Program, Int>> {
      return if (supports.isEmpty()) {
        listOf()
      } else {
        val weights = mutableMapOf<Int, MutableList<Program>>()

        supports.forEach { name ->
          val program = programs[name] ?: error("Missing $name in programs map!")
          val temp = program.towerWeight(programs)
          weights.computeIfAbsent(temp.first + temp.second) { mutableListOf() }.add(program)
        }

        val answer = mutableListOf<Pair<Program, Int>>()
        weights.filter { it.value.size < 2 }.forEach { (key, value) ->
          value.forEach {
            val unbalanced = it.findUnbalanced(programs)
            val desired = weights.filter { wt -> wt.value.size >= 2 }.keys.first()

            if (unbalanced.isEmpty()) {
              val diff = abs(desired - key)
              if (desired > key) {
                answer.add(Pair(it, it.weight + diff))
              } else {
                answer.add(Pair(it, it.weight - diff))
              }
            } else {
              answer.addAll(unbalanced)
            }
          }
        }
        answer
      }
    }
  }

  private val programs: Map<String, Program> by lazy { inputToPrograms(input) }

  /**
   * The program at the bottom holding up all the other programs.
   *
   * For example, if your list is the following:
   *
   * ```
   * pbga (66)
   * xhth (57)
   * ebii (61)
   * havc (66)
   * ktlj (57)
   * fwft (72) -> ktlj, cntj, xhth
   * qoyq (66)
   * padx (45) -> pbga, havc, qoyq
   * tknk (41) -> ugml, padx, fwft
   * jptl (61)
   * ugml (68) -> gyxo, ebii, jptl
   * gyxo (61)
   * cntj (57)
   * ```
   *
   * ...then you would be able to recreate the structure of the towers that looks like this:
   *
   * ```
   *                 gyxo
   *               /
   *          ugml - ebii
   *        /      \
   *       |         jptl
   *       |
   *       |         pbga
   *      /        /
   * tknk --- padx - havc
   *      \        \
   *       |         qoyq
   *       |
   *       |         ktlj
   *        \      /
   *          fwft - cntj
   *               \
   *                 xhth
   * ```
   *
   * In this example, tknk is at the bottom of the tower (the **bottom program**), and is
   * holding up ugml, padx, and fwft. Those programs are, in turn, holding up other programs;
   * in this example, none of those programs are holding up any other programs, and are all
   * the tops of their own towers. (The actual tower balancing in front of you is much larger.)
   */
  val bottom: String by lazy { programAtBottom() }

  @SuppressFBWarnings
  private fun inputToPrograms(input: List<String>): Map<String, Program> {
    return input.map {
      val split = it.split(" -> ".toRegex())

      val nameAndWeight = split[0].split(" ")
      val name = nameAndWeight[0]
      val weight = nameAndWeight[1]
          .replace("(", "")
          .replace(")", "")
          .clean()
          .toInt()

      val held = if (split.size > 1) {
        split[1].split(", ".toRegex()).toSet()
      } else {
        setOf()
      }
      Program(name, weight, held)
    }.associateBy { it.name }
  }

  @SuppressFBWarnings
  private fun programAtBottom(): String {
    val holding = programs.values.filter { it.supports.isNotEmpty() }
    var answer = ""

    holding.forEach { program ->
      if (answer == "") {
        val up = holding.filter { it.supports.contains(program.name) }

        if (up.isEmpty()) {
          answer = program.name
        }
      }
    }

    return answer
  }

  /**
   * The programs explain the situation: they can't get down. Rather, they **could** get down,
   * if they weren't expending all of their energy trying to keep the tower balanced.
   * Apparently, one program has the **wrong weight**, and until it's fixed, they're stuck here.
   *
   * For any program holding a disc, each program standing on that disc forms a sub-tower.
   * Each of those sub-towers are supposed to be the same weight, or the disc itself isn't balanced.
   * The weight of a tower is the sum of the weights of the programs in that tower.
   *
   * In the example above, this means that for ugml's disc to be balanced, gyxo, ebii,
   * and jptl must all have the same weight, and they do: 61.
   *
   * However, for tknk to be balanced, each of the programs standing on its disc
   * **and all programs above it** must each match. This means that the following sums
   * must all be the same:
   *
   * - ugml + (gyxo + ebii + jptl) = 68 + (61 + 61 + 61) = 251
   * - padx + (pbga + havc + qoyq) = 45 + (66 + 66 + 66) = 243
   * - fwft + (ktlj + cntj + xhth) = 72 + (57 + 57 + 57) = 243
   *
   * As you can see, tknk's disc is unbalanced: ugml's stack is heavier than the other two.
   * Even though the nodes above ugml are balanced, ugml itself is too heavy: it needs to be
   * 8 units lighter for its stack to weigh 243 and keep the towers balanced. If this change
   * were made, its weight would be 60.
   *
   * @return The name of the unbalanced program and what it should weigh.
   */
  fun getUnbalancedProgram(): Pair<String, Int> {
    val unbalanced = programs[bottom]?.findUnbalanced(programs)
        ?: error("$bottom missing from programs map!")
    val prog = unbalanced.first().first
    val shouldbe = unbalanced.first().second
    return Pair(prog.name, shouldbe)
  }
}
