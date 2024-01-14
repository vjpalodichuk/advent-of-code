package com.capital7software.aoc.lib.collection;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a Heap, which is capable of efficiently maintaining a sorted list of elements, and the
 * various operations it supports.
 *
 * @author Vincent J Palodichuk
 * <A HREF="mailto:vincent@capital7software.com"> (e-mail me) </A>
 * @version 01/02/2024
 *
 * @param <T> The type of the value of an element of this Heap.
 */
public interface Heap<T> extends Cloneable {
    /**
     * Adds the specified item to this ArrayHeap.
     * The Heap property is maintained after the new item has been added.
     *
     * @param element The non-null element to add.
     * @return True if the element was successfully added to this ArrayHeap.
     */
    boolean add(@NotNull T element);

    /**
     * Removes all elements from this Heap.
     */
    void clear();

    /**
     * Generate a copy of this heap.
     * <p>
     * <b>Note:</b>
     * <ul>
     * 	<li>
     * 		Be sure to cast the return value to the proper <code>Heap&lt;T&gt;</code> type
     *      before use.
     * 	</li>
     * </ul>
     * @return The return value is a copy of this heap. Subsequent changes to the copy will not affect
     * 	       the original, nor vice versa.
     * @throws OutOfMemoryError Indicates that there is insufficient memory for the new heap.
     * <p>
     * (non-Javadoc)
     * @see Object#clone()
     */
    @NotNull
    Heap<T> clone();

    /**
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
     *
     * @return The number of elements in this Heap.
     */
    int size();

    /**
     * Arranges the contents of this Heap in reverse order of their priority such that the top of the Heap is
     * the last element and the first element would have been the last element removed if remove was called.
     * <p>
     * This method is mainly used to iterate over the elements of this Heap in reverse order without having to
     * remove any elements from this Heap.
     * <p>
     * After calling this method, this Heap no longer complies with the properties of a heap. To fix this
     * heapify should be called.
     */
    void sort();

    /**
     * If the elements contained within this Heap have changed since being added to this Heap, this method can be
     * called to fix the top element in this Heap from the bottom up.
     *
     * @param element The element that needs to be adjusted.
     */
    void adjustTopUp(@NotNull T element);
}
