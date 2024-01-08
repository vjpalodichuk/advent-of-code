package com.capital7software.aoc2015.lib.graph.constaint;

import com.capital7software.aoc2015.lib.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public interface Solver<T extends Number & Comparable<T>> {
    void addUnknown(@NotNull String id);

    void setValueDomain(@NotNull ValueDomain<T> valueDomain);

    void addVariable(@NotNull String id, @NotNull Function<Map<String, T>, T> variable);

    void addConstraint(@NotNull String id, @NotNull BiPredicate<Map<String, T>, Map<String, T>> constraint);

    void setScoreFunction(@NotNull BiFunction<Map<String, T>, Map<String, T>, T> score);

    @NotNull
    Pair<T, Map<String, T>> max(long maxIterations);

    @NotNull
    Pair<T, Map<String, T>> min(long maxIterations);
}
