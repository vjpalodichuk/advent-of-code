package com.capital7software.aoc.lib.game

import com.capital7software.aoc.lib.collection.PriorityQueue
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * >You come upon a column of four floors that have been entirely sealed off from the rest of
 * the building except for a small dedicated lobby. There are some radiation warnings and a
 * big sign which reads "Radioisotope Testing Facility".
 *
 * >According to the project status board, this facility is currently being used to
 * experiment with Radioisotope Thermoelectric Generators (RTGs, or simply
 * "generators") that are designed to be paired with specially-constructed microchips.
 * Basically, an RTG is a highly radioactive rock that generates electricity through heat.
 *
 * >The experimental RTGs have poor radiation containment, so they're dangerously
 * radioactive. The chips are prototypes and don't have normal radiation shielding,
 * but they do have the ability to **generate an electromagnetic radiation shield when
 * powered.** Unfortunately, they can **only** be powered by their corresponding RTG.
 * An RTG powering a microchip is still dangerous to other microchips.
 *
 * >In other words, if a chip is ever left in the same area as another RTG,
 * and it's not connected to its own RTG, the chip will be **fried**. Therefore, it
 * is assumed that you will follow procedure and keep chips connected to their
 * corresponding RTG when they're in the same room, and away from other RTGs otherwise.
 *
 * >These microchips sound very interesting and useful to your current activities,
 * and you'd like to try to retrieve them. The fourth floor of the facility has an
 * assembling machine which can make a self-contained, shielded computer for you to
 * take with you - that is, if you can bring it all the RTGs and microchips.
 *
 * >Within the radiation-shielded part of the facility (in which it's safe to have
 * these pre-assembly RTGs), there is an elevator that can move between the four
 * floors. Its capacity rating means it can carry at most yourself and two RTGs or
 * microchips in any combination. (They're rigged to some heavy diagnostic equipment -
 * the assembling machine will detach it for you.) As a security measure, the
 * elevator will only function if it contains at least one RTG or microchip. The
 * elevator always stops on each floor to recharge, and this takes long enough that
 * the items within it and the items on that floor can irradiate each other. (You can
 * prevent this if a Microchip and its Generator end up on the same floor in this way,
 * as they can be connected while the elevator is recharging.)
 *
 * >You make some notes of the locations of each component of interest (your puzzle input).
 * Before you don a hazmat suit and start moving things around, you'd like to have an
 * idea of what you need to do.
 *
 * **When you enter the containment area, you and the elevator will start on the first floor.**
 *
 * For example, suppose the isolated area has the following arrangement:
 *
 *    The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
 *    The second floor contains a hydrogen generator.
 *    The third floor contains a lithium generator.
 *    The fourth floor contains nothing relevant.
 *
 * As a diagram (F# for a Floor number, E for Elevator, H for Hydrogen, L for Lithium,
 * M for Microchip, and G for Generator), the initial state looks like this:
 *
 *    F4 .  .  .  .  .
 *    F3 .  .  .  LG .
 *    F2 .  HG .  .  .
 *    F1 E  .  HM .  LM
 *
 * Then, to get everything up to the assembling machine on the fourth floor,
 * the following steps could be taken:
 *
 * - Bring the Hydrogen-compatible Microchip to the second floor, which is safe because it
 * can get power from the Hydrogen Generator:
 *
 *
 *     F4 .  .  .  .  .
 *     F3 .  .  .  LG .
 *     F2 E  HG HM .  .
 *     F1 .  .  .  .  LM
 *
 * - Bring both Hydrogen-related items to the third floor, which is safe because the
 * Hydrogen-compatible microchip is getting power from its generator:
 *
 *
 *    F4 .  .  .  .  .
 *    F3 E  HG HM LG .
 *    F2 .  .  .  .  .
 *    F1 .  .  .  .  LM
 *
 * - Leave the Hydrogen Generator on floor three, but bring the Hydrogen-compatible
 * Microchip back down with you so you can still use the elevator:
 *
 *
 *    F4 .  .  .  .  .
 *    F3 .  HG .  LG .
 *    F2 E  .  HM .  .
 *    F1 .  .  .  .  LM
 *
 * - At the first floor, grab the Lithium-compatible Microchip, which is safe
 * because Microchips don't affect each other:
 *
 *
 *    F4 .  .  .  .  .
 *    F3 .  HG .  LG .
 *    F2 .  .  .  .  .
 *    F1 E  .  HM .  LM
 *
 * - Bring both Microchips up one floor, where there is nothing to fry them:
 *
 *
 *    F4 .  .  .  .  .
 *    F3 .  HG .  LG .
 *    F2 E  .  HM .  LM
 *    F1 .  .  .  .  .
 *
 * - Bring both Microchips up again to floor three, where they can be temporarily
 * connected to their corresponding generators while the elevator recharges,
 * preventing either of them from being fried:
 *
 *
 *    F4 .  .  .  .  .
 *    F3 E  HG HM LG LM
 *    F2 .  .  .  .  .
 *    F1 .  .  .  .  .
 *
 * - Bring both Microchips to the fourth floor:
 *
 *
 *    F4 E  .  HM .  LM
 *    F3 .  HG .  LG .
 *    F2 .  .  .  .  .
 *    F1 .  .  .  .  .
 *
 * - Leave the Lithium-compatible microchip on the fourth floor, but bring the
 * Hydrogen-compatible one, so you can still use the elevator; this is safe because
 * although the Lithium Generator is on the destination floor, you can connect
 * Hydrogen-compatible microchip to the Hydrogen Generator there:
 *
 *
 *    F4 .  .  .  .  LM
 *    F3 E  HG HM LG .
 *    F2 .  .  .  .  .
 *    F1 .  .  .  .  .
 *
 * - Bring both Generators up to the fourth floor, which is safe because you can
 * connect the Lithium-compatible Microchip to the Lithium Generator upon arrival:
 *
 *
 *    F4 E  HG .  LG LM
 *    F3 .  .  HM .  .
 *    F2 .  .  .  .  .
 *    F1 .  .  .  .  .
 *
 * - Bring the Lithium Microchip with you to the third floor so you can use the elevator:
 *
 *
 *    F4 .  HG .  LG .
 *    F3 E  .  HM .  LM
 *    F2 .  .  .  .  .
 *    F1 .  .  .  .  .
 *
 * - Bring both Microchips to the fourth floor:
 *
 *
 *    F4 E  HG HM LG LM
 *    F3 .  .  .  .  .
 *    F2 .  .  .  .  .
 *    F1 .  .  .  .  .
 *
 * In this arrangement, it takes 11 steps to collect all the objects at the fourth
 * floor for assembly. (Each elevator stop counts as one step, even if nothing
 * is added to or removed from it.)
 *
 * @param initialFloors The initial floor state for the new game.
 */
class GeneratorsAndChips(initialFloors: Collection<Floor>) {
  companion object {
    private val FLOOR_REGEX: Regex = """The (?<floor>\w+) floor contains""".toRegex()
    private val GENERATOR_REGEX: Regex = """a (?<generator>\w+) generator""".toRegex()
    private val MICROCHIP_REGEX: Regex = """a (?<microchip>\w+)-compatible microchip""".toRegex()
    private val NOTHING_RELEVANT: Regex = "nothing relevant".toRegex()

    /**
     * Builds and returns a new game instance with the specified [Floor] configuration.
     *
     * Please note that this does not validate that the loaded configuration is valid
     * and solvable.
     *
     * @param input The [List] of [String] that will be parsed into the [Floor]
     * configurations for this game.
     * @return The new game instance with the specified [Floor] configuration.
     */
    @SuppressFBWarnings
    fun buildGeneratorAndChips(input: List<String>): GeneratorsAndChips {
      return GeneratorsAndChips(input.map { parseFloor(it) })
    }

    private fun parseFloor(input: String): Floor {
      val id: Int = textToId(
          FLOOR_REGEX.find(input)?.groups?.get("floor")?.value?.lowercase()?.trim()
      )

      return if (NOTHING_RELEVANT.find(input) != null) {
        Floor(id, mapOf(), mapOf())
      } else {
        val genMap = mutableMapOf<String, GamePiece.Generator>()
        val micMap = mutableMapOf<String, GamePiece.Microchip>()

        var genMatches = GENERATOR_REGEX.find(input)

        while (genMatches != null) {
          val generator = genMatches
              .groups["generator"]?.value?.lowercase()?.trim() ?: error("Missing generator!")

          genMap[generator] = GamePiece.Generator(generator)

          genMatches = genMatches.next()
        }

        var micMatches = MICROCHIP_REGEX.find(input)

        while (micMatches != null) {
          val microchip = micMatches
              .groups["microchip"]?.value?.lowercase()?.trim() ?: error("Missing microchip!")

          micMap[microchip] = GamePiece.Microchip(microchip)

          micMatches = micMatches.next()
        }

        Floor(id, genMap, micMap)
      }
    }

    private fun textToId(text: String?) : Int {
      return when (text) {
        "first" -> 1
        "second" -> 2
        "third" -> 3
        "fourth" -> 4
        "fifth" -> 5
        "sixth" -> 6
        "seventh" -> 7
        "eighth" -> 8
        "ninth" -> 9
        "tenth" -> 10
        "eleventh" -> 11
        "twelfth" -> 12
        "thirteenth" -> 13
        "fourteenth" -> 14
        "fifteenth" -> 15
        else -> -1
      }
    }
  }

  private var floors = initialFloors.associateBy { it.id }.toMutableMap()

  /**
   * [Generator] and [Microchip] fill the various floors of the facility. A [Microchip] can only
   * be left on a floor without getting fried if that floor contains no [Generator] or it contains
   * the [Generator] that the [Microchip] connects to. A [Microchip] may only be carried on to
   * the elevator with another [Microchip] or the [Generator] it connects to. A [Microchip] and
   * [Generator] connect if they have the same name.
   *
   * @param name The name of the piece.
   */
  sealed class GamePiece(val name: String) {
    /**
     * A [Generator] is radioactive and will fry any [Microchip] that isn't safely connected
     * to its matching [Generator]. So, don't ever leave a [Microchip] with a [Generator] that
     * it doesn't connect to unless that [Microchip] is already safely connected to its
     * matching [Generator].
     *
     * @param name The name of the piece.
     */
    class Generator(name: String) : GamePiece(name) {
      override fun toString(): String {
        return "$name-generator"
      }
    }

    /**
     * A [Microchip] is sensitive to radiation and can easily be fried if it is left with a
     * [Generator] that it doesn't connect to unless it is already safely connected to its matching
     * [Generator].
     *
     * @param name The name of the piece.
     */
    class Microchip(name: String) : GamePiece(name) {
      override fun toString(): String {
        return "$name-microchip"
      }
    }
  }

  /**
   * Represents a Floor in the game.
   *
   * @param id The floor ID which should be its unique number.
   * @param generators The [Map] of generators, where the key is the name, to add to this floor.
   * @param microchips The [Map] of microchips, where the key is the name, to add to this floor.
   */
  @SuppressFBWarnings
  data class Floor(
      val id: Int,
      val generators: Map<String, GamePiece.Generator>,
      val microchips: Map<String, GamePiece.Microchip>,
  ) {
    /**
     * The number of [GamePiece.Generator] on this [Floor].
     */
    val generatorCount = generators.size
    /**
     * The number of [GamePiece.Microchip] on this [Floor].
     */
    val microchipCount = microchips.size
    /**
     * The total number of [GamePiece.Generator] and [GamePiece.Microchip] on this [Floor].
     */
    val size = generatorCount + microchipCount

    /**
     * True if this [Floor] has a fried [GamePiece.Microchip]
     */
    val hasFriedChips: Boolean by lazy {
      generatorCount > 0 && microchips.any { !generators.containsKey(it.key) }
    }

    /**
     * The [Set] of names for the [GamePiece.Generator] and [GamePiece.Microchip] pairs that
     * are on this [Floor].
     */
    val matchedPairs: Set<String> by lazy {
      generators.values.filter { microchips.containsKey(it.name) }.map { it.name }.toSet()
    }

    /**
     * Returns a new [Floor] instance with the specified piece removed.
     *
     * @param piece THe piece to remove.
     * @return A new [Floor] instance based on this instance with the specified pieces removed.
     */
    fun minus(piece: GamePiece): Floor {
      return when (piece) {
        is GamePiece.Generator -> Floor(id, generators.minus(piece.name), microchips)
        is GamePiece.Microchip -> Floor(id, generators, microchips.minus(piece.name))
      }
    }

    /**
     * Returns a new [Floor] instance with the specified [GamePiece.Generator] and
     * [GamePiece.Microchip] removed.
     *
     * @param generator The [GamePiece.Generator] to remove.
     * @param microchip The [GamePiece.Microchip] to remove.
     * @return A new [Floor] instance based on this instance with the specified pieces removed.
     */
    fun minus(generator: GamePiece.Generator, microchip: GamePiece.Microchip): Floor {
      return Floor(id, generators.minus(generator.name), microchips.minus(microchip.name))
    }

    /**
     * Returns a new [Floor] instance with the specified [Collection] of ][GamePiece.Generator]
     * removed.
     *
     * @param pieces The [Collection] of [GamePiece.Generator] to remove from this Floor.
     * @return A new [Floor] instance based on this instance with the specified pieces removed.
     *
     */
    fun minusGenerators(pieces: Collection<GamePiece.Generator>): Floor {
      return Floor(id, generators.minus(pieces.map { it.name }.toSet()), microchips)
    }

    /**
     * Returns a new [Floor] instance with the specified [Collection] of ][GamePiece.Microchip]
     * removed.
     *
     * @param pieces The [Collection] of [GamePiece.Microchip] to remove from this Floor.
     * @return A new [Floor] instance based on this instance with the specified pieces removed.
     *
     */
    fun minusMicrochips(pieces: Collection<GamePiece.Microchip>): Floor {
      return Floor(id, generators, microchips.minus(pieces.map { it.name }.toSet()))
    }

    /**
     * Returns a new [Floor] instance with the specified piece added.
     *
     * @param piece THe piece to add.
     * @return A new [Floor] instance based on this instance with the specified pieces added.
     */
    fun plus(piece: GamePiece): Floor {
      return when (piece) {
        is GamePiece.Generator -> Floor(id, generators.plus(Pair(piece.name, piece)), microchips)
        is GamePiece.Microchip -> Floor(id, generators, microchips.plus(Pair(piece.name, piece)))
      }
    }

    /**
     * Returns a new [Floor] instance with the specified [GamePiece.Generator] and
     * [GamePiece.Microchip] added.
     *
     * @param generator The [GamePiece.Generator] to add.
     * @param microchip The [GamePiece.Microchip] to add.
     * @return A new [Floor] instance based on this instance with the specified pieces added.
     */
    fun plus(generator: GamePiece.Generator, microchip: GamePiece.Microchip): Floor {
      return Floor(
          id,
          generators.plus(Pair(generator.name, generator)),
          microchips.plus(Pair(microchip.name, microchip))
      )
    }

    /**
     * Returns a new [Floor] instance with the specified [Collection] of ][GamePiece.Generator]
     * added.
     *
     * @param pieces The [Collection] of [GamePiece.Generator] to add to this Floor.
     * @return A new [Floor] instance based on this instance with the specified pieces added.
     *
     */
    fun plusGenerators(pieces: Collection<GamePiece.Generator>): Floor {
      return Floor(id, generators.plus(pieces.map { Pair(it.name, it) }.toSet()), microchips)
    }

    /**
     * Returns a new [Floor] instance with the specified [Collection] of ][GamePiece.Microchip]
     * added.
     *
     * @param pieces The [Collection] of [GamePiece.Microchip] to add to this Floor.
     * @return A new [Floor] instance based on this instance with the specified pieces added.
     *
     */
    fun plusMicrochips(pieces: Collection<GamePiece.Microchip>): Floor {
      return Floor(id, generators, microchips.plus(pieces.map { Pair(it.name, it) }.toSet()))
    }

  }

  /**
   * Represents a move in the game.
   *
   * @param toFloor The [Floor] the elevator moved to.
   * @param step The 1-based step this move represents.
   * @param pieces The [Set] of generators and microchips that are being moved.
   */
  @SuppressFBWarnings
  data class GameMove(
      val toFloor: Int,
      val step: Int,
      val pieces: Set<GamePiece>
  )

  /**
   * Represents the current state of the game.
   *
   * This class contains an AI that can solve the game from this state, if it is possible,
   * in the minimum number of steps!
   *
   * This class also can generate the [List] of [GameMove] that lead to this state.
   *
   * @param currentFloor The current floor that the elevator is on.
   * @param goalFloor The target floor to move all pieces to.
   * @param floors The [Map] of [Floor] where the [Floor.id] is the key.
   * @param steps The number of steps taken to get to this state.
   * @param parent The state that generated this state.
   */
  @SuppressFBWarnings
  class GameState(
      val currentFloor: Int,
      private val goalFloor: Int,
      val floors: Map<Int, Floor>,
      val steps: Int = 0,
      private val parent: GameState? = null,
  ) {
    /**
     * Calculating remaining steps:
     *
     * The number of steps remaining is always calculable; **the goal floor is never
     * included in the calculation**.
     *
     * The general formula is:
     *
     * - For floors below the elevator's current floor for all n:
     * ```2 * n```
     * - For floors at the elevator where n > 1 or above and n is even:
     * ```2 * n - 3```
     * - For floors above the elevator where n > 1 and n is odd:
     * ```
     *    if (chipCount > genCount) {
     *      2 * n - 1
     *    } else {
     *      2 * n - 3
     *    }
     * ```
     * - For floors at or above the elevator where n = 1:
     * ```1```
     * For example:
     * ```
     *     State after 5 moves:
     *
     *     F4 .  .  .  .  .
     *     F3 .  HG .  LG .
     *     F2 E  .  HM .  LM
     *     F1 .  .  .  .  .
     * ```
     * From the above state, there are 6 moves left:
     *
     * - Elevator is at floor 2 and there are no objects below it.
     * - Floor 2 n = 2 so our calculation is 2 * 2 - 3 which is 1.
     * - To calculate Floor 3, we add the objects from the previous floors to floor 3
     * and then apply the formula based on the total. In the example, there are 2 objects below
     * floor 3 and so n = 4 and because n > 1, we apply 2 * n - 3 which is 5.
     * - The steps remaining is then the sum of the total from each floor which is 0 (floor 1 is
     * empty), 1, and 5 which is 6!
     *
     * Here is another example from the same game:
     * ```
     *     State after 8 moves:
     *
     *     F4 .  .  .  .  LM
     *     F3 E  HG HM LG .
     *     F2 .  .  .  .  .
     *     F1 .  .  .  .  .
     * ```
     * From the above state, there are 3 moves left:
     *
     * - Elevator is at floor 3 and there are no objects below it.
     * - Floor 3 n = 3 which and because n > 1 the formula is 2 * n - 3 which is 3.
     */
    val stepsRemaining: Int by lazy {
      var n = 0
      var remaining = 0
      var genCount = 0
      var chipCount = 0

      for (floorId in 1 until goalFloor) {
        n += floors[floorId]?.size ?: 0
        chipCount += floors[floorId]?.microchipCount ?: 0
        genCount += floors[floorId]?.generatorCount ?: 0

        remaining += if (floorId < currentFloor) {
          2 * n // Elevator is above this floor so 2 * n
        } else if ((floorId == currentFloor || n % 2 == 0) && n > 1) {
          2 * n - 3 // Elevator is at or below this floor and n is greater than 1 or even
        } else if (floorId > currentFloor && n > 1 && chipCount > genCount) {
          2 * n - 1 // Elevator is below this floor and n is greater than 1 and odd
        } else if (floorId > currentFloor && n > 1) {
          2 * n - 3
        } else if (n == 1) {
          1 // Elevator is at or below this floor and n == 1
        } else {
          0 // Elevator is at or below this floor and n == 0
        }
      }

      remaining
    }

    /**
     * True if all [GamePiece] are on the [goalFloor]; otherwise false.
     */
    @SuppressFBWarnings
    private val allAtGoal: Boolean by lazy {
      floors
          .filter { it.key != goalFloor }
          .map { it.value.size }
          .sum() == 0
    }

    /**
     * True if the [currentFloor] is equal to the [goalFloor] and [allAtGoal] is also true;
     * otherwise false.
     */
    val isWin: Boolean by lazy {
      currentFloor == goalFloor && allAtGoal
    }

    /**
     * True if this [GameState] contains at least one [Floor] with a fried [GamePiece.Microchip]
     */
    val hasFriedChips: Boolean by lazy {
      floors.values.any { it.hasFriedChips }
    }

    /**
     * Returns all possible **unique** next states where the remaining number of steps is less
     * than this state's remaining number of steps or an empty list if no such state exists.
     *
     * **Uniqueness is determined by the type and quantity of [GamePiece] that are moved to a
     * different floor.**
     *
     * For example, suppose the isolated area has the following arrangement:
     *```
     *   The first floor contains a hydrogen-compatible microchip, a lithium-compatible microchip,
     *   a hydrogen generator, a lithium generator, a plutonium-compatible microchip, and a
     *   plutonium generator.
     *   The second floor contains nothing relevant.
     *   The third floor contains nothing relevant.
     *   The fourth floor contains nothing relevant.
     *```
     * As a diagram (F# for a Floor number, E for Elevator, H for Hydrogen, L for Lithium,
     * P for Plutonium, M for Microchip, and G for Generator), the initial state looks like this:
     *```
     *   F4 .  .  .  .  .  .  .
     *   F3 .  .  .  .  .  .  .
     *   F2 .  .  .  .  .  .  .
     *   F1 E  HG HM LG LM PG PM
     *```
     * - The above initial state can be solved in 27 steps (2 * 6 - 3) + (2 * 6 - 3) + (2 * 6 - 3)
     * = 9 + 9 + 9 = 27.
     *
     * - There are 24 possible moves (including duplicates and moves that result in frying a
     * microchip) from this initial state.
     *
     * - Any move that attempts to move a single [GamePiece.Generator] will result in a
     * [GamePiece.Microchip] being fried, so we can eliminate from consideration any move that is
     * for a single [GamePiece.Generator]. That reduces the move total to 21.
     *
     * - Any move that attempts to move two [GamePiece.Generator] will result in two
     * [GamePiece.Microchip] being fried, so we can eliminate from consideration any move that is
     * for two [GamePiece.Generator]. That reduces the move total to 15.
     *
     * - Any move that attempts to move a single [GamePiece.Microchip] will result in an
     * increase in the number of remaining steps (2 * 5) + (2 * 6 - 3) + (2 * 6 - 3) = 28, so
     * we can eliminate from consideration any move that is for a single [GamePiece.Microchip].
     * That reduces the move total to 12.
     *
     * - That means we can move any two [GamePiece.Microchip] together, or we can move a
     * [GamePiece.Generator] with its compatible [GamePiece.Microchip] together. There are 12
     * moves (including duplicates) possible for those two situations.
     *
     * - Eliminating the duplicate moves (3 G + M and 3 M + M) leaves six moves to consider.
     *
     * - Of those six moves, there are only two **unique** possible [GameState]: G + M was moved
     * or M + M was moved; it doesn't matter which G + M or M + M pair were moved.
     *
     * - Looking further, these two possible states both will lead to a solution.
     *
     * ```
     *     State after 1 move: 26 steps remaining.
     *
     *     F4 .  .  .  .  .  .  .    F4 .  .  .  .  .  .  .
     *     F3 .  .  .  .  .  .  .    F3 .  .  .  .  .  .  .
     *     F2 E  HG HM .  .  .  .    F2 E  .  HM .  LM .  .
     *     F1 .  .  .  LG LM PG PM   F1 .  HG .  LG .  PG PM
     *
     *     State after 2 moves: 25 steps remaining.
     *
     *     F4 .  .  .  .  .  .  .    F4 .  .  .  .  .  .  .
     *     F3 E  HG HM .  .  .  .    F3 E  .  HM .  LM .  .
     *     F2 .  .  .  .  .  .  .    F2 .  .  .  .  .  .  .
     *     F1 .  .  .  LG LM PG PM   F1 .  HG .  LG .  PG PM
     *
     *     State after 3 moves: 24 steps remaining.
     *
     *     F4 E  HG HM .  .  .  .    F4 E  .  HM .  LM .  .
     *     F3 .  .  .  .  .  .  .    F3 .  .  .  .  .  .  .
     *     F2 .  .  .  .  .  .  .    F2 .  .  .  .  .  .  .
     *     F1 .  .  .  LG LM PG PM   F1 .  HG .  LG .  PG PM
     *
     *     State after 4 moves: 23 steps remaining.
     *
     *     F4 .  .  HM .  .  .  .    F4 .  .  .  .  LM .  .
     *     F3 E  HG .  .  .  .  .    F3 E  .  HM .  .  .  .
     *     F2 .  .  .  .  .  .  .    F2 .  .  .  .  .  .  .
     *     F1 .  .  .  LG LM PG PM   F1 .  HG .  LG .  PG PM
     *
     *     State after 5 moves: 22 steps remaining.
     *
     *     F4 .  .  HM .  .  .  .    F4 .  .  .  .  LM .  .
     *     F3 .  .  .  .  .  .  .    F3 .  .  .  .  .  .  .
     *     F2 E  HG .  .  .  .  .    F2 E  .  HM .  .  .  .
     *     F1 .  .  .  LG LM PG PM   F1 .  HG .  LG .  PG PM
     *
     *     State after 6 moves: 21 steps remaining.
     *
     *     F4 .  .  HM .  .  .  .    F4 .  .  .  .  LM .  .
     *     F3 .  .  .  .  .  .  .    F3 .  .  .  .  .  .  .
     *     F2 .  .  .  .  .  .  .    F2 .  .  .  .  .  .  .
     *     F1 E  HG .  LG LM PG PM   F1 E  HG HM LG .  PG PM
     * ```
     *
     * - Notice how after the 3rd move in the first state (HG+HM) that it **matters** which piece,
     * HG or HM, is moved down whereas in the second state, it does **not matter** if HM or LM is
     * brought down. If HM had been moved in the first state, then that game would end after
     * move 5 as HM cannot be moved back to F1 without HG.
     *
     * A [GameState] is returned in the [Set] if it meets **all these conditions:**
     * - It has fewer steps than this [GameState].
     * - Moving the piece(s) from this state's [currentFloor] doesn't fry any [GamePiece.Microchip]
     * that remain on this state's [currentFloor].
     * - No [GamePiece.Microchip] on the destination floor (if any) are fried by the piece(s)
     * that are moved there.
     * - The new [GameState] is **not** equal to this state's parent (if any).
     *
     * @return A [Set] of all possible **unique** next [GameState] states where the remaining
     * number of steps is less than this state's remaining number of steps.
     */
    fun getNextStates(): Set<GameState> {
      if (isWin) return setOf()

      val floor = floors[currentFloor] ?: error("$currentFloor doesn't exist in floors!")
      val floorAbove = if (currentFloor < goalFloor) {
        floors[currentFloor + 1] ?: error("${currentFloor + 1} doesn't exist in floors!")
      } else {
        null
      }
      val floorBelow = if (currentFloor > 1) {
        floors[currentFloor - 1] ?: error("${currentFloor - 1} doesn't exist in floors!")
      } else {
        null
      }

      val tryPairs = floor.matchedPairs.isNotEmpty()
      val newStates = mutableSetOf<GameState>()
      val newSteps = steps + 1
      val processed = mutableSetOf<GamePiece>()
      val queue = ArrayDeque<GamePiece>(floor.size).apply {
        addAll(floor.generators.values)
        addAll(floor.microchips.values)
      }

      while (queue.isNotEmpty()) {
        val piece = queue.removeFirst()

        processed.add(piece)

        if (piece is GamePiece.Generator) {
          if (tryPairs && floor.matchedPairs.contains(piece.name)) {
            handleMatchedPair(piece, floor, floorAbove, floorBelow, newSteps, newStates)
          }
        }

        handleDouble(piece, floor, floorAbove, floorBelow, newSteps, newStates, processed)
        handleSingle(piece, floor, floorAbove, floorBelow, newSteps, newStates)
      }

      return newStates
    }

    private fun handleSingle(
        piece: GamePiece,
        floor: Floor,
        floorAbove: Floor?,
        floorBelow: Floor?,
        newSteps: Int,
        newStates: MutableSet<GameState>
    ) {
      when (piece) {
        is GamePiece.Generator -> {
          val floorWithout = floor.minus(piece)
          if (floorAbove != null && !floorWithout.hasFriedChips) {
            val floorWith = floorAbove.plus(piece)
            handleBuildState(floorWithout, floorWith, newSteps, newStates)
          }
          if (floorBelow != null && !floorWithout.hasFriedChips) {
            val floorWith = floorBelow.plus(piece)
            handleBuildState(floorWithout, floorWith, newSteps, newStates)
          }
        }

        is GamePiece.Microchip -> {
          val floorWithout = floor.minus(piece)
          if (floorAbove != null && !floorWithout.hasFriedChips) {
            val floorWith = floorAbove.plus(piece)
            handleBuildState(floorWithout, floorWith, newSteps, newStates)
          }
          if (floorBelow != null && !floorWithout.hasFriedChips) {
            val floorWith = floorBelow.plus(piece)
            handleBuildState(floorWithout, floorWith, newSteps, newStates)
          }
        }
      }
    }

    private fun handleDouble(
        piece: GamePiece,
        floor: Floor,
        floorAbove: Floor?,
        floorBelow: Floor?,
        newSteps: Int,
        newStates: MutableSet<GameState>,
        processed: MutableSet<GamePiece>
    ) {
      when (piece) {
        is GamePiece.Generator -> {
          floor.generators
              .filter { it.key != piece.name && !processed.contains(it.value) }
              .forEach {
                val list = listOf(piece, it.value)
                val floorWithout = floor.minusGenerators(list)
                if (floorAbove != null && !floorWithout.hasFriedChips) {
                  val floorWith = floorAbove.plusGenerators(list)
                  handleBuildState(floorWithout, floorWith, newSteps, newStates)
                }
                if (floorBelow != null && !floorWithout.hasFriedChips) {
                  val floorWith = floorBelow.plusGenerators(list)
                  handleBuildState(floorWithout, floorWith, newSteps, newStates)
                }
              }
        }

        is GamePiece.Microchip -> {
          floor.microchips
              .filter { it.key != piece.name && !processed.contains(it.value) }
              .forEach {
                val list = listOf(piece, it.value)
                val floorWithout = floor.minusMicrochips(list)
                if (floorAbove != null && !floorWithout.hasFriedChips) {
                  val floorWith = floorAbove.plusMicrochips(list)
                  handleBuildState(floorWithout, floorWith, newSteps, newStates)
                }
                if (floorBelow != null && !floorWithout.hasFriedChips) {
                  val floorWith = floorBelow.plusMicrochips(list)
                  handleBuildState(floorWithout, floorWith, newSteps, newStates)
                }
              }
        }
      }
    }

    private fun handleMatchedPair(
        piece: GamePiece.Generator,
        floor: Floor,
        floorAbove: Floor?,
        floorBelow: Floor?,
        newSteps: Int,
        newStates: MutableSet<GameState>
    ) {
      val chip = floor.microchips[piece.name] ?: error("Missing microchip from pair!")
      val floorWithout = floor.minus(piece, chip) // Always OK to move a pair
      if (floorAbove != null) {
        val floorWith = floorAbove.plus(piece, chip)
        handleBuildState(floorWithout, floorWith, newSteps, newStates)
      }
      if (floorBelow != null) {
        val floorWith = floorBelow.plus(piece, chip)
        handleBuildState(floorWithout, floorWith, newSteps, newStates)
      }
    }

    private fun handleBuildState(
        source: Floor,
        target: Floor,
        newSteps: Int,
        newStates: MutableSet<GameState>,
    ) {
      if (!target.hasFriedChips) {
        val state = buildState(source, target, newSteps)

        if (state != null) {
          newStates.add(state)
        }
      }
    }

    private fun buildState(source: Floor, target: Floor, newSteps: Int): GameState? {
      val newFloors = mutableMapOf<Int, Floor>().apply { putAll(floors) }
      newFloors[source.id] = source
      newFloors[target.id] = target
      val newState = GameState(
          target.id,
          goalFloor,
          newFloors,
          newSteps,
          this
      )

      return if (newState.stepsRemaining < stepsRemaining) {
        newState
      } else {
        null
      }
    }

    /**
     * Generates and returns a [List] of [GameMove] that lead to this state.
     *
     * @return A [List] of [GameMove] that lead to this state.
     */
    fun gameMoves() : List<GameMove> {
      val moves = mutableListOf<GameMove>()
      collectGameMoves(moves)
      return moves
    }

    private fun collectGameMoves(moves: MutableList<GameMove>) {
      if (parent != null) {
        parent.collectGameMoves(moves)
        moves.add(GameMove(currentFloor, steps, getMovedPieces()))
      }
    }

    private fun getMovedPieces() : Set<GamePiece> {
      return if (parent == null) {
        setOf()
      } else {
        val gens = mutableSetOf<GamePiece.Generator>()
        val chips = mutableSetOf<GamePiece.Microchip>()
        val pieces = mutableSetOf<GamePiece>()

        parent.floors.values.forEach {
          val f = floors[it.id]
          if (f != null) {
            if (it.generatorCount > f.generatorCount) {
              gens.addAll(it.generators.values.toSet() - f.generators.values.toSet())
            } else {
              gens.addAll(f.generators.values.toSet() - it.generators.values.toSet())
            }

            if (it.microchipCount > f.microchipCount) {
              chips.addAll(it.microchips.values.toSet() - f.microchips.values.toSet())
            } else {
              chips.addAll(f.microchips.values.toSet() - it.microchips.values.toSet())
            }
          }
        }
        pieces.addAll(gens)
        pieces.addAll(chips)
        pieces
      }
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is GameState) return false

      if (currentFloor != other.currentFloor) return false
      if (floors.keys != other.floors.keys) return false
      for (floor in floors.keys) {
        if (floors[floor]?.generatorCount != other.floors[floor]?.generatorCount) return false
        if (floors[floor]?.microchipCount != other.floors[floor]?.microchipCount) return false
      }

      return true
    }

    override fun hashCode(): Int {
      var result = 31 + currentFloor.hashCode()
      for (floor in floors.keys.sorted()) {
        result = 31 * result + (floors[floor]?.size.hashCode())
      }
      return result
    }
  }

  /**
   * Adds a new [GamePiece.Generator] to the first [Floor] with the specified name.
   */
  fun addGenerator(name: String) {
    floors[1] = floors[1]?.plus(GamePiece.Generator(name)) ?: error("Missing 1st floor!!")
  }

  /**
   * Adds a new [GamePiece.Microchip] to the first [Floor] with the specified name.
   */
  fun addMicrochip(name: String) {
    floors[1] = floors[1]?.plus(GamePiece.Microchip(name)) ?: error("Missing 1st floor!!")
  }

  /**
   * Attempts to solve this game. Returns a [Pair] where the first element is the minimum
   * number of moves to solve the game from the starting state and the second element
   * is a [List] of [GameMove] to solve this game. If the [GameMove] [List] is empty, then
   * no solution was found.
   */
  fun solve(): Pair<Int, List<GameMove>> {
    val start = GameState(1, floors.keys.maxOrNull() ?: floors.size, floors)

    if (start.hasFriedChips) {
      error("Starting state is not valid as it has fried microchips!")
    }

    val minState = findWinInMinSteps(start)

    val moves: List<GameMove> = minState?.gameMoves() ?: listOf()

    return Pair(start.stepsRemaining, moves)
  }

  private fun findWinInMinSteps(initial: GameState): GameState? {
    val initialCapacity = 50

    val gameStates = PriorityQueue(
        initialCapacity,
        Comparator
            .comparingInt<Triple<Int, Int, GameState>> { it.first }
            .thenComparingInt { it.second }
    )

    val openStates = mutableSetOf<GameState>().apply { add(initial) }
    val closedStates = mutableSetOf<GameState>()

    gameStates.add(Triple(0, 0, initial))

    var stateId = 0

    while (gameStates.isNotEmpty()) {
      val currentEntry = gameStates.poll()
      val current = currentEntry.third

      if (current.isWin) {
        return current
      }

      openStates.remove(current)
      closedStates.add(current)

      for (state in current.getNextStates()) {
        if (closedStates.contains(state) || openStates.contains(state)) {
          continue
        }
        openStates.add(state)
        gameStates.offer(Triple(state.stepsRemaining, stateId++, state))
      }
    }

    return null
  }
}
