package com.capital7software.aoc.lib.collection;

import java.util.Comparator;

/**
 * Represents a Minimum Heap, which is capable of efficiently maintaining
 * a sorted list of elements, and the various operations it supports.
 * Supports natural ordering for types that implement the Comparable interface. For those that
 * do not, a Comparator must be specified.
 *
 * @param <T> The type of the value of an element of this Heap.
 * @author Vincent J Palodichuk
 *     <A HREF="mailto:vincent@capital7software.com"> (e-mail me) </A>
 * @version 01/02/2024
 */
public class MinHeap<T> extends ArrayHeap<T> {
  /**
   * Instantiates a new MinHeap with the specified capacity and uses the specified Comparator.
   *
   * @param initialCapacity The initial capacity of this MaxHeap.
   * @param comparator      The Comparator to use.
   */
  public MinHeap(int initialCapacity, Comparator<? super T> comparator) {
    super(initialCapacity, comparator);
  }

  /**
   * Instantiates a new MinHeap with the specified items and uses the specified Comparator.
   *
   * @param items      The initial items of this MaxHeap.
   * @param comparator The Comparator to use.
   */
  public MinHeap(T[] items, Comparator<? super T> comparator) {
    super(items, comparator);
  }

  /**
   * Instantiates a new MinHeap with the specified initial capacity and uses natural ordering.
   *
   * @param initialCapacity The initial capacity of this MaxHeap.
   */
  public MinHeap(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Instantiates a new MinHeap with the specified items and uses natural ordering.
   *
   * @param items The initial items of this MaxHeap.
   */
  public MinHeap(T[] items) {
    super(items);
  }
}
