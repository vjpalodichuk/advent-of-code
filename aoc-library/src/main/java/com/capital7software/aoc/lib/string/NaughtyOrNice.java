package com.capital7software.aoc.lib.string;

/**
 * A nice string is one with all the following properties:
 * <ul>
 *     <li>
 *         It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
 *     </li>
 *     <li>
 *         It contains at least one letter that appears twice in a row, like xx, abcdde (dd),
 *         or aabbccdd (aa, bb, cc, or dd).
 *     </li>
 *     <li>
 *         It does not contain the strings ab, cd, pq, or xy, even if they are part of one of
 *         the other requirements.
 *     </li>
 * </ul>
 * For example:
 * <ul>
 *     <li>
 *         ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double
 *         letter (...dd...), and none of the disallowed substrings.
 *     </li>
 *     <li>
 *         aaa is nice because it has at least three vowels and a double letter, even though the
 *         letters used by different rules overlap.
 *     </li>
 *     <li>
 *         jchzalrnumimnmhp is naughty because it has no double letter.
 *     </li>
 *     <li>
 *         haegwjzuvuyypxyu is naughty because it contains the string xy.
 *     </li>
 *     <li>
 *         dvszwmarrgswjxmb is naughty because it contains only one vowel.
 *     </li>
 * </ul>
 * How many strings are nice?
 *
 * <p><br>
 * Realizing the error of his ways, Santa has switched to a better model of determining whether
 * a string is naughty or nice. None of the old rules apply, as they are all clearly ridiculous.
 *
 * <p><br>
 * Now, a nice string is one with all the following properties:
 * <ul>
 *     <li>
 *         It contains a pair of any two letters that appears at least twice in the string without
 *         overlapping, like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
 *     </li>
 *     <li>
 *         It contains at least one letter which repeats with exactly one letter between them,
 *         like xyx, abcdefeghi (efe), or even aaa.
 *     </li>
 * </ul>
 * For example:
 * <ul>
 *     <li>
 *         qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter
 *         that repeats with exactly one letter between them (zxz).
 *     </li>
 *     <li>
 *         xxyxx is nice because it has a pair that appears twice and a letter that repeats with
 *         one between, even though the letters used by each rule overlap.
 *     </li>
 *     <li>
 *         uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single
 *         letter between them.
 *     </li>
 *     <li>
 *         ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo),
 *         but no pair that appears twice.
 *     </li>
 * </ul>
 * How many strings are nice under these new rules?
 */
public record NaughtyOrNice() {
  /**
   * A nice string is one with all the following properties.
   * <ul>
   *     <li>
   *         It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
   *     </li>
   *     <li>
   *         It contains at least one letter that appears twice in a row, like xx, abcdde (dd),
   *         or aabbccdd (aa, bb, cc, or dd).
   *     </li>
   *     <li>
   *         It does not contain the strings ab, cd, pq, or xy, even if they are part of one of
   *         the other requirements.
   *     </li>
   * </ul>
   * For example:
   * <ul>
   *     <li>
   *         ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double
   *         letter (...dd...), and none of the disallowed substrings.
   *     </li>
   *     <li>
   *         aaa is nice because it has at least three vowels and a double letter, even though the
   *         letters used by different rules overlap.
   *     </li>
   *     <li>
   *         jchzalrnumimnmhp is naughty because it has no double letter.
   *     </li>
   *     <li>
   *         haegwjzuvuyypxyu is naughty because it contains the string xy.
   *     </li>
   *     <li>
   *         dvszwmarrgswjxmb is naughty because it contains only one vowel.
   *     </li>
   * </ul>
   *
   * @param letters The letters of the string to test if it is Naught or Nice.
   * @return Returns true if the letters make up a Nice string.
   */
  public static boolean isNice(String letters) {
    var vowelCount = 0;
    var doubleLetterCount = 0;
    var restrictedCount = 0;
    var lastChar = letters.charAt(0);

    if (isVowel(lastChar)) {
      vowelCount++;
    }

    for (int i = 1; i < letters.length(); i++) {
      var thisChar = letters.charAt(i);

      if (isVowel(thisChar)) {
        vowelCount++;
      }

      if (isDoubleLetter(lastChar, thisChar)) {
        doubleLetterCount++;
      }

      if (isRestricted(lastChar, thisChar)) {
        restrictedCount++;
      }

      if (restrictedCount != 0) {
        break; // Can't be nice if it contains a restricted character sequence!!
      }
      lastChar = thisChar;
    }

    return restrictedCount == 0 && vowelCount >= 3 && doubleLetterCount >= 1;
  }

  /**
   * Realizing the error of his ways, Santa has switched to a better model of determining whether
   * a string is naughty or nice. None of the old rules apply, as they are all clearly ridiculous.
   *
   * <p><br>
   * Now, a nice string is one with all the following properties:
   * <ul>
   *     <li>
   *         It contains a pair of any two letters that appears at least twice in the string without
   *         overlapping, like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
   *     </li>
   *     <li>
   *         It contains at least one letter which repeats with exactly one letter between them,
   *         like xyx, abcdefeghi (efe), or even aaa.
   *     </li>
   * </ul>
   * For example:
   * <ul>
   *     <li>
   *         qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter
   *         that repeats with exactly one letter between them (zxz).
   *     </li>
   *     <li>
   *         xxyxx is nice because it has a pair that appears twice and a letter that repeats with
   *         one between, even though the letters used by each rule overlap.
   *     </li>
   *     <li>
   *         uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single
   *         letter between them.
   *     </li>
   *     <li>
   *         ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo),
   *         but no pair that appears twice.
   *     </li>
   * </ul>
   *
   * @param letters The letters of the string to test if it is Naught or Nice.
   * @return Returns true if the letters make up a Nice string.
   */
  public static boolean isNewNice(String letters) {
    var twoLetterCount = 0;
    var repeatLetterCount = 0;
    var lastChar = letters.charAt(0);

    for (int i = 1; i < letters.length() - 1; i++) {
      final var thisChar = letters.charAt(i);
      var thisString = letters.substring(i - 1, i + 1);

      if (twoLetterCount == 0) {
        var twoLetterIndex = letters.lastIndexOf(thisString);

        if (twoLetterIndex > i) {
          twoLetterCount++;
        }
      }

      if (repeatLetterCount == 0) {
        var nextChar = letters.charAt(i + 1);

        if (lastChar == nextChar) {
          repeatLetterCount++;
        }
      }

      if (repeatLetterCount > 0 && twoLetterCount > 0) {
        break;
      }

      lastChar = thisChar;
    }

    return repeatLetterCount > 0 && twoLetterCount > 0;
  }

  /**
   * Returns true if the specified characters form a restricted sequence.
   *
   * @param c The first character to check.
   * @param o The second character to check.
   * @return True if the specified characters form a restricted sequence.
   */
  public static boolean isRestricted(char c, char o) {
    return (c == 'a' && o == 'b') || (c == 'c' && o == 'd')
        || (c == 'p' && o == 'q') || (c == 'x' && o == 'y');
  }

  /**
   * Returns true if the two characters are the same character.
   *
   * @param c The first character to check.
   * @param o The second character to check.
   * @return True if the two characters are the same character.
   */
  public static boolean isDoubleLetter(char c, char o) {
    return c == o;
  }

  /**
   * Returns true if the specified character is a vowel.
   *
   * @param c The character to check.
   * @return True if the specified character is a vowel.
   */
  public static boolean isVowel(char c) {
    return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
  }
}
