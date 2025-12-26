plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

object Versions {
  const val JETBRAINS_ANNOTATIONS = "26.0.2"
  const val JUNIT = "5.11.4"
  const val ARTIFACTORY = "5+"
  const val SPOTBUGS = "6.4.8"
  const val KOTLIN = "2.3.0"
  const val DETEKT = "1.23.8"
  const val DOKKA = "2.1.0"
}

dependencies {
  implementation("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
  implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:${Versions.SPOTBUGS}")
  implementation("org.jfrog.buildinfo:build-info-extractor-gradle:${Versions.ARTIFACTORY}")
  implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${Versions.KOTLIN}")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.DETEKT}")
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:${Versions.DOKKA}")
  // Test dependencies
  testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

configurations.all {
  this.exclude(group = "ch.qos.logback")

  resolutionStrategy.dependencySubstitution {
    // Security Fixes!
    substitute(module("org.apache.commons:commons-lang3:3.1.4"))
        .using(module("org.apache.commons:commons-lang3:3.20.0"))
        .because("""https://github.com/vjpalodichuk/advent-of-code/security/dependabot/12""")
  }
}
