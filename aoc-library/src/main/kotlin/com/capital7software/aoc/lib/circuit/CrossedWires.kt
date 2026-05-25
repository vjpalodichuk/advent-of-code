package com.capital7software.aoc.lib.circuit

import com.capital7software.aoc.lib.circuit.gate.AndGate16Bit
import com.capital7software.aoc.lib.circuit.gate.Gate
import com.capital7software.aoc.lib.circuit.gate.IdentityGate
import com.capital7software.aoc.lib.circuit.gate.OrGate16Bit
import com.capital7software.aoc.lib.circuit.signal.Signal
import com.capital7software.aoc.lib.circuit.signal.SignalInteger
import com.capital7software.aoc.lib.circuit.signal.SignalSupplier
import com.capital7software.aoc.lib.circuit.wire.Wire
import com.capital7software.aoc.lib.circuit.wire.WireInteger
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.util.Optional

/**
 * Models a crossed-wires boolean circuit where each wire can carry either `0`, `1`, or no signal.
 *
 * Part 1 is evaluated lazily through the existing [Wire] and [Gate] abstractions. Part 2 uses the
 * parsed gate connections to identify outputs that violate ripple-carry-adder structure.
 *
 * @param wires The wires keyed by their IDs.
 * @param connections The parsed gate connections in this circuit.
 */
class CrossedWires private constructor(
    private val wires: Map<String, Wire<Int>>,
    private val connections: List<GateConnection>
) {
  /**
   * Companion object for parsing a crossed-wires circuit.
   */
  @SuppressFBWarnings
  companion object {
    private val InitialWireRegex = """^(?<wire>\w+):\s(?<value>[01])$""".toRegex()
    private val GateRegex =
        """^(?<left>\w+)\s(?<operation>AND|OR|XOR)\s(?<right>\w+)\s->\s(?<output>\w+)$""".toRegex()

    /**
     * Parses the provided crossed-wires circuit input.
     *
     * @param input The puzzle input.
     * @return A [CrossedWires] circuit.
     */
    fun parse(input: List<String>): CrossedWires {
      val wires = mutableMapOf<String, Wire<Int>>()
      val connections = mutableListOf<GateConnection>()
      var parsingInitialWires = true
      var gateId = 0

      input.forEach { line ->
        when {
          line.isBlank() -> parsingInitialWires = false
          parsingInitialWires -> parseInitialWire(line, wires, gateId++)
          else -> parseGate(line, wires, connections, gateId++)
        }
      }

      return CrossedWires(wires, connections)
    }

    private fun parseInitialWire(
        line: String,
        wires: MutableMap<String, Wire<Int>>,
        gateId: Int
    ) {
      val match = InitialWireRegex.matchEntire(line)
          ?: error("Unable to parse initial wire value: $line")

      val wireId = match.groups["wire"]?.value
          ?: error("Unable to parse wire ID from: $line")
      val value = match.groups["value"]?.value?.toInt()
          ?: error("Unable to parse wire value from: $line")

      val signal = SignalInteger(value)
      val gate = IdentityGate("initial-$gateId-$wireId") { Optional.of(signal) }
      val wire = wires.getOrPut(wireId) { WireInteger(wireId) }

      wire.source(gate)
    }

    private fun parseGate(
        line: String,
        wires: MutableMap<String, Wire<Int>>,
        connections: MutableList<GateConnection>,
        gateId: Int
    ) {
      val match = GateRegex.matchEntire(line)
          ?: error("Unable to parse gate: $line")

      val leftWireId = match.groups["left"]?.value
          ?: error("Unable to parse left input from: $line")
      val operation = match.groups["operation"]?.value
          ?: error("Unable to parse operation from: $line")
      val rightWireId = match.groups["right"]?.value
          ?: error("Unable to parse right input from: $line")
      val outputWireId = match.groups["output"]?.value
          ?: error("Unable to parse output wire from: $line")

      val left = wires.getOrPut(leftWireId) { WireInteger(leftWireId) }
      val right = wires.getOrPut(rightWireId) { WireInteger(rightWireId) }
      val output = wires.getOrPut(outputWireId) { WireInteger(outputWireId) }
      val gate = when (Operation.valueOf(operation)) {
        Operation.AND -> AndGate16Bit("gate-$gateId-$outputWireId", left, right)
        Operation.OR -> OrGate16Bit("gate-$gateId-$outputWireId", left, right)
        Operation.XOR -> XorGateInteger("gate-$gateId-$outputWireId", left, right)
      }

      output.source(gate)
      connections.add(
          GateConnection(
              left = leftWireId,
              operation = Operation.valueOf(operation),
              right = rightWireId,
              output = outputWireId
          )
      )
    }
  }

  /**
   * Returns the decimal number represented by all wires whose IDs start with [prefix].
   *
   * Wires are interpreted as little-endian bits: `z00` is bit `0`, `z01` is bit `1`, and so on.
   *
   * @param prefix The output wire prefix.
   * @return The decimal value represented by the matching wires.
   */
  fun output(prefix: String = "z"): Long {
    return wires
        .filterKeys { it.startsWith(prefix) }
        .toSortedMap(compareByDescending { it })
        .values
        .joinToString(separator = "") { wire -> wire.requiredSignal().toString() }
        .toLong(radix = 2)
  }

  /**
   * Returns the sorted comma-separated list of gate outputs that have been swapped.
   *
   * The real puzzle input is a ripple-carry adder, while the smaller example uses a direct bitwise
   * shape. The circuit topology is used to select the appropriate validator.
   *
   * @return The sorted comma-separated swapped wire IDs.
   */
  fun swappedOutputWires(): String {
    return if (connections.any { it.operation == Operation.OR }) {
      swappedRippleCarryAdderOutputWires()
    } else {
      swappedBitwiseAndOutputWires()
    }
  }

  private fun swappedBitwiseAndOutputWires(): String {
    return connections
        .asSequence()
        .filter { connection -> connection.output.startsWith("z") }
        .filterNot { connection -> connection.isDirectBitwiseAndOutput() }
        .map { connection -> connection.output }
        .sorted()
        .joinToString(",")
  }

  @SuppressFBWarnings
  private fun swappedRippleCarryAdderOutputWires(): String {
    val byInput = connections
        .flatMap { connection -> listOf(connection.left to connection, connection.right to connection) }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )
    val highestZ = connections
        .map { it.output }
        .filter { it.startsWith("z") }
        .maxOrNull()
        ?: return ""

    val swapped = mutableSetOf<String>()

    connections.forEach { connection ->
      val consumers = byInput[connection.output].orEmpty()

      if (
          connection.output.startsWith("z")
          && connection.output != highestZ
          && connection.operation != Operation.XOR
      ) {
        swapped.add(connection.output)
      }

      if (
          connection.operation == Operation.XOR
          && !connection.output.startsWith("z")
          && !connection.hasPrimaryInput()
      ) {
        swapped.add(connection.output)
      }

      if (
          connection.operation == Operation.XOR
          && connection.hasPrimaryInput()
          && !connection.isBitZeroInput()
          && consumers.none { it.operation == Operation.XOR }
      ) {
        swapped.add(connection.output)
      }

      if (
          connection.operation == Operation.AND
          && !connection.isBitZeroInput()
          && consumers.none { it.operation == Operation.OR }
      ) {
        swapped.add(connection.output)
      }
    }

    return swapped.sorted().joinToString(",")
  }

  private fun Wire<Int>.requiredSignal(): Int {
    return supply()
        .flatMap { it.signal() }
        .orElseThrow { IllegalStateException("Wire ${id()} did not produce a signal.") }
  }

  /**
   * A parsed binary gate connection.
   *
   * @param left The left input wire ID.
   * @param operation The gate operation.
   * @param right The right input wire ID.
   * @param output The output wire ID.
   */
  data class GateConnection(
      val left: String,
      val operation: Operation,
      val right: String,
      val output: String
  ) {
    /**
     * Returns true if this gate connection has a primary input.
     */
    fun hasPrimaryInput(): Boolean {
      return left.isPrimaryInput() || right.isPrimaryInput()
    }

    /**
     * Returns true if this gate connection is a bit-zero input.
     */
    fun isBitZeroInput(): Boolean {
      return setOf(left, right) == setOf("x00", "y00")
    }

    /**
     * Returns true if this gate connection is a direct bitwise AND output.
     */
    fun isDirectBitwiseAndOutput(): Boolean {
      val outputBit = output.removePrefix("z")

      return operation == Operation.AND && setOf(left, right) == setOf("x$outputBit", "y$outputBit")
    }

    private fun String.isPrimaryInput(): Boolean {
      return startsWith("x") || startsWith("y")
    }
  }

  /**
   * Supported crossed-wires binary operations.
   */
  enum class Operation {
    /**
     * Bitwise AND.
     */
    AND,
    /**
     * Bitwise OR.
     */
    OR,
    /**
     * Bitwise XOR.
     */
    XOR
  }

  /**
   * A reusable integer XOR gate.
   *
   * This gate mirrors the behavior of the existing integer circuit gates, but does not apply a 16-bit
   * mask because the crossed-wires circuit only carries `0` or `1`.
   */
  private data class XorGateInteger(
      private val id: String,
      private val first: SignalSupplier<Int>,
      private val second: SignalSupplier<Int>
  ) : Gate<Int> {
    override fun id(): String = id

    override fun supply(): Optional<Signal<Int>> {
      val left = first.supply().flatMap { it.signal() }
      val right = second.supply().flatMap { it.signal() }

      if (left.isEmpty || right.isEmpty) {
        return Optional.empty()
      }

      return Optional.of(SignalInteger(left.get() xor right.get()))
    }
  }
}
