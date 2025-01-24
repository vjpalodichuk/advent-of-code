package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeSolution
import com.capital7software.aoc.lib.computer.SmallComputer
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * --- Day 25: Clock Signal ---
 * You open the door and find yourself on the roof. The city sprawls away from you for
 * miles and miles.
 *
 * There's not much time now - it's already Christmas, but you're nowhere near the
 * North Pole, much too far to deliver these stars to the sleigh in time.
 *
 * However, maybe the **huge antenna** up here can offer a solution. After all,
 * the sleigh doesn't need the stars, exactly; it needs the timing data they provide,
 * and you happen to have a massive signal generator right here.
 *
 * You connect the stars you have to your prototype computer, connect that to the antenna,
 * and begin the transmission.
 *
 * Nothing happens.
 *
 * You call the service number printed on the side of the antenna and quickly
 * explain the situation. "I'm not sure what kind of equipment you have connected over there,"
 * he says, "but you need a clock signal." You try to explain that this is a signal for a clock.
 *
 * "No, no, a clock signal - timing information so the antenna computer knows how to read
 * the data you're sending it. An endless, alternating pattern of 0, 1, 0, 1, 0, 1, 0, 1, 0, 1...."
 * He trails off.
 *
 * You ask if the antenna can handle a clock signal at the frequency you would need to use
 * for the data from the stars. "There's **no way** it can! The only antenna we've installed
 * capable of **that** is on top of a top-secret Easter Bunny installation,
 * and you're **definitely** not-" You hang up the phone.
 *
 * You've extracted the antenna's clock signal generation assembunny code (your puzzle input);
 * it looks mostly compatible with code you worked on just recently.
 *
 * This antenna code, being a signal generator, uses one extra instruction:
 *
 * - out x **transmits** x (either an integer or the value of a register) as the next value for
 * the clock signal.
 *
 * The code takes a value (via register a) that describes the signal to generate,
 * but you're not sure how it's used. You'll have to find the input to produce the right
 * signal through experimentation.
 *
 * What is the lowest positive integer that can be used to initialize register a and cause
 * the code to output a clock signal of 0, 1, 0, 1... repeating forever?
 *
 * Your puzzle answer was 192.
 *
 * -**-- Part Two ---**
 *
 * The antenna is ready. Now, all you need is the fifty stars required to generate the
 * signal for the sleigh, but you don't have enough.
 *
 * You look toward the sky in desperation... suddenly noticing that a lone star has been
 * installed at the top of the antenna! Only 49 more to go.
 *
 * You have enough stars to **Transmit the Signal.**
 */
class Day25 : AdventOfCodeSolution {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(Day25::class.java)
  }

  override fun getDefaultInputFilename(): String {
    return "inputs/input_day_25-01.txt"
  }

  override fun runPart1(input: List<String>) {
    val start = Instant.now()
    val answer = getLowestPositiveInteger(input)
    val end = Instant.now()

    log.info("$answer is the lowest positive integer to generate the infinite signal!")
    logTimings(log, start, end)
  }

  override fun runPart2(input: List<String>) {
    val start = Instant.now()
    val answer = "The signal has been transmitted!"
    val end = Instant.now()

    log.info(answer)
    logTimings(log, start, end)
  }

  /**
   * Calculates and returns the lowest positive integer that can be set in register 'a' to
   * generate an infinite signal of 0, 1, 0, 1, 0, 1...
   *
   * @param input The [List] of [String] instructions to parse and execute.
   * @return The fewest number of steps starting at source and visiting each node at least once.
   */
  @SuppressFBWarnings
  fun getLowestPositiveInteger(input: List<String>): Int {
    val outputs = mutableListOf<Long>()
    val instance = SmallComputer.buildSmallComputer(
        input,
        outputHandler = { output: Long -> outputs.add(output) }
    )
    var answer = 0
    val target = listOf<Long>(0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
    for (i in 0..<Int.MAX_VALUE) {
      instance["a"] = i.toLong()
      outputs.clear()
      instance.runInfinite(max = 20)
      if (target == outputs) {
        answer = i
        break
      }
    }
    return answer
  }
}
