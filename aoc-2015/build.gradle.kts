plugins {
    id("capital7software.library-conventions")
}

version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":aoc-library"))
}

tasks.withType<JavaExec>().configureEach {
    dependsOn(":assemble")
    jvmArgs = listOf("-Xss4m")
    group = "aoc-2015"
    description = "Advent of Code 2015 Puzzles"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "com.capital7software.aoc.lib.AdventOfCodeRunner"
}

tasks.register("run", JavaExec::class)
