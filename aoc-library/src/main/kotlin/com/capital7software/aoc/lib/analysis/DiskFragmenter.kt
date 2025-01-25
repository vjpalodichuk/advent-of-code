package com.capital7software.aoc.lib.analysis

import com.capital7software.aoc.lib.collection.PriorityQueue

/**
 * Another push of the button leaves you in the familiar hallways of some friendly
 * [amphipods](https://adventofcode.com/2021/day/23)! Good thing you each somehow got your own
 * personal mini submarine. The Historians jet away in search of the Chief, mostly by driving
 * directly into walls.
 *
 * While The Historians quickly figure out how to pilot these things, you notice an amphipod in
 * the corner struggling with his computer. He's trying to make more contiguous free space by
 * compacting all the files, but his program isn't working; you offer to help.
 *
 * He shows you the **disk map** (your puzzle input) he's already generated. For example:
 *
 * ```
 * 2333133121414131402
 * ```
 *
 * The disk map uses a dense format to represent the layout of **files** and **free space** on
 * the disk. The digits alternate between indicating the length of a file and the length of
 * free space.
 *
 * So, a disk map like ```12345``` would represent a one-block file, two blocks of free space,
 * a three-block file, four blocks of free space, and then a five-block file. A disk map like
 * ```90909``` would represent three nine-block files in a row (with no free space between them).
 *
 * Each file on disk also has an **ID number** based on the order of the files as they appear
 * **before** they are rearranged, starting with ID ```0```. So, the disk map ```12345``` has
 * three files: a one-block file with ID ```0```, a three-block file with ID ```1```, and a
 * five-block file with ID ```2```. Using one character for each block where digits are the file
 * ID and ```.``` is free space, the disk map ```12345``` represents these individual blocks:
 *
 * ```
 * 0..111....22222
 * ```
 *
 * The first example above, 2333133121414131402, represents these individual blocks:
 *
 * ```
 * 00...111...2...333.44.5555.6666.777.888899
 * ```
 *
 * @param input [String] that represents the current drive-map.
 */
class DiskFragmenter(input: String) {
  private val diskMap = input.toCharArray()

  companion object {
    private const val OFFSET = 0x30 // ASCII 0

    private fun buildLayout(input: CharArray): Pair<MutableList<Block>, PriorityQueue<Block>> {
      var currentId = 0L

      val layout = mutableListOf<Block>()
      val freeBlocks = PriorityQueue<Block>(100, compareBy { it.index })

      input.forEachIndexed { index, c ->
        // Even index represents a file, odd free-space
        val length = c.code - OFFSET

        if (index % 2 == 0) {
          for (i in 0 until length) {
            layout.add(Block(layout.size, currentId, true))
          }
          currentId++
        } else {
          for (i in 0 until length) {
            val block = Block(layout.size, -1, false)
            layout.add(block)
            freeBlocks.add(block)
          }
        }
      }
      return Pair(layout, freeBlocks)
    }

    private fun buildFatLayout(input: CharArray): Pair<MutableList<FatBlock>, PriorityQueue<FatBlock>> {
      var currentId = 0L

      val files = mutableListOf<FatBlock>()
      val freeBlocks = PriorityQueue<FatBlock>(100, compareBy { it.start })
      var lastIndex = 0

      input.forEachIndexed { index, c ->
        // Even index represents a file, odd free-space
        val length = c.code - OFFSET
        val start = lastIndex
        lastIndex += length

        if (length > 0) {
          if (index % 2 == 0) {
            files.add(FatBlock(currentId, true, start, length))
            currentId++
          } else {
            freeBlocks.add(FatBlock(-1, false, start, length))
          }
        }
      }
      return Pair(files, freeBlocks)
    }

    private fun swapBlocks(blockA: Block, blockB: Block, blocks: MutableList<Block>) {
      val newA = blockA.copy(index = blockB.index)
      val newB = blockB.copy(index = blockA.index)

      blocks[newA.index] = newA
      blocks[newB.index] = newB
    }

    private fun swapFatBlocks(
        dataBlock: FatBlock,
        freeBlock: FatBlock,
    ): Pair<FatBlock, List<FatBlock>> {
      val newData = dataBlock.copy(start = freeBlock.start)
      val newFree = mutableListOf(freeBlock.copy(start = dataBlock.start, length = dataBlock.length))

      // Do we have to insert a FatBlock for some leftover free-space?
      if (freeBlock.length >= dataBlock.length) {
        newFree.add(FatBlock(
            -1,
            false,
            freeBlock.start + dataBlock.length,
            freeBlock.length - dataBlock.length
        ))
      }

      return Pair(newData, newFree)
    }
  }

  private data class Block(val index: Int, val id: Long, val data: Boolean) {
    val checkSum: Long = if (data) index * id else 0L
  }

  private data class FatBlock(val id: Long, val data: Boolean, val start: Int, val length: Int) {
    val end: Int = start + length - 1

    val checkSum: Long = if (data) {
      var sum = 0L
      for (i in start..end) {
        sum += (i * id)
      }
      sum
    } else {
      0L
    }
  }

  /**
   * Calculates and returns the checksum calculated after defragmentation is complete.
   *
   * The amphipod would like to **move file blocks one at a time** from the end of the disk to the
   * leftmost free space block (until there are no gaps remaining between file blocks). For the
   * disk map ```12345```, the process looks like this:
   *
   * ```
   * 0..111....22222
   * 02.111....2222.
   * 022111....222..
   * 0221112...22...
   * 02211122..2....
   * 022111222......
   * ```
   *
   * The first example requires a few more steps:
   *
   * ```
   * 00...111...2...333.44.5555.6666.777.888899
   * 009..111...2...333.44.5555.6666.777.88889.
   * 0099.111...2...333.44.5555.6666.777.8888..
   * 00998111...2...333.44.5555.6666.777.888...
   * 009981118..2...333.44.5555.6666.777.88....
   * 0099811188.2...333.44.5555.6666.777.8.....
   * 009981118882...333.44.5555.6666.777.......
   * 0099811188827..333.44.5555.6666.77........
   * 00998111888277.333.44.5555.6666.7.........
   * 009981118882777333.44.5555.6666...........
   * 009981118882777333644.5555.666............
   * 00998111888277733364465555.66.............
   * 0099811188827773336446555566..............
   * ```
   *
   * The final step of this file-compacting process is to update the **filesystem checksum**. To
   * calculate the checksum, add up the result of multiplying each of these blocks' position with
   * the file ID number it contains. The leftmost block is in position ```0```. If a block contains
   * free space, skip it instead.
   *
   * Continuing the first example, the first few blocks' position multiplied by its file ID number
   * are ```0 * 0 = 0```, ```1 * 0 = 0```, ```2 * 9 = 18```, ```3 * 9 = 27```, ```4 * 8 = 32```,
   * and so on. In this example, the checksum is the sum of these, **```1928```**.
   * @return The checksum calculated after defragmentation is complete.
   */
  fun compactDefragmentation(): Long {
    val (layout, freeBlocks) = buildLayout(diskMap)
    var currentIndex = layout.size - 1

    while (freeBlocks.isNotEmpty() && freeBlocks.peek().index < currentIndex) {
      val nextFree = freeBlocks.poll()
      val nextData = if (layout[currentIndex].data) {
        layout[currentIndex]
      } else {
        while (!layout[currentIndex].data && currentIndex > nextFree.index) {
          currentIndex--
        }
        if (currentIndex > nextFree.index) {
          layout[currentIndex]
        } else {
          null
        }
      }
      if (nextData != null) {
        swapBlocks(nextData, nextFree, layout)
        // Must ensure the available free space reconsiders this free block since it moved.
        freeBlocks.add(layout[currentIndex])
      }
      currentIndex--
    }

    return layout.sumOf { it.checkSum }
  }

  /**
   * Upon completion, two things immediately become clear. First, the disk definitely has a lot more
   * contiguous free space, just like the amphipod hoped. Second, the computer is running much more
   * slowly! Maybe introducing all of that
   * [file system fragmentation](https://en.wikipedia.org/wiki/File_system_fragmentation)
   * was a bad idea?
   *
   * The eager amphipod already has a new plan: rather than move individual blocks, he'd like to
   * try compacting the files on his disk by moving **whole files** instead.
   *
   * This time, attempt to move whole files to the leftmost span of free space blocks that could fit
   * the file. Attempt to move each file exactly once in order of **decreasing file ID number**
   * starting with the file with the highest file ID number. If there is no span of free space to
   * the left of a file that is large enough to fit the file, the file does not move.
   *
   * The first example from above now proceeds differently:
   *
   * ```
   * 00...111...2...333.44.5555.6666.777.888899
   * 0099.111...2...333.44.5555.6666.777.8888..
   * 0099.1117772...333.44.5555.6666.....8888..
   * 0099.111777244.333....5555.6666.....8888..
   * 00992111777.44.333....5555.6666.....8888..
   * ```
   *
   * The process of updating the filesystem checksum is the same; now, this example's checksum would
   * be **```2858```**.
   *
   * @return The checksum after performing a full-file defragmentation.
   */
  fun fullFileDefragmentation(): Long {
    val (files, freeBlocks) = buildFatLayout(diskMap)
    val newFiles = mutableListOf<FatBlock>()

    while (files.isNotEmpty()) {
      val file = files.removeLast()
      val nextFree: FatBlock? = findNextFreeThatFits(file, freeBlocks)

      if (nextFree != null) {
        val (newFile, newFree) = swapFatBlocks(file, nextFree)
        newFiles.add(newFile)
        freeBlocks.addAll(newFree)
      } else {
        newFiles.add(file)
      }
    }

    return newFiles.sumOf { it.checkSum }
  }

  private fun findNextFreeThatFits(file: FatBlock, freeBlocks: PriorityQueue<FatBlock>): FatBlock? {
    var done = false
    var result: FatBlock? = null
    val seen = mutableListOf<FatBlock>()

    while (!done && freeBlocks.isNotEmpty()) {
      val nextFree = freeBlocks.poll()

      if (nextFree.length >= file.length && nextFree.start < file.start) {
        done = true
        result = nextFree
      } else if (freeBlocks.isNotEmpty() && nextFree.end + 1 == freeBlocks.peek().start) {
        val newFree = nextFree.copy(length = nextFree.length + freeBlocks.poll().length)
        freeBlocks.add(newFree)
      } else {
        seen.add(nextFree)
      }
    }
    // Add back the seen but not used free space.
    freeBlocks.addAll(seen)

    return result
  }
}
