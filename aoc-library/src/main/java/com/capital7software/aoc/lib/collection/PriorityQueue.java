package com.capital7software.aoc.lib.collection;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;

/**
 * A PriorityQueue that uses a backing MinHeap to maintain the priority of its items.
 * Supports both natural ordering for items that implement the Comparable interface. For those
 * items that do not support the Comparable interface a Comparator must be passed to the
 * constructor.
 *
 * @param <T> The type of the items held by this PriorityQueue.
 */
public class PriorityQueue<T> extends MinHeap<T> implements Queue<T> {
  /**
   * Instantiates a new empty PriorityQueue with default capacity and uses the natural ordering.
   * Please note that the type of the items held by this PriorityQueue must implement the
   * Comparable interface.
   */
  public PriorityQueue() {
    super(DEFAULT_INITIAL_SIZE);
  }

  /**
   * Instantiates a new empty PriorityQueue with the specified capacity and uses the natural
   * ordering. Please note that the type of the items held by this PriorityQueue must implement the
   * Comparable interface.
   *
   * @param initialCapacity The initial size of this PriorityQueue.
   */
  public PriorityQueue(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Instantiates a new empty PriorityQueue with the specified capacity and uses the specified
   * Comparator.
   *
   * @param initialCapacity The initial size of this PriorityQueue.
   * @param comparator      The Comparator to use for this PriorityQueue.
   */
  public PriorityQueue(int initialCapacity, Comparator<? super T> comparator) {
    super(initialCapacity, comparator);
  }

  /**
   * Instantiates a new PriorityQueue with the specified items and uses the specified Comparator.
   *
   * @param items      The items to store in this PriorityQueue.
   * @param comparator The Comparator to use for this PriorityQueue.
   */
  public PriorityQueue(T[] items, Comparator<? super T> comparator) {
    super(items, comparator);
  }

  /**
   * An iterator that iterates in-order over the elements of this PriorityQueue.
   */
  @SuppressFBWarnings
  public class PriorityQueueIterator implements Iterator<T> {
    private int cursor;

    /**
     * Instantiates a new iterator instance.
     */
    public PriorityQueueIterator() {
      cursor = 0;
    }

    @Override
    public boolean hasNext() {
      return cursor < numberOfItems;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() throws NoSuchElementException {
      if (cursor >= items.length) {
        throw new NoSuchElementException("next called when this Iterator has no next element!");
      }
      return (T) items[cursor++];
    }
  }

  @Override
  public @NotNull Iterator<T> iterator() {
    return new PriorityQueueIterator();
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> element) {
    throw new UnsupportedOperationException(
        "PriorityQueue<T>.removeAllAll(Collection<?> element): Method not implemented.");
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> element) {
    throw new UnsupportedOperationException(
        "PriorityQueue<T>.retainAll(Collection<?> element): Method not implemented.");
  }

  @Override
  public T element() {
    if (isEmpty()) {
      throw new NoSuchElementException("element has been called on an empty PriorityQueue");
    }
    return peek();
  }

  @Override
  public boolean offer(T element) {
    return add(element);
  }

  @Override
  public T poll() {
    return remove();
  }
}
