package com.capital7software.aoc2015.lib.collection;

import java.util.Comparator;

/**
 * Represents a Maximum Heap, which is capable of efficiently maintaining a
 * sorted list of elements, and the various operations it supports.
 *
 * @param <T> The type of the value of an element of this Heap.
 * @author Vincent J Palodichuk
 * <A HREF="mailto:vincent@capital7software.com"> (e-mail me) </A>
 * @version 01/02/2024
 */
public class MaxHeap<T extends Comparable<T>> extends ArrayHeap<T> {
    public MaxHeap(int initialCapacity, Comparator<? super T> comparator) {
        super(initialCapacity, comparator);
    }

    public MaxHeap(T[] items, Comparator<? super T> comparator) {
        super(items, comparator);
    }

    public MaxHeap(int initialCapacity) {
        super(initialCapacity, Comparator.reverseOrder());
    }

    public MaxHeap(T[] items) {
        super(items, Comparator.reverseOrder());
    }
}
