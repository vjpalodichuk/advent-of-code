package com.capital7software.aoc.lib.grid

import com.capital7software.aoc.lib.geometry.Direction
import com.capital7software.aoc.lib.geometry.Point2D
import com.capital7software.aoc.lib.util.Triple
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlin.collections.set
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * The types of objects found in the warehouse.
 *
 * @param name The name of the object.
 * @param symbol The character representation of the object.
 * @param moveable Is the object a moveable object?
 * @param empty Is the object just empty such that another object can move on to it?
 * @param box Is the object a box that can be moved around the warehouse?
 */
sealed class WarehouseObject(
    val name: String,
    val symbol: Char,
    val moveable: Boolean,
    val empty: Boolean,
    val box: Boolean
) {
  /**
   * A warehouse wall. All walls cannot be moved and are considered to be occupied.
   */
  class Wall : WarehouseObject("WALL", '#', false, false, false)

  /**
   * A box can be moved around the warehouse. A box adjacent to another [moveable] object is capable
   * of moving all adjacent [moveable] objects provided the last object in the chain is able to move
   * to an [empty] space.
   */
  class Box : WarehouseObject("BOX", 'O', true, false, true)

  /**
   * A box can be moved around the warehouse. A box adjacent to another [moveable] object is capable
   * of moving all adjacent [moveable] objects provided the last object in the chain is able to move
   * to an [empty] space.
   */
  class BoxLeft : WarehouseObject("BOX_LEFT", '[', true, false, true) {
    @SuppressFBWarnings
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false
      return true
    }

    override fun hashCode(): Int {
      return javaClass.hashCode()
    }
  }

  /**
   * A box can be moved around the warehouse. A box adjacent to another [moveable] object is capable
   * of moving all adjacent [moveable] objects provided the last object in the chain is able to move
   * to an [empty] space.
   */
  class BoxRight : WarehouseObject("BOX_RIGHT", ']', true, false, true) {
    @SuppressFBWarnings
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false
      return true
    }

    override fun hashCode(): Int {
      return javaClass.hashCode()
    }
  }

  /**
   * Represents plain old empty space. Any other object can move through this object.
   */
  class Empty : WarehouseObject("EMPTY", '.', true, true, false)

  /**
   * A robot is capable of accepting commands to move about the warehouse. If during a move
   * operation a robot encounters a [moveable] object, it will attempt to move the object during
   * the execution of the command. If the move is successful, the robot advances; otherwise, it
   * stays in the same position and will continue with the next command to execute.
   */
  class Robot : WarehouseObject("ROBOT", '@', true, false, false)

  /**
   * String representation of this object
   */
  override fun toString(): String {
    return symbol.toString()
  }


}

/**
 * An extension function to convert a [Char] into a [WarehouseObject]
 *
 * @return An instance of the corresponding [WarehouseObject]
 */
fun Char.toWarehouseObject(): WarehouseObject = when (this) {
  '#' -> WarehouseObject.Wall()
  'O' -> WarehouseObject.Box()
  '.' -> WarehouseObject.Empty()
  '@' -> WarehouseObject.Robot()
  '[' -> WarehouseObject.BoxLeft()
  ']' -> WarehouseObject.BoxRight()
  else -> error("Invalid warehouse object: $this")
}

/**
 * The types of commands that a [WarehouseObject.Robot] can execute.
 *
 * @param type The character representation of the command.
 * @param direction The [Direction] that the command moves in.
 */
enum class RobotCommand(val type: Char, val direction: Direction) {
  /**
   * Moves the robot up (north).
   */
  UP('^', Direction.NORTH),

  /**
   * Moves the robot down (south).
   */
  DOWN('v', Direction.SOUTH),

  /**
   * Moves the robot left (west).
   */
  LEFT('<', Direction.WEST),

  /**
   * Moves the robot right (east).
   */
  RIGHT('>', Direction.EAST)
}

/**
 * An extension function to convert a [Char] into a [RobotCommand]
 *
 * @return An instance of the corresponding [RobotCommand]
 */
private fun Char.toRobotCommand(): RobotCommand = when (this) {
  '^' -> RobotCommand.UP
  'v' -> RobotCommand.DOWN
  '<' -> RobotCommand.LEFT
  '>' -> RobotCommand.RIGHT
  else -> error("Invalid robot command: $this")
}

/**
 * The warehouse that the [WarehouseObject.Robot] patrols.
 *
 * @param grid The starting layout of all objects in this [Warehouse].
 * @param start The starting position of the [WarehouseObject.Robot].
 * @param commands The [List] of [RobotCommand]s to have the [WarehouseObject.Robot] execute.
 * @param large If true, then the number of columns is double and objects, except for the
 * robot, double in width.
 */
@SuppressFBWarnings
class Warehouse private constructor(
    val grid: Grid2D<WarehouseObject>,
    val start: Point2D<Int>,
    val commands: List<RobotCommand>,
    val large: Boolean = false
) {
  /**
   * Contains the [load] static method to create this [Warehouse].
   */
  companion object {
    private val logger: Logger = LoggerFactory.getLogger(Warehouse::class.java)

    /**
     * Parses the provided [inputs] to build and return a new [Warehouse] instance.
     *
     * @param inputs The [List] of [String]s to parse into [WarehouseObject]s.
     * @param large If true, then the sizes of objects, except the [WarehouseObject.Robot], are
     * doubled.
     * @return A new Warehouse built from the specified inputs.
     */
    fun load(inputs: List<String>, large: Boolean = false): Warehouse {
      val glyphs = hashSetOf('^', 'v', '<', '>')
      val gridList = mutableListOf<List<WarehouseObject>>()
      val commands = mutableListOf<RobotCommand>()
      val width = if (large) inputs[0].length * 2 else inputs[0].length
      var start = Point2D(0, 0)

      inputs.forEachIndexed { index, input ->
        if (input.startsWith('#')) {
          gridList.add(input.flatMapIndexed { index2, ch ->
            val obj = ch.toWarehouseObject()
            if (obj is WarehouseObject.Robot) {
              start = if (large) {
                Point2D(index2 * 2, index)
              } else {
                Point2D(index2, index)
              }
            }
            return@flatMapIndexed if (large) {
              when (obj) {
                is WarehouseObject.Box -> {
                  listOf(WarehouseObject.BoxLeft(), WarehouseObject.BoxRight())
                }

                is WarehouseObject.Empty -> {
                  listOf(obj, WarehouseObject.Empty())
                }

                is WarehouseObject.Robot -> {
                  listOf(obj, WarehouseObject.Empty())
                }

                is WarehouseObject.Wall -> {
                  listOf(obj, WarehouseObject.Wall())
                }

                else -> error("Invalid robot command: $this")
              }
            } else {
              listOf(obj)
            }
          })
        } else if (input.isNotBlank() && glyphs.contains(input.first())) {
          commands.addAll(input.map { glyph -> glyph.toRobotCommand() })
        }
      }

      val height = gridList.size
      val grid: Grid2D<WarehouseObject> = Grid2D(
          width,
          height,
          gridList.flatten().toTypedArray()
      )

      return Warehouse(grid, start, commands, large)
    }
  }

  /**
   * Returns a [List] of [Point2D]s that contain the position of every box.
   *
   * @return The list of all current box positions.
   */
  fun boxes(): List<Point2D<Int>> = positions(WarehouseObject.Box()) + positions(WarehouseObject.BoxLeft())

  private fun <T : WarehouseObject> positions(obj: T): List<Point2D<Int>> {
    val result = mutableListOf<Point2D<Int>>()

    for (y in 0 until grid.rows()) {
      for (x in 0 until grid.columns()) {
        if (grid[x, y].javaClass == obj.javaClass) {
          result.add(Point2D(x, y))
        }
      }
    }
    return result
  }

  /**
   * Runs all of the [RobotCommand]s.
   */
  fun execute() {
    var current = start

    if (logger.isDebugEnabled) {
      logger.debug("Initial State:\n${toString()}\n")
    }

    commands.forEachIndexed { index, command ->
      if (logger.isDebugEnabled) {
        logger.debug("Executing RobotCommand $index: $command")
      }
      current = execute(
          command,
          current
      )
      if (logger.isDebugEnabled) {
        logger.debug("Current State:\n${toString()}\n")
      }
    }
  }

  private fun execute(
      command: RobotCommand,
      position: Point2D<Int>
  ): Point2D<Int> {
    val point = position.pointInDirection(command.direction)
    val neighbor = if (grid.isOnGrid(point)) grid[point] else null

    return if (neighbor == null || !neighbor.moveable) {
      position
    } else {
      moveRobot(position, point, neighbor, command.direction)
    }
  }

  private fun moveRobot(
      from: Point2D<Int>,
      to: Point2D<Int>,
      neighbor: WarehouseObject,
      direction: Direction
  ): Point2D<Int> = when (neighbor) {
    is WarehouseObject.Wall -> from
    is WarehouseObject.Robot -> from
    is WarehouseObject.Empty -> {
      grid.swap(from, to)
      to
    }

    is WarehouseObject.Box -> {
      moveBoxes(to, direction)
      if (grid[to] is WarehouseObject.Empty) {
        grid.swap(from, to)
        to
      } else {
        from
      }
    }

    is WarehouseObject.BoxLeft, is WarehouseObject.BoxRight -> {
      moveLargeBoxes(to, direction)
      if (grid[to] is WarehouseObject.Empty) {
        grid.swap(from, to)
        to
      } else {
        from
      }
    }
  }

    private fun moveLargeBoxes(position: Point2D<Int>, direction: Direction) {
      val obj = grid[position]

      if (obj !is WarehouseObject.BoxLeft && obj !is WarehouseObject.BoxRight) return

      when (direction) {
        Direction.WEST, Direction.EAST -> {
          val point = position.pointInDirection(direction)

          performBoxMoves(direction, grid[point], position, point)
        }

        Direction.NORTH, Direction.SOUTH -> {
          val box = getLargeBox(obj, position)
          val boxesToMove = collectLargeBoxesToMove(box, direction)

          if (boxesToMove != null) {
            performLargeBoxMoves(boxesToMove, direction)
          }
        }

        else -> error("Invalid Direction: $direction")
      }
    }

    private fun collectLargeBoxesToMove(
        start: LargeBox,
        direction: Direction
    ): Set<LargeBox>? {
      val queue = ArrayDeque<LargeBox>()
      val boxesToMove = linkedSetOf<LargeBox>()

      queue.add(start)

      while (queue.isNotEmpty()) {
        val current = queue.removeFirst()

        if (!boxesToMove.add(current)) {
          continue
        }

        val nextPositions = listOf(
            current.left.pointInDirection(direction),
            current.right.pointInDirection(direction)
        )

        nextPositions.forEach { nextPosition ->
          if (!grid.isOnGrid(nextPosition)) {
            return null
          }

          when (val next = grid[nextPosition]) {
            is WarehouseObject.Wall -> return null
            is WarehouseObject.Empty -> Unit
            is WarehouseObject.BoxLeft, is WarehouseObject.BoxRight -> {
              queue.add(getLargeBox(next, nextPosition))
            }
            else -> {
              if (!next.moveable) {
                return null
              }
            }
          }
        }
      }

      return boxesToMove
    }

    private fun performLargeBoxMoves(
        boxesToMove: Set<LargeBox>,
        direction: Direction
    ) {
      val orderedBoxes = when (direction) {
        Direction.NORTH -> boxesToMove.sortedBy { it.left.y() }
        Direction.SOUTH -> boxesToMove.sortedByDescending { it.left.y() }
        else -> error("Invalid vertical direction: $direction")
      }

      orderedBoxes.forEach { box ->
        grid[box.left] = WarehouseObject.Empty()
        grid[box.right] = WarehouseObject.Empty()
      }

      orderedBoxes.forEach { box ->
        val newLeft = box.left.pointInDirection(direction)
        val newRight = box.right.pointInDirection(direction)

        grid[newLeft] = WarehouseObject.BoxLeft()
        grid[newRight] = WarehouseObject.BoxRight()
      }
    }

  private fun moveBoxes(position: Point2D<Int>, direction: Direction) {
    if (grid[position] !is WarehouseObject.Box) return

    var point = position.pointInDirection(direction)

    if (!grid.isOnGrid(point)) return

    while (grid.isOnGrid(point) && grid[point] !is WarehouseObject.Wall) {
      if (grid[point] is WarehouseObject.Empty) {
        grid.swap(position, point)
        break
      } else {
        point = point.pointInDirection(direction)
      }
    }
  }

  private data class LargeBox(val left: Point2D<Int>, val right: Point2D<Int>)

  private fun getLargeBox(
      current: WarehouseObject,
      currentPos: Point2D<Int>
  ): LargeBox  =
    when (current) {
      is WarehouseObject.BoxLeft -> {
        val point = currentPos.pointInDirection(Direction.EAST)
        LargeBox(currentPos, point)
      }

      is WarehouseObject.BoxRight -> {
        val point = currentPos.pointInDirection(Direction.WEST)
        LargeBox(point, currentPos)
      }

      else -> error("getLargeBox: Unexpected current")
    }


  private fun performBoxMoves(
      direction: Direction,
      src: WarehouseObject,
      initial: Point2D<Int>,
      pos: Point2D<Int>
  ) {
    var obj: WarehouseObject = src
    var point: Point2D<Int> = pos
    while (grid.isOnGrid(point) && obj !is WarehouseObject.Wall) {
      if (obj is WarehouseObject.Empty) {
        val row = grid.getRow(initial.y())
        val sub = if (direction == Direction.EAST) {
          row.subList(initial.x(), point.x()).toList()
        } else {
          row.subList(point.x() + 1, initial.x() + 1).toList()
        }
        row[initial.x()] = row[point.x()]
        if (direction == Direction.EAST) {
          sub.forEachIndexed { index, item ->
            row[initial.x() + index + 1] = item
          }
        } else {
          sub.forEachIndexed { index, item ->
            row[point.x() + index] = item
          }
        }
        grid.setRow(initial.y(), row)
        break
      } else {
        point = point.pointInDirection(direction)

        if (!grid.isOnGrid(point)) break

        obj = grid[point]
      }
    }
  }

  private fun MutableMap<Int, Pair<Point2D<Int>, Point2D<Int>>>.toBoxMoves() =
    map { (key, value) -> Triple(key, value.first, value.second) }

  private fun LargeBox.canMoveIn(direction: Direction): Boolean {
    val nextLeftPos = left.pointInDirection(direction)
    val nextRightPos = right.pointInDirection(direction)
    val nextLeft = grid[nextLeftPos]!!
    val nextRight = grid[nextRightPos]!!

    return nextLeft.moveable && nextRight.moveable
  }

  private fun LargeBox.neighbors(direction: Direction): List<LargeBox> {
    val nextLeftPos = left.pointInDirection(direction)
    val nextRightPos = right.pointInDirection(direction)
    val nextLeft = grid[nextLeftPos]!!
    val nextRight = grid[nextRightPos]!!

    return if (!nextLeft.moveable || !nextRight.moveable || (nextLeft.empty && nextRight.empty)) {
      emptyList()
    } else {
      when (nextLeft) {
        is WarehouseObject.BoxLeft -> listOf(LargeBox(nextLeftPos, nextRightPos))
        is WarehouseObject.BoxRight -> {
          when (nextRight) {
            is WarehouseObject.BoxLeft -> listOf(
                getLargeBox(nextLeft, nextLeftPos),
                getLargeBox(nextRight, nextRightPos),
            )
            is WarehouseObject.Empty -> listOf(getLargeBox(nextLeft, nextLeftPos))
            else -> error("Not an expected WarehouseObject.BoxRight: $nextRight")
          }
        }
        is WarehouseObject.Empty -> {
          when (nextRight) {
            is WarehouseObject.BoxLeft -> listOf(
                getLargeBox(nextRight, nextRightPos),
            )
            else -> error("Not an expected WarehouseObject.BoxRight: $nextRight - $nextRightPos")
          }
        }
        else -> error("Not a WarehouseObject.BoxLeft: $nextLeft - $nextLeftPos")
      }
    }
  }

  private fun updateColumnMap(
      box: LargeBox,
      columnMap: MutableMap<Int, Pair<Point2D<Int>, Point2D<Int>>>,
      direction: Direction? = null,
  ) {
    if (direction != null) {
      columnMap[box.left.x()] = Pair(
          columnMap[box.left.x()]?.first ?: box.left,
          box.left.pointInDirection(direction)
      )
      columnMap[box.right.x()] = Pair(
          columnMap[box.right.x()]?.first ?: box.right,
          box.right.pointInDirection(direction)
      )
    } else {
      columnMap[box.left.x()] = Pair(
          columnMap[box.left.x()]?.first ?: box.left,
          box.left
      )
      columnMap[box.right.x()] = Pair(
          columnMap[box.right.x()]?.first ?: box.right,
          box.right
      )
    }
  }

  private fun calculateLargeBoxMovesRecursive(
      queue: List<LargeBox>,
      direction: Direction,
      columnMap: MutableMap<Int, Pair<Point2D<Int>, Point2D<Int>>>,
      visited: Set<LargeBox>
  ): List<Triple<Int, Point2D<Int>, Point2D<Int>>> {
    if (queue.isEmpty()) return columnMap.toBoxMoves()

    val current = queue.first()
    val rest = queue.drop(1)

    if (!current.canMoveIn(direction)) {
      columnMap[current.left.x()] = Pair(current.left, current.left)
      columnMap[current.right.x()] = Pair(current.right, current.right)
      return columnMap.toBoxMoves()
    }

    val neighbors = current.neighbors(direction)

    updateColumnMap(current, columnMap, direction)

    val newVisited = visited + current

    val next = neighbors.filter { it !in newVisited }

    return calculateLargeBoxMovesRecursive(rest + next, direction, columnMap, newVisited)
  }

  /**
   * String representation of this object
   */
  override fun toString(): String {
    val builder = StringBuilder()

    for (y in 0 until grid.rows()) {
      if (builder.isNotEmpty()) builder.appendLine()

      for (x in 0 until grid.columns()) {
        builder.append(grid[x, y].toString())
      }
    }

    builder.appendLine()

    return builder.toString()
  }

}

private fun Point2D<Int>.toGoodsPositioningSystem(): Long {
  val scaler = 100L
  return scaler * y() + x()
}

/**
 * You appear back inside your own mini submarine! Each Historian drives their mini submarine in a
 * different direction; maybe the Chief has his own submarine down here somewhere as well?
 *
 * You look up to see a vast school of [lanternfish](https://adventofcode.com/2021/day/6) swimming
 * past you. On closer inspection, they seem quite anxious, so you drive your mini submarine over
 * to see if you can help.
 *
 * Because lanternfish populations grow rapidly, they need a lot of food, and that food needs to be
 * stored somewhere. That's why these lanternfish have built elaborate warehouse complexes operated
 * by robots!
 *
 * These lanternfish seem so anxious because they have lost control of the robot that operates one
 * of their most important warehouses! It is currently running amok, pushing around boxes in the
 * warehouse with no regard for lanternfish logistics **or** lanternfish inventory management
 * strategies.
 *
 * Right now, none of the lanternfish are brave enough to swim up to an unpredictable robot so they
 * could shut it off. However, if you could anticipate the robot's movements, maybe they could find
 * a safe option.
 *
 * @param warehouse The [Warehouse] that is causing us woes :-(
 */
class WarehouseWoes private constructor(val warehouse: Warehouse) {
  /**
   * Contains the [load] method to build and construct a new [WarehouseWoes] instance.
   */
  companion object {
    /**
     * Parses the specified [inputs] into a new [WarehouseWoes] instance.
     *
     * @param inputs The [List] of [String]s to parse into [WarehouseObject]s.
     * @return A new WarehouseWoes built from the specified inputs.
     */
    fun load(inputs: List<String>, large: Boolean = false): WarehouseWoes =
        WarehouseWoes(Warehouse.load(inputs, large))
  }

  /**
   * Executes the Robot's commands
   */
  fun execute() = warehouse.execute()

  /**
   * String representation of this object
   */
  override fun toString(): String {
    return warehouse.toString()
  }

  /**
   * The sum of all Goods Positioning System coordinates from all boxes.
   */
  val gpsSum: Long by lazy { warehouse.boxes().sumOf { it.toGoodsPositioningSystem() } }

}
