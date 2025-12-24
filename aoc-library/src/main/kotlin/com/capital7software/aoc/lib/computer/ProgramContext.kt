package com.capital7software.aoc.lib.computer

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.Objects
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Contains the current state of the executing program.
 */
interface ProgramContext {
  /**
   * A special register that an [Instruction] may use to read from, write to, or both. Anytime
   * output is written to, the instruction may call an interrupt handler to handle the output.
   */
  var output: Long

  /**
   * A special register that an [Instruction] may use to read from, write to, or both.
   * Typically, this will be the frequency of the last sound played. Depending on the
   * instruction, an interrupt handler may be called to process the sound or recover
   * the last sound frequency played depending on which [Instruction] has just
   * been executed.
   */
  var soundOutput: Long

  /**
   * The number of [Instruction] that have been loaded in to this context. The [get] and [set]
   * operation's index parameter must be in the range of 0 <= index < [instructionCount].
   */
  val instructionCount: Int

  /**
   * The names of the [Register]s the processor supports. These are the keys to the get / set
   * register operations.
   */
  val keys: List<String>

  /**
   * This handler may be called by any [Instruction] that interacts with [output].
   * The caller should pass the most recent output to the handler as its parameter but, it is
   * up to the specific instruction on what is actually passed.
   */
  val outputHandler: (output: Long) -> Unit

  /**
   * This handler may be called by any [Instruction] that interacts with [soundOutput].
   * The caller should pass the most recent [soundOutput] to the handler as its parameter but,
   * is up to the specific instruction on what is actually passed.
   */
  val soundHandler: (output: Long) -> Unit

  /**
   * The index of the [Instruction] that is about to be executed or is currently executing.
   */
  val index: Int

  /**
   * Returns true if this thread context is blocked waiting for a message.
   */
  val blocked: Boolean

  /**
   * Returns the number of times a message was sent through this context.
   *
   * @return The number of times a message was sent through this context.
   */
  fun sendCount(): Int

  /**
   * Returns the number of times a message was received and processed.
   *
   * @return The number of times a message was received and processed.
   */
  fun receiveCount(): Int

  /**
   * Places the specified value in the message queue of the linked context.
   * Returns as soon as the message has been placed in the linked context's queue.
   *
   * If there is no linked context, then false is returned.
   *
   * @param value The value to place in the message queue of the linked context.
   * @return True unless there is no linked context.
   */
  fun send(value: Long): Boolean

  /**
   * Consumes the oldest message waiting in the message queue and returns it. If there
   * are no messages in the queue, will not return until a message is received.
   */
  fun receive(): Long?

  /**
   * Returns the value of the specified register. If the specified [Register] name doesn't
   * exist, an [IllegalStateException] is thrown.
   *
   * @param register The name of the [Register] to retrieve the value from.
   * @return The value of the specified register.
   */
  operator fun get(register: String): Long

  /**
   * Returns the [Instruction] at the specified [index]. An [IndexOutOfBoundsException] is thrown
   * if the specified index is outside the range of 0 <= index < [instructionCount].
   *
   * @return The [Instruction] at the specified [index].
   */
  operator fun get(index: Int): Instruction

  /**
   * Sets the specified [Register] name to the specified value. If the specified [Register]
   * name doesn't exist, an [IllegalStateException] is thrown.
   *
   * @param register The name of the register to set.
   * @param value The value to set the register to.
   */
  operator fun set(register: String, value: Long)

  /**
   * Replaces the existing [Instruction] at the specified [index] with the specified [Instruction].
   * An [IndexOutOfBoundsException] is thrown if the specified index is outside the range
   * of 0 <= index < [instructionCount].
   *
   * @param index The index of the [Instruction] that is going to be replaced.
   * @param instruction The [Instruction] to replace the existing instruction with.
   */
  operator fun set(index: Int, instruction: Instruction)

  /**
   * Invokes the next instruction and sets the instruction pointer to the next instruction
   * to execute. If the instruction pointer is outside the range of valid
   * indices, nothing happens.
   *
   * @return The index of the next instruction to execute.
   */
  fun invoke(): Int

  /**
   * Returns a copy of the registers in this context.
   *
   * @return A copy of the registers in this context.
   */
  fun getRegisters(): Map<String, Register>
}

/**
 * An implementation of the [ProgramContext] interface. Supports the linking of an additional
 * context to allow for message passing through [send] and [receive].
 *
 * The instruction pointer is set to the first instruction in the list.
 *
 * @param outputHandler The handler that is called for output data.
 * @param soundHandler The handler that is called for sound data.
 * @param instructions The list of instructions to use for this context.
 * @param registers The map of registers to use for this context.
 */
class DefaultProgramContext (
    override val outputHandler: (output: Long) -> Unit,
    override val soundHandler: (output: Long) -> Unit,
    instructions: List<Instruction>,
    registers: Map<String, Register>,
) : ProgramContext {
  override var output: Long = Long.MIN_VALUE
  override var soundOutput: Long = Long.MIN_VALUE
  private val instructions: MutableList<Instruction> = instructions.toMutableList()
  private val registers: MutableMap<String, Register> = mutableMapOf<String, Register>().apply {
    registers.forEach {
      put(it.key, it.value.copy())
    }
  }
  override val instructionCount: Int = instructions.size
  override val keys: List<String> = registers.keys.sorted()
    @SuppressFBWarnings get

  /**
   * The context that will receive the messages this context sends!
   */
  var linkedContext: DefaultProgramContext? = null
    @SuppressFBWarnings get
    @SuppressFBWarnings set

  private var currentIndex: Int = 0
  override val index: Int
    get() = currentIndex

  private val queue: ConcurrentLinkedQueue<Long> = ConcurrentLinkedQueue()

  private var sendCount: Int = 0
  private var receiveCount: Int = 0

  private var blockedStatus: Boolean = false

  override val blocked: Boolean
    get() = blockedStatus

  override fun sendCount(): Int = sendCount
  override fun receiveCount(): Int = receiveCount

  override fun send(value: Long): Boolean {
    val didSend = linkedContext?.queue?.offer(value) ?: false

    if (didSend) {
      sendCount++
    }

    return didSend
  }

  override fun receive(): Long? {
    val value = queue.poll()

    if (value == null) {
      blockedStatus = true
    } else {
      receiveCount++
      blockedStatus = false
    }

    return value
  }

  override fun get(register: String): Long {
    return registers[register]?.value ?: error("Unable to locate Register: $register")
  }

  override fun get(index: Int): Instruction {
    Objects.checkIndex(index, instructionCount)

    return instructions[index]
  }

  override fun set(register: String, value: Long) {
    registers[register]?.let { it.value = value }
  }

  override fun set(index: Int, instruction: Instruction) {
    Objects.checkIndex(index, instructionCount)

    instructions[index] = instruction
  }

  override fun invoke(): Int {
    if (currentIndex in 0..instructionCount) {
      val offset = instructions[currentIndex].invoke(this)
      currentIndex += offset
    }
    return currentIndex
  }

  override fun getRegisters(): Map<String, Register> {
    return mutableMapOf<String, Register>().apply {
      registers.forEach { put(it.key, it.value.copy()) }
    }
  }
}
