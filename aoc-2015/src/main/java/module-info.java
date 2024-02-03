/**
 * The Advent of Code 2015 module for sharing solutions among various puzzles.
 *
 */
module com.capital7software.aoc.aoc2015aoc {
    requires java.base;
    requires org.jetbrains.annotations;
    requires com.github.spotbugs.annotations;
    requires org.slf4j;
    requires com.capital7software.aoc.lib;

    exports com.capital7software.aoc.aoc2015aoc;
    exports com.capital7software.aoc.aoc2015aoc.days;
}