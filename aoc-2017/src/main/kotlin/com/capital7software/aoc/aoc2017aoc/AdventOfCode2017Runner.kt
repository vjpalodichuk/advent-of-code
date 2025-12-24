package com.capital7software.aoc.aoc2017aoc

import com.capital7software.aoc.lib.AdventOfCodeRunner

/**
 * The Day runner for the 2017 Advent of Code! Let the games begin!
 *
 * The night before Christmas, one of Santa's Elves calls you in a panic.
 * "The printer's broken! We can't print the **Naughty or Nice List**!"
 * By the time you make it to sub-basement 17, there are only a few minutes
 * until midnight. "We have a big problem," she says; "there must be almost **fifty**
 * bugs in this system, but nothing else can print The List. Stand in this square,
 * quick! There's no time to explain; if you can convince them to pay you in stars,
 * you'll be able to--" She pulls a lever and the world goes blurry.
 *
 * When your eyes can focus again, everything seems a lot more pixelated than before.
 * She must have sent you inside the computer! You check the system clock: **25 milliseconds**
 * until midnight. With that much time, you should be able to collect all fifty stars
 * by December 25th.
 *
 * Collect stars by solving puzzles. Two puzzles will be made available on each day
 * in the Advent calendar; the second puzzle is unlocked when you complete the first.
 * Each puzzle grants one star. Good luck!
 *
 */
class AdventOfCode2017Runner : AdventOfCodeRunner() {
  @Suppress("comments:UndocumentedPublicClass")
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
      val packageName = "com.capital7software.aoc.aoc2017aoc.days."

      runSolution(packageName, args)
    }
  }
}
