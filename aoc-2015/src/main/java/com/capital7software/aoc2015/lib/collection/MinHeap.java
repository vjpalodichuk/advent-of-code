package com.capital7software.aoc2015.lib.collection;

import java.util.Comparator;

/**
 * Represents a Minimum Heap, which is capable of efficiently maintaining
 * a sorted list of elements, and the various operations it supports.
 *
 * @author Vincent J Palodichuk
 * <A HREF="mailto:vincent@capital7software.com"> (e-mail me) </A>
 * @version 01/02/2024
 *
 * @param <T> The type of the value of an element of this Heap.
 */
public class MinHeap<T> extends ArrayHeap<T> {
    public MinHeap(int initialCapacity, Comparator<? super T> comparator) {
        super(initialCapacity, comparator);
    }

    public MinHeap(T[] items, Comparator<? super T> comparator) {
        super(items, comparator);
    }

    public MinHeap(int initialCapacity) {
        super(initialCapacity);
    }

    public MinHeap(T[] items) {
        super(items);
    }
}
