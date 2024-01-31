package com.capital7software.aoc.lib.string;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Stores a copy of the specified input String that can then be hashed at will.
 *
 * @param string The String to save to operate on.
 */
public record HashString(String string) {

  /**
   * The HASH algorithm is a way to turn any string of characters into a single number in the
   * range 0 to 255. To run the HASH algorithm on a string, start with a current value of 0.
   * Then, for each character in the string starting from the beginning:
   * <ol>
   *     <li>
   *         Determine the ASCII code for the current character of the string.
   *     </li>
   *     <li>
   *         Increase the current value by the ASCII code you just determined.
   *     </li>
   *     <li>
   *         Set the current value to itself multiplied by 17.
   *     </li>
   *     <li>
   *         Set the current value to the remainder of dividing itself by 256.
   *     </li>
   * </ol>
   * After following these steps for each character in the string in order, the current value is
   * the output of the HASH algorithm.
   *
   * <p><br>
   * So, to find the result of running the HASH algorithm on the string HASH:
   *
   * <p><br>
   * The current value starts at 0.<br>
   * The first character is H; its ASCII code is 72.<br>
   * The current value increases to 72.<br>
   * The current value is multiplied by 17 to become 1224.<br>
   * The current value becomes 200 (the remainder of 1224 divided by 256).<br>
   * The next character is A; its ASCII code is 65.<br>
   * The current value increases to 265.<br>
   * The current value is multiplied by 17 to become 4505.<br>
   * The current value becomes 153 (the remainder of 4505 divided by 256).<br>
   * The next character is S; its ASCII code is 83.<br>
   * The current value increases to 236.<br>
   * The current value is multiplied by 17 to become 4012.<br>
   * The current value becomes 172 (the remainder of 4012 divided by 256).<br>
   * The next character is H; its ASCII code is 72.<br>
   * The current value increases to 244.<br>
   * The current value is multiplied by 17 to become 4148.<br>
   * The current value becomes 52 (the remainder of 4148 divided by 256).<br>
   *
   * <p><br>
   * So, the result of running the HASH algorithm on the string HASH is 52.
   *
   * @return The hash of this HashString.
   */
  public int hashAscii() {
    var characters = string.getBytes(StandardCharsets.US_ASCII);

    var hash = 0L;

    for (var character : characters) {
      hash += character;
      hash *= 17;
      hash %= 256;
    }

    return (int) hash;
  }

  /**
   * Builds and returns a List of HashString parsed from the specified List of input Strings.
   *
   * @param input The List of String to parse and hash.
   * @return A List of HashString parsed from the specified List of input Strings.
   */
  public static List<HashString> parse(List<String> input) {
    var builder = new StringBuilder();

    input.forEach(builder::append);

    var temp = builder.toString().replace("\n", "");
    return Arrays.stream(temp.split(",")).map(HashString::new).toList();
  }
}
