/**
 * The Advent of Code 2023 module for sharing solutions among various puzzles.
 */
module com.capital7software.aoc.aoc2023aoc {
  requires java.logging;
  requires java.base;
  requires org.jetbrains.annotations;
  requires com.github.spotbugs.annotations;
  requires com.capital7software.aoc.lib;
  requires org.apache.logging.log4j;
  requires org.apache.logging.log4j.core;
  requires org.apache.logging.log4j.slf4j2.impl;

  exports com.capital7software.aoc.aoc2023aoc;
  exports com.capital7software.aoc.aoc2023aoc.days;
}