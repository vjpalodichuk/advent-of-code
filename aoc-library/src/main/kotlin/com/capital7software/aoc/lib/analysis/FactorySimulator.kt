package com.capital7software.aoc.lib.analysis

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.function.Consumer

/**
 * All [FactoryObject] have a [String] ID that uniquely identifies that instance within its class.
 * Meaning, that the ID should be unique among [FactoryObject.Bin] or [FactoryObject.Bot].
 *
 * Factory objects help run the factory. Bins provide values to bots and a place for bots to put
 * their output. Bots get their values from Bins and other Bots and send their output to Bins
 * and other Bots. [FactoryInstruction] determine what a Bin or Bot does.
 *
 * @param id The ID to use for this [FactoryObject]
 */
sealed class FactoryObject(val id: String) : Consumer<Int> {
  /**
   * Returns true if the Bin or Bot has seen all the specified values at least once.
   *
   * @return True if the Bin or Bot has seen all the specified values at least once.
   */
  abstract fun hasSeenAll(values: Collection<Int>): Boolean

  /**
   * Bins consume values from Bots and provide values to Bots when instructed to do so.
   *
   * @param id The ID to use for this [FactoryObject.Bin]
   */
  class Bin(
      id: String,
  ) : FactoryObject(id) {
    private val values: MutableList<Int> = mutableListOf()

    override fun accept(value: Int) {
      values.add(value)
    }

    override fun hasSeenAll(values: Collection<Int>): Boolean {
      return this.values.containsAll(values)
    }

    /**
     * Returns the sum of the values this Bin holds.
     *
     * @return The sum of the values this Bin holds.
     */
    fun sum() : Int = values.sum()
  }

  /**
   * Bots are the workhorse of the factory as they keep production rolling along! Bots consume
   * values from Bins and other Bots, and they send their values to other Bots and Bins when
   * they contain two values. After sending their values along, the values are erased from the
   * sending bot.
   *
   * @param id The ID to use for this [FactoryObject.Bot]
   */
  class Bot(
      id: String,
  ) : FactoryObject(id) {
    private val valuesSeen: MutableSet<Int> = mutableSetOf()
    private var lowValue: Int? = null
    private var highValue: Int? = null

    /**
     * The [Consumer] that this [Bot] sends it low value to.
     */
    var lowConsumer: Consumer<Int>? = null
    /**
     * The [Consumer] that this [Bot] sends it high value to.
     */
    var highConsumer: Consumer<Int>? = null

    override fun accept(value: Int) {
      valuesSeen.add(value)

      var currentLow = lowValue
      var currentHigh = highValue

      if (currentLow == null) {
        lowValue = value
      } else if (currentHigh == null) {
        if (value >= currentLow) {
          highValue = value
          currentHigh = value
        } else {
          highValue = currentLow
          currentHigh = currentLow
          lowValue = value
          currentLow = value
        }

        if (lowConsumer == null || highConsumer == null) {
          error("Both low and high Consumers need to be set!")
        }

        lowConsumer!!.accept(currentLow)

        lowValue = null

        highConsumer!!.accept(currentHigh)

        highValue = null
      }
    }

    override fun hasSeenAll(values: Collection<Int>): Boolean {
      return valuesSeen.containsAll(values)
    }
  }
}

/**
 * A class that is capable of executing instructions needed for the [FactorySimulator].
 */
sealed class FactoryInstruction {
  @Suppress("comments:UndocumentedPublicClass")
  companion object {
    /**
     * Creates and returns a [FactoryInstruction] that is capable or parsing and executing the
     * specified raw instruction. An [IllegalStateException] is thrown if no existing
     * instruction is capable of parsing and executing the specified raw instruction.
     *
     * @param instruction THe raw [String] instruction.
     * @return A [FactoryInstruction] instance setup to parse and execute the specified
     * raw [String] instruction.
     * @throws IllegalStateException If the specified raw [String] instruction cannot be
     * parsed and executed.
     */
    fun create(
        instruction: String,
    ): FactoryInstruction {
      return if (Value.matchRegEx.matches(instruction)) {
        Value(instruction)
      } else if (Bot.matchRegEx.matches(instruction)) {
        Bot(instruction)
      } else {
        error("Unknown FactoryInstruction encountered!")
      }
    }
  }

  /**
   * Runs the instruction. The bins and bots maps may be updated if this instruction has
   * to create any bins and bots that are needed that do not already exist. Returns true if
   * this instruction was processed.
   *
   * @param bins The [MutableMap] of bins that is keyed by the [String] ID of the bins and whose
   * value is the [FactoryObject.Bin] with that ID.
   * @param bots The [MutableMap] of bots that is keyed by the [String] ID of the bots and whose
   * value is the [FactoryObject.Bot] with that ID.
   * @return True if this instruction was processed.
   */
  abstract fun invoke(
      bins: MutableMap<String, FactoryObject.Bin>,
      bots: MutableMap<String, FactoryObject.Bot>,
  ): Boolean

  /**
   * The Value [FactoryInstruction] class is capable of parsing a Value based instruction.
   *
   * For example:
   *
   *     value 41 goes to bot 204
   *     value 17 goes to bot 155
   *     value 67 goes to bot 142
   *
   * When invoked, the Value instruction will create any Bots or Bins that are needed if they
   * do not already exist.
   *
   * @param instruction The raw instruction that is parsed when invoke is called.
   */
  class Value(
      val instruction: String,
  ) : FactoryInstruction() {
    @Suppress("comments:UndocumentedPublicClass")
    companion object {
      /**
       * The [Regex] used to match this instruction with raw instructions it is capable of
       * parsing
       */
      val matchRegEx = """value \d+ goes to bot \d+"""
          .toRegex()
        @SuppressFBWarnings
        get

      /**
       * The [Regex] used to extract the values from the raw instructions this instruction can
       * parse.
       */
      val extractRegex = """value (?<value>\d+) goes to bot (?<id>\d+)"""
          .toRegex()
        @SuppressFBWarnings
        get

      private const val REQUIRED_MATCH_GROUPS: Int = 2
    }

    override fun invoke(
        bins: MutableMap<String, FactoryObject.Bin>,
        bots: MutableMap<String, FactoryObject.Bot>,
    ): Boolean {
      val matchResult = extractRegex.find(instruction)

      if (matchResult == null || matchResult.groups.size < REQUIRED_MATCH_GROUPS) {
        return false
      }

      val value = matchResult.groups["value"]?.value?.toInt() ?: 0
      val id = matchResult.groups["id"]?.value ?: "unknown"

      bots.computeIfAbsent(id) { FactoryObject.Bot(id) }.accept(value)

      return true
    }
  }

  /**
   * The Bot [FactoryInstruction] class is capable of parsing a Bot based instruction.
   *
   * For example:
   *
   *     bot 2 gives low to output 21 and high to bot 13
   *     bot 7 gives low to bot 7 and high to bot 11
   *     bot 5 gives low to bot 23 and high to output 29
   *
   * When invoked, the Bot instruction will create any Bots or Bins that are needed if they
   * do not already exist.
   *
   * @param instruction The raw instruction that is parsed when invoke is called.
   */
  class Bot(
      val instruction: String,
  ) : FactoryInstruction() {
    @Suppress("comments:UndocumentedPublicClass")
    companion object {
      /**
       * The [Regex] used to match this instruction with raw instructions it is capable of
       * parsing
       */
      val matchRegEx = """bot \d+ gives low to (?:output|bot) \d+ and high to (?:output|bot) \d+"""
          .toRegex()
        @SuppressFBWarnings
        get
      /**
       * The [Regex] used to extract the values from the raw instructions this instruction can
       * parse.
       */
      val extractRegex = ("bot (?<botId>\\d+) gives low to (?<lowType>output|bot) (?<lowId>\\d+) "
          + "and high to (?<highType>output|bot) (?<highId>\\d+)")
          .toRegex()
        @SuppressFBWarnings
        get

      private const val REQUIRED_MATCH_GROUPS: Int = 5
    }

    override fun invoke(
        bins: MutableMap<String, FactoryObject.Bin>,
        bots: MutableMap<String, FactoryObject.Bot>,
    ): Boolean {
      val matchResult = extractRegex.find(instruction)

      if (matchResult == null || matchResult.groups.size < REQUIRED_MATCH_GROUPS) {
        return false
      }

      val botId = matchResult.groups["botId"]?.value?.lowercase()?.trim() ?: "unknown"
      val lowType = matchResult.groups["lowType"]?.value?.lowercase()?.trim() ?: "unknown"
      val lowId = matchResult.groups["lowId"]?.value?.lowercase()?.trim() ?: "unknown"
      val highType = matchResult.groups["highType"]?.value?.lowercase()?.trim() ?: "unknown"
      val highId = matchResult.groups["highId"]?.value?.lowercase()?.trim() ?: "unknown"

      val bot = bots.computeIfAbsent(botId) { FactoryObject.Bot(botId) }
      bot.lowConsumer = if (lowType == "bot") {
        bots.computeIfAbsent(lowId) { FactoryObject.Bot(lowId) }
      } else {
        bins.computeIfAbsent(lowId) { FactoryObject.Bin(lowId) }
      }
      bot.highConsumer = if (highType == "bot") {
        bots.computeIfAbsent(highId) { FactoryObject.Bot(highId) }
      } else {
        bins.computeIfAbsent(highId) { FactoryObject.Bin(highId) }
      }

      return true
    }
  }
}

/**
 * You come upon a factory in which many robots are zooming around handing small
 * microchips to each other.
 *
 * Upon closer examination, you notice that each bot only proceeds when it has
 * **two** microchips, and once it does, it gives each one to a different bot or puts
 * it in a marked "output" bin. Sometimes, bots take microchips from "input" bins, too.
 *
 * Inspecting one of the microchips, it seems like they each contain a single number;
 * the bots must use some logic to decide what to do with each chip. You access the
 * local control computer and download the bots' instructions (your puzzle input).
 *
 * Some of the instructions specify that a specific-valued microchip should be
 * given to a specific bot; the rest of the instructions indicate what a given
 * bot should do with its **lower-value** or **higher-value** chip.
 *
 * For example, consider the following instructions:
 *
 *    value 5 goes to bot 2
 *    bot 2 gives low to bot 1 and high to bot 0
 *    value 3 goes to bot 1
 *    bot 1 gives low to output 1 and high to bot 0
 *    bot 0 gives low to output 2 and high to output 0
 *    value 2 goes to bot 2
 *
 * - Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a value-2 chip
 * and a value-5 chip.
 * - Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and its
 * higher one (5) to bot 0.
 * - Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and gives
 * the value-3 chip to bot 0.
 * - Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
 *
 * In the end, output bin 0 contains a value-5 microchip, output bin 1 contains a
 * value-2 microchip, and output bin 2 contains a value-3 microchip. In this
 * configuration, bot number 2 is responsible for comparing value-5 microchips
 * with value-2 microchips.
 */
class FactorySimulator(instructions: List<FactoryInstruction>) {
  @Suppress("comments:UndocumentedPublicClass")
  companion object {
    /**
     * Builds and returns a new [FactorySimulator] instance loaded with
     * [FactoryInstruction] parsed from the [List] of raw [String] instructions.
     */
    @SuppressFBWarnings
    fun buildFactorySimulator(input: List<String>): FactorySimulator {
      return FactorySimulator(input.map { FactoryInstruction.create(it) })
    }
  }

  /**
   * The [FactoryInstruction] that are loaded in to this [FactorySimulator]
   */
  val instructions: List<FactoryInstruction> = instructions.toList()
    get() = field.toList()

  private val bins = mutableMapOf<String, FactoryObject.Bin>()
  private val bots = mutableMapOf<String, FactoryObject.Bot>()

  /**
   * Applies the all the [FactoryInstruction] that are loaded in this [FactorySimulator].
   * The [FactoryInstruction.Bot] instructions are applied first and then the
   * [FactoryInstruction.Value] instructions are applied. All instructions are applied in the
   * order that they have been loaded into this [FactorySimulator].
   *
   */
  @SuppressFBWarnings
  fun apply() {
    // Bot instructions have to be invoked prior to Value instructions to
    // ensure that all the Bots and Bins have been created.
    instructions.filterIsInstance<FactoryInstruction.Bot>()
        .forEach { it.invoke(bins, bots) }

    instructions.filterIsInstance<FactoryInstruction.Value>()
        .forEach { it.invoke(bins, bots) }
  }

  /**
   * Returns the ID of the bot that compares the specified [Collection] of [Int] values or null
   * if no bots have seen all the specified values.
   *
   * @param values The set of values that must have been seen in order to be returned.
   * @return The ID of the bot that compares the specified [Collection] of [Int] values or null
   * if no bots have seen all the specified values.
   */
  fun idOfBotThatCompares(values: Collection<Int>): String? {
    return bots
        .entries
        .firstOrNull { it.value.hasSeenAll(values) }
        ?.value
        ?.id
  }

  /**
   * Calculates and returns the product of the sum of the bin IDs specified in the
   * [Collection] of [String]. If any specified bin has a sum of 0, then the product will be
   * 0.
   *
   * @param binIds The [Collection] of [String] IDs of the bins to include in the calculation.
   * @return The product of the sum of the bin IDs specified in the [Collection] of [String].
   */
  @SuppressFBWarnings
  fun productOfOutputBins(binIds: Collection<String>): Long {
    return bins
        .entries
        .filter { binIds.contains(it.key) }
        .map { it.value.sum() }
        .reduce { a, b -> a * b }
        .toLong()
  }
}
