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
    description = "Advent of Code 2023 Puzzles"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "com.capital7software.aoc.aoc2023.AdventOfCode2023Runner"
}

tasks.register("run2023", JavaExec::class)

tasks.register("day21", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day21"
}
tasks.register("day22", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day22"
}
tasks.register("day23", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day23"
}
tasks.register("day24", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day24"
}
tasks.register("day25", JavaExec::class) {
    mainClass = "com.capital7software.aoc.aoc2023.days.Day25"
}
