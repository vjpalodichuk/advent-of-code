plugins {
    id("capital7software.library-conventions")
}

version = "1.0.0"

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaExec>().configureEach {
    dependsOn(":assemble")
    jvmArgs = listOf("-Xss4m")
    group = "aoc-library"
    description = "Advent of Code Library"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "com.capital7software.aoc.lib.AdventOfCodeRunner"
}

tasks.register("run", JavaExec::class)
