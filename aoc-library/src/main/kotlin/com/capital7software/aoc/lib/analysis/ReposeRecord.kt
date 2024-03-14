package com.capital7software.aoc.lib.analysis

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

/**
 * You've sneaked into another supply closet - this time, it's across from the prototype
 * suit manufacturing lab. You need to sneak inside and fix the issues with the suit, but
 * there's a guard stationed outside the lab, so this is as close as you can safely get.
 *
 * As you search the closet for anything that might help, you discover that you're not
 * the first person to want to sneak in. Covering the walls, someone has spent an hour
 * starting every midnight for the past few months secretly observing this guard post!
 * They've been writing down the ID of **the one guard on duty that night** - the Elves
 * seem to have decided that one guard was enough for the overnight shift - as well as
 * when they fall asleep or wake up while at their post (your puzzle input).
 *
 * For example, consider the following records, which have already been organized
 * into chronological order:
 *
 * ```
 * [1518-11-01 00:00] Guard #10 begins shift
 * [1518-11-01 00:05] falls asleep
 * [1518-11-01 00:25] wakes up
 * [1518-11-01 00:30] falls asleep
 * [1518-11-01 00:55] wakes up
 * [1518-11-01 23:58] Guard #99 begins shift
 * [1518-11-02 00:40] falls asleep
 * [1518-11-02 00:50] wakes up
 * [1518-11-03 00:05] Guard #10 begins shift
 * [1518-11-03 00:24] falls asleep
 * [1518-11-03 00:29] wakes up
 * [1518-11-04 00:02] Guard #99 begins shift
 * [1518-11-04 00:36] falls asleep
 * [1518-11-04 00:46] wakes up
 * [1518-11-05 00:03] Guard #99 begins shift
 * [1518-11-05 00:45] falls asleep
 * [1518-11-05 00:55] wakes up
 * ```
 *
 * Timestamps are written using year-month-day hour:minute format. The guard falling asleep
 * or waking up is always the one whose shift most recently started. Because all asleep/awake
 * times are during the midnight hour (00:00 - 00:59), only the minute portion (00 - 59)
 * is relevant for those events.
 *
 * Visually, these records show that the guards are asleep at these times:
 *
 * ```
 * Date   ID   Minute
 *             000000000011111111112222222222333333333344444444445555555555
 *             012345678901234567890123456789012345678901234567890123456789
 * 11-01  #10  .....####################.....#########################.....
 * 11-02  #99  ........................................##########..........
 * 11-03  #10  ........................#####...............................
 * 11-04  #99  ....................................##########..............
 * 11-05  #99  .............................................##########.....
 * ```
 *
 * The columns are Date, which shows the month-day portion of the relevant day; ID, which
 * shows the guard on duty that day; and Minute, which shows the minutes during which the
 * guard was asleep within the midnight hour. (The Minute column's header shows the minute's
 * ten's digit in the first row and the one's digit in the second row.) Awake is shown as .,
 * and asleep is shown as #.
 *
 * Note that guards count as asleep on the minute they fall asleep, and they count as awake
 * on the minute they wake up. For example, because Guard #10 wakes up at 00:25 on
 * 1518-11-01, minute 25 is marked as awake.
 *
 * If you can figure out the guard most likely to be asleep at a specific time, you might
 * be able to trick that guard into working tonight so you can have the best chance of
 * sneaking in. You have two strategies for choosing the best guard/minute combination.
 *
 * **Strategy 1:** Find the guard that has the most minutes asleep. What minute does that
 * guard spend asleep the most?
 *
 * In the example above, Guard #10 spent the most minutes asleep, a total of 50 minutes
 * (20+25+5), while Guard #99 only slept for a total of 30 minutes (10+10+10). Guard #**10**
 * was asleep most during minute **24** (on two days, whereas any other minute the guard was
 * asleep was only seen on one day).
 *
 * While this example listed the entries in chronological order, your entries are in the
 * order you found them. You'll need to organize them before they can be analyzed.
 *
 * @param input The overnight duty shift and the dates and times the guard falls asleep
 * and wakes up to parse.
 */
class ReposeRecord @SuppressFBWarnings constructor(input: List<String>) {
  private companion object {
    private val DATETIME_REGEX: Regex = "(?<datetime>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2})".toRegex()
    private const val DATETIME_GROUP: String = "datetime"
    private val GUARD_AWAKE_REGEX: Regex = "wakes up".toRegex()
    private val GUARD_REGEX: Regex = "Guard #(?<guard>\\d+) begins".toRegex()
    private const val GUARD_GROUP: String = "guard"
    private val GUARD_SLEEP_REGEX: Regex = "falls asleep".toRegex()

    private val FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd HH:mm")
        .toFormatter()
        .withZone(ZoneId.of("UTC"))

    @SuppressFBWarnings
    private fun parseEntries(input: List<String>): List<Entry> {
      val answer = mutableListOf<Entry>()

      val items = input.sorted()

      var lastId = -1

      for (i in items.indices) {
        val item = items[i]

        val dateTime = getInstant(item)
        val guardId = getId(item, lastId)

        val type = if (guardId != lastId || GUARD_REGEX.containsMatchIn(item)) {
          lastId = guardId
          Type.START
        } else if (GUARD_AWAKE_REGEX.containsMatchIn(item)) {
          Type.WAKE
        } else if (GUARD_SLEEP_REGEX.containsMatchIn(item)) {
          Type.SLEEP
        } else {
          error("Unknown type of event: $item")
        }
        answer.add(Entry(dateTime, guardId, type))
      }
      return answer
    }

    private fun getId(input: String, lastId: Int): Int {
      return if (GUARD_REGEX.containsMatchIn(input)) {
        val match = GUARD_REGEX.find(input) ?: error("Unable to find a valid Guard ID")
        val group = match.groups[GUARD_GROUP] ?: error("Unable to find $GUARD_GROUP in the results")
        group.value.toInt()
      } else {
        lastId
      }
    }

    private fun getInstant(input: String): Instant {
      val match = DATETIME_REGEX.find(input)

      check(match != null) { "Unable to find a valid date and time pattern in $input" }

      val group = match.groups[DATETIME_GROUP]
          ?: error("Unable to find the $DATETIME_GROUP in the results")

      return FORMATTER.parse(group.value, Instant::from)
    }
  }

  private enum class Type {
    START,
    SLEEP,
    WAKE
  }

  private data class Entry(val instant: Instant, val guard: Int, val type: Type)

  private val entries: List<Entry> = parseEntries(input)

  private val guardMap: Map<Int, List<Entry>> = entries.groupBy { it.guard }

  private val sleepTimes: Map<Int, Int> = guardMap.map { it.key to guardTotalSleep(it.key) }.toMap()

  private fun guardTotalSleep(guard: Int): Int {
    val events = guardMap[guard]!!
    var answer = 0

    var lastSleep = -1

    events.forEach { event ->
      if (event.type == Type.SLEEP) {
        lastSleep = event.instant.atZone(ZoneOffset.UTC).minute
      } else if (event.type == Type.WAKE) {
        val wake = event.instant.atZone(ZoneOffset.UTC).minute
        val duration = wake - lastSleep
        answer += duration
      }
    }

    return answer
  }

  private fun guardSleepPerMinute(guard: Int): Map<Int, Int> {
    val events = guardMap[guard]!!
    val answer = mutableMapOf<Int, Int>()

    var lastSleep = -1

    events.forEach { event ->
      if (event.type == Type.SLEEP) {
        lastSleep = event.instant.atZone(ZoneOffset.UTC).minute
      } else if (event.type == Type.WAKE) {
        val wake = event.instant.atZone(ZoneOffset.UTC).minute
        val duration = wake - lastSleep

        for (i in 0 ..< duration) {
          answer.putIfAbsent(i + lastSleep, 0)
          answer[i + lastSleep] = answer[i + lastSleep]!! + 1
        }
      }
    }

    return answer
  }

  /**
   * Returns the product of multiplying the ID of the guard that sleeps the most by the minute
   * that the guard is asleep the most in.
   *
   * @return The product of multiplying the ID of the guard that sleeps the most by the minute
   * that the guard is asleep the most in.
   */
  fun guardChecksum(): Int {
    val lazyGuard = sleepTimes.maxBy { it.value }
    val lazyMinute = guardSleepPerMinute(lazyGuard.key).maxBy { it.value }

    return lazyGuard.key * lazyMinute.key
  }


  /**
   * Returns the product of multiplying the ID of the guard that sleeps more than any
   * other guard during any particular minute multiplied by that particular minute.
   *
   * @return The product of multiplying the ID of the guard that sleeps more than any
   * other guard during any particular minute multiplied by that particular minute.
   */
  fun guardAlternateChecksum(): Int {
    val temp = mutableListOf<Triple<Int, Int, Int>>()

    sleepTimes.filter { it.value > 0 }.keys.forEach {guard ->
      val lazyMinute = guardSleepPerMinute(guard).maxBy { it.value }
      temp.add(Triple(guard, lazyMinute.key, lazyMinute.value))
    }

    val mostLazy = temp.maxBy { it.third }

    return mostLazy.first * mostLazy.second
  }
}
