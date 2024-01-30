plugins {
    id("capital7software.library-conventions")
}

version = rootProject.version

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
