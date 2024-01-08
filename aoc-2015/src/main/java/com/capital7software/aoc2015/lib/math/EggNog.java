package com.capital7software.aoc2015.lib.math;

import com.capital7software.aoc2015.lib.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record EggNog(long liters, List<Long> containers) {

    public static EggNog parse(long liters, List<String> input) {
        return new EggNog(liters, input.stream().map(Long::parseLong).toList());
    }

    public Pair<Integer, List<Integer>> combinations() {
        var count = new AtomicInteger(0);
        List<Integer> usedList = new ArrayList<>();

        countCombinations(0, 0, count, 0, usedList);
        return new Pair<>(count.get(), usedList);
    }

    private void countCombinations(long currentLength, int index, AtomicInteger count, int used, List<Integer> usedList) {
        for (int i = index; i < containers.size(); i++) {
            if (currentLength + containers.get(i) == liters) {
                count.incrementAndGet();
                usedList.add(used + 1);
            } else if (currentLength + containers.get(i) < liters) {
                countCombinations(currentLength + containers.get(i), i + 1, count, used + 1, usedList);
            }
        }
    }
}
