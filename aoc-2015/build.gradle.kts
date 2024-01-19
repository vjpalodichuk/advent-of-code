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
    mainClass = "com.capital7software.aoc.aoc2015.AdventOfCode2015Runner"
}

tasks.register("run2015", JavaExec::class)
