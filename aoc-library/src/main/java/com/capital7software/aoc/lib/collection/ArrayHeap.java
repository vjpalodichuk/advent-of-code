package com.capital7software.aoc.lib.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

/**
 * Invariant of the ArrayHeap class.
 * <ol>
 *     <li>
 *         The number of elements in the heap is in the instance variable numberOfItems.
 *     </li>
 *     <li>
 *         For an empty heap, we do not care what is stored in the items array;
 *         for a non-empty heap, the elements in the heap are stored in items[0]
 *         through items[numberOfItems - 1], and we don't care what's in the rest of
 *         the items array.
 *     </li>
 *     <li>
 *         The heap is a complete tree.
 *     </li>
 *     <li>
 *         For a max heap, items[0] always contains an item with the greatest value.
 *     </li>
 *     <li>
 *         For a min heap, items[0] always contains an items with the least value.
 *     </li>
 * </ol>
 *
 * @param <T> The type of the value of an element of this ArrayHeap.
 * @author Vincent J Palodichuk
 *     <a href="mailto:vincent@capital7software.com"> (e-mail me) </a>
 * @version 01/02/2024
 */
public abstract class ArrayHeap<T> implements Heap<T> {
  /**
   * The default initial size if one isn't specified.
   */
  protected static final int DEFAULT_INITIAL_SIZE = 32;
  /**
   * The index where the highest priority item is kept.
   */
  protected static final int ROOT_INDEX = 0;
  /**
   * The array to hold all the items in this Heap.
   */
  protected Object[] items;
  /**
   * The number of items currently in this Heap.
   */
  protected int numberOfItems;

  /**
   * The comparator, or null if head uses elements'
   * natural ordering.
   */
  protected final Comparator<? super T> comparator;

  /**
   * Instantiates a new ArrayHeap with a default size.
   */
  public ArrayHeap() {
    this(DEFAULT_INITIAL_SIZE);
  }

  /**
   * Instantiates a new ArrayHeap with initial space of the specified size.
   *
   * @param initialSize The initial size of the new ArrayHeap.
   * @param comparator  The comparator to use for comparison operations.
   */
  public ArrayHeap(int initialSize, Comparator<? super T> comparator) {
    numberOfItems = 0;
    this.comparator = comparator;
    ensureCapacity(initialSize);
  }

  /**
   * Instantiates a new ArrayHeap with initial space of the specified size.
   *
   * @param initialSize The initial size of the new ArrayHeap.
   */
  public ArrayHeap(int initialSize) {
    this(initialSize, null);
  }

  /**
   * Instantiates a new ArrayHeap whose size and elements are provided in the specified items array.
   *
   * @param items      The array to clone and populate the new ArrayHeap with.
   * @param comparator The comparator to use for comparison operations.
   */
  public ArrayHeap(@NotNull T[] items, Comparator<? super T> comparator) {
    numberOfItems = items.length;
    this.items = items.clone();
    this.comparator = comparator;

    heapify();
  }

  /**
   * Instantiates a new ArrayHeap whose size and elements are provided in the specified items array.
   *
   * @param items The array to clone and populate the new ArrayHeap with.
   */
  public ArrayHeap(@NotNull T[] items) {
    this(items, null);
  }

  /**
   * Type safe getter for an element in the items array.
   *
   * @param items The array to get the item from.
   * @param index The index of the item in the array.
   * @param <T>   The type of the items.
   * @return The item in th array at that index or null if there is no element at that index.
   */
  @SuppressWarnings("unchecked")
  protected static <T extends Comparable<T>> T getItem(@NotNull Object[] items, int index) {
    return (T) items[index];
  }

  /**
   * Returns the element found at the specified index.
   *
   * @param index The index to retrieve the element from.
   * @return The element found at the specified index.
   */
  protected T getItem(int index) {
    return getItem(items, index);
  }

  /**
   * Calculates the index of the child to the left of the specified parent index.
   *
   * @param parentIndex The parent of the child we want the index for.
   * @return The index of the child to the left of the specified parent index.
   */
  protected static int getLeftChildIndex(int parentIndex) {
    return (parentIndex * 2) + 1;
  }

  /**
   * Calculates the index of the parent for the specified child index.
   *
   * @param childIndex The child index we want to find the parent index for.
   * @return The index of the parent for the specified child index.
   */
  protected static int getParentIndex(int childIndex) {
    return (childIndex - 1) / 2;
  }

  /**
   * Calculates the index of the child to the right of the specified parent index.
   *
   * @param parentIndex The parent of the child we want the index for.
   * @return The index of the child to the right of the specified parent index.
   */
  protected static int getRightChildIndex(int parentIndex) {
    return (parentIndex * 2) + 2;
  }

  /**
   * Rebuilds this heap into a Heap from the bottom up.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void heapify() {
    // Rebuild the heap from the bottom heap up!
    final var elems = items;
    var n = numberOfItems;
    var i = (n >>> 1) - 1; // Only do half!
    final var comp = comparator;

    if (comp == null) {
      for (; i >= 0; i--) {
        siftDownComparable(i, (T) elems[i], elems, n);
      }
    } else {
      for (; i >= 0; i--) {
        siftDownComparator(i, (T) elems[i], elems, n, comp);
      }
    }
  }

  /**
   * Sorts the specified items array in reverse order so that the element with the maximum value
   * is moved to the end of the array and the first element in the array will have the
   * minimum value.
   *
   * <p><br>
   * After this method returns, the items array has been sorted and is no longer a Heap.
   *
   * @param heapify If true, the specified items array is first rebuilt into a Heap.
   */
  @SuppressWarnings("unchecked")
  protected void heapSort(boolean heapify) {
    if (heapify) {
      heapify();
    }

    int endIndex = numberOfItems - 1;

    while (endIndex > ROOT_INDEX) {
      // Put the highest priority element at the end of the array
      swap(endIndex, ROOT_INDEX);
      numberOfItems--; // One less element to consider

      // Move the new top element in to the correct position.
      var element = items[ROOT_INDEX];
      siftDown(ROOT_INDEX, (T) element);
      endIndex--;
    }
  }

  /**
   * Sorts and destroys this Heap. After this method returns, the Heap
   * has been destroyed and the items array contains the elements in
   * reverse priority.
   */
  protected void heapSort() {
    heapSort(false);
  }

  /**
   * Maintains the specified Heap by comparing the values of the parent and children and swapping
   * them when the children are not less than the parent. This process starts at the specified
   * startIndex and continues as long as a swap occurred or the endIndex has been reached.
   *
   * @param fillIndex The index of the position to fill.
   * @param element   The element to insert.
   */
  protected void siftDown(
      int fillIndex,
      @NotNull T element
  ) {
    if (comparator != null) {
      siftDownComparator(fillIndex, element, items, numberOfItems, comparator);
    } else {
      siftDownComparable(fillIndex, element, items, numberOfItems);
    }
  }

  /**
   * Maintains the specified Heap by comparing the values of the parent and children and swapping
   * them when the children are not less than the parent. This process starts at the specified
   * startIndex and continues as long as a swap occurred or the endIndex has been reached.
   *
   * @param fillIndex  The index that needs to be filled.
   * @param element    The element to insert.
   * @param items      The Heap to maintain.
   * @param size       The number of items in the heap.
   * @param comparator The comparator to use for comparison operations.
   * @param <T>        The type of the elements in the items array.
   */
  @SuppressWarnings("unchecked")
  protected static <T> void siftDownComparator(
      int fillIndex,
      @NotNull T element,
      @NotNull Object[] items,
      int size,
      @NotNull Comparator<? super T> comparator
  ) {
    // We need to ensure that we maintain the heap
    int half = size >>> 1; // divide by 2.

    while (fillIndex < half) {
      var child = getLeftChildIndex(fillIndex);
      var c = items[child];
      var right = getRightChildIndex(fillIndex);
      var r = items[right];
      if (right < size && comparator.compare((T) c, (T) r) > 0) {
        c = items[child = right];
      }
      if (comparator.compare(element, (T) c) <= 0) {
        break; // We are done!
      }
      items[fillIndex] = c;
      fillIndex = child;
    }
    items[fillIndex] = element;
  }

  /**
   * Maintains the specified Heap by comparing the values of the parent and children and swapping
   * them when the children are not less than the parent. This process starts at the specified
   * startIndex and continues as long as a swap occurred or the endIndex has been reached.
   *
   * @param fillIndex The index that needs to be filled.
   * @param element   The element to insert.
   * @param items     The Heap to maintain.
   * @param size      The number of items in the heap.
   * @param <T>       The type of the elements in the items array.
   */
  @SuppressWarnings("unchecked")
  protected static <T> void siftDownComparable(
      int fillIndex,
      T element,
      @NotNull Object[] items,
      int size
  ) {
    // We need to ensure that we maintain the heap
    int half = size >>> 1; // divide by 2.
    var key = (Comparable<? super T>) element;

    while (fillIndex < half) {
      var child = getLeftChildIndex(fillIndex);
      var c = items[child];
      var right = getRightChildIndex(fillIndex);
      var r = items[right];
      if (right < size && ((Comparable<? super T>) c).compareTo((T) r) > 0) {
        c = items[child = right];
      }
      if (key.compareTo((T) c) <= 0) {
        break; // We are done!
      }
      items[fillIndex] = c;
      fillIndex = child;
    }
    items[fillIndex] = key;
  }

  /**
   * Similar to siftDown but starts with the child at endIndex and continues up to startIndex
   * as long as a swap of parent and child needs to be performed.
   *
   * @param fillIndex The index we need to fill as something has changed.
   * @param element   The element that needs to be moved.
   */
  protected void siftUp(int fillIndex, T element) {
    if (comparator != null) {
      siftUpComparator(fillIndex, element, items, comparator);
    } else {
      siftUpComparable(fillIndex, element, items);
    }
  }

  /**
   * Similar to siftDown but starts with the child at endIndex and continues up to startIndex as
   * long as a swap of parent and child needs to be performed.
   *
   * @param fillIndex  The index we need to fill as something has changed.
   * @param element    The element that needs to be moved.
   * @param items      The Heap to maintain.
   * @param comparator The comparator to use for comparison operations.
   * @param <T>        The type of the elements in the items array.
   */
  @SuppressWarnings("unchecked")
  protected static <T> void siftUpComparator(
      int fillIndex,
      @NotNull T element,
      @NotNull Object[] items,
      @NotNull Comparator<? super T> comparator
  ) {
    // We need to ensure that we maintain the heap
    while (fillIndex > 0) {
      var parentIndex = getParentIndex(fillIndex);
      var e = items[parentIndex];
      if (comparator.compare(element, (T) e) >= 0) {
        break; // Nothing to move!
      }
      items[fillIndex] = e;
      fillIndex = parentIndex;
    }
    items[fillIndex] = element;
  }

  /**
   * Similar to siftDown but starts with the child at endIndex and continues up to startIndex as
   * long as a swap of parent and child needs to be performed.
   *
   * @param fillIndex The index we need to fill as something has changed.
   * @param element   The element that needs to be moved.
   * @param items     The Heap to maintain.
   * @param <T>       The type of the elements in the items array.
   */
  @SuppressWarnings("unchecked")
  protected static <T> void siftUpComparable(
      int fillIndex,
      @NotNull T element,
      @NotNull Object[] items
  ) {
    var key = (Comparable<? super T>) element;
    // We need to ensure that we maintain the heap
    while (fillIndex > 0) {
      var parentIndex = getParentIndex(fillIndex);
      var e = items[parentIndex];
      if (key.compareTo((T) e) >= 0) {
        break; // Nothing to move!
      }
      items[fillIndex] = e;
      fillIndex = parentIndex;
    }
    items[fillIndex] = key;
  }

  /**
   * Returns the comparator used to order the elements in this
   * queue, or {@code null} if this queue is sorted according to
   * the {@linkplain Comparable natural ordering} of its elements.
   *
   * @return the comparator used to order this queue, or
   *     {@code null} if this queue is sorted according to the
   *     natural ordering of its elements
   */
  public Comparator<? super T> comparator() {
    return comparator;
  }

  /**
   * Typesafe swap of the values of the two specified indexes in the specified items array.
   *
   * @param items The array to swap the values in.
   * @param i     The first index in the swap.
   * @param j     The second index in the swap.
   * @param <T>   The type of the items.
   */
  protected static <T> void swap(@NotNull T[] items, int i, int j) {
    T temp = items[i];
    items[i] = items[j];
    items[j] = temp;
  }

  /**
   * Swaps the elements of this ArrayHeap for indexes i and j.
   *
   * @param i The first index in the swap.
   * @param j The second index in the swap.
   */
  protected void swap(int i, int j) {
    swap(items, i, j);
  }

  @Override
  public boolean add(@NotNull T element) {
    var count = numberOfItems;
    if (count >= items.length) {
      // Double the capacity and add 1; this works even if manyItems is 0.
      // However, in
      // case that manyItems * 2 + 1 is beyond integer.MAX_VALUE, there
      // will be an
      // arithmetic overflow and the sequence will fail.
      ensureCapacity(count * 2 + 1);
    }

    siftUp(count, element);
    numberOfItems = count + 1;

    return true;
  }

  @Override
  public void clear() {
    final Object[] elems = items;
    for (int i = 0, count = numberOfItems; i < count; i++) {
      elems[i] = null;
    }

    numberOfItems = 0;
  }

  @SuppressWarnings("unchecked")
  @Override
  @NotNull
  public Heap<T> clone() {
    // Clone a Heap<T> object
    ArrayHeap<T> answer;

    try {
      answer = (ArrayHeap<T>) super.clone();
    } catch (CloneNotSupportedException exception) {
      // This exception should not occur. But if it does, it would
      // indicate a programming error that made super.clone unavailable. The most common cause
      // would be forgetting the "implements Cloneable" clause at the start of the class.
      throw new RuntimeException("ArrayHeap<T>.clone(): This class does not implement Cloneable.");
    }

    // Deep-copy the array.
    answer.items = items.clone();

    return answer;
  }

  /**
   * Change the current capacity of this heap.
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>This heap's capacity has been changed to at least
   * <code>minimumCapacity</code>. If the capacity was already at or greater
   * than <code>minimumCapacity</code>, then the capacity is left unchanged.
   * </li>
   * </ul>
   *
   * @param minimumCapacity The new capacity of this heap.
   * @throws OutOfMemoryError Indicates insufficient memory for altering the capacity.
   */
  public void ensureCapacity(int minimumCapacity) {
    if (items == null) {
      items = new Object[minimumCapacity];
    } else if (items.length < minimumCapacity) {
      Object[] temp;
      temp = new Object[minimumCapacity];
      System.arraycopy(items, 0, temp, 0, numberOfItems);
      items = temp;
    }
  }

  /**
   * Return the current capacity of this heap.
   *
   * <p><br>
   * <b>Note:</b>
   * <ul>
   * <li>The add method works efficiently (without needing more memory) until
   * this capacity is reached.</li>
   * </ul>
   *
   * @return Returns the current capacity of this heap.
   */
  public int getCapacity() {
    return items.length;
  }

  @Override
  public boolean isEmpty() {
    return (numberOfItems == 0);
  }

  /**
   * Returns a copy of the elements in the items array.
   *
   * @return A copy of the elements in the items array.
   */
  @NotNull
  public Object @NotNull [] toArray() {
    return Arrays.copyOf(items, numberOfItems);
  }

  /**
   * A type-safe way of obtaining a copy of the elements in the items array.
   *
   * @param array The destination array. If it is large enough it will be filled with the
   *              items in this Heap.
   * @param <E>   The type of the elements in the array.
   * @return A copy of the items array.
   */
  @NotNull
  @SuppressWarnings({"all", "unchecked"})
  public <E> E @NotNull [] toArray(@NotNull E @NotNull [] array) {
    final int size = this.numberOfItems;
    if (array.length < size) {
      // Make a new array of a's runtime type, but my contents:
      return (E[]) Arrays.copyOf(items, size, array.getClass());
    }

    System.arraycopy(items, 0, array, 0, size);
    if (array.length > size) {
      array[size] = null;
    }
    return array;
  }

  @Override
  public T peek() {
    return numberOfItems > 0 ? getItem(ROOT_INDEX) : null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T remove() {
    final T answer;
    final Object[] elems;

    if ((answer = (T) ((elems = items)[ROOT_INDEX])) != null) {
      final int count;
      final T element = (T) elems[(count = --numberOfItems)];
      elems[count] = null;
      if (count > ROOT_INDEX) {
        // Still have items to fix back in to a heap!
        final var comp = comparator;
        if (comp == null) {
          siftDownComparable(ROOT_INDEX, element, elems, count);
        } else {
          siftDownComparator(ROOT_INDEX, element, elems, count, comp);
        }
      }
    }

    return answer;
  }

  /**
   * Removes a single instance of the specified element from this queue,
   * if it is present.  More formally, removes an element {@code e} such
   * that {@code o.equals(e)}, if this queue contains one or more such
   * elements.  Returns {@code true} if and only if this queue contained
   * the specified element (or equivalently, if this queue changed as a
   * result of the call).
   *
   * @param o element to be removed from this queue, if present
   * @return {@code true} if this queue changed as a result of the call
   */
  public boolean remove(Object o) {
    int i = indexOf(o);
    if (i == -1) {
      return false;
    } else {
      removeAt(i);
      return true;
    }
  }

  @Override
  public int size() {
    return numberOfItems;
  }

  @Override
  public void sort() {
    heapSort();
  }

  @Override
  public void adjustTopUp(@NotNull T element) {
    var index = indexOf(element);

    if (index > 0) {
      siftUp(index, element);
    }
  }

  /**
   * Reduce the current capacity of this heap to its actual size (i.e., the number of
   * elements it contains).
   *
   * <p><br>
   * <b>Postcondition:</b>
   * <ul>
   * <li>This heap's capacity has been changed to its current size.</li>
   * </ul>
   *
   * @throws OutOfMemoryError Indicates insufficient memory for altering the capacity.
   */
  public void trimToSize() {
    Object[] temp;

    if (items.length != numberOfItems) {
      temp = new Object[numberOfItems];
      System.arraycopy(items, 0, temp, 0, numberOfItems);
      items = temp;
    }
  }

  /**
   * Adds all the items in the specified collection to this Heap.
   *
   * @param collection The collection of items to add to this Heap.
   * @return True if the items were added.
   */
  public boolean addAll(@NotNull Collection<? extends T> collection) {
    collection.forEach(this::add);
    return true;
  }

  /**
   * Returns true if this heap contains the specified element. More formally, returns true if
   * and only if this heap contains at least one element e such that o.equals(e).
   *
   * @param object object to be checked for containment in this heap.
   * @return True if this heap contains the specified element.
   */
  public boolean contains(Object object) {
    return indexOf(object) >= 0;
  }

  /**
   * Returns true if this heap contains all the specified elements. More formally, returns true
   * if and only if this heap contains all the elements such that for each element o.equals(e).
   *
   * @param collection The list that contains the objects to be checked for containment
   *                   in this heap.
   * @return True if this heap contains all the specified elements in the list.
   */
  public boolean containsAll(@NotNull Collection<?> collection) {
    return collection.stream().map(this::contains).filter(it -> it).count() == collection.size();
  }

  /**
   * The index of the specified element in this heap or -1 if the element is not found in this heap.
   *
   * @param o The element to search for.
   * @return The index of the specified element or -1.
   */
  public int indexOf(Object o) {
    if (o != null) {
      final Object[] es = items;
      for (int i = 0, n = numberOfItems; i < n; i++) {
        if (o.equals(es[i])) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Removes the ith element from queue.
   *
   * <p><br>
   * Normally this method leaves the elements at up to i-1,
   * inclusive, untouched.  Under these circumstances, it returns
   * null.  Occasionally, in order to maintain the heap invariant,
   * it must swap a later element of the list with one earlier than
   * i.  Under these circumstances, this method returns the element
   * that was previously at the end of the list and is now at some
   * position before i. This fact is used by iterator.remove so as to
   * avoid missing traversing elements.
   *
   * @param i The index of the item to remove.
   * @return The removed item or null if the item doesn't exist.
   */
  @SuppressWarnings("unchecked")
  protected T removeAt(int i) {
    // assert i >= 0 && i < size;
    final Object[] es = items;
    int s = --numberOfItems;
    if (s == i) {
      // removed last element
      es[i] = null;
    } else {
      T moved = (T) es[s];
      es[s] = null;
      siftDown(i, moved);
      if (es[i] == moved) {
        siftUp(i, moved);
        if (es[i] != moved) {
          return moved;
        }
      }
    }
    return null;
  }

  /**
   * Identity-based version for use in Itr.remove.
   *
   * @param o element to be removed from this queue, if present
   */
  void removeEq(Object o) {
    final Object[] es = items;
    for (int i = 0, n = numberOfItems; i < n; i++) {
      if (o == es[i]) {
        removeAt(i);
        break;
      }
    }
  }
}

