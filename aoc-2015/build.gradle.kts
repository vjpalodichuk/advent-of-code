plugins {
    java
}

group = "com.capital7software.aoc"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaExec>().configureEach {
    dependsOn(":assemble")
    jvmArgs = listOf("-Xss4m")
    group = "aoc-2015"
    description = "Advent of Code 2015 Puzzles"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "com.capital7software.aoc2015.lib.AdventOfCodeRunner"
}

tasks.register("run", JavaExec::class)
