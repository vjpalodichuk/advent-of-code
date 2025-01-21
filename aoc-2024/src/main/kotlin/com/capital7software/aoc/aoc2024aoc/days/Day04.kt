package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.game.CeresSearch
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.system.measureNanoTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 4: Ceres Search ---**
 *
 * "Looks like the Chief's not here. Next!" One of The Historians pulls out a device and pushes
 * the only button on it. After a brief flash, you recognize the interior of the
 * [Ceres monitoring station](https://adventofcode.com/2019/day/10)!
 *
 * As the search for the Chief continues, a small Elf who lives on the station tugs on your shirt;
 * she'd like to know if you could help her with her **word search** (your puzzle input).
 * She only has to find one word: ```XMAS```.
 *
 * This word search allows words to be horizontal, vertical, diagonal, written backwards, or
 * even overlapping other words. It's a little unusual, though, as you don't merely need to
 * find one instance of ```XMAS``` - you need to find **all of them**. Here are a few ways
 * ```XMAS``` might appear, where irrelevant characters have been replaced with ```.```:
 *
 * ```
 * ..X...
 * .SAMX.
 * .A..A.
 * XMAS.S
 * .X....
 * ```
 *
 * The actual word search will be full of letters instead. For example:
 *
 * ```
 * MMMSXXMASM
 * MSAMXMSMSA
 * AMXSXMAAMM
 * MSAMASMSMX
 * XMASAMXAMM
 * XXAMMXXAMA
 * SMSMSASXSS
 * SAXAMASAAA
 * MAMMMXMMMM
 * MXMXAXMASX
 * ```
 *
 * In this word search, ```XMAS``` occurs a total of **```18```** times; here's the same word
 * search again, but where letters not involved in any ```XMAS``` have been replaced with ```.```:
 *
 * ```
 * ....XXMAS.
 * .SAMXMS...
 * ...S..A...
 * ..A.A.MS.X
 * XMASAMX.MM
 * X.....XA.A
 * S.S.S.S.SS
 * .A.A.A.A.A
 * ..M.M.M.MM
 * .X.X.XMASX
 * ```
 *
 * Take a look at the little Elf's word search. **How many times does ```XMAS``` appear?**
 *
 * Your puzzle answer was ```2530```.
 *
 * **--- Part Two ---**
 *
 * The Elf looks quizzically at you. Did you misunderstand the assignment?
 *
 * Looking for the instructions, you flip over the word search to find that this isn't actually
 * an **```XMAS```** puzzle; it's an **```X-MAS```** puzzle in which you're supposed to find
 * two ```MAS``` in the shape of an ```X```. One way to achieve that is like this:
 *
 * ```
 * M.S
 * .A.
 * M.S
 * ```
 *
 * Irrelevant characters have again been replaced with ```.``` in the above diagram.
 * Within the ```X```, each ```MAS``` can be written forwards or backwards.
 *
 * Here's the same example from before, but this time all the ```X-MAS```es have been kept instead:
 *
 * ```
 * .M.S......
 * ..A..MSMS.
 * .M.S.MAA..
 * ..A.ASMSM.
 * .M.S.M....
 * ..........
 * S.S.S.S.S.
 * .A.A.A.A..
 * M.M.M.M.M.
 * ..........
 * ```
 *
 * In this example, an ```X-MAS``` appears **```9```** times.
 *
 * Flip the word search from the instructions back over to the word search side and try again.
 * **How many times does an ```X-MAS``` appear?**
 *
 * Your puzzle answer was ```1921```.
 */
class Day04 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day04::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_04-01.txt"

  override fun runPart1(input: List<String>) {
    var answer: Int
    val elapsed = measureNanoTime {
      answer = calculateOccurrencesOfXmas(input)
    }
    log.info("$answer is the number of times XMAS occurs in the specified input!")
    logTimings(log, elapsed)
  }

  override fun runPart2(input: List<String>) {
    var answer: Int
    val elapsed = measureNanoTime {
      answer = calculateOccurrencesOfMas(input)
    }
    log.info("$answer is number of times pairs of MAS form an X in the specified input!")
    logTimings(log, elapsed)
  }

  /**
   * Returns the number of times XMAS appears in the input.
   *
   * @param input The list of strings to form the word search.
   * @return The number of times XMAS appears in the input.
   */
  @SuppressFBWarnings
  fun calculateOccurrencesOfXmas(input: List<String>): Int {
    return CeresSearch(input).findAll("XMAS").size
  }

  /**
   * Returns the number of times a pair of MAS form an X in the input.
   *
   * @param input The list of strings to form the word search.
   * @return The number of times a pair of MAS form an X in the input.
   */
  @SuppressFBWarnings
  fun calculateOccurrencesOfMas(input: List<String>): Int {
    return CeresSearch(input).findAllX("MAS").size
  }
}
