package com.capital7software.aoc.lib.crypt

import com.capital7software.aoc.lib.util.Pair
import com.capital7software.aoc.lib.util.Triple
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for
 * all the economically forward-thinking little girls and boys.
 */
@SuppressFBWarnings
object Md5Fun {
  private const val HEX_RADIX = 16

  private const val BYTE_MASK = 0xff

  private const val HEX_OFFSET = 0x100

  private const val REQUIRED_MATCH_GROUPS: Int = 1
  private val TRIPLE_REGEX: Regex = """(?<repeating>.)\1{2,}""".toRegex()
  private val QUINT_REGEX: Regex = """(?<repeating>.)\1{4,}""".toRegex()
  private const val KEY_REGEX_GROUP: String = "repeating"
  private const val MAX_DISTANCE: Int = 1_000
  private const val STRETCHED_HASHINGS: Int = 2_016

  /**
   * For example:
   *
   * If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043
   * starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
   *
   * If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash
   * starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like
   * 000006136ef....
   *
   * @param secret       The secret text to find a number to join with to produce a hash with the
   * required number of leading zeros.
   * @param leadingZeros The number of leading zeros the MD5 hash must have.
   * @param startIndex The index to start from; defaults to 0.
   * @return The lowest positive number from startingIndex to join with the secret to
   * produce a MD5 hash with the required number of leading zeros. The first element in the
   * returned [Pair] is the generated hash and the second element is the lowest positive number.
   */
  @JvmStatic
  @JvmOverloads
  fun lowestPositiveHashWithLeadingZeros(
      secret: String,
      leadingZeros: Int,
      startIndex: Long = 0,
  ): Pair<String, Long> {
    val startsWith = "0".repeat(leadingZeros)
    val answer: Pair<String, Long> = Pair()

    var count: Long = startIndex
    try {
      val md = MessageDigest.getInstance("MD5")
      var done = false
      while (!done) {
        val hash = generateHash(md, secret + count)

        if (hash.startsWith(startsWith)) {
          answer.first(hash)
          done = true
        } else {
          count++
        }
      }
    } catch (e: NoSuchAlgorithmException) {
      throw IllegalStateException(e)
    }

    answer.second(count)

    return answer
  }

  /**
   * The eight-character password for the door is generated one character at a time by finding
   * the MD5 hash of some Door ID (your puzzle input) and an increasing integer
   * index (starting with 0).
   *
   * A hash indicates the next character in the password if its hexadecimal representation
   * starts with five zeroes. If it does, the sixth character in the hash is the next
   * character of the password.
   *
   * For example, if the Door ID is abc:
   *
   * The first index which produces a hash that starts with five zeroes is 3231929,
   * which we find by hashing abc3231929; the sixth character of the hash, and thus the
   * first character of the password, is 1.
   *
   * 5017308 produces the next interesting hash, which starts with 000008f82..., so the
   * second character of the password is 8.
   *
   * The third time a hash starts with five zeroes is for abc5278568, discovering the
   * character f. In this example, after continuing this search a total of eight
   * times, the password is 18f47a30.
   *
   * Given the actual Door ID, what is the password?
   *
   * @param secret THe secret used to generate the hashes.
   * @param leadingZeros THe number of leading zeroes in the hash for it to be used.
   * @param startIndex The index to start generating a password from.
   * @param passwordLength THe length of the password which can be up to 16 characters in length.
   * @return The new password.
   */
  @JvmStatic
  @JvmOverloads
  fun generatePassword(
      secret: String,
      leadingZeros: Int,
      startIndex: Long = 0,
      passwordLength: Int = 8,
  ): String {
    val builder = StringBuilder()

    var currentIndex = startIndex

    IntRange(1, passwordLength).forEach { _ ->
      val nextChar = lowestPositiveHashWithLeadingZeros(secret, leadingZeros, currentIndex)
      builder.append(nextChar.first()[leadingZeros])
      currentIndex = nextChar.second() + 1
    }

    return builder.toString()
  }

  /**
   * As the door slides open, you are presented with a second door that uses a slightly more
   * inspired security mechanism. Clearly unimpressed by the last version (in what movie is
   * the password decrypted **in order**?!), the Easter Bunny engineers have worked out
   * a better solution.
   *
   * Instead of simply filling in the password from left to right, the hash now also
   * indicates the **position** within the password to fill. You still look for hashes that
   * begin with five zeroes; however, now, the **sixth** character represents
   * the **position** (0-7), and the **seventh** character is the character to put in that position.
   *
   * A hash result of 000001f means that f is the **second** character in the password.
   * Use only the **first result** for each position, and ignore invalid positions.
   *
   * For example, if the Door ID is `abc`:
   *
   * - The first interesting hash is from abc3231929, which produces 0000015...; so,
   * 5 goes in position 1: _5______.
   *
   * - In the previous method, 5017308 produced an interesting hash; however, it is ignored,
   * because it specifies an invalid position (8).
   *
   * - The second interesting hash is at index 5357525, which produces 000004e...; so, e
   * goes in position 4: _5__e___.
   *
   * You almost choke on your popcorn as the final character falls into place, producing
   * the password `05ace8e3`.
   *
   * Given the actual Door ID and this new method, what is the password? Be extra proud
   * of your solution if it uses a cinematic "decrypting" animation.
   *
   * @param secret The secret used to generate the hashes.
   * @param leadingZeros The number of leading zeroes in the hash for it to be used.
   * @param startIndex The index to start generating a password from.
   * @param passwordLength The length of the password which can be up to 16 characters in length.
   * @return The new password.
   */
  @JvmStatic
  @JvmOverloads
  fun generateAdvancedPassword(
      secret: String,
      leadingZeros: Int,
      startIndex: Long = 0,
      passwordLength: Int = 8
  ): String {
    val answer = CharArray(passwordLength)
    var currentIndex = startIndex
    val filledPositions = mutableSetOf<Int>()
    while (filledPositions.size < passwordLength) {
      val nextChar = lowestPositiveHashWithLeadingZeros(secret, leadingZeros, currentIndex)
      val pos = nextChar.first()[leadingZeros].toString().toInt(radix = HEX_RADIX)
      val c = nextChar.first()[leadingZeros + 1]

      if (pos in 0..<passwordLength && !filledPositions.contains(pos)) {
        answer[pos] = c
        filledPositions.add(pos)
      }
      currentIndex = nextChar.second() + 1
    }

    return String(answer)
  }

  /**
   * Digests the specified message and produces a hexadecimal string of the resulting hash.
   *
   * @param md THe MessageDigest to digest and convert to a hexadecimal string.
   * @return The MD5 hash as a hexadecimal string.
   */
  @JvmStatic
  fun hashToString(md: MessageDigest): String {
    val buffer = md.digest()
    val sb = StringBuilder()

    for (b in buffer) {
      sb.append(((b.toInt() and BYTE_MASK) + HEX_OFFSET).toString(HEX_RADIX).substring(1))
    }

    return sb.toString()
  }

  /**
   * To generate keys, you first get a stream of random data by taking the
   * MD5 of a pre-arranged salt (your puzzle input) and an increasing integer
   * index (starting with 0, and represented in decimal); the resulting MD5 hash
   * should be represented as a string of **lowercase** hexadecimal digits.
   *
   * However, not all of these MD5 hashes are **keys**, and you need 64 new keys
   * for your one-time pad. A hash is a key **only if:**
   *
   * - It contains **three** of the same character in a row, like 777. Only consider
   * the first such triplet in a hash.
   * - One of the next 1000 hashes in the stream contains that same character **five**
   * times in a row, like 77777.
   *
   * Considering future hashes for five-of-a-kind sequences does not cause those hashes
   * to be skipped; instead, regardless of whether the current hash is a key, always
   * resume testing for keys starting with the very next hash.
   *
   * For example, if the pre-arranged salt is abc:
   *
   * - The first index which produces a triple is 18, because the MD5 hash of abc18
   * contains ...cc38887a5.... However, index 18 does not count as a key for your
   * one-time pad, because none of the next thousand hashes (index 19 through index 1018)
   * contain 88888.
   * - The next index which produces a triple is 39; the hash of abc39 contains eee.
   * It is also the first key: one of the next thousand hashes (the one at index 816)
   * contains eeeee.
   * - None of the next six triples are keys, but the one after that, at index 92,
   * is: it contains 999 and index 200 contains 99999.
   * - Eventually, index 22728 meets all the criteria to generate the 64th key.
   *
   * So, using our example salt of abc, index 22728 produces the 64th key.
   *
   * Of course, in order to make this process even more secure, you've also implemented
   * key stretching.
   *
   * Key stretching forces attackers to spend more time generating hashes. Unfortunately,
   * it forces everyone else to spend more time, too.
   *
   * To implement key stretching, whenever you generate a hash, before you use it, you first
   * find the MD5 hash of that hash, then the MD5 hash of **that** hash, and so on, a total of
   * **2016 additional hashings**. Always use lowercase hexadecimal representations of hashes.
   *
   * For example, to find the stretched hash for index 0 and salt abc:
   *
   * - Find the MD5 hash of abc0: 577571be4de9dcce85a041ba0410f29f.
   * - Then, find the MD5 hash of that hash: eec80a0c92dc8a0777c619d9bb51e910.
   * - Then, find the MD5 hash of that hash: 16062ce768787384c81fe17a7a60c7e3.
   * - ...repeat many times...
   * - Then, find the MD5 hash of that hash: a107ff634856bb300138cac6568c0f24.
   *
   * So, the stretched hash for index 0 in this situation is a107ff.... In the end, you
   * find the original hash (one use of MD5), then find the hash-of-the-previous-hash 2016
   * times, for a total of 2017 uses of MD5.
   *
   * The rest of the process remains the same, but now the keys are entirely different.
   * Again for salt abc:
   *
   * - The first triple (222, at index 5) has no matching 22222 in the next thousand hashes.
   * - The second triple (eee, at index 10) hash a matching eeeee at index 89, and so
   * it is the first key.
   * - Eventually, index 22551 produces the 64th key (triple fff with matching fffff at
   * index 22859.
   *
   * @param salt The salt to use when generating a hash.
   * @param limit The maximum number of keys to generate.
   * @param startIndex The index to start generating keys from.
   * @param stretched If true, the hashes are stretched before being used.
   * @return A [List] of [Pair] where the first element is the key and the second is the index.
   */
  @JvmStatic
  @JvmOverloads
  fun generateOneTimePadKeys(
      salt: String,
      limit: Int = 1,
      startIndex: Int = 0,
      stretched: Boolean = false,
  ): List<Pair<String, Int>> {
    require(salt.isNotBlank()) { "salt cannot be blank: $salt" }
    require(limit >= 1) { "limit must be a positive number: $limit" }
    require(startIndex >= 0) { "startingIndex must be greater than or equal to zero: $startIndex" }

    val answer = mutableListOf<Pair<String, Int>>()
    val triples = mutableListOf<Triple<MatchGroup, Int, String>>()
    val quints = mutableMapOf<String, MutableList<Triple<MatchGroup, Int, String>>>()
    var currentIndex = startIndex
    val md = MessageDigest.getInstance("MD5")

    generatePadHashes(md, salt, currentIndex, currentIndex + MAX_DISTANCE, stretched, triples, quints)
    currentIndex += (MAX_DISTANCE + 1)

    while (answer.size < limit && triples.isNotEmpty()) {
      val triple = triples.removeFirst()
      val diff = currentIndex - triple.second()
      if (diff < MAX_DISTANCE) {
        val end = currentIndex + (MAX_DISTANCE - diff)
        generatePadHashes(md, salt, currentIndex, end, stretched, triples, quints)
        currentIndex = end + 1
      }

      if (quints.containsKey(triple.first().value)) {
        val list = quints[triple.first().value]?.filter {
          triple.second() + 1 <= it.second() && it.second() <= triple.second() + MAX_DISTANCE
        }

        if (!list.isNullOrEmpty()) {
          answer.add(Pair(triple.third(), triple.second()))
        }
      }
    }
    return answer
  }

  /**
   * Generates and returns a hash of the specified input using the specified [MessageDigest].
   *
   * @param md The [MessageDigest] to use to generate the hash.
   * @param input The [String] to hash.
   * @return A hash [String] of the input [String].
   */
  fun generateHash(md: MessageDigest, input: String) : String {
    try {
      md.update(input.toByteArray(StandardCharsets.UTF_8))

      return hashToString(md)
    } catch (e: NoSuchAlgorithmException) {
      throw IllegalStateException(e)
    }
  }

  private fun generatePadHashes(
      md: MessageDigest,
      salt: String,
      startIndex: Int,
      endIndex: Int,
      stretched: Boolean,
      triples: MutableList<Triple<MatchGroup, Int, String>>,
      quints: MutableMap<String, MutableList<Triple<MatchGroup, Int, String>>>,
  ) {
    for (i in startIndex .. endIndex) {
      var hash = generateHash(md, salt + i)

      if (stretched) {
        hash = stretchHash(md, hash)
      }
      val triple = getFirstTriple(hash)

      if (triple != null) {
        triples.add(Triple(triple, i, hash))

        getQuints(hash).forEach {
          quints.computeIfAbsent(it.value) { mutableListOf() }.add(Triple(it, i, hash))
        }
      }
    }
  }

  private fun stretchHash(md: MessageDigest, hash: String): String {
    var temp = hash

    @Suppress("unused_parameter")
    for (i in 0 until STRETCHED_HASHINGS) {
      temp = generateHash(md, temp)
    }

    return temp
  }

  /**
   * Tests the specified string for a character that repeats at least three times in a row. If a
   * repeating character is encountered it is returned; otherwise null is returned.
   *
   * @param input The string to check for repeating characters.
   * @return The first character to repeat at least three times in a row; otherwise null.
   */
  @JvmStatic
  fun getFirstTriple(input: String): MatchGroup? {
    val match = TRIPLE_REGEX.find(input)

    if (match != null && match.groups.size >= REQUIRED_MATCH_GROUPS) {
      return match.groups[KEY_REGEX_GROUP]
    }

    return null
  }

  /**
   * Tests the specified string for a character that repeats at least five times in a row. If a
   * repeating character is encountered it is included in the returned [List].
   *
   * @param input The string to check for repeating characters.
   * @return A [List] of characters that repeat at least five times in a row.
   */
  @JvmStatic
  fun getQuints(input: String): List<MatchGroup> {
    var match = QUINT_REGEX.find(input)
    val answer = mutableListOf<MatchGroup>()

    while (match != null) {
      if (match.groups.size >= REQUIRED_MATCH_GROUPS) {
        val group = match.groups[KEY_REGEX_GROUP]

        if (group != null) {
          answer.add(group)
        }

        match = match.next()
      }
    }

    return answer
  }
}

/**
 * Returns the length of this [IntRange].
 *
 * @return The length of this [IntRange].
 */
fun IntRange.length(): Int {
  return endInclusive - start + 1
}
