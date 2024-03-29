/**
 * The Advent of Code 2015 module for sharing solutions among various puzzles.
 */
module com.capital7software.aoc.aoc2016aoc {
  requires java.base;
  requires org.jetbrains.annotations;
  requires com.github.spotbugs.annotations;
  requires org.apache.logging.log4j;
  requires org.apache.logging.log4j.core;
  requires org.apache.logging.log4j.slf4j2.impl;
  requires static lombok;
  requires com.capital7software.aoc.lib;
  requires kotlin.stdlib;

  exports com.capital7software.aoc.aoc2016aoc;
  exports com.capital7software.aoc.aoc2016aoc.days;
}