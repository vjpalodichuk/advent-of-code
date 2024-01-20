package com.capital7software.aoc.lib.analysis;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * In the giant field just outside, the springs are arranged into rows.
 * For each row, the condition records show every spring and whether
 * it is operational (.) or damaged (#). This is the part of the condition
 * records that is itself damaged; for some springs, it is simply unknown (?)
 * whether the spring is operational or damaged.
 * <p><br>
 * However, the engineer that produced the condition records also duplicated
 * some of this information in a different format! After the list of springs
 * for a given row, the size of each contiguous group of damaged springs is
 * listed in the order those groups appear in the row. This list always accounts
 * for every damaged spring, and each number is the entire size of its contiguous
 * group (that is, groups are always separated by at least one operational
 * spring: #### would always be 4, never 2,2).
 * <p><br>
 * So, condition records with no unknown spring conditions might look like this:
 * <p><br>
 * <code>
 * #.#.### 1,1,3<br>
 * .#...#....###. 1,1,3<br>
 * .#.###.#.###### 1,3,1,6<br>
 * ####.#...#... 4,1,1<br>
 * #....######..#####. 1,6,5<br>
 * .###.##....# 3,2,1<br>
 * </code>
 * <p><br>
 * However, the condition records are partially damaged; some of the springs' conditions
 * are actually unknown (?). For example:
 * <p><br>
 * <code>
 * ???.### 1,1,3<br>
 * .??..??...?##. 1,1,3<br>
 * ?#?#?#?#?#?#?#? 1,3,1,6<br>
 * ????.#...#... 4,1,1<br>
 * ????.######..#####. 1,6,5<br>
 * ?###???????? 3,2,1<br>
 * </code>
 * <p><br>
 * Equipped with this information, it is your job to figure out how many different arrangements
 * of operational and broken springs fit the given criteria in each row.
 * <p><br>
 * In the first line (???.### 1,1,3), there is exactly one way separate groups of one, one,
 * and three broken springs (in that order) can appear in that row: the first three unknown
 * springs must be broken, then operational, then broken (#.#), making the whole row #.#.###.
 * <p><br>
 * The second line is more interesting: .??..??...?##. 1,1,3 could be a total of four
 * different arrangements. The last ? must always be broken (to satisfy the final contiguous
 * group of three broken springs), and each ?? must hide exactly one of the two broken
 * springs. (Neither ?? could be both broken springs or they would form a single contiguous
 * group of two; if that were true, the numbers afterward would have been 2,3 instead.)
 * Since each ?? can either be #. or .#, there are four possible arrangements of springs.
 * <p><br>
 * The last line is actually consistent with ten different arrangements! Because the first
 * number is 3, the first and second ? must both be . (if either were #, the first number
 * would have to be 4 or higher). However, the remaining run of unknown spring conditions
 * have many different ways they could hold groups of two and one broken springs:
 * <p><br>
 * <code>
 * ?###???????? 3,2,1<br>
 * .###.##.#...<br>
 * .###.##..#..<br>
 * .###.##...#.<br>
 * .###.##....#<br>
 * .###..##.#..<br>
 * .###..##..#.<br>
 * .###..##...#<br>
 * .###...##.#.<br>
 * .###...##..#<br>
 * .###....##.#<br>
 * </code>
 * <p><br>
 * In this example, the number of possible arrangements for each row is:
 * <p><br>
 * <code>
 * ???.### 1,1,3 - 1 arrangement<br>
 * .??..??...?##. 1,1,3 - 4 arrangements<br>
 * ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement<br>
 * ????.#...#... 4,1,1 - 1 arrangement<br>
 * ????.######..#####. 1,6,5 - 4 arrangements<br>
 * ?###???????? 3,2,1 - 10 arrangements<br>
 * </code>
 * <p><br>
 * Adding all the possible arrangement counts together produces a total of 21 arrangements.
 * <p><br>
 * For each row, count all the different arrangements of operational and broken springs that
 * meet the given criteria. What is the sum of those counts?
 * <p><br>
 * As you look out at the field of springs, you feel like there are way more springs than the
 * condition records list. When you examine the records, you discover that they were actually
 * folded up this whole time!
 * <p><br>
 * To unfold the records, on each row, replace the list of spring conditions with five copies
 * of itself (separated by ?) and replace the list of contiguous groups of damaged springs with
 * five copies of itself (separated by ,).
 * <p><br>
 * So, this row:
 * <p><br>
 * <code>
 * .# 1<br>
 * </code>
 * <p><br>
 * Would become:
 * <p><br>
 * <code>
 * .#?.#?.#?.#?.# 1,1,1,1,1<br>
 * </code>
 * <p><br>
 * The first line of the above example would become:
 * <p><br>
 * <code>
 * ???.###????.###????.###????.###????.### 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3<br>
 * </code>
 * <p><br>
 * In the above example, after unfolding, the number of possible arrangements for some rows is now much larger:
 * <p><br>
 * <code>
 * ???.### 1,1,3 - 1 arrangement<br>
 * .??..??...?##. 1,1,3 - 16384 arrangements<br>
 * ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement<br>
 * ????.#...#... 4,1,1 - 16 arrangements<br>
 * ????.######..#####. 1,6,5 - 2500 arrangements<br>
 * ?###???????? 3,2,1 - 506250 arrangements<br>
 * </code>
 * <p><br>
 * After unfolding, adding all the possible arrangement counts together produces 525152.
 * <p><br>
 * Unfold your condition records; what is the new sum of possible arrangement counts?
 */
public class HotSprings {

    private enum Spring {
        Operational('.'),
        Damaged('#'),
        Unknown('?');

        private final char label;

        Spring(char label) {
            this.label = label;
        }

        public static Spring from(char label) {
            for (var value : values()) {
                if (value.label == label) {
                    return value;
                }
            }

            return null;
        }
    }

    private record SpringRecord(List<Spring> springs, List<Integer> damagedSequence) {
        private static final String INITIAL_SPLIT = " ";
        private static final String DAMAGED_SPLIT = ",";

        public static SpringRecord parse(String line) {
            return parse(line, false);
        }

        public static SpringRecord parse(String line, boolean unfold) {
            var split = line.split(INITIAL_SPLIT);

            var springs = new ArrayList<>(split[0].chars()
                    .mapToObj(it -> (char) it)
                    .map(Spring::from)
                    .toList());

            if (unfold) {
                for (int i = 0; i < 4; i++) {
                    springs.add(Spring.Unknown);
                    springs.addAll(split[0].chars()
                            .mapToObj(it -> (char) it)
                            .map(Spring::from)
                            .toList());
                }
            }

            var damagedSequence = new ArrayList<>(Arrays.stream(split[1].split(DAMAGED_SPLIT))
                    .map(Integer::parseInt)
                    .toList());

            if (unfold) {
                for (int i = 0; i < 4; i++) {
                    damagedSequence.addAll(Arrays.stream(split[1].split(DAMAGED_SPLIT))
                            .map(Integer::parseInt)
                            .toList());
                }
            }

            return new SpringRecord(springs, damagedSequence);
        }

        @Override
        public String toString() {
            return "SpringRecord{" +
                    "springs=" + springs +
                    ", damagedSequence=" + damagedSequence +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SpringRecord that)) {
                return false;
            }
            return springs().equals(that.springs()) && damagedSequence().equals(that.damagedSequence());
        }

        @Override
        public int hashCode() {
            return Objects.hash(springs(), damagedSequence());
        }

        /**
         * @return Returns the total number of valid sequences that are possible that satisfy the damagedSequence of
         * this record. A valid sequence is one where the Unknown Springs are replaced with either Operational or
         * Damaged Springs and the damagedSequence is satisfied.
         */
        public long validSequences() {
            var cache = new HashMap<SpringRecord, Long>();
            return validSequences(cache);
        }

        /**
         * This is a recursive method that uses a cache to avoid calculating the same solution.
         *
         * @param cache The cache that will be used
         * @return The total number of valid sequences
         */
        private long validSequences(HashMap<SpringRecord, Long> cache) {
            // Stopping conditions
            // If we already solved this sequence, return it.
            if (cache.containsKey(this)) {
                return cache.get(this);
            }

            // If there are no Springs left to process, then we check if we have any damaged sequences left as well.
            // If we don't have any damaged sequences left to check, then we return 1 as the sequence is valid,
            // else 0.
            if (springs.isEmpty()) {
                if (damagedSequence.isEmpty()) {
                    return 1;
                } else {
                    return 0;
                }
            }

            // If we have some Springs left, but we don't have any damaged sequences left to check, then if
            // the remaining springs are all operational the sequence is valid, and we return 1, else 0.
            if (damagedSequence.isEmpty()) {
                if (springs.contains(Spring.Damaged)) {
                    return 0;
                } else {
                    return 1;
                }
            }

            // Get the next spring and sequence
            var spring = springs.getFirst();
            var sequence = damagedSequence.getFirst();

            // If the next spring could be Operational we ignore it and continue to check the remainder
            // of the sequence
            var countOperational = 0L;

            if (spring != Spring.Damaged) {
                countOperational = new SpringRecord(springs.subList(1, springs.size()), damagedSequence)
                        .validSequences(cache);
            }

            // If the next spring could be Damaged we have to check that the conditions to ensure that the damaged
            // sequence is met. The conditions are:
            // - Must be at least as many Springs remaining as expected by the next damage sequence.
            // - The next damage sequence's count of Springs must all be Damaged or possibly Damaged.
            // - After the damage sequence, there are no Springs left to process, the sequence is ended by an
            //   Operational Spring or possibly Operational Spring.
            // If these conditions are met, we can move on to the next damage sequence and process the remaining
            // Springs.
            var countDamaged = 0L;
            // Assume current Spring is operational or that the current damage sequence is
            // greater than the remaining number of springs!
            if (spring != Spring.Operational && sequence <= springs.size() &&
                    springs.subList(0, sequence).stream().noneMatch(it -> it == Spring.Operational) &&
                    !(springs.size() != sequence && springs.get(sequence) == Spring.Damaged)) {
                countDamaged = new SpringRecord(
                        springs.subList(Math.min(sequence + 1, springs.size()), springs.size()),
                        damagedSequence.subList(1, damagedSequence.size())
                ).validSequences(cache);
            }

            var total = countOperational + countDamaged;

            // Store the total in the cache!!
            cache.put(this, total);

            return total;
        }
    }

    private final List<SpringRecord> records;

    private HotSprings(@NotNull List<SpringRecord> records) {
        this.records = new ArrayList<>(records);
    }

    /**
     * Builds and returns a new HotSprings from the List of Strings. The Strings represent the spring records
     * to parse. If unfold is true, then each spring record is replaced with five copies of itself!
     *
     * @param input The List of Strings to parse in to spring records.
     * @param unfold If true, then each record is replaced with five copies of itself.
     * @return A new HotSprings populated with the spring record from the parsed List of Strings.
     */
    public static @NotNull HotSprings buildHotSprings(@NotNull List<String> input, boolean unfold) {
        if (unfold) {
            return new HotSprings(input.stream().map(it -> SpringRecord.parse(it, true)).toList());
        } else {
            return new HotSprings(input.stream().map(SpringRecord::parse).toList());
        }
    }

    /**
     * Returns the sum of all possible arrangements of spring records.
     *
     * @return The sum of all possible arrangements of spring records.
     */
    public long sumOfAllPossibleArrangements() {
        return records
                .stream()
                .mapToLong(SpringRecord::validSequences)
                .sum();
    }
}
