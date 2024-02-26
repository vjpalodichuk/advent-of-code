package com.capital7software.aoc.lib.analysis

import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.log2
import kotlin.math.pow

/**
 * The Elves contact you over a highly secure emergency channel. Back at the North Pole,
 * the Elves are busy misunderstanding White Elephant parties.
 */
object ElfParty {
  /**
   * An Elf at the [ElfParty].
   *
   * @param id The ID of the Elf at the party when first sitting down at the table.
   * @param presents The number of presents this Elf currently is holding.
   */
  data class Elf(val id: Int, var presents: Int) {
    /**
     * Takes all presents from the specified [Elf] and gives them to this [Elf].
     */
    fun take(elf: Elf) {
      presents += elf.presents
      elf.presents = 0
    }

    override fun toString(): String {
      return "$id-$presents"
    }
  }

  /**
   * Calculates and returns the [Elf] that is left with all the presents.
   *
   * Each Elf brings a present. They all sit in a circle, numbered starting with position 1.
   * Then, starting with the first Elf, they take turns stealing all the presents from the
   * Elf to their left. An Elf with no presents is removed from the circle and does not take turns.
   *
   * For example, with five Elves (numbered 1 to 5):
   * ```
   *   1
   * 5   2
   *  4 3
   * ```
   *
   * - Elf 1 takes Elf 2's present.
   * - Elf 2 has no presents and is skipped.
   * - Elf 3 takes Elf 4's present.
   * - Elf 4 has no presents and is also skipped.
   * - Elf 5 takes Elf 1's two presents.
   * - Neither Elf 1 nor Elf 2 have any presents, so both are skipped.
   * - Elf 3 takes Elf 5's three presents.
   *
   * So, with **five** Elves, the Elf that sits starting in position 3 gets all the presents.
   *
   * @param numElves The number of elves exchanging gifts.
   */
  fun leftExchange(numElves: Int): Elf {
    var list = ArrayList<Elf>()

    for (i in 1..numElves) {
      val elf = Elf(i, 1)
      list.add(elf)
    }

    while (list.size > 1) {
      val temp = ArrayList<Elf>()

      for (i in 0 until list.size step 2) {
        val elf = list.get(i)
        if (elf.presents == 0) {
          continue
        }
        val elf2 = list[(i + 1) % list.size]
        elf.take(elf2)

        temp.add(elf)
      }
      if (temp[0].presents == 0) {
        temp.removeAt(0)
      }
      list = temp
    }
    return list.first()
  }

  /**
   * Calculates and returns the [Elf] that ends up with all the presents.
   *
   * Uses the formula:
   *
   * numElves = 2^a + l
   * Where 2^a is the largest power of 2 in numElves and l is what is left over.
   *
   * winner = 2l + 1
   *
   * @param numElves The number of elves exchanging gifts.
   */
  fun leftExchangeFast(numElves: Int): Elf {
    val base = 2.0.pow(log2(numElves.toDouble()).toInt().toDouble()).toInt()
    val rest = numElves - base
    val answer = 2 * rest + 1

    return Elf(answer, numElves)
  }

  /**
   * Realizing the folly of their present-exchange rules, the Elves agree to instead steal
   * presents from the Elf **directly across the circle**. If two Elves are across the circle,
   * the one on the left (from the perspective of the stealer) is stolen from. The other rules
   * remain unchanged: Elves with no presents are removed from the circle entirely, and the
   * other elves move in slightly to keep the circle evenly spaced.
   *
   * For example, with five Elves (again numbered 1 to 5):
   *
   * - The Elves sit in a circle; Elf 1 goes first:
   * ```
   *   1
   * 5   2
   *  4 3
   * ```
   * - Elves 3 and 4 are across the circle; Elf 3's present is stolen, being the one to the left.
   * Elf 3 leaves the circle, and the rest of the Elves move in:
   * ```
   *   1           1
   * 5   2  -->  5   2
   *  4 -          4
   * ```
   * - Elf 2 steals from the Elf directly across the circle, Elf 5:
   * ```
   *   1         1
   * -   2  -->     2
   *   4         4
   * ```
   * - Next is Elf 4 who, choosing between Elves 1 and 2, steals from Elf 1:
   * ```
   *  -          2
   *     2  -->
   *  4          4
   * ```
   * - Finally, Elf 2 steals from Elf 4:
   * ```
   *  2
   *     -->  2
   *  -
   * ```
   * So, with **five** Elves, the Elf that sits starting in position 2 gets all the presents.
   *
   * @param numElves The number of elves exchanging gifts.
   */
  fun acrossExchange(numElves: Int): Elf {
    val list = ArrayList<Elf>()

    for (i in 1..numElves) {
      val elf = Elf(i, 1)
      list.add(elf)
    }

    var index = 0
    while (list.size > 1) {
      val elf = list[index]
      val t = ((list.size / 2) + index) % list.size
      val elf2 = list.removeAt(t)
      elf.take(elf2)

      if (t > index) {
        index++
      }
      if (index >= list.size) {
        index = 0
      }
    }

    return list.first()
  }

  /**
   * Calculates and returns the [Elf] that ends up with all the presents.
   *
   * Uses powers of 3 to quickly calculate the Elf that gets all the presents.
   *
   * - First calculates the power of 3 of numElves.
   * - Then raises that power to the power of 3 and assigns that value to b.
   * - Then
   *    - Returns numElves if it is equal to b.
   *    - Else returns numElves - b if that value is less than or equal to b.
   *    - Else returns 2 * numElves - 3 * b.
   *
   * @param numElves The number of elves exchanging gifts.
   */
  fun acrossExchangeFast(numElves: Int): Elf {
    val power = floor(ln(numElves.toDouble()) / ln(3.0))
    val b = 3.0.pow(power).toInt()
    return if (numElves == b) {
      Elf(numElves, numElves)
    } else if (numElves - b <= b) {
      Elf(numElves - b, numElves)
    } else {
      Elf(2 * numElves - 3 * b, numElves)
    }
  }
}
