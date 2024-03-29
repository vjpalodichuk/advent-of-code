package com.capital7software.aoc.lib.analysis

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * **--- Day 2: Inventory Management System ---**
 *
 * You stop falling through time, catch your breath, and check the screen on the device.
 * "Destination reached. Current Year: 1518. Current Location: North Pole Utility Closet
 * 83N10." You made it! Now, to find those anomalies.
 *
 * Outside the utility closet, you hear footsteps and a voice. "...I'm not sure either.
 * But now that so many people have chimneys, maybe he could sneak in that way?"
 * Another voice responds, "Actually, we've been working on a new kind of **suit** that
 * would let him fit through tight spaces like that. But, I heard that a few days ago,
 * they lost the prototype fabric, the design plans, everything! Nobody on the team
 * can even seem to remember important details of the project!"
 *
 * "Wouldn't they have had enough fabric to fill several boxes in the warehouse?
 * They'd be stored together, so the box IDs should be similar. Too bad it would
 * take forever to search the warehouse for **two similar box IDs**..." They walk too
 * far away to hear anymore.
 *
 * Late at night, you sneak to the warehouse - who knows what kinds of paradoxes you
 * could cause if you were discovered - and use your fancy wrist device to quickly
 * scan every box and produce a list of the likely candidates (your puzzle input).
 *
 * To make sure you didn't miss any, you scan the likely candidate boxes again,
 * counting the number that have an ID containing **exactly two of any letter** and
 * then separately counting those with **exactly three of any letter**. You can multiply
 * those two counts together to get a rudimentary checksum and compare it to what
 * your device predicts.
 *
 * Confident that your list of box IDs is complete, you're ready to find the boxes
 * full of prototype fabric.
 *
 * The boxes will have IDs which differ by exactly one character at the same position
 * in both strings.
 *
 * @param input The list of candidate box ids to analyze.
 */
class InventoryManagement @SuppressFBWarnings constructor(input: List<String>) {
  private val boxIds: List<String> = input.toList()
  private val counts: Map<String, Map<Char, Int>> by lazy {
    boxIds.associateWith { it.groupingBy { it }.eachCount() }
  }

  /**
   * The checksum of all the box IDs.
   *
   * For example, if you see the following box IDs:
   *
   * - abcdef contains no letters that appear exactly two or three times.
   * - bababc contains two a and three b, so it counts for both.
   * - abbcde contains two b, but no letter appears exactly three times.
   * - abcccd contains three c, but no letter appears exactly two times.
   * - aabcdd contains two a and two d, but it only counts once.
   * - abcdee contains two e.
   * - ababab contains three a and three b, but it only counts once.
   *
   * Of these box IDs, four of them contain a letter which appears exactly twice,
   * and three of them contain a letter which appears exactly three times.
   * Multiplying these together produces a checksum of 4 * 3 = 12.
   *
   */
  val checksum: Int by lazy { calculateChecksum() }

  /**
   * The two boxes that contain the prototype fabric. The first element is the first box ID, the
   * second element is the second box ID, and the third element are the common characters
   * between them.
   *
   * For example, given the following box IDs:
   *
   * ```
   * abcde
   * fghij
   * klmno
   * pqrst
   * fguij
   * axcye
   * wvxyz
   * ```
   *
   * The IDs abcde and axcye are close, but they differ by two characters (the second and fourth).
   * However, the IDs fghij and fguij differ by exactly one character, the third (h and u).
   * Those must be the correct boxes.
   *
   */
  val prototypes: Triple<String, String, String> by lazy { findPrototypes() }

  private fun calculateChecksum(): Int {
    val twos = counts.count { it.value.values.contains(2) }
    val threes = counts.count { it.value.values.contains(3) }

    return twos * threes
  }

  private fun findPrototypes(): Triple<String, String, String> {
    for (i in boxIds.indices) {
      for (j in i + 1 ..< boxIds.size) {
        val a = boxIds[i]
        val b = boxIds[j]
        val diff = countDifferences(a, b)
        if (diff.first == 1) {
          return Triple(a, b, diff.second)
        }
      }
    }
    return Triple("", "", "")
  }

  private fun countDifferences(a: String, b: String): Pair<Int, String> {
    val common = StringBuilder()
    var differences = 0

    if (a.length < b.length) {
      differences += (b.length - a.length)

      for (i in a.indices) {
        if (a[i] == b[i]) {
          common.append(a[i])
        } else {
          differences++
        }
      }
    } else if (b.length < a.length) {
      differences += (a.length - b.length)

      for (i in b.indices) {
        if (a[i] == b[i]) {
          common.append(a[i])
        } else {
          differences++
        }
      }
    } else {
      for (i in a.indices) {
        if (a[i] == b[i]) {
          common.append(a[i])
        } else {
          differences++
        }
      }
    }

    return Pair(differences, common.toString())
  }
}
