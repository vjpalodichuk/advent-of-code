package com.capital7software.aoc.lib.crypt

import com.capital7software.aoc.lib.util.Pair
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

private const val HEX_RADIX = 16

private const val BYTE_MASK = 0xff

private const val HEX_OFFSET = 0x100

/**
 * Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for
 * all the economically forward-thinking little girls and boys.
 */
object Md5Fun {
  /**
   * For example:
   *
   * If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043
   * starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
   * <br></br><br></br>
   * If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash
   * starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like
   * 000006136ef....
   *
   * @param secret       The secret text to find a number to join with to produce a hash with the
   * required number of leading zeros.
   * @param leadingZeros The number of leading zeros the MD5 hash must have.
   * @param startingIndex The index to start from; defaults to 0.
   * @return The lowest positive number from startingIndex to join with the secret to
   * produce a MD5 hash with the required number of leading zeros.
   */
  @JvmStatic
  fun lowestPositiveHashWithLeadingZeros(
      secret: String,
      leadingZeros: Int,
      startingIndex: Long = 0,
  ): Pair<String, Long> {
    val startsWith = "0".repeat(leadingZeros)
    val answer: Pair<String, Long> = Pair()

    var count: Long = startingIndex
    try {
      val md = MessageDigest.getInstance("MD5")
      var done = false
      while (!done) {
        val target = secret + count
        md.update(target.toByteArray(StandardCharsets.US_ASCII))

        val hash = hashToString(md)

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
   * @param startingIndex The index to start generating a password from.
   * @param passwordLength THe length of the password which can be up to 16 characters in length.
   * @return The new password.
   */
  @JvmStatic
  fun generatePassword(
      secret: String,
      leadingZeros: Int,
      startingIndex: Long = 0,
      passwordLength: Int = 8,
  ): String {
    val builder = StringBuilder()

    var currentIndex = startingIndex

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
   * @param secret THe secret used to generate the hashes.
   * @param leadingZeros THe number of leading zeroes in the hash for it to be used.
   * @param startingIndex The index to start generating a password from.
   * @param passwordLength THe length of the password which can be up to 16 characters in length.
   * @return The new password.
   */
  @JvmStatic
  fun generateAdvancedPassword(
      secret: String,
      leadingZeros: Int,
      startingIndex: Long = 0,
      passwordLength: Int = 8
  ): String {
    val answer = CharArray(passwordLength)
    var currentIndex = startingIndex
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
}
