package com.capital7software.aoc.aoc2018aoc

import com.capital7software.aoc.lib.AdventOfCodeRunner

/**
 * "We've detected some temporal anomalies," one of Santa's Elves at the Temporal Anomaly
 * Research and Detection Instrument Station tells you. She sounded pretty worried when
 * she called you down here. "At 500-year intervals into the past, someone has been
 * changing Santa's history!"
 *
 * "The good news is that the changes won't propagate to our time stream for another
 * 25 days, and we have a device" - she attaches something to your wrist - "that will
 * let you fix the changes with no such propagation delay. It's configured to send you
 * 500 years further into the past every few days; that was the best we could do on
 * such short notice."
 *
 * "The bad news is that we are detecting roughly **fifty** anomalies throughout time;
 * the device will indicate fixed anomalies with stars. The other bad news is that
 * we only have one device and you're the best person for the job! Good lu--" She
 * taps a button on the device and you suddenly feel like you're falling. To save
 * Christmas, you need to get all fifty stars by December 25th.
 *
 * Collect stars by solving puzzles. Two puzzles will be made available on each
 * day in the Advent calendar; the second puzzle is unlocked when you complete
 * the first. Each puzzle grants one star. Good luck!
 */
class AdventOfCode2018Runner : AdventOfCodeRunner() {
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
      val packageName = "com.capital7software.aoc.aoc2018aoc.days."

      runSolution(packageName, args)
    }
  }
}
