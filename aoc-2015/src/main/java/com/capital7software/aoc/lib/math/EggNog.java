package com.capital7software.aoc.lib.math;

import com.capital7software.aoc.lib.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The elves bought too much eggnog again. The amount they bought is specified in liters.
 * To fit it all into your refrigerator, you'll need to move it into smaller containers.
 * You take an inventory of the capacities of the available containers which is specified in containers.
 * <br><br>
 * For capital7software, suppose you have containers of size 20, 15, 10, 5, and 5 liters.
 * If you need to store 25 liters, there are four ways to do it:
 * <ul>
 *     <li>
 *         15 and 10
 *     </li>
 *     <li>
 *         20 and 5 (the first 5)
 *     </li>
 *     <li>
 *         20 and 5 (the second 5)
 *     </li>
 *     <li>
 *         15, 5, and 5
 *     </li>
 * </ul>
 *
 * @param liters The total amount of Eggnog that needs to be stored.
 * @param containers The list of container sizes that are available to store the Eggnog.
 */
public record EggNog(long liters, List<Long> containers) {

    /**
     * Parses the list of containers and returns a new instance of EggNog with the specified number of liters and
     * a list of container sizes from the input list of strings.
     *
     * @param liters The total amount of Eggnog that needs to be stored.
     * @param input The list of strings where each string is a non-zero integer indicating its capacity.
     * @return A new instance of EggNog with the specified liters and list of available container capacities.
     */
    public static @NotNull EggNog parse(long liters, @NotNull List<String> input) {
        return new EggNog(liters, input.stream().map(Long::parseLong).toList());
    }

    /**
     * Counts and returns a Pair of the number of different combinations that can be formed from the list of
     * container capacities for this EggNog instance to exactly contain liters amount of EggNog.
     *
     * @return The first element of the pair is the number of combinations and the second element is a List of
     * the actual combinations.
     */
    public @NotNull Pair<Integer, List<List<Long>>> combinations() {
        var count = new AtomicInteger(0);
        List<List<Long>> usedList = new ArrayList<>();
        List<Long> currentUsed = new ArrayList<>();

        countCombinations(0, 0, count, currentUsed, usedList);
        return new Pair<>(count.get(), usedList);
    }

    private void countCombinations(
            long currentLength,
            int index,
            AtomicInteger count,
            List<Long> currentUsed,
            List<List<Long>> usedList
    ) {
        for (int i = index; i < containers.size(); i++) {
            currentUsed.add(containers.get(i));
            if (currentLength + containers.get(i) == liters) {
                count.incrementAndGet();
                usedList.add(new ArrayList<>(currentUsed));
            } else if (currentLength + containers.get(i) < liters) {
                countCombinations(
                        currentLength + containers.get(i),
                        i + 1,
                        count,
                        currentUsed,
                        usedList
                );
            }
            if (!currentUsed.isEmpty()) {
                currentUsed.removeLast();
            }
        }
    }
}
