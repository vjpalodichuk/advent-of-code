package com.capital7software.aoc.aoc2018aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.analysis.SuitClaims
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.system.measureNanoTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * **--- Day 3: No Matter How You Slice It ---**
 *
 * The Elves managed to locate the chimney-squeeze prototype fabric for Santa's suit
 * (thanks to someone who helpfully wrote its box IDs on the wall of the warehouse
 * in the middle of the night). Unfortunately, anomalies are still affecting them -
 * nobody can even agree on how to **cut** the fabric.
 *
 * The whole piece of fabric they're working on is a very large square -
 * at least 1000 inches on each side.
 *
 * Each Elf has made a **claim** about which area of fabric would be ideal for Santa's suit.
 * All claims have an ID and consist of a single rectangle with edges parallel to the edges
 * of the fabric. Each claim's rectangle is defined as follows:
 *
 * - The number of inches between the left edge of the fabric and the left edge of the rectangle.
 * - The number of inches between the top edge of the fabric and the top edge of the rectangle.
 * - The width of the rectangle in inches.
 * - The height of the rectangle in inches.
 *
 * A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a rectangle 3 inches from
 * the left edge, 2 inches from the top edge, 5 inches wide, and 4 inches tall. Visually,
 * it claims the square inches of fabric represented by # (and ignores the square inches
 * of fabric represented by .) in the diagram below:
 *
 * ```
 * ...........
 * ...........
 * ...#####...
 * ...#####...
 * ...#####...
 * ...#####...
 * ...........
 * ...........
 * ...........
 * ```
 *
 * The problem is that many of the claims **overlap**, causing two or more claims to cover part
 * of the same areas. For example, consider the following claims:
 *
 * ```
 * #1 @ 1,3: 4x4
 * #2 @ 3,1: 4x4
 * #3 @ 5,5: 2x2
 * ```
 *
 * Visually, these claim the following areas:
 *
 * ```
 * ........
 * ...2222.
 * ...2222.
 * .11XX22.
 * .11XX22.
 * .111133.
 * .111133.
 * ........
 * ```
 *
 * The four square inches marked with X are claimed by **both 1 and 2**. (Claim 3, while
 * adjacent to the others, does not overlap either of them.)
 *
 * If the Elves all proceed with their own plans, none of them will have enough fabric.
 * **How many square inches of fabric are within two or more claims?**
 *
 * Your puzzle answer was **110891**.
 *
 * **--- Part Two ---**
 *
 * Amidst the chaos, you notice that exactly one claim doesn't overlap by even a single
 * square inch of fabric with any other claim. If you can somehow draw attention to it,
 * maybe the Elves will be able to make Santa's suit after all!
 *
 * For example, in the claims above, only claim 3 is intact after all claims are made.
 *
 * **What is the ID of the only claim that doesn't overlap?**
 *
 * Your puzzle answer was **297**.
 */
class Day03 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day03::class.java)
  }

  override fun getDefaultInputFilename(): String = "inputs/input_day_03-01.txt"

  override fun runPart1(input: List<String>) {
    var answer: Int
    val elapsed = measureNanoTime {
      answer = getAreaOfOverlap(input)
    }
    log.info("$answer is the area of overlapping claims!")
    logTimings(log, elapsed)
  }

  override fun runPart2(input: List<String>) {
    var answer: SuitClaims.Claim
    val elapsed = measureNanoTime {
      answer = getNoOverlapClaim(input)
    }
    log.info("${answer.id} is the ID of the claim with no overlap! ${answer.rectangle}")
    logTimings(log, elapsed)
  }

  /**
   * Returns the area of overlap.
   *
   * @param input The list of claims.
   * @return The area of overlap.
   */
  @SuppressFBWarnings
  fun getAreaOfOverlap(input: List<String>): Int {
    return SuitClaims(input).overlapArea
  }

  /**
   * Returns the [SuitClaims.Claim] of the one claim that doesn't overlap any other claim.
   *
   * @param input The list of claims.
   * @return The [SuitClaims.Claim] that doesn't overlap any other claims.
   */
  @SuppressFBWarnings
  fun getNoOverlapClaim(input: List<String>): SuitClaims.Claim {
    return SuitClaims(input).noOverlap!!
  }
}
