package com.capital7software.aoc.lib.collection

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serial
import java.io.Serializable
import java.util.AbstractQueue
import java.util.ArrayDeque
import java.util.Arrays
import java.util.Objects
import java.util.PriorityQueue
import java.util.SortedSet
import java.util.Spliterator
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.max


/**
 * A PriorityQueue that uses a backing heap to maintain the priority of its items and a
 * [HashMap] to keep track of the items in the queue.
 *
 * Supports natural ordering for items that implement the Comparable interface. For those
 * items that do not support the Comparable interface a Comparator must be passed to the
 * constructor.
 *
 * Unlike [PriorityQueue], this queue uses a [HashMap] to keep track of the element indexes in
 * the heap. This means this queue performs contains operations in O(1) and adjust operations
 * in O(log n) time. Additionally, **duplicates are NOT allowed** in this queue. If an attempt is
 * made to add the same element twice it will fail!
 *
 * This class and its iterator implement all the *optional* methods of the
 * [Collection] and [Iterator] interfaces.  The Iterator provided in method [iterator] and
 * the Spliterator provided in method [spliterator] are *not* guaranteed to traverse
 * the elements of the priority queue in any particular order. If you need ordered
 * traversal, consider using `Arrays.sort(pq.toArray())` or [sort].
 *
 * Implementation note: this implementation provides O(log(n)) time for the enqueuing and
 * dequeue methods ([offer], [poll], [remove], and [add]);
 * constant time for the [remove] object and [contains] methods; and constant time for
 * the retrieval methods [peek], [element], and [size]). Space complexity of this implementation
 * is larger due to the use of a [HashMap] to track the indexes of the elements.
 *
 * @param initialCapacity The initial size of this PriorityQueue.
 * @param comparator      The Comparator to use for this PriorityQueue.
 * @param <T> The type of the items held by this PriorityQueue.
 */
class PriorityQueueSet<T : Any> @JvmOverloads constructor(
    initialCapacity: Int = DEFAULT_INITIAL_CAPACITY,
    @SuppressWarnings("serial") // Conditionally serializable
    val comparator: Comparator<T>? = null
) : AbstractQueue<T>(), MutableSet<T>, Cloneable, RandomAccess, Heap<T>, Iterable<T>, Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 1L
    private const val DEFAULT_INITIAL_CAPACITY: Int = 15
    private const val ROOT_INDEX: Int = 0
    private const val SOFT_MAX_ARRAY_LENGTH: Int = Int.MAX_VALUE - 8

    /**
     * Adapted from the Java JDK.
     *
     * Computes a new array length given an array's current length, a minimum growth
     * amount, and a preferred growth amount. The computation is done in an overflow-safe
     * fashion.
     *
     * This method is used by objects that contain an array that might need to be grown
     * in order to fulfill some immediate need (the minimum growth amount) but would also
     * like to request more space (the preferred growth amount) in order to accommodate
     * potential future needs. The returned length is usually clamped at the soft maximum
     * length in order to avoid hitting the JVM implementation limit. However, the soft
     * maximum will be exceeded if the minimum growth amount requires it.
     *
     * If the preferred growth amount is less than the minimum growth amount, the
     * minimum growth amount is used as the preferred growth amount.
     *
     * The preferred length is determined by adding the preferred growth amount to the
     * current length. If the preferred length does not exceed the soft maximum length
     * (SOFT_MAX_ARRAY_LENGTH) then the preferred length is returned.
     *
     * If the preferred length exceeds the soft maximum, we use the minimum growth
     * amount. The minimum required length is determined by adding the minimum growth
     * amount to the current length. If the minimum required length exceeds Integer.MAX_VALUE,
     * then this method throws OutOfMemoryError. Otherwise, this method returns the greatest of
     * the soft maximum or the minimum required length.
     *
     * Note that this method does not do any array allocation itself; it only does array
     * length growth computations. However, it will throw OutOfMemoryError as noted above.
     *
     * Note also that this method cannot detect the JVM's implementation limit, and it
     * may compute and return a length value up to and including Integer.MAX_VALUE that
     * might exceed the JVM's implementation limit. In that case, the caller will likely
     * attempt an array allocation with that length and encounter an OutOfMemoryError.
     * Of course, regardless of the length value returned from this method, the caller
     * may encounter OutOfMemoryError if there is insufficient heap to fulfill the request.
     *
     * @param oldLength   current length of the array (must be non-negative)
     * @param minGrowth   minimum required growth amount (must be positive)
     * @param prefGrowth  preferred growth amount
     * @return the new array length
     * @throws OutOfMemoryError if the new length would exceed Integer.MAX_VALUE
     */
    private fun newLength(oldLength: Int, minGrowth: Int, prefGrowth: Int): Int {
      // preconditions not checked because of inlining
      // assert oldLength >= 0
      // assert minGrowth > 0

      // might overflow
      val prefLength = (oldLength + max(minGrowth.toDouble(), prefGrowth.toDouble())).toInt()
      return if (prefLength in 1..SOFT_MAX_ARRAY_LENGTH) {
        prefLength
      } else {
        // put code cold in a separate method
        hugeLength(oldLength, minGrowth)
      }
    }

    /**
     * Adapted from the Java JDK. Calculates and returns a new length for an array.
     *
     * @param oldLength The previous length of the array.
     * @param minGrowth The minimum amount the new length will increase by.
     * @return The new length to use for the array.
     */
    private fun hugeLength(oldLength: Int, minGrowth: Int): Int {
      val minLength = oldLength + minGrowth
      return if (minLength < 0) { // overflow
        throw OutOfMemoryError(
            "Required array length $oldLength + $minGrowth is too large")
      } else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
        SOFT_MAX_ARRAY_LENGTH
      } else {
        minLength
      }
    }

    /**
     * Ensures that the specified items array has at least the specified capacity.
     *
     * @param minimumCapacity The new capacity of this heap.
     * @param items The items array to possibly expand.
     * @param numberOfItems The number of items that are in the items array.
     * @return A new array with at least the specified capacity, and it contains the contents
     * of the specified items array.
     * @throws OutOfMemoryError Indicates insufficient memory for altering the capacity.
     */
    private fun ensureCapacity(
        minimumCapacity: Int,
        items: Array<Any?>? = null,
        numberOfItems: Int = 0
    ): Array<Any?> {
      val oldCapacity = items?.size ?: 0
      val preferredGrowth = if (oldCapacity < 64) oldCapacity + 2 else oldCapacity shr 1

      val newCapacity = newLength(
          oldCapacity,
          minimumCapacity - oldCapacity,
          preferredGrowth
      )

      return if (items == null) {
        arrayOfNulls(newCapacity)
      } else if (items.size < newCapacity) {
        val temp: Array<Any?> = arrayOfNulls(newCapacity)
        System.arraycopy(items, 0, temp, 0, numberOfItems)
        temp
      } else {
        items
      }
    }

    /**
     * Reduce the current capacity of the specified queue to its actual size (i.e., the number of
     * elements it contains).
     *
     * @throws OutOfMemoryError Indicates insufficient memory for altering the capacity.
     */
    @JvmStatic
    @JvmOverloads
    fun trimToSize(
        items: Array<Any?>,
        numberOfItems: Int = items.size
    ): Array<Any?> {
      return if (items.size != numberOfItems) {
        val temp: Array<Any?> = arrayOfNulls(numberOfItems)
        System.arraycopy(items, 0, temp, 0, numberOfItems)
        temp
      } else {
        items
      }
    }

    /**
     * Calculates the index of the child to the left of the specified parent index.
     *
     * @param parentIndex The parent of the child we want the index for.
     * @return The index of the child to the left of the specified parent index.
     */
    private fun getLeftChildIndex(parentIndex: Int): Int {
      return (parentIndex * 2) + 1
    }

    /**
     * Calculates the index of the parent for the specified child index.
     *
     * @param childIndex The child index we want to find the parent index for.
     * @return The index of the parent for the specified child index.
     */
    private fun getParentIndex(childIndex: Int): Int {
      return (childIndex - 1) / 2
    }

    /**
     * Calculates the index of the child to the right of the specified parent index.
     *
     * @param parentIndex The parent of the child we want the index for.
     * @return The index of the child to the right of the specified parent index.
     */
    private fun getRightChildIndex(parentIndex: Int): Int {
      return (parentIndex * 2) + 2
    }

    /**
     * Maintains the specified Heap by comparing the values of the parent and children and swapping
     * them when the children are not less than the parent. This process starts at the specified
     * startIndex and continues as long as a swap occurred or the endIndex has been reached.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param startIndex The index that needs to be filled.
     * @param element    The element to insert.
     * @param items      The Heap to maintain.
     * @param indexes    The map of elements to indexes.
     * @param size       The number of items in the heap.
     * @param comparator The comparator to use for comparison operations.
     * @param <T>        The type of the elements in the items array.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> siftDownComparator(
        startIndex: Int,
        element: T,
        items: Array<Any?>,
        indexes: MutableMap<T, Int>,
        size: Int,
        comparator: Comparator<in T>
    ) {
      // We need to ensure that we maintain the heap
      var index = startIndex
      val half = size ushr 1 // divide by 2.

      while (index < half) {
        var child = getLeftChildIndex(index)
        var c = items[child]
        val right = getRightChildIndex(index)
        val r = items[right]
        if (right < size && comparator.compare(c as T?, r as T?) > 0) {
          child = right
          c = items[child]
        }
        if (comparator.compare(element, c as T) <= 0) {
          break // We are done!
        }
        items[index] = c
        indexes[c] = index
        index = child
      }
      items[index] = element
      indexes[element] = index
    }

    /**
     * Maintains the specified Heap by comparing the values of the parent and children and swapping
     * them when the children are not less than the parent. This process starts at the specified
     * startIndex and continues as long as a swap occurred or the endIndex has been reached.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param startIndex The index that needs to be filled.
     * @param element    The element to insert.
     * @param items      The Heap to maintain.
     * @param indexes    The map of elements to indexes.
     * @param size       The number of items in the heap.
     * @param <T>        The type of the elements in the items array.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> siftDownComparable(
        startIndex: Int,
        element: T,
        items: Array<Any?>,
        indexes: MutableMap<T, Int>,
        size: Int
    ) {
      // We need to ensure that we maintain the heap
      var index = startIndex
      val half = size ushr 1 // divide by 2.
      val key = element as Comparable<T>

      while (index < half) {
        var child = getLeftChildIndex(index)
        var c = items[child]
        val right = getRightChildIndex(index)
        if (right < size && (c as Comparable<T>) > items[right] as T) {
          child = right
          c = items[child]
        }
        if (key <= c as T) {
          break // We are done!
        }
        items[index] = c
        indexes[c] = index
        index = child
      }
      items[index] = element
      indexes[element] = index
    }

    /**
     * Similar to siftDown but starts with the parent at startIndex and continues up to
     * the root index as long as a swap of parent and child needs to be performed.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param startIndex The index we need to fill as something has changed.
     * @param element    The element that needs to be moved.
     * @param items      The Heap to maintain.
     * @param indexes    The map of elements to indexes.
     * @param comparator The comparator to use for comparison operations.
     * @param <T>        The type of the elements in the items array.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> siftUpComparator(
        startIndex: Int,
        element: T,
        items: Array<Any?>,
        indexes: MutableMap<T, Int>,
        comparator: Comparator<in T>
    ) {
      // We need to ensure that we maintain the heap
      var index = startIndex
      while (index > 0) {
        val parentIndex = getParentIndex(index)
        val e = items[parentIndex] as T
        if (comparator.compare(element, e) >= 0) {
          break // Nothing to move!
        }
        items[index] = e
        indexes[e] = index
        index = parentIndex
      }
      items[index] = element
      indexes[element] = index
    }

    /**
     * Similar to siftDown but starts with the parent at startIndex and continues up as
     * long as a swap of parent and child needs to be performed.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param startIndex The index we need to fill as something has changed.
     * @param element    The element that needs to be moved.
     * @param items      The Heap to maintain.
     * @param indexes    The map of elements to indexes.
     * @param <T>        The type of the elements in the items array.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> siftUpComparable(
        startIndex: Int,
        element: T,
        items: Array<Any?>,
        indexes: MutableMap<T, Int>
    ) {
      var index = startIndex
      val key = element as Comparable<T>
      // We need to ensure that we maintain the heap
      while (index > 0) {
        val parentIndex = getParentIndex(index)
        val e = items[parentIndex] as T
        if (key >= e) {
          break // Nothing to move!
        }
        items[index] = e
        indexes[e] = index
        index = parentIndex
      }
      items[index] = element
      indexes[element] = index
    }

    /**
     * Typesafe swap of the values of the two specified indexes in the specified items array.
     *
     * @param items   The array to swap the values in.
     * @param indexes The map of elements to indexes.
     * @param first   The first index in the swap.
     * @param second  The second index in the swap.
     * @param <T>     The type of the items.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> swap(
        items: Array<Any?>,
        indexes: MutableMap<T, Int>,
        first: Int,
        second: Int
    ) {
      val temp = items[first] as T
      val temp2 = items[second] as T
      items[first] = temp2
      indexes[temp2] = first

      items[second] = temp
      indexes[temp] = second
    }

    /**
     * Returns a List that contains the same elements as the specified heap but in sequential
     * order.
     *
     * @param items         The heap array to modify.
     * @param indexes       The map of elements to indexes.
     * @param numberOfItems The number of items in the heap.
     * @param comparator    The comparator to use; if null, then natural order is used.
     * @param <T>           The type of the elements in the items array.
     * @return A List that contains the same elements as the specified heap but in sequential
     * order.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> heapSort(
        items: Array<Any?>,
        indexes: MutableMap<T, Int>,
        numberOfItems: Int,
        comparator: Comparator<T>?
    ): List<T> {
      val cloned = items.clone()
      val clonedIndexes = HashMap(indexes)
      var count = numberOfItems

      var endIndex = numberOfItems - 1

      while (endIndex > ROOT_INDEX) {
        // Put the highest priority element at the end of the array
        swap(cloned, clonedIndexes, endIndex, ROOT_INDEX)
        count-- // One less element to consider

        // Move the new top element in to the correct position.
        val element = cloned[ROOT_INDEX]
        if (comparator == null) {
          siftDownComparable(ROOT_INDEX, element as T, cloned, clonedIndexes, count)
        } else {
          siftDownComparator(ROOT_INDEX, element as T, cloned, clonedIndexes, count, comparator)
        }
        endIndex--
      }
      val list = ArrayList<T>(numberOfItems)
      for (i in 0 until numberOfItems) {
        list.add(cloned[i] as T)
      }
      return list
    }

    /**
     * Rebuilds the specified heap into a Heap from the bottom up.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param items         The heap array to modify.
     * @param indexes       The map of elements to indexes.
     * @param numberOfItems The number of items in the heap.
     * @param comparator    The comparator to use; if null, then natural order is used.
     * @param <T>           The type of the elements in the items array.
    </T> */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> heapify(
        items: Array<Any?>,
        indexes: MutableMap<T, Int>,
        numberOfItems: Int,
        comparator: Comparator<T>?
    ) {
      // Rebuild the heap from the bottom heap up!
      var i = (numberOfItems ushr 1) - 1 // Only do half!

      if (comparator == null) {
        while (i >= 0) {
          siftDownComparable(i, items[i] as T, items, indexes, numberOfItems)
          i--
        }
      } else {
        while (i >= 0) {
          siftDownComparator(i, items[i] as T, items, indexes, numberOfItems, comparator)
          i--
        }
      }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getUnchecked(index: Int, items: Array<Any?>): T? {
      return items[index] as T?
    }

    /**
     * Creates a `PriorityQueueSet` containing the elements in the specified collection.
     * This priority queue will be ordered according to the same ordering as the given
     * collection if it is a [SortedSet], [PriorityQueue],
     * [com.capital7software.aoc.lib.collection.PriorityQueue], or [PriorityQueueSet];
     * otherwise natural ordering will be used.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param  collection the collection whose elements are to be placed into this priority queue.
     * @throws ClassCastException if elements of the specified queue cannot be compared to
     * one another according to the collection's ordering.
     * @throws NullPointerException if the specified collection or any of its elements are null.
     */
    @JvmStatic
    @JvmOverloads
    fun <T : Any> from(
        collection: Collection<T>,
        comparator: Comparator<T>? = null
    ): PriorityQueueSet<in T> {
      return when (collection) {
        is SortedSet -> {
          initElementsFromCollection(collection, collection.comparator())
        }

        is com.capital7software.aoc.lib.collection.PriorityQueue -> {
          initElementsFromCollection(collection, collection.comparator)
        }

        is PriorityQueueSet -> initElementsFromCollection(collection, collection.comparator)
        is PriorityQueue -> initFromPriorityQueue(collection)
        else -> initFromCollection(collection, comparator)
      }
    }

    /**
     * Creates a `PriorityQueueSet` containing the elements in the specified queue. This
     * priority queue will be ordered according to the same ordering as the given queue.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param  queue the queue whose elements are to be placed into this priority queue.
     * @throws ClassCastException if elements of the specified queue cannot be compared to one
     * another according to the queue's ordering.
     * @throws NullPointerException if the specified queue or any of its elements are null.
     */
    @JvmStatic
    fun <T : Any> from(queue: PriorityQueue<out T>): PriorityQueueSet<T> {
      return initFromPriorityQueue(queue)
    }

    /**
     * Creates a `PriorityQueueSet` containing the elements in the specified queue. This
     * priority queue will be ordered according to the same ordering as the given queue.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param  queue the queue whose elements are to be placed into this priority queue.
     * @throws ClassCastException if elements of the specified queue cannot be compared to one
     * another according to the queue's ordering.
     * @throws NullPointerException if the specified queue or any of its elements are null.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> from(
        queue: com.capital7software.aoc.lib.collection.PriorityQueue<out T>
    ): PriorityQueueSet<T> {
      val comparator = queue.comparator
      return initElementsFromCollection(queue, comparator as Comparator<T>)
    }

    /**
     * Creates a `PriorityQueueSet` containing the elements in the specified queue. This
     * priority queue will be ordered according to the same ordering as the given queue.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param  queueSet the queue whose elements are to be placed into this priority queue.
     * @throws ClassCastException if elements of the specified queue cannot be compared to
     * one another according to the queue's ordering.
     * @throws NullPointerException if the specified queue or any of its elements are null.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> from(queueSet: PriorityQueueSet<out T>): PriorityQueueSet<T> {
      val comparator = queueSet.comparator
      return initElementsFromCollection(queueSet, comparator as Comparator<T>?)
    }

    /**
     * Creates a `PriorityQueueSet` containing the elements in the specified sorted set.
     * This priority queue will be ordered according to the same ordering as the given sorted set.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param  sortedSet the sorted set whose elements are to be placed into this priority queue.
     * @throws ClassCastException if elements of the specified sorted set cannot be compared to
     * one another according to the sorted set's ordering.
     * @throws NullPointerException if the specified sorted set or any of its elements are null.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> from(sortedSet: SortedSet<out T>): PriorityQueueSet<T> {
      val comparator = sortedSet.comparator()
      return initElementsFromCollection(sortedSet, comparator as Comparator<T>)
    }

    /**
     * Ensures that the specified array has space for at least a single element.
     * Returns the specified array if it has space; otherwise a new array is returned.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param elements The array to check
     * @return The specified array if it has space; otherwise a new array.
     */
    private fun ensureNonEmpty(elements: Array<Any?>): Array<Any?> {
      return if ((elements.isNotEmpty())) {
        elements
      } else {
        arrayOfNulls(1)
      }
    }

    /**
     * Initializes queue array with elements from the given queue. Does not rebuild the heap
     * after the elements have been loaded.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param queue The [PriorityQueue] to load into a new queue.
     * @return A new [PriorityQueueSet] built with the elements from the specified collection.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> initFromPriorityQueue(queue: PriorityQueue<out T>): PriorityQueueSet<T> {
      return if (queue.javaClass == PriorityQueue::class.java) {
        val items = ensureNonEmpty(queue.toTypedArray())
        val size = queue.size
        val comparator = queue.comparator() as Comparator<T>
        PriorityQueueSet(items, size, comparator)
      } else {
        initFromCollection(queue, null)
      }
    }

    /**
     * Initializes queue array with elements from the given Collection. Does not rebuild the heap
     * after the elements have been loaded.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param collection The collection to load into a queue.
     * @param comparator The comparator to use; may be null.
     * @return A new [PriorityQueueSet] built with the elements from the specified collection.
     */
    private fun <T : Any> initElementsFromCollection(
        collection: Collection<T>,
        comparator: Comparator<T>?
    ): PriorityQueueSet<T> {
      var elements: Array<Any?> = collection.toTypedArray()
      val len = elements.size
      if (collection.javaClass != ArrayList::class.java) {
        elements = Arrays.copyOf(elements, len, Array<Any?>::class.java)
      }
      if (len == 1 || comparator != null) {
        for (element in elements) {
          if (element == null) {
            throw NullPointerException("The element is null.")
          }
        }
      }
      val items = ensureNonEmpty(elements)
      return PriorityQueueSet(items, len, comparator)
    }

    /**
     * Initializes queue array with elements from the given Collection. Rebuilds the heap
     * once the elements have been loaded.
     *
     * Adapted from java.util.PriorityQueue
     *
     * @param collection The collection to load into a queue.
     * @param comparator The comparator to use; may be null.
     * @return A new [PriorityQueueSet] built with the elements from the specified collection.
     */
    private fun <T : Any> initFromCollection(
        collection: Collection<T>,
        comparator: Comparator<T>?
    ): PriorityQueueSet<T> {
      val queue: PriorityQueueSet<T> = initElementsFromCollection(collection, comparator)
      queue.heapify()
      return queue
    }
  }

  private var numberOfItems: Int = 0

  @Transient
  private var items: Array<Any?> = ensureCapacity(initialCapacity)

  @Transient
  private var indexes: HashMap<T, Int> = hashMapOf()

  @Transient
  private var modCount: Int = 0

  /**
   * Instantiates a new empty PriorityQueue with default capacity and uses the specified
   * [Comparator] for ordering elements.
   *
   * @param comparator The comparator to use with this instance.
   */
  constructor(comparator: Comparator<T>) : this(DEFAULT_INITIAL_CAPACITY, comparator)

  private constructor(items: Array<Any?>, count: Int, comparator: Comparator<T>?) : this(1, comparator) {
    this.items = items
    numberOfItems = count

    for (i in 0 until numberOfItems) {
      indexes[getUnchecked(i)!!] = i
    }
  }

  /**
   * Returns the number of elements in this queue.
   *
   * @return The number of elements in this queue.
   */
  override val size: Int
    get() = numberOfItems

  /**
   * Maintains the specified Heap by comparing the values of the parent and children and swapping
   * them when the children are not less than the parent. This process starts at the specified
   * startIndex and continues as long as a swap occurred or the end has been reached.
   *
   * @param startIndex The index of the position to fill.
   * @param element    The element to insert.
   */
  private fun siftDown(
      startIndex: Int,
      element: T
  ) {
    if (comparator != null) {
      siftDownComparator(startIndex, element, items, indexes, numberOfItems, comparator)
    } else {
      siftDownComparable(startIndex, element, items, indexes, numberOfItems)
    }
  }

  /**
   * Similar to siftDown but starts with the child at endIndex and continues up to startIndex
   * as long as a swap of parent and child needs to be performed.
   *
   * @param startIndex The index we need to fill as something has changed.
   * @param element    The element that needs to be moved.
   */
  private fun siftUp(startIndex: Int, element: T) {
    if (comparator != null) {
      siftUpComparator(startIndex, element, items, indexes, comparator)
    } else {
      siftUpComparable(startIndex, element, items, indexes)
    }
  }

  /**
   * Sorts the specified items array in reverse order so that the element with the maximum value
   * is moved to the end of the array and the first element in the array will have the
   * minimum value.
   *
   * @return Returns a new List with the ordered items.
   */
  private fun heapSort(): List<T> {
    return heapSort(items, indexes, numberOfItems, comparator)
  }

  private fun getUnchecked(index: Int): T? {
    return getUnchecked(index, items)
  }

  /**
   * Returns the element at the specified position in this list.
   *
   * @param  index index of the element to return
   * @return the element at the specified position in this list
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  operator fun get(index: Int): T {
    Objects.checkIndex(index, size)
    return getUnchecked(index)!!
  }

  /**
   * Get the first element of this queue.
   *
   * @return The first element of this queue.
   * @throws NoSuchElementException {@inheritDoc}
   */
  fun getFirst(): T {
    if (size == 0) {
      throw NoSuchElementException()
    } else {
      return getUnchecked(ROOT_INDEX)!!
    }
  }

  /**
   * Get the last element of this queue.
   *
   * @return The last element of this queue.
   * @throws NoSuchElementException {@inheritDoc}
   */
  fun getLast(): T {
    val last = size - 1
    if (last < 0) {
      throw NoSuchElementException()
    } else {
      return getUnchecked(last)!!
    }
  }

  private fun checkDoesNotExist(element: T): T {
    return if (!indexes.containsKey(element)) {
      element
    } else {
      error("The element already exists in this queue.")
    }
  }

  /**
   * Replaces the element at the specified position in this queue with the specified element.
   * Please note that if the specified element already exists in this queue and the specified
   * index is **not** for the existing element, an exception will be thrown.
   *
   * Once this queue has been updated with the new element, the queue is adjusted as if
   * [PriorityQueueSet.adjust] has been called to move the element into its correct position.
   *
   * @param index index of the element to replace.
   * @param element element to be stored at the specified position.
   * @return The element that was replaced.
   * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size()).
   */
  operator fun set(index: Int, element: T): T {
    Objects.checkIndex(index, size)
    val existingIndex = indexOf(element)

    if (existingIndex >= ROOT_INDEX && existingIndex != index) {
      checkDoesNotExist(element)
    }

    val elements: Array<Any?> = items
    val elementIndexes: MutableMap<T, Int> = indexes

    val oldValue: T = getUnchecked(index)!!
    elementIndexes.remove(oldValue)
    elements[index] = element
    elementIndexes[element] = index

    siftUp(index, element)
    if (elements[index] === element) {
      siftDown(index, element)
    }

    return oldValue
  }

  /**
   * Returns the index of the specified element in this queue or -1 if the element does not
   * exist in this queue.
   *
   * @return The index of the specified element in this queue or -1 if the element does not
   * exist in this queue.
   */
  fun indexOf(element: T): Int {
    return indexes[element] ?: -1
  }

  /**
   * Removes the ith element from queue.
   *
   * Normally this method leaves the elements at up to i-1,
   * inclusive, untouched.  Under these circumstances, it returns
   * null.  Occasionally, in order to maintain the heap invariant,
   * it must swap a later element of the list with one earlier than
   * i.  Under these circumstances, this method returns the element
   * that was previously at the end of the list and is now at some
   * position before i. This fact is used by iterator.remove to
   * avoid missing traversing elements.
   *
   * Adapted from java.util.PriorityQueue
   *
   * @param index The index of the item to remove.
   * @return The removed item or null if the item doesn't exist.
   */
  fun removeAt(index: Int): T? {
    val elements = items
    val elementIndexes = indexes
    val end = --numberOfItems

    if (end == index) {
      // removed last element
      elementIndexes.remove(getUnchecked(index, elements))
      elements[index] = null
    } else {
      val moved: T = getUnchecked(end, elements)!!
      elementIndexes.remove(moved)
      elements[end] = null
      siftDown(index, moved)
      if (elements[index] === moved) {
        siftUp(index, moved)
        if (elements[index] !== moved) {
          return moved
        }
      }
    }
    return null
  }

  override fun heapify() {
    heapify(items, indexes, numberOfItems, comparator)
  }

  override fun sort(): List<T> {
    return heapSort()
  }

  override fun adjust(element: T) {
    val elements = items
    val index = indexOf(element)

    if (index > 0) {
      siftUp(index, element)
      if (element === getUnchecked<T>(index, elements)) {
        siftDown(index, element)
      }
    }
  }

  override operator fun contains(element: T): Boolean {
    return indexes.containsKey(element)
  }

  override fun clear() {
    modCount++
    val elements = items
    val elementIndexes = indexes
    val count = numberOfItems

    for (i in 0 until count) {
      elements[i] = null
    }

    elementIndexes.clear()
    numberOfItems = 0
  }

  @Suppress("UNCHECKED_CAST")
  override fun clone(): PriorityQueueSet<T> {
    try {
      val cloned = super.clone() as PriorityQueueSet<T>
      cloned.items = items.clone()
      cloned.indexes = indexes.clone() as HashMap<T, Int>
      return cloned
    } catch (_: CloneNotSupportedException) {
      error("The clone method is not supported!")
    }
  }

  override fun iterator(): MutableIterator<T> {
    return PriorityQueueSetIterator()
  }

  /**
   * Adapted from java.util.PriorityQueue's Itr Iterator implementation.
   */
  private inner class PriorityQueueSetIterator : MutableIterator<T> {
    /**
     * Index (into queue array) of element to be returned by
     * subsequent call to next.
     */
    private var cursor = 0

    /**
     * Index of element returned by most recent call to next,
     * unless that element came from the forgetMeNot list.
     * Set to -1 if element is deleted by a call to remove.
     */
    private var lastRet = -1

    /**
     * A queue of elements that were moved from the unvisited portion of
     * the heap into the visited portion as a result of "unlucky" element
     * removals during the iteration.  (Unlucky element removals are those
     * that require a siftup instead of a siftdown.)  We must visit all
     * the elements in this list to complete the iteration.  We do this
     * after we've completed the "normal" iteration.
     *
     * We expect that most iterations, even those involving removals,
     * will not need to store elements in this field.
     */
    private var forgetMeNot: ArrayDeque<T>? = null

    /**
     * Element returned by the most recent call to next iff that
     * element was drawn from the forgetMeNot list.
     */
    private var lastRetElt: T? = null

    /**
     * The modCount value that the iterator believes that the backing
     * Queue should have.  If this expectation is violated, the iterator
     * has detected concurrent modification.
     */
    private var expectedModCount: Int = modCount

    override fun hasNext(): Boolean {
      return cursor < size || (forgetMeNot != null && !forgetMeNot!!.isEmpty())
    }

    override fun next(): T {
      if (expectedModCount != modCount) {
        throw ConcurrentModificationException()
      }
      if (cursor < size) {
        lastRet = cursor++
        return getUnchecked(lastRet)!!
      }
      if (forgetMeNot != null) {
        lastRet = -1
        lastRetElt = forgetMeNot!!.poll()
        if (lastRetElt != null) {
          return lastRetElt as T
        }
      }
      throw NoSuchElementException()
    }

    override fun remove() {
      if (expectedModCount != modCount) {
        throw ConcurrentModificationException()
      }
      if (lastRet != -1) {
        val moved: T? = removeAt(lastRet)
        lastRet = -1
        if (moved == null) {
          cursor--
        } else {
          if (forgetMeNot == null) forgetMeNot = ArrayDeque<T>()
          forgetMeNot!!.add(moved)
        }
      } else if (lastRetElt != null) {
        remove(lastRetElt!!)
        lastRetElt = null
      } else {
        error("Unable to remove any elements.")
      }
      expectedModCount = modCount
    }
  }

  override fun spliterator(): Spliterator<T> {
    return PriorityQueueSetSpliterator(0, -1, 0)
  }

  /**
   * Adapted from java.util.PriorityQueue's Spliterator implementation
   */
  private inner class PriorityQueueSetSpliterator(
      private var index: Int, // current index, modified on advance/split
      private var fence: Int, // -1 until first use
      private var expectedModCount: Int  // initialized when fence set
  ) : Spliterator<T> {
    private fun getFence(): Int { // initialize fence to size on first use
      var hi: Int = fence
      if (hi < 0) {
        expectedModCount = modCount
        fence = size
        hi = fence
      }
      return hi
    }

    override fun trySplit(): PriorityQueueSetSpliterator? {
      val hi = getFence()
      val lo = index
      val mid = (lo + hi) ushr 1
      return if ((lo >= mid)) {
        null
      } else {
        index = mid
        PriorityQueueSetSpliterator(lo, mid, expectedModCount)
      }
    }

    override fun forEachRemaining(action: Consumer<in T>) {
      if (fence < 0) {
        fence = size
        expectedModCount = modCount
      }
      val elements: Array<Any?> = items
      val hi: Int = fence
      var element: T?
      var i = index
      index = fence

      while (i < hi) {
        element = getUnchecked(i, elements)
        if (element == null) {
          // only happen if concurrent modification
          break
        }

        action.accept(element)
        i++
      }
      if (modCount != expectedModCount) {
        throw ConcurrentModificationException()
      }
    }

    override fun tryAdvance(action: Consumer<in T>): Boolean {
      if (fence < 0) {
        fence = size
        expectedModCount = modCount
      }
      val i: Int = index
      if (i < fence) {
        index = i + 1
        val e: T? = getUnchecked(i, items)

        if (e == null || modCount != expectedModCount) {
          throw ConcurrentModificationException()
        }
        action.accept(e)
        return true
      }
      return false
    }

    override fun estimateSize(): Long {
      return (getFence() - index).toLong()
    }

    override fun characteristics(): Int {
      return Spliterator.SIZED or Spliterator.SUBSIZED or Spliterator.NONNULL
    }
  }

  override fun poll(): T? {
    val elements = items
    val elementIndexes = indexes
    val answer: T? = getUnchecked(ROOT_INDEX, elements)

    if (answer != null) {
      modCount++
      val count: Int = --numberOfItems
      val element: T? = getUnchecked(count, elements)
      elements[count] = null
      elementIndexes.remove(answer)
      if (element != null && count > ROOT_INDEX) {
        // Still have items to fix back in to a heap!
        val comp: Comparator<in T>? = comparator
        if (comp == null) {
          siftDownComparable(ROOT_INDEX, element, elements, elementIndexes, count)
        } else {
          siftDownComparator(ROOT_INDEX, element, elements, elementIndexes, count, comp)
        }
      }
    }

    return answer
  }

  override fun offer(element: T): Boolean {
    checkDoesNotExist(element)
    modCount++
    val i = size
    if (i >= items.size) {
      items = ensureCapacity(i + 1, items, size)
    }
    siftUp(i, element)
    numberOfItems = i + 1
    return true
  }

  override fun peek(): T? {
    return getUnchecked(ROOT_INDEX)
  }

  /**
   * Returns a copy of the elements in the items array.
   *
   * @return A copy of the elements in the items array.
   */
  override fun toArray(): Array<Any?> {
    return items.copyOf(numberOfItems)
  }

  /**
   * A type-safe way of obtaining a copy of the elements in the items array.
   *
   * @param array The destination array. If it is large enough it will be filled with the
   * items in this Heap.
   * @param <T> The type of the elements in the array.
   * @return A copy of the items array.
   */
  override fun <T> toArray(array: Array<T?>): Array<T?> {
    val count = this.numberOfItems
    if (array.size < count) {
      // Make a new array with the proper runtime type
      return Arrays.copyOf<T?, Any?>(items, count, array.javaClass) as Array<T?>
    }

    System.arraycopy(items, 0, array, 0, count)
    if (array.size > count) {
      array[count] = null
    }
    return array
  }

  override fun remove(element: T): Boolean {
    val index = indexOf(element)
    return if (index >= 0) {
      removeAt(index) != null
    } else {
      false
    }
  }

  override fun removeIf(filter: Predicate<in T>): Boolean {
    return bulkRemove(filter)
  }

  override fun removeAll(elements: Collection<T>): Boolean {
    return bulkRemove { e -> elements.contains(e) }
  }

  override fun retainAll(elements: Collection<T>): Boolean {
    return bulkRemove { e -> !elements.contains(e) }
  }

  /**
   * Performs a multi-pass removal. All elements that pass the filter are marked
   * and then removed. Indexes for the elements that are being removed are deleted
   * during the first pass and then the elements are removed during the second pass.
   */
  private fun bulkRemove(filter: Predicate<in T>): Boolean {
    val expectedModCount = ++modCount
    val elements: Array<Any?> = items
    val elementIndexes: MutableMap<T, Int> = indexes
    val end = size

    // skip past any elements at the start that are staying
    var i = 0
    while (i < end && !filter.test(getUnchecked(i, elements)!!)) {
      i++
    }

    if (i >= end) {
      if (modCount != expectedModCount) {
        throw ConcurrentModificationException()
      }
      return false // keeping everything
    }

    val start = i
    elementIndexes.remove(getUnchecked(start, elements)!!)
    val toDelete = BitSet(end - start)
    toDelete[0] = true

    i = start + 1
    while (i < end) {
      val elem: T = getUnchecked(i, elements)!!
      if (filter.test(elem)) {
        elementIndexes.remove(elem) // delete the index
        toDelete[i - start] = true // mark element for removal
      }
      i++
    }
    if (modCount != expectedModCount) {
      throw ConcurrentModificationException()
    }
    var j = start
    i = start
    while (i < end) {
      if (!toDelete[i - start]) {
        val elem: T = getUnchecked(i, elements)!! // element to move
        elementIndexes[elem] = j // update index for the element with new location
        elements[j++] = elem // perform actual move
      }
      i++
    }
    numberOfItems = j
    // Null out the end of the items array
    i = j
    while (i < end) {
      elements[i] = null
      i++
    }
    heapify() // ensure to maintain the heap property
    return true
  }

  override fun forEach(action: Consumer<in T>) {
    Objects.requireNonNull(action)
    val expectedModCount = modCount
    val elements: Array<Any?> = items
    val count = size
    for (i in 0 until count) {
      action.accept(getUnchecked(i, elements)!!)
    }
    if (expectedModCount != modCount) {
      throw ConcurrentModificationException()
    }
  }

  /**
   * Saves this queue to a stream (that is, serializes it).
   *
   * @param s the stream
   * @throws java.io.IOException if an I/O error occurs
   * @serialData The length of the array backing the instance is
   * emitted (int), followed by all of its elements
   * (each an `Object`) in the proper order.
   */
  @Serial
  @Throws(IOException::class)
  private fun writeObject(s: ObjectOutputStream) {
    // Write out non transient properties.
    s.defaultWriteObject()

    // Write out the data.
    val elements: Array<Any?> = items
    val count = size
    for (i in 0 until count) {
      s.writeObject(getUnchecked(i, elements))
    }
  }

  /**
   * Reconstitutes the `PriorityQueueSet` instance from a stream
   * (that is, deserializes it).
   *
   * @param s the stream
   * @throws ClassNotFoundException if the class of a serialized object
   * could not be found
   * @throws java.io.IOException if an I/O error occurs
   */
  @Serial
  @Throws(IOException::class, ClassNotFoundException::class)
  private fun readObject(s: ObjectInputStream) {
    // Read in non transient properties.
    s.defaultReadObject()

    items = arrayOfNulls(max(size.toDouble(), 1.0).toInt())
    indexes = HashMap(items.size)

    val elements: Array<Any?> = items
    val elementIndexes: MutableMap<T, Int> = indexes

    // Read in all elements.
    val count = size
    for (i in 0 until count) {
      elements[i] = s.readObject()
      elementIndexes[getUnchecked(i)!!] = i
    }

    heapify()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is PriorityQueueSet<*>) return false

    if (numberOfItems != other.numberOfItems) return false
    if (!items.contentEquals(other.items)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = numberOfItems
    result = 31 * result + items.contentHashCode()
    return result
  }

  override fun toString(): String {
    return "PriorityQueueSet(numberOfItems=$numberOfItems, " +
        "items=${items.contentToString()}, indexes=$indexes, modCount=$modCount)"
  }

}
