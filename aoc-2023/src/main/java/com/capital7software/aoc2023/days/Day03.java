package com.capital7software.aoc2023.days;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Day03 {
    private record PartNumber(
            int id,
            int offset,
            int lineNumber,
            int symbolOffset,
            int symbolLineNumber,
            char symbolChar
    ) {
        public boolean connectedToGear() {
            return symbolChar == '*';
        }

        @Override
            public String toString() {
                return "PartNumber{" +
                        "id='" + id + '\'' +
                        ", offset=" + offset +
                        ", lineNumber=" + lineNumber +
                        ", symbolOffset=" + symbolOffset +
                        ", symbolLineNumber=" + symbolLineNumber +
                        ", symbolChar=" + symbolChar +
                        '}';
            }
        }

    private static class NumberEntry {
        private final int offset;
        private final String id;
        private final int lineNumber;
        private PartNumber partNumber;

        public NumberEntry(int offset, String id, int lineNumber) {
            this.offset = offset;
            this.id = id;
            this.lineNumber = lineNumber;
        }

        public int getOffset() {
            return offset;
        }

        public String getId() {
            return id;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public PartNumber getPartNumber() {
            return partNumber;
        }

        public void setPartNumber(PartNumber partNumber) {
            this.partNumber = partNumber;
        }

        public boolean hasPartNumber() {
            return partNumber != null;
        }

        @Override
        public String toString() {
            return "NumberEntry{" +
                    "offset=" + offset +
                    ", id='" + id + '\'' +
                    ", lineNumber='" + lineNumber + '\'' +
                    ", partNumber=" + partNumber +
                    '}';
        }
    }

    private record PotentialGear(
            char symbol,
            int lineNumber,
            int offset
    ) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PotentialGear that = (PotentialGear) o;
            return symbol == that.symbol && lineNumber == that.lineNumber && offset == that.offset;
        }

        @Override
        public int hashCode() {
            return Objects.hash(symbol, lineNumber, offset);
        }

        @Override
        public String toString() {
            return "PotentialGear{" +
                    "symbol=" + symbol +
                    ", lineNumber=" + lineNumber +
                    ", offset=" + offset +
                    '}';
        }
    }

    private static final String inputFilename = "inputs/input_day_03-01.txt";
    public static void main(String[] args) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            var url = classloader.getResource(inputFilename);
            assert url != null;
            var path = Paths.get(url.toURI());
            // Open the input file and read all the lines!
            var lines = Files.readAllLines(path);
            System.out.println("Read " + lines.size() + " lines of the engine schematic");

            var partNumbers = findAllPartNumbers(lines);

            System.out.println(("Found " + partNumbers.size() + " valid part numbers!"));
            partNumbers.forEach(System.out::println);

            var sum = partNumbers
                    .stream()
                    .filter(NumberEntry::hasPartNumber)
                    .mapToLong(it -> it.getPartNumber().id())
                    .sum();

            System.out.println("Summation of all part numbers is: " + sum);

            var potentialGears = new HashMap<PotentialGear, List<PartNumber>>();

            partNumbers.forEach(it -> {
                if (it.hasPartNumber() && it.getPartNumber().connectedToGear()) {
                    var key = new PotentialGear(it.getPartNumber().symbolChar(),
                            it.getPartNumber().symbolLineNumber(),
                            it.getPartNumber().symbolOffset());
                    potentialGears.computeIfAbsent(key, k -> new ArrayList<>()).add(it.getPartNumber());
                }
            });

            var gearRatios = potentialGears
                    .keySet()
                    .stream()
                    .filter(it -> potentialGears.get(it).size() == 2)
                    .mapToInt(it -> potentialGears.get(it).get(0).id() * potentialGears.get(it).get(1).id())
                    .sum();

            System.out.println("Summation of all gear ratios is: " + gearRatios);

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A number is considered a part number if it is adjacent to a symbol either vertically, horizontally, or diagonally;
     * A period is not considered a symbol.
     *
     * @param schematic Must contain at least one non-empty line and all lines must be of equal length in terms of
     *                  number of characters.
     * @return A list of all part numbers found in the schematic.
     */
    private static List<NumberEntry> findAllPartNumbers(List<String> schematic) {
        var partNumbers = new ArrayList<NumberEntry>();

        // When we encounter a number in the current line and it is not adjacent to a non-period symbol, we then check
        // the previous line (if the current line is not the first line) for a non-period symbol that is diagonally or
        // vertically adjacent to the current number. If we still haven't found an adjacent non-period symbol we then
        // proceed to check the next line (if the current line is not the last line) for a symbol that is diagonally or
        // vertically adjacent to the current number.
        //
        // If we have found a symbol adjacent to the current number, we then add that number to the current list of
        // parts. We continue until we have processed the entire schematic.
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
        return partNumbers;
    }

    private static PartNumber getPartNumber(
            NumberEntry numberEntry,
            List<String> schematic,
            char[] currentLine,
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

    private static PartNumber getVerticallyOrDiagonallyAdjacent(
            NumberEntry numberEntry,
            List<String> schematic,
            int currentLineIndex
    ) {
        var beforeOffset = numberEntry.getOffset() - 1;
        var afterOffset = numberEntry.getOffset() + numberEntry.getId().length();
        PartNumber valid;
        // Check the previous line provided this isn't the first line
        if (currentLineIndex > 0) {
            // We simply need to check for a symbol from the previous start index of the current number through
            // the index that follows the current number in the previous line. If a symbol is found, we return true.
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
            // We simply need to check for a symbol from the previous start index of the current number through
            // the index that follows the current number in the next line. If a symbol is found, we return true.
            return getLineWithSymbol(numberEntry,
                    beforeOffset,
                    afterOffset,
                    schematic.get(currentLineIndex + 1).toCharArray(),
                    currentLineIndex + 1);
        }

        return null;
    }

    private static PartNumber getHorizontallyAdjacent(
            NumberEntry numberEntry,
            char[] currentLine
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
                        currentLine[afterOffset]);            }
        }
        return null;
    }

    private static PartNumber getLineWithSymbol(NumberEntry numberEntry, int startOffset, int endOffset, char[] line, int lineNumber) {
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

    private static boolean isSymbol(char c) {
        return c != '.' && !Character.isLetterOrDigit(c);
    }

    private static NumberEntry findNumber(char[] line, int offset, int lineNumber) {
        var builder = new StringBuilder();
        var foundStart = false;
        var startIndex = offset;

        for (int i = offset; i < line.length; i++) {
            if (!Character.isDigit(line[i])) {
                if (foundStart) {
                    break; // at the end of the number
                } else {
                    continue; // continue until we found the start of the number
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
