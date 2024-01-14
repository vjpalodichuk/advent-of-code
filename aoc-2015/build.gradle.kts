plugins {
    java
}

group = "com.capital7software.aoc"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:unchecked")
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
