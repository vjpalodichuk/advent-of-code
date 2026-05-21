package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import com.capital7software.aoc.lib.computer.ChronospatialComputer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day17Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day17Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day17()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testProgramOutputExample() {
    val expected = "4,6,3,5,6,3,5,2,1,0"

    val actual = Day17().programOutput(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The program output $actual is not the expected output $expected."
    )
  }

  @Test
  fun testProgramOutputExample2() {
    setupFromFile("inputs/input_day_17-02.txt")

    val expected = 117440L
    val actual = Day17().lowestPositiveInitialRegisterA(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The lowest value for Register A $actual is not the expected lowest value $expected."
    )
  }

  @Test
  fun testComboOperandCanReadRegisterC() {
    val input = listOf(
        "Register A: 0",
        "Register B: 0",
        "Register C: 9",
        "",
        "Program: 2,6"
    )

    val computer = ChronospatialComputer.load(input)
    computer.run()

    Assertions.assertEquals(
        1L,
        computer["B"],
        "Register B should contain 1 after executing bst with combo operand C."
    )
  }

  @Test
  fun testOutputInstruction() {
    val input = listOf(
        "Register A: 10",
        "Register B: 0",
        "Register C: 0",
        "",
        "Program: 5,0,5,1,5,4"
    )
    val expected = "0,1,2"

    val actual = ChronospatialComputer.load(input).runToString()

    Assertions.assertEquals(
        expected,
        actual,
        "The program output $actual is not the expected output $expected."
    )
  }

  @Test
  fun testAdvAndJnzLoopExample() {
    val input = listOf(
        "Register A: 2024",
        "Register B: 0",
        "Register C: 0",
        "",
        "Program: 0,1,5,4,3,0"
    )
    val expected = "4,2,5,6,7,7,7,7,3,1,0"

    val computer = ChronospatialComputer.load(input)
    val actual = computer.runToString()

    Assertions.assertEquals(
        expected,
        actual,
        "The program output $actual is not the expected output $expected."
    )
    Assertions.assertEquals(
        0L,
        computer["A"],
        "Register A should contain 0 after the program halts."
    )
  }

  @Test
  fun testLiteralXor() {
    val input = listOf(
        "Register A: 0",
        "Register B: 29",
        "Register C: 0",
        "",
        "Program: 1,7"
    )

    val computer = ChronospatialComputer.load(input)
    computer.run()

    Assertions.assertEquals(
        26L,
        computer["B"],
        "Register B should contain 26 after XOR with literal 7."
    )
  }

  @Test
  fun testRegisterXor() {
    val input = listOf(
        "Register A: 0",
        "Register B: 2024",
        "Register C: 43690",
        "",
        "Program: 4,0"
    )

    val computer = ChronospatialComputer.load(input)
    computer.run()

    Assertions.assertEquals(
        44354L,
        computer["B"],
        "Register B should contain 44354 after XOR with register C."
    )
  }

  @Test
  fun testProgramOutput() {
    val instance = Day17()

    val actual = instance.programOutput(lines)

    Assertions.assertTrue(
        actual.matches("""\d(?:,\d)*""".toRegex()),
        "The program output should be a comma-separated list of 3-bit values."
    )
  }

  @Test
  fun testLowestPositiveInitialRegisterAProducesProgramCopyExample() {
    val input = listOf(
        "Register A: 2024",
        "Register B: 0",
        "Register C: 0",
        "",
        "Program: 0,3,5,4,3,0"
    )
    val expectedProgram = listOf(0, 3, 5, 4, 3, 0)

    val registerA = ChronospatialComputer.load(
        input
    ).lowestPositiveInitialRegisterAForProgramCopy()

    val computer = ChronospatialComputer.load(input)
    computer["A"] = registerA
    val actual = computer.run()

    Assertions.assertEquals(
        expectedProgram,
        actual,
        "The program output $actual is not an exact copy of $expectedProgram."
    )
  }

  @Test
  fun testLowestPositiveInitialRegisterA() {
    val instance = Day17()

    setupFromFile("inputs/input_day_17-03.txt")

    val expected = 105734774294938L

    val actual = instance.lowestPositiveInitialRegisterA(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The lowest positive initial register A value $actual is not the expected value $expected."
    )
  }
}
