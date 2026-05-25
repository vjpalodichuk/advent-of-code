package com.capital7software.aoc.lib.math

/**
 * Models the Monkey Exchange Market secret-number generator.
 *
 * Each buyer starts with an initial secret number. A buyer's next secret is produced by applying
 * the puzzle's mix-and-prune process. Secrets are always pruned to 24 bits.
 *
 * @param initialSecrets The initial secret number for each buyer.
 */
class MonkeyMarket private constructor(
    private val initialSecrets: List<Long>
) {
  /**
   * Provides the static [parse] method.
   */
  companion object {
    private const val PRUNE_MASK = 0xFFFFFFL
    private const val DEFAULT_GENERATIONS = 2000
    private const val PRICE_MOD = 10
    private const val CHANGE_OFFSET = 9
    private const val CHANGE_RADIX = 19
    private const val SEQUENCE_LENGTH = 4
    private const val SEQUENCE_MODULUS = CHANGE_RADIX *
        CHANGE_RADIX *
        CHANGE_RADIX *
        CHANGE_RADIX
    /**
     * Parses the puzzle input into a [MonkeyMarket].
     *
     * @param input The puzzle input, one initial secret per non-blank line.
     * @return A [MonkeyMarket] initialized with the parsed secrets.
     */
    fun parse(input: List<String>): MonkeyMarket {
      return MonkeyMarket(
          input.asSequence()
              .map { it.trim() }
              .filter { it.isNotEmpty() }
              .map { it.toLong() }
              .toList()
      )
    }

    /**
     * Evolves a single secret once.
     *
     * @param secret The current secret.
     * @return The next secret.
     */
    fun nextSecret(secret: Long): Long {
      var current = prune(secret xor (secret shl 6))
      current = prune(current xor (current shr 5))
      current = prune(current xor (current shl 11))
      return current
    }

    private fun prune(secret: Long): Long = secret and PRUNE_MASK

    private fun price(secret: Long): Int = (secret % PRICE_MOD).toInt()

    private fun appendChange(sequence: Int, change: Int): Int {
      return ((sequence * CHANGE_RADIX) + change + CHANGE_OFFSET) % SEQUENCE_MODULUS
    }

    /**
     * Convenience method for Part 1.
     *
     * @param input The puzzle input.
     * @return The sum of each buyer's 2000th generated secret.
     */
    fun part1(input: List<String>): Long = parse(input).sumOfNthSecret(DEFAULT_GENERATIONS)

    /**
     * Convenience method for Part 2.
     *
     * @param input The puzzle input.
     * @return The most bananas obtainable with a single four-change sequence.
     */
    fun part2(input: List<String>): Long = parse(input).mostBananas(DEFAULT_GENERATIONS)
  }

  /**
   * Returns the sum of the nth generated secret for each buyer.
   *
   * The initial secret is generation 0, so `generations = 2000` returns the 2000th new secret.
   *
   * @param generations The number of times to evolve each initial secret.
   * @return The sum of the evolved secrets.
   */
  fun sumOfNthSecret(generations: Int = DEFAULT_GENERATIONS): Long {
    require(generations >= 0) { "generations must be greater than or equal to zero." }

    return initialSecrets.sumOf { initial ->
      generateSecret(initial, generations)
    }
  }

  /**
   * Returns the most bananas obtainable by choosing one sequence of four consecutive price changes.
   *
   * For each buyer, only the first occurrence of a sequence matters because the monkey sells
   * immediately when that sequence is first seen. This method therefore records each buyer's first
   * price for each sequence and adds it to a global total for that sequence.
   *
   * @param generations The number of new secret numbers to generate for each buyer.
   * @return The highest total bananas obtainable across all four-change sequences.
   */
  fun mostBananas(generations: Int = DEFAULT_GENERATIONS): Long {
    require(generations >= SEQUENCE_LENGTH) {
      "generations must be greater than or equal to $SEQUENCE_LENGTH."
    }

    val totals = mutableMapOf<Int, Long>()

    initialSecrets.forEach { initial ->
      val seen = mutableSetOf<Int>()
      var secret = initial
      var previousPrice = price(secret)
      var sequence = 0

      repeat(generations) { index ->
        secret = nextSecret(secret)

        val currentPrice = price(secret)
        val change = currentPrice - previousPrice
        sequence = appendChange(sequence, change)

        if (index >= SEQUENCE_LENGTH - 1 && seen.add(sequence)) {
          totals[sequence] = totals.getOrDefault(sequence, 0L) + currentPrice
        }

        previousPrice = currentPrice
      }
    }

    return totals.values.maxOrNull() ?: 0L
  }

  /**
   * Evolves [initial] the requested number of [generations].
   *
   * @param initial The initial secret.
   * @param generations The number of times to evolve the secret.
   * @return The evolved secret.
   */
  fun generateSecret(initial: Long, generations: Int = DEFAULT_GENERATIONS): Long {
    var secret = initial

    repeat(generations) {
      secret = nextSecret(secret)
    }

    return secret
  }
}
