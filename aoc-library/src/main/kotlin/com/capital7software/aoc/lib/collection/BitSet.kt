package com.capital7software.aoc.lib.collection

/**
 * A Set of bits! Individual bits can be manipulated using [set] to set the specified bit
 * and [clear] to clear the specified bit. Bits are 0-based and range from 0 to requestedBits
 * minus 1.
 *
 * Adapted from java.util.PriorityQueue
 *
 * @param requestedBits The number of bits that are in this set.
 */
class BitSet(requestedBits: Int) {
  private val bits: LongArray = LongArray(((requestedBits - 1) ushr 6) + 1)

  /**
   * Set the specified bit to 1 if value is true; otherwise clears the specified bit.
   *
   * @param bit The bit to set to 1.
   * @param value If value is true the bit is set to 1; otherwise clears the specified bit.
   */
  operator fun set(bit: Int, value: Boolean) {
    if (value) {
      bits[bit ushr 6] = bits[bit ushr 6] or (1L shl bit)
    } else {
      clear(bit)
    }
  }

  /**
   * Get the specified bit. Returns true if the bit is set; otherwise false.
   *
   * @param bit The bit to set to 1.
   * @return True if the bit is set; otherwise false.
   */
  operator fun get(bit: Int): Boolean {
    return isSet(bit)
  }

  /**
   * Returns true if the specified bit has not been set to 1.
   *
   * @param bit The bit to check.
   * @return True if the specified bit has not been set to 1.
   */
  fun isClear(bit: Int): Boolean {
    return (bits[bit ushr 6] and (1L shl bit)) == 0L
  }

  /**
   * Returns true if the specified bit has been set to 1.
   *
   * @param bit The bit to check.
   * @return True if the specified bit has been set to 1.
   */
  fun isSet(bit: Int): Boolean {
    return !isClear(bit)
  }

  /**
   * Clears the specified bit by setting it to 0.
   *
   * @param bit The bit to clear.
   */
  fun clear(bit: Int) {
    bits[bit ushr 6] = bits[bit ushr 6] and (1L shl bit).inv()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is BitSet) return false

    if (!bits.contentEquals(other.bits)) return false

    return true
  }

  override fun hashCode(): Int {
    return bits.contentHashCode()
  }

  override fun toString(): String {
    return "BitSet(bits=${bits.contentToString()})"
  }
}
