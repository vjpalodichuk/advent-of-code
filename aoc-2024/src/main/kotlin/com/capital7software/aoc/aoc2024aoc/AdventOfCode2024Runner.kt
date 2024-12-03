package com.capital7software.aoc.aoc2024aoc

import com.capital7software.aoc.lib.AdventOfCodeRunner

/**
 * The **Chief Historian** is always present for the big Christmas sleigh launch, but nobody has
 * seen him in months! Last anyone heard, he was visiting locations that are historically
 * significant to the North Pole; a group of Senior Historians has asked you to accompany
 * them as they check the places they think he was most likely to visit.
 *
 * As each location is checked, they will mark it on their list with a **star**. They figure the
 * Chief Historian **must** be in one of the first fifty places they'll look, so in order to save
 * Christmas, you need to help them get **fifty stars** on their list before Santa takes off on
 * December 25th.
 *
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the
 * Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle
 * grants **one star**. Good luck!
 *
 */
class AdventOfCode2024Runner : AdventOfCodeRunner() {
  companion object {
    /**
     * The main entrypoint of the runner. The first argument must be the name of a solution to
     * run. The solutions are typically the DayXX classes where XX is the numerical day to
     * execute.
     *
     * @param args The arguments to the program. The first required argument is the day class to
     * run, such as Day01 to run the Day01 solution. The second argument is optional and is the
     * filename to use as input for the run.
     */
    @JvmStatic
    fun main(args: Array<String>) {
      val packageName = "com.capital7software.aoc.aoc2024aoc.days."

      runSolution(packageName, args)
    }
  }
}
