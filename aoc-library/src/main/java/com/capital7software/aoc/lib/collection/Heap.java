package com.capital7software.aoc.lib.collection;

import java.util.List;

/**
 * Represents a Heap, which is capable of efficiently maintaining a sorted list of elements,
 * and the various operations it supports.
 *
 * @param <T> The type of the value of an element of this Heap.
 * @author Vincent J Palodichuk
 *     <A HREF="mailto:vincent@capital7software.com"> (e-mail me) </A>
 * @version 01/02/2024
 */
public interface Heap<T> {
  /**
   * Adds the specified item to this Heap.
   * The Heap property is maintained after the new item has been added.
   *
   * @param element The non-null element to add.
   * @return True if the element was successfully added to this ArrayHeap.
   */
  boolean add(T element);

  /**
   * Removes all elements from this Heap.
   */
  void clear();

  /**
   * Returns true if this Heap contains no elements.
   *
   * @return True if this Heap contains no elements.
   */
  boolean isEmpty();

  /**
   * Returns but does not remove the head of this Heap.
   *
   * @return The head of this Heap.
   */
  T peek();

  /**
   * Structures the contents of this Heap so that it meets the properties of a heap.
   */
  void heapify();

  /**
   * Removes and returns the head of this Heap. A null reference is returned if this Heap is empty.
   * The Heap property is maintained after the item has been removed.
   *
   * @return The head of this Heap.
   */
  T remove();

  /**
   * Arranges the contents of this Heap in reverse order of their priority such that the top of
   * the Heap is the last element and the first element would have been the last element removed
   * if remove was called.
   *
   * <p><br>
   * After this method returns, this Heap is in the same state it was in prior to this method
   * being called.
   *
   * @return Returns a new List with the ordered items.
   */
  List<T> sort();

  /**
   * If the specified element contained within this Heap has changed since being added,
   * this method can be called to adjust the element's position within this Heap.
   *
   * @param element The non-null element to adjust.
   */
  void adjust(T element);
}
