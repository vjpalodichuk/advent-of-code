package com.capital7software.aoc2015.lib.graph.constaint;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ValueDomain <T extends Number & Comparable<T>> {
    @NotNull
    T getRandomValue();

    @NotNull
    List<T> getRandomValues(int count);
}
