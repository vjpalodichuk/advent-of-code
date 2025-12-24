package com.capital7software.aoc.lib.analysis

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings


/**
 * The Historians take you to a familiar [rope bridge](https://adventofcode.com/2022/day/9) over
 * a river in the middle of a jungle. The Chief isn't on this side of the bridge, though;
 * maybe he's on the other side?
 *
 * When you go to cross the bridge, you notice a group of engineers trying to repair it.
 * (Apparently, it breaks pretty frequently.) You won't be able to cross until it's fixed.
 *
 * You ask how long it'll take; the engineers tell you that it only needs final calibrations,
 * but some young elephants were playing nearby and **stole all the operators** from their
 * calibration equations! They could finish the calibrations if only someone could determine
 * which test values could possibly be produced by placing any combination of operators into
 * their calibration equations.
 *
 * @param input [List] of [String]s that represents the test values and calibration equations.
 */
class BridgeRepair(input: List<String>) {
  private companion object {
    @SuppressFBWarnings
    private fun buildCalibrations(input: List<String>): List<CalibrationTest> {
      return input.map { line ->
        val split = line.split(": ")
        val expectedValue = split[0].toLong()
        val inputs = split[1].split(" ").map { it.trim().toLong() }
        CalibrationTest(expectedValue, inputs)
      }
    }
  }

  private val calibrations: List<CalibrationTest> by lazy { buildCalibrations(input) }

  /**
   * To finish the calibrations someone needs to determine which test values could possibly be
   * produced by placing any combination of operators into their calibration equations.
   *
   * For example:
   *
   * ```
   * 190: 10 19
   * 3267: 81 40 27
   * 83: 17 5
   * 156: 15 6
   * 7290: 6 8 6 15
   * 161011: 16 10 13
   * 192: 17 8 14
   * 21037: 9 7 18 13
   * 292: 11 6 16 20
   * ```
   *
   * Each line represents a single equation. The test value appears before the colon on each line;
   * it is your job to determine whether the remaining numbers can be combined with operators to
   * produce the test value.
   *
   * Operators are **always evaluated left-to-right**, **not** according to precedence rules.
   * Furthermore, numbers in the equations cannot be rearranged. Glancing into the jungle, you
   * can see elephants holding two different types of operators: **add** (```+```) and **multiply**
   * (```*```).
   *
   * Only three of the above equations can be made true by inserting operators:
   *
   * - ```190: 10 19``` has only one position that accepts an operator: between ```10``` and
   * ```19```. Choosing ```+``` would give ```29```, but choosing ```*``` would give the test
   * value (```10 * 19 = 190```).
   * - ```3267: 81 40 27``` has two positions for operators. Of the four possible configurations of
   * the operators, **two** cause the right side to match the test value: ```81 + 40 * 27``` and
   * ```81 * 40 + 27``` both equal ```3267``` (when evaluated left-to-right)!
   * - ```292: 11 6 16 20``` can be solved in exactly one way: ```11 + 6 * 16 + 20```.
   *
   * The engineers just need the **total calibration result**, which is the sum of the test values
   * from just the equations that could possibly be true. In the above example, the sum of the test
   * values for the three equations listed above is **```3749```**.
   *
   * **To use only add and multiply operators, set [addAndMulOnly] to true**.
   *
   * The engineers seem concerned; the total calibration result you gave them is nowhere close to
   * being within safety tolerances. Just then, you spot your mistake: some well-hidden elephants
   * are holding a **third type of operator**.
   *
   * The [concatenation](https://en.wikipedia.org/wiki/Concatenation) operator (```||```) combines
   * the digits from its left and right inputs into a single number. For example, ```12 || 345```
   * would become ```12345```. All operators are still evaluated left-to-right.
   *
   * Now, apart from the three equations that could be made true using only addition
   * and multiplication, the above example has three more equations that can be made
   * true by inserting operators:
   *
   * - ```156: 15 6``` can be made true through a single concatenation: ```15 || 6 = 156```.
   * - ```7290: 6 8 6 15``` can be made true using ```6 * 8 || 6 * 15```.
   * - ```192: 17 8 14``` can be made true using ```17 || 8 + 14```.
   *
   * Adding up all six test values (the three that could be made before using only ```+``` and
   * ```*``` plus the new three that can now be made by also using ```||```) produces the new
   * **total calibration result** of **```11387```**.
   *
   * @param addAndMulOnly If true, then only complete and valid results are returned; otherwise,
   * all complete results are returned.
   * @return A [Map] where the key is the unique [CalibrationTest] and the value is a [List] of
   * complete solutions.
   */
  @SuppressFBWarnings
  fun possiblyTrueTestValues(addAndMulOnly: Boolean = true): Map<CalibrationTest, List<CalibrationTestResult>> {
    val operators = if (addAndMulOnly) {
      CalibrationTestOperator
          .entries
          .filter { it == CalibrationTestOperator.MULTIPLY || it == CalibrationTestOperator.ADD }
    } else {
      CalibrationTestOperator.entries.toList()
    }
    return calibrations
        .associateWith { it.solve(operators, validOnly = true) }
        .filterValues { it.isNotEmpty() }
  }
}

/**
 * Operations that can be performed between two operands during a [CalibrationTest]
 *
 * @property symbol The character representation of this operator.
 */
enum class CalibrationTestOperator(val symbol: String) {
  /**
   * Adds to operands together and returns the results of the addition.
   */
  ADD("+") {
    override fun apply(operand1: Long, operand2: Long): Long {
      return operand1 + operand2
    }
  },

  /**
   * Multiplies two operands together and returns the results of the multiplication.
   */
  MULTIPLY("*") {
    override fun apply(operand1: Long, operand2: Long): Long {
      return operand1 * operand2
    }
  },

  /**
   * Concatenates operand1 with operand2 and returns the result. For example, if operand1 is 1
   * and operand2 is 2, then the result of this operation would be 12.
   */
  CONCATENATE("||") {
    override fun apply(operand1: Long, operand2: Long): Long {
      return "$operand1$operand2".toLong()
    }
  };

  /**
   * Applies the operation using the two operands as inputs.
   *
   * @param operand1 The first or "left" operand.
   * @param operand2 The second or "right" operand.
   * @return The result of the operation.
   */
  abstract fun apply(operand1: Long, operand2: Long): Long

  override fun toString(): String {
    return symbol
  }
}

/**
 * A simple calibration test class. The [expected] property is the desired result from solving
 * the test. The [operands] are the individual values that need to be operated on in a
 * left-to-right evaluation. Unfortunately, the tests do not include the [CalibrationTestOperator]s
 * needed in order to properly evaluate the [operands]. The list of available operators to use is
 * passed to [solve]. The list of [CalibrationTestResult] can be used to discover the correct
 * operators to use.
 *
 * @property expected The value that must be calculated from the [operands] to consider the test
 * valid.
 * @property operands The list of values to use in the calculations of this test.
 */
@SuppressFBWarnings
data class CalibrationTest(
    val expected: Long,
    val operands: List<Long>,
) {
  /**
   * Solve this test! To receive only valid and complete test results, then set [validOnly] to true.
   * Please note that an empty list may be returned if [validOnly] is true and no valid compete
   * solutions were found. If false, then many invalid intermediate and final results will be
   * included.
   *
   * @param operators The available [CalibrationTestOperator]s to use during each calculation.
   * @param validOnly If true, then only valid and complete results will be returned. If false,
   * then many invalid intermediate and final results will be included.
   */
  fun solve(
      operators: Collection<CalibrationTestOperator>,
      validOnly: Boolean = true
  ): List<CalibrationTestResult> {
    /**
     *  We have a few options to select from:
     *
     *  - Use a DepthFirstPathfinder to dynamically build a graph while finding all valid
     *  results. Efficient but seems a bit over the top for this problem.
     *  - Use an AI algorithm like Local Search to randomly find a solution. Not
     *  guaranteed to find all solutions or halt! This is also over-the-top.
     *  - Use recursion to quickly find all solutions. Very efficient.
     */
    val results = mutableListOf<CalibrationTestResult>()
    val currentOperand = operands.first()
    val currentResult = CalibrationTestResult(
        this,
        0,
        listOf(currentOperand),
        listOf(),
        listOf(),
        currentOperand,
        valid = true
    )

    findSolutions(operators, currentResult, results, validOnly)

    return results
  }

  private fun findSolutions(
      operators: Collection<CalibrationTestOperator>,
      currentResult: CalibrationTestResult,
      results: MutableList<CalibrationTestResult>,
      validOnly: Boolean
  ) {
    if (currentResult.complete && currentResult.valid) {
      results.add(currentResult)
    } else if (currentResult.complete && !validOnly) {
      results.add(currentResult)
    } else if (currentResult.valid || !validOnly) {
      // we can continue the calculations with the next operand
      val nextIndex = currentResult.step + 1
      val complete = nextIndex == currentResult.test.operands.size - 1
      val operand = currentResult.test.operands[nextIndex]

      operators.forEach { operator ->
        val actual = operator.apply(currentResult.actual, operand)
        val valid = if (complete && actual == currentResult.test.expected) {
          true
        } else if (!complete && actual <= currentResult.test.expected) {
          true
        } else {
          false
        }

        val nextResult = currentResult.copy(
            step = nextIndex,
            operands = currentResult.operands.plus(operand),
            operators = currentResult.operators.plus(operator),
            results = currentResult.results.plus(actual),
            actual = actual,
            complete = complete,
            valid = valid
        )

        findSolutions(operators, nextResult, results, validOnly)
      }
    }
  }
}

/**
 * The result of executing a calibration test or a step in a calibration test. All lists are
 * ordered as they are encountered or calculated.
 *
 * Every step includes the history of the previous step via the [operands] and [operators]
 * properties and to a lesser extent, the [results] property. The order of the elements is such
 * that all operations can be recreated to get to the value in [actual]. There is always one
 * fewer operator than operand while there are the same number of [results] as [operators].
 *
 * @property test The [CalibrationTest] that this result is computed from.
 * @property step The step number in the calibration test. Steps range from 1...n. Where
 * n is the number of [CalibrationTest.operands] minus 1. This is because step 1's result
 * is calculated using the first two operands in the [CalibrationTest] while the remainder
 * is calculated using the result from the previous step and the next operand in the
 * [CalibrationTest].
 * @property operands The list of operands from the [CalibrationTest] that have been used to
 * calculate the [actual] value. This includes the operand(s) used in this step.
 * @property operators The list of operators used for each step's (including this one) calculation.
 * @property results The results of each step's (including this one) calculation.
 * @property actual This step's calculation result. The name is a reflection of it being a result.
 * @property complete If this is false, then this is an **intermediate** calculation. If true, then
 * this result represents the final calculation need to complete the [CalibrationTest].
 * @property valid During the final calculation in the [CalibrationTest], if it is true,
 * then [actual] matches [CalibrationTest.expected] and if not, this if false. During and
 * **intermediate** calculation this will be true if [actual] is less than
 * [CalibrationTest.expected].
 */
@SuppressFBWarnings
data class CalibrationTestResult(
    val test: CalibrationTest,
    val step: Int,
    val operands: List<Long>,
    val operators: List<CalibrationTestOperator>,
    val results: List<Long>,
    val actual: Long,
    val complete: Boolean = false,
    val valid: Boolean = false,
)
