package com.capital7software.aoc.lib.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class PriorityQueue<T> extends MinHeap<T> implements Queue<T> {
    public PriorityQueue() {
        super(DEFAULT_INITIAL_SIZE);
    }

    public PriorityQueue(int initialCapacity) {
        super(initialCapacity);
    }

    public PriorityQueue(int initialCapacity, Comparator<? super T> comparator) {
        super(initialCapacity, comparator);
    }

    public PriorityQueue(T[] items, Comparator<? super T> comparator) {
        super(items, comparator);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        throw new UnsupportedOperationException("PriorityQueue<T>.iterator(): Method not implemented.");
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> arg0) {
        throw new UnsupportedOperationException("PriorityQueue<T>.removeAllAll(Collection<?> arg0): Method not implemented.");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> arg0) {
        throw new UnsupportedOperationException("PriorityQueue<T>.retainAll(Collection<?> arg0): Method not implemented.");
    }

    @Override
    public T element() {
        if (isEmpty()) {
            throw new NoSuchElementException("element has been called on an empty PriorityQueue");
        }
        return peek();
    }

    @Override
    public boolean offer(T arg0) {
        return add(arg0);
    }

    @Override
    public T poll() {
        return remove();
    }
}
