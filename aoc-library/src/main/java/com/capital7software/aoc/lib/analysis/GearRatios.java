package com.capital7software.aoc.lib.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * The engineer explains that an engine part seems to be missing from the engine,
 * but nobody can figure out which one. If you can add up all the part numbers in
 * the engine schematic, it should be easy to work out which part is missing.
 *
 * <p><br>
 * The engine schematic (your puzzle input) consists of a visual representation of the engine.
 * There are lots of numbers and symbols you don't really understand, but apparently any number
 * adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum.
 * (Periods (.) do not count as a symbol.)
 *
 * <p><br>
 * Here is an example engine schematic:
 *
 * <p><br>
 * <code>
 * 467..114..<br>
 * ...*......<br>
 * ..35..633.<br>
 * ......#...<br>
 * 617*......<br>
 * .....+.58.<br>
 * ..592.....<br>
 * ......755.<br>
 * ...$.*....<br>
 * .664.598..<br>
 * </code>
 *
 * <p><br>
 * In this schematic, two numbers are not part numbers because they are not adjacent to
 * a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a
 * symbol and so is a part number; their sum is 4361.
 *
 * <p><br>
 * Of course, the actual engine schematic is much larger. What is the sum of all the
 * part numbers in the engine schematic?
 *
 * <p><br>
 * Your puzzle answer was 529618.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * The engineer finds the missing part and installs it in the engine! As the engine springs
 * to life, you jump in the closest gondola, finally ready to ascend to the water source.
 *
 * <p><br>
 * You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately,
 * the gondola has a phone labeled "help", so you pick it up and the engineer answers.
 *
 * <p><br>
 * Before you can explain the situation, she suggests that you look out the window.
 * There stands the engineer, holding a phone in one hand and waving with the other.
 * You're going so slowly that you haven't even left the station. You exit the gondola.
 *
 * <p><br>
 * The missing part wasn't the only issue - one of the gears in the engine is wrong.
 * A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is
 * the result of multiplying those two numbers together.
 *
 * <p><br>
 * This time, you need to find the gear ratio of every gear and add them all up so that
 * the engineer can figure out which gear needs to be replaced.
 *
 * <p><br>
 * Consider the same engine schematic again:
 *
 * <p><br>
 * <code>
 * 467..114..<br>
 * ...*......<br>
 * ..35..633.<br>
 * ......#...<br>
 * 617*......<br>
 * .....+.58.<br>
 * ..592.....<br>
 * ......755.<br>
 * ...$.*....<br>
 * .664.598..<br>
 * </code>
 *
 * <p><br>
 * In this schematic, there are two gears. The first is in the top left; it has part
 * numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right;
 * its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent
 * to one part number.) Adding up all the gear ratios produces 467835.
 *
 * <p><br>
 * What is the sum of all the gear ratios in your engine schematic?
 */
public record GearRatios() {
  /**
   * A Gondola part number.
   *
   * @param id               The ID of the part, which is its number.
   * @param offset           The offset in the schematic where the part was found.
   * @param lineNumber       The line number in the schematic where the part was found.
   * @param symbolOffset     The offset in the schematic where the symbol it was adjacent
   *                         to was found.
   * @param symbolLineNumber The line number in the schematic where the symbol was found.
   * @param symbolChar       The character that represents the symbol.
   */
  public record PartNumber(
      int id,
      int offset,
      int lineNumber,
      int symbolOffset,
      int symbolLineNumber,
      char symbolChar
  ) {
    /**
     * Returns true if this PartNumber is connected to a gear.
     *
     * @return True if this PartNumber is connected to a gear.
     */
    public boolean connectedToGear() {
      return symbolChar == '*';
    }
  }

  private static class NumberEntry {
    @Getter
    private final int offset;
    private final String id;
    @Getter
    private final int lineNumber;
    @Setter
    @Getter
    private PartNumber partNumber;

    public NumberEntry(int offset, @NotNull String id, int lineNumber) {
      this.offset = offset;
      this.id = id;
      this.lineNumber = lineNumber;
    }

    public @NotNull String getId() {
      return id;
    }

    public boolean hasPartNumber() {
      return partNumber != null;
    }

    @Override
    public String toString() {
      return "NumberEntry{"
          + "offset=" + offset
          + ", id='" + id + '\''
          + ", lineNumber='" + lineNumber + '\''
          + ", partNumber=" + partNumber
          + '}';
    }
  }

  private record PotentialGear(
      char symbol,
      int lineNumber,
      int offset
  ) {
  }

  /**
   * A number is considered a part number if it is adjacent to a symbol either vertically,
   * horizontally, or diagonally;
   * A period is not considered a symbol.
   *
   * @param schematic Must contain at least one non-empty line and all lines must be of equal
   *                  length in terms of number of characters.
   * @return A list of all part numbers found in the schematic.
   */
  public @NotNull List<PartNumber> findAllPartNumbers(@NotNull List<String> schematic) {
    var partNumbers = new ArrayList<NumberEntry>();

    // When we encounter a number in the current line, and it is not adjacent to a non-period
    // symbol, we then check the previous line (if the current line is not the first line) for
    // a non-period symbol that is diagonally or vertically adjacent to the current number. If we
    // still haven't found an adjacent non-period symbol we then proceed to check the next line
    // (if the current line is not the last line) for a symbol that is diagonally or vertically
    // adjacent to the current number.
    //
    // If we have found a symbol adjacent to the current number, we then add that number to the
    // current list of parts. We continue until we have processed the entire schematic.
    // We start at the top until the end.
    for (int i = 0; i < schematic.size(); i++) {
      var line = schematic.get(i).toCharArray();
      int j = 0;
      while (j < line.length) {
        var numberEntry = findNumber(line, j, i);
        if (numberEntry == null) {
          // didn't find another number so move on to the next line
          break;
        }

        // Now that we have a number, we have to determine if it is a part number
        var partNumber = getPartNumber(numberEntry, schematic, line, i);

        if (partNumber != null) {
          numberEntry.setPartNumber(partNumber);
          partNumbers.add(numberEntry);
        }

        // Move past the current number
        j = numberEntry.getOffset() + numberEntry.getId().length();
      }
    }
    return partNumbers
        .stream()
        .filter(NumberEntry::hasPartNumber)
        .map(NumberEntry::getPartNumber)
        .toList();
  }

  /**
   * Returns a list of all gear ratios from the specified List of PartNumbers.
   *
   * @param partNumbers The PartNumbers to get the gear ratios for.
   * @return A list of all gear ratios from the specified List of PartNumbers.
   */
  public @NotNull List<Long> findAllGearRatios(@NotNull List<PartNumber> partNumbers) {
    var potentialGears = new HashMap<PotentialGear, List<PartNumber>>();

    partNumbers.forEach(it -> {
      if (it.connectedToGear()) {
        var key = new PotentialGear(it.symbolChar(),
                                    it.symbolLineNumber(),
                                    it.symbolOffset());
        potentialGears.computeIfAbsent(key, k -> new ArrayList<>()).add(it);
      }
    });

    return potentialGears
        .keySet()
        .stream()
        .filter(it -> potentialGears.get(it).size() == 2)
        .map(it -> (long) potentialGears
            .get(it)
            .getFirst()
            .id() * potentialGears.get(it).get(1).id()
        )
        .toList();
  }

  private PartNumber getPartNumber(
      @NotNull NumberEntry numberEntry,
      @NotNull List<String> schematic,
      char @NotNull [] currentLine,
      int currentLineIndex
  ) {
    PartNumber valid;

    // Check horizontally first
    valid = getHorizontallyAdjacent(numberEntry, currentLine);

    if (valid == null) {
      // We only continue to check if we haven't found a valid adjacent symbol
      valid = getVerticallyOrDiagonallyAdjacent(numberEntry, schematic, currentLineIndex);
    }

    return valid;
  }

  private PartNumber getVerticallyOrDiagonallyAdjacent(
      @NotNull NumberEntry numberEntry,
      @NotNull List<String> schematic,
      int currentLineIndex
  ) {
    var beforeOffset = numberEntry.getOffset() - 1;
    var afterOffset = numberEntry.getOffset() + numberEntry.getId().length();
    PartNumber valid;
    // Check the previous line provided this isn't the first line
    if (currentLineIndex > 0) {
      // We simply need to check for a symbol from the previous start index of the current
      // number through the index that follows the current number in the previous line.
      // If a symbol is found, we return true.
      valid = getLineWithSymbol(numberEntry,
                                beforeOffset,
                                afterOffset,
                                schematic.get(currentLineIndex - 1).toCharArray(),
                                currentLineIndex - 1);
      if (valid != null) {
        return valid;
      }
    }
    // Check the next line provided this isn't the last line
    if (currentLineIndex < schematic.size() - 1) {
      // We simply need to check for a symbol from the previous start index of the
      // current number through the index that follows the current number in the next line.
      // If a symbol is found, we return true.
      return getLineWithSymbol(numberEntry,
                               beforeOffset,
                               afterOffset,
                               schematic.get(currentLineIndex + 1).toCharArray(),
                               currentLineIndex + 1);
    }

    return null;
  }

  private PartNumber getHorizontallyAdjacent(
      @NotNull NumberEntry numberEntry,
      char @NotNull [] currentLine
  ) {
    var beforeOffset = numberEntry.getOffset() - 1;
    var afterOffset = numberEntry.getOffset() + numberEntry.getId().length();

    // Check before the start
    if (beforeOffset >= 0 && beforeOffset < currentLine.length) {
      if (isSymbol(currentLine[beforeOffset])) {
        return new PartNumber(Integer.parseInt(numberEntry.getId()),
                              numberEntry.getOffset(),
                              numberEntry.getLineNumber(),
                              beforeOffset,
                              numberEntry.getLineNumber(),
                              currentLine[beforeOffset]);
      }
    }
    if (afterOffset >= 0 && afterOffset < currentLine.length) {
      if (isSymbol(currentLine[afterOffset])) {
        return new PartNumber(Integer.parseInt(numberEntry.getId()),
                              numberEntry.getOffset(),
                              numberEntry.getLineNumber(),
                              afterOffset,
                              numberEntry.getLineNumber(),
                              currentLine[afterOffset]);
      }
    }
    return null;
  }

  private PartNumber getLineWithSymbol(
      @NotNull NumberEntry numberEntry,
      int startOffset,
      int endOffset,
      char @NotNull [] line,
      int lineNumber
  ) {
    // Ensure the offsets fall within the valid bounds of the line!
    if (startOffset < 0) {
      startOffset = 0;
    }
    if (endOffset >= line.length) {
      endOffset = line.length - 1;
    }

    PartNumber result = null;

    for (int i = startOffset; i <= endOffset; i++) {
      if (isSymbol(line[i])) {
        result = new PartNumber(Integer.parseInt(numberEntry.getId()),
                                numberEntry.getOffset(),
                                numberEntry.getLineNumber(),
                                i,
                                lineNumber,
                                line[i]);
        break;
      }
    }

    return result;
  }

  private boolean isSymbol(char c) {
    return c != '.' && !Character.isLetterOrDigit(c);
  }

  private NumberEntry findNumber(char[] line, int offset, int lineNumber) {
    var builder = new StringBuilder();
    var foundStart = false;
    var startIndex = offset;

    for (int i = offset; i < line.length; i++) {
      if (!Character.isDigit(line[i])) {
        if (foundStart) {
          break; // at the end of the number
        } else {
          continue; // continue until we find the start of the number
        }
      }
      // We found a digit
      if (!foundStart) {
        startIndex = i;
        foundStart = true;
      }
      builder.append(line[i]);
    }

    // If we found a number return it along with the starting index in the line!
    if (foundStart) {
      return new NumberEntry(startIndex, builder.toString(), lineNumber);
    } else {
      return null; // No number found
    }
  }
}
