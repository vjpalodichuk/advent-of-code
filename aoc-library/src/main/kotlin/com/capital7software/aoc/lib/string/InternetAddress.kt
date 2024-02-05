package com.capital7software.aoc.lib.string

import com.capital7software.aoc.lib.util.Pair
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

private const val ABBA_LENGTH = 4
private const val ABA_LENGTH = 3

/**
 * While snooping around the local network of EBHQ, you compile a list of IP addresses
 * (they're IPv7, of course; IPv6 is much too limited). You'd like to figure out which
 * IPs support TLS (transport-layer snooping).
 *
 * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or **ABBA**.
 * An ABBA is any four-character sequence which consists of a pair of two different
 * characters followed by the reverse of that pair, such as xyyx or abba. However,
 * the IP also must not have an ABBA within any hypernet sequences, which are
 * contained by **square brackets**.
 *
 * For example:
 *
 * - `abba[mnop]qrst` supports TLS (abba outside square brackets).
 * - `abcd[bddb]xyyx` does not support TLS (bddb is within square brackets,
 * even though xyyx is outside square brackets).
 * - `aaaa[qwer]tyui` does not support TLS (aaaa is invalid; the interior characters
 * must be different).
 * - `ioxxoj[asdfgh]zxcvbn` supports TLS (oxxo is outside square brackets, even though
 * it's within a larger string).
 *
 * **How many IPs** in your puzzle input support TLS?
 *
 * You would also like to know which IPs support **SSL** (super-secret listening).
 *
 * An IP supports SSL if it has an Area-Broadcast Accessor, or **ABA**, anywhere in
 * the supernet sequences (outside any square bracketed sections), and a corresponding
 * Byte Allocation Block, or **BAB**, anywhere in the hypernet sequences. An ABA is any
 * three-character sequence which consists of the same character twice with a different
 * character between them, such as xyx or aba. A corresponding BAB is the same characters
 * but in reversed positions: yxy and bab, respectively.
 *
 * For example:
 *
 * - `aba[bab]xyz` supports SSL (aba outside square brackets with corresponding bab within
 * square brackets).
 * - `xyx[xyx]xyx` does **not** support SSL (xyx, but no corresponding yxy).
 * - `aaa[kek]eke` supports SSL (eke in supernet with corresponding kek in hypernet;
 * the aaa sequence is not related, because the interior character must be different).
 * - `zazbz[bzb]cdb` supports SSL (zaz has no corresponding aza, but zbz has a
 * corresponding bzb, even though zaz and zbz overlap).
 *
 * How many IPs in your puzzle input support SSL?
 */
object InternetAddress {

  /**
   * Returns true if the specified four character [String] is in ABBA format.
   *
   * @param sub The four character [String] to check.
   * @return True if the specified four character [String] is in ABBA format.
   */
  private fun isAbba(sub: String): Boolean {
    if (sub.length != ABBA_LENGTH) {
      return false
    }

    return sub.first() == sub.last()
        && sub.first() != sub.get(1)
        && sub.get(1) == sub.get(2)
        && !sub.contains('[')
        && !sub.contains(']')
  }

  /**
   * Returns true if the specified three character [String] is in ABA format.
   *
   * @param sub The three character [String] to check.
   * @return True if the specified three character [String] is in ABA format.
   */
  private fun isAba(sub: String): Boolean {
    if (sub.length != ABA_LENGTH) {
      return false
    }

    return sub.first() == sub.last()
        && sub.first() != sub.get(1)
        && !sub.contains('[')
        && !sub.contains(']')
  }

  /**
   * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or **ABBA**.
   * An ABBA is any four-character sequence which consists of a pair of two different
   * characters followed by the reverse of that pair, such as xyyx or abba. However,
   * the IP also must not have an ABBA within any hypernet sequences, which are
   * contained by **square brackets**.
   *
   * For example:
   *
   * - `abba[mnop]qrst` supports TLS (abba outside square brackets).
   * - `abcd[bddb]xyyx` does not support TLS (bddb is within square brackets,
   * even though xyyx is outside square brackets).
   * - `aaaa[qwer]tyui` does not support TLS (aaaa is invalid; the interior characters
   * must be different).
   * - `ioxxoj[asdfgh]zxcvbn` supports TLS (oxxo is outside square brackets, even though
   * it's within a larger string).
   *
   * @param ipAddress The IPv7 [String] to check for TLS support.
   * @return True if the specified IPv7 [String] supports TLS.
   */
  fun supportsTls(ipAddress: String): Boolean {
    var inBrackets = false
    var abbaFound = false
    var abbaFoundInBrackets = false

    var i = 0

    while (!abbaFoundInBrackets && i <= ipAddress.length - ABBA_LENGTH) {
      if (inBrackets && ipAddress.get(i) == ']') {
        inBrackets = false
        i++
      } else if (inBrackets) {
        val abbaSub = ipAddress.substring(i, i + ABBA_LENGTH)
        val foundInBrackets: Boolean = isAbba(abbaSub)

        if (foundInBrackets) {
          abbaFoundInBrackets = true
        }
        i++
      } else if (ipAddress.get(i) == '[') {
        inBrackets = true
        i++
      } else if (!abbaFound) {
        val abbaSub = ipAddress.substring(i, i + ABBA_LENGTH)
        abbaFound = isAbba(abbaSub)
        if (abbaFound) {
          i += ABBA_LENGTH
        } else {
          i++
        }
      } else {
        i++
      }
    }
    return abbaFound && !abbaFoundInBrackets
  }

  /**
   * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or **ABBA**.
   * An ABBA is any four-character sequence which consists of a pair of two different
   * characters followed by the reverse of that pair, such as xyyx or abba. However,
   * the IP also must not have an ABBA within any hypernet sequences, which are
   * contained by **square brackets**.
   *
   * For example:
   *
   * - `abba[mnop]qrst` supports TLS (abba outside square brackets).
   * - `abcd[bddb]xyyx` does not support TLS (bddb is within square brackets,
   * even though xyyx is outside square brackets).
   * - `aaaa[qwer]tyui` does not support TLS (aaaa is invalid; the interior characters
   * must be different).
   * - `ioxxoj[asdfgh]zxcvbn` supports TLS (oxxo is outside square brackets, even though
   * it's within a larger string).
   *
   * @param ipAddress The [List] of IPv7 [String] addresses to check for TLS support.
   * @return The [List] of IPv7 [String] addresses that support TLS.
   */
  @SuppressFBWarnings
  fun supportsTls(ipAddresses: List<String>): List<String> {
    return ipAddresses.filter { supportsTls(it) }
  }

  private fun toBab(aba: String): String {
    val builder = StringBuilder()

    builder.append(aba[1])
    builder.append(aba.first())
    builder.append(aba[1])

    return builder.toString()
  }

  private fun getAbasAndBabs(ipAddress: String) : Pair<Set<String>, Set<String>> {
    val babs = mutableSetOf<String>()
    val abas = mutableSetOf<String>()

    var inBrackets = false

    var i = 0

    while (i <= ipAddress.length - ABA_LENGTH) {
      if (inBrackets && ipAddress.get(i) == ']') {
        inBrackets = false
        i++
      } else if (inBrackets) {
        val babSub = ipAddress.substring(i, i + ABA_LENGTH)
        if (isAba(babSub)) {
          babs.add(babSub)
        }
        i++
      } else if (ipAddress.get(i) == '[') {
        inBrackets = true
        i++
      } else {
        val abaSub = ipAddress.substring(i, i + ABA_LENGTH)
        if (isAba(abaSub)) {
          abas.add(abaSub)
        }
        i++
      }
    }

    return Pair(abas, babs)
  }

  /**
   * An IP supports SSL if it has an Area-Broadcast Accessor, or **ABA**, anywhere in
   * the supernet sequences (outside any square bracketed sections), and a corresponding
   * Byte Allocation Block, or **BAB**, anywhere in the hypernet sequences. An ABA is any
   * three-character sequence which consists of the same character twice with a different
   * character between them, such as xyx or aba. A corresponding BAB is the same characters
   * but in reversed positions: yxy and bab, respectively.
   *
   * For example:
   *
   * - `aba[bab]xyz` supports SSL (aba outside square brackets with corresponding bab within
   * square brackets).
   * - `xyx[xyx]xyx` does **not** support SSL (xyx, but no corresponding yxy).
   * - `aaa[kek]eke` supports SSL (eke in supernet with corresponding kek in hypernet;
   * the aaa sequence is not related, because the interior character must be different).
   * - `zazbz[bzb]cdb` supports SSL (zaz has no corresponding aza, but zbz has a
   * corresponding bzb, even though zaz and zbz overlap).
   *
   * @param ipAddress The IPv7 [String] to check for SSL support.
   * @return True if the specified IPv7 [String] supports SSL.
   */
  @SuppressFBWarnings
  fun supportsSsl(ipAddress: String): Boolean {
    val pair = getAbasAndBabs(ipAddress)
    val abas = pair.first()
    val babs = pair.second()

    return abas.filter { babs.contains(toBab(it)) }.isNotEmpty()
  }

  /**
   * An IP supports SSL if it has an Area-Broadcast Accessor, or **ABA**, anywhere in
   * the supernet sequences (outside any square bracketed sections), and a corresponding
   * Byte Allocation Block, or **BAB**, anywhere in the hypernet sequences. An ABA is any
   * three-character sequence which consists of the same character twice with a different
   * character between them, such as xyx or aba. A corresponding BAB is the same characters
   * but in reversed positions: yxy and bab, respectively.
   *
   * For example:
   *
   * - `aba[bab]xyz` supports SSL (aba outside square brackets with corresponding bab within
   * square brackets).
   * - `xyx[xyx]xyx` does **not** support SSL (xyx, but no corresponding yxy).
   * - `aaa[kek]eke` supports SSL (eke in supernet with corresponding kek in hypernet;
   * the aaa sequence is not related, because the interior character must be different).
   * - `zazbz[bzb]cdb` supports SSL (zaz has no corresponding aza, but zbz has a
   * corresponding bzb, even though zaz and zbz overlap).
   *
   * @param ipAddress The [List] of IPv7 [String] addresses to check for SSL support.
   * @return The [List] of IPv7 [String] addresses that support SSL.
   */
  @SuppressFBWarnings
  fun supportsSsl(ipAddresses: List<String>): List<String> {
    return ipAddresses.filter { supportsSsl(it) }
  }
}
