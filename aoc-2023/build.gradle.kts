plugins {
    id("capital7software.library-conventions")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":aoc-library"))
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs = listOf("-Xss4m")
    group = "aoc"
    description = "Advent of Code 2015 Puzzles"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "com.capital7software.aoc.aoc2023.AdventOfCode2015Runner"
}

tasks.register("run2023", JavaExec::class)

tasks.register("day06", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day06"
}
tasks.register("day07", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day07"
}
tasks.register("day08", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day08"
}
tasks.register("day09", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day09"
}
tasks.register("day10", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day10"
}
tasks.register("day11", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day11"
}
tasks.register("day12", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day12"
}
tasks.register("day13", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day13"
}
tasks.register("day14", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day14"
}
tasks.register("day15", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day15"
}
tasks.register("day16", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day16"
}
tasks.register("day17", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day17"
}
tasks.register("day18", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day18"
}
tasks.register("day19", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day19"
}
tasks.register("day20", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day20"
}
tasks.register("day21", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day21"
}
tasks.register("day22", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day22"
}
tasks.register("day23", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day23"
}
tasks.register("day24", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day24"
}
tasks.register("day25", JavaExec::class) {
    mainClass = "com.capital7software.aoc2023.days.Day25"
}
