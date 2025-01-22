package com.capital7software.aoc.lib.analysis

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

/**
 * **-- Print Queue --**
 *
 * The North Pole printing department is busier than ever this close to Christmas, and while The
 * Historians continue their search of this historically significant facility, an Elf operating a
 * [very familiar printer](https://adventofcode.com/2017/day/1) beckons you over.
 *
 * The Elf must recognize you, because they waste no time explaining that the new
 * **sleigh launch safety manual** updates won't print correctly. Failure to update the safety
 * manuals would be dire indeed, so you offer your services.
 *
 * @param input The [List] of [String]s that contain the [PageOrderingRule]s and the [PageUpdate]s
 * to parse.
 */
class PrintQueue(input: List<String>) {
  companion object {
    private val RULE_REGEX = "^(?<page1>\\d+)(?<operation>\\D)(?<page2>\\d+)\$".toRegex()
    private const val PAGE1_GROUP = "page1"
    private const val PAGE2_GROUP = "page2"
    private const val OPERATION_GROUP = "operation"

    private fun parseRules(input: List<String>): List<PageOrderingRule> {
      var process = true
      val result = mutableListOf<PageOrderingRule>()

      input.forEach { line ->
        if (line.isEmpty()) {
          process = false
        }
        if (process) {
          result.add(parseRule(line))
        }
      }

      return result
    }

    private fun parseRule(line: String): PageOrderingRule {
      check(line.isNotEmpty()) { "line can not be empty" }

      val matches = RULE_REGEX.find(line)

      check(matches != null) { "Unable to parse Rule: $line" }

      val page1 = matches.groups[PAGE1_GROUP]?.value?.toIntOrNull()
      checkNotNull(page1) { "Unable to parse Rule: $line" }
      val page2 = matches.groups[PAGE2_GROUP]?.value?.toIntOrNull()
      checkNotNull(page2) { "Unable to parse Rule: $line" }
      val operationSymbol = matches.groups[OPERATION_GROUP]?.value
      checkNotNull(operationSymbol) { "Unable to parse Rule: $line" }
      val operation = PageOrderingOperation.from(operationSymbol)
      checkNotNull(operation) { "Unable to parse Rule operation: $operationSymbol" }
      return PageOrderingRule(page1, page2, operation)
    }

    private fun parseUpdates(input: List<String>): List<PageUpdate> {
      var process = false
      val result = mutableListOf<PageUpdate>()

      input.forEach { line ->
        if (process) {
          result.add(parseUpdate(line))
        }
        if (line.isEmpty()) {
          process = true
        }
      }

      return result
    }

    @SuppressFBWarnings
    private fun parseUpdate(line: String): PageUpdate {
      check(line.isNotEmpty()) { "line can not be empty" }

      return PageUpdate(line.split(",").map { it.trim().toInt() })
    }
  }

  // Format is:
  // RULE
  // RULE
  // ...
  // BLANK
  // UPDATE
  // UPDATE
  // ...
  private val rules: List<PageOrderingRule> = parseRules(input)
  private val updates: List<PageUpdate> = parseUpdates(input)

  /**
   * Returns the sum of the middle pages of the [PageUpdate]s that are already in the
   * correct order.
   *
   * Safety protocols clearly indicate that new pages for the safety manuals must be printed in a
   * **very specific order***. The notation ```X|Y``` means that if both page number ```X``` and
   * page number ```Y``` are to be produced as part of an update, page number ```X``` **must** be
   * printed at some point before page number ```Y```.
   *
   * The Elf has for you both the **page ordering rules** and the **pages to produce in each update**
   * (your puzzle input), but can't figure out whether each update has the pages in the right order.
   *
   * For example:
   *
   * ```
   * 47|53
   * 97|13
   * 97|61
   * 97|47
   * 75|29
   * 61|13
   * 75|53
   * 29|13
   * 97|29
   * 53|29
   * 61|53
   * 97|53
   * 61|29
   * 47|13
   * 75|47
   * 97|75
   * 47|61
   * 75|61
   * 47|29
   * 75|13
   * 53|13
   *
   * 75,47,61,53,29
   * 97,61,53,29,13
   * 75,29,13
   * 75,97,47,61,53
   * 61,13,29
   * 97,13,75,29,47
   * ```
   *
   * The first section specifies the **page ordering rules**, one per line. The first rule,
   * ```47|53```, means that if an update includes both page number 47 and page number
   * 53, then page number 47 **must** be printed at some point before page number 53.
   * (47 doesn't necessarily need to be **immediately** before 53; other pages are allowed
   * to be between them.)
   *
   * The second section specifies the page numbers of each **update**. Because most safety manuals
   * are different, the pages needed in the updates are different too. The first update,
   * ```75,47,61,53,29```, means that the update consists of page numbers 75, 47, 61, 53, and 29.
   *
   * To get the printers going as soon as possible, start by identifying
   * **which updates are already in the right order**.
   *
   * In the above example, the first update (```75,47,61,53,29```) is in the right order:
   *
   * - ```75``` is correctly first because there are rules that put each other page after it:
   * ```75|47```, ```75|61```, ```75|53```, and ```75|29```.
   * - ```47``` is correctly second because 75 must be before it (```75|47```) and every other page
   * must be after it according to ```47|61```, ```47|53```, and ```47|29```.
   * - ```61``` is correctly in the middle because 75 and 47 are before it (```75|61``` and
   * ```47|61```) and 53 and 29 are after it (```61|53``` and ```61|29```).
   * - ```53``` is correctly fourth because it is before page number 29 (```53|29```).
   * - ```29``` is the only page left and so is correctly last.
   *
   * Because the first update does not include some page numbers, the ordering rules involving
   * those missing page numbers are ignored.
   *
   * The second and third updates are also in the correct order according to the rules. Like the
   * first update, they also do not include every page number, and so only some of the ordering
   * rules apply - within each update, the ordering rules that involve missing page numbers
   * are not used.
   *
   * The fourth update, ```75,97,47,61,53```, is **not** in the correct order: it would print 75
   * before 97, which violates the rule ```97|75```.
   *
   * The fifth update, ```61,13,29```, is also **not** in the correct order, since it breaks
   * the rule ```29|13```.
   *
   * The last update, ```97,13,75,29,47```, is not in the correct order due to breaking
   * several rules.
   *
   * For some reason, the Elves also need to know the **middle page number** of each update being
   * printed. Because you are currently only printing the correctly-ordered updates, you will
   * need to find the middle page number of each correctly-ordered update. In the above example,
   * the correctly-ordered updates are:
   *
   * ```
   * 75,47,61,53,29
   * 97,61,53,29,13
   * 75,29,13
   * ```
   *
   * These have middle page numbers of ```61```, ```53```, and ```29``` respectively. Adding
   * these page numbers together gives **```143```**.
   *
   * @return The sum of the middle pages of the [PageUpdate]s that are already in the
   * correct order.
   */
  fun sumOfMiddlePagesOfValidPageUpdates(): Long {
    val validUpdates = getValidUpdates()

    return validUpdates.sumOf { it.middlePage().toLong() }
  }

  /**
   * Returns the sum of the middle pages of the [PageUpdate]s that are currently in the
   * incorrect order. The middle page is selected after the [PageUpdate] has been re-ordered.
   *
   * While the Elves get to work printing the correctly-ordered updates, you have a little time
   * to fix the rest of them.
   *
   * For each of the **incorrectly-ordered updates**, use the page ordering rules to put the page
   * numbers in the right order. For the above example, here are the three incorrectly-ordered
   * updates and their correct orderings:
   *
   * - ```75,97,47,61,53``` becomes ```97,75,47,61,53```.
   * - ```61,13,29``` becomes ```61,29,13```.
   * - ```97,13,75,29,47``` becomes ```97,75,47,29,13```.
   *
   * After taking **only the incorrectly-ordered updates** and ordering them correctly, their
   * middle page numbers are ```47```, ```29```, and ```47```. Adding these together
   * produces **```123```**.
   *
   * @return The sum of the middle pages of the [PageUpdate]s that are currently in the
   * incorrect order. The middle page is selected after the [PageUpdate] has been re-ordered.
   */
  fun sumOfMiddlePagesOfInvalidPageUpdates(): Long {
    val invalidUpdates = getInvalidUpdates()
    val validUpdates = fixUpdates(invalidUpdates)
    return validUpdates.sumOf { it.middlePage().toLong() }
  }

  @SuppressFBWarnings
  private fun fixUpdates(invalidUpdates: List<PageUpdate>): List<PageUpdate> {
    return invalidUpdates.map { invalid ->
      val rulesToApply = getRulesForUpdate(invalid)
      var update = invalid

      var passesAll = false
      // Continuously swap entries of rules that fail until all rules pass!
      // The loop will exit once the update passes all the rules that apply to it.
      while (!passesAll) {
        passesAll = true
        rulesToApply.forEach { rule ->
          if (!rule.apply(update)) {
            update = swap(update, rule.page1, rule.page2)
            passesAll = false
          }
        }
      }
      update
    }
  }

  private fun swap(update: PageUpdate, page1: Int, page2: Int): PageUpdate {
    val pages = update.pages.toMutableList()
    val a = pages.indexOf(page1)
    val b = pages.indexOf(page2)
    pages[a] = page2
    pages[b] = page1
    return PageUpdate(pages)
  }

  @SuppressFBWarnings
  private fun getValidUpdates(): List<PageUpdate> {
    return updates.filter { update ->
      val rulesToApply = getRulesForUpdate(update)

      var valid = true

      rulesToApply.forEach { rule ->
        if (valid) {
          if (!rule.apply(update)) {
            valid = false
          }
        }
      }

      valid
    }
  }

  @SuppressFBWarnings
  private fun getInvalidUpdates(): List<PageUpdate> {
    return updates.filter { update ->
      val rulesToApply = getRulesForUpdate(update)

      var valid = true

      rulesToApply.forEach { rule ->
        if (valid) {
          if (!rule.apply(update)) {
            valid = false
          }
        }
      }

      !valid
    }
  }

  @SuppressFBWarnings
  private fun getRulesForUpdate(update: PageUpdate) =
      rules.filter { rule -> rule.doesApply(update) }
}

/**
 * When comparing the ordering of two pages from a [PageUpdate] in a [PageOrderingRule],
 * this determines if page x must come [BEFORE] or [AFTER] y
 *
 * @property symbol The [String] representation of this operation.
 */
enum class PageOrderingOperation(val symbol: String) {
  BEFORE("|") {
    override fun apply(page1Index: Int, page2Index: Int): Boolean {
      return page1Index < page2Index
    }
  },
  AFTER(":") {
    override fun apply(page1Index: Int, page2Index: Int): Boolean {
      return page1Index > page2Index
    }
  };

  /**
   * Applies this operation to the specified pages. Returns
   * true if the order of the pages is correct for this rule.
   *
   * @param page1Index The index of page1 in the [PageUpdate]
   * @param page2Index The index of page2 in the [PageUpdate]
   */
  abstract fun apply(page1Index: Int, page2Index: Int): Boolean

  companion object {
    /**
     * Returns the instance that represents the specified symbol or null if the symbol is
     * not a valid [PageOrderingOperation].
     *
     * @return The instance that represents the specified symbol or null if the symbol is
     * not a valid [PageOrderingOperation].
     */
    fun from(operationSymbol: String): PageOrderingOperation? {
      return entries.firstOrNull { operationSymbol == it.symbol }
    }
  }
}

/**
 * A list of page numbers in the order they should be updated.
 *
 * @property pages The ordered list of pages this update applies to.
 */
@SuppressFBWarnings
data class PageUpdate(val pages: List<Int>) {
  private val pageSet = pages.toSet()

  /**
   * Returns true if the specified pages are in this page update.
   */
  fun containsAll(page1: Int, page2: Int): Boolean {
    return pageSet.contains(page1) && pageSet.contains(page2)
  }

  /**
   * Returns the value of the page found in the middle of this update. If the update has an
   * even number of pages, then the "middle" page is the last page in the lower-half of the pages.
   *
   * @return The value of the page found in the middle of this update. If the update has an
   * even number of pages, then the "middle" page is the last page in the lower-half of the pages.
   */
  fun middlePage(): Int {
    return if (pages.size % 2 != 0) pages[pages.size / 2] else pages[(pages.size - 1) / 2]
  }
}

/**
 * Defines a page ordering rule that is used when determining if a [PageUpdate] is valid or not.
 * The rule only applies
 *
 * @property page1: The first page number this rule applies to.
 * @property page2: The second page number this rule applies to.
 * @property operation: The [PageOrderingOperation] that determines if [page1] must be before or
 * after [page2]
 */
data class PageOrderingRule(
    val page1: Int,
    val page2: Int,
    val operation: PageOrderingOperation) {
  /**
   * Returns true if this rule applies to the specified page.
   *
   * @return True if this rule applies to the specified page.
   */
  fun contains(page: Int): Boolean {
    return page == page1 || page == page2
  }

  /**
   * Returns true if this rules applies to the specified [PageUpdate].
   *
   * @param update The PageUpdate to check if this rule applies to it.
   * @return True if this rules applies to the specified [PageUpdate].
   *
   */
  fun doesApply(update: PageUpdate): Boolean {
    return update.containsAll(page1, page2)
  }

  /**
   * Applies this rule and returns true if the [update] passes it.
   *
   * @param update The [PageUpdate] to apply this rule to.
   * @return True if the [update] passes this rule.
   */
  fun apply(update: PageUpdate): Boolean {
    val page1Index = update.pages.indexOf(page1)
    val page2Index = update.pages.indexOf(page2)

    return operation.apply(page1Index, page2Index)
  }
}
