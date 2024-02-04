package com.capital7software.aoc.lib.crypt

import com.capital7software.aoc.lib.util.Pair
import com.capital7software.aoc.lib.util.Triple
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

private const val TOP_FIVE = 5

private const val LETTERS_IN_ALPHABET = 26

/**
 * Finally, you come across an information kiosk with a list of rooms. Of course,
 * the list is encrypted and full of decoy data, but the instructions to decode the
 * list are barely hidden nearby. Better remove the decoy data first.
 *
 * Each room consists of an encrypted name (lowercase letters separated by dashes)
 * followed by a dash, a sector ID, and a checksum in square brackets.
 *
 * A room is real (not a decoy) if the checksum is the five most common letters in
 * the encrypted name, in order, with ties broken by alphabetization. For example:
 *
 * - `aaaaa-bbb-z-y-x-123[abxyz]` is a real room because the most common letters are a
 * (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
 * - `a-b-c-d-e-f-g-h-987[abcde]` is a real room because although the letters are all
 * tied (1 of each), the first five are listed alphabetically.
 * - `not-a-real-room-404[oarel]` is a real room.
 * - `totally-real-room-200[decoy]` is not.
 *
 * Of the real rooms from the list above, the sum of their sector IDs is 1514.
 *
 * What is the **sum of the sector IDs of the real rooms**?
 *
 * With all the decoy data out of the way, it's time to decrypt this list and get moving.
 *
 * The room names are encrypted by a state-of-the-art shift cipher, which is nearly
 * unbreakable without the right software. However, the information kiosk designers
 * at Easter Bunny HQ were not expecting to deal with a master cryptographer like yourself.
 *
 * To decrypt a room name, rotate each letter forward through the alphabet a number of
 * times equal to the room's sector ID. `A` becomes `B`, `B` becomes `C`, `Z` becomes `A`,
 * and so on. Dashes become spaces.
 *
 * For example, the real name for `qzmt-zixmtkozy-ivhz-343` is `very encrypted name`.
 *
 * **What is the sector ID** of the room where North Pole objects are stored?
 *
 */
@SuppressFBWarnings
class SecureRooms(private val rooms: List<Triple<String, Long, String>>) {
  companion object {
    /**
     * Builds and returns a new [SecureRooms] instance loaded with the rooms parsed from
     * the specified [List] of [String].
     *
     * @param input The [List] of [String] to parse in to encrypted rooms.
     * @return A new [SecureRooms] loaded with the encrypted rooms parsed from the input.
     */
    fun buildSecureRooms(input: List<String>): SecureRooms {
      val rooms = mutableListOf<Triple<String, Long, String>>()

      input.forEach { line ->
        val lastDashIndex = line.lastIndexOf('-')

        if (lastDashIndex >= 0) {
          val encryptedName = line.substring(0, lastDashIndex)
          val bracketIndex = line.indexOf('[', lastDashIndex)
          val sectorId = line.substring(lastDashIndex + 1, bracketIndex).toLong()
          val checksum = line.substring(bracketIndex + 1, line.length - 1)

          rooms.add(Triple(encryptedName, sectorId, checksum))
        }
      }

      return SecureRooms(rooms)
    }
  }

  private fun isRealRoom(encryptedName: String, checksum: String): Boolean {
    val topFive = getTopFive(encryptedName)
    var valid = true

    checksum.toCharArray().withIndex().forEach {
      if (it.value != topFive[it.index].first()) {
        valid = false
      }
    }

    return valid
  }

  private fun getTopFive(encryptedName: String): List<Pair<Char, Long>> {
    return encryptedName
        .filter { it != '-' }
        .groupingBy { it }
        .eachCount()
        .mapNotNull { Pair(it.key, it.value.toLong()) }
        .sortedWith { a, b ->
          if (a.second() > b.second()) {
            -1
          } else if (a.second() < b.second()) {
            1
          } else {
            a.first().compareTo(b.first())
          }
        }
        .take(TOP_FIVE)
  }

  private fun decryptRealRoom(room: Triple<String, Long, String>): Pair<String, Long> {
    // There are only 26 letters in the alphabet, so we can cut the rotations to make
    // down to just the remainder.
    val rotateCount = room.second() % LETTERS_IN_ALPHABET

    val decryptedName = CharArray(room.first().length) { '0' }

    room.first().toCharArray().withIndex().forEach {
      if (it.value == '-') { // dashes become spaces
        decryptedName[it.index] = ' '
      } else if (('z' - it.value) >= rotateCount) { // simple add
        decryptedName[it.index] = (it.value + rotateCount.toInt())
      } else { // wraps around
        val distanceToZ = 'z' - it.value
        val distanceAfterA = 'a' + (rotateCount - distanceToZ).toInt() - 1
        decryptedName[it.index] = distanceAfterA
      }
    }

    return Pair(String(decryptedName), room.second())
  }

  /**
   * Calculates and returns the sum of Sector IDs of the rooms that are real.
   *
   * @return The sum of Sector IDs of the rooms that are real.
   */
  fun sunOfRealRoomSectorIds(): Long {
    return rooms
        .filter { isRealRoom(it.first(), it.third()) }
        .mapNotNull { it.second() }
        .sum()
  }

  /**
   * Decrypts the real rooms and returns them. The first property of the pair is the decrypted
   * room name and the second property is the Sector ID.
   *
   * @return A [List] of [Pair] where the first property is the decrypted room name
   * and the second property is the Sector ID.
   */
  fun decryptRealRooms(): List<Pair<String, Long>> {
    return rooms
        .filter { isRealRoom(it.first(), it.third()) }
        .map { decryptRealRoom(it) }
  }
}
